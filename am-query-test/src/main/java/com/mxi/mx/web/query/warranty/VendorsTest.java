
package com.mxi.mx.web.query.warranty;

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
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.key.WarrantyContractKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the Vendors query file.
 *
 * @author akash
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class VendorsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), VendorsTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test Search Results of Vendors query
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testVendors() throws Exception {
      execute( new WarrantyContractKey( "4650:1" ) );
      assertEquals( 2, iDataSet.getRowCount() );

      while ( iDataSet.next() ) {
         if ( iDataSet.getString( "vendor_key" ) == "4650:1" ) {
            testRow( new VendorKey( "4650:1" ), "MXI Tech. (MXI)", "REPAIR", "APPROVED" );
         }

         if ( iDataSet.getString( "vendor_key" ) == "4650:2" ) {
            testRow( new VendorKey( "4650:2" ), "Sample Vendor Name (VEN)", "PURCHASE",
                  "APPROVED" );
         }
      }
   }


   /**
    * Execute the query.
    *
    * @param aWarrantyContract
    *           Warranty Contract Key
    */
   private void execute( WarrantyContractKey aWarrantyContract ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aWarrantyContract, new String[] { "aWarrantyDefnDbId", "aWarrantyDefnId" } );
      lArgs.add( "aOrgDbId", 0 );
      lArgs.add( "aOrgId", 1 );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aVendor
    *           Vendor Key
    * @param aVendorCdName
    *           Vendor Code Name
    * @param aVendorTypeCd
    *           Vendor Type Code
    * @param aVendorStatusCd
    *           Vendor Status Code
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( VendorKey aVendor, String aVendorCdName, String aVendorTypeCd,
         String aVendorStatusCd ) throws Exception {
      MxAssert.assertEquals( aVendor.toString(), iDataSet.getString( "vendor_key" ) );
      MxAssert.assertEquals( aVendorCdName, iDataSet.getString( "vendor_cd_name" ) );
      MxAssert.assertEquals( aVendorTypeCd, iDataSet.getString( "vendor_type_cd" ) );
   }
}
