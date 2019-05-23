
package com.mxi.mx.web.query.todolist;

import static org.junit.Assert.assertEquals;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author Libin Cai
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetWarehousesByHrTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetWarehousesByHrTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where Invoices in the date range are retrieved.
    *
    * <ol>
    * <li>Query for all Invoices in date range.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGetWarehouseByHr() throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aHrDbId", 4650 );
      lArgs.add( "aHrId", 6000073 );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();

      MxAssert.assertEquals( "4650:1000011", iDataSet.getString( "location_key" ) );
      MxAssert.assertEquals( "BOS/S-LN-BOS/SRVSTORE", iDataSet.getString( "loc_cd" ) );
      MxAssert.assertEquals( "BOS Serviceable Store", iDataSet.getString( "loc_name" ) );
   }
}
