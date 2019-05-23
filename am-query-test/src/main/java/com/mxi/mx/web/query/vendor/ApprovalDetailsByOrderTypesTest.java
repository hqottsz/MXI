
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
import com.mxi.mx.core.key.OrgVendorPoTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This test case is used to test the ApprovalDetailsByOrderTypes.xml.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ApprovalDetailsByOrderTypesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            ApprovalDetailsByOrderTypesTest.class );
   }


   /**
    * This test case is used to test the ApprovalDetailsByOrderTypes.xml.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testApprovalDetailsByOrderTypes() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aOrgDbId", 0 );
      lArgs.add( "aOrgId", 1 );
      lArgs.add( "aVendorDbId", 4650 );
      lArgs.add( "aVendorId", 10004 );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert for No of Rows
      MxAssert.assertEquals( "Number of retrieved rows", 6, lDs.getRowCount() );

      lDs.next();
      testRow( lDs, new RefPoTypeKey( "0:PURCHASE" ),
            new OrgVendorPoTypeKey( "0:PURCHASE:0:1:4650:10004" ), "UNAPPRVD", null, "PURCHASE",
            null, "This vendor Po type is Unapproved from serviceing.", "VNSUPP" );
      lDs.next();
      testRow( lDs, new RefPoTypeKey( "0:REPAIR" ),
            new OrgVendorPoTypeKey( "0:REPAIR:0:1:4650:10004" ), "UNAPPRVD", null, "REPAIR", null,
            "This vendor Po type is Unapproved from serviceing.", "VNSUPP" );

      lDs.next();
      testRow( lDs, new RefPoTypeKey( "0:BORROW" ),
            new OrgVendorPoTypeKey( "0:BORROW:0:1:4650:10004" ), "UNAPPRVD", null, "BORROW", null,
            "This vendor Po type is Unapproved from serviceing.", "VNSUPP" );
      lDs.next();
      testRow( lDs, new RefPoTypeKey( "0:CONSIGN" ),
            new OrgVendorPoTypeKey( "0:CONSIGN:0:1:4650:10004" ), "UNAPPRVD", null, "CONSIGN", null,
            "This vendor Po type is Unapproved from serviceing.", "VNSUPP" );
      lDs.next();
      testRow( lDs, new RefPoTypeKey( "0:CSGNXCHG" ),
            new OrgVendorPoTypeKey( "0:CSGNXCHG:0:1:4650:10004" ), "UNAPPRVD", null, "CSGNXCHG",
            null, "This vendor Po type is Unapproved from serviceing.", "VNSUPP" );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           the dataset.
    * @param aRefPoTypeKey
    *           the ref po type key.
    * @param aOrgVendorPoTypeKey
    *           the org vendor po type key.
    * @param aVendorStatusCD
    *           the vendor status cd.
    * @param aWarningLdesc
    *           the warning ldesc.
    * @param aPoTypeCd
    *           the po type cd.
    * @param aExpiryDate
    *           the expiry date.
    * @param aUserStageNote
    *           the user stage note
    * @param aStageReasonCd
    *           the stage reason cd
    */
   private void testRow( DataSet aDs, RefPoTypeKey aRefPoTypeKey,
         OrgVendorPoTypeKey aOrgVendorPoTypeKey, String aVendorStatusCD, String aWarningLdesc,
         String aPoTypeCd, String aExpiryDate, String aUserStageNote, String aStageReasonCd ) {

      // Check for the po type key
      MxAssert.assertEquals( "po_type_key", aRefPoTypeKey, aDs.getString( "po_type_key" ) );

      // Check for the org vendor po type key
      MxAssert.assertEquals( "org_vendor_po_type_key", aOrgVendorPoTypeKey,
            aDs.getString( "org_vendor_po_type_key" ) );

      // Check for the vendor status cd
      MxAssert.assertEquals( "vendor_status_cd", aVendorStatusCD,
            aDs.getString( "vendor_status_cd" ) );

      // Check for the warning ldesc
      MxAssert.assertEquals( "warning_ldesc", aWarningLdesc, aDs.getString( "warning_ldesc" ) );

      // Check for the po type cd
      MxAssert.assertEquals( "po_type_cd", aPoTypeCd, aDs.getString( "po_type_cd" ) );

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
