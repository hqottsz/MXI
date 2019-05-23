
package com.mxi.mx.report.query.inventory;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.key.InventoryKey;


/**
 * This class tests the query IncompleteKitLines.qrx
 *
 * @author srengasamy
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class IncompleteKitLinesTest {

   @ClassRule
   public static final DatabaseConnectionRule sDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), IncompleteKitLinesTest.class );
   }


   /**
    * Tests the retrieval of the Inventory Kit Lines.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testInventoryKitLines() throws Exception {
      QuerySet lQuerySet = this.execute( new InventoryKey( 4650, 391148 ) );

      Assert.assertEquals( "Number of retrieved rows", 1, lQuerySet.getRowCount() );

      // user update status
      Assert.assertTrue( lQuerySet.next() );
      testRow( lQuerySet, "021A (STG 1 BLADE)", "9267M28P09 (STG 1 BLADE)", "1 EA" );

      Assert.assertFalse( lQuerySet.next() );
   }


   /**
    * This method executes the query in InventoryKitLines.qrx
    *
    * @param aInventoryKey
    *           The inventory key.
    *
    * @return The QuerySet after execution.
    */
   private QuerySet execute( InventoryKey aInventoryKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aInventoryKey, "aInvNoDbId", "aInvNoId" );

      return QueryExecutor.executeQuery( getClass(), lDataSetArgument );
   }


   /**
    * Assert the Row Coloumns.
    *
    * @param aQuerySet
    *           the QuerySet
    * @param aPartGroup
    *           the part group
    * @param aPartNumber
    *           the part number
    * @param aPickQty
    *           the pick qty
    */
   private void testRow( QuerySet aQuerySet, String aPartGroup, String aPartNumber,
         String aPickQty ) {
      Assert.assertEquals( "part_group", aPartGroup, aQuerySet.getString( "part_group" ) );
      Assert.assertEquals( "part_number", aPartNumber, aQuerySet.getString( "part_number" ) );
      Assert.assertEquals( "pick_qty", aPickQty, aQuerySet.getString( "pick_qty" ) );
   }
}
