
package com.mxi.mx.web.query.inventory;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.HumanResource;
import com.mxi.am.domain.Organization;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.InvalidSearchCriteriaException;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Ensures that <code>EditInventoryTasks</code> query works
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class EditInventoryTasksTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   private static final HumanResourceKey TEST_USER = new HumanResourceKey( 4650, 1 );
   private static final InventoryKey AIRCRAFT_KEY = new InventoryKey( 4650, 300785 );
   private static final InventoryKey SUB_ASSY_KEY = new InventoryKey( 4650, 325358 );
   private static final InventoryKey SYSTEM_KEY = new InventoryKey( 4650, 300884 );
   private static final InventoryKey TRACK_KEY = new InventoryKey( 4650, 300762 );
   private static final InventoryKey ASSY_SYS_KEY = new InventoryKey( 4650, 325360 );
   private static final InventoryKey ASSY_TRACK_KEY = new InventoryKey( 4650, 399205 );

   private static final TaskKey SYSTEM_TASK_KEY = new TaskKey( 1, 238684 );
   private static final TaskKey SYSTEM_WITH_ASSY_TASK_KEY = new TaskKey( 1, 238685 );
   private static final TaskKey SUB_ASSY_TASK_KEY = new TaskKey( 1, 238686 );
   private static final TaskKey ASSY_TRK_TASK_KEY = new TaskKey( 1, 238687 );
   private static final TaskKey ACFT_TASK_KEY = new TaskKey( 1, 238688 );
   private static final TaskKey TRK_TASK_KEY = new TaskKey( 1, 238689 );
   private static final TaskKey ASSY_SYS_TASK_KEY = new TaskKey( 1, 238690 );
   private static final TaskKey ASSY_ROOT_TASK_KEY = new TaskKey( 1, 238691 );
   private static final TaskKey SUB_SYS_TASK_KEY = new TaskKey( 1, 238692 );

   private final InventoryTask LT_SUB_ASSY_INV_TASK =
         new InventoryTask( SUB_ASSY_TASK_KEY.toValueString(), "1.LT", "ENG_REQ1 (ENG_REQ1)" );
   private final InventoryTask RT_SUB_ASSY_INV_TASK =
         new InventoryTask( SUB_ASSY_TASK_KEY.toValueString(), "2.RT", "ENG_REQ1 (ENG_REQ1)" );


   /**
    * Ensure all the optional string params for the where clause are handled and do not throw an
    * exception
    *
    * @throws InvalidSearchCriteriaException
    */
   @Test
   public void testQueryCanHandleOptionalWhereClauseParams() throws InvalidSearchCriteriaException {

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( AIRCRAFT_KEY );

      // add optional arguments to the mandatory [WHERE_CLAUSE]
      lArguments.addWhereLike( "rvw_inv_tasks.task_cd", "taskCd" );
      lArguments.addWhereLike( "rvw_inv_tasks.task_name", "taskName" );
      lArguments.addWhereLike( "rvw_inv_tasks.task_class_cd", "taskClass" );
      lArguments.addWhereLike( "rvw_inv_tasks.assmbl_bom_cd", "assmblBomCd" );
      lArguments.addWhereBoolean( "rvw_inv_tasks.on_condition_bool", true );
      lArguments.addWhereBoolean( "rvw_inv_tasks.review_receipt_bool", true );

      // ACT
      List<TaskKey> lTasks = getTaskKeys( AIRCRAFT_KEY, lArguments );

      // ASSERT
      Assert.assertNotNull( lTasks );
      Assert.assertTrue( "Retrieved Tasks list is empty", lTasks.isEmpty() );

   }


   /**
    *
    * We are ensuring that the proper tasks are being retrieved by this query when:
    *
    * <ol>
    * <li>the tasks are defined against sub-component root</li>
    * <li>the tasks is initialized against the aircraft</li>
    * <li>we are searching from the root of the aircraft</li>
    * </ol>
    *
    * @throws Exception
    */
   @Test
   public void testSearchFromAircraftRoot() throws Exception {

      InventoryKey lRootKey = AIRCRAFT_KEY;

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( lRootKey );
      lArguments.add( "aShowInitialized", false );

      // ACT
      List<TaskKey> lTasks = getTaskKeys( lRootKey, lArguments );

      // ASSERT
      List<TaskKey> lExpectedTasks =
            Arrays.asList( ACFT_TASK_KEY, SYSTEM_WITH_ASSY_TASK_KEY, SUB_ASSY_TASK_KEY,
                  SUB_ASSY_TASK_KEY, ASSY_TRK_TASK_KEY, ASSY_SYS_TASK_KEY, ASSY_ROOT_TASK_KEY,
                  ASSY_ROOT_TASK_KEY, SYSTEM_TASK_KEY, TRK_TASK_KEY, SUB_SYS_TASK_KEY );
      assertTasks( lExpectedTasks, lTasks );
   }


   /**
    * Tests to see if the expected number of results returns when searching from ACFT root for the
    * ACFT
    *
    * @throws Exception
    */
   @Test
   public void testSearchFromAcftRootForAcft() throws Exception {

      InventoryKey lOpenedFrom = AIRCRAFT_KEY;

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( lOpenedFrom );
      lArguments.add( "aShowInitialized", false );
      lArguments.addWhereLike( "rvw_inv_tasks.assmbl_bom_cd", "A320" );

      // ACT
      List<TaskKey> lTasks = getTaskKeys( lOpenedFrom, lArguments );

      // ASSERT
      List<TaskKey> lExpectedTasks = Arrays.asList( ACFT_TASK_KEY );
      assertTasks( lExpectedTasks, lTasks );
   }


   /**
    * Tests to see if the expected number of results returns when searching from ACFT root for the
    * System
    *
    * @throws Exception
    */
   @Test
   public void testSearchFromAcftRootForSystem() throws Exception {

      InventoryKey lOpenedFrom = AIRCRAFT_KEY;

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( lOpenedFrom );
      lArguments.add( "aShowInitialized", false );
      lArguments.addWhereLike( "rvw_inv_tasks.assmbl_bom_cd", "21-00-00" );

      // ACT
      List<TaskKey> lTasks = getTaskKeys( lOpenedFrom, lArguments );

      // ASSERT
      List<TaskKey> lExpectedTasks = Arrays.asList( SYSTEM_TASK_KEY );
      assertTasks( lExpectedTasks, lTasks );
   }


   /**
    * Tests to see if the expected number of results returns when searching from ACFT root for the
    * Track
    *
    * @throws Exception
    */
   @Test
   public void testSearchFromAcftRootForTrk() throws Exception {

      InventoryKey lOpenedFrom = AIRCRAFT_KEY;

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( lOpenedFrom );
      lArguments.add( "aShowInitialized", false );
      lArguments.addWhereLike( "rvw_inv_tasks.assmbl_bom_cd", "21-20-00" );

      // ACT
      List<TaskKey> lTasks = getTaskKeys( lOpenedFrom, lArguments );

      // ASSERT
      List<TaskKey> lExpectedTasks = Arrays.asList( TRK_TASK_KEY );
      assertTasks( lExpectedTasks, lTasks );
   }


   /**
    * Tests to see if the expected number of results returns when searching from ACFT root for the
    * Sub Assembly. In this case, we have two engines attached so we should find the same task for
    * both engines listed here.
    *
    * @throws Exception
    */
   @Test
   public void testSearchFromAcftRootForSubAssy() throws Exception {

      InventoryKey lOpenedFrom = AIRCRAFT_KEY;

      List<InventoryTask> lExpectedInvTasks = new ArrayList<EditInventoryTasksTest.InventoryTask>();
      lExpectedInvTasks.add( LT_SUB_ASSY_INV_TASK );
      lExpectedInvTasks.add( RT_SUB_ASSY_INV_TASK );

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( lOpenedFrom );
      lArguments.add( "aShowInitialized", false );
      lArguments.addWhereLike( "rvw_inv_tasks.assmbl_bom_cd", "CFM56-3C" );

      // ACT
      List<InventoryTask> lInvTaskDefns = getInventoryTasks( lOpenedFrom, lArguments );

      // ASSERT - Results are expected to be ordered.
      assertInvTasks( lExpectedInvTasks, lInvTaskDefns );

   }


   /**
    * Tests to see if the expected number of results returns when searching from SYSTEM
    *
    * @throws Exception
    */
   @Test
   public void testSearchFromSystem() throws Exception {

      InventoryKey lOpenedFrom = SYSTEM_KEY;

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( lOpenedFrom );
      lArguments.add( "aShowInitialized", false );

      // ACT
      List<TaskKey> lTasks = getTaskKeys( lOpenedFrom, lArguments );

      // ASSERT
      List<TaskKey> lExpectedTasks =
            Arrays.asList( SYSTEM_TASK_KEY, TRK_TASK_KEY, SUB_SYS_TASK_KEY );
      assertTasks( lExpectedTasks, lTasks );
   }


   /**
    * Tests to see if the expected number of results returns when searching from TRACK
    *
    * @throws Exception
    */
   @Test
   public void testSearchFromTrk() throws Exception {

      InventoryKey lOpenedFrom = TRACK_KEY;

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( lOpenedFrom );
      lArguments.add( "aShowInitialized", false );

      // ACT
      List<TaskKey> lTasks = getTaskKeys( lOpenedFrom, lArguments );

      // ASSERT
      List<TaskKey> lExpectedTasks = Arrays.asList( TRK_TASK_KEY );
      assertTasks( lExpectedTasks, lTasks );
   }


   /**
    *
    * We are ensuring that the proper tasks are being retrieved by this query when:
    *
    * <ol>
    * <li>the tasks are defined against sub-component root</li>
    * <li>we are searching from the root of the engine slot</li>
    * </ol>
    *
    * @throws Exception
    */
   @Test
   public void testSearchFromSubAssembly() throws Exception {

      InventoryKey lRootKey = SUB_ASSY_KEY;

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( lRootKey );
      lArguments.add( "aShowInitialized", false );

      // ACT
      List<TaskKey> lTasks = getTaskKeys( lRootKey, lArguments );

      // ASSERT
      List<TaskKey> lExpectedTasks = Arrays.asList( ASSY_SYS_TASK_KEY, SUB_ASSY_TASK_KEY,
            ASSY_TRK_TASK_KEY, ASSY_ROOT_TASK_KEY );
      assertTasks( lExpectedTasks, lTasks );

   }


   /**
    * Tests to see if the expected number of results returns when searching from SUB ASSEMBLY with
    * no sub components
    *
    * @throws Exception
    */
   @Test
   public void testSearchFromSubAssyNoSubcomponents() throws Exception {

      InventoryKey lOpenedFrom = SUB_ASSY_KEY;

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( lOpenedFrom );
      lArguments.add( "aShowInitialized", false );
      lArguments.add( "aShowSubInventory", false );

      // ACT
      List<TaskKey> lTasks = getTaskKeys( lOpenedFrom, lArguments );

      // ASSERT
      List<TaskKey> lExpectedTasks = Arrays.asList( ASSY_ROOT_TASK_KEY, SUB_ASSY_TASK_KEY );
      assertTasks( lExpectedTasks, lTasks );
   }


   /**
    * Tests to see if the expected number of results returns when searching from SUB ASSEMBLY for
    * SUB ASSEMBLY
    *
    * @throws Exception
    */
   @Test
   public void testSearchFromSubAssyForSubAssy() throws Exception {

      InventoryKey lOpenedFrom = SUB_ASSY_KEY;

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( lOpenedFrom );
      lArguments.add( "aShowInitialized", false );
      lArguments.addWhereLike( "rvw_inv_tasks.assmbl_bom_cd", "CFM56-3C" );

      // ACT
      List<TaskKey> lTasks = getTaskKeys( lOpenedFrom, lArguments );

      // ASSERT
      List<TaskKey> lExpectedTasks = Arrays.asList( SUB_ASSY_TASK_KEY );
      assertTasks( lExpectedTasks, lTasks );
   }


   /**
    * Tests to see if the expected number of results returns when searching from SUB ASSEMBLY for
    * SYSTEM
    *
    * @throws Exception
    */
   @Test
   public void testSearchFromSubAssyForSys() throws Exception {

      InventoryKey lOpenedFrom = SUB_ASSY_KEY;

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( lOpenedFrom );
      lArguments.add( "aShowInitialized", false );
      lArguments.addWhereLike( "rvw_inv_tasks.assmbl_bom_cd", "71-10-00" );

      // ACT
      List<TaskKey> lTasks = getTaskKeys( lOpenedFrom, lArguments );

      // ASSERT
      List<TaskKey> lExpectedTasks = Arrays.asList( ASSY_SYS_TASK_KEY );
      assertTasks( lExpectedTasks, lTasks );
   }


   /**
    * Tests to see if the expected number of results returns when searching from SUB ASSEMBLY for
    * ASSEMBLY TRACK
    *
    * @throws Exception
    */
   @Test
   public void testSearchFromSubAssyForTrk() throws Exception {

      InventoryKey lOpenedFrom = SUB_ASSY_KEY;

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( lOpenedFrom );
      lArguments.add( "aShowInitialized", false );
      lArguments.addWhereLike( "rvw_inv_tasks.assmbl_bom_cd", "71-00-02-3-3-005" );

      // ACT
      List<TaskKey> lTasks = getTaskKeys( lOpenedFrom, lArguments );

      // ASSERT
      List<TaskKey> lExpectedTasks = Arrays.asList( ASSY_TRK_TASK_KEY );
      assertTasks( lExpectedTasks, lTasks );
   }


   /**
    * Tests to see if the expected number of results returns when searching from SUB ASSEMBLY SYSTEM
    *
    * @throws Exception
    */
   @Test
   public void testSearchFromSubAssySystem() throws Exception {

      InventoryKey lOpenedFrom = ASSY_SYS_KEY;

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( lOpenedFrom );
      lArguments.add( "aShowInitialized", false );

      // ACT
      List<TaskKey> lTasks = getTaskKeys( lOpenedFrom, lArguments );

      // ASSERT
      List<TaskKey> lExpectedTasks = Arrays.asList( ASSY_SYS_TASK_KEY, ASSY_TRK_TASK_KEY );
      assertTasks( lExpectedTasks, lTasks );
   }


   /**
    * Tests to see if the expected number of results returns when searching from SUB ASSEMBLY for
    * ASSEMBLY TRACK
    *
    * @throws Exception
    */
   @Test
   public void testSearchFromSubAssyTrk() throws Exception {

      InventoryKey lOpenedFrom = ASSY_TRACK_KEY;

      // get default arguments
      DataSetArgument lArguments = getDefaultArguments( lOpenedFrom );
      lArguments.add( "aShowInitialized", false );

      // ACT
      List<TaskKey> lTasks = getTaskKeys( lOpenedFrom, lArguments );

      // ASSERT
      List<TaskKey> lExpectedTasks = Arrays.asList( ASSY_TRK_TASK_KEY );
      assertTasks( lExpectedTasks, lTasks );
   }


   /**
    * This test case is testing if the requirement definition with correct prevent manual
    * initialization is returned against the aircraft given an aircraft and a configuration slot
    * based requirement definition with its prevent manual initialization which sets to true.
    *
    * <pre>
    *    Given an aircraft assembly and an aircraft based on it
    *    And a configuration slot based requirement definition
    *    And its prevent manual initialization is true
    *    And the organization of the requirement definition
    *    When execute this query EditInventoryTasks.qrx
    *    Then verify the task definition with correct prevent manual initialization is returned against the aircraft
    * </pre>
    */
   @Test
   public void testConfigSlotBasedRequirementDefinitionWithPreventManualInitializationIsReturned() {
      // Given the organization
      final OrgKey lOrgkey = Domain
            .createOrganization( aOrganization -> aOrganization.setType( RefOrgTypeKey.OPERATOR ) );
      HumanResourceKey lHrKey = Domain.createHumanResource( aHr -> {
         aHr.setUser( Domain.createUser() );
         aHr.setOrganization( lOrgkey );
      } );

      // Given a assembly
      final PartNoKey lAircraftPart = createAircraftPart();
      final AssemblyKey lAircraftAssembly = createAcftAssyWithAcftPart( lAircraftPart );
      final ConfigSlotKey lAircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssembly );

      // Given a configuration slot based requirement definition with prevent manual
      // initialization which sets to true
      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
         aReqDefn.setTaskClass( RefTaskClassKey.REQ );
         aReqDefn.setOrganization( lOrgkey );
         aReqDefn.setPreventManualInitialization( true );
      } );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
      } );

      // When execute this query EditInventoryTasks.qrx
      final QuerySet lQs = executeQuery( lAircraft, lHrKey );

      // verify the task definition with correct prevent manual initialization is returned
      final int lExpectedRowCount = 1;
      final int lActualRowCount = lQs.getRowCount();
      assertEquals( "Unexpected Number of Rows returned", lExpectedRowCount, lActualRowCount );

      lQs.first();

      final TaskTaskKey lExpectedRequirementDefinitionKey = lReqDefinition;
      final TaskTaskKey lActualRequirementDefinitionKey =
            lQs.getKey( TaskTaskKey.class, "task_definition_key" );
      assertEquals( "Unexpected Task Definition key", lExpectedRequirementDefinitionKey,
            lActualRequirementDefinitionKey );

      final boolean lActualPreventManualInitialization =
            lQs.getBoolean( "prevent_manual_init_bool" );
      assertTrue( lActualPreventManualInitialization );
   }


   /**
    * This test case is testing if the adhoc task with prevent manual initialization having false is
    * returned given an adhoc task.
    *
    * <pre>
    *    Given an adhoc task
    *    When execute this query EditInventoryTasks.qrx
    *    Then verify the adhoc task  with prevent manual initialization having false is returned
    * </pre>
    */
   @Test
   public void testAdhocTaskWithPreventManualInitializationIsReturned() {

      // Given a assembly
      final PartNoKey lAircraftPart = createAircraftPart();
      final AssemblyKey lAircraftAssembly = createAcftAssyWithAcftPart( lAircraftPart );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
      } );

      final TaskKey lTask = Domain.createAdhocTask( aAdhocTask -> {
         aAdhocTask.setInventory( lAircraft );
         aAdhocTask.setStatus( RefEventStatusKey.ACTV );
      } );

      // When execute this query EditInventoryTasks.qrx
      final QuerySet lQs = executeQuery( lAircraft, null );

      // verify the adhoc task is returned
      final int lExpectedRowCount = 1;
      final int lActualRowCount = lQs.getRowCount();
      assertEquals( "Unexpected Number of Rows returned", lExpectedRowCount, lActualRowCount );

      lQs.first();

      final TaskKey lExpectedTaskKey = lTask;
      final TaskKey lActualTaskKey = lQs.getKey( TaskKey.class, "task_key" );

      assertEquals( "Unexpected Task key", lExpectedTaskKey, lActualTaskKey );

      final boolean lActualPreventManualInitialization =
            lQs.getBoolean( "prevent_manual_init_bool" );
      assertFalse( lActualPreventManualInitialization );
   }


   /**
    * <pre>
    * Given an aircraft assembly and an aircraft based on it
    * And an on-condition requirement definition exists against the aircraft assembly
    * And a requirement based on the requirement definition exists against the aircraft
    *
    * The query returns the on-condition requirement that exists against the aircraft
    *
    * </pre>
    *
    */
   @Test
   public void itReturnsOnConditionRequirementAgainstTheAircraft() throws Exception {

      // Query requires task definition to be associated with an organization and have an
      // organization type
      final OrgKey lOrgkey = Domain.createOrganization( new DomainConfiguration<Organization>() {

         @Override
         public void configure( Organization aBuilder ) {
            aBuilder.setType( RefOrgTypeKey.MRO );
         }
      } );

      final UserKey lUser = Domain.createUser();
      final HumanResourceKey lHrKey =
            Domain.createHumanResource( new DomainConfiguration<HumanResource>() {

               @Override
               public void configure( HumanResource aBuilder ) {
                  aBuilder.setOrganization( lOrgkey );
                  aBuilder.setUser( lUser );
               }
            } );
      int lUserId = OrgHr.findByPrimaryKey( lHrKey ).getUserId();
      UserParameters.setInstance( lUserId, "LOGIC", new UserParametersFake( lUserId, "LOGIC" ) );

      // ARRANGE
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAircraftAssembly );
         }
      } );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aBuilder ) {
                  aBuilder.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aBuilder.setOnCondition( true );
                  aBuilder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aBuilder.setOrganization( lOrgkey );
               }
            } );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {
            aBuilder.setStatus( RefEventStatusKey.ACTV );
            aBuilder.setDefinition( lReqDefn );
            aBuilder.setInventory( lAircraft );
         }
      } );

      DataSetArgument lArguments = new DataSetArgument();
      lArguments.add( lHrKey, "aHrDbId", "aHrId" );
      lArguments.add( lAircraft, "aInvNoDbId", "aInvNoId" );
      // these arguments are required by the query to exhibit the behavior under test
      lArguments.add( "aShowSubInventory", true );
      lArguments.add( "aShowInitialized", true );
      lArguments.add( "aShowNonApplicable", false );
      lArguments.addWhereBoolean( "rvw_inv_tasks.on_condition_bool", true );
      lArguments.addWhere( "DISABLE_ADHOC_TASK_QUERY", "1 = 0" );
      lArguments.addWhere( "SHOW_TASK_CLASS_CLAUSE",
            "ref_task_class.class_mode_cd IN ( 'BLOCK', 'REQ', 'REF' )" );
      lArguments.addWhere( "ROWNUM_WHERE_CLAUSE", "ROWNUM = 1" );
      // ACT

      // Copy data from vw_h_baseline_task to the global temporary table
      MxDataAccess.getInstance().execute(
            "com.mxi.mx.core.query.bsync.initialize.MaterializeHighestBaselineTaskView",
            lArguments );

      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArguments );
      // ASSERT
      Assert.assertEquals( "Unexpected number of tasks returned", 1, lDs.getRowCount() );
      lDs.first();
      String lTaskDefnKey = lDs.getString( "task_definition_key" );
      String lTaskKey = lDs.getString( "task_key" );
      TaskTaskKey lActualTaskDefn = new TaskTaskKey( lTaskDefnKey );
      TaskKey lActualTask = new TaskKey( lTaskKey );
      Assert.assertEquals( "Unexpected task definition returned", lReqDefn, lActualTaskDefn );
      Assert.assertEquals( "Unexpected task returned", lReq, lActualTask );

   }


   /**
    * Provides a {@link DataSetArgument} with the default values
    *
    * @return the {@link DataSetArgument} arguments
    */
   private DataSetArgument getDefaultArguments( InventoryKey aInventoryKey ) {

      DataSetArgument lArguments = new DataSetArgument();

      lArguments.add( TEST_USER, "aHrDbId", "aHrId" );

      lArguments.add( aInventoryKey, "aInvNoDbId", "aInvNoId" );

      // these are the defaults that are currently managed via the EditInventoryTasksController
      // class.
      lArguments.add( "aShowSubInventory", true );
      lArguments.add( "aShowInitialized", false );
      lArguments.add( "aShowNonApplicable", false );

      // these where's are mandatory but have a default value
      lArguments.addWhere( "DISABLE_ADHOC_TASK_QUERY", "1 = 1" );
      lArguments.addWhere( "SHOW_TASK_CLASS_CLAUSE",
            "ref_task_class.class_mode_cd IN ( 'BLOCK', 'REQ' )" );
      lArguments.addWhere( "ROWNUM_WHERE_CLAUSE", "ROWNUM <= 101" );

      return lArguments;
   }


   /**
    * Compares to see if the expected tasks is equal to the actual tasks
    *
    * @param aExpectedTasks
    * @param aActualTasks
    */
   private void assertTasks( List<TaskKey> aExpectedTasks, List<TaskKey> aActualTasks ) {
      Assert.assertNotNull( aActualTasks );
      Assert.assertTrue(
            "Failed to retrieve the correct number of tasks. Retrieved " + aActualTasks
                  + " expected " + aExpectedTasks + ".",
            aExpectedTasks.size() == aActualTasks.size() );
      Assert.assertTrue( "Failed to retrieve the correct tasks. Retrieved " + aActualTasks
            + " expected " + aExpectedTasks + ".", aActualTasks.containsAll( aExpectedTasks ) );
   }


   /**
    * Compares to see if the expected {@link InventoryTask}'s is equal to the actual
    * {@link InventoryTask}'s. Equals means the list has the same number of elements and the same
    * {@link InventoryTask}'s.
    *
    * @param aExpectedInvTasks
    * @param aActualInvTasks
    */
   private void assertInvTasks( List<InventoryTask> aExpectedInvTasks,
         List<InventoryTask> aActualInvTasks ) {
      Assert.assertNotNull( aActualInvTasks );
      Assert.assertTrue(
            "Failed to retrieve the correct number of tasks. Retrieved " + aActualInvTasks
                  + " expected " + aExpectedInvTasks + ".",
            aExpectedInvTasks.size() == aActualInvTasks.size() );
      Assert.assertTrue( "Failed to retrieve the correct tasks. Retrieved " + aActualInvTasks
            + " expected " + aExpectedInvTasks + ".",
            aActualInvTasks.containsAll( aExpectedInvTasks ) );
   }


   /**
    * Returns a {@link List} of {@link TaskKey} for specified inventory
    *
    * @param aInvKey
    *           the Inventory Key
    * @param aArgs
    *           the query {@link DataSetArgument}
    *
    * @return {@link List} of {@link TaskKey}
    */
   private List<TaskKey> getTaskKeys( InventoryKey aInvKey, DataSetArgument lArgs ) {

      List<TaskKey> lList = new ArrayList<TaskKey>();

      // Copy data from vw_h_baseline_task to the global temporary table
      MxDataAccess.getInstance().execute(
            "com.mxi.mx.core.query.bsync.initialize.MaterializeHighestBaselineTaskView", lArgs );

      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // this is a collection of task keys
      while ( lDs.next() ) {
         String lTaskKey = lDs.getString( "task_definition_key" );

         lList.add( new TaskKey( lTaskKey ) );
      }

      return lList;
   }


   private QuerySet executeQuery( InventoryKey aInventoryKey, HumanResourceKey aHrKey ) {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aHrKey, "aHrDbId", "aHrId" );

      lArgs.add( aInventoryKey, "aInvNoDbId", "aInvNoId" );

      // these are the defaults that are currently managed.
      lArgs.add( "aShowSubInventory", true );
      lArgs.add( "aShowInitialized", false );
      lArgs.add( "aShowNonApplicable", false );

      // these where's are mandatory but have a default value
      lArgs.addWhere( "DISABLE_ADHOC_TASK_QUERY", "1 = 1" );
      lArgs.addWhere( "SHOW_TASK_CLASS_CLAUSE", "1 = 1" );
      lArgs.addWhere( "ROWNUM_WHERE_CLAUSE", "1 = 1" );

      // Copy data from vw_h_baseline_task to the global temporary table
      MxDataAccess.getInstance().execute(
            "com.mxi.mx.core.query.bsync.initialize.MaterializeHighestBaselineTaskView", lArgs );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   private AssemblyKey createAcftAssyWithAcftPart( final PartNoKey aAircraftPart ) {
      return Domain.createAircraftAssembly( aAcftAssy -> {
         aAcftAssy.setRootConfigurationSlot( aRootConfigSlot -> {
            aRootConfigSlot.addPartGroup( aRootCsPartGroup -> {
               aRootCsPartGroup.setInventoryClass( ACFT );
               aRootCsPartGroup.addPart( aAircraftPart );
            } );
         } );
      } );
   }


   private PartNoKey createAircraftPart() {
      return Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.ACFT );
      } );
   }


   /**
    * Returns a collection of tasks for inventory specified
    *
    * @param aInvKey
    *           an {@link InventoryKey}
    * @param aArgs
    *           the query {@link DataSetArgument}
    *
    * @return {@link List} of {@link InventoryTask}
    */
   private List<InventoryTask> getInventoryTasks( InventoryKey aInvKey, DataSetArgument aArgs ) {

      aArgs.add( "aInvNoDbId", aInvKey.getDbId() );
      aArgs.add( "aInvNoId", aInvKey.getId() );

      // Copy data from vw_h_baseline_task to the global temporary table
      MxDataAccess.getInstance().execute(
            "com.mxi.mx.core.query.bsync.initialize.MaterializeHighestBaselineTaskView", aArgs );

      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), aArgs );

      List<InventoryTask> lList = new ArrayList<InventoryTask>();

      // this is a collection of Inventory Tasks
      while ( lDs.next() ) {
         String lTaskKey = lDs.getString( "task_definition_key" );
         String lPosCode = lDs.getString( "eqp_pos_cd" );
         String lTaskDefnName = lDs.getString( "task_definition_name" );

         lList.add( new InventoryTask( lTaskKey, lPosCode, lTaskDefnName ) );
      }

      return lList;
   }


   protected final class InventoryTask {

      /**
       * {@inheritDoc}
       */
      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + getOuterType().hashCode();
         result = prime * result + ( ( iPosCode == null ) ? 0 : iPosCode.hashCode() );
         result = prime * result + ( ( iTaskDefnName == null ) ? 0 : iTaskDefnName.hashCode() );
         result = prime * result + ( ( iTaskKey == null ) ? 0 : iTaskKey.hashCode() );
         return result;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public boolean equals( Object obj ) {
         if ( this == obj )
            return true;
         if ( obj == null )
            return false;
         if ( getClass() != obj.getClass() )
            return false;
         InventoryTask other = ( InventoryTask ) obj;
         if ( !getOuterType().equals( other.getOuterType() ) )
            return false;
         if ( iPosCode == null ) {
            if ( other.iPosCode != null )
               return false;
         } else if ( !iPosCode.equals( other.iPosCode ) )
            return false;
         if ( iTaskDefnName == null ) {
            if ( other.iTaskDefnName != null )
               return false;
         } else if ( !iTaskDefnName.equals( other.iTaskDefnName ) )
            return false;
         if ( iTaskKey == null ) {
            if ( other.iTaskKey != null )
               return false;
         } else if ( !iTaskKey.equals( other.iTaskKey ) )
            return false;
         return true;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public String toString() {
         return this.getClass().getSimpleName() + " [iTaskKey=" + iTaskKey + ", iPosCode="
               + iPosCode + ", TaskDefn=" + iTaskDefnName + "]";
      }


      private String iTaskKey = "";
      private String iPosCode = "";
      private String iTaskDefnName = "";


      public InventoryTask(String aTaskKey, String aPosCode, String aTaskDefnName) {
         iTaskKey = aTaskKey;
         iPosCode = aPosCode;
         iTaskDefnName = aTaskDefnName;
      }


      public String getTaskKey() {
         return iTaskKey;
      }


      public String getPosCode() {
         return iPosCode;
      }


      public String getTaskDefnName() {
         return iTaskDefnName;
      }


      private EditInventoryTasksTest getOuterType() {
         return EditInventoryTasksTest.this;
      }

   }
}
