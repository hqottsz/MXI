package com.mxi.mx.core.query.plsql.axoneventpublishpkg;

import static com.mxi.am.db.AxonDomainEventDao.PAYLOAD_COLUMN;
import static com.mxi.am.db.AxonDomainEventDao.PAYLOAD_TYPE_COLUMN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.db.AxonDomainEventDao;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.production.task.domain.TaskAssignedToWorkPackageEvent;
import com.mxi.mx.core.production.task.domain.TaskCreatedEvent;
import com.mxi.mx.core.production.task.domain.TaskUnassignedFromWorkPackageEvent;


/**
 * This is a test class for testing the procedure axon_event_publish_pkg.publish_task_created_event
 * in the database
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class AxonEventPublishTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule fakeInjectionOverrideRule = new InjectionOverrideRule();

   private final ObjectMapper mapper = new ObjectMapper();

   private static final AxonDomainEventDao AXON_DOMAIN_EVENT_DAO = new AxonDomainEventDao();

   private static final double DEFAULT_FC_MODEL_RATE = 2.252363464d;
   private static final String USER_NOTE = "te\"st" + StringUtils.repeat( "A", 5000 );
   private static final java.util.Date NOW_DATE =
         DateUtils.truncate( new java.util.Date(), Calendar.SECOND );
   private static final float MAX_RANGE_QT = 5.0f;
   private static final TaskKey PREVIOUS_TASK = new TaskKey( 9999, 223344 );
   private static final RefStageReasonKey REASON = RefStageReasonKey.NEW;
   private static final HumanResourceKey HR = HumanResourceKey.ADMIN;
   private static final InventoryKey AIRCRAFT_KEY = new InventoryKey( "1:1234" );
   private static final TaskTaskKey REQ_DEFINITION_KEY = new TaskTaskKey( "1:1234" );
   private static final TaskKey TASK_KEY = new TaskKey( "1:1234" );
   private static final TaskKey WK_PKG_KEY = new TaskKey( "1:2234" );


   @Test
   public void publishNewlyCreatedTaskEvent() throws Exception {
      // ARRANGE
      final PartNoKey aircraftPartKey = Domain.createPart();
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( configurationSlot -> {
            configurationSlot.addPartGroup( partGroup -> {
               partGroup.setInventoryClass( RefInvClassKey.ACFT );
               partGroup.addPart( aircraftPartKey );
            } );
         } );
      } );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
         aircraft.setForecastModel( Domain.createForecastModel( forecastModel -> forecastModel
               .addRange( 1, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE ) ) );
         aircraft.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      final ConfigSlotKey aircraftRootConfigSlotKey = new ConfigSlotKey( aircraftAssemblyKey, 0 );

      final TaskTaskKey reqDefinitionKey = Domain.createRequirementDefinition( reqDefinition -> {
         reqDefinition.againstConfigurationSlot( aircraftRootConfigSlotKey );
         reqDefinition.setRecurring( false );
         reqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefinition.setOnCondition( true );
         reqDefinition.setScheduledFromManufacturedDate();
         reqDefinition.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      final TaskKey taskKey = Domain.createRequirement( requirement -> {
         requirement.setDefinition( reqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );
         requirement.setInventory( aircraftKey );
         requirement.addUsageDeadline( DataTypeKey.HOURS, BigDecimal.TEN, null );
      } );

      int result = execute( taskKey, aircraftKey, reqDefinitionKey );

      assertEquals( "Unexpected result of the procedure execution.", 1, result );

      final QuerySet querySet = AXON_DOMAIN_EVENT_DAO.findAll();
      assertEquals( "Unexpected number of events.", 1, querySet.getRowCount() );

      // validate the data is deserialized correctly.
      querySet.next();
      String payload = querySet.getString( PAYLOAD_COLUMN );
      TaskCreatedEvent event = ( TaskCreatedEvent ) mapper.readValue( payload,
            Class.forName( querySet.getString( PAYLOAD_TYPE_COLUMN ) ) );

      assertEquals( "Incorrect deserializtion of task key", taskKey,
            event.getNewlyCreatedTaskKey() );
      assertEquals( "Incorrect deserializtion of task definition key", reqDefinitionKey,
            event.getTaskTaskKey() );
      assertEquals( "Incorrect deserializtion of aircraft key", aircraftKey,
            event.getInventoryKey() );
      assertEquals( "Incorrect deserializtion of previous task key", PREVIOUS_TASK,
            event.getPreviousTaskKey() );
      assertEquals( "Incorrect deserializtion of completion date", NOW_DATE,
            event.getPreviousCompletionDate() );
      assertEquals( "Incorrect deserializtion of reason key", REASON, event.getReasonKey() );
      assertEquals( "Incorrect deserializtion of user note", USER_NOTE, event.getUserNote() );
      assertEquals( "Incorrect deserializtion of hr key", HR, event.getHumanResourceKey() );
      assertFalse( "Incorrect deserializtion of isCalledExternally", event.isCalledExternally() );
      assertFalse( "Incorrect deserializtion of isHistoric", event.isHistoric() );
      assertFalse( "Incorrect deserializtion of isCreatedNATask", event.isCreatedNATask() );
      assertEquals( "Incorrect deserializtion of forecasting range quantity", MAX_RANGE_QT,
            event.getForecastRangeQuantity(), 0 );
      assertTrue( "Incorrect deserializtion of isDeadlineExisting", event.isDeadlineExisting() );
   }


   @Test
   public void doNotPublishAxonEventWhenEnableSpecifiedConfigParm() throws Exception {
      // ARRANGE

      CallableStatement lPrepareCallPublishEvent;
      lPrepareCallPublishEvent = iDatabaseConnectionRule.getConnection().prepareCall(
            "BEGIN   axon_event_publish_pkg.setConfigParmValue(as_enableAxonFrameworkFailSafe =>?); END;" );
      lPrepareCallPublishEvent.setString( 1, "TRUE" );

      lPrepareCallPublishEvent.execute();

      // ACT
      final int result = execute( TASK_KEY, AIRCRAFT_KEY, REQ_DEFINITION_KEY );

      // ASSERT
      assertEquals( "Unexpected result of the procedure execution.", 1, result );

      final QuerySet querySet = AXON_DOMAIN_EVENT_DAO.findAll();
      assertEquals( "Unexpected number of events.", 0, querySet.getRowCount() );
   }


   @Test
   public void publishAxonEventWhenDisableSpecifiedConfigParm() throws Exception {
      // ARRANGE

      CallableStatement lPrepareCallPublishEvent;
      lPrepareCallPublishEvent = iDatabaseConnectionRule.getConnection().prepareCall(
            "BEGIN   axon_event_publish_pkg.setConfigParmValue(as_enableAxonFrameworkFailSafe => ?); END;" );
      lPrepareCallPublishEvent.setString( 1, "FALSE" );

      lPrepareCallPublishEvent.execute();

      // ACT
      final int result = execute( TASK_KEY, AIRCRAFT_KEY, REQ_DEFINITION_KEY );

      // ASSERT
      assertEquals( "Unexpected result of the procedure execution.", 1, result );

      final QuerySet querySet = AXON_DOMAIN_EVENT_DAO.findAll();
      assertEquals( "Unexpected number of events.", 1, querySet.getRowCount() );
   }


   @Test
   public void publishAxonEventWhenSpecifiedConfigParmValueIsNull() throws Exception {
      // ARRANGE

      CallableStatement lPrepareCallPublishEvent;
      lPrepareCallPublishEvent = iDatabaseConnectionRule.getConnection().prepareCall(
            "BEGIN   axon_event_publish_pkg.setConfigParmValue(as_enableAxonFrameworkFailSafe =>?); END;" );
      lPrepareCallPublishEvent.setNull( 1, Types.NULL );

      lPrepareCallPublishEvent.execute();

      // ACT
      final int result = execute( TASK_KEY, AIRCRAFT_KEY, REQ_DEFINITION_KEY );

      // ASSERT
      assertEquals( "Unexpected result of the procedure execution.", 1, result );

      final QuerySet querySet = AXON_DOMAIN_EVENT_DAO.findAll();
      assertEquals( "Unexpected number of events.", 1, querySet.getRowCount() );
   }


   @Test
   public void publishTaskAssignToWorkPackageEventTest() throws Exception {
      // ARRANGE
      CallableStatement lPrepareCallPublishEvent;
      lPrepareCallPublishEvent = iDatabaseConnectionRule.getConnection()
            .prepareCall( "BEGIN   axon_event_publish_pkg.publish_tsk_assign_to_wk_evt("
                  + "an_WorkPackageTaskDbId =>?, an_WorkPackageTaskId => ?,"
                  + "an_TaskDbId =>?, an_TaskId => ?," + "an_HrDbId => ?, an_HrId => ?,"
                  + "as_Note => ?, an_ReasonDbId => ?, an_ReasonCd => ?,"
                  + "on_Return => ?); END;" );

      lPrepareCallPublishEvent.setInt( 1, WK_PKG_KEY.getDbId() );
      lPrepareCallPublishEvent.setInt( 2, WK_PKG_KEY.getId() );
      lPrepareCallPublishEvent.setInt( 3, TASK_KEY.getDbId() );
      lPrepareCallPublishEvent.setInt( 4, TASK_KEY.getId() );
      lPrepareCallPublishEvent.setInt( 5, HR.getDbId() );
      lPrepareCallPublishEvent.setInt( 6, HR.getId() );
      lPrepareCallPublishEvent.setString( 7, USER_NOTE );
      lPrepareCallPublishEvent.setInt( 8, REASON.getDbId() );
      lPrepareCallPublishEvent.setString( 9, REASON.getCd() );
      lPrepareCallPublishEvent.registerOutParameter( 10, Types.INTEGER );

      lPrepareCallPublishEvent.execute();

      // ACT
      final int result = lPrepareCallPublishEvent.getInt( 10 );

      // ASSERT
      assertEquals( "Unexpected result of the procedure execution.", 1, result );

      final QuerySet querySet = AXON_DOMAIN_EVENT_DAO.findAll();
      assertEquals( "Unexpected number of events.", 1, querySet.getRowCount() );

      // validate the data is deserialized correctly.
      querySet.next();
      String payload = querySet.getString( PAYLOAD_COLUMN );
      TaskAssignedToWorkPackageEvent event = ( TaskAssignedToWorkPackageEvent ) mapper
            .readValue( payload, Class.forName( querySet.getString( PAYLOAD_TYPE_COLUMN ) ) );

      assertEquals( "Incorrect deserializtion of work package key", WK_PKG_KEY,
            event.getWorkPackageKey() );
      assertEquals( "Incorrect deserializtion of task key", TASK_KEY, event.getTaskKey() );
      assertEquals( "Incorrect deserializtion of human resource key", HR,
            event.getHumanResourceKey() );
      assertEquals( "Incorrect deserializtion of reason key", REASON, event.getReasonKey() );
      assertEquals( "Incorrect deserializtion of note", USER_NOTE, event.getNote() );
   }


   @Test
   public void publishTaskUnassignFromWorkPackageEventTest() throws Exception {
      // ARRANGE
      CallableStatement lPrepareCallPublishEvent;
      lPrepareCallPublishEvent = iDatabaseConnectionRule.getConnection()
            .prepareCall( "BEGIN   axon_event_publish_pkg.publish_tsk_unassgn_frm_wk_evt("
                  + "an_WorkPackageTaskDbId =>?, an_WorkPackageTaskId => ?,"
                  + "an_TaskDbId =>?, an_TaskId => ?," + "an_HrDbId => ?, an_HrId => ?,"
                  + "on_Return => ?); END;" );

      lPrepareCallPublishEvent.setInt( 1, WK_PKG_KEY.getDbId() );
      lPrepareCallPublishEvent.setInt( 2, WK_PKG_KEY.getId() );
      lPrepareCallPublishEvent.setInt( 3, TASK_KEY.getDbId() );
      lPrepareCallPublishEvent.setInt( 4, TASK_KEY.getId() );
      lPrepareCallPublishEvent.setInt( 5, HR.getDbId() );
      lPrepareCallPublishEvent.setInt( 6, HR.getId() );
      lPrepareCallPublishEvent.registerOutParameter( 7, Types.INTEGER );

      lPrepareCallPublishEvent.execute();

      // ACT
      final int result = lPrepareCallPublishEvent.getInt( 7 );

      // ASSERT
      assertEquals( "Unexpected result of the procedure execution.", 1, result );

      final QuerySet querySet = AXON_DOMAIN_EVENT_DAO.findAll();
      assertEquals( "Unexpected number of events.", 1, querySet.getRowCount() );

      // validate the data is deserialized correctly.
      querySet.next();
      String payload = querySet.getString( PAYLOAD_COLUMN );
      TaskUnassignedFromWorkPackageEvent event = ( TaskUnassignedFromWorkPackageEvent ) mapper
            .readValue( payload, Class.forName( querySet.getString( PAYLOAD_TYPE_COLUMN ) ) );

      assertEquals( "Incorrect deserializtion of work package key", WK_PKG_KEY,
            event.getWorkPackageKey() );
      assertEquals( "Incorrect deserializtion of task key", TASK_KEY, event.getTaskKey() );
      assertEquals( "Incorrect deserializtion of human resource key", HR,
            event.getHumanResourceKey() );
   }


   /**
    * call axon_event_publish_pkg.publish_task_created_event
    *
    * @param taskKey
    *           the Task identifier for the new task
    * @param inventoryKey
    *           the inventory identifier that the task will be created on.
    * @param taskTaskKey
    *           the task definition identifier the task is against.
    *
    * @return the result of the procedure execution
    *
    */
   protected int execute( TaskKey taskKey, InventoryKey inventoryKey, TaskTaskKey taskTaskKey )
         throws Exception {

      CallableStatement lPrepareCallPublishEvent;

      lPrepareCallPublishEvent = iDatabaseConnectionRule.getConnection().prepareCall(
            "BEGIN   axon_event_publish_pkg.publish_task_created_event(an_SchedDbId => ?,"
                  + "an_SchedId =>?, an_OrigTaskTaskDbId => ?,"
                  + "an_OrigTaskTaskId =>?, an_MaxRangeQt => ?,"
                  + "an_InvNoDbId => ?, an_InvNoId => ?,"
                  + "an_PreviousTaskDbId => ?, an_PreviousTaskId => ?, ad_PreviousCompletionDt => ?,"
                  + "an_ReasonDbId => ?, an_ReasonCd => ?," + "as_UserNote => ?, an_HrDbId => ?,"
                  + "an_HrId => ?, ab_CalledExternally => false,"
                  + "ab_Historic => false, ab_CreateNATask => false," + "on_Return => ?); END;" );

      lPrepareCallPublishEvent.setInt( 1, taskKey.getDbId() );
      lPrepareCallPublishEvent.setInt( 2, taskKey.getId() );
      lPrepareCallPublishEvent.setInt( 3, taskTaskKey.getDbId() );
      lPrepareCallPublishEvent.setInt( 4, taskTaskKey.getId() );
      lPrepareCallPublishEvent.setFloat( 5, MAX_RANGE_QT );
      lPrepareCallPublishEvent.setInt( 6, inventoryKey.getDbId() );
      lPrepareCallPublishEvent.setInt( 7, inventoryKey.getId() );
      lPrepareCallPublishEvent.setInt( 8, PREVIOUS_TASK.getDbId() );
      lPrepareCallPublishEvent.setInt( 9, PREVIOUS_TASK.getId() );
      lPrepareCallPublishEvent.setDate( 10, new java.sql.Date( NOW_DATE.getTime() ) );
      lPrepareCallPublishEvent.setInt( 11, REASON.getDbId() );
      lPrepareCallPublishEvent.setString( 12, REASON.getCd() );
      lPrepareCallPublishEvent.setString( 13, USER_NOTE );
      lPrepareCallPublishEvent.setInt( 14, HR.getDbId() );
      lPrepareCallPublishEvent.setInt( 15, HR.getId() );

      lPrepareCallPublishEvent.registerOutParameter( 16, Types.INTEGER );

      lPrepareCallPublishEvent.execute();

      return lPrepareCallPublishEvent.getInt( 16 );
   }

}
