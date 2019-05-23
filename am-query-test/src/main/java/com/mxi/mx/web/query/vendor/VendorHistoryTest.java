
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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This test case is used to test the VendorHistoryTest.xml.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class VendorHistoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), VendorHistoryTest.class );
   }


   /**
    * This test case is used to test the VendorHistoryTest.xml.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testOverCompleteKits() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aIncludeAllOrg", 1 );
      lArgs.add( "aVendorDbId", 4650 );
      lArgs.add( "aVendorId", 10004 );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert for No of Rows
      MxAssert.assertEquals( "Number of retrieved rows:", 6, lDs.getRowCount() );

      lDs.next();
      testRow( lDs, "MXI (Mxi Technologies)", "BORROW", "WARNING", "sathish rengasamy", null,
            "Verify the products" );
      lDs.next();
      testRow( lDs, "MXI (Mxi Technologies)", "CONSIGN", "UNAPPRVD", "sathish rengasamy", "NN",
            "Vendor was unapproved." );
      lDs.next();
      testRow( lDs, "MXI (Mxi Technologies)", "TEST", "WARNING", "sathish rengasamy", null,
            "A warning was issued for the vendor." );

      lDs.next();
      testRow( lDs, "MXI (Mxi Technologies)", "EXCHANGE", "UNAPPRVD", "sathish rengasamy", "NN",
            "Vendor was unapproved." );
      lDs.next();
      testRow( lDs, "MXI (Mxi Technologies)", "OVERHAUL", "UNAPPRVD", "sathish rengasamy", "PP",
            "Exclude this product" );

      lDs.next();
      testRow( lDs, "MXI (Mxi Technologies)", "CSGNXCHG", "WARNING", "sathish rengasamy", null,
            "Verify the products" );
   }


   /**
    * Test the case where vendor history is a combination of approvals with types and legacy history
    * where there is no organization nor types.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testVendorHistory() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aIncludeAllOrg", 1 );
      lArgs.add( "aVendorDbId", 4650 );
      lArgs.add( "aVendorId", 1 );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert for No of Rows
      MxAssert.assertEquals( "Number of retrieved rows:", 3, lDs.getRowCount() );

      lDs.next();
      testRow( lDs, "MXI (Mxi Technologies)", "TEST", "WARN", "Ling Soh", "WARN",
            "Vendor approved with warning." );

      lDs.next();
      testRow( lDs, "MXI (Mxi Technologies)", "EXCHANGE", "UNAPPRVE", "Ling Soh", "UNAPPRVE",
            "Vendor unapproved." );

      lDs.next();
      testRow( lDs, null, null, "APPROVE", "Ling Soh", "APPROVE", "Vendor approved." );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           the dataset.
    * @param aOrganization
    *           the ref service type key.
    * @param aType
    *           the org vendor service type key.
    * @param aStatus
    *           the vendor status cd.
    * @param aUserLabel
    *           the warning ldesc.
    * @param aReason
    *           the stage reason cd
    * @param aUserStageNote
    *           the user stage note
    */
   private void testRow( DataSet aDs, String aOrganization, String aType, String aStatus,
         String aUserLabel, String aReason, String aUserStageNote ) {

      // Check for the organization
      MxAssert.assertEquals( "organization", aOrganization, aDs.getString( "organization" ) );

      // Check for the Type
      MxAssert.assertEquals( "type_cd", aType, aDs.getString( "type_cd" ) );

      // Check for the status
      MxAssert.assertEquals( "status", aStatus, aDs.getString( "status" ) );

      // Check for the user label
      MxAssert.assertEquals( "user_label", aUserLabel, aDs.getString( "user_label" ) );

      // Check for the reason
      MxAssert.assertEquals( "reason", aReason, aDs.getString( "reason" ) );

      // Check for the user stage note
      MxAssert.assertEquals( "user_stage_note", aUserStageNote,
            aDs.getString( "user_stage_note" ) );
   }
}
