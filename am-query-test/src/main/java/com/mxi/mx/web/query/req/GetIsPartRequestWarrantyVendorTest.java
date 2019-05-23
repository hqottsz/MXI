
package com.mxi.mx.web.query.req;

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
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.VendorKey;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetIsPartRequestWarrantyVendorTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetIsPartRequestWarrantyVendorTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where the main warranty vendor exists for Part Request which is linked to
    * warranty evaluation results.
    *
    * <ol>
    * <li>Query by Part Request id and vendor id.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testMainVendorExists() throws Exception {
      execute( new PartRequestKey( 4650, 1000 ), new VendorKey( 4650, 6000 ) );
      assertEquals( 1, iDataSet.getRowCount() );
   }


   /**
    * Test the case where the warranty vendor does not exists for Part Request which is linked to
    * warranty evaluation results.
    *
    * <ol>
    * <li>Query by Part Request id and vendor id.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testVendorDoesNotExists() throws Exception {
      execute( new PartRequestKey( 4650, 1001 ), new VendorKey( 4650, 6005 ) );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where the warranty vendor exists for Part Request which is linked to warranty
    * evaluation results.
    *
    * <ol>
    * <li>Query by Part Request id and vendor id.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testVendorExists() throws Exception {
      execute( new PartRequestKey( 4650, 1001 ), new VendorKey( 4650, 6001 ) );
      assertEquals( 1, iDataSet.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aPartReq
    *           the part request key.
    * @param aVendor
    *           the vendor key.
    */
   private void execute( PartRequestKey aPartReq, VendorKey aVendor ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aPartReq, "aReqPartDbId", "aReqPartId" );
      lArgs.add( aVendor, "aVendorDbId", "aVendorId" );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
