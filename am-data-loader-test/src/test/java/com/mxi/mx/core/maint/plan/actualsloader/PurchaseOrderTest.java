package com.mxi.mx.core.maint.plan.actualsloader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.AbstractDatabaseConnection;
import com.mxi.mx.util.TableUtil;


/**
 * This class is designed to help in verifying the data by finding DB_IDs and IDs help in creating
 * the SQLQuery to verify the information
 *
 */
public class PurchaseOrderTest extends AbstractDatabaseConnection {

   simpleIDs iPoIds;
   String iPoNumber;
   simpleIDs iPoTypeCd;
   simpleIDs iHrCd;
   simpleIDs iVendorCd;
   simpleIDs iVendorAccountCd;
   simpleIDs iShipToLocationCd;
   simpleIDs iReShipToLocationCd;
   simpleIDs iBorrowRateCd;
   simpleIDs iFreightOnBoardCd;
   simpleIDs iBillToCd;
   simpleIDs iCurrencyCd;
   simpleIDs iTransportTypeCd;
   simpleIDs iTermsConditionCd;
   simpleIDs iConsignCd;
   simpleIDs iBrokerCd;
   simpleIDs iBrokerAccountCd;
   simpleIDs iReceiptOrganizationCd;
   simpleIDs iSerialNoOem;
   simpleIDs iPartNoOem;
   simpleIDs iManufactCd;
   simpleIDs iWpName;
   simpleIDs iQtyUnitCd;
   simpleIDs iAccountCd;
   simpleIDs iPriceTypeCd;
   simpleIDs iChargeCd;
   simpleIDs iTaxCd;
   String iOrderQt;
   String iPoLineId;
   String iSerialOem;
   String iOrdExtRef;
   String iVendorWpNo;
   simpleIDs iReqPriorityCd;


   /**
    * Updating staging table Map by adding ID fields and update other instance variables. Then it
    * will verification to use the instance variable to configure Data Map
    *
    * @param aDataMap
    */
   public void findIds( Map<String, String> aDataMap ) {

      String lQuery = "";

      for ( Entry<String, String> lEntry : aDataMap.entrySet() ) {
         switch ( lEntry.getKey().toUpperCase() ) {
            case ( "PO_NUMBER" ):
               lQuery = "Select Event_DB_ID, Event_ID from Evt_Event where Event_Sdesc = "
                     + lEntry.getValue()
                     + " and event_type_cd = \'PO\' and EVENT_STATUS_CD = 'POISSUED' AND EVENT_STATUS_DB_ID = 0";
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iPoIds = getIDs( lQuery, "EVENT_DB_ID", "EVENT_ID" );
               iPoNumber = lEntry.getValue();
               break;
            case ( "ORD_TYPE_CD" ):
               lQuery = "Select PO_TYPE_DB_ID, PO_TYPE_CD from REF_PO_TYPE where po_type_CD = "
                     + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iPoTypeCd = getIDs( lQuery, "PO_TYPE_DB_ID", "PO_TYPE_CD" );
               break;
            case ( "CONTACT_HR_CD" ):
               lQuery =
                     "Select HR_DB_ID, HR_ID from UTL_USER INNER JOIN ORG_HR ON UTL_USER.USER_ID = org_hr.user_id where USERNAME = "
                           + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iHrCd = getIDs( lQuery, "HR_DB_ID", "HR_ID" );
               break;
            case ( "VENDOR_CD" ):
               lQuery = "Select Vendor_DB_ID, Vendor_ID from org_Vendor where vendor_cd = "
                     + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iVendorCd = getIDs( lQuery, "VENDOR_DB_ID", "VENDOR_ID" );
               break;
            case ( "VENDOR_ACCOUNT_CD" ):
               lQuery = "Select Vendor_DB_ID, Vendor_ID from org_Vendor_Account where account_cd = "
                     + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iVendorAccountCd = getIDs( lQuery, "VENDOR_DB_ID", "VENDOR_ID" );
               break;
            case ( "BROKER_CD" ):
               lQuery = "Select Vendor_DB_ID, Vendor_ID from org_Vendor where vendor_cd = "
                     + lEntry.getValue() + " and vendor_type_cd = \'BROKER\'";
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iBrokerCd = getIDs( lQuery, "VENDOR_DB_ID", "VENDOR_ID" );
               break;
            case ( "BROKER_ACCOUNT_CD" ):
               lQuery = "Select VENDOR_DB_ID, VENDOR_ID from org_Vendor_account where "
                     + "ACCOUNT_CD = " + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iBrokerAccountCd = getIDs( lQuery, "VENDOR_DB_ID", "VENDOR_ID" );
               break;
            case ( "CURRENCY_CD" ):
               lQuery = "Select CURRENCY_DB_ID, CURRENCY_CD from REF_CURRENCY where CURRENCY_CD = "
                     + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iCurrencyCd = getIDs( lQuery, "CURRENCY_DB_ID", "CURRENCY_CD" );
               break;
            case ( "SHIP_TO_LOCATION_CD" ):
               lQuery = "Select LOC_DB_ID, LOC_ID from inv_loc where LOC_CD = " + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iShipToLocationCd = getIDs( lQuery, "LOC_DB_ID", "LOC_ID" );
               break;

            case ( "RE_SHIP_TO_LOCATION_CD" ):
               lQuery = "Select LOC_DB_ID, LOC_ID from inv_loc where LOC_CD = " + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iReShipToLocationCd = getIDs( lQuery, "LOC_DB_ID", "LOC_ID" );
               break;
            case ( "TERMS_CONDITIONS_CD" ):
               lQuery =
                     "Select TERMS_CONDITIONS_DB_ID, TERMS_CONDITIONS_CD from REF_TERMS_CONDITIONS where TERMS_CONDITIONS_CD = "
                           + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iTermsConditionCd =
                     getIDs( lQuery, "TERMS_CONDITIONS_DB_ID", "TERMS_CONDITIONS_CD" );
               break;
            case ( "BILL_TO_CD" ):
               lQuery =
                     "Select PAYMENT_INFO_DB_ID, PAYMENT_INFO_CD from Ref_Po_Payment_Info where PAYMENT_INFO_CD = "
                           + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iBillToCd = getIDs( lQuery, "PAYMENT_INFO_DB_ID", "PAYMENT_INFO_CD" );
               break;
            case ( "TRANSPORT_TYPE_CD" ):
               lQuery =
                     "Select TRANSPORT_TYPE_DB_ID, TRANSPORT_TYPE_CD from REF_TRANSPORT_TYPE where TRANSPORT_TYPE_CD = "
                           + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iTransportTypeCd = getIDs( lQuery, "TRANSPORT_TYPE_DB_ID", "TRANSPORT_TYPE_CD" );
               break;
            case ( "CONSIGN_TO_CD" ):
               lQuery =
                     "Select PAYMENT_INFO_DB_ID, PAYMENT_INFO_CD from Ref_Po_Payment_Info where PAYMENT_INFO_CD = "
                           + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iConsignCd = getIDs( lQuery, "PAYMENT_INFO_DB_ID", "PAYMENT_INFO_CD" );
               break;
            case ( "BORROW_RATE_CD" ):
               lQuery =
                     "Select BORROW_RATE_DB_ID, BORROW_RATE_CD from REF_BORROW_RATE where BORROW_RATE_CD = "
                           + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iBorrowRateCd = getIDs( lQuery, "BORROW_RATE_DB_ID", "BORROW_RATE_CD" );
               break;
            case ( "FREIGHT_ON_BOARD_CD" ):
               lQuery = "Select FOB_DB_ID, FOB_CD from REF_FOB where FOB_CD = " + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iFreightOnBoardCd = getIDs( lQuery, "FOB_DB_ID", "FOB_CD" );
               break;
            case ( "RECEIPT_ORGANIZATION_CD" ):
               lQuery = "Select org_DB_ID, org_ID from org_org where ORG_CD = " + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iReceiptOrganizationCd = getIDs( lQuery, "ORG_DB_ID", "ORG_ID" );
               break;
            case ( "SERIAL_NO_OEM" ):
               lQuery = "Select INV_NO_Db_ID, INV_NO_ID from inv_inv where SERIAL_NO_OEM = "
                     + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iSerialNoOem = getIDs( lQuery, "INV_NO_Db_ID", "INV_NO_ID" );
               iSerialOem = lEntry.getKey();
               break;
            case ( "PART_NO_OEM" ):
               lQuery = "Select PART_NO_Db_ID, PART_NO_ID from eqp_part_no where PART_NO_OEM = "
                     + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iPartNoOem = getIDs( lQuery, "PART_NO_DB_ID", "PART_NO_ID" );
               break;
            case ( "MANUFACT_CD" ):
               lQuery = "Select MANUFACT_DB_ID, MANUFACT_CD from eqp_manufact where MANUFACT_CD = "
                     + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iManufactCd = getIDs( lQuery, "MANUFACT_DB_ID", "MANUFACT_CD" );
               break;
            case ( "WP_NAME" ):
               lQuery = "Select Event_DB_ID, Event_ID from Evt_Event where Event_Sdesc = "
                     + lEntry.getValue() + " and event_type_cd = \'TS\'";
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iWpName = getIDs( lQuery, "EVENT_DB_ID", "EVENT_ID" );
               break;
            case ( "QTY_UNIT_CD" ):
               lQuery = "Select QTY_UNIT_DB_ID, QTY_UNIT_CD from REF_QTY_UNIT where QTY_UNIT_CD = "
                     + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iQtyUnitCd = getIDs( lQuery, "QTY_UNIT_DB_ID", "QTY_UNIT_CD" );
               break;
            case ( "ACCOUNT_CD" ):
               lQuery = "Select ACCOUNT_DB_ID, ACCOUNT_ID from FNC_ACCOUNT where ACCOUNT_CD = "
                     + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iAccountCd = getIDs( lQuery, "ACCOUNT_DB_ID", "ACCOUNT_ID" );
               break;
            case ( "PRICE_TYPE_CD" ):
               lQuery =
                     "Select PRICE_TYPE_DB_ID, PRICE_TYPE_CD from REF_PRICE_TYPE where PRICE_TYPE_CD = "
                           + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iPriceTypeCd = getIDs( lQuery, "PRICE_TYPE_DB_ID", "PRICE_TYPE_CD" );
               break;
            case ( "CHARGE_CODE" ):
               lQuery = "Select CHARGE_ID from CHARGE where CHARGE_CODE = " + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iChargeCd = getIDs( lQuery, "CHARGE_ID", "CHARGE_ID" );
               break;
            case ( "ORDER_QT" ):
               iOrderQt = lEntry.getValue();
               break;
            case ( "PO_LINE_ORD" ):
               iPoLineId = lEntry.getValue();
               break;
            case ( "PRIORITY_CD" ):
               iReqPriorityCd = new simpleIDs( "0", lEntry.getValue() );
               break;
            case ( "ORD_EXT_REF" ):
               iOrdExtRef = lEntry.getValue();
               break;
            case ( "VENDOR_WP_NO" ):
               iVendorWpNo = lEntry.getValue();
               break;
         }

      }

   }


   /**
    *
    * This method will test to see if the query from findIds() method is returning only one row.
    *
    * @param aQuery
    *           - This is query from findIds()
    * @param aColumnName
    *           - this Column Name in the staging related to query.
    */
   public void checkForOneRowResult( String aQuery, String aColumnName ) {

      Assert.assertTrue( aColumnName + ": Query does not return one row: " + aQuery,
            countRowsOfQuery( aQuery ) == 1 );

   }


   /**
    *
    * This method runs the query, and returns the ids.
    *
    * @param aQuery
    *           - executes query
    * @param aDbId
    *           - column name of the DB ID
    * @param aId
    *           - column name of the ID
    * @return - Returns the Ids based on the simpleIDs class
    */
   @Override
   public simpleIDs getIDs( String aQuery, String aDbId, String aId ) {

      ResultSet lResultSet = null;
      String lDbId = null;
      String lId = null;

      try {
         PreparedStatement lStatement = getConnection().prepareStatement( aQuery,
               ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );

         lResultSet = lStatement.executeQuery( aQuery );
         lResultSet.next();
         lDbId = lResultSet.getString( aDbId );
         lId = lResultSet.getString( aId );
         if ( !lResultSet.isLast() )
            throw new IllegalArgumentException( "This query returns more than one row: " + aQuery );
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return new simpleIDs( lDbId, lId );
   }


   /**
    *
    * This method will perform the verification for PO_HEADER table, by updating data map to match
    * the Maintenix table.
    *
    * @param aUpdatedDataMap
    *           - The original data map used to insert data into the staging table
    *
    */
   public void verifyPoHeader( Map<String, String> aUpdatedDataMap ) {

      // Required C_PO_header fields
      aUpdatedDataMap.remove( "PO_NUMBER" );
      aUpdatedDataMap.put( "PO_DB_ID", iPoIds.getNO_DB_ID() );
      aUpdatedDataMap.put( "PO_ID", iPoIds.getNO_ID() );
      aUpdatedDataMap.remove( "ORD_TYPE_CD" );
      aUpdatedDataMap.put( "PO_TYPE_DB_ID", iPoTypeCd.getNO_DB_ID() );
      aUpdatedDataMap.put( "PO_TYPE_CD", "\'" + iPoTypeCd.getNO_ID() + "\'" );
      aUpdatedDataMap.put( "REQ_PRIORITY_DB_ID", iReqPriorityCd.getNO_DB_ID() );
      aUpdatedDataMap.put( "REQ_PRIORITY_CD", iReqPriorityCd.getNO_ID() );
      aUpdatedDataMap.remove( "PRIORITY_CD" );
      aUpdatedDataMap.remove( "CONTACT_HR_CD" );
      aUpdatedDataMap.put( "CONTACT_HR_DB_ID", iHrCd.getNO_DB_ID() );
      aUpdatedDataMap.put( "CONTACT_HR_ID", iHrCd.getNO_ID() );
      // Note: defect has been raised, the defect will automatically set the CONTACT_HR_ID to ADMIN
      // during PO import process regardless of the value.
      aUpdatedDataMap.put( "CONTACT_HR_ID", "3" ); // remove this line once it is fixed.
      aUpdatedDataMap.remove( "VENDOR_CD" );
      aUpdatedDataMap.put( "VENDOR_DB_ID", iVendorCd.getNO_DB_ID() );
      aUpdatedDataMap.put( "VENDOR_ID", iVendorCd.getNO_ID() );
      aUpdatedDataMap.remove( "VENDOR_ACCOUNT_CD" );
      aUpdatedDataMap.put( "VENDOR_ACCOUNT_DB_ID", iVendorAccountCd.getNO_DB_ID() );
      aUpdatedDataMap.put( "VENDOR_ACCOUNT_ID", iVendorAccountCd.getNO_ID() );
      aUpdatedDataMap.put( "CURRENCY_DB_ID", iCurrencyCd.getNO_DB_ID() );
      aUpdatedDataMap.remove( "SHIP_TO_LOCATION_CD" );
      aUpdatedDataMap.put( "SHIP_TO_LOC_DB_ID", iShipToLocationCd.getNO_DB_ID() );
      aUpdatedDataMap.put( "SHIP_TO_LOC_ID", iShipToLocationCd.getNO_ID() );
      // Optional C_PO_header fields
      if ( aUpdatedDataMap.get( "BROKER_CD" ) != null ) {
         aUpdatedDataMap.remove( "BROKER_CD" );
         aUpdatedDataMap.put( "BROKER_DB_ID", iBrokerCd.getNO_DB_ID() );
         aUpdatedDataMap.put( "BROKER_ID", iBrokerCd.getNO_ID() );
      }
      if ( aUpdatedDataMap.get( "BROKER_ACCOUNT_CD" ) != null ) {
         aUpdatedDataMap.put( "BROKER_ACCOUNT_DB_ID", iBrokerAccountCd.getNO_DB_ID() );
         aUpdatedDataMap.put( "BROKER_ACCOUNT_ID", iBrokerAccountCd.getNO_ID() );
         aUpdatedDataMap.remove( "BROKER_ACCOUNT_CD" );
      }
      if ( aUpdatedDataMap.get( "RE_SHIP_TO_LOCATION_CD" ) != null ) {
         aUpdatedDataMap.remove( "RE_SHIP_TO_LOCATION_CD" );
         aUpdatedDataMap.put( "RE_SHIP_TO_DB_ID", iReShipToLocationCd.getNO_DB_ID() );
         aUpdatedDataMap.put( "RE_SHIP_TO_ID", iReShipToLocationCd.getNO_ID() );
      }
      if ( aUpdatedDataMap.get( "TERMS_CONDITIONS_CD" ) != null )
         aUpdatedDataMap.put( "TERMS_CONDITIONS_DB_ID", iTermsConditionCd.getNO_DB_ID() );

      if ( aUpdatedDataMap.get( "BILL_TO_CD" ) != null )
         aUpdatedDataMap.put( "BILL_TO_DB_ID", iBillToCd.getNO_DB_ID() );

      if ( aUpdatedDataMap.get( "TRANSPORT_TYPE_CD" ) != null ) {
         aUpdatedDataMap.put( "TRANSPORT_TYPE_DB_ID", iTransportTypeCd.getNO_DB_ID() );
         aUpdatedDataMap.put( "TRANSPORT_TYPE_CD", "\'" + iTransportTypeCd.getNO_ID() + "\'" );
      }
      if ( aUpdatedDataMap.get( "CONSIGN_TO_CD" ) != null ) {
         aUpdatedDataMap.put( "CONSIGN_TO_DB_ID", iConsignCd.getNO_DB_ID() );
         aUpdatedDataMap.put( "CONSIGN_TO_CD", "\'" + iConsignCd.getNO_ID() + "\'" );
      }
      if ( aUpdatedDataMap.get( "BORROW_RATE_CD" ) != null ) {
         aUpdatedDataMap.put( "BORROW_RATE_DB_ID", iBorrowRateCd.getNO_DB_ID() );
         aUpdatedDataMap.put( "BORROW_RATE_CD", "\'" + iBorrowRateCd.getNO_ID() + "\'" );
      }
      if ( aUpdatedDataMap.get( "FREIGHT_ON_BOARD_CD" ) != null ) {
         aUpdatedDataMap.remove( "FREIGHT_ON_BOARD_CD" );
         aUpdatedDataMap.put( "FOB_DB_ID", iFreightOnBoardCd.getNO_DB_ID() );
         aUpdatedDataMap.put( "FOB_CD", "\'" + iFreightOnBoardCd.getNO_ID() + "\'" );
      }
      if ( aUpdatedDataMap.get( "RECEIPT_ORGANIZATION_CD" ) != null ) {
         aUpdatedDataMap.remove( "RECEIPT_ORGANIZATION_CD" );
         aUpdatedDataMap.put( "ORG_DB_ID", iReceiptOrganizationCd.getNO_DB_ID() );
         aUpdatedDataMap.put( "ORG_DB_ID", iReceiptOrganizationCd.getNO_DB_ID() );
         aUpdatedDataMap.put( "ORG_ID", iReceiptOrganizationCd.getNO_ID() );
         aUpdatedDataMap.put( "CREATED_BY_ORG_DB_ID", iReceiptOrganizationCd.getNO_DB_ID() );
         aUpdatedDataMap.put( "CREATED_BY_ORG_ID", iReceiptOrganizationCd.getNO_ID() );
      }
      if ( aUpdatedDataMap.get( "RECEIVER_NOTE" ) != null ) {
         String lTemp = aUpdatedDataMap.get( "RECEIVER_NOTE" );
         aUpdatedDataMap.remove( "RECEIVER_NOTE" );
         aUpdatedDataMap.put( "RECEIVE_NOTE", lTemp );
      }
      if ( aUpdatedDataMap.get( "RECEIPT_INSP_BOOL" ) != null ) {
         if ( aUpdatedDataMap.get( "RECEIPT_INSP_BOOL" ).equalsIgnoreCase( "\'Y\'" ) )
            aUpdatedDataMap.put( "RECEIPT_INSP_BOOL", "\'1\'" );
         else
            aUpdatedDataMap.put( "RECEIPT_INSP_BOOL", "\'0\'" );
      }
      aUpdatedDataMap.remove( "SERIAL_NO_OEM" );
      aUpdatedDataMap.remove( "PART_NO_OEM" );
      aUpdatedDataMap.remove( "MANUFACT_CD" );
      aUpdatedDataMap.remove( "WP_NAME" );
      aUpdatedDataMap.remove( "VENDOR_WP_NO" );
      aUpdatedDataMap.remove( "ORD_EXT_REF" );

      Assert.assertTrue( "Verify values in PO_HEADER: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.PO_HEADER, aUpdatedDataMap ) ) );

   }


   /**
    *
    * This method will perform the verification for PO_LINE table, by updating data map to match the
    * Maintenix table.
    *
    * @param aUpdatedDataMap
    *           - The original data map used to insert data into the staging table
    *
    */
   public void verifyPoLine( Map<String, String> aUpdatedDataMap, simpleIDs aSchedId ) {

      aUpdatedDataMap.remove( "PO_NUMBER" );
      aUpdatedDataMap.put( "PO_DB_ID", iPoIds.getNO_DB_ID() );
      aUpdatedDataMap.put( "PO_ID", iPoIds.getNO_ID() );
      if ( aUpdatedDataMap.get( "PO_LINE_TYPE_CD" ).equals( "REPAIR" ) ) {
         aUpdatedDataMap.put( "PART_NO_DB_ID", iPartNoOem.getNO_DB_ID() );
         aUpdatedDataMap.put( "PART_NO_ID", iPartNoOem.getNO_ID() );
      }
      aUpdatedDataMap.put( "PO_LINE_ID", iPoLineId );
      aUpdatedDataMap.remove( "PO_LINE_ORD" );
      if ( aSchedId != null ) {
         aUpdatedDataMap.put( "SCHED_DB_ID", aSchedId.getNO_DB_ID() );
         aUpdatedDataMap.put( "SCHED_ID", aSchedId.getNO_ID() );
      }
      aUpdatedDataMap.put( "QTY_UNIT_DB_ID", iQtyUnitCd.getNO_DB_ID() );
      aUpdatedDataMap.remove( "ACCOUNT_CD" );
      aUpdatedDataMap.put( "ACCOUNT_DB_ID", iAccountCd.getNO_DB_ID() );
      aUpdatedDataMap.put( "ACCOUNT_ID", iAccountCd.getNO_ID() );
      aUpdatedDataMap.remove( "MANUFACT_CD" );
      aUpdatedDataMap.remove( "PART_NO_OEM" );

      if ( aUpdatedDataMap.get( "MAINT_RECEIPT_BOOL" ) != null ) {
         String ltemp = aUpdatedDataMap.get( "MAINT_RECEIPT_BOOL" );
         aUpdatedDataMap.put( "MAINT_PICKUP_BOOL", ltemp );
         aUpdatedDataMap.remove( "MAINT_RECEIPT_BOOL" );
      }
      if ( aUpdatedDataMap.get( "PRICE_TYPE_CD" ) != null )
         aUpdatedDataMap.put( "PRICE_TYPE_DB_ID", iPriceTypeCd.getNO_ID() );
      aUpdatedDataMap.remove( "CONFIRM_UNIT_PRICE_BOOL" ); // This column is not being used in
                                                           // Maintenix

      Assert.assertTrue( "Verify values in PO_LINE: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.PO_LINE, aUpdatedDataMap ) ) );

   }


   /**
    *
    * This method will perform the verification for PO_LINE_CHARGE table, by updating data map to
    * match the Maintenix table.
    *
    * @param aUpdatedDataMap
    *           - The original data map used to insert data into the staging table
    *
    */
   public void verifyPoLineCharge( Map<String, String> aUpdatedDataMap ) {
      aUpdatedDataMap.remove( "PO_NUMBER" );
      aUpdatedDataMap.put( "PO_DB_ID", iPoIds.getNO_DB_ID() );
      aUpdatedDataMap.put( "PO_ID", iPoIds.getNO_ID() );
      aUpdatedDataMap.put( "PO_LINE_ID", iPoLineId );
      aUpdatedDataMap.remove( "PO_LINE_ORD" );
      aUpdatedDataMap.remove( "CHARGE_CODE" );
      aUpdatedDataMap.put( "CHARGE_ID", "\'" + iChargeCd.getNO_ID() + "\'" );

      Assert.assertTrue( "Verify values in PO_LINE_CHARGE: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.PO_LINE_CHARGE, aUpdatedDataMap ) ) );

   }


   /**
    *
    * This method will perform the verification for PO_LINE_TAX table, by updating data map to match
    * the Maintenix table.
    *
    * @param aUpdatedDataMap
    *           - The original data map used to insert data into the staging table
    * @param aTaxId
    *           - The tax id generated during data setup prior to the test.
    */

   public void verifyPoLineTax( Map<String, String> aUpdatedDataMap, String aTaxId ) {
      aUpdatedDataMap.remove( "PO_NUMBER" );
      aUpdatedDataMap.put( "PO_DB_ID", iPoIds.getNO_DB_ID() );
      aUpdatedDataMap.put( "PO_ID", iPoIds.getNO_ID() );
      aUpdatedDataMap.put( "PO_LINE_ID", iPoLineId );
      aUpdatedDataMap.remove( "PO_LINE_ORD" );
      aUpdatedDataMap.put( "TAX_ID", aTaxId );
      aUpdatedDataMap.remove( "TAX_CODE" );

      Assert.assertTrue( "Verify values in PO_LINE_TAX: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.PO_LINE_TAX, aUpdatedDataMap ) ) );
   }


   public void verifyPoAuth() {
      String lQuery = "Select * from po_auth where po_db_id = " + iPoIds.getNO_DB_ID()
            + " and po_id = " + iPoIds.getNO_ID();

      Assert.assertTrue( "Verify values in PO_AUTH: ", runQueryReturnsOneRow( lQuery ) );
   }


   /**
    * This method will perform the verification for SHIP_SHIPMENT and SHIP_SHIPMENT_LINE tables
    *
    */
   public void verifyShipments() {
      // For SHIP_SHIPMENT Table Entries
      Map<String, String> lShipDataMap = new LinkedHashMap<>();
      lShipDataMap.put( "PO_DB_ID", iPoIds.getNO_DB_ID() );
      lShipDataMap.put( "PO_ID", iPoIds.getNO_ID() );
      lShipDataMap.put( "REQ_PRIORITY_DB_ID", iReqPriorityCd.getNO_DB_ID() );
      lShipDataMap.put( "REQ_PRIORITY_CD", iReqPriorityCd.getNO_ID() );
      lShipDataMap.put( "SHIPMENT_TYPE_DB_ID", "\'0\'" );
      lShipDataMap.put( "SHIPMENT_TYPE_CD", "\'SENDREP\'" );

      if ( iTransportTypeCd.getNO_ID() != null ) {
         lShipDataMap.put( "TRANSPORT_TYPE_DB_ID", iTransportTypeCd.getNO_DB_ID() );
         lShipDataMap.put( "TRANSPORT_TYPE_CD", "\'" + iTransportTypeCd.getNO_ID() + "\'" );
      }

      String lQuery = TableUtil.findRecordInDatabase( TableUtil.SHIP_SHIPMENT, lShipDataMap );
      // First entry
      Assert.assertTrue( "Verify values in SHIP_SHIPMENT(1): ", runQueryReturnsOneRow( lQuery ) );
      simpleIDs lShipIdsFirstEntry = getIDs( lQuery, "SHIPMENT_DB_ID", "SHIPMENT_ID" );

      // Second entry
      lShipDataMap.put( "SHIPMENT_TYPE_CD", "\'" + iPoTypeCd.getNO_ID() + "\'" );
      lQuery = TableUtil.findRecordInDatabase( TableUtil.SHIP_SHIPMENT, lShipDataMap );
      Assert.assertTrue( "Verify values in SHIP_SHIPMENT(2): ", runQueryReturnsOneRow( lQuery ) );
      simpleIDs lShipIdsSecondEntry = getIDs( lQuery, "SHIPMENT_DB_ID", "SHIPMENT_ID" );

      Map<String, String> lShipLineDataMap = new LinkedHashMap<>();
      lShipLineDataMap.put( "PO_DB_ID", iPoIds.getNO_DB_ID() );
      lShipLineDataMap.put( "PO_ID", iPoIds.getNO_ID() );
      lShipLineDataMap.put( "PO_LINE_ID", iPoLineId );
      lShipLineDataMap.put( "PART_NO_DB_ID", iPartNoOem.getNO_DB_ID() );
      lShipLineDataMap.put( "PART_NO_ID", iPartNoOem.getNO_ID() );
      lShipLineDataMap.put( "SERIAL_NO_OEM", iSerialOem );
      lShipLineDataMap.put( "LINE_NO_ORD", iPoLineId );
      lShipLineDataMap.put( "EXPECT_QT", iOrderQt );
      lShipLineDataMap.put( "SHIPMENT_DB_ID", lShipIdsSecondEntry.getNO_DB_ID() );
      lShipLineDataMap.put( "SHIPMENT_ID", lShipIdsSecondEntry.getNO_ID() );

      Assert.assertTrue( "Verify values in SHIP_SHIPMENT_LINE(1): ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.SHIP_SHIPMENT_LINE, lShipLineDataMap ) ) );

      lShipLineDataMap.put( "SHIPMENT_DB_ID", lShipIdsFirstEntry.getNO_DB_ID() );
      lShipLineDataMap.put( "SHIPMENT_ID", lShipIdsFirstEntry.getNO_ID() );
      lShipLineDataMap.put( "INV_NO_DB_ID", iSerialNoOem.getNO_DB_ID() );
      lShipLineDataMap.put( "INV_NO_ID", iSerialNoOem.getNO_ID() );
      Assert.assertTrue( "Verify values in SHIP_SHIPMENT_LINE(1): ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.SHIP_SHIPMENT_LINE, lShipLineDataMap ) ) );
   }


   /**
    * This method will perform the verification for EVT_EVENT table
    *
    * @param aEventRows
    *           - This is number of event rows generated for the particular test
    */
   public void verifyEvents( int aEventRows ) {

      Map<String, String> lEvtEventDataMap = new LinkedHashMap<>();
      // First entry
      lEvtEventDataMap.put( "EVENT_DB_ID", iPoIds.getNO_DB_ID() );
      lEvtEventDataMap.put( "EVENT_ID", iPoIds.getNO_ID() );
      lEvtEventDataMap.put( "EVENT_SDESC", iPoNumber );
      if ( lEvtEventDataMap.get( "EXT_KEY_SDESC" ) != null )
         lEvtEventDataMap.put( "EXT_KEY_SDESC", iOrdExtRef );
      lEvtEventDataMap.put( "EVENT_STATUS_CD", "\'POISSUED\'" );

      Assert.assertTrue( "Verify values in EVT_EVENT(1): ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EVT_EVENT, lEvtEventDataMap ) ) );
      // Second entry
      lEvtEventDataMap.remove( "EVENT_ID" );
      lEvtEventDataMap.remove( "EVENT_DB_ID" );
      lEvtEventDataMap.remove( "EXT_KEY_SDESC" );

      String lPoNumber = iPoNumber.substring( 1 ); // removes the first apostrophe
      lEvtEventDataMap.put( "EVENT_SDESC", "\'S_IN_" + lPoNumber );
      lEvtEventDataMap.put( "EVENT_STATUS_CD", "\'IXPEND\'" );
      lEvtEventDataMap.put( "EVENT_TYPE_CD", "\'IX\'" );

      Assert.assertTrue( "Verify values in EVT_EVENT(2): ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EVT_EVENT, lEvtEventDataMap ) ) );
      // Third Entry
      if ( aEventRows == 3 ) {
         lEvtEventDataMap.put( "EVENT_SDESC", "\'S_OUT_" + lPoNumber );
         Assert.assertTrue( "Verify values in EVT_EVENT(3): ", runQueryReturnsOneRow(
               TableUtil.findRecordInDatabase( TableUtil.EVT_EVENT, lEvtEventDataMap ) ) );
      }
   }


   /**
    * This method will perform the verification for SCHED_STASK table
    *
    * @param aEventIds
    *           - This is EventId created during data setup steps
    */
   public void verifySchedTask( simpleIDs aEventIds ) {
      Map<String, String> lSchedStaskDataMap = new LinkedHashMap<>();
      lSchedStaskDataMap.put( "SCHED_DB_ID", aEventIds.getNO_DB_ID() );
      lSchedStaskDataMap.put( "SCHED_ID", aEventIds.getNO_ID() );
      lSchedStaskDataMap.put( "RO_VENDOR_DB_ID", iVendorCd.getNO_DB_ID() );
      lSchedStaskDataMap.put( "RO_VENDOR_ID", iVendorCd.getNO_ID() );
      lSchedStaskDataMap.put( "RO_REF_SDESC", iPoNumber );

      if ( iVendorWpNo != null ) {
         lSchedStaskDataMap.put( "VENDOR_WO_REF_SDESC", iVendorWpNo );
      }
      Assert.assertTrue( "Verify values in SCHED_STASK: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.SCHED_STASK, lSchedStaskDataMap ) ) );
   }


   /**
    *
    * This method will perform the verification of CSV file load.
    *
    */
   public void VerifyPoStagingTables() {
      // Verifying data into C_PO_HEADER table
      Map<String, String> lCPoHeaderMap = new LinkedHashMap<>();

      lCPoHeaderMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoHeaderMap.put( "ORD_TYPE_CD", "\'PURCHASE\'" );
      lCPoHeaderMap.put( "PRIORITY_CD", "\'NORMAL\'" );
      lCPoHeaderMap.put( "CONTACT_HR_CD", "\'ADMIN\'" );
      lCPoHeaderMap.put( "VENDOR_CD", "\'10001\'" );
      lCPoHeaderMap.put( "VENDOR_ACCOUNT_CD", "\'10001\'" );
      lCPoHeaderMap.put( "BROKER_CD", "\'10001\'" );
      lCPoHeaderMap.put( "BROKER_ACCOUNT_CD", "\'10001\'" );
      lCPoHeaderMap.put( "SHIP_TO_LOCATION_CD", "\'AIRPORT3\'" );
      lCPoHeaderMap.put( "SHIP_TO_CODE", "\'SHOP\'" );
      lCPoHeaderMap.put( "TRANSPORT_TYPE_CD", "\'FEDEX\'" );
      lCPoHeaderMap.put( "RECEIPT_INSP_BOOL", "\'1\'" );
      lCPoHeaderMap.put( "CURRENCY_CD", "\'USD\'" );
      lCPoHeaderMap.put( "EXCHG_QT", "\'1\'" );
      lCPoHeaderMap.put( "ISSUED_DT", "to_date(\'01-01-2018\', \'dd-mm-yyyy\')" );
      lCPoHeaderMap.put( "TERMS_CONDITIONS_CD", "\'TERMS\'" );
      lCPoHeaderMap.put( "FREIGHT_ON_BOARD_CD", "\'FOB\'" );
      lCPoHeaderMap.put( "ORD_EXT_REF", "\'TEST\'" );
      lCPoHeaderMap.put( "BORROW_RATE_CD", "\'RATE_CD\'" );
      lCPoHeaderMap.put( "WP_NAME", "\'PO_TEST123\'" );
      lCPoHeaderMap.put( "SERIAL_NO_OEM", "\'SN000013\'" );
      lCPoHeaderMap.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lCPoHeaderMap.put( "MANUFACT_CD", "\'10001\'" );
      lCPoHeaderMap.put( "VENDOR_WP_NO", "\'V10001\'" );
      lCPoHeaderMap.put( "VENDOR_NOTE", "\'TEST\'" );
      lCPoHeaderMap.put( "RECEIVER_NOTE", "\'TEST\'" );
      lCPoHeaderMap.put( "RE_SHIP_TO_LOCATION_CD", "\'AIRPORT3\'" );
      lCPoHeaderMap.put( "BILL_TO_CD", "\'10001\'" );
      lCPoHeaderMap.put( "CONSIGN_TO_CD", "\'10001\'" );
      lCPoHeaderMap.put( "RECEIPT_ORGANIZATION_CD", "\'10001\'" );
      // Verify the first row of data
      Assert.assertTrue( "Verify values in C_PO_HEADER: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.C_PO_HEADER, lCPoHeaderMap ) ) );
      // Verify the second row of data
      lCPoHeaderMap.put( "PO_NUMBER", "\'PO300001\'" );
      Assert.assertTrue( "Verify values in C_PO_HEADER: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.C_PO_HEADER, lCPoHeaderMap ) ) );

      // Verifying data into C_PO_LINE table
      Map<String, String> lCPoLineMap = new LinkedHashMap<>();

      lCPoLineMap.put( "PO_NUMBER", "\'PO300000\'" );
      lCPoLineMap.put( "PO_LINE_TYPE_CD", "\'REPAIR\'" );
      lCPoLineMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineMap.put( "LINE_LDESC", "\'TEST\'" );
      lCPoLineMap.put( "PART_NO_OEM", "\'ACFT_ASSY_PN1\'" );
      lCPoLineMap.put( "MANUFACT_CD", "\'10001\'" );
      lCPoLineMap.put( "ORDER_QT", "\'1\'" );
      lCPoLineMap.put( "QTY_UNIT_CD", "\'EA\'" );
      lCPoLineMap.put( "UNIT_PRICE", "\'200\'" );
      lCPoLineMap.put( "BASE_UNIT_PRICE", "\'232\'" );
      lCPoLineMap.put( "ACCOUNT_CD", "\'5\'" );
      lCPoLineMap.put( "CONFIRM_UNIT_PRICE_BOOL", "\'1\'" );
      lCPoLineMap.put( "PROMISE_BY_DT", "to_date(\'01-04-2018\', \'dd-mm-yyyy\')" );
      lCPoLineMap.put( "RETURN_BY_DT", "to_date(\'01-04-2019\', \'dd-mm-yyyy\')" );
      lCPoLineMap.put( "CONFIRM_PROMISE_BY_BOOL", "\'1\'" );
      lCPoLineMap.put( "PO_LINE_EXT_SDESC", "\'TEST\'" );
      lCPoLineMap.put( "MAINT_RECEIPT_BOOL", "\'1\'" );
      lCPoLineMap.put( "VENDOR_NOTE", "\'TEST\'" );
      lCPoLineMap.put( "RECEIVER_NOTE", "\'TEST\'" );
      lCPoLineMap.put( "PRICE_TYPE_CD", "\'PURCHASE\'" );

      // Verify the first row of data
      Assert.assertTrue( "Verify values in C_PO_LINE: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.C_PO_LINE, lCPoLineMap ) ) );

      // Verify the first row of data
      lCPoLineMap.put( "PO_NUMBER", "\'PO300001\'" );
      Assert.assertTrue( "Verify values in C_PO_LINE: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.C_PO_LINE, lCPoLineMap ) ) );

      // Verifying data into C_PO_LINE_TAX table
      Map<String, String> lCPoLineTaxMap = new LinkedHashMap<>();

      lCPoLineTaxMap.put( "PO_NUMBER", "\'PO100000\'" );
      lCPoLineTaxMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineTaxMap.put( "TAX_CODE", "\'HST1\'" );
      lCPoLineTaxMap.put( "TAX_RATE", "0.15" );
      // Verify the first row of data
      Assert.assertTrue( "Verify values in C_PO_LINE_TAX: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.C_PO_LINE_TAX, lCPoLineTaxMap ) ) );
      // Verify the second row of data
      lCPoLineTaxMap.put( "PO_NUMBER", "\'PO100001\'" );
      Assert.assertTrue( "Verify values in C_PO_LINE_TAX: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.C_PO_LINE_TAX, lCPoLineTaxMap ) ) );

      // Verifying data into C_PO_LINE_CHARGE
      Map<String, String> lCPoLineChargeMap = new LinkedHashMap<>();

      lCPoLineChargeMap.put( "PO_NUMBER", "\'PO100000\'" );
      lCPoLineChargeMap.put( "PO_LINE_ORD", "\'1\'" );
      lCPoLineChargeMap.put( "CHARGE_CODE", "\'BORROW\'" );
      lCPoLineChargeMap.put( "CHARGE_AMOUNT", "230.00" );

      Assert.assertTrue( "Verify values in C_PO_LINE_CHARGE: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.C_PO_LINE_CHARGE, lCPoLineChargeMap ) ) );

      // Verify the second row of data
      lCPoLineChargeMap.put( "PO_NUMBER", "\'PO100001\'" );
      Assert.assertTrue( "Verify values in C_PO_LINE_CHARGE: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.C_PO_LINE_CHARGE, lCPoLineChargeMap ) ) );
   }

}
