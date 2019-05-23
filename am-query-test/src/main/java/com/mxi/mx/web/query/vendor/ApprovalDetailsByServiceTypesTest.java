
package com.mxi.mx.web.query.vendor;

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
import com.mxi.mx.core.key.OrgVendorServiceTypeKey;
import com.mxi.mx.core.key.RefServiceTypeKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This test case is used to test the ApprovalDetailsByServiceTypes.xml.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ApprovalDetailsByServiceTypesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            ApprovalDetailsByServiceTypesTest.class );
   }


   /**
    * This test case is used to test the ApprovalDetailsByServiceTypesTest.xml.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testApprovalDetailsByServiceTypes() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aOrgDbId", 0 );
      lArgs.add( "aOrgId", 1 );
      lArgs.add( "aVendorDbId", 4650 );
      lArgs.add( "aVendorId", 10004 );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert for No of Rows
      MxAssert.assertEquals( "Number of retrieved rows", 4, lDs.getRowCount() );

      lDs.next();
      testRow( lDs, new RefServiceTypeKey( "10:INSPECTION" ),
            new OrgVendorServiceTypeKey( "10:INSPECTION:0:1:4650:10004" ), "UNAPPRVD", null,
            "INSPECTION", null, "Company is blacklisted", "VNNLE" );
      lDs.next();
      testRow( lDs, new RefServiceTypeKey( "10:MOD" ),
            new OrgVendorServiceTypeKey( "10:MOD:0:1:4650:10004" ), "WARNING", "Check for Quality",
            "MOD", "30-Jul-2010 03:31:12", "Quality should not compromise", null );
      lDs.next();
      testRow( lDs, new RefServiceTypeKey( "10:OVERHAUL" ),
            new OrgVendorServiceTypeKey( "10:OVERHAUL:0:1:4650:10004" ), "WARNING",
            "Check for Quality", "OVERHAUL", "30-Jul-2010 03:31:12",
            "Quality should not compromise", null );
      lDs.next();
      testRow( lDs, new RefServiceTypeKey( "10:TEST" ),
            new OrgVendorServiceTypeKey( "10:TEST:0:1:4650:10004" ), "APPROVED", null, "TEST", null,
            null, null );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           the dataset.
    * @param aRefServiceTypeKey
    *           the ref service type key.
    * @param aOrgVendorServiceTypeKey
    *           the org vendor service type key.
    * @param aVendorStatusCD
    *           the vendor status cd.
    * @param aWarningLdesc
    *           the warning ldesc.
    * @param aServiceTypeCd
    *           the service type cd.
    * @param aExpiryDate
    *           the expiry date.
    * @param aUserStageNote
    *           the user stage note
    * @param aStageReasonCd
    *           the stage reason cd
    */
   private void testRow( DataSet aDs, RefServiceTypeKey aRefServiceTypeKey,
         OrgVendorServiceTypeKey aOrgVendorServiceTypeKey, String aVendorStatusCD,
         String aWarningLdesc, String aServiceTypeCd, String aExpiryDate, String aUserStageNote,
         String aStageReasonCd ) {

      // Check for the service type key
      MxAssert.assertEquals( "service_type_key", aRefServiceTypeKey,
            aDs.getString( "service_type_key" ) );

      // Check for the org vendor service type key
      MxAssert.assertEquals( "org_vendor_service_type_key", aOrgVendorServiceTypeKey,
            aDs.getString( "org_vendor_service_type_key" ) );

      // Check for the vendor status cd
      MxAssert.assertEquals( "vendor_status_cd", aVendorStatusCD,
            aDs.getString( "vendor_status_cd" ) );

      // Check for the warning ldesc
      MxAssert.assertEquals( "warning_ldesc", aWarningLdesc, aDs.getString( "warning_ldesc" ) );

      // Check for the service type cd
      MxAssert.assertEquals( "service_type_cd", aServiceTypeCd,
            aDs.getString( "service_type_cd" ) );

      // Check for the expiry date
      MxAssert.assertEquals( "expiry_dt", aExpiryDate, aDs.getString( "expiry_dt" ) );

      // Check for the user stage note
      MxAssert.assertEquals( "user_stage_note", aUserStageNote,
            aDs.getString( "user_stage_note" ) );

      // Check for the stage reason cd
      MxAssert.assertEquals( "stage_reason_cd", aStageReasonCd,
            aDs.getString( "stage_reason_cd" ) );
   }
}
