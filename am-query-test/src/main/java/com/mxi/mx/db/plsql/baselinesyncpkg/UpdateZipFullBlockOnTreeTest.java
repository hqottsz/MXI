
package com.mxi.mx.db.plsql.baselinesyncpkg;

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
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.plsql.StoredProcedureCall;
import com.mxi.mx.core.table.ref.RefTaskClass;


/**
 * Test the BaselineSyncPkg.updateZipFullBlockOnTree plsql procedure.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateZipFullBlockOnTreeTest {

   private static final int ZIP_ID = 1;
   private static final String BLOCK_CHAIN_1 = "BLOCK_CHAIN_1";
   private static final String BLOCK_CHAIN_2 = "BLOCK_CHAIN_2";
   private static final ConfigSlotKey CONFIG_SLOT_1 = new ConfigSlotKey( 1, "CS_1", 1 );
   private static final TaskDefnKey BLOCK_DEFN_1 = new TaskDefnKey( 1, 1 );
   private static final TaskDefnKey BLOCK_DEFN_2 = new TaskDefnKey( 1, 2 );
   private static final TaskDefnKey BLOCK_DEFN_3 = new TaskDefnKey( 1, 3 );
   private static final TaskDefnKey BLOCK_DEFN_4 = new TaskDefnKey( 1, 4 );
   private static final TaskDefnKey REQ_DEFN_1 = new TaskDefnKey( 10, 1 );
   private static final TaskDefnKey REQ_DEFN_2 = new TaskDefnKey( 10, 2 );
   private static final TaskDefnKey REQ_DEFN_3 = new TaskDefnKey( 10, 3 );

   private static InventoryKey iAcftInv;
   private static InventoryKey iNonAcftInv;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Exercises the following scenario:
    *
    * <pre>
    *         Block_CHAIN_1 = BLOCK_1* -> BLOCK_2
    *                             |           |
    *                           REQ_1       REQ_2
    * 
    *         Block_CHAIN_2 = BLOCK_3  -> BLOCK_4
    *                             |           |
    *                           REQ_3       REQ_1
    * 
    *         (* indicates target block)
    * 
    *       Expect zip_task to contain the following:
    *         All the blocks from the block chain and their associated requirements
    *         (as BLOCK_1 is within the block chain).
    *         As well, BLOCK_4 (as REQ_1, which is in BLOCK_1, is also in BLOCK_4).
    * </pre>
    */
   @Test
   public void testBlockChainsReqExistsInAnotherBlockChain() {
      TaskRevisionBuilder lReq1RevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( RefTaskClassKey.REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV );

      TaskKey lReq1 =
            new TaskBuilder().withTaskRevision( lReq1RevBuilder ).onInventory( iAcftInv ).build();

      TaskRevisionBuilder lReq2RevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_2 )
            .withTaskClass( RefTaskClassKey.REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV );

      TaskKey lReq2 =
            new TaskBuilder().withTaskRevision( lReq2RevBuilder ).onInventory( iAcftInv ).build();

      TaskRevisionBuilder lReq3RevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_3 )
            .withTaskClass( RefTaskClassKey.REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV );

      new TaskBuilder().withTaskRevision( lReq3RevBuilder ).onInventory( iAcftInv ).build();

      TaskRevisionBuilder lBlock1Builder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique()
            .mapToRequirement( REQ_DEFN_1 ).withBlockChainDesc( BLOCK_CHAIN_1 )
            .withConfigSlot( CONFIG_SLOT_1 );

      TaskKey lBlock1 =
            new TaskBuilder().withTaskRevision( lBlock1Builder ).onInventory( iAcftInv ).build();

      TaskRevisionBuilder lBlock2Builder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique()
            .mapToRequirement( REQ_DEFN_2 ).withBlockChainDesc( BLOCK_CHAIN_1 )
            .withConfigSlot( CONFIG_SLOT_1 );

      TaskKey lBlock2 =
            new TaskBuilder().withTaskRevision( lBlock2Builder ).onInventory( iAcftInv ).build();

      TaskRevisionBuilder lBlock3Builder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_3 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique()
            .mapToRequirement( REQ_DEFN_3 ).withBlockChainDesc( BLOCK_CHAIN_2 )
            .withConfigSlot( CONFIG_SLOT_1 );

      new TaskBuilder().withTaskRevision( lBlock3Builder ).onInventory( iAcftInv ).build();

      TaskRevisionBuilder lBlock4Builder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_4 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique()
            .mapToRequirement( REQ_DEFN_1 ).withBlockChainDesc( BLOCK_CHAIN_2 )
            .withConfigSlot( CONFIG_SLOT_1 );

      TaskKey lBlock4 =
            new TaskBuilder().withTaskRevision( lBlock4Builder ).onInventory( iAcftInv ).build();

      executePrecedure( lBlock1, iAcftInv );

      // Verify that all the blocks, and their associated requirements, in BLOCK_CHAIN_1 are in
      // zip_task (as BLOCK_1 is within that block chain). As well, verify that BLOCK_4 is in
      // zip_task (as REQ_1, which is in BLOCK_1, is also in BLOCK_4).
      assertZipTasks( lBlock1, lBlock2, lReq1, lReq2, lBlock4 );
   }


   /**
    * Exercises the following scenario:
    *
    * <pre>
    *         Block Chain = BLOCK_1* (ACFT) -> BLOCK_2 (ACFT)
    * 
    *         (* indicates target block)
    * 
    *       Expect zip_task to contain the following:
    *         All the blocks from the block chain (as BLOCK_1 is within the block chain).
    *         (note: ensure ACFT inventory is properly handled)
    * </pre>
    */
   @Test
   public void testBlockChainWithNoReqsAgainstAcft() {
      TaskRevisionBuilder lBlockRevBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique()
            .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 );

      TaskTaskKey lBlockRev = lBlockRevBuilder.build();

      TaskKey lBlock1 = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iAcftInv ).build();

      executePrecedure( lBlock1, iAcftInv );

      // Verify that both blocks are in the zip_task table.
      assertZipTasks( lBlock1, lBlock2 );
   }


   /**
    * Exercises the following scenario:
    *
    * <pre>
    *         Block Chain = BLOCK_1* (non-ACFT) -> BLOCK_2 (non-ACFT)
    * 
    *         (* indicates target block)
    * 
    *       Expect zip_task to contain the following:
    *         All the blocks from the block chain (as BLOCK_1 is within the block chain).
    *         (note: ensuring non-ACFT inventory is properly handled)
    * </pre>
    */
   @Test
   public void testBlockChainWithNoReqsAgainstNonAcft() {
      TaskRevisionBuilder lBlockRevBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique()
            .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 );

      TaskTaskKey lBlockRev = lBlockRevBuilder.build();

      TaskKey lBlock1 = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iNonAcftInv )
            .build();

      TaskKey lBlock2 = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iNonAcftInv )
            .build();

      executePrecedure( lBlock1, iNonAcftInv );

      // Verify that both blocks are in the zip_task table.
      assertZipTasks( lBlock1, lBlock2 );
   }


   /**
    * Exercises the following scenario:
    *
    * <pre>
    *         Block Chain = BLOCK_1* (ACFT) -> BLOCK_2 (ACFT)
    *                            |                  |
    *                          REQ1 (ACFT)        REQ1 (ACFT)
    * 
    *         (* indicates target block)
    * 
    *       Expect zip_task to contain the following:
    * 
    * 
    * 
    *         All the blocks from the block chain (as BLOCK_1 is within the block chain).
    *         As well, REQ_1 (as REQ_1 is associated to BLOCK_1 and BLOCK_2).
    * </pre>
    */
   @Test
   public void testBlockChainWithReqAgainstAcft() {
      TaskRevisionBuilder lBlockRevBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique()
            .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
            .mapToRequirement( REQ_DEFN_1 );

      TaskTaskKey lBlockRev = lBlockRevBuilder.build();

      TaskKey lBlock1 = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iAcftInv ).build();

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( RefTaskClassKey.REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV );

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iAcftInv ).build();

      executePrecedure( lBlock1, iAcftInv );

      // Verify that both blocks and the requirement are in the zip_task table.
      assertZipTasks( lBlock1, lBlock2, lReq );
   }


   /**
    * Exercises the following scenario:
    *
    * <pre>
    *         Block Chain = BLOCK_1* (non-ACFT) -> BLOCK_2 (non-ACFT)
    *                            |                      |
    *                          REQ1 (non-ACFT)        REQ1 (non-ACFT)
    * 
    *         (* indicates target block)
    * 
    *       Expect zip_task to contain the following:
    *         All the blocks from the block chain (as BLOCK_1 is within the block chain).
    *         As well, REQ_1 (as REQ_1 is associated to BLOCK_1 and BLOCK_2).
    *         (note: ensuring non-ACFT inventory is properly handled)
    * </pre>
    */
   @Test
   public void testBlockChainWithReqAgainstNonAcft() {
      TaskRevisionBuilder lBlockRevBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique()
            .withBlockChainDesc( BLOCK_CHAIN_1 ).withConfigSlot( CONFIG_SLOT_1 )
            .mapToRequirement( REQ_DEFN_1 );

      TaskTaskKey lBlockRev = lBlockRevBuilder.build();

      TaskKey lBlock1 = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iNonAcftInv )
            .build();

      TaskKey lBlock2 = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iNonAcftInv )
            .build();

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( RefTaskClassKey.REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV );

      TaskKey lReq =
            new TaskBuilder().withTaskRevision( lReqRevBuilder ).onInventory( iNonAcftInv ).build();

      executePrecedure( lBlock1, iNonAcftInv );

      // Verify that both blocks and the requirement are in the zip_task table.
      assertZipTasks( lBlock1, lBlock2, lReq );
   }


   /**
    * Exercises the following scenario:
    *
    * <pre>
    *          BLOCK_1*
    *               |
    *             REQ_1
    * 
    *          BLOCK_2
    *               |
    *             REQ_1
    * 
    *         (* indicates target block)
    * 
    *       Expect zip_task to contain the following:
    *         BLOCK_1 and its associated REQ_1.
    *         As well, BLOCK_2 (as REQ_1 is also associated to BLOCK_2).
    * </pre>
    */
   @Test
   public void testBlocksReqExistsInAnotherBlock() {
      TaskRevisionBuilder lBlock1RevBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique()
            .mapToRequirement( REQ_DEFN_1 );

      TaskTaskKey lBlock1Rev = lBlock1RevBuilder.build();

      TaskKey lBlock1 = new TaskBuilder().withTaskRevision( lBlock1Rev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iAcftInv ).build();

      TaskRevisionBuilder lBlock2RevBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_2 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique()
            .mapToRequirement( REQ_DEFN_1 );

      TaskTaskKey lBlock2Rev = lBlock2RevBuilder.build();

      TaskKey lBlock2 = new TaskBuilder().withTaskRevision( lBlock2Rev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iAcftInv ).build();

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( RefTaskClassKey.REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV );

      TaskTaskKey lReqRev = lReqRevBuilder.build();

      TaskKey lReq1 = new TaskBuilder().withTaskRevision( lReqRev )
            .withTaskClass( RefTaskClassKey.REQ ).onInventory( iAcftInv ).build();

      executePrecedure( lBlock1, iAcftInv );

      // Verify that BLOCK_1 and REQ_1 are in the zip_task table. As well, BLOCK_2 is in the
      // zip_task table because REQ_1 is also in that block.
      assertZipTasks( lBlock1, lReq1, lBlock2 );
   }


   /**
    * Exercises the following scenario:
    *
    * <pre>
    *          BLOCK_1* (against ACFT)
    * 
    *         (* indicates target block)
    * 
    *       Expect zip_task to contain BLOCK_1.
    * </pre>
    */
   @Test
   public void testBlockWithNoReqsAgainstAcft() {
      TaskRevisionBuilder lBlockRevBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique();

      TaskTaskKey lBlockRev = lBlockRevBuilder.build();

      TaskKey lBlock = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iAcftInv ).build();

      executePrecedure( lBlock, iAcftInv );

      // Verify that the block is in the zip_task table.
      assertZipTasks( lBlock );
   }


   /**
    * Exercises the following scenario:
    *
    * <pre>
    *         BLOCK_1* (non-ACFT)
    * 
    *         (* indicates target block)
    * 
    *       Expect zip_task to contain BLOCK_1.
    *       (note: ensuring non-ACFT inventory is properly handled)
    * </pre>
    */
   @Test
   public void testBlockWithNoReqsAgainstNonAcft() {
      TaskRevisionBuilder lBlockRevBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique();

      TaskTaskKey lBlockRev = lBlockRevBuilder.build();

      TaskKey lBlock = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iNonAcftInv )
            .build();

      executePrecedure( lBlock, iNonAcftInv );

      // Verify that the block is in the zip_task table.
      assertZipTasks( lBlock );
   }


   /**
    * Exercises the following scenario:
    *
    * <pre>
    *           BLOCK_1* (ACFT)
    *                |
    *              REQ_1 (ACFT)
    * 
    *         (* indicates target block)
    * 
    *       Expect zip_task to contain BLOCK_1 and its associated REQ_1.
    * </pre>
    */
   @Test
   public void testBlockWithReqAgainstAcft() {
      // GOOD - Tests first and second select in union.

      TaskRevisionBuilder lBlockRevBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique()
            .mapToRequirement( REQ_DEFN_1 );

      TaskTaskKey lBlockRev = lBlockRevBuilder.build();

      TaskKey lBlock = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iAcftInv ).build();

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( RefTaskClassKey.REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV );

      TaskTaskKey lReqRev = lReqRevBuilder.build();

      TaskKey lReq = new TaskBuilder().withTaskRevision( lReqRev )
            .withTaskClass( RefTaskClassKey.REQ ).onInventory( iAcftInv ).build();

      executePrecedure( lBlock, iAcftInv );

      // Verify that the block and requirement are in the zip_task table.
      assertZipTasks( lBlock, lReq );
   }


   /**
    * Exercises the following scenario:
    *
    * <pre>
    *           BLOCK_1* (non-ACFT)
    *                |
    *              REQ_1 (non-ACFT)
    * 
    * 
    *         (* indicates target block)
    * 
    *       Expect zip_task to contain BLOCK_1 and its associated REQ_1.
    *       (note: ensuring non-ACFT inventory is properly handled)
    * </pre>
    */
   @Test
   public void testBlockWithReqAgainstNonAcft() {
      TaskRevisionBuilder lBlockRevBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique()
            .mapToRequirement( REQ_DEFN_1 );

      TaskTaskKey lBlockRev = lBlockRevBuilder.build();

      TaskKey lBlock = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iNonAcftInv )
            .build();

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( RefTaskClassKey.REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV );

      TaskTaskKey lReqRev = lReqRevBuilder.build();

      TaskKey lReq = new TaskBuilder().withTaskRevision( lReqRev )
            .withTaskClass( RefTaskClassKey.REQ ).onInventory( iNonAcftInv ).build();

      executePrecedure( lBlock, iNonAcftInv );

      // Verify that the block and requirement are in the zip_task table.
      assertZipTasks( lBlock, lReq );
   }


   /**
    * Exercises the following scenario:
    *
    * <pre>
    *        Block Chain = BLOCK_1 (rev1)* --> BLOCK_1 (rev2)
    *                           |                   |
    *                         REQ_1               REQ_1
    * 
    *         (* indicates target block)
    * 
    *       Expect zip_task to contain the following:
    *         All the blocks from the block chain (as BLOCK_1 is within the block chain).
    *         As well, REQ_1 (as REQ_1 is associated to both blocks).
    *         (note: ensuring blocks from both versions of the block defn are added)
    * </pre>
    */
   @Test
   public void testMultipleBlocksFromDifferentVersionsOfDefn() {
      TaskRevisionBuilder lBlock1RevBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique()
            .mapToRequirement( REQ_DEFN_1 );

      TaskTaskKey lBlock1Rev1 = lBlock1RevBuilder.build();

      TaskKey lBlock1 = new TaskBuilder().withTaskRevision( lBlock1Rev1 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iAcftInv ).build();

      TaskTaskKey lBlock1Rev2 = lBlock1RevBuilder.build();

      TaskKey lBlock2 = new TaskBuilder().withTaskRevision( lBlock1Rev2 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iAcftInv ).build();

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( RefTaskClassKey.REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV );

      TaskTaskKey lReqRev = lReqRevBuilder.build();

      TaskKey lReq = new TaskBuilder().withTaskRevision( lReqRev )
            .withTaskClass( RefTaskClassKey.REQ ).onInventory( iAcftInv ).build();

      executePrecedure( lBlock1, iAcftInv );

      // Verify that both blocks and the requirement are in the zip_task table.
      assertZipTasks( lBlock1, lBlock2, lReq );
   }


   /**
    * Exercises the following scenario:
    *
    * <pre>
    *         BLOCK_1* (against ACFT)
    * 
    *         BLOCK_2  (against ACFT)
    * 
    *         (* indicates target block)
    * 
    *       Expect zip_task to contain the following:
    *         Both blocks, as they are from the same block definition.
    * </pre>
    */
   @Test
   public void testMultipleBlocksFromSameDefnWithNoReqs() {
      TaskRevisionBuilder lBlockRevBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique();

      TaskTaskKey lBlockRev = lBlockRevBuilder.build();

      TaskKey lBlock1 = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iAcftInv ).build();

      TaskKey lBlock2 = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iAcftInv ).build();

      executePrecedure( lBlock1, iAcftInv );

      // Verify that both blocks are in the zip_task table, as they are both from the same block
      // definition.
      assertZipTasks( lBlock1, lBlock2 );
   }


   /**
    * Exercises the following scenario:
    *
    * <pre>
    *        Block Chain = BLOCK_1*   -->   BLOCK_2
    *                           |                |
    *                         REQ_1 (rev1)      REQ_1 (rev1)
    *                         REQ_2 (rev2)      REQ_2 (rev2)
    * 
    *         (* indicates target block)
    * 
    *       Expect zip_task to contain the following:
    *         All the blocks from block chain (as BLOCK_1 is within the block chain).
    *         As well, both requirements.
    *         (note: ensuring requirements from both versions of the requirement definition are added)
    * </pre>
    */
   @Test
   public void testMultipleBlocksWithReqsFromDifferentVersionsOfDefn() {
      TaskRevisionBuilder lBlockRevBuilder = new TaskRevisionBuilder().withTaskDefn( BLOCK_DEFN_1 )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).isUnique()
            .mapToRequirement( REQ_DEFN_1 );

      TaskTaskKey lBlockRev = lBlockRevBuilder.build();

      TaskKey lBlock = new TaskBuilder().withTaskRevision( lBlockRev )
            .withTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).onInventory( iAcftInv ).build();

      TaskRevisionBuilder lReqRevBuilder = new TaskRevisionBuilder().withTaskDefn( REQ_DEFN_1 )
            .withTaskClass( RefTaskClassKey.REQ ).withStatus( RefTaskDefinitionStatusKey.ACTV );

      TaskTaskKey lReqRev1 = lReqRevBuilder.build();

      TaskKey lReq1 = new TaskBuilder().withTaskRevision( lReqRev1 )
            .withTaskClass( RefTaskClassKey.REQ ).onInventory( iAcftInv ).build();

      TaskTaskKey lReqRev2 = lReqRevBuilder.build();

      TaskKey lReq2 = new TaskBuilder().withTaskRevision( lReqRev2 )
            .withTaskClass( RefTaskClassKey.REQ ).onInventory( iAcftInv ).build();

      executePrecedure( lBlock, iAcftInv );

      // Verify that all blocks and requirements are in the zip_task table.
      assertZipTasks( lBlock, lReq1, lReq2 );
   }


   @Before
   public void loadData() throws Exception {
      // Create a BLOCK task class.
      new RefTaskClass( TaskRevisionBuilder.TASK_CLASS_BLOCK ).create();

      // Create both an ACFT and non-ACFT inventory.
      iAcftInv = new InventoryBuilder().withClass( RefInvClassKey.ACFT ).allowSync().build();
      iNonAcftInv = new InventoryBuilder().withClass( RefInvClassKey.TRK ).allowSync().build();
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
    * Execute the stored procedure under test using the provided parameters.
    *
    * @param aTargetBlockKey
    *           block task key
    * @param aTargetInventoryKey
    *           inventory key
    */
   private void executePrecedure( TaskKey aTargetBlockKey, InventoryKey aTargetInventoryKey ) {

      StoredProcedureCall lStoredProcedureCall = StoredProcedureCall.getInstance();
      lStoredProcedureCall.updateZipFullBlockOnTree( ZIP_ID, aTargetBlockKey, aTargetInventoryKey );
   }
}
