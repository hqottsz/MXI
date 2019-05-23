
package com.mxi.mx.core.query.plsql;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefOwnerTypeKey;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.core.unittest.table.inv.InvInv;


/**
 * This test class covers the functionality of the setKitOwnership procedure in the kit_pkg
 *
 * @author cdaley
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class SetKitOwnershipTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), SetKitOwnershipTest.class );
   }


   /**
    * This test case covers when the sub inventory for a kit has only a single owner
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testDistinctOwner() throws Exception {
      InventoryKey lKitNoKey = new InventoryKey( 1, 1 );

      // ACTION: Execute the proceudre
      int lResult = execute( lKitNoKey );

      MxAssert.assertEquals( 1, lResult );

      // assert that the common owner/type is used
      InvInv lInvInv = new InvInv( lKitNoKey );
      lInvInv.assertOwner( new OwnerKey( 1, 1 ) );
      lInvInv.assertOwnerTypeCd( RefOwnerTypeKey.EXCHRCVD.getCd() );
   }


   /**
    * This test case covers when the sub inventory for a kit has multiple owners
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testMultipleOwner() throws Exception {
      InventoryKey lKitNoKey = new InventoryKey( 2, 1 );

      // ACTION: Execute the proceudre
      int lResult = execute( lKitNoKey );

      MxAssert.assertEquals( 1, lResult );

      // assert that the default local owner is used, type is OTHER
      InvInv lInvInv = new InvInv( lKitNoKey );
      lInvInv.assertOwner( new OwnerKey( 1, 4 ) );
      lInvInv.assertOwnerTypeCd( RefOwnerTypeKey.OTHER.getCd() );
   }


   /**
    * This test case covers when the sub inventory for a kit has multiple owner types
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testMultipleOwnerType() throws Exception {
      InventoryKey lKitNoKey = new InventoryKey( 3, 1 );

      // ACTION: Execute the proceudre
      int lResult = execute( lKitNoKey );

      MxAssert.assertEquals( 1, lResult );

      // assert that the default local owner is used, type is OTHER
      InvInv lInvInv = new InvInv( lKitNoKey );
      lInvInv.assertOwner( new OwnerKey( 1, 4 ) );
      lInvInv.assertOwnerTypeCd( RefOwnerTypeKey.OTHER.getCd() );
   }


   /**
    * This test case covers when there are no sub-inventory
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNoOwnsers() throws Exception {
      InventoryKey lKitNoKey = new InventoryKey( 4, 1 );

      // ACTION: Execute the proceudre
      int lResult = execute( lKitNoKey );

      MxAssert.assertEquals( 1, lResult );

      // assert that the default local owner is used, type is OTHER
      InvInv lInvInv = new InvInv( lKitNoKey );
      lInvInv.assertOwner( new OwnerKey( 1, 4 ) );
      lInvInv.assertOwnerTypeCd( RefOwnerTypeKey.OTHER.getCd() );
   }


   /**
    * Execute the function.
    *
    * @param aInventoryKey
    *           The root of a Task Tree.
    *
    * @return the calculated Planning Yield Date.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private int execute( InventoryKey aInventoryKey ) throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventoryKey, new String[] { "aKitNoDbId", "aKitNoId" } );

      // Execute the query
      return QueryExecutor.executePlsql( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs ).getInteger( "aReturn" );
   }
}
