package com.mxi.mx.web.query.fault;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.utils.StringUtils;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.fault.MxFaultDetailsService;


/**
 * Unit tests for the com.mxi.mx.query.fault.GetCorrectiveTaskOfOriginatingFault query.
 */
public final class GetCorrectiveTaskOfOriginatingFaultTest {

   @Rule
   public DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule sFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static final String QUERY =
         "com.mxi.mx.web.query.fault.GetCorrectiveTaskOfOriginatingFault";


   /**
    *
    * <pre>
    *
    * Given - an aircraft assembly with an aircraft part AND
    *         an aircraft inventory based on this assembly AND
    *         a fault on this aircraft AND
    *         a completed corrective task associated with this fault
    * When  - a follow-on requirement is created from the fault and the query is executed
    * Then  - the corrective task associated to the fault from which the follow-on was created from is returned.
    *
    * </pre>
    *
    * @throws TriggerException
    * @throws MxException
    *
    */
   @Test
   public void itReturnsCorrectiveTaskUsingFollowOnTaskCreatedFromCompletionOfFault()
         throws MxException, TriggerException {
      // Given
      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addPartGroup( rootCSPartGroup -> {
               rootCSPartGroup.setInventoryClass( ACFT );
               rootCSPartGroup.addPart( aircraftPart );
            } );
         } );
      } );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPart );
      } );

      final ConfigSlotKey aircraftRootCS = Domain.readRootConfigurationSlot( aircraftAssemblyKey );

      final TaskTaskKey followOnRequirement =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( aircraftRootCS );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
            } );

      final TaskKey faultCorrTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftKey );
         correctiveTask.setStatus( RefEventStatusKey.COMPLETE );
      } );

      Domain.createFault( fault -> {
         fault.setInventory( aircraftKey );
         fault.setCorrectiveTask( faultCorrTaskKey );
      } );

      TaskKey createdFollowOnTask = new MxFaultDetailsService().createFollowOnTaskFromDefinition(
            faultCorrTaskKey, aircraftAssemblyKey, aircraftKey, followOnRequirement,
            Domain.createHumanResource() );

      // When
      QuerySet qs = executeQuery( createdFollowOnTask );

      // Then
      qs.first();
      assertThat( "The query should have only returned 1 row.", qs.getRowCount(), is( 1 ) );

      String correctiveTaskKey = qs.getString( "faultrel_corr_task_key" );
      assertThat( "A task key should have been returned.", StringUtils.isBlank( correctiveTaskKey ),
            is( false ) );
      assertThat( "The fault corrective task key should have been returned.",
            new TaskKey( correctiveTaskKey ), is( faultCorrTaskKey ) );

   }


   /**
    *
    * Execute the query using the provided task and return the resulting query set.
    *
    * @param aTask
    * @return list of corrective tasks
    */
   private QuerySet executeQuery( TaskKey aTask ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aTask, "aTaskDbId", "aTaskId" );

      return QuerySetFactory.getInstance().executeQuery( QUERY, lDataSetArgument );
   }

}
