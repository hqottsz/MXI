
package com.mxi.mx.core.maintenance.plan.item.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.maintenance.eng.prog.block.model.BlockRevisionId;
import com.mxi.mx.core.maintenance.eng.prog.req.model.ReqRevisionId;
import com.mxi.mx.core.maintenance.plan.item.model.PlanningItemEntity;
import com.mxi.mx.core.maintenance.plan.item.model.PlanningItemId;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.unittest.table.stask.SchedStaskUtil;
import com.mxi.mx.persistence.store.AspectualResult;
import com.mxi.mx.persistence.store.Result;


/**
 * This class tests the {@link JdbcPlanningItemEntityDao} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class JdbcPlanningItemEntityDaoTest {

   private RefTaskClassKey iBlockTaskClass;

   private InventoryKey iInventory;

   private GlobalParametersStub iLogicParameters;

   private JdbcPlanningItemEntityDao iPlanningItemEntityDao;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests that the id of a requirement or block revision is returned when the definition aspect is
    * requested.
    */
   @Test
   public void testFindByIdWithDefinitionAspect() {

      TaskKey lTestBlockTask =
            new TaskBuilder().withTaskClass( iBlockTaskClass ).onInventory( iInventory ).build();

      TaskTaskKey lReqTaskRevision =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ ).build();
      TaskKey lReqTask = new TaskBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskRevision( lReqTaskRevision ).withParentTask( lTestBlockTask )
            .onInventory( iInventory ).build();

      TaskTaskKey lBlockTaskRevision =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.CHECK ).build();
      TaskKey lBlockTask = new TaskBuilder().withTaskClass( iBlockTaskClass )
            .withTaskRevision( lBlockTaskRevision ).withParentTask( lTestBlockTask )
            .onInventory( iInventory ).build();

      PlanningItemId lBlockPlanningItemId =
            new PlanningItemId( new SchedStaskUtil( lBlockTask ).getTable().getAlternateKey() );
      BlockRevisionId lBlockId = new BlockRevisionId(
            TaskTaskTable.findByPrimaryKey( lBlockTaskRevision ).getAlternateKey() );

      PlanningItemId lReqPlanningItemId =
            new PlanningItemId( new SchedStaskUtil( lReqTask ).getTable().getAlternateKey() );
      ReqRevisionId lReqId = new ReqRevisionId(
            TaskTaskTable.findByPrimaryKey( lReqTaskRevision ).getAlternateKey() );

      // test for block
      AspectualResult<PlanningItemEntity, PlanningItemEntity.Aspect> lResult =
            iPlanningItemEntityDao.find( lBlockPlanningItemId );
      PlanningItemEntity lEntity = lResult
            .withAspects( Collections.singleton( PlanningItemEntity.Aspect.DEFINITION_SUMMARY ) )
            .now();

      // test for req
      assertNotNull( lEntity.getDefinitionSummary() );
      assertEquals( lBlockId, lEntity.getDefinitionSummary().getBlockRevisionId() );
      assertNull( lEntity.getDefinitionSummary().getReqRevisionId() );

      lResult = iPlanningItemEntityDao.find( lReqPlanningItemId );
      lEntity = lResult
            .withAspects( Collections.singleton( PlanningItemEntity.Aspect.DEFINITION_SUMMARY ) )
            .now();

      assertNotNull( lEntity.getDefinitionSummary() );
      assertNull( lEntity.getDefinitionSummary().getBlockRevisionId() );
      assertEquals( lReqId, lEntity.getDefinitionSummary().getReqRevisionId() );
   }


   /**
    * Tests that the setPreventDeadlineSync method properly updates the database.
    */
   @Test
   public void testThatSetPreventDeadlineSyncUpdatesDatabase() {
      TaskKey lTask = new TaskBuilder().build();

      final Result<PlanningItemEntity> lTestableResult = buildTestableResult( lTask );

      new SchedStaskUtil( lTask ).assertPreventDeadlineSync( false );

      iPlanningItemEntityDao.setPreventDeadlineSync( lTestableResult, true );

      new SchedStaskUtil( lTask ).assertPreventDeadlineSync( true );

      iPlanningItemEntityDao.setPreventDeadlineSync( lTestableResult, false );

      new SchedStaskUtil( lTask ).assertPreventDeadlineSync( false );
   }


   @Before
   public void loadData() throws Exception {

      ConfigSlotKey lHoleConfigSlot = new ConfigSlotBuilder( "HOLE" ).withName( "Hole" ).build();
      ConfigSlotPositionKey lHolePosition =
            new ConfigSlotPositionBuilder().withConfigSlot( lHoleConfigSlot ).build();

      // inventory key
      iInventory = new InventoryBuilder().withConfigSlotPosition( lHolePosition )
            .withClass( RefInvClassKey.ACFT ).build();

      iPlanningItemEntityDao = new JdbcPlanningItemEntityDao();

      DataSetArgument lRefTaskClassArgs = new DataSetArgument();
      lRefTaskClassArgs.add( "task_class_db_id", 0 );
      lRefTaskClassArgs.add( "task_class_cd", "BLOCK" );
      lRefTaskClassArgs.add( "class_mode_cd", "BLOCK" );

      MxDataAccess.getInstance().executeInsert( "ref_task_class", lRefTaskClassArgs );

      iBlockTaskClass = new RefTaskClassKey( 0, "BLOCK" );
   }


   /**
    * Builds a result that can be used during the test.
    *
    * @param aTask
    *           The task key
    *
    * @return The fake result
    */
   private Result<PlanningItemEntity> buildTestableResult( final TaskKey aTask ) {
      return new Result<PlanningItemEntity>() {

         @Override
         public PlanningItemEntity now() {
            UUID lId = SchedStaskTable.findByPrimaryKey( aTask ).getAlternateKey();
            PlanningItemEntity lEntity = new PlanningItemEntity( new PlanningItemId( lId ) );
            lEntity.setLegacyKey( aTask );

            return lEntity;
         }
      };
   }
}
