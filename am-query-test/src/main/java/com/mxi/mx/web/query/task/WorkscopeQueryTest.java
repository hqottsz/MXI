package com.mxi.mx.web.query.task;

import static com.mxi.am.domain.Domain.createAdhocTask;
import static com.mxi.am.domain.Domain.createAircraft;
import static com.mxi.am.domain.Domain.createAircraftAssembly;
import static com.mxi.am.domain.Domain.createCorrectiveTask;
import static com.mxi.am.domain.Domain.createDeferralReference;
import static com.mxi.am.domain.Domain.createFault;
import static com.mxi.am.domain.Domain.createRequirementDefinition;
import static com.mxi.am.domain.Domain.createWorkPackage;
import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFailDeferKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Unit tests for the query com.mxi.mx.web.query.task.Workscope.qrx.
 *
 *
 * There exists another test class {@link WorkscopeTest} which uses an XML file to load test data
 * (shared by all test methods). However, the tests in this class use domain entities/builds to
 * better manage test data on a test by test basis. (Note, a test class cannot intermix the test
 * data loading techniques.)
 */
public class WorkscopeQueryTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private static final String EMPTY_STRING = null;
   private static final ArrayList<Integer> EMPTY_ARRAY = new ArrayList<Integer>();
   private static final boolean COLLECTED = true;
   private static final boolean NOT_COLLECTED = false;


   /**
    * Verify that all tasks assigned to the work scope of a work package are returned from the query
    * when no filters are provided.
    */
   @Test
   public void returnsAllTasksWhenNoFilters() throws Exception {

      // Given an aircraft for the tasks.
      // This inventory is needed by the query but is not related to this scenario.
      InventoryKey aircraft = createAircraft();

      // Given a work package with tasks both assigned to its work scope and not assigned
      // to its work scope.
      TaskKey task1 = createAdhocTask( task -> {
         task.setInventory( aircraft );
      } );
      TaskKey task2 = createAdhocTask( task -> {
         task.setInventory( aircraft );
      } );
      TaskKey task3 = createAdhocTask( task -> {
         task.setInventory( aircraft );
      } );
      TaskKey task4 = createAdhocTask( task -> {
         task.setInventory( aircraft );
      } );
      TaskKey workPackage = createWorkPackage( wp -> {
         wp.addTask( task1 );
         wp.addTask( task2 );
         wp.addTask( task3 );
         wp.addTask( task4 );
         wp.addWorkScopeTask( task1 );
         wp.addWorkScopeTask( task2 );
      } );

      // When the query is executed without filters.
      DataSetArgument args = getQueryArgsWithoutFilters();
      args.add( workPackage, "aCheckDbId", "aCheckId" );

      QuerySet qs = executeQuery( args );

      // Then only the tasks assigned to the work scope are returned.
      assertThat( "Unexpectd number of tasks returned.", qs.getRowCount(), is( 2 ) );

      List<TaskKey> actualTasks = new ArrayList<>();
      while ( qs.next() ) {
         actualTasks.add( qs.getKey( TaskKey.class, "event_key" ) );
      }
      assertThat( "Unexpected task returned.", actualTasks, containsInAnyOrder( task1, task2 ) );
   }


   /**
    * Verify that only tasks awaiting certification are returned from the query when the "only show
    * tasks awaiting certification" filter is on.
    *
    * Note: the actual logic for filtering based on awaiting certification is done outside this
    * query (i.e. within WorkscopeController.java). Unfortunately, at this time the controller
    * cannot be auto tested without refactoring. But since that logic is very small and unlikely to
    * change we will test it here (i.e. add the dynamic where clause).
    */
   @Test
   public void returnsTasksAwaitingCertificationWhenUsingFilter() {

      // Given an aircraft for the tasks.
      // This inventory is needed by the query but is not related to this scenario.
      InventoryKey aircraft = createAircraft();

      // Given a work package with tasks assigned to its work scope.
      // Some of those tasks require certification while other do not.
      TaskKey taskAwaitingCert1 = createAdhocTask( task -> {
         task.setInventory( aircraft );
         task.addLabour( labour -> {
            labour.setRequiresCertification( true );
         } );
      } );
      TaskKey taskAwaitingCert2 = createAdhocTask( task -> {
         task.setInventory( aircraft );
         task.addLabour( labour -> {
            labour.setRequiresCertification( true );
         } );
      } );
      TaskKey taskNotAwaitingCert1 = createAdhocTask( task -> {
         task.setInventory( aircraft );
         task.addLabour( labour -> {
            labour.setRequiresCertification( false );
         } );
      } );
      TaskKey taskNotAwaitingCert2 = createAdhocTask( task -> {
         task.setInventory( aircraft );
         task.addLabour( labour -> {
            labour.setRequiresCertification( false );
         } );
      } );

      TaskKey workPackage = createWorkPackage( wp -> {
         wp.addWorkScopeTask( taskAwaitingCert1 );
         wp.addWorkScopeTask( taskAwaitingCert2 );
         wp.addWorkScopeTask( taskNotAwaitingCert1 );
         wp.addWorkScopeTask( taskNotAwaitingCert2 );
      } );

      // When the query is executed with a filter to only show tasks awaiting certification.
      DataSetArgument args = getQueryArgsWithoutFilters();
      args.add( "aIsWorkTypeApplicable", true );
      args.addWhere( "sched_labour_role.labour_role_type_cd IN ( \'CERT\' )" );
      args.add( workPackage, "aCheckDbId", "aCheckId" );

      QuerySet qs = QueryExecutor.executeQuery( databaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.task.Workscope", args );

      // Then only the tasks assigned to the work scope that are awaiting certification are
      // returned.
      assertThat( "Unexpectd number of tasks returned.", qs.getRowCount(), is( 2 ) );
      List<TaskKey> actualTasks = new ArrayList<>();
      while ( qs.next() ) {
         actualTasks.add( qs.getKey( TaskKey.class, "event_key" ) );
      }
      assertThat( "Unexpected task returned.", actualTasks,
            containsInAnyOrder( taskAwaitingCert1, taskAwaitingCert2 ) );
   }


   /**
    * Verify that only tasks awaiting inspection are returned from the query when the "only show
    * tasks awaiting inspection" filter is on.
    *
    * Note: the actual logic for filtering based on awaiting inspection is done outside this query
    * (i.e. within WorkscopeController.java). Unfortunately, at this time the controller cannot be
    * auto tested without refactoring. But since that logic is very small and unlikely to change we
    * will test it here (i.e. add the dynamic where clause).
    */
   @Test
   public void returnsTasksAwaitingInspectionWhenUsingFilter() {

      // Given an aircraft for the tasks.
      // This inventory is needed by the query but is not related to this scenario.
      InventoryKey aircraft = createAircraft();

      // Given a work package with tasks assigned to its work scope.
      // Some of those tasks require inspection while other do not.
      TaskKey taskAwaitingInsp1 = createAdhocTask( task -> {
         task.setInventory( aircraft );
         task.addLabour( labour -> {
            labour.setRequiresInspection( true );
         } );
      } );
      TaskKey taskAwaitingInsp2 = createAdhocTask( task -> {
         task.setInventory( aircraft );
         task.addLabour( labour -> {
            labour.setRequiresInspection( true );
         } );
      } );
      TaskKey taskNotAwaitingInsp1 = createAdhocTask( task -> {
         task.setInventory( aircraft );
         task.addLabour( labour -> {
            labour.setRequiresInspection( false );
         } );
      } );
      TaskKey taskNotAwaitingIsnp2 = createAdhocTask( task -> {
         task.setInventory( aircraft );
         task.addLabour( labour -> {
            labour.setRequiresInspection( false );
         } );
      } );

      TaskKey workPackage = createWorkPackage( wp -> {
         wp.addWorkScopeTask( taskAwaitingInsp1 );
         wp.addWorkScopeTask( taskAwaitingInsp2 );
         wp.addWorkScopeTask( taskNotAwaitingInsp1 );
         wp.addWorkScopeTask( taskNotAwaitingIsnp2 );
      } );

      // When the query is executed with a filter to only show tasks awaiting inspection.
      DataSetArgument args = getQueryArgsWithoutFilters();
      args.add( "aIsWorkTypeApplicable", true );
      args.addWhere( "sched_labour_role.labour_role_type_cd IN ( \'INSP\' )" );
      args.add( workPackage, "aCheckDbId", "aCheckId" );

      QuerySet qs = QueryExecutor.executeQuery( databaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.task.Workscope", args );

      // Then only the tasks assigned to the work scope that are awaiting inspection are returned.
      assertThat( "Unexpectd number of tasks returned.", qs.getRowCount(), is( 2 ) );
      List<TaskKey> actualTasks = new ArrayList<>();
      while ( qs.next() ) {
         actualTasks.add( qs.getKey( TaskKey.class, "event_key" ) );
      }
      assertThat( "Unexpected task returned.", actualTasks,
            containsInAnyOrder( taskAwaitingInsp1, taskAwaitingInsp2 ) );
   }


   /**
    * Verify that all tasks assigned to the work scope of a work package are returned from the query
    * when no filters are provided and that any tasks that are being collected are marked as being
    * collected (and the others are not marked as being collected).
    */
   @Test
   public void returnsCollectedTasksThatAreMarkedAsBeingCollected() throws Exception {

      // Given an aircraft for the tasks.
      // This inventory is needed by the query but is not related to this scenario.
      InventoryKey aircraft = createAircraft();

      // Given a work package with tasks assigned to its work scope.
      // Some of those tasks are marked as being collected and some are not.
      TaskKey taskCollected1 = createAdhocTask( task -> {
         task.setInventory( aircraft );
      } );
      TaskKey taskCollected2 = createAdhocTask( task -> {
         task.setInventory( aircraft );
      } );
      TaskKey taskNotCollected1 = createAdhocTask( task -> {
         task.setInventory( aircraft );
      } );
      TaskKey taskNotCollected2 = createAdhocTask( task -> {
         task.setInventory( aircraft );
      } );
      TaskKey workPackage = createWorkPackage( wp -> {
         wp.addWorkScopeTask( taskCollected1, COLLECTED );
         wp.addWorkScopeTask( taskCollected2, COLLECTED );
         wp.addWorkScopeTask( taskNotCollected1, NOT_COLLECTED );
         wp.addWorkScopeTask( taskNotCollected2, NOT_COLLECTED );
      } );

      // When the query is executed without filters.
      DataSetArgument args = getQueryArgsWithoutFilters();
      args.add( workPackage, "aCheckDbId", "aCheckId" );

      QuerySet qs = executeQuery( args );

      // Then all the tasks assigned to the work scope are returned.
      assertThat( "Unexpectd number of tasks returned.", qs.getRowCount(), is( 4 ) );

      // Then the collected tasks are marked as being collected.
      Map<TaskKey, Boolean> taskCollectedMap = new HashMap<>();
      while ( qs.next() ) {
         taskCollectedMap.put( qs.getKey( TaskKey.class, "event_key" ),
               qs.getBoolean( "collected_bool" ) );
      }
      assertThat( "Expected task to be marked as collected; task=" + taskCollected1,
            taskCollectedMap.get( taskCollected1 ), is( true ) );
      assertThat( "Expected task to be marked as collected; task=" + taskCollected2,
            taskCollectedMap.get( taskCollected2 ), is( true ) );
      assertThat( "Expected task to be marked as not collected; task=" + taskNotCollected1,
            taskCollectedMap.get( taskNotCollected1 ), is( false ) );
      assertThat( "Expected task to be marked as not collected; task=" + taskNotCollected2,
            taskCollectedMap.get( taskNotCollected2 ), is( false ) );
   }


   /**
    * Verify that query returns reference request approval status for deferral reference and for
    * repair reference.
    *
    */
   @Test
   public void returnsFaultReferenceRequestStatus() throws Exception {

      final AssemblyKey faultAssembly = createAircraftAssembly( acftAssembly -> {
         acftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addPartGroup( rootCsPartGroup -> {
               rootCsPartGroup.setInventoryClass( ACFT );
            } );
         } );
      } );

      final InventoryKey lFaultAssemblyInventory = createAircraft( acft -> {
         acft.setAssembly( faultAssembly );
         acft.addSystem( "SYSTEM_NAME" );
      } );

      final InventoryKey lCorrectiveTaskInventory =
            new InventoryBuilder().withAssemblyInventory( lFaultAssemblyInventory ).build();

      FailDeferRefKey deferralReferenceKey = createDeferralReference( deferralReference -> {
         deferralReference.setFaultDeferralKey( new RefFailDeferKey( 0, "MEL A" ) );
         deferralReference.setName( "DEF-REF-1" );
         deferralReference.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         deferralReference.setAssemblyKey( faultAssembly );
         deferralReference.setStatus( "ACTV" );
         deferralReference.getFailedSystemInfo().setFailedSystemAltId( UUID.randomUUID() );
         deferralReference.getOperators().add( new CarrierKey( "1:1" ) );
      } );

      FaultKey faultKeyWithDeferralReference = createFault( fault -> {
         fault.setName( "Fault1" );
         fault.setCurrentDeferralReference( deferralReferenceKey );
         fault.setCreateReferenceRequest( true );
      } );

      TaskKey correctiveTaskForDeferralReference = createCorrectiveTask( task -> {
         task.setInventory( lCorrectiveTaskInventory );
         task.setFaultKey( faultKeyWithDeferralReference );
         task.setStatus( RefEventStatusKey.ACTV );
      } );

      TaskTaskKey repairReferenceKey = createRequirementDefinition( reqDefinition -> {
         reqDefinition.setTaskName( "repairReferenceOtherAssembly" );
         reqDefinition.setCode( "abc" );
         reqDefinition.setTaskClass( RefTaskClassKey.REPREF );
         reqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefinition.againstConfigurationSlot( new ConfigSlotKey( "1000:abc:1" ) );
         reqDefinition.setOpsRestrictionsDesc( "I am a restriction in repair reference." );
         reqDefinition.setMocApprovalBool( true );
      } );

      TaskKey correctiveTaskForRepairReference = createCorrectiveTask( task -> {
         task.setInventory( lFaultAssemblyInventory );
         task.setStatus( RefEventStatusKey.ACTV );
      } );

      FaultKey faultKeyWithRepairReference = createFault( fault -> {
         fault.setName( "Fault2" );
         fault.setCorrectiveTask( correctiveTaskForRepairReference );
         fault.setCurrentRepairReference( repairReferenceKey );
      } );

      TaskKey workPackage = createWorkPackage( wp -> {
         wp.addWorkScopeTask( correctiveTaskForDeferralReference, COLLECTED );
         wp.addWorkScopeTask( correctiveTaskForRepairReference, COLLECTED );
      } );

      // When the query is executed without filters.
      DataSetArgument args = getQueryArgsWithoutFilters();
      args.add( workPackage, "aCheckDbId", "aCheckId" );

      QuerySet qs = executeQuery( args );
      while ( qs.next() ) {
         String statusCode = qs.getString( "reference_request_status_cd" );
         assertThat( "Expected request approval status code is", statusCode, is( "PENDING" ) );
      }

   }


   private DataSetArgument getQueryArgsWithoutFilters() {
      DataSetArgument args = new DataSetArgument();
      // These are the filters that we are NOT setting.
      args.addIntegerArray( "aZoneDbIdArray", EMPTY_ARRAY );
      args.addIntegerArray( "aZoneIdArray", EMPTY_ARRAY );
      args.add( "aIsZoneFilterApplied", false );
      args.add( "aIsWorkTypeApplicable", false );
      // The query uses -999999 to not filter based on sub-inventory.
      args.add( "aSubInvDbId", -999999 );
      args.add( "aSubInvId", -999999 );
      // This aUnassignString is mandatory but not applicable to this test.
      args.add( "aUnassignString", EMPTY_STRING );
      // This WHERE_WORK_PACKAGE is mandatory but not applicable to this test.
      args.addWhere( "WHERE_WORK_PACKAGE", "1 = 1" );
      return args;
   }


   private QuerySet executeQuery( DataSetArgument args ) {
      return QueryExecutor.executeQuery( databaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.task.Workscope", args );
   }

}
