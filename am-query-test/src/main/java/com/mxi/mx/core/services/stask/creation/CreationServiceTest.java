
package com.mxi.mx.core.services.stask.creation;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.UserTransaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.misc.NoUserTransactionAdapter;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.validation.Messages;
import com.mxi.mx.common.validation.ValidationException;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefCdKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.plsql.StoredProcedureCall;
import com.mxi.mx.core.plsql.delegates.PartProcedures;
import com.mxi.mx.core.plsql.delegates.TaskProcedures;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.services.inventory.InventoryServiceFactory;
import com.mxi.mx.core.services.inventory.oper.InventoryOperationalInterface;
import com.mxi.mx.core.services.taskdefn.exception.InvalidClassModeException;


/**
 * This class tests the CreationService class.
 *
 * Note that this test class lives in am-query-test in order to leverage related am-query-test
 * builders.
 *
 * @author dsewell
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class CreationServiceTest {

   private static final BigDecimal MIN_FORECAST_RANGE = new BigDecimal( 30 );
   private static final BigDecimal REPEAT_INTERVAL = new BigDecimal( 10 );

   @Mock
   private InventoryOperationalInterface iMockOperationalService;
   @Mock
   private PartProcedures iMockPartProcedures;
   @Mock
   private TaskProcedures iMockTaskProcedures;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Just to init mocked items.
    *
    * @throws Exception
    */
   @Before
   public void setUp() throws Exception {
      MockitoAnnotations.initMocks( this );
   }


   /**
    * Tests that no alert is sent when a task is deleted
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testGenerateForecastedTasksTaskKey_TaskDeleted() throws Exception {
      this.mockSetUp();

      final TaskKey TASK = new TaskKey( 0, 0 );

      final UserTransaction lTx = mock( UserTransaction.class );

      when( iMockTaskProcedures.generateForecastedTasks( TASK, false ) )
            .thenThrow( new RuntimeException() );

      // try to generate forecasted tasks for a non-existent task
      new CreationService().generateForecastedTasks( lTx, TASK );

      verify( lTx ).rollback();
      verify( lTx, times( 2 ) ).begin();
      verify( lTx ).commit();

      this.mockTearDown();
   }


   /**
    * Ensure that a historic task cannot be created for a recurring reference document.
    *
    * @throws Exception
    *            an unexpected error
    */
   @Test
   public void testThatHistoricTaskCannotBeCreatedForRecurringRefDoc() throws Exception {

      // data setup
      TaskKey lParentTask = null;
      Date lCompletionDate = null;
      UsageSnapshot[] lCompletionUsage = null;
      RefStageReasonKey lReason = null;
      HumanResourceKey lAuthorizingHr = null;
      String lNote = null;
      TaskKey lFollowingTask = null;

      final InventoryKey lInventory = new InventoryBuilder().build();

      final TaskTaskKey lTaskTask =
            new TaskRevisionBuilder().withTaskClass( TaskRevisionBuilder.TASK_CLASS_REF )
                  .withTaskCode( RefTaskClassKey.REF_CLASS_MODE_CD ).isRecurring().build();

      final Messages lMessages = new Messages();

      final String lExpectedExceptionMsg =
            i18n.get( "core.msg.CANNOT_CREATE_HISTORIC_RECURRING_REF_DOC_message" );

      try {

         // execute logic
         new CreationService().createHistoric( lInventory, lTaskTask, lParentTask, lCompletionDate,
               lCompletionUsage, lReason, lAuthorizingHr, lNote, lFollowingTask, lMessages );
         fail( "Expected ValidationException" );
      } catch ( ValidationException e ) {

         // ensure correct validation exception
         if ( !e.getMessage().contains( lExpectedExceptionMsg ) ) {
            fail( "Expected ValidationException with message: " + lExpectedExceptionMsg );
         }
      }
   }


   /**
    * Tests that a JIC can be created as a subtask of a CORR task.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatJICsCanBeCreatedUnderCORRTasks() throws Exception {

      // data setup
      final TaskKey lTaskKey = new TaskKey( 1, 1 );
      HumanResourceKey lAuthorizingHr = null;
      Date lPreviousCompletionDt = null;
      boolean lTrigger = false;
      boolean lIgnoreApplicability = true;
      boolean lPreventByClassMode = true;

      this.mockSetUp();

      final TaskTaskKey lTaskTask =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.JIC ).build();

      final InventoryKey lInventory = new InventoryBuilder().build();

      TaskKey lParentTask = new TaskBuilder().withTaskClass( RefTaskClassKey.CORR )
            .onInventory( lInventory ).build();

      when( iMockTaskProcedures.createTaskFromDefinition( isNull( TaskKey.class ), eq( lInventory ),
            eq( lTaskTask ), isNull( HumanResourceKey.class ), isNull( Date.class ),
            isNull( RefCdKey.class ), isNull( String.class ), eq( false ), eq( true ),
            isNull( Date.class ), eq( false ) ) ).thenReturn( lTaskKey );

      // execute logic
      TaskKey lCreatedTask = new CreationService().createTaskFromDefinition( null, lInventory,
            lTaskTask, lAuthorizingHr, lParentTask, lTrigger, lIgnoreApplicability,
            lPreventByClassMode, lPreviousCompletionDt );

      verify( iMockTaskProcedures ).createTaskFromDefinition( isNull( TaskKey.class ),
            eq( lInventory ), eq( lTaskTask ), isNull( HumanResourceKey.class ),
            isNull( Date.class ), isNull( RefCdKey.class ), isNull( String.class ), eq( false ),
            eq( true ), isNull( Date.class ), eq( false ) );

      // validation
      assertEquals( "createTaskFromDefinition did not return expected TaskKey", lTaskKey,
            lCreatedTask );

      this.mockTearDown();
   }


   /**
    * Tests that an exception is thrown when we try to create a JIC as a subtask of a non-CORR task.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatJICsCannotBeCreatedUnderNonCORRTasks() throws Exception {

      // data setup
      HumanResourceKey lAuthorizingHr = null;
      Date lPreviousCompletionDt = null;
      boolean lTrigger = false;
      boolean lIgnoreApplicability = true;
      boolean lPreventByClassMode = true;

      final TaskTaskKey lTaskTask =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.JIC ).build();

      final InventoryKey lInventory = new InventoryBuilder().build();

      TaskKey lParentTask = new TaskBuilder().withTaskClass( RefTaskClassKey.REQ )
            .onInventory( lInventory ).build();

      try {

         // execute logic
         new CreationService().createTaskFromDefinition( null, lInventory, lTaskTask,
               lAuthorizingHr, lParentTask, lTrigger, lIgnoreApplicability, lPreventByClassMode,
               lPreviousCompletionDt );

         fail( "Expected InvalidClassModeException" );
      } catch ( InvalidClassModeException e ) {
         // pass
      }
   }


   /**
    * Test that the generateForecastedTasks method properly generates tasks. Scenario: No active
    * tasks exist. Expected Result: No forecasted tasks created.
    *
    * <ul>
    * <li>Create new Task with status 'COMPLETE'.</li>
    * <li>Call core logic</li>
    * <li>Ensure no 'FORECAST' tasks created</li>
    * </ul>
    *
    * @exception Exception
    *               if an error occurs.
    */
   @Test
   public void forecastTasksNotGeneratedWhenNoActiveTask() throws Exception {
      // Given an activated recurring requirement that is not based on any conditions
      // And the requirement is not manged by a maintenance program
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly = createAcftAssyWithAcftPart( lAircraftPart );
      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lAircraftPart );
         aAircraft.allowSynchronization();
      } );

      final TaskTaskKey lRequirementDefinition =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot( lAircraftRootConfigSlot );
               aRequirementDefinition.setOnCondition( false );
               aRequirementDefinition.setRecurring( true );
               aRequirementDefinition.setScheduledFromManufacturedDate();
               aRequirementDefinition.setMinimumForecastRange( MIN_FORECAST_RANGE );
               aRequirementDefinition.addRecurringSchedulingRule( DataTypeKey.CDY,
                     REPEAT_INTERVAL );
            } );

      TaskKey lTaskKey = Domain.createRequirement( aRequirement -> {
         aRequirement.setDefinition( lRequirementDefinition );
         aRequirement.setStatus( RefEventStatusKey.COMPLETE );
         aRequirement.setInventory( lAircraft );
      } );

      new CreationService().generateForecastedTasks( new NoUserTransactionAdapter() );

      // Check to see that the forecasted tasks were not created for the not applicable task
      org.junit.Assert.assertFalse(
            "The method generateForecastedTasks had errors: "
                  + "Forecasted tasks for not applicable task were created",
            isForecastedTasksCreated( lTaskKey ) );
   }


   /**
    * Test that the generateForecastedTasks method properly generates tasks. Scenario: An active
    * tasks exists without any forecasted tasks. Expected Result: Multiple forecasted tasks are
    * created.
    *
    * <ul>
    * <li>Create new Task with status 'ACTV'.</li>
    * <li>Call core logic</li>
    * <li>Ensure multiple 'FORECAST' tasks are created</li>
    * </ul>
    *
    * @exception Exception
    *               if an error occurs.
    */
   @Test
   public void forecastTasksGeneratedWhenNoneExist() throws Exception {
      // Given an activated recurring requirement that is not based on any conditions
      // And the requirement is not manged by a maintenance program
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly = createAcftAssyWithAcftPart( lAircraftPart );
      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lAircraftPart );
         aAircraft.allowSynchronization();
      } );

      final TaskTaskKey lRequirementDefinition =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot( lAircraftRootConfigSlot );
               aRequirementDefinition.setOnCondition( false );
               aRequirementDefinition.setRecurring( true );
               aRequirementDefinition.setScheduledFromManufacturedDate();
               aRequirementDefinition.setMinimumForecastRange( MIN_FORECAST_RANGE );
               aRequirementDefinition.addRecurringSchedulingRule( DataTypeKey.CDY,
                     REPEAT_INTERVAL );
            } );

      TaskKey lTaskKey = Domain.createRequirement( aRequirement -> {
         aRequirement.setDefinition( lRequirementDefinition );
         aRequirement.setStatus( RefEventStatusKey.ACTV );
         aRequirement.setInventory( lAircraft );
      } );

      new CreationService().generateForecastedTasks( new NoUserTransactionAdapter() );

      // poll database to see if job has run and created forecasted tasks
      org.junit.Assert.assertTrue(
            "The method generateForecastedTasks had errors: " + "Forecasted tasks were not created",
            isForecastedTasksCreated( lTaskKey ) );
   }


   /**
    * Returns all the ForecastedTasks for this TaskBean.
    *
    * @param aTaskKey
    *           Task used to check for forecasted tasks.
    *
    * @return The ForcastedTasks value.
    */
   private boolean isForecastedTasksCreated( TaskKey aTaskKey ) {
      // prepare database access
      DataSetArgument lDataSetArg = new DataSetArgument();

      lDataSetArg.add( "al_eventdbid", aTaskKey.getDbId() );
      lDataSetArg.add( "al_eventid", aTaskKey.getId() );

      // get forcasted tasks
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.task.SelectForcastedTasks", lDataSetArg );

      if ( lQs.next() ) {

         return true;
      }

      // return task array
      return false;
   }


   private AssemblyKey createAcftAssyWithAcftPart( final PartNoKey aAircraftPart ) {
      return Domain.createAircraftAssembly( aAcftAssy -> {
         aAcftAssy.setRootConfigurationSlot( aRootConfigSlot -> {
            aRootConfigSlot.addPartGroup( aPartGroup -> {
               aPartGroup.setInventoryClass( ACFT );
               aPartGroup.addPart( aAircraftPart );
            } );
         } );
      } );
   }


   /**
    *
    * This function is a trade-off between mock and real stuff. It is used whenever we want to mock
    * Stored Procedure calls.
    *
    * @throws Exception
    */
   private void mockSetUp() throws Exception {
      StoredProcedureCall lSpc = StoredProcedureCall.getInstance();
      lSpc.setDelegate( iMockTaskProcedures );
      lSpc.setDelegate( iMockPartProcedures );

      InventoryServiceFactory.setInstance( new InventoryServiceFactory() {

         @Override
         public InventoryOperationalInterface getOperationalService() {
            return iMockOperationalService;
         }
      } );
   }


   /**
    *
    * This function is a trade-off between mock and real stuff. It is used whenever we want to mock
    * Stored Procedure calls.
    *
    * @throws Exception
    */
   private void mockTearDown() throws Exception {
      InventoryServiceFactory.setInstance( null );
   }

}
