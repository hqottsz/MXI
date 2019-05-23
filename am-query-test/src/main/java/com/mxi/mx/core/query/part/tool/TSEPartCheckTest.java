
package com.mxi.mx.core.query.part.tool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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


/**
 * Tests the TSEPartCheck Query
 *
 * @author dsewell
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class TSEPartCheckTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), TSEPartCheckTest.class );
   }


   private static final InventoryKey NO_PART_GROUP_INV = new InventoryKey( 4650, 3 );
   private static final InventoryKey COMHW_PART_GROUP_INV = new InventoryKey( 4650, 1 );
   private static final InventoryKey TSE_PART_GROUP_INV = new InventoryKey( 4650, 2 );
   private static final InventoryKey TWO_PART_GROUP_INV = new InventoryKey( 4650, 0 );

   private DataSet iDataSet;


   /**
    * Tests a tool with no part group
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testNoPartGroup() throws Exception {
      executeQuery( NO_PART_GROUP_INV );

      assertTrue( iDataSet.next() );

      assertEquals( false, iDataSet.getBoolean( "tse_part_bool" ) );
   }


   /**
    * Tests a tool with comhw part group
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testOneNonTSEPartGroup() throws Exception {
      executeQuery( COMHW_PART_GROUP_INV );

      assertTrue( iDataSet.next() );

      assertEquals( false, iDataSet.getBoolean( "tse_part_bool" ) );
   }


   /**
    * Tests a tool with tse part group
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testOneTSEPartGroup() throws Exception {
      executeQuery( TSE_PART_GROUP_INV );

      assertTrue( iDataSet.next() );

      assertEquals( true, iDataSet.getBoolean( "tse_part_bool" ) );
   }


   /**
    * Tests a tool with tse & comhw part groups
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testTwoPartGroups() throws Exception {
      executeQuery( TWO_PART_GROUP_INV );

      assertTrue( iDataSet.next() );

      assertEquals( true, iDataSet.getBoolean( "tse_part_bool" ) );
   }


   /**
    * Executes the query with the given inventory
    *
    * @param aInventory
    *           The inventory argument for the query
    */
   private void executeQuery( InventoryKey aInventory ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInvNoDbId", "aInvNoId" );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
