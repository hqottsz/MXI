package com.mxi.mx.core.services.bsync;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Block;
import com.mxi.am.domain.BlockDefinition;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.bsync.helpers.BaselineSyncHelper;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * Tests the behaviours of {@linkplain BaselineSyncService}
 *
 */
@RunWith( Theories.class )
public class BaselineSyncServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static InventoryKey iAircraft;
   private static InventoryKey iAircraftTrk;
   private static InventoryKey iEngine;
   private static InventoryKey iEngineTrk;
   private static InventoryKey iEngineTrkTrk;

   private TaskTaskKey iReqDefn;
   private TaskTaskKey iBlockDefn;
   private TaskTaskKey iAnotherReqDefn;

   private TaskKey iBlockTask;
   private TaskKey iReqTask;
   private TaskKey iAnotherReqTask;

   private TaskInventory iBlockInvDataPoint;
   private TaskInventory iReqInvDataPoint;
   private TaskInventory iAnotherReqInvDataPoint;

   private HumanResourceKey iAuthHr;
   private int iUserId;


   // Inventory against which the tasks will be created.
   private enum TaskInventory {
      AIRCRAFT, AIRCRAFT_TRK, ENGINE, ENGINE_TRK, ENGINE_TRK_TRK;
   }


   public static @DataPoints TaskInventory[] iTaskInventories = TaskInventory.values();


   /**
    * Verify that {@linkplain BaselineSyncService#requestZipTree(InventoryKey)} assigns REQ tasks to
    * BLOCK tasks for all inventory installed on the provided aircraft. This includes tasks against
    * inventory installed on sub-assemblies. Note that REQ tasks may only be assigned to BLOCK tasks
    * that are on the same (sub)assembly.
    *
    * <pre>
    * Given an inventory tree for an aircraft that includes installed inventory and installed sub-assemblies (with installed inventory)
    *   and a REQ defn assigned to a BLOCK defn
    *   and combinations of a BLOCK and REQ tasks that are assigned to the various inventory (tasks based on the defns)
    *  When the BLOCK task and REQ task are on the same assembly
    *   and zipping is requested for the full tree of the aircraft
    *   and the zip queue and zip tasks are processed
    *  Then the REQ task is assigned to the BLOCK task
    * </pre>
    *
    * Note: Theory data points are used to provide the various combinations of inventory against
    * which the REQ and BLOCK tasks are created.
    */
   @Theory
   public void assignReqToBlockForAircraft( TaskInventory aBlockInvDataPoint,
         TaskInventory aReqInvDataPoint ) throws Exception {

      // When the BLOCK task and REQ task are on the same assembly.
      assumeTrue( areAllTasksOnAircraft( aBlockInvDataPoint, aReqInvDataPoint )
            || areAllTasksOnEngine( aBlockInvDataPoint, aReqInvDataPoint ) );

      iBlockInvDataPoint = aBlockInvDataPoint;
      iReqInvDataPoint = aReqInvDataPoint;

      // Given an inventory tree for an aircraft that includes installed inventory and installed
      // sub-assemblies (with installed inventory).
      withInventoryTreeForAircraft();

      // Given a REQ defn assigned to a BLOCK defn.
      withReqDefnAssignedToBlockDefn();

      // Given combinations of a BLOCK and REQ tasks that are assigned to the various inventory
      // (tasks based on the defns).
      withBlockTask();
      withReqTask();
      ensureReqNotAssignedToBlock();

      // When zipping is requested for the full tree of the aircraft.
      int lZipId = BaselineSyncService.getInstance().requestZipTree( iAircraft );

      // When the zip queue and zip tasks are processed.
      BaselineSyncHelper.getInstance().performZipping( lZipId, iAuthHr );

      // Then the REQ task is assigned to the BLOCK task.
      assertReqIsAssignedToBlock();

   }


   /**
    * Verify that {@linkplain BaselineSyncService#requestZipTree(InventoryKey)} does not assign REQ
    * tasks to BLOCK tasks when those tasks are against different assemblies.
    *
    * <pre>
    * Given an inventory tree for an aircraft that includes installed inventory and installed sub-assemblies (with installed inventory)
    *   and a REQ defn assigned to a BLOCK defn
    *   and combinations of a BLOCK and REQ tasks that are assigned to the various inventory (tasks based on the defns)
    *  When the BLOCK task and REQ task are on different assemblies
    *   and zipping is requested for the full tree of the aircraft
    *   and the zip queue and zip tasks are processed
    *  Then the REQ task is not assigned to the BLOCK task
    * </pre>
    *
    * Note: Theory data points are used to provide the various combinations of inventory against
    * which the REQ and BLOCK tasks are created.
    */
   @Theory
   public void notAssignReqToBlockForAircraftWhenTasksOnDifferentAssemlbies(
         TaskInventory aBlockInvDataPoint, TaskInventory aReqInvDataPoint ) throws Exception {

      // When the BLOCK task and REQ task are on different assemblies.
      assumeTrue( !areAllTasksOnAircraft( aBlockInvDataPoint, aReqInvDataPoint )
            && !areAllTasksOnEngine( aBlockInvDataPoint, aReqInvDataPoint ) );

      iBlockInvDataPoint = aBlockInvDataPoint;
      iReqInvDataPoint = aReqInvDataPoint;

      // Given an inventory tree for an aircraft that includes installed inventory and installed
      // sub-assemblies (with installed inventory).
      withInventoryTreeForAircraft();

      // Given a REQ defn assigned to a BLOCK defn.
      withReqDefnAssignedToBlockDefn();

      // Given combinations of a BLOCK and REQ tasks that are assigned to the various inventory
      // (tasks based on the defns).
      withBlockTask();
      withReqTask();
      ensureReqNotAssignedToBlock();

      // When zipping is requested for the full tree of the aircraft.
      int lZipId = BaselineSyncService.getInstance().requestZipTree( iAircraft );

      // When the zip queue and zip tasks are processed.
      BaselineSyncHelper.getInstance().performZipping( lZipId, iAuthHr );

      // Then the REQ task is not assigned to the BLOCK task.
      assertReqIsNotAssignedToBlock();
   }


   /**
    * Verify that {@linkplain BaselineSyncService#requestZipTree(InventoryKey)} assigns REQ tasks to
    * BLOCK tasks for all inventory installed on the assembly of the provided inventory but
    * excluding sub-assemblies.
    *
    * <pre>
    * Given an inventory tree for an aircraft that includes installed inventory and installed sub-assemblies (with installed inventory)
    *   and a REQ defn assigned to a BLOCK defn
    *   and combinations of a BLOCK and REQ tasks that are assigned to the various inventory (tasks based on the defns)
    *  When the BLOCK task and REQ task are on the aircraft
    *   and zipping is requested for the full tree of an inventory attached to the aircraft
    *   and the zip queue and zip tasks are processed
    *  Then the REQ task is assigned to the BLOCK task
    * </pre>
    *
    * Note: Theory data points are used to provide the various combinations of inventory against
    * which the REQ and BLOCK tasks are created.
    */
   @Theory
   public void assignReqToBlockForTrkOnAircraft( TaskInventory aBlockInvDataPoint,
         TaskInventory aReqInvDataPoint ) throws Exception {

      // When the BLOCK task and REQ task are on the aircraft
      assumeTrue( areAllTasksOnAircraft( aBlockInvDataPoint, aReqInvDataPoint ) );

      iBlockInvDataPoint = aBlockInvDataPoint;
      iReqInvDataPoint = aReqInvDataPoint;

      // Given an inventory tree for an aircraft that includes installed inventory and installed
      // sub-assemblies (with installed inventory).
      withInventoryTreeForAircraft();

      // Given a REQ defn assigned to a BLOCK defn.
      withReqDefnAssignedToBlockDefn();

      // Given combinations of a BLOCK and REQ tasks that are assigned to the various inventory
      // (tasks based on the defns).
      withBlockTask();
      withReqTask();
      ensureReqNotAssignedToBlock();

      // When zipping is requested for the full tree of an inventory attached to the
      // aircraft.
      int lZipId = BaselineSyncService.getInstance().requestZipTree( iAircraftTrk );

      // When the zip queue and zip tasks are processed.
      BaselineSyncHelper.getInstance().performZipping( lZipId, iAuthHr );

      // Then the REQ task is assigned to the BLOCK task.
      assertReqIsAssignedToBlock();

   }


   /**
    * Verify that {@linkplain BaselineSyncService#requestZipTree(InventoryKey)} does not assign REQ
    * tasks to BLOCK tasks that are against sub-assemblies on the assembly of the provided
    * inventory.
    *
    * <pre>
    * Given an inventory tree for an aircraft that includes installed inventory and installed sub-assemblies (with installed inventory)
    *   and a REQ defn assigned to a BLOCK defn
    *   and combinations of a BLOCK and REQ tasks that are assigned to the various inventory (tasks based on the defns)
    *  When the BLOCK task and REQ task are on not on the aircraft
    *   and zipping is requested for the full tree of an inventory attached to the aircraft
    *   and the zip queue and zip tasks are processed
    *  Then the REQ task is not assigned to the BLOCK task
    * </pre>
    *
    * Note: Theory data points are used to provide the various combinations of inventory against
    * which the REQ and BLOCK tasks are created.
    */
   @Theory
   public void notAssignReqToBlockForTrkOnAircraftWhenTasksNotOnAircraft(
         TaskInventory aBlockInvDataPoint, TaskInventory aReqInvDataPoint ) throws Exception {

      // When the BLOCK task and REQ task are on not on the aircraft
      assumeTrue( !areAllTasksOnAircraft( aBlockInvDataPoint, aReqInvDataPoint ) );

      iBlockInvDataPoint = aBlockInvDataPoint;
      iReqInvDataPoint = aReqInvDataPoint;

      // Given an inventory tree for an aircraft that includes installed inventory and installed
      // sub-assemblies (with installed inventory).
      withInventoryTreeForAircraft();

      // Given a REQ defn assigned to a BLOCK defn.
      withReqDefnAssignedToBlockDefn();

      // Given combinations of a BLOCK and REQ tasks that are assigned to the various inventory
      // (tasks based on the defns).
      withBlockTask();
      withReqTask();
      ensureReqNotAssignedToBlock();

      // When zipping is requested for the full tree of an inventory attached to the
      // aircraft.
      int lZipId = BaselineSyncService.getInstance().requestZipTree( iAircraftTrk );

      // When the zip queue and zip tasks are processed.
      BaselineSyncHelper.getInstance().performZipping( lZipId, iAuthHr );

      // Then the REQ task is not assigned to the BLOCK task.
      assertReqIsNotAssignedToBlock();

   }


   /**
    * Verify that {@linkplain BaselineSyncService#requestZipTree(InventoryKey)} assigns REQ tasks to
    * BLOCK tasks for all inventory installed on the provided attached engine.
    *
    * <pre>
    * Given an inventory tree for an aircraft that includes installed inventory and installed sub-assemblies (with installed inventory)
    *   and a REQ defn assigned to a BLOCK defn
    *   and combinations of a BLOCK and REQ tasks that are assigned to the various inventory (tasks based on the defns)
    *  When the BLOCK task and REQ task are on the engine
    *   and zipping is requested for the full tree of the installed engine
    *   and the zip queue and zip tasks are processed
    *  Then the REQ task is assigned to the BLOCK task
    * </pre>
    *
    * Note: Theory data points are used to provide the various combinations of inventory against
    * which the REQ and BLOCK tasks are created.
    */
   @Theory
   public void assignReqToBlockForAttachedEngine( TaskInventory aBlockInvDataPoint,
         TaskInventory aReqInvDataPoint ) throws Exception {

      // When the BLOCK task and REQ task are on the engine.
      assumeTrue( areAllTasksOnEngine( aBlockInvDataPoint, aReqInvDataPoint ) );

      iBlockInvDataPoint = aBlockInvDataPoint;
      iReqInvDataPoint = aReqInvDataPoint;

      // Given an inventory tree for an aircraft that includes installed inventory and installed
      // sub-assemblies (with installed inventory).
      withInventoryTreeForAircraft();

      // Given a REQ defn assigned to a BLOCK defn.
      withReqDefnAssignedToBlockDefn();

      // Given combinations of a BLOCK and REQ tasks that are assigned to the various inventory
      // (tasks based on the defns).
      withBlockTask();
      withReqTask();
      ensureReqNotAssignedToBlock();

      // When zipping is requested for the full tree of the installed engine.
      int lZipId = BaselineSyncService.getInstance().requestZipTree( iEngine );

      // When the zip queue and zip tasks are processed.
      BaselineSyncHelper.getInstance().performZipping( lZipId, iAuthHr );

      // Then the REQ task is assigned to the BLOCK task.
      assertReqIsAssignedToBlock();

   }


   /**
    * Verify that {@linkplain BaselineSyncService#requestZipTree(InventoryKey)} assigns REQ tasks to
    * BLOCK tasks for all inventory installed on the provided attached engine.
    *
    * <pre>
    * Given an inventory tree for an aircraft that includes installed inventory and installed sub-assemblies (with installed inventory)
    *   and a REQ defn assigned to a BLOCK defn
    *   and combinations of a BLOCK and REQ tasks that are assigned to the various inventory (tasks based on the defns)
    *  When the BLOCK task and REQ task are not on the engine
    *   and zipping is requested for the full tree of the installed engine
    *   and the zip queue and zip tasks are processed
    *  Then the REQ task is not assigned to the BLOCK task
    * </pre>
    *
    * Note: Theory data points are used to provide the various combinations of inventory against
    * which the REQ and BLOCK tasks are created.
    */
   @Theory
   public void notAssignReqToBlockForAttachedEngineWhenTasksNotOnEngine(
         TaskInventory aBlockInvDataPoint, TaskInventory aReqInvDataPoint ) throws Exception {

      // When the BLOCK task and REQ task are not on the engine.
      assumeTrue( !areAllTasksOnEngine( aBlockInvDataPoint, aReqInvDataPoint ) );

      iBlockInvDataPoint = aBlockInvDataPoint;
      iReqInvDataPoint = aReqInvDataPoint;

      // Given an inventory tree for an aircraft that includes installed inventory and installed
      // sub-assemblies (with installed inventory).
      withInventoryTreeForAircraft();

      // Given a REQ defn assigned to a BLOCK defn.
      withReqDefnAssignedToBlockDefn();

      // Given combinations of a BLOCK and REQ tasks that are assigned to the various inventory
      // (tasks based on the defns).
      withBlockTask();
      withReqTask();
      ensureReqNotAssignedToBlock();

      // When zipping is requested for the full tree of the installed engine.
      int lZipId = BaselineSyncService.getInstance().requestZipTree( iEngine );

      // When the zip queue and zip tasks are processed.
      BaselineSyncHelper.getInstance().performZipping( lZipId, iAuthHr );

      // Then the REQ task is not assigned to the BLOCK task.
      assertReqIsNotAssignedToBlock();

   }


   /**
    * Verify that {@linkplain BaselineSyncService#requestZipTree(InventoryKey)} assigns REQ tasks to
    * BLOCK tasks for all inventory installed on the provided loose engine.
    *
    * <pre>
    * Given an inventory tree for a loose engine
    *   and a REQ defn assigned to a BLOCK defn
    *   and combinations of a BLOCK and REQ tasks that are assigned to the various inventory (tasks based on the defns)
    *  When the BLOCK task and REQ task are on the loose engine
    *   and zipping is requested for the full tree of the loose engine
    *   and the zip queue and zip tasks are processed
    *  Then the REQ task is assigned to the BLOCK task
    * </pre>
    *
    * Note: Theory data points are used to provide the various combinations of inventory against
    * which the REQ and BLOCK tasks are created.
    */
   @Theory
   public void assignReqToBlockForLooseEngine( TaskInventory aBlockInvDataPoint,
         TaskInventory aReqInvDataPoint ) throws Exception {

      // When the BLOCK task and REQ task are on the loose engine.
      assumeTrue( areAllTasksOnEngine( aBlockInvDataPoint, aReqInvDataPoint ) );

      iBlockInvDataPoint = aBlockInvDataPoint;
      iReqInvDataPoint = aReqInvDataPoint;

      // Given an inventory tree for a loose engine.
      withInventoryTreeForLooseEngine();

      // Given a REQ defn assigned to a BLOCK defn.
      withReqDefnAssignedToBlockDefn();

      // Given combinations of a BLOCK and REQ tasks that are assigned to the various inventory
      // (tasks based on the defns).
      withBlockTask();
      withReqTask();
      ensureReqNotAssignedToBlock();

      // When zipping is requested for the full tree of the loose engine.
      int lZipId = BaselineSyncService.getInstance().requestZipTree( iEngine );

      // When the zip queue and zip tasks are processed.
      BaselineSyncHelper.getInstance().performZipping( lZipId, iAuthHr );

      // Then the REQ task is assigned to the BLOCK task.
      assertReqIsAssignedToBlock();

   }


   /**
    * Verify that {@linkplain BaselineSyncService#requestZipTree(InventoryKey)} assigns REQ tasks to
    * BLOCK tasks for all inventory installed on the loose engine on which the provided inventory is
    * installed.
    *
    * <pre>
    * Given an inventory tree for a loose engine
    *   and a REQ defn assigned to a BLOCK defn
    *   and combinations of a BLOCK and REQ tasks that are assigned to the various inventory (tasks based on the defns)
    *  When the BLOCK task and REQ task are on the loose engine
    *   and zipping is requested for the full tree of an inventory attached to the loose engine
    *   and the zip queue and zip tasks are processed
    *  Then the REQ task is assigned to the BLOCK task
    * </pre>
    *
    * Note: Theory data points are used to provide the various combinations of inventory against
    * which the REQ and BLOCK tasks are created.
    */
   @Theory
   public void assignReqToBlockForTrkOnLooseEngine( TaskInventory aBlockInvDataPoint,
         TaskInventory aReqInvDataPoint ) throws Exception {

      // When the BLOCK task and REQ task are on the loose engine.
      assumeTrue( areAllTasksOnEngine( aBlockInvDataPoint, aReqInvDataPoint ) );

      iBlockInvDataPoint = aBlockInvDataPoint;
      iReqInvDataPoint = aReqInvDataPoint;

      // Given an inventory tree for a loose engine.
      withInventoryTreeForLooseEngine();

      // Given a REQ defn assigned to a BLOCK defn.
      withReqDefnAssignedToBlockDefn();

      // Given combinations of a BLOCK and REQ tasks that are assigned to the various inventory
      // (tasks based on the defns).
      withBlockTask();
      withReqTask();
      ensureReqNotAssignedToBlock();

      // When zipping is requested for the full tree of the loose engine.
      int lZipId = BaselineSyncService.getInstance().requestZipTree( iEngineTrk );

      // When the zip queue and zip tasks are processed.
      BaselineSyncHelper.getInstance().performZipping( lZipId, iAuthHr );

      // Then the REQ task is assigned to the BLOCK task.
      assertReqIsAssignedToBlock();

   }


   /**
    *
    * Verify that {@linkplain BaselineSyncService#requestZipTree(InventoryKey)} assigns multiple REQ
    * tasks to a BLOCK task regardless of where on the same assembly those REQ task inventory are
    * installed.
    *
    * <pre>
    * Given an inventory tree for a loose engine
    *   and multiple REQ defns assigned to a BLOCK defn
    *   and combinations of a BLOCK and multiple REQ tasks that are assigned to the various inventory (tasks based on the defns)
    *  When zipping is requested for the full tree of the loose engine
    *   and the zip queue and zip tasks are processed
    *   and the BLOCK task and REQ tasks are on the engine
    *  Then the REQ tasks are assigned to the BLOCK task
    * </pre>
    */
   @Theory
   public void assignMultipleReqsToBlockOnLooseEngine( TaskInventory aBlockInvDataPoint,
         TaskInventory aReqInvDataPoint1, TaskInventory aReqInvDataPoint2 ) throws Exception {

      // When the BLOCK task and REQ task are on the loose engine.
      assumeTrue( areAllTasksOnEngine( aBlockInvDataPoint, aReqInvDataPoint1, aReqInvDataPoint2 ) );

      iBlockInvDataPoint = aBlockInvDataPoint;
      iReqInvDataPoint = aReqInvDataPoint1;
      iAnotherReqInvDataPoint = aReqInvDataPoint2;

      // Given an inventory tree for a loose engine.
      withInventoryTreeForLooseEngine();

      // Given multiple REQ defns assigned to a BLOCK defn.
      withMultipleReqDefnAssignedToBlockDefn();

      // Given combinations of a BLOCK and multiple REQ tasks that are assigned to the various
      // inventory (tasks based on the defns)
      withBlockTask();
      withReqTask();
      withAnotherReqTask();

      // Ensure the two Reqs are not assigned to the Block.
      assertFalse(
            "Req task must NOT be assigned to Block task=" + iBlockInvDataPoint
                  + " , Req inventory=" + iReqInvDataPoint,
            isReqSubtaskOfBlock( iReqTask, iBlockTask ) );
      assertFalse(
            "Req task must NOT be assigned to Block task=" + iBlockInvDataPoint
                  + " , Req inventory=" + iAnotherReqInvDataPoint,
            isReqSubtaskOfBlock( iAnotherReqTask, iBlockTask ) );

      // When zipping is requested for the full tree of the loose engine.
      int lZipId = BaselineSyncService.getInstance().requestZipTree( iEngine );

      // When the zip queue and zip tasks are processed.
      BaselineSyncHelper.getInstance().performZipping( lZipId, iAuthHr );

      // Then the REQ tasks are assigned to the BLOCK task.
      assertTrue(
            "Expected Req task to be assigned to Block task; for Block inventory="
                  + iBlockInvDataPoint + " , Req inventory=" + iReqInvDataPoint,
            isReqSubtaskOfBlock( iReqTask, iBlockTask ) );
      assertTrue(
            "Expected Req task to be assigned to Block task; for Block inventory="
                  + iBlockInvDataPoint + " , Req inventory=" + iAnotherReqInvDataPoint,
            isReqSubtaskOfBlock( iAnotherReqTask, iBlockTask ) );

   }


   @Before
   public void before() {
      // Create a Human Resource to test with and ensure it has the appropriate user parameters set
      // up to perform zipping. Due to the confusion over Human Resources and Users within
      // Maintenix, we will not use a Domain entity to represent this HR. Instead we will use the
      // HumanResourseBuilder directly.
      iAuthHr = new HumanResourceDomainBuilder().build();
      iUserId = OrgHr.findByPrimaryKey( iAuthHr ).getUserId();
      UserParameters.setInstance( iUserId, "LOGIC", new UserParametersFake( iUserId, "LOGIC" ) );
   }


   @After
   public void after() {
      UserParameters.setInstance( iUserId, "LOGIC", null );
   }


   /**
    *
    * Sets up an inventory tree for an aircraft with the following hierarchy:
    *
    * <pre>
    * Aircraft
    *    TRK
    *    Engine ( i.e.sub - assembly )
    *       TRK
    *          TRK
    * </pre>
    */
   private void withInventoryTreeForAircraft() {

      iEngineTrkTrk = Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setAllowSynchronization( true );
         }
      } );

      iEngineTrk = Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setAllowSynchronization( true );
            aTrk.addTracked( iEngineTrkTrk );
         }
      } );

      iEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.allowSynchronization();
            aEngine.addTracked( iEngineTrk );
         }
      } );

      iAircraftTrk = Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setAllowSynchronization( true );
         }
      } );

      iAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.allowSynchronization();
            aAircraft.addTracked( iAircraftTrk );
            aAircraft.addEngine( iEngine );
         }
      } );

   }


   private void withInventoryTreeForLooseEngine() {

      iEngineTrkTrk = Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setAllowSynchronization( true );
         }
      } );

      iEngineTrk = Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setAllowSynchronization( true );
            aTrk.addTracked( iEngineTrkTrk );
         }
      } );

      iEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.allowSynchronization();
            aEngine.addTracked( iEngineTrk );
         }
      } );
   }


   private void withReqDefnAssignedToBlockDefn() {
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.setUnique( true );
                  aReqDefinition.setOnCondition( false );
               }
            } );
      iBlockDefn = Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

         @Override
         public void configure( BlockDefinition aBlockDefinition ) {
            aBlockDefinition.makeUnique();
            aBlockDefinition.addRequirementDefinition( iReqDefn );
         }
      } );
   }


   private void withMultipleReqDefnAssignedToBlockDefn() {
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.setUnique( true );
                  aReqDefinition.setOnCondition( false );
               }
            } );

      iAnotherReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefinition ) {
                  aReqDefinition.setUnique( true );
                  aReqDefinition.setOnCondition( false );
               }
            } );

      iBlockDefn = Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

         @Override
         public void configure( BlockDefinition aBlockDefinition ) {
            aBlockDefinition.makeUnique();
            aBlockDefinition.addRequirementDefinition( iReqDefn );
            aBlockDefinition.addRequirementDefinition( iAnotherReqDefn );
         }
      } );
   }


   private void withBlockTask() {

      iBlockTask = Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block aBlock ) {
            aBlock.setDefinition( iBlockDefn );
            aBlock.setInventory( pickInvBasedOnDataPoint( iBlockInvDataPoint ) );
         }
      } );
   }


   private void withReqTask() {

      iReqTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( iReqDefn );
            aReq.setInventory( pickInvBasedOnDataPoint( iReqInvDataPoint ) );
         }
      } );
   }


   private void withAnotherReqTask() {

      iAnotherReqTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( iAnotherReqDefn );
            aReq.setInventory( pickInvBasedOnDataPoint( iAnotherReqInvDataPoint ) );
         }
      } );
   }


   private InventoryKey pickInvBasedOnDataPoint( TaskInventory aTestInv ) {
      switch ( aTestInv ) {
         case AIRCRAFT:
            return iAircraft;
         case AIRCRAFT_TRK:
            return iAircraftTrk;
         case ENGINE:
            return iEngine;
         case ENGINE_TRK:
            return iEngineTrk;
         case ENGINE_TRK_TRK:
            return iEngineTrkTrk;
         default:
            throw new RuntimeException( "Unknown aBlockTaskInv datapoint." );
      }
   }


   private boolean areAllTasksOnAircraft( TaskInventory... aTaskInvDataPoints ) {

      for ( TaskInventory lTaskInvDataPoint : aTaskInvDataPoints ) {
         if ( !( lTaskInvDataPoint == TaskInventory.AIRCRAFT
               || lTaskInvDataPoint == TaskInventory.AIRCRAFT_TRK ) ) {
            return false;
         }
      }
      return true;
   }


   private boolean areAllTasksOnEngine( TaskInventory... aTaskInvDataPoints ) {

      for ( TaskInventory lTaskInvDataPoint : aTaskInvDataPoints ) {
         if ( !( lTaskInvDataPoint == TaskInventory.ENGINE
               || lTaskInvDataPoint == TaskInventory.ENGINE_TRK
               || lTaskInvDataPoint == TaskInventory.ENGINE_TRK_TRK ) ) {
            return false;
         }
      }
      return true;
   }


   private boolean isReqSubtaskOfBlock( TaskKey aReqTask, TaskKey aBlockTask ) {
      TaskKey lHTaskKey = SchedStaskTable.findByPrimaryKey( aReqTask ).getHTaskKey();
      return ( lHTaskKey != null && lHTaskKey.equals( aBlockTask ) );
   }


   private void ensureReqNotAssignedToBlock() {
      assertFalse(
            "Req task must NOT be assigned to Block task=" + iBlockInvDataPoint
                  + " , Req inventory=" + iReqInvDataPoint,
            isReqSubtaskOfBlock( iReqTask, iBlockTask ) );
   }


   private void assertReqIsAssignedToBlock() {
      assertTrue(
            "Expected Req task to be assigned to Block task; for Block inventory="
                  + iBlockInvDataPoint + " , Req inventory=" + iReqInvDataPoint,
            isReqSubtaskOfBlock( iReqTask, iBlockTask ) );
   }


   private void assertReqIsNotAssignedToBlock() {
      assertFalse(
            "Expected Req task to NOT be assigned to Block task=" + iBlockInvDataPoint
                  + " , Req inventory=" + iReqInvDataPoint,
            isReqSubtaskOfBlock( iReqTask, iBlockTask ) );
   }

}
