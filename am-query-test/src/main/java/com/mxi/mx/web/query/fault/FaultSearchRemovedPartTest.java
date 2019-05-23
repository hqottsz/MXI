
package com.mxi.mx.web.query.fault;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.web.search.fault.ByRemovedPart;


/**
 * Tests the query com.mxi.mx.query.fault.FaultsearchBasic
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FaultSearchRemovedPartTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   /**
    * Tests the query with an invalid removed part
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInvalidRemovedPart() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      /* Invalid Part no */

      ByRemovedPart lTestObject = new ByRemovedPart();
      lTestObject.addSearchArgs( lArgs, "INVALID PART" );

      // execute the query
      DataSet lFaultsDs = execute( lArgs, 100, 4650, 1 );

      // There should be 0 row
      MxAssert.assertEquals( "Number of retrieved rows", 0, lFaultsDs.getRowCount() );
   }


   /**
    * Tests the search by removed part query
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSearchByRemovedPart() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      // build the WHERE_CLAUSE for the query

      ByRemovedPart lTestObject = new ByRemovedPart();
      lTestObject.addSearchArgs( lArgs, "CFM56-3C" );

      // execute the query
      DataSet lFaultsDs = execute( lArgs, 100, 4650, 1 );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lFaultsDs.getRowCount() );

      lFaultsDs.next();

      testRow( lFaultsDs, new EventKey( "4650:107452" ), "Test Found Fault Card", "0", "MINOR",
            "OPEN", "Document Reference", null, new TaskKey( "4650:107455" ),
            "Test Found Fault Card", "NONE", null, "T0000SWM", null, null, null, null, null, null,
            null, null, "T0000SWM", new AircraftKey( "5000000:67919" ), "Airbus A319/A320 - CFKOJ",
            new InventoryKey( "5000000:67919" ), "Airbus A319/A320 - CFKOJ",
            new AssemblyKey( "5000000:A320" ), "Airbus A319/A320", "A320", null,
            new SimpleDateFormat( "dd-MMM-yyyy hh:mm" ).parse( "13-JUN-2005 10:57" ), null, null,
            null, null, null, null, null, null,
            new FlightLegId( "14B0687242AA410FA60371D3594FCDD3" ), null, "AP - Flight - 01" );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), FaultSearchRemovedPartTest.class,
            FaultSearchByTypeData.getDataFile() );
   }


   /**
    * Execute the query
    *
    * @param aArgs
    *           the argument set
    * @param aRowNum
    *           number of rows
    * @param aHrDbId
    *           hr_db_id
    * @param aHrId
    *           hr_id
    *
    * @return the result
    */
   private DataSet execute( DataSetArgument aArgs, Integer aRowNum, Integer aHrDbId,
         Integer aHrId ) {

      // Build query arguments
      aArgs.add( "aRowNum", aRowNum );
      aArgs.add( "aHrDbId", aHrDbId );
      aArgs.add( "aHrId", aHrId );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), aArgs );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           The dataset
    * @param aEventKey
    *           The Event Key
    * @param aFaultSdesc
    *           The Fault Sdesc
    * @param aHistBool
    *           The History Boolean
    * @param aFailSevCd
    *           The FailSevCd
    * @param aFaultStatus
    *           The FaultStatus
    * @param aDocRefSdesc
    *           The Doc Ref Sdesc
    * @param aDeferRefSdesc
    *           The Defer Ref Sdesc
    * @param aCorrTaskKey
    *           The Corr Task Key
    * @param aCorrTaskSdesc
    *           The Corr Task Sdesc
    * @param aSchedPriorityCd
    *           The Sched Priority Cd
    * @param aTaskRefSdesc
    *           The Task Ref Sdesc
    * @param aCorrBarcodeSdesc
    *           The Corr Barcode Sdesc
    * @param aCheckKey
    *           The Check Key
    * @param aCheckName
    *           The Check Name
    * @param aWoLineId
    *           The Wo Line Id
    * @param aWoRefSdesc
    *           The Wo Ref Sdesc
    * @param aRoLineSdesc
    *           The Ro Line Sdesc
    * @param aVendorKey
    *           The Vendor Key
    * @param aRoRefSdesc
    *           The Ro Ref Sdesc
    * @param aVendorWoRefSdesc
    *           The Vendor Wo Ref Sdesc
    * @param aRootBarcodeSdesc
    *           The Root Barcode Sdesc
    * @param aAircraftKey
    *           The Aircraft Key
    * @param aAircraftName
    *           The Aircraft Name
    * @param aInventoryKey
    *           The Inventory Key
    * @param aInventoryName
    *           The Inventory Name
    * @param aAssemblyKey
    *           The Assembly Key
    * @param aAssemblyName
    *           The Assembly Name
    * @param aAssemblyBomCd
    *           The Assemblt Bom Cd
    * @param aSchedDeadDt
    *           The Sched Dead Dt
    * @param aRaisedGdt
    *           The Raised Gdt
    * @param aCorrEventEnd
    *           The Corr Event End
    * @param aFaultCompleteDate
    *           The fault Complete Date
    * @param aDeviationQt
    *           The Deviation Qt
    * @param aDomainTypeCd
    *           The Domain Type Cd
    * @param aUsageRemQt
    *           The Usage rem Qt
    * @param aEngUnitCd
    *           The End Unit Cd
    * @param aDataTypeCd
    *           The Data Type Cd
    * @param aEngUnitMultQt
    *           The Eng Unit Mult Qt
    * @param aFlightKey
    *           The Flight Key
    * @param aFlightLogRef
    *           The Flgiht Log Ref
    * @param aFlightName
    *           The Flight Name
    */
   private void testRow( DataSet aDs, EventKey aEventKey, String aFaultSdesc, String aHistBool,
         String aFailSevCd, String aFaultStatus, String aDocRefSdesc, String aDeferRefSdesc,
         TaskKey aCorrTaskKey, String aCorrTaskSdesc, String aSchedPriorityCd, String aTaskRefSdesc,
         String aCorrBarcodeSdesc, TaskKey aCheckKey, String aCheckName, String aWoLineId,
         String aWoRefSdesc, String aRoLineSdesc, VendorKey aVendorKey, String aRoRefSdesc,
         String aVendorWoRefSdesc, String aRootBarcodeSdesc, AircraftKey aAircraftKey,
         String aAircraftName, InventoryKey aInventoryKey, String aInventoryName,
         AssemblyKey aAssemblyKey, String aAssemblyName, String aAssemblyBomCd, String aSchedDeadDt,
         Date aRaisedGdt, Date aCorrEventEnd, Date aFaultCompleteDate, String aDeviationQt,
         String aDomainTypeCd, String aUsageRemQt, String aEngUnitCd, String aDataTypeCd,
         String aEngUnitMultQt, FlightLegId aFlightKey, String aFlightLogRef, String aFlightName ) {
      MxAssert.assertEquals( "event_key", aEventKey, new EventKey( aDs.getString( "event_key" ) ) );
      MxAssert.assertEquals( "fault_sdesc", aFaultSdesc, aDs.getString( "fault_sdesc" ) );
      MxAssert.assertEquals( "hist_bool", aHistBool, aDs.getString( "hist_bool" ) );
      MxAssert.assertEquals( "fail_sev_cd", aFailSevCd, aDs.getString( "fail_sev_cd" ) );
      MxAssert.assertEquals( "fault_status", aFaultStatus, aDs.getString( "fault_status" ) );
      MxAssert.assertEquals( "doc_ref_sdesc", aDocRefSdesc, aDs.getString( "doc_ref_sdesc" ) );
      MxAssert.assertEquals( "defer_ref_sdesc", aDeferRefSdesc,
            aDs.getString( "defer_ref_sdesc" ) );
      MxAssert.assertEquals( "corrective_task_key", aCorrTaskKey,
            new TaskKey( aDs.getString( "corrective_task_key" ) ) );
      MxAssert.assertEquals( "corrective_task_sdesc", aCorrTaskSdesc,
            aDs.getString( "corrective_task_sdesc" ) );
      MxAssert.assertEquals( "sched_priority_cd", aSchedPriorityCd,
            aDs.getString( "sched_priority_cd" ) );
      MxAssert.assertEquals( "task_ref_sdesc", aTaskRefSdesc, aDs.getString( "task_ref_sdesc" ) );
      MxAssert.assertEquals( "corr_barcode_sdesc", aCorrBarcodeSdesc,
            aDs.getString( "corr_barcode_sdesc" ) );
      if ( aDs.getString( "check_key" ) == null ) {
         MxAssert.assertEquals( "check_key", aCheckKey, null );
      } else {
         MxAssert.assertEquals( "check_key", aCheckKey,
               new TaskKey( aDs.getString( "check_key" ) ) );
      }

      MxAssert.assertEquals( "check_name", aCheckName, aDs.getString( "check_name" ) );
      MxAssert.assertEquals( "wo_line_id", aWoLineId, aDs.getString( "wo_line_id" ) );
      MxAssert.assertEquals( "wo_ref_sdesc", aWoRefSdesc, aDs.getString( "wo_ref_sdesc" ) );
      MxAssert.assertEquals( "ro_line_sdesc", aRoLineSdesc, aDs.getString( "ro_line_sdesc" ) );
      if ( aDs.getString( "vendor_key" ) == null ) {
         MxAssert.assertEquals( "vendor_key", aVendorKey, null );
      } else {
         MxAssert.assertEquals( "vendor_key", aVendorKey,
               new VendorKey( aDs.getString( "vendor_key" ) ) );
      }

      MxAssert.assertEquals( "ro_ref_sdesc", aRoRefSdesc, aDs.getString( "ro_ref_sdesc" ) );
      MxAssert.assertEquals( "vendor_wo_ref_sdesc", aVendorWoRefSdesc,
            aDs.getString( "vendor_wo_ref_sdesc" ) );
      MxAssert.assertEquals( "root_barcode_sdesc", aRootBarcodeSdesc,
            aDs.getString( "root_barcode_sdesc" ) );
      if ( aDs.getString( "aircraft_key" ) == null ) {
         MxAssert.assertEquals( "aircraft_key", aAircraftKey, null );
      } else {
         MxAssert.assertEquals( "aircraft_key", aAircraftKey,
               new AircraftKey( aDs.getString( "aircraft_key" ) ) );
      }

      MxAssert.assertEquals( "aircraft_name", aAircraftName, aDs.getString( "aircraft_name" ) );
      if ( aDs.getString( "inventory_key" ) == null ) {
         MxAssert.assertEquals( "inventory_key", aInventoryKey, null );
      } else {
         MxAssert.assertEquals( "inventory_key", aInventoryKey,
               new InventoryKey( aDs.getString( "inventory_key" ) ) );
      }

      MxAssert.assertEquals( "inventory_name", aInventoryName, aDs.getString( "inventory_name" ) );
      if ( aDs.getString( "assembly_key" ) == null ) {
         MxAssert.assertEquals( "assembly_key", aAssemblyKey, null );
      } else {
         MxAssert.assertEquals( "assembly_key", aAssemblyKey,
               new AssemblyKey( aDs.getString( "assembly_key" ) ) );
      }

      MxAssert.assertEquals( "assmbl_name", aAssemblyName, aDs.getString( "assmbl_name" ) );
      MxAssert.assertEquals( "assmbl_bom_cd", aAssemblyBomCd, aDs.getString( "assmbl_bom_cd" ) );
      MxAssert.assertEquals( "sched_dead_dt", aSchedDeadDt, aDs.getString( "sched_dead_dt" ) );
      MxAssert.assertEquals( "raised_gdt", aRaisedGdt, aDs.getDate( "raised_gdt" ) );
      MxAssert.assertEquals( "corr_event_end", aCorrEventEnd, aDs.getDate( "corr_event_end" ) );
      MxAssert.assertEquals( "fault_completed_date", aFaultCompleteDate,
            aDs.getDate( "fault_completed_date" ) );
      MxAssert.assertEquals( "deviation_qt", aDeviationQt, aDs.getString( "deviation_qt" ) );
      MxAssert.assertEquals( "domain_type_cd", aDomainTypeCd, aDs.getString( "domain_type_cd" ) );
      MxAssert.assertEquals( "usage_rem_qt", aUsageRemQt, aDs.getString( "usage_rem_qt" ) );
      MxAssert.assertEquals( "eng_unit_cd", aEngUnitCd, aDs.getString( "eng_unit_cd" ) );
      MxAssert.assertEquals( "data_type_cd", aDataTypeCd, aDs.getString( "data_type_cd" ) );
      MxAssert.assertEquals( "eng_unit_mult_qt", aEngUnitMultQt,
            aDs.getString( "eng_unit_mult_qt" ) );
      if ( aDs.getString( "flight_key" ) == null ) {
         MxAssert.assertEquals( "flight_key", aFlightKey, null );
      } else {
         MxAssert.assertEquals( "flight_key", aFlightKey,
               aDs.getId( FlightLegId.class, "flight_key" ) );
      }

      MxAssert.assertEquals( "flight_log_reference", aFlightLogRef,
            aDs.getString( "flight_log_reference" ) );
      MxAssert.assertEquals( "flight_name", aFlightName, aDs.getString( "flight_name" ) );
   }
}
