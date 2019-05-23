package com.mxi.mx.core.maint.plan.actualsloader;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.AbstractDatabaseConnection;
import com.mxi.mx.util.BatchFileManager;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;


/**
 *
 * This is an automated test to verify Purchase Orders information from C_PO_IMPORT package that
 * gets loaded into the MX database. The tests are only testing successful imports.
 *
 */
public class PurchaseOrder extends AbstractDatabaseConnection {

   @Rule
   public TestName testName = new TestName();

   // these are variables prepare for the test and it will be removed at the end of each test

   static simpleIDs iEventIds = null;
   static String iTaxId = "utl_raw.cast_to_raw('5')";
   String iSerialNoOem1 = "SN000013";
   String iSerialNoOem2 = "Z23-Z982";
   String iPartNoOem1 = "ACFT_ASSY_PN1";
   String iPartNoOem2 = "A0000005";
   String iWorkPackageName1 = "PO_TEST123";
   String iWorkPackageName2 = "PO_TEST1234";
   String iManufactCd = "10001";

   private ArrayList<String> iClearActualsTables = new ArrayList<String>();
   {
      iClearActualsTables.add( "DELETE FROM C_PO_HEADER" );
      iClearActualsTables.add( "DELETE FROM C_PO_LINE" );
      iClearActualsTables.add( "DELETE FROM C_PO_LINE_TAX" );
      iClearActualsTables.add( "DELETE FROM C_PO_LINE_CHARGE" );
   }


   @Before
   @Override
   public void before() throws Exception {
      super.before();
      createPrepData();
   }


   /**
    * Setup the Preparation Data required for the tests.
    *
    */
   private void createPrepData() {
      String lQuery = null;

      // TAX entry
      lQuery = "INSERT INTO tax(tax_id,tax_code,tax_name,tax_rate) VALUES (" + iTaxId
            + ",\'GST23\',\'GST23\',0.15)";
      runUpdate( lQuery );

      // EVT_EVENT entry
      String lTCName = testName.getMethodName();
      if ( lTCName.equals( "TestPoTypeRepairForACFT" ) ) {
         iEventIds =
               new simpleIDs( "\'4650\'", "\'" + getNextValueInSequence( "event_id_seq" ) + "\'" );
         createAssociatedIds( iEventIds, iPartNoOem1, iSerialNoOem1, iWorkPackageName1 );
      }

      if ( lTCName.contains( "LooseInventory" ) ) {

         addInvInvData();
         iEventIds =
               new simpleIDs( "\'4650\'", "\'" + getNextValueInSequence( "event_id_seq" ) + "\'" );
         createAssociatedIds( iEventIds, iPartNoOem2, iSerialNoOem2, iWorkPackageName2 );

         // Consign entry
         lQuery =
               "insert into REF_PO_PAYMENT_INFO (PAYMENT_INFO_DB_ID, PAYMENT_INFO_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD)"
                     + "values (4650, 'TESTGRP', 'TEST', 'TEST', 1)";
         runUpdate( lQuery );
      }
   }


   /**
    * Adds a row to inv_inv table with a particular Serial_No_Oem value
    *
    */
   private void addInvInvData() {
      String lQuery = "SELECT part_no_db_id, part_no_id  FROM eqp_part_no WHERE  PART_NO_OEM = '"
            + iPartNoOem2 + "' AND MANUFACT_CD = '10001' AND inv_class_cd = 'TRK'";

      PurchaseOrderTest lDataSetup = new PurchaseOrderTest();
      simpleIDs lPartNoIds = lDataSetup.getIDs( lQuery, "PART_NO_DB_ID", "PART_NO_ID" );

      int lInvNoID = getNextValueInSequence( "inv_no_id_seq" );
      String lQueryInsert =
            "INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd, part_no_db_id, part_no_id, serial_no_oem) "
                  + "VALUES(4650, " + lInvNoID + ", 4650, " + lInvNoID + ", 0, 'TRK', "
                  + lPartNoIds.getNO_DB_ID() + ", " + lPartNoIds.getNO_ID() + ", '" + iSerialNoOem2
                  + "')";
      runUpdate( lQueryInsert );
   }


   /**
    *
    * Create Test Data for some of the tests
    *
    * @param aEventIds
    *           - Event Id necessary for the test
    * @param aPartNoOem
    *           - Part No OEM that is linked to Event ID
    * @param aSerialNoOem
    *           - Serial No OEM that is linked to Event ID
    * @param aWorkPackage
    *           - Work Package that is linked to Event ID
    */
   private void createAssociatedIds( simpleIDs aEventIds, String aPartNoOem, String aSerialNoOem,
         String aWorkPackage ) {

      // EVT_EVENT entry
      String lQuery =
            "insert into EVT_EVENT (EVENT_DB_ID, EVENT_ID, EVENT_TYPE_DB_ID, EVENT_TYPE_CD, EVENT_STATUS_DB_ID, EVENT_STATUS_CD, H_EVENT_DB_ID, H_EVENT_ID, EVENT_SDESC)"
                  + "values (" + aEventIds.getNO_DB_ID() + ", " + aEventIds.getNO_ID()
                  + ", 0, 'TS', 0, 'ACTV', " + aEventIds.getNO_DB_ID() + ", " + aEventIds.getNO_ID()
                  + ", \'" + aWorkPackage + "\' )";
      runUpdate( lQuery );

      // Create Associated SCHED_STASK entry
      lQuery = "insert into SCHED_STASK (SCHED_DB_ID, SCHED_ID, TASK_CLASS_CD) values ("
            + aEventIds.getNO_DB_ID() + ", " + aEventIds.getNO_ID() + ", 'CHECK')";
      runUpdate( lQuery );

      // Create Associated EVT_INV entry
      int lInvNoId = getIntValueFromQuery(
            "SELECT inv_no_id FROM inv_inv WHERE inv_inv.serial_no_oem = \'" + aSerialNoOem + "\'",
            "inv_no_id" );
      int lPartNoId = getIntValueFromQuery(
            "SELECT * FROM eqp_part_no WHERE eqp_part_no.part_no_oem = \'" + aPartNoOem + "\'",
            "part_no_id" );
      lQuery =
            "insert into EVT_INV (EVENT_DB_ID, EVENT_ID, EVENT_INV_ID, INV_NO_DB_ID, INV_NO_ID, H_INV_NO_DB_ID, H_INV_NO_ID, ASSMBL_DB_ID, ASSMBL_CD, PART_NO_DB_ID, PART_NO_ID)"
                  + "values (" + aEventIds.getNO_DB_ID() + ", " + aEventIds.getNO_ID()
                  + ", 1, 4650, " + lInvNoId + ", 4650, " + lInvNoId + ", 4650, 'ACFT_CD1', 4650, "
                  + lPartNoId + ")";
      runUpdate( lQuery );

   }


   @Override
   @After
   public void after() throws Exception {
      classDataSetup( iClearActualsTables );
      cleanPoData();
      super.after();
   }


   /**
    * Removes pre-data and imported data generated by Po_Import_Pkg
    *
    */
   private void cleanPoData() {
      // remove preparation data
      runUpdate( "Delete from tax where tax_code = 'GST23'" );
      if ( iEventIds != null ) {
         runUpdate( "Delete from evt_event where EVENT_ID = " + iEventIds.getNO_ID() );
         runUpdate( "Delete from SCHED_STASK where SCHED_ID = " + iEventIds.getNO_ID() );
         runUpdate( "Delete from evt_inv where EVENT_ID = " + iEventIds.getNO_ID() );
      }

      String lTCName = testName.getMethodName();
      if ( lTCName.contains( "LooseInventory" ) ) {
         runUpdate( "Delete from REF_PO_PAYMENT_INFO where payment_info_cd = 'TESTGRP'" );
         runUpdate( "Delete from inv_inv where SERIAL_NO_OEM = '" + iSerialNoOem2 + "\'" );
      }

      String lQuery =
            "select count(*) from Evt_Event where (Event_Sdesc = 'PO300000' and event_type_cd = \'PO\')"
                  + " OR (Event_Sdesc like 'PO_TEST12%' and event_type_cd = \'TS\')"
                  + " OR (Event_Sdesc like '%PO300000' and event_status_cd = \'IXPEND\')";

      // if rows were imported then remove rows
      if ( countRowsOfQuery( lQuery ) > 0 ) {
         // using same query as above
         lQuery = lQuery.substring( 15 );
         lQuery = "Select Event_DB_ID, Event_ID" + lQuery;

         String[] lIds = { "EVENT_DB_ID", "EVENT_ID" };
         List<String> lfields = new ArrayList<String>( Arrays.asList( lIds ) );
         List<ArrayList<String>> lPoIds = execute( lQuery, lfields );

         // Run the delete same rows in previous query
         lQuery = lQuery.substring( 28 );
         lQuery = "Delete" + lQuery;
         runUpdate( lQuery );

         String lWhereArgPO = "";
         for ( int i = 0; i < lPoIds.size(); i++ ) {
            if ( i == 0 )
               lWhereArgPO += "(PO_DB_ID = \'" + lPoIds.get( i ).get( 0 ) + "\' AND PO_ID = \'"
                     + lPoIds.get( i ).get( 1 ) + "\')";
            else
               lWhereArgPO += "OR (PO_DB_ID = \'" + lPoIds.get( i ).get( 0 ) + "\' AND PO_ID = \'"
                     + lPoIds.get( i ).get( 1 ) + "\')";
         }
         runUpdate( createDeleteQuery( "po_line_tax", lWhereArgPO ) );
         runUpdate( createDeleteQuery( "po_header", lWhereArgPO ) );
         runUpdate( createDeleteQuery( "po_line_charge", lWhereArgPO ) );
         runUpdate( createDeleteQuery( "po_line", lWhereArgPO ) );
         runUpdate( createDeleteQuery( "po_auth", lWhereArgPO ) );

         // Running the same For loop, but updating field names for Ship_Shipment table
         String lWhereArgShip = "";
         for ( int i = 0; i < lPoIds.size(); i++ ) {
            if ( i == 0 )
               lWhereArgShip += "(SHIPMENT_DB_ID = \'" + lPoIds.get( i ).get( 0 )
                     + "\' AND SHIPMENT_ID = \'" + lPoIds.get( i ).get( 1 ) + "\')";
            else
               lWhereArgShip += "OR (SHIPMENT_DB_ID = \'" + lPoIds.get( i ).get( 0 )
                     + "\' AND SHIPMENT_ID = \'" + lPoIds.get( i ).get( 1 ) + "\')";
         }
         runUpdate( createDeleteQuery( "SHIP_SHIPMENT", lWhereArgShip ) );

         // Running the same For loop, but updating field names for Ship_Shipment table
         String lWhereArgShipLine = "";
         for ( int i = 0; i < lPoIds.size(); i++ ) {
            if ( i == 0 )
               lWhereArgShipLine += "(SHIPMENT_DB_ID = \'" + lPoIds.get( i ).get( 0 )
                     + "\' AND SHIPMENT_ID = \'" + lPoIds.get( i ).get( 1 ) + "\')";
            else
               lWhereArgShipLine += "OR (SHIPMENT_DB_ID = \'" + lPoIds.get( i ).get( 0 )
                     + "\' AND SHIPMENT_ID = \'" + lPoIds.get( i ).get( 1 ) + "\')";
         }
         runUpdate( createDeleteQuery( "SHIP_SHIPMENT_LINE", lWhereArgShipLine ) );
      }
      iEventIds = null;
   }


   /**
    *
    * Creating a delete Query.
    *
    * @param aTable
    *           - the table of where the data will be removed
    * @param aWhereArg
    *           - the Where Arguments (or search criteria)
    * @return a delete query in a String format
    *
    */

   private String createDeleteQuery( String aTable, String aWhereArg ) {
      return "Delete From " + aTable + " Where " + aWhereArg;
   }


   /**
    *
    * This is a test is for Purchase Order Package of type 'Repair' for an Aircraft. Expect the
    * record to be successfully imported.
    */

   @Test
   public void TestPoTypeRepairForACFT() {

      // inserting data into C_PO_HEADER table
      Map<String, String> lCPoHeaderMap = new LinkedHashMap<>();

      lCPoHeaderMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoHeaderMap.put( "ORD_TYPE_CD", "\'REPAIR\'" );
      lCPoHeaderMap.put( "PRIORITY_CD", "\'NORMAL\'" );
      lCPoHeaderMap.put( "CONTACT_HR_CD", "\'notavailable\'" );
      lCPoHeaderMap.put( "VENDOR_CD", "\'10001\'" );
      lCPoHeaderMap.put( "VENDOR_ACCOUNT_CD", "\'10001\'" );
      lCPoHeaderMap.put( "SHIP_TO_LOCATION_CD", "\'AIRPORT3\'" );
      lCPoHeaderMap.put( "SHIP_TO_CODE", "\'SHOP\'" );
      lCPoHeaderMap.put( "CURRENCY_CD", "\'USD\'" );
      lCPoHeaderMap.put( "EXCHG_QT", "\'1\'" );
      lCPoHeaderMap.put( "ISSUED_DT", "to_date(\'01-01-2018\', \'dd-mm-yyyy\')" );
      lCPoHeaderMap.put( "WP_NAME", "\'PO_TEST123\'" );
      lCPoHeaderMap.put( "SERIAL_NO_OEM", "\'SN000013\'" );
      lCPoHeaderMap.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lCPoHeaderMap.put( "MANUFACT_CD", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_HEADER, lCPoHeaderMap ) );

      // inserting data into C_PO_LINE table
      Map<String, String> lCPoLineMap = new LinkedHashMap<>();

      lCPoLineMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineMap.put( "PO_LINE_TYPE_CD", "\'REPAIR\'" );
      lCPoLineMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineMap.put( "ORDER_QT", "\'1\'" );
      lCPoLineMap.put( "UNIT_PRICE", "\'200.00\'" );
      lCPoLineMap.put( "ACCOUNT_CD", "\'5\'" );
      lCPoLineMap.put( "PROMISE_BY_DT", "to_date(\'01-04-2018\', \'dd-mm-yyyy\')" );
      lCPoLineMap.put( "QTY_UNIT_CD", "\'EA\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE, lCPoLineMap ) );

      // inserting data into C_PO_LINE_TAX table
      Map<String, String> lCPoLineTaxMap = new LinkedHashMap<>();

      lCPoLineTaxMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineTaxMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineTaxMap.put( "TAX_CODE", "\'GST23\'" );
      lCPoLineTaxMap.put( "TAX_RATE", "0.15" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE_TAX, lCPoLineTaxMap ) );

      // inserting data into C_PO_LINE_CHARGE
      Map<String, String> lCPoLineChargeMap = new LinkedHashMap<>();

      lCPoLineChargeMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineChargeMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineChargeMap.put( "CHARGE_CODE", "\'BORROW\'" );
      lCPoLineChargeMap.put( "CHARGE_AMOUNT", "230.00" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE_CHARGE, lCPoLineChargeMap ) );

      Assert.assertTrue( "Validation errors occurred", runValidationPurchaseOrder() == 1 );

      Assert.assertTrue( "Import errors occurred", runImportPurchaseOrder() == 1 );

      PurchaseOrderTest lVerifyData = new PurchaseOrderTest();
      lVerifyData.findIds( lCPoHeaderMap );
      lVerifyData.findIds( lCPoLineMap );
      lVerifyData.findIds( lCPoLineChargeMap );
      lVerifyData.findIds( lCPoLineTaxMap );
      lVerifyData.verifyPoHeader( lCPoHeaderMap );
      lVerifyData.verifyPoLine( lCPoLineMap, iEventIds );
      lVerifyData.verifyPoLineCharge( lCPoLineChargeMap );
      lVerifyData.verifyPoLineTax( lCPoLineTaxMap, iTaxId );
      lVerifyData.verifyPoAuth();
      lVerifyData.verifySchedTask( iEventIds );
   }


   /**
    *
    *
    * This is a test is for Purchase Order Package of type 'Repair' and the inventory is loose.
    * Expect the record should be to successfully imported.
    */
   @Test
   public void TestPoTypeRepairForLooseInventory() {

      // inserting data into C_PO_HEADER table
      Map<String, String> lCPoHeaderMap = new LinkedHashMap<>();

      lCPoHeaderMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoHeaderMap.put( "ORD_TYPE_CD", "\'REPAIR\'" );
      lCPoHeaderMap.put( "PRIORITY_CD", "\'NORMAL\'" );
      lCPoHeaderMap.put( "CONTACT_HR_CD", "\'notavailable\'" );
      lCPoHeaderMap.put( "VENDOR_CD", "\'10001\'" );
      lCPoHeaderMap.put( "VENDOR_ACCOUNT_CD", "\'10001\'" );
      lCPoHeaderMap.put( "BROKER_CD", "\'10005\'" );
      lCPoHeaderMap.put( "BROKER_ACCOUNT_CD", "\'10005\'" );
      lCPoHeaderMap.put( "SHIP_TO_LOCATION_CD", "\'AIRPORT3/DOCK\'" );
      lCPoHeaderMap.put( "SHIP_TO_CODE", "\'SHOP\'" );
      lCPoHeaderMap.put( "TRANSPORT_TYPE_CD", "\'FEDEX\'" );
      lCPoHeaderMap.put( "RECEIPT_INSP_BOOL", "\'Y\'" );
      lCPoHeaderMap.put( "CURRENCY_CD", "\'USD\'" );
      lCPoHeaderMap.put( "EXCHG_QT", "\'1\'" );
      lCPoHeaderMap.put( "ISSUED_DT", "to_date(\'01-01-2018\', \'dd-mm-yyyy\')" );
      lCPoHeaderMap.put( "WP_NAME", "\'PO_TEST1234\'" );
      lCPoHeaderMap.put( "SERIAL_NO_OEM", "\'" + iSerialNoOem2 + "\'" );
      lCPoHeaderMap.put( "PART_NO_OEM", "\'A0000005\'" );
      lCPoHeaderMap.put( "MANUFACT_CD", "\'10001\'" );
      lCPoHeaderMap.put( "TERMS_CONDITIONS_CD", "\'PREPAID\'" );
      lCPoHeaderMap.put( "FREIGHT_ON_BOARD_CD", "\'VENDOR\'" );
      lCPoHeaderMap.put( "ORD_EXT_REF", "\'ORD_EXT_REF\'" );
      lCPoHeaderMap.put( "VENDOR_WP_NO", "\'VENDOR_WP_NO\'" );
      lCPoHeaderMap.put( "VENDOR_NOTE", "\'VENDOR_NOTE\'" );
      lCPoHeaderMap.put( "RECEIVER_NOTE", "\'RECEIVER_NOTE\'" );
      lCPoHeaderMap.put( "RE_SHIP_TO_LOCATION_CD", "\'AIRPORT1/DOCK\'" );
      lCPoHeaderMap.put( "BILL_TO_CD", "\'TESTGRP\'" );
      lCPoHeaderMap.put( "CONSIGN_TO_CD", "\'TESTGRP\'" );
      lCPoHeaderMap.put( "RECEIPT_ORGANIZATION_CD", "\'MXI\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_HEADER, lCPoHeaderMap ) );

      // inserting data into C_PO_LINE table
      Map<String, String> lCPoLineMap = new LinkedHashMap<>();

      lCPoLineMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineMap.put( "PO_LINE_TYPE_CD", "\'REPAIR\'" );
      lCPoLineMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineMap.put( "ORDER_QT", "\'1\'" );
      lCPoLineMap.put( "UNIT_PRICE", "\'200.00\'" );
      lCPoLineMap.put( "ACCOUNT_CD", "\'5\'" );
      lCPoLineMap.put( "PROMISE_BY_DT", "to_date(\'01-04-2018\', \'dd-mm-yyyy\')" );
      lCPoLineMap.put( "QTY_UNIT_CD", "\'EA\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE, lCPoLineMap ) );

      // inserting data into C_PO_LINE_TAX table
      Map<String, String> lCPoLineTaxMap = new LinkedHashMap<>();

      lCPoLineTaxMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineTaxMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineTaxMap.put( "TAX_CODE", "\'GST23\'" );
      lCPoLineTaxMap.put( "TAX_RATE", "0.15" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE_TAX, lCPoLineTaxMap ) );

      // inserting data into C_PO_LINE_CHARGE
      Map<String, String> lCPoLineChargeMap = new LinkedHashMap<>();

      lCPoLineChargeMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineChargeMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineChargeMap.put( "CHARGE_CODE", "\'BORROW\'" );
      lCPoLineChargeMap.put( "CHARGE_AMOUNT", "230.00" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE_CHARGE, lCPoLineChargeMap ) );

      Assert.assertTrue( "Validation errors occurred", runValidationPurchaseOrder() == 1 );

      Assert.assertTrue( "Import errors occurred", runImportPurchaseOrder() == 1 );

      // Verifying Data
      PurchaseOrderTest lVerifyData = new PurchaseOrderTest();
      lVerifyData.findIds( lCPoHeaderMap );
      lVerifyData.findIds( lCPoLineMap );
      lVerifyData.findIds( lCPoLineChargeMap );
      lVerifyData.findIds( lCPoLineTaxMap );
      lVerifyData.verifyPoHeader( lCPoHeaderMap );
      lVerifyData.verifyPoLine( lCPoLineMap, iEventIds );
      lVerifyData.verifyPoLineCharge( lCPoLineChargeMap );
      lVerifyData.verifyPoLineTax( lCPoLineTaxMap, iTaxId );
      lVerifyData.verifyPoAuth();
      lVerifyData.verifyShipments();
      lVerifyData.verifyEvents( 3 );
   }


   /**
    *
    * This is a test is for Purchase Order Package of type 'Purchase'. Expect the record should be
    * to successfully imported.
    */
   @Test
   public void TestPoTypePurchase() {

      // inserting data into C_PO_HEADER table
      Map<String, String> lCPoHeaderMap = new LinkedHashMap<>();

      lCPoHeaderMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoHeaderMap.put( "ORD_TYPE_CD", "\'PURCHASE\'" );
      lCPoHeaderMap.put( "PRIORITY_CD", "\'NORMAL\'" );
      lCPoHeaderMap.put( "CONTACT_HR_CD", "\'notavailable\'" );
      lCPoHeaderMap.put( "VENDOR_CD", "\'10001\'" );
      lCPoHeaderMap.put( "VENDOR_ACCOUNT_CD", "\'10001\'" );
      lCPoHeaderMap.put( "SHIP_TO_LOCATION_CD", "\'AIRPORT3/DOCK\'" );
      lCPoHeaderMap.put( "SHIP_TO_CODE", "\'SHOP\'" );
      lCPoHeaderMap.put( "TRANSPORT_TYPE_CD", "\'TRUCK\'" );
      lCPoHeaderMap.put( "RECEIPT_INSP_BOOL", "\'Y\'" );
      lCPoHeaderMap.put( "CURRENCY_CD", "\'USD\'" );
      lCPoHeaderMap.put( "EXCHG_QT", "\'1\'" );
      lCPoHeaderMap.put( "ISSUED_DT", "to_date(\'01-01-2018\', \'dd-mm-yyyy\')" );
      lCPoHeaderMap.put( "TERMS_CONDITIONS_CD", "\'PREPAID\'" );
      lCPoHeaderMap.put( "FREIGHT_ON_BOARD_CD", "\'VENDOR\'" );
      lCPoHeaderMap.put( "ORD_EXT_REF", "\'ORD_EXT_REF\'" );
      lCPoHeaderMap.put( "VENDOR_WP_NO", "\'VENDOR_WP_NO\'" );
      lCPoHeaderMap.put( "VENDOR_NOTE", "\'VENDOR_NOTE\'" );
      lCPoHeaderMap.put( "RECEIVER_NOTE", "\'RECEIVER_NOTE\'" );
      lCPoHeaderMap.put( "RECEIPT_ORGANIZATION_CD", "\'MXI\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_HEADER, lCPoHeaderMap ) );

      // inserting data into C_PO_LINE table
      Map<String, String> lCPoLineMap = new LinkedHashMap<>();

      lCPoLineMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineMap.put( "PO_LINE_TYPE_CD", "\'PURCHASE\'" );
      lCPoLineMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineMap.put( "ORDER_QT", "\'1\'" );
      lCPoLineMap.put( "UNIT_PRICE", "\'200.00\'" );
      lCPoLineMap.put( "ACCOUNT_CD", "\'INVOICE\'" );
      lCPoLineMap.put( "PROMISE_BY_DT", "to_date(\'01-04-2018\', \'dd-mm-yyyy\')" );
      lCPoLineMap.put( "QTY_UNIT_CD", "\'EA\'" );
      lCPoLineMap.put( "PART_NO_OEM", "\'A0000005\'" );
      lCPoLineMap.put( "MANUFACT_CD", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE, lCPoLineMap ) );

      // inserting data into C_PO_LINE_TAX table
      Map<String, String> lCPoLineTaxMap = new LinkedHashMap<>();

      lCPoLineTaxMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineTaxMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineTaxMap.put( "TAX_CODE", "\'GST23\'" );
      lCPoLineTaxMap.put( "TAX_RATE", "0.15" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE_TAX, lCPoLineTaxMap ) );

      // inserting data into C_PO_LINE_CHARGE
      Map<String, String> lCPoLineChargeMap = new LinkedHashMap<>();

      lCPoLineChargeMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineChargeMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineChargeMap.put( "CHARGE_CODE", "\'BORROW\'" );
      lCPoLineChargeMap.put( "CHARGE_AMOUNT", "230.00" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE_CHARGE, lCPoLineChargeMap ) );

      Assert.assertTrue( "Validation errors occurred", runValidationPurchaseOrder() == 1 );

      Assert.assertTrue( "Import errors occurred", runImportPurchaseOrder() == 1 );

      // Verifying Data
      PurchaseOrderTest lVerifyData = new PurchaseOrderTest();
      lVerifyData.findIds( lCPoHeaderMap );
      lVerifyData.findIds( lCPoLineMap );
      lVerifyData.findIds( lCPoLineChargeMap );
      lVerifyData.findIds( lCPoLineTaxMap );
      lVerifyData.verifyPoHeader( lCPoHeaderMap );
      lVerifyData.verifyPoLine( lCPoLineMap, null );
      lVerifyData.verifyPoLineCharge( lCPoLineChargeMap );
      lVerifyData.verifyPoLineTax( lCPoLineTaxMap, iTaxId );
      lVerifyData.verifyPoAuth();
      lVerifyData.verifyEvents( 2 );
   }


   /**
    *
    * This is a test is for Purchase Order Package of type 'Purchase'. Expect the record should be
    * to successfully imported.
    */
   @Test
   public void TestPoTypeBorrow() {

      // inserting data into C_PO_HEADER table
      Map<String, String> lCPoHeaderMap = new LinkedHashMap<>();

      lCPoHeaderMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoHeaderMap.put( "ORD_TYPE_CD", "\'BORROW\'" );
      lCPoHeaderMap.put( "PRIORITY_CD", "\'NORMAL\'" );
      lCPoHeaderMap.put( "CONTACT_HR_CD", "\'notavailable\'" );
      lCPoHeaderMap.put( "VENDOR_CD", "\'10001\'" );
      lCPoHeaderMap.put( "VENDOR_ACCOUNT_CD", "\'10001\'" );
      lCPoHeaderMap.put( "SHIP_TO_LOCATION_CD", "\'AIRPORT3/DOCK\'" );
      lCPoHeaderMap.put( "SHIP_TO_CODE", "\'SHOP\'" );
      lCPoHeaderMap.put( "TRANSPORT_TYPE_CD", "\'TRUCK\'" );
      lCPoHeaderMap.put( "RECEIPT_INSP_BOOL", "\'Y\'" );
      lCPoHeaderMap.put( "CURRENCY_CD", "\'USD\'" );
      lCPoHeaderMap.put( "EXCHG_QT", "\'1\'" );
      lCPoHeaderMap.put( "ISSUED_DT", "to_date(\'01-01-2018\', \'dd-mm-yyyy\')" );
      lCPoHeaderMap.put( "TERMS_CONDITIONS_CD", "\'PREPAID\'" );
      lCPoHeaderMap.put( "FREIGHT_ON_BOARD_CD", "\'VENDOR\'" );
      lCPoHeaderMap.put( "BORROW_RATE_CD", "\'IATA\'" );
      lCPoHeaderMap.put( "ORD_EXT_REF", "\'ORD_EXT_REF\'" );
      lCPoHeaderMap.put( "VENDOR_WP_NO", "\'VENDOR_WP_NO\'" );
      lCPoHeaderMap.put( "VENDOR_NOTE", "\'VENDOR_NOTE\'" );
      lCPoHeaderMap.put( "RECEIVER_NOTE", "\'RECEIVER_NOTE\'" );
      lCPoHeaderMap.put( "RECEIPT_ORGANIZATION_CD", "\'MXI\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_HEADER, lCPoHeaderMap ) );

      // inserting data into C_PO_LINE table
      Map<String, String> lCPoLineMap = new LinkedHashMap<>();

      lCPoLineMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineMap.put( "PO_LINE_TYPE_CD", "\'BORROW\'" );
      lCPoLineMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineMap.put( "ORDER_QT", "\'1\'" );
      lCPoLineMap.put( "BASE_UNIT_PRICE", "\'60.00\'" );
      lCPoLineMap.put( "ACCOUNT_CD", "\'5\'" );
      lCPoLineMap.put( "PROMISE_BY_DT", "to_date(\'01-04-2018\', \'dd-mm-yyyy\')" );
      lCPoLineMap.put( "QTY_UNIT_CD", "\'EA\'" );
      lCPoLineMap.put( "PART_NO_OEM", "\'A0000005\'" );
      lCPoLineMap.put( "MANUFACT_CD", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE, lCPoLineMap ) );

      // inserting data into C_PO_LINE_TAX table
      Map<String, String> lCPoLineTaxMap = new LinkedHashMap<>();

      lCPoLineTaxMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineTaxMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineTaxMap.put( "TAX_CODE", "\'GST23\'" );
      lCPoLineTaxMap.put( "TAX_RATE", "0.15" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE_TAX, lCPoLineTaxMap ) );

      // inserting data into C_PO_LINE_CHARGE
      Map<String, String> lCPoLineChargeMap = new LinkedHashMap<>();

      lCPoLineChargeMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineChargeMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineChargeMap.put( "CHARGE_CODE", "\'BORROW\'" );
      lCPoLineChargeMap.put( "CHARGE_AMOUNT", "230.00" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE_CHARGE, lCPoLineChargeMap ) );

      Assert.assertTrue( "Validation errors occurred", runValidationPurchaseOrder() == 1 );

      Assert.assertTrue( "Import errors occurred", runImportPurchaseOrder() == 1 );

      // Verifying Data
      PurchaseOrderTest lVerifyData = new PurchaseOrderTest();
      lVerifyData.findIds( lCPoHeaderMap );
      lVerifyData.findIds( lCPoLineMap );
      lVerifyData.findIds( lCPoLineChargeMap );
      lVerifyData.findIds( lCPoLineTaxMap );
      lVerifyData.verifyPoHeader( lCPoHeaderMap );
      lVerifyData.verifyPoLine( lCPoLineMap, null );
      lVerifyData.verifyPoLineCharge( lCPoLineChargeMap );
      lVerifyData.verifyPoLineTax( lCPoLineTaxMap, iTaxId );
      lVerifyData.verifyPoAuth();
      lVerifyData.verifyEvents( 2 );
   }


   /**
    *
    * This is a test is for Purchase Order Package of type 'CONSIGN'. Expect the record should be to
    * successfully imported.
    */
   @Test
   public void TestPoTypeConsign() {

      // inserting data into C_PO_HEADER table
      Map<String, String> lCPoHeaderMap = new LinkedHashMap<>();

      lCPoHeaderMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoHeaderMap.put( "ORD_TYPE_CD", "\'CONSIGN\'" );
      lCPoHeaderMap.put( "PRIORITY_CD", "\'NORMAL\'" );
      lCPoHeaderMap.put( "CONTACT_HR_CD", "\'notavailable\'" );
      lCPoHeaderMap.put( "VENDOR_CD", "\'10001\'" );
      lCPoHeaderMap.put( "VENDOR_ACCOUNT_CD", "\'10001\'" );
      lCPoHeaderMap.put( "SHIP_TO_LOCATION_CD", "\'AIRPORT3/DOCK\'" );
      lCPoHeaderMap.put( "SHIP_TO_CODE", "\'SHOP\'" );
      lCPoHeaderMap.put( "TRANSPORT_TYPE_CD", "\'AIR\'" );
      lCPoHeaderMap.put( "RECEIPT_INSP_BOOL", "\'Y\'" );
      lCPoHeaderMap.put( "CURRENCY_CD", "\'USD\'" );
      lCPoHeaderMap.put( "EXCHG_QT", "\'1\'" );
      lCPoHeaderMap.put( "ISSUED_DT", "to_date(\'01-01-2018\', \'dd-mm-yyyy\')" );
      lCPoHeaderMap.put( "VENDOR_NOTE", "\'VENDOR_NOTE\'" );
      lCPoHeaderMap.put( "RECEIVER_NOTE", "\'RECEIVER_NOTE\'" );
      lCPoHeaderMap.put( "RECEIPT_ORGANIZATION_CD", "\'MXI\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_HEADER, lCPoHeaderMap ) );

      // inserting data into C_PO_LINE table
      Map<String, String> lCPoLineMap = new LinkedHashMap<>();

      lCPoLineMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineMap.put( "PO_LINE_TYPE_CD", "\'CONSIGN\'" );
      lCPoLineMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineMap.put( "ORDER_QT", "\'1\'" );
      lCPoLineMap.put( "UNIT_PRICE", "\'200.00\'" );
      lCPoLineMap.put( "ACCOUNT_CD", "\'CONSIGN\'" );
      lCPoLineMap.put( "PROMISE_BY_DT", "to_date(\'01-04-2018\', \'dd-mm-yyyy\')" );
      lCPoLineMap.put( "QTY_UNIT_CD", "\'EA\'" );
      lCPoLineMap.put( "PART_NO_OEM", "\'A0000005\'" );
      lCPoLineMap.put( "MANUFACT_CD", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE, lCPoLineMap ) );

      // inserting data into C_PO_LINE_TAX table
      Map<String, String> lCPoLineTaxMap = new LinkedHashMap<>();

      lCPoLineTaxMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineTaxMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineTaxMap.put( "TAX_CODE", "\'GST23\'" );
      lCPoLineTaxMap.put( "TAX_RATE", "0.15" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE_TAX, lCPoLineTaxMap ) );

      // inserting data into C_PO_LINE_CHARGE
      Map<String, String> lCPoLineChargeMap = new LinkedHashMap<>();

      lCPoLineChargeMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineChargeMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineChargeMap.put( "CHARGE_CODE", "\'BORROW\'" );
      lCPoLineChargeMap.put( "CHARGE_AMOUNT", "230.00" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE_CHARGE, lCPoLineChargeMap ) );

      Assert.assertTrue( "Validation errors occurred", runValidationPurchaseOrder() == 1 );

      Assert.assertTrue( "Import errors occurred", runImportPurchaseOrder() == 1 );

      // Verifying Data
      PurchaseOrderTest lVerifyData = new PurchaseOrderTest();
      lVerifyData.findIds( lCPoHeaderMap );
      lVerifyData.findIds( lCPoLineMap );
      lVerifyData.findIds( lCPoLineChargeMap );
      lVerifyData.findIds( lCPoLineTaxMap );
      lVerifyData.verifyPoHeader( lCPoHeaderMap );
      lVerifyData.verifyPoLine( lCPoLineMap, null );
      lVerifyData.verifyPoLineCharge( lCPoLineChargeMap );
      lVerifyData.verifyPoLineTax( lCPoLineTaxMap, iTaxId );
      lVerifyData.verifyPoAuth();
      lVerifyData.verifyEvents( 2 );
   }


   /**
    *
    * This is a test is for Purchase Order Package of type 'CSGNXCHG'. Expect the record should be
    * to successfully imported. Batch Files are being used to execute the validation and import
    * processes.
    */
   @Test
   public void TestPoTypeConsignExchange() {

      // inserting data into C_PO_HEADER table
      Map<String, String> lCPoHeaderMap = new LinkedHashMap<>();

      lCPoHeaderMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoHeaderMap.put( "ORD_TYPE_CD", "\'CSGNXCHG\'" );
      lCPoHeaderMap.put( "PRIORITY_CD", "\'NORMAL\'" );
      lCPoHeaderMap.put( "CONTACT_HR_CD", "\'notavailable\'" );
      lCPoHeaderMap.put( "VENDOR_CD", "\'10001\'" );
      lCPoHeaderMap.put( "VENDOR_ACCOUNT_CD", "\'10001\'" );
      lCPoHeaderMap.put( "SHIP_TO_LOCATION_CD", "\'AIRPORT3/DOCK\'" );
      lCPoHeaderMap.put( "SHIP_TO_CODE", "\'SHOP\'" );
      lCPoHeaderMap.put( "TRANSPORT_TYPE_CD", "\'RAIL\'" );
      lCPoHeaderMap.put( "RECEIPT_INSP_BOOL", "\'Y\'" );
      lCPoHeaderMap.put( "CURRENCY_CD", "\'USD\'" );
      lCPoHeaderMap.put( "EXCHG_QT", "\'1\'" );
      lCPoHeaderMap.put( "ISSUED_DT", "to_date(\'01-01-2018\', \'dd-mm-yyyy\')" );
      lCPoHeaderMap.put( "VENDOR_NOTE", "\'VENDOR_NOTE\'" );
      lCPoHeaderMap.put( "RECEIVER_NOTE", "\'RECEIVER_NOTE\'" );
      lCPoHeaderMap.put( "RECEIPT_ORGANIZATION_CD", "\'MXI\'" );
      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_HEADER, lCPoHeaderMap ) );

      // inserting data into C_PO_LINE table
      Map<String, String> lCPoLineMap = new LinkedHashMap<>();

      lCPoLineMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineMap.put( "PO_LINE_TYPE_CD", "\'CSGNXCHG\'" );
      lCPoLineMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineMap.put( "ORDER_QT", "\'1\'" );
      lCPoLineMap.put( "UNIT_PRICE", "\'200.00\'" );
      lCPoLineMap.put( "ACCOUNT_CD", "\'CONSIGN\'" );
      lCPoLineMap.put( "PROMISE_BY_DT", "to_date(\'01-04-2018\', \'dd-mm-yyyy\')" );
      lCPoLineMap.put( "QTY_UNIT_CD", "\'EA\'" );
      lCPoLineMap.put( "PART_NO_OEM", "\'A0000005\'" );
      lCPoLineMap.put( "MANUFACT_CD", "\'10001\'" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE, lCPoLineMap ) );

      // inserting data into C_PO_LINE_TAX table
      Map<String, String> lCPoLineTaxMap = new LinkedHashMap<>();

      lCPoLineTaxMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineTaxMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineTaxMap.put( "TAX_CODE", "\'GST23\'" );
      lCPoLineTaxMap.put( "TAX_RATE", "0.15" );

      runInsert( TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE_TAX, lCPoLineTaxMap ) );

      // inserting data into C_PO_LINE_CHARGE
      Map<String, String> lCPoLineChargeMap = new LinkedHashMap<>();

      lCPoLineChargeMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineChargeMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineChargeMap.put( "CHARGE_CODE", "\'BORROW\'" );
      lCPoLineChargeMap.put( "CHARGE_AMOUNT", "230.00" );

      runInsert(
            TableUtil.getInsertForTableByMap( TableUtil.C_PO_LINE_CHARGE, lCPoLineChargeMap ) );

      BatchFileManager lFileMgr = new BatchFileManager();
      lFileMgr.validatePurchaseOrderViaBatch();
      Assert.assertTrue( "Validation errors occurred", VerifyProcessingOfPurchaseOrders() == 0 );

      lFileMgr.importPurchaseOrderViaBatch();
      Assert.assertTrue( "Import errors occurred", VerifyProcessingOfPurchaseOrders() == 0 );

      // Verifying Data
      PurchaseOrderTest lVerifyData = new PurchaseOrderTest();
      lVerifyData.findIds( lCPoHeaderMap );
      lVerifyData.findIds( lCPoLineMap );
      lVerifyData.findIds( lCPoLineChargeMap );
      lVerifyData.findIds( lCPoLineTaxMap );
      lVerifyData.verifyPoHeader( lCPoHeaderMap );
      lVerifyData.verifyPoLine( lCPoLineMap, null );
      lVerifyData.verifyPoLineCharge( lCPoLineChargeMap );
      lVerifyData.verifyPoLineTax( lCPoLineTaxMap, iTaxId );
      lVerifyData.verifyPoAuth();
      lVerifyData.verifyEvents( 2 );

   }


   /**
    * This test is to verify a.load_purchase_order.bat is to load data from csv file to staging
    * table properly.
    *
    *
    */
   @Test
   public void testLoadCSV() {

      BatchFileManager lFileMgr = new BatchFileManager();
      lFileMgr.copyFile( TestConstants.TEST_CASE_DATA, TestConstants.PO_HEADER_CSV_FILE,
            TestConstants.PO_BATCH_FOLDER + "\\data\\" );
      lFileMgr.copyFile( TestConstants.TEST_CASE_DATA, TestConstants.PO_LINE_CSV_FILE,
            TestConstants.PO_BATCH_FOLDER + "\\data\\" );
      lFileMgr.copyFile( TestConstants.TEST_CASE_DATA, TestConstants.PO_LINE_TAX_CSV_FILE,
            TestConstants.PO_BATCH_FOLDER + "\\data\\" );
      lFileMgr.copyFile( TestConstants.TEST_CASE_DATA, TestConstants.PO_LINE_CHARGE_CSV_FILE,
            TestConstants.PO_BATCH_FOLDER + "\\data\\" );
      lFileMgr.loadPurchaseOrderViaBatch();

      // Verify staging files were loaded
      PurchaseOrderTest lVerifyData = new PurchaseOrderTest();
      lVerifyData.VerifyPoStagingTables();

   }


   /**
    * This will check the proc tables to see if there are any errors during validation or import
    * processes.
    *
    * @return lCount - number error codes found.
    */
   private int VerifyProcessingOfPurchaseOrders() {

      int lCount =
            countRowsOfQuery( "SELECT COUNT(*) FROM C_proc_PO_HEADER WHERE result_cd IS NOT NULL" );
      lCount +=
            countRowsOfQuery( "SELECT COUNT(*) FROM C_proc_PO_LINE WHERE result_cd IS NOT NULL" );
      lCount += countRowsOfQuery(
            "SELECT COUNT(*) FROM C_proc_PO_LINE_TAX WHERE result_cd IS NOT NULL" );
      lCount += countRowsOfQuery(
            "SELECT COUNT(*) FROM C_proc_PO_LINE_CHARGE WHERE result_cd IS NOT NULL" );
      if ( lCount != 1 ) {
         System.out.println( "============= Data Errors ===============" );
         displayErrorMessages( TableUtil.C_PROC_PO_HEADER );
         displayErrorMessages( TableUtil.C_PROC_PO_LINE );
         displayErrorMessages( TableUtil.C_PROC_PO_LINE_CHARGE );
         displayErrorMessages( TableUtil.C_PROC_PO_LINE_TAX );
      }
      return lCount;
   }


   /**
    * Run the Purchase Order Validation using a direct call to the plsql using a prepared statement.
    * This does not call any batch files.
    *
    *
    */
   protected int runValidationPurchaseOrder() {
      int lResult = 0;
      CallableStatement lPrepareCallInventory;

      try {

         lPrepareCallInventory = getConnection().prepareCall(
               "BEGIN    c_po_import.validate_purchase_order(on_retcode => :on_retcode, ov_retmsg => :ov_retmsg); END;" );

         lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallInventory.registerOutParameter( 2, Types.VARCHAR );

         lPrepareCallInventory.execute();
         commit();
         lResult = lPrepareCallInventory.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      if ( lResult != 1 ) {
         System.out.println( "============= Data Errors ===============" );
         displayErrorMessages( TableUtil.C_PROC_PO_HEADER );
         displayErrorMessages( TableUtil.C_PROC_PO_LINE );
         displayErrorMessages( TableUtil.C_PROC_PO_LINE_CHARGE );
         displayErrorMessages( TableUtil.C_PROC_PO_LINE_TAX );
      }

      return lResult;
   }


   /**
    * Run the Purchase Order Import using a direct call to the plsql using a prepared statement.
    * This does not call any batch files.
    *
    *
    */
   protected int runImportPurchaseOrder() {
      int lResult = 0;
      CallableStatement lPrepareCallInventory;

      try {

         lPrepareCallInventory = getConnection().prepareCall(
               "BEGIN    c_po_import.import_purchase_order(on_retcode => :on_retcode, ov_retmsg => :ov_retmsg); END;" );

         lPrepareCallInventory.registerOutParameter( 1, Types.INTEGER );
         lPrepareCallInventory.registerOutParameter( 2, Types.VARCHAR );

         lPrepareCallInventory.execute();
         commit();
         lResult = lPrepareCallInventory.getInt( 1 );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
      if ( lResult != 1 ) {
         System.out.println( "============= Data Errors ===============" );
         displayErrorMessages( TableUtil.C_PROC_PO_HEADER );
         displayErrorMessages( TableUtil.C_PROC_PO_LINE );
         displayErrorMessages( TableUtil.C_PROC_PO_LINE_CHARGE );
         displayErrorMessages( TableUtil.C_PROC_PO_LINE_TAX );
      }
      return lResult;
   }


   /**
    * Displays the error messages in the console
    *
    * @return
    *
    */
   private void displayErrorMessages( String aTable ) {
      String lQuery = "SELECT DL_REF_MESSAGE.Result_cd, DL_REF_MESSAGE.tech_desc FROM " + aTable
            + " inner join DL_REF_MESSAGE on " + aTable + ".Result_cd = DL_REF_MESSAGE.Result_cd";
      ResultSet lResult = null;
      PreparedStatement lStatement;

      try {
         lStatement = getConnection().prepareStatement( lQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
               ResultSet.CONCUR_READ_ONLY );
         lResult = lStatement.executeQuery( lQuery );

         while ( lResult.next() ) {
            if ( lResult.getString( "RESULT_CD" ) != null )
               System.out
                     .println( "Table: " + aTable + " > Error: " + lResult.getString( "RESULT_CD" )
                           + " - " + lResult.getString( "TECH_DESC" ) );
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
   }

}
