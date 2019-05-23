
package com.mxi.mx.core.unittest.table.po;

import java.math.BigDecimal;
import java.util.Date;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefBorrowRateKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefFobKey;
import com.mxi.mx.core.key.RefPoAuthLvlStatusKey;
import com.mxi.mx.core.key.RefPoPaymentInfoKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.RefSpec2kCustKey;
import com.mxi.mx.core.key.RefTermsConditionsKey;
import com.mxi.mx.core.key.RefTransportTypeKey;
import com.mxi.mx.core.key.VendorAccountKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Class for accessing data from the PO_HEADER table.
 *
 * @author $Author: jcimino $
 * @version $Revision: 1.9 $
 */
public class PoHeader {

   /** Results of the query. */
   private DataSet iActual;

   /** Columns in the table. */
   private String[] iCols = { "PO_DB_ID", "PO_ID", "PO_TYPE_DB_ID", "PO_TYPE_CD",
         "REQ_PRIORITY_DB_ID", "REQ_PRIORITY_CD", "SHIP_TO_LOC_DB_ID", "SHIP_TO_LOC_ID",
         "VENDOR_DB_ID", "VENDOR_ID", "VENDOR_ACCOUNT_DB_ID", "VENDOR_ACCOUNT_ID",
         "VENDOR_ACCOUNT_CD", "ORG_DB_ID", "ORG_ID", "CURRENCY_DB_ID", "CURRENCY_CD", "EXCHG_QT",
         "CONTACT_HR_DB_ID", "CONTACT_HR_ID", "TERMS_CONDITIONS_DB_ID", "TERMS_CONDITIONS_CD",
         "FOB_DB_ID", "FOB_CD", "TRANSPORT_TYPE_DB_ID", "TRANSPORT_TYPE_CD", "PO_AUTH_FLOW_DB_ID",
         "PO_AUTH_FLOW_CD", "BORROW_RATE_DB_ID", "BORROW_RATE_CD", "VENDOR_NOTE", "ISSUED_DT",
         "CLOSED_DT", "SHIP_TO_CODE", "SPEC2K_CUST_DB_ID", "SPEC2K_CUST_CD", "LAST_MOD_DT",
         "RECEIVE_NOTE", "RE_SHIP_TO_DB_ID", "RE_SHIP_TO_ID", "BILL_TO_DB_ID", "BILL_TO_CD",
         "CONSIGN_TO_DB_ID", "CONSIGN_TO_CD", "CREATED_BY_ORG_DB_ID", "CREATED_BY_ORG_ID",
         "AUTH_STATUS_DB_ID", "AUTH_STATUS_CD", "BUDGET_CHECK_STATUS_DB_ID",
         "BUDGET_CHECK_STATUS_CD", "BUDGET_CHECK_CD", "BUDGET_CHECK_REF" };

   /** Results of the query. */
   private int iCurrentRow;


   /**
    * Initializes the class.
    *
    * @param aPurchaseOrder
    *           primary key of the table.
    */
   public PoHeader(PurchaseOrderKey aPurchaseOrder) {

      iCurrentRow = 1;

      // Obtain actual value
      iActual = MxDataAccess.getInstance().executeQuery( iCols, aPurchaseOrder.getTableName(),
            aPurchaseOrder.getPKWhereArg() );
   }


   /**
    * Asserts that the authorization status is correct on the purchase order
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertAuthStatus( RefPoAuthLvlStatusKey aExpected ) {

      RefPoAuthLvlStatusKey lActual = iActual.getKeyAt( RefPoAuthLvlStatusKey.class, 1,
            "auth_status_db_id", "auth_status_cd" );

      // Check if the actual and expected values match
      MxAssert.assertEquals( "auth_status_db_id:auth_status_cd", aExpected, lActual );
   }


   /**
    * Asserts that the Bill To is correct on the purchase order
    *
    * @param aExpected
    *           The expected Priority
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertBilTo( RefPoPaymentInfoKey aExpected ) throws Exception {

      // Retrieve the actual Bill To
      RefPoPaymentInfoKey lActual =
            new RefPoPaymentInfoKey( iActual.getIntAt( iCurrentRow, "bill_to_db_id" ),
                  iActual.getStringAt( iCurrentRow, "bill_to_cd" ) );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "bill_to_db_id:bill_to_cd", aExpected, lActual );
   }


   /**
    * Asserts that the <code>borrow_rate</code> and argument values are equal.
    *
    * @param aBorrowRate
    *           the expected value.
    */
   public void assertBorrowRate( RefBorrowRateKey aBorrowRate ) {
      RefBorrowRateKey lActual = null;

      // Retrieve the actual value
      Integer lDbId = iActual.getIntegerAt( 1, "borrow_rate_db_id" );
      String lCd = iActual.getStringAt( 1, "borrow_rate_cd" );

      if ( lDbId != null ) {
         lActual = new RefBorrowRateKey( lDbId.intValue(), lCd );
      }

      // Check if the actual and expected values match
      MxAssert.assertEquals( "borrow_rate_db_id:borrow_rate_cd", aBorrowRate, lActual );
   }


   /**
    * Asserts that the budget check code is correct on the purchase order
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertBudgetCheckCd( Object aExpected ) {
      String lActual = iActual.getStringAt( iCurrentRow, "budget_check_cd" );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "budget_check_cd", aExpected, lActual );
   }


   /**
    * Asserts that the budget check ref is correct on the purchase order
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertBudgetCheckRef( Object aExpected ) {
      String lActual = iActual.getStringAt( iCurrentRow, "budget_check_ref" );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "budget_check_ref", aExpected, lActual );
   }


   /**
    * Asserts that the budget check status is correct on the purchase order
    *
    * @param aExpected
    *           the expected value.
    */
   public void assertBudgetCheckStatus( RefPoAuthLvlStatusKey aExpected ) {

      RefPoAuthLvlStatusKey lActual = iActual.getKeyAt( RefPoAuthLvlStatusKey.class, 1,
            "budget_check_status_db_id", "budget_check_status_cd" );

      // Check if the actual and expected values match
      MxAssert.assertEquals( "budget_check_status_db_id:budget_check_status_cd", aExpected,
            lActual );
   }


   /**
    * Asserts that the closed dt is correct on the purchase order
    *
    * @param aExpected
    *           The closed dt
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertClosedDt( Date aExpected ) throws Exception {

      // Retrieve the actual closed dt
      Date lActual = iActual.getDateAt( iCurrentRow, "closed_dt" );

      // Assert that the actual and expected values match
      MxAssert.assertEquals( "closed_dt", aExpected, lActual );
   }


   /**
    * Asserts that the Consign To is correct on the purchase order
    *
    * @param aExpected
    *           The expected Priority
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertConsignTo( RefPoPaymentInfoKey aExpected ) throws Exception {

      // Retrieve the actual Consign To
      RefPoPaymentInfoKey lActual =
            new RefPoPaymentInfoKey( iActual.getIntAt( iCurrentRow, "consign_to_db_id" ),
                  iActual.getStringAt( iCurrentRow, "consign_to_cd" ) );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "consign_to_db_id:consign_to_cd", aExpected, lActual );
   }


   /**
    * Asserts that the receipt Organization is correct on the purchase order
    *
    * @param aExpected
    *           The expected Vendor
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertCreatedByOrganization( OrgKey aExpected ) throws Exception {

      // Retrieve the actual Vendor
      OrgKey lActual = new OrgKey( iActual.getIntAt( iCurrentRow, "CREATED_BY_ORG_DB_ID" ),
            iActual.getIntAt( iCurrentRow, "CREATED_BY_ORG_ID" ) );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "CREATED_BY_ORG_DB_ID:CREATED_BY_ORG_ID", aExpected, lActual );
   }


   /**
    * Asserts that the currency is correct on the purchase order
    *
    * @param aExpected
    *           The expected currency
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertCurrency( RefCurrencyKey aExpected ) throws Exception {

      // Retrieve the actual currency
      RefCurrencyKey lActual =
            new RefCurrencyKey( iActual.getIntAt( iCurrentRow, "currency_db_id" ),
                  iActual.getStringAt( iCurrentRow, "currency_cd" ) );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "currency_db_id:currency_cd", aExpected, lActual );
   }


   /**
    * Asserts that the exchange rate is correct on the purchase order
    *
    * @param aExpected
    *           The exchange rate
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertExchangeRate( BigDecimal aExpected ) throws Exception {

      // Retrieve the actual exchange rate
      BigDecimal lActual = iActual.getBigDecimalAt( iCurrentRow, "exchg_qt" );

      // Assert that the actual and expected exchange rates match
      MxAssert.assertEquals( "exchg_qt", aExpected, lActual );
   }


   /**
    * Asserts that the row with <code>po_db_id:po_id</code> = <code>iEvent</code> exists in table.
    */
   public void assertExist() {
      if ( !iActual.hasNext() ) {
         MxAssert.fail( "The po_header table does not have a row" );
      }
   }


   /**
    * Asserts that the Freight On Board is correct on the purchase order
    *
    * @param aExpected
    *           The expected FreightOnBoard
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertFreightOnBoard( RefFobKey aExpected ) throws Exception {

      RefFobKey lActual = null;

      // Retrieve the actual FreightOnBoard
      if ( !iActual.isNullAt( iCurrentRow, "fob_db_id" ) ) {
         lActual = new RefFobKey( iActual.getIntAt( iCurrentRow, "fob_db_id" ),
               iActual.getStringAt( iCurrentRow, "fob_cd" ) );
      }

      // Assert that the actual and expected match
      MxAssert.assertEquals( "fob_db_id:fob_cd", aExpected, lActual );
   }


   /**
    * Asserts that the issued dt is correct on the purchase order
    *
    * @param aExpected
    *           The issued dt
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertIssuedDt( Date aExpected ) throws Exception {

      // Retrieve the actual closed dt
      Date lActual = iActual.getDateAt( iCurrentRow, "issued_dt" );

      // Assert that the actual and expected values match
      MxAssert.assertEquals( "issued_dt", aExpected, lActual );
   }


   /**
    * Assert the last_mod_dt column.
    *
    * @param aExpected
    *           The expected value.
    */
   public void assertLastModifiedDate( Date aExpected ) {

      // Assert that the actual and expected are equal
      MxAssert.assertEquals( "last_mod_dt", aExpected,
            iActual.getDateAt( iCurrentRow, "last_mod_dt" ) );
   }


   /**
    * Asserts that the Organization is correct on the purchase order
    *
    * @param aExpected
    *           The expected Vendor
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertOrganization( OrgKey aExpected ) throws Exception {

      // Retrieve the actual Vendor
      OrgKey lActual = new OrgKey( iActual.getIntAt( iCurrentRow, "ORG_DB_ID" ),
            iActual.getIntAt( iCurrentRow, "ORG_ID" ) );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "org_db_id:org_id", aExpected, lActual );
   }


   /**
    * Asserts that the <code>po_type</code> and argument values are equal.
    *
    * @param aPoType
    *           the expected value.
    */
   public void assertPoType( RefPoTypeKey aPoType ) {

      RefPoTypeKey lActual = null;

      // Retrieve the actual value
      Integer lDbId = iActual.getIntegerAt( 1, "po_type_db_id" );
      String lCd = iActual.getStringAt( 1, "po_type_cd" );

      if ( lDbId != null ) {
         lActual = new RefPoTypeKey( lDbId.intValue(), lCd );
      }

      // Check if the actual and expected values match
      MxAssert.assertEquals( "po_type_db_id:po_type_cd", aPoType, lActual );
   }


   /**
    * Asserts that the Priority is correct on the purchase order
    *
    * @param aExpected
    *           The expected Priority
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertPriority( RefReqPriorityKey aExpected ) throws Exception {

      // Retrieve the actual Priority
      RefReqPriorityKey lActual =
            new RefReqPriorityKey( iActual.getIntAt( iCurrentRow, "req_priority_db_id" ),
                  iActual.getStringAt( iCurrentRow, "req_priority_cd" ) );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "req_priority_db_id:req_priority_cd", aExpected, lActual );
   }


   /**
    * Asserts that the Purchasing Contact is correct on the purchase order
    *
    * @param aExpected
    *           The expected Purchasing Contact
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertPurchasingContact( HumanResourceKey aExpected ) throws Exception {

      // Retrieve the actual Purchasing Contact
      HumanResourceKey lActual =
            new HumanResourceKey( iActual.getIntAt( iCurrentRow, "contact_hr_db_id" ),
                  iActual.getIntAt( iCurrentRow, "contact_hr_id" ) );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "contact_hr_db_id:contact_hr_id", aExpected, lActual );
   }


   /**
    * DOCUMENT ME!
    *
    * @param aExpected
    *           DOCUMENT ME!
    */
   public void assertReceiveNote( Object aExpected ) {
      String lActual = iActual.getStringAt( iCurrentRow, "receive_note" );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "receive_note", aExpected, lActual );
   }


   /**
    * Asserts that the Re-Expedite To Location is correct on the purchase order
    *
    * @param aExpected
    *           The expected Ship To Location
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertReExpediteToLocation( LocationKey aExpected ) throws Exception {

      // Retrieve the actual Re-Expedite To Location
      LocationKey lActual = new LocationKey( iActual.getIntAt( iCurrentRow, "re_ship_to_db_id" ),
            iActual.getIntAt( iCurrentRow, "re_ship_to_id" ) );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "re_ship_to_db_id:re_ship_to_id", aExpected, lActual );
   }


   /**
    * DOCUMENT ME!
    *
    * @param aExpected
    *           DOCUMENT ME!
    */
   public void assertShipToCode( Object aExpected ) {

      // Retrieve the actual
      String lActual = iActual.getStringAt( iCurrentRow, "ship_to_code" );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "ship_to_code", aExpected, lActual );
   }


   /**
    * Asserts that the Ship To Location is correct on the purchase order
    *
    * @param aExpected
    *           The expected Ship To Location
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertShipToLocation( LocationKey aExpected ) throws Exception {

      // Retrieve the actual Ship To Location
      LocationKey lActual = new LocationKey( iActual.getIntAt( iCurrentRow, "ship_to_loc_db_id" ),
            iActual.getIntAt( iCurrentRow, "ship_to_loc_id" ) );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "ship_to_loc_db_id:ship_to_loc_id", aExpected, lActual );
   }


   /**
    * Asserts that the Spec2k_Cust_Cd is correct on the purchase order
    *
    * @param aExpected
    *           The expected spec2k_cust_cd
    *
    * @throws Exception
    *            If an error occurs
    */
   public void assertSpec2kCustomer( RefSpec2kCustKey aExpected ) throws Exception {
      // Retrieve the actual spec2k_cust_cd
      RefSpec2kCustKey lActual =
            new RefSpec2kCustKey( iActual.getIntAt( iCurrentRow, "spec2k_cust_db_id" ),
                  iActual.getStringAt( iCurrentRow, "spec2k_cust_cd" ) );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "spec2k_cust_db_id:spec2k_cust_cd", aExpected, lActual );
   }


   /**
    * Asserts that the Terms & Conditions is correct on the purchase order
    *
    * @param aExpected
    *           The expected Terms & Conditions
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertTermsConditions( RefTermsConditionsKey aExpected ) throws Exception {

      // Retrieve the actual Terms & Conditions
      RefTermsConditionsKey lActual =
            new RefTermsConditionsKey( iActual.getIntAt( iCurrentRow, "terms_conditions_db_id" ),
                  iActual.getStringAt( iCurrentRow, "terms_conditions_cd" ) );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "terms_conditions_db_id:terms_conditions_cd", aExpected, lActual );
   }


   /**
    * Asserts that the Transportation Type is correct on the purchase order
    *
    * @param aExpected
    *           The expected Transportation Type
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertTransportationType( RefTransportTypeKey aExpected ) throws Exception {

      // Retrieve the actual Transportation Type
      RefTransportTypeKey lActual =
            new RefTransportTypeKey( iActual.getIntAt( iCurrentRow, "transport_type_db_id" ),
                  iActual.getStringAt( iCurrentRow, "transport_type_cd" ) );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "transport_type_db_id:transport_type_cd", aExpected, lActual );
   }


   /**
    * Asserts that the Vendor is correct on the purchase order
    *
    * @param aExpected
    *           The expected Vendor
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertVendor( VendorKey aExpected ) throws Exception {

      // Retrieve the actual Vendor
      VendorKey lActual = new VendorKey( iActual.getIntAt( iCurrentRow, "vendor_db_id" ),
            iActual.getIntAt( iCurrentRow, "vendor_id" ) );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "vendor_db_id:vendor_id", aExpected, lActual );
   }


   /**
    * Asserts that the Vendor Account is correct on the purchase order
    *
    * @param aExpected
    *           The expected Vendor Account
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertVendorAccount( VendorAccountKey aExpected ) throws Exception {

      // Assert that the actual and expected match
      MxAssert.assertEquals( "vendor account", aExpected,
            iActual.isNullAt( iCurrentRow, "vendor_account_db_id" ) ? null
                  : new VendorAccountKey( iActual.getIntAt( iCurrentRow, "vendor_account_db_id" ),
                        iActual.getIntAt( iCurrentRow, "vendor_account_id" ),
                        iActual.getStringAt( iCurrentRow, "vendor_account_cd" ) ) );
   }


   /**
    * Asserts that the Vendor Note is correct on the purchase order
    *
    * @param aExpected
    *           The expected Vendor Note
    *
    * @throws Exception
    *            If an error occurs.
    */
   public void assertVendorNote( String aExpected ) throws Exception {

      // Retrieve the actual currency
      String lActual = iActual.getStringAt( iCurrentRow, "vendor_note" );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "vendor_note", aExpected, lActual );
   }
}
