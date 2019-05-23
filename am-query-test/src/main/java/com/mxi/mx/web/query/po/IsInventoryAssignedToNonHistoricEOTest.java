
/**
 *
 */
package com.mxi.mx.web.query.po;

import java.io.InputStream;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the IsInventoryAssignedToNonHistoricEO.qrx
 *
 * @author okamenskova
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IsInventoryAssignedToNonHistoricEOTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            IsInventoryAssignedToNonHistoricEOTest.class );
   }


   /**
    * Returns the value of the data file property.
    *
    * @return the value of the data file property.
    */
   public InputStream getDataFileInputStream() {
      return getClass().getResourceAsStream( getClass().getSimpleName() + ".xml" );
   }


   /**
    * Tests the query under the following data setup for a inventory:
    *
    * <ol>
    * <li>Fully recieved PO - non historic</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryFullyRecievedPO() throws Exception {

      // execute the query
      DataSet lDs = execute( new InventoryKey( 4650, 1 ) );

      // There should be no row returned
      MxAssert.assertEquals( "Number of retrieved rows", 0, lDs.getRowCount() );
   }


   /**
    * Tests the query under the following data setup for a inventory:
    *
    * <ol>
    * <li>Historic PO</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryHistoricPO() throws Exception {

      // execute the query
      DataSet lDs = execute( new InventoryKey( 4650, 2 ) );

      // There should be no row returned
      MxAssert.assertEquals( "Number of retrieved rows", 0, lDs.getRowCount() );
   }


   /**
    * Tests the query under the following data setup for a inventory:
    *
    * <ol>
    * <li>Historic PO</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryNonHistoricPO() throws Exception {

      // execute the query
      DataSet lDs = execute( new InventoryKey( 4650, 3 ) );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aInventory
    *           the inventory key
    *
    * @return the result
    */
   private DataSet execute( InventoryKey aInventory ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInvNoDbId", "aInvNoId" );

      // Execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      return lDs;
   }
}
