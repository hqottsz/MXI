package com.mxi.mx.core.maint.plan.baselineloader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.AbstractDatabaseConnection;
import com.mxi.mx.util.TableUtil;


/**
 * This class will verify all the data after import
 *
 */
public class PartAdvisoryTest extends AbstractDatabaseConnection {

   simpleIDs iAdvisoryTypeCd;
   simpleIDs iPartNoOem;
   simpleIDs iManufactCd;
   simpleIDs iAdvsryIds;
   simpleIDs iAddByIds;
   String iAdvsryDt;
   String iAdvsryNote;
   String iAdvsryName;


   public void findIds( Map<String, String> aDataMap ) {

      String lQuery = "";

      for ( Entry<String, String> lEntry : aDataMap.entrySet() ) {
         switch ( lEntry.getKey().toUpperCase() ) {
            case ( "ADVSRY_NAME" ):
               lQuery = "Select Advsry_DB_ID, Advsry_ID from EQP_ADVSRY where advsry_name = "
                     + lEntry.getValue();
               iAdvsryIds = getIDs( lQuery, "ADVSRY_DB_ID", "ADVSRY_ID" );
               iAdvsryName = lEntry.getValue();
               break;

            case ( "PART_NO_OEM" ):
               lQuery = "Select PART_NO_Db_ID, PART_NO_ID from eqp_part_no where PART_NO_OEM = "
                     + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iPartNoOem = getIDs( lQuery, "PART_NO_DB_ID", "PART_NO_ID" );
               break;
            case ( "ADVSRY_TYPE_CD" ):
               lQuery =
                     "SELECT REF_ADVSRY_TYPE.ADVSRY_TYPE_DB_ID, REF_ADVSRY_TYPE.ADVSRY_TYPE_CD from REF_ADVSRY_TYPE where ADVSRY_TYPE_CD = "
                           + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iAdvisoryTypeCd = getIDs( lQuery, "ADVSRY_TYPE_DB_ID", "ADVSRY_TYPE_CD" );
               break;
            case ( "ADDED_BY" ):
               lQuery =
                     "Select HR_DB_ID, HR_ID from UTL_USER INNER JOIN ORG_HR ON UTL_USER.USER_ID = org_hr.user_id where USERNAME = "
                           + lEntry.getValue();
               checkForOneRowResult( lQuery, lEntry.getKey() );
               iAddByIds = getIDs( lQuery, "HR_DB_ID", "HR_ID" );
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

      String lQuery = aQuery.replaceAll( ".*from", "Select Count(*) from" );
      Assert.assertTrue( aColumnName + ": Query does not return one row: " + aQuery,
            countRowsOfQuery( lQuery ) == 1 );

   }


   /**
    *
    * This will find the required IDs from Staging table data and verify the data is loaded properly
    * into eqp_advsry table.
    *
    * @param aPartAdvisoryMap
    *           Data loaded into the staging table
    */
   public void verifyEqpAdvsry( Map<String, String> aPartAdvisoryMap ) {

      Map<String, String> lEqpAdvsryMap = new LinkedHashMap<>();

      lEqpAdvsryMap.put( "ADVSRY_DB_ID", iAdvsryIds.getNO_DB_ID() );
      lEqpAdvsryMap.put( "ADVSRY_ID", iAdvsryIds.getNO_ID() );
      lEqpAdvsryMap.put( "ADVSRY_NAME", iAdvsryName );
      if ( aPartAdvisoryMap.get( "ADVSRY_DT" ) != null )
         lEqpAdvsryMap.put( "ADVSRY_DT", aPartAdvisoryMap.get( "ADVSRY_DT" ) );

      if ( aPartAdvisoryMap.get( "ADDED_BY" ) == null ) {
         Map<String, String> TempMap = new LinkedHashMap<>();
         TempMap.put( "ADDED_BY", "\'ADMIN\'" );
         findIds( TempMap );
      }
      lEqpAdvsryMap.put( "ADVSRY_HR_DB_ID", iAddByIds.getNO_DB_ID() );
      lEqpAdvsryMap.put( "ADVSRY_HR_ID", iAddByIds.getNO_ID() );
      lEqpAdvsryMap.put( "ADVSRY_TYPE_DB_ID", iAdvisoryTypeCd.getNO_DB_ID() );
      lEqpAdvsryMap.put( "ADVSRY_TYPE_CD", "\'" + iAdvisoryTypeCd.getNO_ID() + "\'" );
      lEqpAdvsryMap.put( "ADVSRY_NOTE", aPartAdvisoryMap.get( "ADVSRY_NOTE" ) );

      Assert.assertTrue( "Verify values in EQP_ADVSRY: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_ADVSRY, lEqpAdvsryMap ) ) );
      if ( aPartAdvisoryMap.get( "ADVSRY_DT" ) == null )
         Assert.assertTrue( "Check the ADVSRY_DT: ", CheckAdvsryDt(
               TableUtil.findRecordInDatabase( TableUtil.EQP_ADVSRY, lEqpAdvsryMap ) ) );
   }


   /**
    * Check the value of ADVSRY_DT by confirming that the value was just imported within 60 seconds.
    *
    * @param aQuery
    *           used to verify the data in the EQP_ADVSRY table.
    * @return a boolean on whether time difference between import is less than 60 seconds
    */
   private boolean CheckAdvsryDt( String aQuery ) {

      // Ensures the user is getting the same Adnvsry date identified in the verification query.
      String lQuery = aQuery.replaceAll( ".*from",
            "SELECT cast((sysdate-ADVSRY_DT) * 86400 as Integer) DIFF_SECONDS from" );
      // DB does the date/time calculation
      int lDateDiff = getIntValueFromQuery( lQuery, "DIFF_SECONDS" );

      if ( ( lDateDiff >= 0 ) && lDateDiff < 60 )
         return true;
      else
         return false;
   }


   /**
    * This will get the system date from the database
    *
    * @return system date
    *
    */

   public void verifyEqpPartAdvsry( String aSerialRange, String aLotRange ) {
      Map<String, String> lEqpPartAdvsryMap = new LinkedHashMap<>();
      lEqpPartAdvsryMap.put( "ADVSRY_DB_ID", iAdvsryIds.getNO_DB_ID() );
      lEqpPartAdvsryMap.put( "ADVSRY_ID", iAdvsryIds.getNO_ID() );
      lEqpPartAdvsryMap.put( "PART_NO_DB_ID", iPartNoOem.getNO_DB_ID() );
      lEqpPartAdvsryMap.put( "PART_NO_ID", iPartNoOem.getNO_ID() );
      lEqpPartAdvsryMap.put( "ACTIVE_BOOL", "\'1\'" );
      if ( aSerialRange != null )
         lEqpPartAdvsryMap.put( "SERIAL_NO_RANGE", aSerialRange );
      if ( aLotRange != null )
         lEqpPartAdvsryMap.put( "LOT_NO_RANGE", aLotRange );

      Assert.assertTrue( "Verify values in EQP_PART_ADVSRY: ", runQueryReturnsOneRow(
            TableUtil.findRecordInDatabase( TableUtil.EQP_PART_ADVSRY, lEqpPartAdvsryMap ) ) );
   }


   /**
    *
    * Verify the INV_ADVSRY table values
    *
    * @param aSerialRange
    *           - used to get the Serial range value was just imported
    * @param aLotRange
    *           - used to get the lot range value that was just imported
    */
   public void verifyInvAdvsry( String aSerialRange, String aLotRange ) {

      String lQuery = "SELECT inv_inv.inv_no_db_id, inv_inv.inv_no_id FROM eqp_part_no "
            + "INNER JOIN inv_inv ON eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND "
            + "eqp_part_no.part_no_id = inv_inv.part_no_id WHERE  EQP_PART_NO.PART_NO_DB_ID = "
            + iPartNoOem.getNO_DB_ID() + " and EQP_PART_NO.PART_NO_ID = " + iPartNoOem.getNO_ID();

      // This will append the Lot and serial Ranges to the query
      String lLotRange = getQueryForRange( aLotRange );
      String lSerialRange = getQueryForRange( aSerialRange );
      if ( lLotRange != null )
         lQuery += " AND " + lLotRange;

      if ( lSerialRange != null )
         lQuery += " AND " + lSerialRange;

      String[] lIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( lIds ) );
      List<ArrayList<String>> lInvIds = execute( lQuery, lfields );

      String lWhereArgInv = "";
      for ( int i = 0; i < lInvIds.size(); i++ ) {
         if ( i == 0 )
            lWhereArgInv += "(INV_NO_DB_ID = \'" + lInvIds.get( i ).get( 0 )
                  + "\' AND INV_NO_ID = \'" + lInvIds.get( i ).get( 1 ) + "\')";
         else
            lWhereArgInv += "OR (INV_NO_DB_ID = \'" + lInvIds.get( i ).get( 0 )
                  + "\' AND INV_NO_ID = \'" + lInvIds.get( i ).get( 1 ) + "\')";
      }

      lQuery = "Select Count(*) from INV_ADVSRY where (" + lWhereArgInv + ") AND ADVSRY_DB_ID = "
            + iAdvsryIds.getNO_DB_ID() + " AND ADVSRY_ID = " + iAdvsryIds.getNO_ID()
            + " And active_bool = 1";

      Assert.assertTrue( "Verify values in INV_ADVSRY: ",
            countRowsOfQuery( lQuery ) == lInvIds.size() );

   }


   /**
    * This will convert serial and lot ranges into SQL so it can be used to verify data has been
    * imported.
    *
    * @param aSerialRange
    * @return
    */
   private String getQueryForRange( String aRange ) {
      String lQuery = null;

      if ( aRange != null )
         switch ( aRange ) {
            case ( "\'\"SN000001\"-\"SN000013\"\'" ):
               lQuery = "inv_inv.serial_no_oem BETWEEN 'SN000001' AND 'SN000013'";
               break;
            case ( "\'\"350\"-\"351\"\'" ):
               lQuery = "inv_inv.lot_oem_tag BETWEEN '350' AND '351'";
               break;
            case ( "\'\"65\"-\"75\"\'" ):
               lQuery = "inv_inv.lot_oem_tag BETWEEN '65' AND '75'";
               break;
         }

      return lQuery;
   }

}
