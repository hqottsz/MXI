package com.mxi.mx.web.query.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.PartRequirement;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.PartRequirementBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.CapabilityLevelKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.eqp.SystemSensitivityDao;
import com.mxi.mx.core.table.eqp.SystemSensitivityTable;
import com.mxi.mx.core.table.eqp.partgroup.PartGroupSensitivityTable;
import com.mxi.mx.core.table.inv.AcftCapLevelsTable;
import com.mxi.mx.web.dao.workscope.JdbcWorkScopeDaoImpl;
import com.mxi.mx.web.dao.workscope.WorkscopeDao;


public class WorkscopeSensitivitiesTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   // Capability Levels
   private static final CapabilityLevelKey ETOPS_YES =
         new CapabilityLevelKey( "4650:YES:4650:ETOPSCAP" );
   private static final CapabilityLevelKey ETOPS_NO =
         new CapabilityLevelKey( "4650:NO:4650:ETOPSCAP" );
   private static final CapabilityLevelKey TEST_CAP_YES =
         new CapabilityLevelKey( "4650:YES:4650:TESTCAP" );
   private static final CapabilityLevelKey TEST_CAP_NO =
         new CapabilityLevelKey( "4650:NO:4650:TESTCAP" );

   // Baseline for Aircraft, System and Part Group
   private static final ConfigSlotKey SYSTEM_CONFIG_SLOT = new ConfigSlotKey( "4650:ASSY:1" );
   private static final PartGroupKey PART_GROUP = new PartGroupKey( "4650:1" );
   private static final PartGroupKey PART_GROUP_2 = new PartGroupKey( "4650:2" );
   private static final PartGroupKey PART_GROUP_3_WITHOUT_SENSITIVITIES =
         new PartGroupKey( "4650:3" );
   private static final PartGroupKey PART_GROUP_4_WITHOUT_SENSITIVITIES =
         new PartGroupKey( "4650:4" );
   private static final PartGroupKey PART_GROUP_5_WITHOUT_SENSITIVITIES =
         new PartGroupKey( "4650:5" );

   // Actuals for Aircraft, System and Tracked Component
   private static final InventoryKey AIRCRAFT = new InventoryKey( "4650:1" );
   private static final InventoryKey SYSTEM_INVENTORY = new InventoryKey( "4650:2" );

   // Sensitivities
   private static final RefSensitivityKey UNASSIGNED_SENSITIVITY = new RefSensitivityKey( "TEST1" );
   private static final RefSensitivityKey INACTIVE_ASSIGNED_SENSITIVITY =
         new RefSensitivityKey( "TEST2" );
   private static final RefSensitivityKey ACTIVE_ASSIGNED_SENSITIVITY_WITH_CAP =
         new RefSensitivityKey( "TEST3" );
   private static final RefSensitivityKey ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP =
         new RefSensitivityKey( "TEST4" );
   private static final RefSensitivityKey ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP_2 =
         new RefSensitivityKey( "TEST5" );

   // Object under test
   private WorkscopeDao iWorkscopeDao;


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), WorkscopeSensitivitiesTest.class );
      iWorkscopeDao = new JdbcWorkScopeDaoImpl();
   }


   @Test
   public void hideSensitivities_cancelledTasks() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.CANCEL ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertNoSensitivities( lDs );
   }


   @Test
   public void hideSensitivities_unassignedTasks() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.UNASSIGN ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertNoSensitivities( lDs );
   }


   @Test
   public void hideSensitivities_terminatedTasks() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );

      TaskKey lTaskKey =
            new TaskBuilder().onInventory( SYSTEM_INVENTORY ).withTaskClass( RefTaskClassKey.CORR )
                  .withStatus( RefEventStatusKey.TERMINATE ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertNoSensitivities( lDs );
   }


   @Test
   public void hideSensitivities_errorTasks() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ERROR ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertNoSensitivities( lDs );
   }


   @Test
   public void hideSensitivities_notApplicableTasks() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.NA ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertNoSensitivities( lDs );
   }


   @Test
   public void hideSensitivities_delayTasks() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.DELAY ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertNoSensitivities( lDs );
   }


   @Test
   public void showSensitivities_activeTasks() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertHasSensitivities( lDs, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );
   }


   @Test
   public void showSensitivities_inWorkTasks() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.IN_WORK ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertHasSensitivities( lDs, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );
   }


   @Test
   public void showSensitivities_pausedTasks() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.PAUSE ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertHasSensitivities( lDs, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );
   }


   @Test
   public void showSensitivities_completeTasks() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.COMPLETE ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertHasSensitivities( lDs, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );
   }


   @Test
   public void hideSensitivities_globallyInactive_assignedToAssemblyOnSystemAndPartGroup() {
      setSystemSensitivity( INACTIVE_ASSIGNED_SENSITIVITY );
      setPartGroupSensitivity( PART_GROUP, INACTIVE_ASSIGNED_SENSITIVITY );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertNoSensitivities( lDs );
   }


   @Test
   public void hideSensitivities_notAssignedToAssemblyOnSystemAndPartGroup() {
      setSystemSensitivity( UNASSIGNED_SENSITIVITY );
      setPartGroupSensitivity( PART_GROUP, UNASSIGNED_SENSITIVITY );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertNoSensitivities( lDs );
   }


   @Test
   public void showSensitivities_assignedAndEnabledOnPartGroup() {
      setPartGroupSensitivity( PART_GROUP, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      addPartRequirementToTask( lTaskKey, PART_GROUP );
      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertHasSensitivities( lDs, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );
   }


   @Test
   public void showSensitivities_assignedAndEnabledOnSystem() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertHasSensitivities( lDs, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );
   }


   @Test
   public void showSensitivities_assignedAndMultipleEnabledOnSystem() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITH_CAP,
            ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );
      setAircraftCapabilityLevel( TEST_CAP_YES );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertHasSensitivities( lDs, ACTIVE_ASSIGNED_SENSITIVITY_WITH_CAP,
            ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );

   }


   @Test
   public void showSensitivities_assignedAndMulitpleEnabledOnPartGroup() {
      setPartGroupSensitivity( PART_GROUP, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP,
            ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP_2 );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      addPartRequirementToTask( lTaskKey, PART_GROUP );
      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertHasSensitivities( lDs, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP,
            ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP_2 );

   }


   @Test
   public void
         showSensitivities_assignedAndMulitplePartRequirements_WithPartGroupSensitivitiesEnabled() {
      setPartGroupSensitivity( PART_GROUP, ACTIVE_ASSIGNED_SENSITIVITY_WITH_CAP,
            ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );
      setPartGroupSensitivity( PART_GROUP_2, ACTIVE_ASSIGNED_SENSITIVITY_WITH_CAP,
            ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP_2 );
      setAircraftCapabilityLevel( TEST_CAP_YES );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      addPartRequirementToTask( lTaskKey, PART_GROUP );
      addPartRequirementToTask( lTaskKey, PART_GROUP_2 );
      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertHasSensitivities( lDs, ACTIVE_ASSIGNED_SENSITIVITY_WITH_CAP,
            ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP_2 );

   }


   @Test
   public void showSensitivities_etopsSignificantTask_assignedAndEnabledOnSystemAndPartGroup() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );
      setPartGroupSensitivity( PART_GROUP, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP_2 );
      setAircraftCapabilityLevel( ETOPS_YES );

      TaskKey lTaskKey =
            new TaskBuilder().onInventory( SYSTEM_INVENTORY ).withTaskClass( RefTaskClassKey.CORR )
                  .withEtopsSignificant( true ).withStatus( RefEventStatusKey.ACTV ).build();

      addPartRequirementToTask( lTaskKey, PART_GROUP );
      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertHasSensitivities( lDs, RefSensitivityKey.ETOPS, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP,
            ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP_2 );

   }


   @Test
   public void showSensitivities_enabledOnSystem_withAcConfiguredCapability() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITH_CAP );
      setAircraftCapabilityLevel( TEST_CAP_YES );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertHasSensitivities( lDs, ACTIVE_ASSIGNED_SENSITIVITY_WITH_CAP );
   }


   @Test
   public void hideSensitivities_enabledOnSystem_withoutAcConfiguredCapability() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITH_CAP );
      setAircraftCapabilityLevel( TEST_CAP_NO );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertNoSensitivities( lDs );
   }


   @Test
   public void showSensitivities_enabledOnPartGroup_withAcConfiguredCapability() {
      setPartGroupSensitivity( PART_GROUP, ACTIVE_ASSIGNED_SENSITIVITY_WITH_CAP );
      setAircraftCapabilityLevel( TEST_CAP_YES );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      addPartRequirementToTask( lTaskKey, PART_GROUP );
      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertHasSensitivities( lDs, ACTIVE_ASSIGNED_SENSITIVITY_WITH_CAP );
   }


   @Test
   public void hideSensitivities_enabledOnPartGroup_withoutAcConfiguredCapability() {
      setPartGroupSensitivity( PART_GROUP, ACTIVE_ASSIGNED_SENSITIVITY_WITH_CAP );
      setAircraftCapabilityLevel( TEST_CAP_NO );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertNoSensitivities( lDs );
   }


   @Test
   public void showSensitivities_etopsSignificantTask_withAcConfiguredCapability() {
      setAircraftCapabilityLevel( ETOPS_YES );

      TaskKey lTaskKey =
            new TaskBuilder().onInventory( SYSTEM_INVENTORY ).withTaskClass( RefTaskClassKey.ADHOC )
                  .withStatus( RefEventStatusKey.ACTV ).withEtopsSignificant( true ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackage = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackage ) );
      assertHasSensitivities( lDs, RefSensitivityKey.ETOPS );
   }


   @Test
   public void
         showSensitivities_etopsSignificantTask_withMultiplePartRequests_disabledOnPartGroups() {

      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP,
            ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP_2 );

      TaskKey lTaskKey =
            new TaskBuilder().onInventory( SYSTEM_INVENTORY ).withTaskClass( RefTaskClassKey.CORR )
                  .withStatus( RefEventStatusKey.ACTV ).withEtopsSignificant( true ).build();

      addPartRequirementToTask( lTaskKey, PART_GROUP_3_WITHOUT_SENSITIVITIES );
      addPartRequirementToTask( lTaskKey, PART_GROUP_4_WITHOUT_SENSITIVITIES );
      addPartRequirementToTask( lTaskKey, PART_GROUP_5_WITHOUT_SENSITIVITIES );
      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackage = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackage ) );
      assertHasSensitivities( lDs, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP,
            ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP_2 );
   }


   @Test
   public void showSensitivities_withMultiplePartRequests_disabledOnPartGroups() {

      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP,
            ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP_2 );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      addPartRequirementToTask( lTaskKey, PART_GROUP_3_WITHOUT_SENSITIVITIES );
      addPartRequirementToTask( lTaskKey, PART_GROUP_4_WITHOUT_SENSITIVITIES );
      addPartRequirementToTask( lTaskKey, PART_GROUP_5_WITHOUT_SENSITIVITIES );
      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackage = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackage ) );
      assertHasSensitivities( lDs, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP,
            ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP_2 );
   }


   @Test
   public void hideSensitivities_etopsSignificantTask_withoutAcConfiguredCapability() {
      setAircraftCapabilityLevel( ETOPS_NO );

      TaskKey lTaskKey =
            new TaskBuilder().onInventory( SYSTEM_INVENTORY ).withTaskClass( RefTaskClassKey.ADHOC )
                  .withStatus( RefEventStatusKey.ACTV ).withEtopsSignificant( true ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackage = addTaskToWorkPackage( lTaskKey, true );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackage ) );
      assertNoSensitivities( lDs );
   }


   @Test
   public void hideSensitivities_enabledOnSystemAndPartGroup_notInWorkscope() {
      setSystemSensitivity( ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );
      setPartGroupSensitivity( PART_GROUP, ACTIVE_ASSIGNED_SENSITIVITY_WITHOUT_CAP );

      TaskKey lTaskKey = new TaskBuilder().onInventory( SYSTEM_INVENTORY )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      createFaultWithCorrectiveTask( lTaskKey );
      TaskKey lWorkPackageKey = addTaskToWorkPackage( lTaskKey, false );

      DataSet lDs = iWorkscopeDao.getWorkScopeDataSet( buildArgs( lWorkPackageKey ) );
      assertEquals( 0, lDs.getTotalRowCount() );
   }


   private void assertNoSensitivities( DataSet lDs ) {
      if ( !lDs.next() ) {
         fail( "Expected a row in the workscope dataset but did not find one when asserting sensitivities." );
      }

      assertNull( lDs.getString( "sensitivity_cds" ) );
   }


   private void assertHasSensitivities( DataSet lDs, RefSensitivityKey... aSensitivities ) {
      assertFalse(
            "Cannot assert that the data set has sensitivities because the list provided is empty.",
            aSensitivities == null || aSensitivities.length == 0 );
      assertTrue(
            "Expected a row in the workscope dataset but did not find one when asserting sensitivities.",
            lDs.next() );

      String lSensitivityCodes = lDs.getString( "sensitivity_cds" );
      assertNotNull( String.format( "No sensitivity codes were found but expected: %s",
            Arrays.toString( aSensitivities ) ), lSensitivityCodes );

      String[] lSensitivities = lSensitivityCodes.split( "," );
      List<RefSensitivityKey> lActualSensitivities = new ArrayList<>();

      for ( String lSensitivitity : lSensitivities ) {
         lActualSensitivities.add( new RefSensitivityKey( lSensitivitity.trim() ) );
      }

      List<RefSensitivityKey> lExpectedSensitivities = Arrays.asList( aSensitivities );
      assertEquals( lExpectedSensitivities, lActualSensitivities );
   }


   private DataSetArgument buildArgs( TaskKey aWorkPackage ) {
      DataSetArgument lArgs = new DataSetArgument();
      {
         lArgs.add( "aCheckDbId", aWorkPackage.getDbId() );
         lArgs.add( "aCheckId", aWorkPackage.getId() );
         lArgs.add( "aIsWorkTypeApplicable", 0 );
         lArgs.add( "aSubInvDbId", -999999 );
         lArgs.add( "aSubInvId", -999999 );
         lArgs.add( "aUnassignString", "UNASSIGN" );
         lArgs.addIntegerArray( "aZoneDbIdArray", Collections.<Integer>emptyList() );
         lArgs.addIntegerArray( "aZoneIdArray", Collections.<Integer>emptyList() );
         lArgs.add( "aIsZoneFilterApplied", 0 );
         lArgs.addWhere( "WHERE_WORK_PACKAGE", "1 = 1" );
      }
      return lArgs;
   }


   private void setAircraftCapabilityLevel( CapabilityLevelKey aCapabilityLevel ) {
      DataSetArgument lArgs = new DataSetArgument();
      {
         lArgs.add( AIRCRAFT, AcftCapLevelsTable.ColumnName.ACFT_NO_DB_ID.name(),
               AcftCapLevelsTable.ColumnName.ACFT_NO_ID.name() );
         lArgs.add( AcftCapLevelsTable.ColumnName.CONFIG_LEVEL_DB_ID.name(),
               aCapabilityLevel.getDbId() );
         lArgs.add( AcftCapLevelsTable.ColumnName.CONFIG_LEVEL_CD.name(),
               aCapabilityLevel.getCd() );
         lArgs.add( AcftCapLevelsTable.ColumnName.CAP_DB_ID.name(), aCapabilityLevel.getCapDbId() );
         lArgs.add( AcftCapLevelsTable.ColumnName.CAP_CD.name(), aCapabilityLevel.getCapCd() );
      }
      MxDataAccess.getInstance().executeInsert( AcftCapLevelsTable.TABLE_NAME, lArgs );
   }


   private void setSystemSensitivity( RefSensitivityKey... aSensitivities ) {
      for ( RefSensitivityKey aSensitivity : aSensitivities ) {
         DataSetArgument lArgs = new DataSetArgument();
         {
            lArgs.add( SYSTEM_CONFIG_SLOT, SystemSensitivityDao.ColumnName.ASSMBL_DB_ID.name(),
                  SystemSensitivityDao.ColumnName.ASSMBL_CD.name(),
                  SystemSensitivityDao.ColumnName.ASSMBL_BOM_ID.name() );
            lArgs.add( aSensitivity, SystemSensitivityDao.ColumnName.SENSITIVITY_CD.name() );
         }
         MxDataAccess.getInstance().executeInsert( SystemSensitivityTable.TABLE_NAME, lArgs );
      }

   }


   private void setPartGroupSensitivity( PartGroupKey aPartGroup,
         RefSensitivityKey... aSensitivities ) {
      for ( RefSensitivityKey aSensitivity : aSensitivities ) {
         DataSetArgument lArgs = new DataSetArgument();
         {
            lArgs.add( aPartGroup, PartGroupSensitivityTable.ColumnName.BOM_PART_DB_ID.name(),
                  PartGroupSensitivityTable.ColumnName.BOM_PART_ID.name() );
            lArgs.add( aSensitivity, PartGroupSensitivityTable.ColumnName.SENSITIVITY_CD.name() );
         }
         MxDataAccess.getInstance().executeInsert( PartGroupSensitivityTable.TABLE_NAME, lArgs );
      }

   }


   private FaultKey createFaultWithCorrectiveTask( final TaskKey aTask ) {
      return Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setCorrectiveTask( aTask );
            aFault.setFailedSystem( SYSTEM_CONFIG_SLOT );
            aFault.setInventory( SYSTEM_INVENTORY );
         }
      } );
   }


   private void addPartRequirementToTask( final TaskKey aTask, PartGroupKey aPartGroupKey ) {
      PartRequirement lPartRequirement = new PartRequirement();
      lPartRequirement.setPartGroup( aPartGroupKey );
      lPartRequirement.setTaskKey( aTask );
      PartRequirementBuilder.build( lPartRequirement );
   }


   private TaskKey addTaskToWorkPackage( final TaskKey aTask, final boolean aCommitToWorkscope ) {
      TaskKey lWorkPackage = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( AIRCRAFT );
            aWorkPackage.addTask( aTask );
            if ( aCommitToWorkscope ) {
               aWorkPackage.addWorkScopeTask( aTask );
            }
         }
      } );

      return lWorkPackage;
   }
}
