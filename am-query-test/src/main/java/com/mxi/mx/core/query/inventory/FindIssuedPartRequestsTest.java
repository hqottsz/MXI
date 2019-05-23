
package com.mxi.mx.core.query.inventory;

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
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.query.inventory.FindIssuedPartRequests
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FindIssuedPartRequestsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), FindIssuedPartRequestsTest.class );
   }


   /**
    * Inventory has an issued adhoc part request. The query returns one rows with the adhoc part
    * request
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testIssuedAdhocPR() throws Exception {
      DataSet lDataSet;

      // Passed in batch inventory record different from the one that was issued (linked by barcode)
      lDataSet = execute( new InventoryKey( "4650:8" ) );

      MxAssert.assertTrue( lDataSet.next() );

      MxAssert.assertEquals( "req_part_key", new PartRequestKey( "4650:6" ),
            lDataSet.getKey( PartRequestKey.class, "req_part_key" ) );
   }


   /**
    * Inventory has a part request and it is issued to a historic task . The query returns no rows
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testIssuedPRTaskComplete() throws Exception {
      DataSet lDataSet;

      lDataSet = execute( new InventoryKey( "4650:1" ) );

      MxAssert.assertTrue( lDataSet.isEmpty() );
   }


   /**
    * Inventory has a part request and it is issued to a non historic task that does not have a
    * labour row that installed the inventory. The query returns one row with the part request key
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testIssuedPRTaskNotComplete() throws Exception {
      DataSet lDataSet;

      lDataSet = execute( new InventoryKey( "4650:2" ) );

      MxAssert.assertTrue( lDataSet.next() );

      MxAssert.assertEquals( "req_part_key", new PartRequestKey( "4650:2" ),
            lDataSet.getKey( PartRequestKey.class, "req_part_key" ) );
   }


   /**
    * Inventory has a part request and it is issued to a non historic task that does not have a
    * labour row that installed the inventory. The query returns one row with the part request key
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testIssuedPRTaskNotCompleteBatchInv() throws Exception {
      DataSet lDataSet;

      lDataSet = execute( new InventoryKey( "4650:6" ) );

      MxAssert.assertTrue( lDataSet.next() );

      MxAssert.assertEquals( "req_part_key", new PartRequestKey( "4650:5" ),
            lDataSet.getKey( PartRequestKey.class, "req_part_key" ) );
   }


   /**
    * Inventory has a part request and it is issued to a non historic task that has a completed
    * labour row that installed the inventory. The query returns no rows
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testIssuedPRTaskNotCompleteLabourComplete() throws Exception {
      DataSet lDataSet;

      lDataSet = execute( new InventoryKey( "4650:5" ) );

      MxAssert.assertTrue( lDataSet.isEmpty() );
   }


   /**
    * Inventory does not have a part request. No rows should be returned
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNoPR() throws Exception {
      DataSet lDataSet;

      lDataSet = execute( new InventoryKey( "4650:3" ) );

      MxAssert.assertTrue( lDataSet.isEmpty() );
   }


   /**
    * Inventory has a Part Request but it is not issued. No rows should be returned
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testPRNotIssued() throws Exception {
      DataSet lDataSet;

      lDataSet = execute( new InventoryKey( "4650:4" ) );

      MxAssert.assertTrue( lDataSet.isEmpty() );
   }


   /**
    * Execute the query
    *
    * @param aInvNoKey
    *           The Inventory Key
    *
    * @return the result
    */
   private DataSet execute( InventoryKey aInvNoKey ) {

      // Build arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aInvNoKey, new String[] { "aInvNoDbId", "aInvNoId" } );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
