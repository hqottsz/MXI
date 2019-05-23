
package com.mxi.mx.db.plsql.baselinesyncpkg;

import static com.mxi.mx.core.key.RefTaskClassKey.REQ;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.SUPRSEDE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.plsql.StoredProcedureCall;
import com.mxi.mx.core.table.ref.RefTaskClass;


/**
 * Unit tests for the plsql package procedure BaselineSyncPkg.RequestZipForNewBlock
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class RequestZipForNewBlockTest {

   private static final TaskDefnKey BLOCK_DEFN_1 = new TaskDefnKey( 1, 1 );
   private static final TaskDefnKey BLOCK_DEFN_2 = new TaskDefnKey( 1, 2 );
   private static final TaskDefnKey BLOCK_DEFN_3 = new TaskDefnKey( 1, 3 );
   private static final TaskDefnKey REQ_DEFN_1 = new TaskDefnKey( 10, 1 );
   private static final TaskDefnKey REQ_DEFN_2 = new TaskDefnKey( 10, 2 );
   private static final TaskDefnKey REQ_DEFN_3 = new TaskDefnKey( 10, 3 );
   private static final RefTaskClassKey BLOCK = TaskRevisionBuilder.TASK_CLASS_BLOCK;
   private static final ConfigSlotKey CONFIG_SLOT_1 = new ConfigSlotKey( 100, "CS1", 1 );
   private static final String BLOCK_CHAIN_1 = "block chain 1";

   private StoredProcedureCall iStoredProcedure;
   private InventoryKey iAcftInv;
   private InventoryKey iSubInv;
   private InventoryKey iAnotherSubInv;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block against acft
    *  1 req against acft (mapped to block)
    * 
    *  call requestZipForNewBlock with acft and block
    * 
    * Expect:
    *  the block and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockAgainstAcftWithReqAgainstAcft() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iAcftInv ).build();

      TaskKey lBlock =
            new TaskBuilder().withTaskRevision( lBlockBuilder ).onInventory( iAcftInv ).build();

      // DataSetArgument lArgs = new DataSetArgument();
      // lArgs.add( "an_BlockDbId", lBlock.getDbId() );
      // lArgs.add( "an_BlockId", lBlock.getId() );
      // executeSql(
      // "get the list of BLOCK task definition revisions",
      // "SELECT task_task.task_db_id, task_task.task_id FROM sched_stask block_stask INNER JOIN
      // task_task ON task_task.task_db_id = block_stask.task_db_id AND task_task.task_id =
      // block_stask.task_id WHERE block_stask.sched_db_id = :an_BlockDbId AND block_stask.sched_id
      // = :an_BlockId AND task_task.block_chain_sdesc IS NULL AND task_task.unique_bool = 1 UNION
      // SELECT block_chain.task_db_id, block_chain.task_id FROM sched_stask block_stask INNER JOIN
      // task_task ON task_task.task_db_id = block_stask.task_db_id AND task_task.task_id =
      // block_stask.task_id INNER JOIN task_task block_chain ON block_chain.block_chain_sdesc =
      // task_task.block_chain_sdesc AND block_chain.revision_ord = task_task.revision_ord AND
      // block_chain.assmbl_db_id = task_task.assmbl_db_id AND block_chain.assmbl_cd =
      // task_task.assmbl_cd AND block_chain.assmbl_bom_id = task_task.assmbl_bom_id WHERE
      // block_stask.sched_db_id = :an_BlockDbId AND block_stask.sched_id = :an_BlockId AND
      // task_task.unique_bool = 1 ",
      // lArgs );

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock );

      assertZipTasks( lBlock, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block against acft
    *  1 req against SUB-INV (mapped to block)
    * 
    *  call requestZipForNewBlock with acft and block
    * 
    * Expect:
    *  the block and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockAgainstAcftWithReqAgainstSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV );

      TaskRevisionBuilder lBlockBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 )
            .withStatus( RefTaskDefinitionStatusKey.ACTV ).isUnique();

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iSubInv ).build();

      TaskKey lBlock =
            new TaskBuilder().withTaskRevision( lBlockBuilder ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock );

      assertZipTasks( lBlock, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block against acft
    *  1 req against sub-inv (mapped to block)
    * 
    *  call requestZipForNewBlock with SUB-INV and block
    * 
    * Expect:
    *  the block and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockAgainstAcftWithReqAgainstSubInvPassingSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV );

      TaskRevisionBuilder lBlockBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 )
            .withStatus( RefTaskDefinitionStatusKey.ACTV ).isUnique();

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iSubInv ).build();

      TaskKey lBlock =
            new TaskBuilder().withTaskRevision( lBlockBuilder ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock );

      assertZipTasks( lBlock, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block against acft
    *  1 req against ACFT (mapped to block)
    * 
    *  call requestZipForNewBlock with sub-inv and block
    * 
    * Expect:
    *  the block and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockAgainstAcftWithReqAgainstAcftPassingSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV );

      TaskRevisionBuilder lBlockBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 )
            .withStatus( RefTaskDefinitionStatusKey.ACTV ).isUnique();

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iAcftInv ).build();

      TaskKey lBlock =
            new TaskBuilder().withTaskRevision( lBlockBuilder ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock );

      assertZipTasks( lBlock, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block against acft
    *  1 req against ANOTHER SUB-INV (mapped to block)
    * 
    *  call requestZipForNewBlock with sub-inv and block
    * 
    * Expect:
    *  the block and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockAgainstAcftWithReqAgainstAnotherSubInvPassingSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV );

      TaskRevisionBuilder lBlockBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 )
            .withStatus( RefTaskDefinitionStatusKey.ACTV ).isUnique();

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iAcftInv ).build();

      TaskKey lBlock = new TaskBuilder().withTaskRevision( lBlockBuilder )
            .onInventory( iAnotherSubInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock );

      assertZipTasks( lBlock, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block against acft
    *  2 REQ AGAINST THE ACFT (MAPPED TO BLOCK)
    * 
    *  call requestZipForNewBlock with acft and block
    * 
    * Expect:
    *  the block and both reqs added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockAgainstAcftWithManyReqAgainstAcft() throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iAcftInv ).build();

      TaskKey lBlock =
            new TaskBuilder().withTaskRevision( lBlockBuilder ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock );

      assertZipTasks( lBlock, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block against acft
    *  1 REQ AGAINST ACFT (MAPPED TO BLOCK)
    *  1 REQ AGAINST SUB-INV (MAPPED TO BLOCK)
    * 
    *  call requestZipForNewBlock with acft and block
    * 
    * Expect:
    *  the block and both reqs added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockAgainstAcftWithOneReqAgainstAcftAndOneReqAgainstSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iSubInv ).build();

      TaskKey lBlock =
            new TaskBuilder().withTaskRevision( lBlockBuilder ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock );

      assertZipTasks( lBlock, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block against acft
    *  2 REQ AGAINST SUB-INV (MAPPED TO BLOCK)
    * 
    *  call requestZipForNewBlock with SUB-INV and block
    * 
    * Expect:
    *  the block and both reqs added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockAgainstAcftWithManyReqAgainstSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iSubInv ).build();

      TaskKey lBlock =
            new TaskBuilder().withTaskRevision( lBlockBuilder ).onInventory( iSubInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock );

      assertZipTasks( lBlock, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block against acft
    *  1 REQ AGAINST SUB-INV (MAPPED TO BLOCK)
    *  1 REQ AGAINST ACFT (MAPPED TO BLOCK)
    * 
    *  call requestZipForNewBlock with sub-inv and block
    * 
    * Expect:
    *  the block and both reqs added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockAgainstAcftWithReqAgainstSubInvAndReqAgainstAcft() throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iSubInv ).build();

      TaskKey lBlock =
            new TaskBuilder().withTaskRevision( lBlockBuilder ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock );

      assertZipTasks( lBlock, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block against acft
    *  1 REQ AGAINST ANOTHER SUB-INV (MAPPED TO BLOCK)
    *  1 req against acft (mapped to block)
    * 
    *  call requestZipForNewBlock with sub-inv and block
    * 
    * Expect:
    *  the block and both reqs added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockAgainstAcftWithReqAgainstAnotherSubInvAndReqAgainstAcft() throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskKey lReq1 = new TaskBuilder().withTaskRevision( lReqRevBuilder1 )
            .onInventory( iAnotherSubInv ).build();

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iAcftInv ).build();

      TaskKey lBlock =
            new TaskBuilder().withTaskRevision( lBlockBuilder ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock );

      assertZipTasks( lBlock, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 BLOCKS AGAINST ACFT
    *  1 req against acft (MAPPED TO BOTH BLOCKS)
    * 
    *  call requestZipForNewBlock with acft and 1 OF THE BLOCKS
    * 
    * Expect:
    *  both blocks and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testManyBlockAgainstAcftWithReqAgainstAcft() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iAcftInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 blocks against acft
    *  1 REQ AGAINST SUB-INV (MAPPED TO BOTH BLOCKS)
    * 
    *  call requestZipForNewBlock with acft and 1 of the blocks
    * 
    * Expect:
    *  both blocks and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testManyBlockAgainstAcftWithReqAgainstSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iSubInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 blocks against acft
    *  1 req against sub-inv (mapped to both blocks)
    * 
    *  call requestZipForNewBlock with SUB-INV and 1 of the blocks
    * 
    * Expect:
    *  both blocks and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testManyBlockAgainstAcftWithReqAgainstSubInvPassingSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iSubInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 blocks against acft
    *  1 REQ AGAINST ACFT (mapped to both blocks)
    * 
    *  call requestZipForNewBlock with sub-inv and 1 of the blocks
    * 
    * Expect:
    *  both blocks and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testManyBlockAgainstAcftWithReqAgainstAcftPassingSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iAcftInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 blocks against acft
    *  1 REQ AGAINST ANOTHER SUB-INV (mapped to both blocks)
    * 
    *  call requestZipForNewBlock with sub-inv and 1 of the blocks
    * 
    * Expect:
    *  both blocks and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testManyBlockAgainstAcftWithReqAgainstAnotherSubInvPassingSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskKey lReq = new TaskBuilder().withTaskRevision( lReqRevBuilder )
            .onInventory( iAnotherSubInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 blocks against acft
    *  2 req against acft (both mapped to both blocks)
    * 
    *  call requestZipForNewBlock with acft and 1 of the blocks
    * 
    * Expect:
    *  both blocks and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testManyBlockAgainstAcftWithManyReqAgainstAcft() throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iAcftInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 blocks against acft
    *  1 REQ AGAINST ACFT (mapped to both blocks)
    *  1 REQ AGAINST SUB-INV (mapped to both blocks)
    * 
    *  call requestZipForNewBlock with acft and 1 of the blocks
    * 
    * Expect:
    *  both blocks and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testManyBlockAgainstAcftWithReqAgainstAcftAndReqAgainstSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iSubInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 block against acft
    *  2 REQ AGAINST SUB-INV (MAPPED TO BOTH BLOCK)
    * 
    *  call requestZipForNewBlock with SUB-INV and block
    * 
    * Expect:
    *  both blocks and both reqs added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testManyBlockAgainstAcftWithManyReqAgainstSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iSubInv ).build();

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iSubInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 block against acft
    *  1 REQ AGAINST SUB-INV (MAPPED TO BOTH BLOCK)
    *  1 REQ AGAINST ACFT (MAPPED TO BOTH BLOCK)
    * 
    *  call requestZipForNewBlock with sub-inv and block
    * 
    * Expect:
    *  both blocks and both reqs added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testManyBlockAgainstAcftWithReqAgainstSubInvAndReqAgainstAcft() throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iSubInv ).build();

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iAcftInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 block against acft
    *  1 REQ AGAINST ANOTHER SUB-INV (MAPPED TO BOTH BLOCK)
    *  1 REQ AGAINST ACFT (MAPPED TO BOTH BLOCK)
    * 
    *  call requestZipForNewBlock with sub-inv and block
    * 
    * Expect:
    *  both blocks and both reqs added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testManyBlockAgainstAcftWithReqAgainstAnotherSubInvAndReqAgainstAcft()
         throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).mapToRequirement( REQ_DEFN_2 )
            .withStatus( ACTV ).isUnique();

      TaskKey lReq1 = new TaskBuilder().withTaskRevision( lReqRevBuilder1 )
            .onInventory( iAnotherSubInv ).build();

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iAcftInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 block against acft
    *  1 req against acft (mapped to one block)
    *  1 REQ AGAINST SUB-INV (MAPPED TO OTHER BLOCK)
    * 
    *  call requestZipForNewBlock with acft and block
    * 
    * Expect:
    *  only the requested block and its req is added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   public void
         testManyBlockAgainstAcftWithOneReqAgainstAcftInOneBlockAndOtherReqAgainstSubInvInOtherBlock()
               throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_2 ).withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iAcftInv ).build();

      new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iSubInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock1 );

      assertZipTasks( lBlock1, lReq1 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 block against acft
    *  1 REQ AGAINST SUB-INV (MAPPED TO ONE BLOCK)
    *  1 REQ AGAINST SUB-INV (MAPPED TO OTHER BLOCK)
    * 
    *  call requestZipForNewBlock with SUB-INV and block
    * 
    * Expect:
    *  only the requested block and its req is added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   public void
         testManyBlockAgainstAcftWithOneReqAgainstSubInvInOneBlockAndOtherReqAgainstSubInvInOtherBlock()
               throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_2 ).withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iSubInv ).build();

      new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iSubInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lReq1 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 block against acft
    *  1 REQ AGAINST SUB-INV (MAPPED TO ONE BLOCK)
    *  1 REQ AGAINST ACFT (MAPPED TO OTHER BLOCK)
    * 
    *  call requestZipForNewBlock with sub-inv and block
    * 
    * Expect:
    *  only the requested block and its req is added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   public void
         testManyBlockAgainstAcftWithOneReqAgainstSubInvInOneBlockAndOtherReqAgainstAcftInOtherBlock()
               throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_2 ).withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iSubInv ).build();

      new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iAcftInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lReq1 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 block against acft
    *  1 REQ AGAINST ANOTHER SUB-INV (MAPPED TO ONE BLOCK)
    *  1 req against acft (mapped to other block)
    * 
    *  call requestZipForNewBlock with sub-inv and block
    * 
    * Expect:
    *  only the requested block and its req is added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   public void
         testManyBlockAgainstAcftWithOneReqAgainstSubInvInOneBlockAndOtherReqAgainstAnotherSuvInvInOtherBlock()
               throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_2 ).withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iSubInv ).build();

      new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iAnotherSubInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lReq1 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  2 block against acft
    *  1 REQ AGAINST ACFT (MAPPED TO ONE BLOCK)
    *  1 REQ AGAINST ACFT (MAPPED TO OTHER BLOCK)
    * 
    *  call requestZipForNewBlock with acft and block
    * 
    * Expect:
    *  only the requested block and its req is added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testManyBlockAgainstAcftWithOneReqInOneBlockAndOtherReqInOtherBlock()
         throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_2 ).withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iAcftInv ).build();

      new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iAcftInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock1 );

      assertZipTasks( lBlock1, lReq1 );

   }


   /**
    * Verify that the provided tasks are the only tasks found in the zip_task table.
    *
    * @param aExpectedTasks
    *           tasks expected in zip_task
    */
   private void assertZipTasks( TaskKey... aExpectedTasks ) {

      // Get tasks in the zip_task table.
      QuerySet lQs =
            QuerySetFactory.getInstance().executeQueryTable( "zip_task", new DataSetArgument() );
      List<TaskKey> lZipTasks = new ArrayList<TaskKey>();
      while ( lQs.next() ) {
         lZipTasks.add( lQs.getKey( TaskKey.class, "sched_db_id", "sched_id" ) );
      }

      // Verify that all the expected tasks are in the zip_task table.
      assertEquals( "zip_task contains the wrong number of tasks", aExpectedTasks.length,
            lQs.getRowCount() );
      for ( TaskKey lExpectedTask : aExpectedTasks ) {
         assertTrue( "zip_task does not contain the expected task: " + lExpectedTask,
               lZipTasks.contains( lExpectedTask ) );
      }
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {

      // Create a BLOCK task class (as it does not exist as 0-level data, believe it or not).
      new RefTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).create();

      // Create an ACFT inventory.
      iAcftInv = new InventoryBuilder().withClass( RefInvClassKey.ACFT ).allowSync().build();

      // Create an inventory attached to the aircraft
      iSubInv = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withParentInventory( iAcftInv ).withAssemblyInventory( iAcftInv ).allowSync().build();

      // Create another inventory attached to the aircraft
      iAnotherSubInv = new InventoryBuilder().withClass( RefInvClassKey.TRK )
            .withParentInventory( iAcftInv ).withAssemblyInventory( iAcftInv ).allowSync().build();

      iStoredProcedure = StoredProcedureCall.getInstance();

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block chain (2 blocks) against acft
    *  1 req against acft (mapped to both blocks)
    * 
    *  call requestZipForNewBlock with acft and 1 of the blocks
    * 
    * Expect:
    *  both blocks and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockChainAgainstAcftWithReqAgainstAcft() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iAcftInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block chain (2 blocks) against acft
    *  1 REQ AGAINST SUB-INV (MAPPED TO BOTH BLOCKS)
    * 
    *  call requestZipForNewBlock with acft and 1 of the blocks
    * 
    * Expect:
    *  both blocks and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockChainAgainstAcftWithReqAgainstSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iSubInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block chain (2 blocks) against acft
    *  1 req against sub-inv (mapped to both blocks)
    * 
    *  call requestZipForNewBlock with SUB-INV and 1 of the blocks
    * 
    * Expect:
    *  both blocks and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockChainAgainstAcftWithReqAgainstSubInvPassingSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iSubInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block chain (2 blocks) against acft
    *  1 req against ACFT (mapped to both blocks)
    * 
    *  call requestZipForNewBlock with SUB-INV and 1 of the blocks
    * 
    * Expect:
    *  both blocks and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockChainAgainstAcftWithReqAgainstAcftPassingSubInv() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iAcftInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block chain (2 blocks) against acft
    *  1 req against ANOTHER SUB-INV (mapped to both blocks)
    * 
    *  call requestZipForNewBlock with SUB-INV and 1 of the blocks
    * 
    * Expect:
    *  both blocks and the req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockChainAgainstAcftWithReqAgainstAnotherSubInvPassingSubInv()
         throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskKey lReq = new TaskBuilder().withTaskRevision( lReqRevBuilder )
            .onInventory( iAnotherSubInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block chain (2 blocks) against acft
    *  1 REQ AGAINST ACFT (MAPPED TO ONE BLOCK)
    *  1 REQ AGAINST ACFT (MAPPED TO OTHER BLOCK)
    * 
    *  call requestZipForNewBlock with ACFT and 1 of the blocks
    * 
    * Expect:
    *  both blocks and both reqs added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockChainAgainstAcftOneReqInOneBlockAndOtherReqInOtherBlock() throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_2 ).withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iAcftInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block chain (2 blocks) against acft
    *  1 REQ AGAINST ACFT (MAPPED TO ONE BLOCK)
    *  1 REQ AGAINST SUB-INV (MAPPED TO OTHER BLOCK)
    * 
    *  call requestZipForNewBlock with ACFT and 1 of the blocks
    * 
    * Expect:
    *  both blocks and both reqs added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   public void
         testBlockChainAgainstAcftOneReqAgainstAcftInOneBlockAndOtherReqAgainstSubInvInOtherBlock()
               throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_2 ).withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iSubInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block chain (2 blocks) against acft
    *  1 REQ AGAINST ACFT (MAPPED TO ONE BLOCK)
    *  1 REQ AGAINST ACFT (MAPPED TO OTHER BLOCK)
    * 
    *  call requestZipForNewBlock with SUB-INV and 1 of the blocks
    * 
    * Expect:
    *  both blocks and both reqs added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockChainAgainstAcftOneReqInOneBlockAndOtherReqInOtherBlockPassingInSubInv()
         throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_2 ).withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iAcftInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block chain (2 blocks) against acft
    *  1 REQ AGAINST SUB-INV (MAPPED TO ONE BLOCK)
    *  1 REQ AGAINST ACFT (MAPPED TO OTHER BLOCK)
    * 
    *  call requestZipForNewBlock with SUB-INV and 1 of the blocks
    * 
    * Expect:
    *  both blocks and both reqs added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   public void
         testBlockChainAgainstAcftOneReqAgainstSubInvInOneBlockAndOtherReqInOtherBlockPassingInSubInv()
               throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_2 ).withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iSubInv ).build();

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iAcftInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block chain (2 blocks) against acft
    *  1 REQ AGAINST SUB-INV (MAPPED TO ONE BLOCK)
    *  1 REQ AGAINST ANOTHER SUB-INV (MAPPED TO OTHER BLOCK)
    * 
    *  call requestZipForNewBlock with SUB-INV and 1 of the blocks
    * 
    * Expect:
    *  both blocks and both reqs added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   public void
         testBlockChainAgainstAcftOneReqAgainstSubInvInOneBlockAndOtherReqAgainstAnotherInOtherBlockPassingInSubInv()
               throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder2 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_2 ).withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iSubInv ).build();

      TaskKey lReq2 = new TaskBuilder().withTaskRevision( lReqRevBuilder2 )
            .onInventory( iAnotherSubInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlockBuilder2 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iSubInv, lBlock1 );

      assertZipTasks( lBlock1, lBlock2, lReq1, lReq2 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    *  1 block chain (2 blocks) against acft
    *  1 single block against the acft
    *  1 req against the acft (mapped to the single block and one of the block chain blocks)
    * 
    *  call requestZipForNewBlock with acft and 1 of the block chain blocks
    * 
    * Expect:
    *  all blocks and req added to zip_queue
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testBlockChainWithReqInOneBlockAndSingleBlockWithSameReq() throws Exception {

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lBlockBuilder1 = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( BLOCK ).mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder11 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskRevisionBuilder lBlockBuilder12 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_3 ).withTaskClass( BLOCK )
                  .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iAcftInv ).build();

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlockBuilder1 ).onInventory( iAcftInv ).build();

      TaskKey lBlock11 =
            new TaskBuilder().withTaskRevision( lBlockBuilder11 ).onInventory( iAcftInv ).build();

      TaskKey lBlock12 =
            new TaskBuilder().withTaskRevision( lBlockBuilder12 ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlock11 );

      assertZipTasks( lBlock1, lBlock11, lBlock12, lReq1 );

   }


   /**
    * <pre>
    *
    * Test scenario:
    * 
    *  block A against acft
    *  - superceeded revision 1
    *  - actv revision 2
    *  block B against acft
    * 
    *  1 req against acft (mapped to the actv rev of block A)
    *  1 req against acft (mapped to the superceeded rev of block A and mapped to block B)
    *  1 req against acft (mapped to block B)
    * 
    *  call requestZipForNewBlock with acft and the actv rev of block A
    * 
    * Expect:
    *  all blocks and the req mapped to the actv rev of block A added to zip_queue
    * 
    *  This test verifies the not-so-obvious behaviour that the query
    *  obtains all revisions of the block task's definition,
    *  then using those revisions obtains all mapped requirements,
    *  then using those requirements obtains all (other) mapped block revisions.
    * 
    *  Because the second req is mapped to the superceeded rev of block A and is mapped to block B,
    *  both of those blocks are also added to the zip_queue
    *  (along with the passed in actv rev of block A and its mapped requirement).
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testSuperceedBlockWithReqInThatBlockAndAnotherBlock() throws Exception {

      TaskRevisionBuilder lReqRevBuilder1 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder2 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( REQ ).withStatus( ACTV );

      TaskRevisionBuilder lReqRevBuilder3 = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_3 )
            .withTaskClass( REQ ).withStatus( ACTV );

      // Block A rev2 (actv) contains Req 1
      TaskRevisionBuilder lBlockBuilderA_Rev2 = new TaskRevisionBuilder()
            .withTaskDefn( BLOCK_DEFN_1 ).withRevisionNumber( 2 ).withTaskClass( BLOCK )
            .mapToRequirement( REQ_DEFN_1 ).withStatus( ACTV ).isUnique();

      // Block A rev1 (superceeed) contains Req 2
      TaskRevisionBuilder lBlockBuilderA_Rev1 =
            new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 ).withRevisionNumber( 1 )
                  .withTaskClass( BLOCK ).withConfigSlot( CONFIG_SLOT_1 )
                  .mapToRequirement( REQ_DEFN_2 ).withStatus( SUPRSEDE ).isUnique();

      // Block B contains Req 2 and Req 3
      TaskRevisionBuilder lBlockBuilderB = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( BLOCK ).withConfigSlot( CONFIG_SLOT_1 ).mapToRequirement( REQ_DEFN_2 )
            .mapToRequirement( REQ_DEFN_3 ).withStatus( ACTV ).isUnique();

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReqRevBuilder1 ).onInventory( iAcftInv ).build();

      new TaskBuilder().withTaskRevision( lReqRevBuilder2 ).onInventory( iAcftInv ).build();

      new TaskBuilder().withTaskRevision( lReqRevBuilder3 ).onInventory( iAcftInv ).build();

      TaskKey lBlockA_Rev1 = new TaskBuilder().withTaskRevision( lBlockBuilderA_Rev1 )
            .onInventory( iAcftInv ).build();

      TaskKey lBlockA_Rev2 = new TaskBuilder().withTaskRevision( lBlockBuilderA_Rev2 )
            .onInventory( iAcftInv ).build();

      TaskKey lBlockB =
            new TaskBuilder().withTaskRevision( lBlockBuilderB ).onInventory( iAcftInv ).build();

      iStoredProcedure.requestZipForNewBlock( iAcftInv, lBlockA_Rev2 );

      assertZipTasks( lBlockA_Rev1, lBlockA_Rev2, lBlockB, lReq1 );

   }
}
