
package com.mxi.mx.web.query.vendor;

import java.text.DateFormat;
import java.util.Date;

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
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.web.query.vendor.VendorSearch
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class VendorSearchTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), VendorSearchTest.class );
   }


   /**
    * Tests basic vendor search
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testBasicSearch() throws Exception {
      DataSet lDataSet;

      lDataSet = execute( null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new VendorKey( 4650, 1 ), "VEND1", // VendorCD,
            "Vendor 1", // VendorName,
            "PURCHASE", // aVendorTypeCD,
            null, // aCertCD,
            DateFormat.getDateInstance().parse( "August 5, 2009" ), // aCertExpiryDate,
            "Vendor 1 Note" // aVendorNote
      );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new VendorKey( 4650, 2 ), "VEND2", // VendorCD,
            "Vendor 2", // VendorName,
            "PURCHASE", // aVendorTypeCD,
            null, // aCertCD,
            DateFormat.getDateInstance().parse( "August 5, 2009" ), // aCertExpiryDate,
            null // aVendorNote
      );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new VendorKey( 4650, 3 ), "VEND3", // VendorCD,
            "Vendor 3", // VendorName,
            "PURCHASE", // aVendorTypeCD,
            null, // aCertCD,
            DateFormat.getDateInstance().parse( "August 5, 2009" ), // aCertExpiryDate,
            "Vendor 3 Note" // aVendorNote
      );
      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * Tests vendor search by organization
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testVendorSearchByOrganization() throws Exception {
      DataSet lDataSet;

      // Test Default Approvals
      lDataSet = execute( new OrgKey( 4650, 2 ), null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new VendorKey( 4650, 1 ), "VEND1", // VendorCD,
            "Vendor 1", // VendorName,
            "PURCHASE", // aVendorTypeCD,
            null, // aCertCD,
            DateFormat.getDateInstance().parse( "August 5, 2009" ), // aCertExpiryDate,
            "Org 2 Vendor 1 Note" // aVendorNote
      );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new VendorKey( 4650, 2 ), "VEND2", // VendorCD,
            "Vendor 2", // VendorName,
            "PURCHASE", // aVendorTypeCD,
            null, // aCertCD,
            DateFormat.getDateInstance().parse( "August 5, 2009" ), // aCertExpiryDate,
            null // aVendorNote
      );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new VendorKey( 4650, 3 ), "VEND3", // VendorCD,
            "Vendor 3", // VendorName,
            "PURCHASE", // aVendorTypeCD,
            null, // aCertCD,
            DateFormat.getDateInstance().parse( "August 5, 2009" ), // aCertExpiryDate,
            "Org 2 Vendor 3 Note" // aVendorNote (overwritten by org_org_vendor)
      );
      MxAssert.assertFalse( lDataSet.next() );

      // Test Same Approvals
      lDataSet = execute( new OrgKey( 4650, 2 ), null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new VendorKey( 4650, 1 ), "VEND1", // VendorCD,
            "Vendor 1", // VendorName,
            "PURCHASE", // aVendorTypeCD,
            null, // aCertCD,
            DateFormat.getDateInstance().parse( "August 5, 2009" ), // aCertExpiryDate,
            "Org 2 Vendor 1 Note" // aVendorNote
      );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new VendorKey( 4650, 2 ), "VEND2", // VendorCD,
            "Vendor 2", // VendorName,
            "PURCHASE", // aVendorTypeCD,
            null, // aCertCD,
            DateFormat.getDateInstance().parse( "August 5, 2009" ), // aCertExpiryDate,
            null // aVendorNote
      );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new VendorKey( 4650, 3 ), "VEND3", // VendorCD,
            "Vendor 3", // VendorName,
            "PURCHASE", // aVendorTypeCD,
            null, // aCertCD,
            DateFormat.getDateInstance().parse( "August 5, 2009" ), // aCertExpiryDate,
            "Org 2 Vendor 3 Note" // aVendorNote
      );
      MxAssert.assertFalse( lDataSet.next() );

      // Test Different Approvals
      lDataSet = execute( new OrgKey( 4650, 3 ), null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new VendorKey( 4650, 1 ), "VEND1", // VendorCD,
            "Vendor 1", // VendorName,
            "PURCHASE", // aVendorTypeCD,
            null, // aCertCD,
            DateFormat.getDateInstance().parse( "August 5, 2009" ), // aCertExpiryDate,
            "Org 3 Vendor 1 Note" // aVendorNote
      );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new VendorKey( 4650, 2 ), "VEND2", // VendorCD,
            "Vendor 2", // VendorName,
            "PURCHASE", // aVendorTypeCD,
            null, // aCertCD,
            DateFormat.getDateInstance().parse( "August 5, 2009" ), // aCertExpiryDate,
            "Org 3 Vendor 2 Note" // aVendorNote
      );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new VendorKey( 4650, 3 ), "VEND3", // VendorCD,
            "Vendor 3", // VendorName,
            "PURCHASE", // aVendorTypeCD,
            null, // aCertCD,
            DateFormat.getDateInstance().parse( "August 5, 2009" ), // aCertExpiryDate,
            null // aVendorNote
      );
      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * Tests a row in the dataset.
    *
    * @param aDs
    *           The dataset
    * @param aVendorKey
    *           The vendor key
    * @param aVendorCD
    *           The vendor code
    * @param aVendorName
    *           The vendor name
    * @param aVendorTypeCD
    *           The vendor type
    * @param aCertCD
    *           The vendor certificate code
    * @param aCertExpiryDate
    *           The vendor certificate expiry
    * @param aVendorNote
    *           The vendor note
    */
   private void assertRow( DataSet aDs, VendorKey aVendorKey, String aVendorCD, String aVendorName,
         String aVendorTypeCD, String aCertCD, Date aCertExpiryDate, String aVendorNote ) {

      MxAssert.assertEquals( "vendor_key", aVendorKey,
            new VendorKey( aDs.getString( "vendor_key" ) ) );

      MxAssert.assertEquals( "vendor_cd", aVendorCD, aDs.getString( "vendor_cd" ) );

      MxAssert.assertEquals( "vendor_name", aVendorName, aDs.getString( "vendor_name" ) );

      MxAssert.assertEquals( "vendor_type_cd", aVendorTypeCD, aDs.getString( "vendor_type_cd" ) );

      MxAssert.assertEquals( "cert_cd", aCertCD, aDs.getString( "cert_cd" ) );

      if ( aCertExpiryDate == null ) {
         MxAssert.assertNull( "cert_expiry_dt", aDs.getObject( "cert_expiry_dt" ) );
      } else {
         MxAssert.assertNotNull( "cert_expiry_dt", aDs.getObject( "cert_expiry_dt" ) );
         MxAssert.assertEquals( "cert_expiry_dt", aCertExpiryDate,
               aDs.getDate( "cert_expiry_dt" ) );
      }

      MxAssert.assertEquals( "vendor_note", aVendorNote, aDs.getString( "vendor_note" ) );
   }


   /**
    * Execute the query
    *
    * @param aOrgKey
    *           Filter by Organization Key
    * @param aVendorKey
    *           Filter by Vendor Key
    *
    * @return the result
    */
   private DataSet execute( OrgKey aOrgKey, VendorKey aVendorKey ) {

      // Build arguments
      DataSetArgument lArgs = new DataSetArgument();

      if ( aOrgKey != null ) {
         lArgs.add( aOrgKey, new String[] { "aOrgDbId", "aOrgId" } );
      }

      if ( aVendorKey != null ) {
         lArgs.add( aVendorKey, new String[] { "aVendorDbId", "aVendorId" } );
      }

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
