
package com.mxi.mx.web.query.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * util class that provides assert methods for common data
 *
 * @author slevert
 */
public class TaskSearchAssertUtil {

   /**
    * assert task #1
    *
    * @param aTasksDs
    *           dataset to test
    *
    * @throws ParseException
    *            if parsing fails
    */
   public void assertTask1( DataSet aTasksDs ) throws ParseException {
      testRow( aTasksDs, new EventKey( "4650:100" ), "aircraft test task 1",
            new AircraftKey( "4650:1" ), "aircraft test - 1", new InventoryKey( "4650:2" ),
            "aircraft test - spoliers and drag", "ACTV", null, "TASK100", "WO - 100", null, null,
            "aircraft test task 1", "TASK100", new EventKey( "4650:100" ), null,
            new EventKey( "4650:1" ), "driving task", null,
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "03-Aug-2007 10:00:00" ), "20", "10",
            "DOMAIN", "ENGUNIT", "DATATYPE", "0.1",

            // Plus one day, as have to take into account driver task deviation value if data type
            // is
            // not US.
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "04-Aug-2007 10:00:00" ),
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "31-DEC-2007 03:31" ), false, false,
            false );
   }


   /**
    * assert task #2
    *
    * @param aTasksDs
    *           dataset to test
    */
   public void assertTask2( DataSet aTasksDs ) {
      testRow( aTasksDs, new EventKey( "4650:200" ), "aircraft test task 2",
            new AircraftKey( "4650:1" ), "aircraft test - 1", new InventoryKey( "4650:2" ),
            "aircraft test - spoliers and drag", "ACTV", null, "TASK200", null, "RO - 200", null,
            "aircraft test task 2", "TASK200", new EventKey( "4650:200" ), null, null, null, null,
            null, null, null, null, null, null, null, null, null, false, true, false );
   }


   /**
    * assert task #3
    *
    * @param aTasksDs
    *           dataset to test
    *
    * @throws ParseException
    *            if parsing fails
    */
   public void assertTask3( DataSet aTasksDs ) throws ParseException {
      testRow( aTasksDs, new EventKey( "4650:300" ), "aircraft test task 3",
            new AircraftKey( "4650:1" ), "aircraft test - 1", new InventoryKey( "4650:2" ),
            "aircraft test - spoliers and drag", "ACTV", null, "TASK300", null, null,
            "Vendor WO - 300", "aircraft test task 3", "TASK300", new EventKey( "4650:300" ), null,
            null, null, null, null, null, null, null, null, null, null, null,
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "02-JAN-2007 03:33" ), true, true,
            false );
   }


   /**
    * assert task #4
    *
    * @param aTasksDs
    *           dataset to test
    *
    * @throws ParseException
    *            if parsing fails
    */
   public void assertTask4( DataSet aTasksDs ) throws ParseException {
      testRow( aTasksDs, new EventKey( "4650:400" ), "aircraft test task 4",
            new AircraftKey( "4650:1" ), "aircraft test - 1", new InventoryKey( "4650:2" ),
            "aircraft test - spoliers and drag", "ACTV", null, "TASK400", null, null,
            "Vendor WO - 400", "aircraft test task 4", "TASK400", new EventKey( "4650:400" ), null,
            null, null, null, null, null, null, null, null, null, null, null,
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "02-JAN-2007 03:34" ), true, false,
            false );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           The dataset
    * @param aEvent
    *           The event key
    * @param aTaskName
    *           The task name
    * @param aAircraft
    *           The aircraft key
    * @param aAircraftDesc
    *           The aircraft desc
    * @param aInventory
    *           The inventory key
    * @param aInventoryDesc
    *           The inventory desc
    * @param aUserStatusCd
    *           The user status
    * @param aWorkTypeCd
    *           The work type
    * @param aBarcodeSdesc
    *           The barcode
    * @param aWoRefSdesc
    *           The WO number
    * @param aRoRefSdesc
    *           The Repair Order Number
    * @param aVendorWoRefDesc
    *           The Vendor WO Number
    * @param aCheckName
    *           The check name
    * @param aCheckBarcode
    *           The check barcode
    * @param aCheckKey
    *           The Check Key
    * @param aSchedPriorityCd
    *           The Reference Code
    * @param aDrivingEventKey
    *           The driving event key
    * @param aDrivingEventSdesc
    *           The driving event desc
    * @param aDrivingBarcodeSdesc
    *           The driving event barcode
    * @param aSchedDeadDt
    *           The scheduled deadline for the event
    * @param aUsageRemQt
    *           The difference between the current usuage count and SCHED_DEAD_QT
    * @param aDeviationQt
    *           The amount by which the deadline can "slip" past its due date before being
    *           considered overdue
    * @param aDomainTypeCd
    *           The Domain type
    * @param aEngUnitCd
    *           The Engineering Unit Reference
    * @param aDataTypeCd
    *           The Data Type
    * @param aEngUnitMultQt
    *           The multiplier
    * @param aSortSchedDeadDt
    *           The scheduled deadline for the event
    * @param aCompletedOn
    *           The date that the event actually ended.
    * @param aPartsReadyBool
    *           The parts ready boolean
    * @param aToolsReadyBool
    *           The tools ready boolean
    * @param aSoftDeadline
    *           DOCUMENT_ME
    */
   private void testRow( DataSet aDs, EventKey aEvent, String aTaskName, AircraftKey aAircraft,
         String aAircraftDesc, InventoryKey aInventory, String aInventoryDesc, String aUserStatusCd,
         String aWorkTypeCd, String aBarcodeSdesc, String aWoRefSdesc, String aRoRefSdesc,
         String aVendorWoRefDesc, String aCheckName, String aCheckBarcode, EventKey aCheckKey,
         String aSchedPriorityCd, EventKey aDrivingEventKey, String aDrivingEventSdesc,
         String aDrivingBarcodeSdesc, Date aSchedDeadDt, String aUsageRemQt, String aDeviationQt,
         String aDomainTypeCd, String aEngUnitCd, String aDataTypeCd, String aEngUnitMultQt,
         Date aSortSchedDeadDt, Date aCompletedOn, Boolean aPartsReadyBool, Boolean aToolsReadyBool,
         Boolean aSoftDeadline ) {
      MxAssert.assertEquals( "event_key", aEvent, new EventKey( aDs.getString( "event_key" ) ) );
      MxAssert.assertEquals( "event_sdesc", aTaskName, aDs.getString( "event_sdesc" ) );
      MxAssert.assertEquals( "aircraft_key", aAircraft,
            new AircraftKey( aDs.getString( "aircraft_key" ) ) );
      MxAssert.assertEquals( "aircraft_sdesc", aAircraftDesc, aDs.getString( "aircraft_sdesc" ) );
      MxAssert.assertEquals( "inv_no_key", aInventory,
            new InventoryKey( aDs.getString( "inv_no_key" ) ) );

      MxAssert.assertEquals( "inv_no_sdesc", aInventoryDesc, aDs.getString( "inv_no_sdesc" ) );
      MxAssert.assertEquals( "user_status_cd", aUserStatusCd, aDs.getString( "user_status_cd" ) );
      MxAssert.assertEquals( "work_type_cd", aWorkTypeCd, aDs.getString( "work_type_cd" ) );
      MxAssert.assertEquals( "barcode_sdesc", aBarcodeSdesc, aDs.getString( "barcode_sdesc" ) );
      MxAssert.assertEquals( "wo_ref_sdesc", aWoRefSdesc, aDs.getString( "wo_ref_sdesc" ) );
      MxAssert.assertEquals( "ro_ref_sdesc", aRoRefSdesc, aDs.getString( "ro_ref_sdesc" ) );
      MxAssert.assertEquals( "vendor_wo_ref_sdesc", aVendorWoRefDesc,
            aDs.getString( "vendor_wo_ref_sdesc" ) );
      MxAssert.assertEquals( "check_name", aCheckName, aDs.getString( "check_name" ) );
      MxAssert.assertEquals( "check_barcode", aCheckBarcode, aDs.getString( "check_barcode" ) );

      if ( aDs.getString( "check_key" ) == null ) {
         MxAssert.assertEquals( "check_key", aCheckKey, null );
      } else {
         MxAssert.assertEquals( "check_key", aCheckKey,
               new EventKey( aDs.getString( "check_key" ) ) );
      }

      MxAssert.assertEquals( "sched_priority_cd", aSchedPriorityCd,
            aDs.getString( "sched_priority_cd" ) );

      if ( aDs.getString( "driving_event_key" ) == null ) {
         MxAssert.assertEquals( "driving_event_key", aDrivingEventKey, null );
      } else {
         MxAssert.assertEquals( "driving_event_key", aDrivingEventKey,
               new EventKey( aDs.getString( "driving_event_key" ) ) );
      }

      MxAssert.assertEquals( "driving_event_sdesc", aDrivingEventSdesc,
            aDs.getString( "driving_event_sdesc" ) );
      MxAssert.assertEquals( "driving_barcode_sdesc", aDrivingBarcodeSdesc,
            aDs.getString( "driving_barcode_sdesc" ) );
      MxAssert.assertEquals( "sched_dead_dt", aSchedDeadDt, aDs.getDate( "sched_dead_dt" ) );

      MxAssert.assertEquals( "usage_rem_qt", aUsageRemQt, aDs.getString( "usage_rem_qt" ) );
      MxAssert.assertEquals( "deviation_qt", aDeviationQt, aDs.getString( "deviation_qt" ) );
      MxAssert.assertEquals( "domain_type_cd", aDomainTypeCd, aDs.getString( "domain_type_cd" ) );
      MxAssert.assertEquals( "eng_unit_cd", aEngUnitCd, aDs.getString( "eng_unit_cd" ) );
      MxAssert.assertEquals( "data_type_cd", aDataTypeCd, aDs.getString( "data_type_cd" ) );
      MxAssert.assertEquals( "eng_unit_mult_qt", aEngUnitMultQt,
            aDs.getString( "eng_unit_mult_qt" ) );
      MxAssert.assertEquals( "ext_sched_dead_dt", aSortSchedDeadDt,
            aDs.getDate( "ext_sched_dead_dt" ) );
      MxAssert.assertEquals( "completed_on", aCompletedOn, aDs.getDate( "completed_on" ) );

      MxAssert.assertEquals( "parts_ready_bool", aPartsReadyBool.booleanValue(),
            aDs.getBoolean( "parts_ready_bool" ) );
      MxAssert.assertEquals( "tools_ready_bool", aToolsReadyBool.booleanValue(),
            aDs.getBoolean( "tools_ready_bool" ) );

      MxAssert.assertEquals( "soft_deadline", aSoftDeadline.booleanValue(),
            aDs.getBoolean( "soft_deadline" ) );
   }
}
