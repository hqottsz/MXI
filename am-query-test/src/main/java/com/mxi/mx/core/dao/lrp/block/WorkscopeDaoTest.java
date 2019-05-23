
package com.mxi.mx.core.dao.lrp.block;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.core.dao.lrp.DaoTestCase;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;
import com.mxi.mx.model.lrp.Block;


/**
 * DOCUMENT_ME
 *
 * @author slevert
 */
public class WorkscopeDaoTest extends DaoTestCase {

   InventoryKey iInventoryKey;

   private WorkscopeDao iDao;


   @Override
   @Before
   public void loadData() throws Exception {
      super.loadData();
      iDao = iFactory.getWorkscopeDao();
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testDeleteAllWorkscopeForEvent() throws Exception {
      IdKey lPlanKey = new IdKeyImpl( 4650, 4 );

      Collection<Block> lBlocks = iDao.getBlocksForPlan( lPlanKey ).values();
      assertNotNull( lBlocks );
      assertEquals( 9, lBlocks.size() );

      // assert that all blocks have their previous blocks cleared
      IdKey lEventKey = new IdKeyImpl( 4650, 1793 );

      boolean lBlockFound = false;
      for ( Block lBlock : lBlocks ) {
         assertNotNull( lBlock.getPreviousBlock() );
         if ( ( lBlock.getLrpEventRefKey().getDbId() == lEventKey.getDbId() )
               && ( lBlock.getLrpEventRefKey().getId() == lEventKey.getId() ) ) {
            lBlockFound = true;
         }
      }

      // make sure we have the block we are going to delete
      assertTrue( lBlockFound );

      // delete one event that has one block in the workscope
      iDao.deleteAllWorkscopeForEvent( lEventKey );

      lBlocks = iDao.getBlocksForPlan( lPlanKey ).values();
      assertNotNull( lBlocks );
      assertEquals( 8, lBlocks.size() );

      // make sure we no longer have the block we wanted delete
      for ( Block lBlock : lBlocks ) {
         assertNotSame( lEventKey.getDbId(), lBlock.getLrpEventRefKey().getDbId() );
         assertNotSame( lEventKey.getId(), lBlock.getLrpEventRefKey().getId() );
      }
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testDeleteAllWorkscopeForPlan() throws Exception {
      IdKey lPlanKey = new IdKeyImpl( 4650, 4 );

      Collection<Block> lBlocks = iDao.getBlocksForPlan( lPlanKey ).values();
      assertNotNull( lBlocks );
      assertEquals( 9, lBlocks.size() );

      // assert that all blocks have their previous blocks
      for ( Block lBlock : lBlocks ) {
         assertNotNull( lBlock.getPreviousBlock() );
      }

      iDao.removePreviousBlocksForPlan( lPlanKey );

      lBlocks = iDao.getBlocksForPlan( lPlanKey ).values();
      assertNotNull( lBlocks );
      assertEquals( 9, lBlocks.size() );

      // assert that all blocks have their previous blocks
      for ( Block lBlock : lBlocks ) {
         assertNull( lBlock.getPreviousBlock() );
      }

      iDao.deleteAllWorkscopeForPlan( lPlanKey );

      lBlocks = iDao.getBlocksForPlan( lPlanKey ).values();
      assertNotNull( lBlocks );
      assertEquals( 0, lBlocks.size() );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testRemovePreviousBlocksForPlan() throws Exception {
      IdKey lPlanKey = new IdKeyImpl( 4650, 4 );

      Collection<Block> lBlocks = iDao.getBlocksForPlan( lPlanKey ).values();
      assertNotNull( lBlocks );
      assertEquals( 9, lBlocks.size() );

      // assert that all blocks have their previous blocks cleared
      for ( Block lBlock : lBlocks ) {
         assertNotNull( lBlock.getPreviousBlock() );
      }

      iDao.removePreviousBlocksForPlan( lPlanKey );

      lBlocks = iDao.getBlocksForPlan( lPlanKey ).values();
      assertNotNull( lBlocks );
      assertEquals( 9, lBlocks.size() );

      // assert that all blocks have their previous blocks cleared
      for ( Block lBlock : lBlocks ) {
         assertEquals( null, lBlock.getPreviousBlock() );
      }
   }
}
