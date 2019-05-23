
package com.mxi.mx.web.query.part;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Test case for PartNoByInventory.qrx
 *
 * @author srengasamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class PartNoByInventoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), PartNoByInventoryTest.class );
   }


   /**
    * Tests the retrieval of Part no by Inventory.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testPartNoByInventory() throws Exception {

      DataSet lDataSet = this.execute( new InventoryKey( "4650:397240" ) );

      lDataSet.next();
      testRow( lDataSet, new PartNoKey( "5001:1075" ),
            "When receiving please verify that all caps and plugs are present" );
   }


   /**
    * Execute the query.
    *
    * @param aInventoryKey
    *           The Inventory Key
    *
    * @return dataSet result.
    */
   private DataSet execute( InventoryKey aInventoryKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventoryKey, new String[] { "aInventoryDbId", "aInventoryId" } );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Tests a row in the dataset.
    *
    * @param aDataSet
    *           the dataset
    * @param aPartNoKey
    *           the part no key.
    * @param aNote
    *           the part note.
    */
   private void testRow( DataSet aDataSet, PartNoKey aPartNoKey, String aNote ) {

      MxAssert.assertEquals( "part_key", aPartNoKey, aDataSet.getString( "part_key" ) );

      MxAssert.assertEquals( "note", aNote, aDataSet.getString( "note" ) );
   }
}
