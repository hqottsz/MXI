
package com.mxi.mx.web.query.fault;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;


/**
 * Unit test for com.mxi.mx.core.query.fault.OpenFaultsByInventoryForInv.qrx.
 *
 * @author ascullion
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class OpenFaultsByInventoryForInvTest {

   private static final int COLUMN_COUNT = 52;

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {

      SqlLoader.load( sDatabaseConnectionRule.getConnection(),
            OpenFaultsByInventoryForInvTest.class, "OpenFaultsByInventoryForInvTest.sql" );

   }


   @Test
   public void getFaultByInventoryTreeTest() {

      DataSetArgument lArgs = new DataSetArgument();

      // Prepare input arguments
      lArgs.add( "aInvNoDbId", "1234" );
      lArgs.add( "aInvNoId", "0101" );
      lArgs.add( "aShowAssignedFaults", true );
      lArgs.add( "aHideNonExecutableFaults", false );

      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Check dimensions of returned result
      Assert.assertEquals( "RowCount", 1, lDs.getRowCount() );
      Assert.assertEquals( "ColumnCount", COLUMN_COUNT, lDs.getColumnCount() );
      lDs.next();

      Assert.assertEquals( "event_key", "1234:301", lDs.getString( "event_key" ) );
      Assert.assertEquals( "event_sdesc", "TreeRootEvent", lDs.getString( "event_sdesc" ) );
      Assert.assertEquals( "sched_priority_cd", "NONE", lDs.getString( "sched_priority_cd" ) );
      Assert.assertEquals( "work_type_cd", null, lDs.getString( "work_type_cd" ) );
      Assert.assertEquals( "barcode_sdesc", "T01", lDs.getString( "barcode_sdesc" ) );
      Assert.assertEquals( "etops_bool", "1", lDs.getString( "etops_bool" ) );
      Assert.assertEquals( "parts_ready_bool", "1", lDs.getString( "parts_ready_bool" ) );
      Assert.assertEquals( "tools_ready_bool", "1", lDs.getString( "tools_ready_bool" ) );
      Assert.assertEquals( "user_status_cd", "ACTV", lDs.getString( "user_status_cd" ) );
      Assert.assertEquals( "deviation_qt", "0", lDs.getString( "deviation_qt" ) );
      Assert.assertEquals( "usage_rem_qt", "1", lDs.getString( "usage_rem_qt" ) );
      Assert.assertEquals( "sched_dead_dt", "05-Jan-2000 00:00:00",
            lDs.getString( "sched_dead_dt" ) );
      Assert.assertEquals( "domain_type_cd", "US", lDs.getString( "domain_type_cd" ) );
      Assert.assertEquals( "eng_unit_cd", "HOUR", lDs.getString( "eng_unit_cd" ) );
      Assert.assertEquals( "data_type_cd", "HOURS", lDs.getString( "data_type_cd" ) );
      Assert.assertEquals( "precision_qt", "2", lDs.getString( "precision_qt" ) );
      Assert.assertEquals( "eng_unit_mult_qt", "0.041667", lDs.getString( "eng_unit_mult_qt" ) );
      Assert.assertEquals( "ext_sched_dead_dt", "05-Jan-2000 00:00:00",
            lDs.getString( "ext_sched_dead_dt" ) );
      Assert.assertEquals( "sort_due_dt", "01-Jan-2000 00:00:00", lDs.getString( "sort_due_dt" ) );
      Assert.assertEquals( "raised_gdt", "03-Jan-2000 00:00:00", lDs.getString( "raised_gdt" ) );

      byte[] lLegId = { ( byte ) 0x06, ( byte ) 0x01 };
      Assert.assertEquals( "leg_id", Arrays.hashCode( lLegId ),
            Arrays.hashCode( lDs.getBytes( "leg_id" ) ) );

      Assert.assertEquals( "flight_sdesc", "A01", lDs.getString( "flight_sdesc" ) );
      Assert.assertEquals( "fail_sev_cd", "SEV", lDs.getString( "fail_sev_cd" ) );
      Assert.assertEquals( "fail_type_cd", "TYPE", lDs.getString( "fail_type_cd" ) );
      Assert.assertEquals( "fail_priority_cd", "PRIORITY", lDs.getString( "fail_priority_cd" ) );
      Assert.assertEquals( "fail_defer_cd", "DEFER", lDs.getString( "fail_defer_cd" ) );
      Assert.assertEquals( "defer_ref_sdesc", "DeferRef", lDs.getString( "defer_ref_sdesc" ) );
      Assert.assertEquals( "op_restriction_ldesc", "OpRestirction",
            lDs.getString( "op_restriction_ldesc" ) );
      Assert.assertEquals( "inventory_key", "1234:103", lDs.getString( "inventory_key" ) );
      Assert.assertEquals( "inv_no_sdesc", "InvTree", lDs.getString( "inv_no_sdesc" ) );
      Assert.assertEquals( "check_key", "1234:302", lDs.getString( "check_key" ) );
      Assert.assertEquals( "check_sdesc", "CheckEvent", lDs.getString( "check_sdesc" ) );
      Assert.assertEquals( "check_barcode", "T02", lDs.getString( "check_barcode" ) );
      Assert.assertEquals( "wo_ref_sdesc", "WO01", lDs.getString( "wo_ref_sdesc" ) );
      Assert.assertEquals( "ro_ref_sdesc", "P01", lDs.getString( "ro_ref_sdesc" ) );
      Assert.assertEquals( "work_loc_key", "1234:501", lDs.getString( "work_loc_key" ) );
      Assert.assertEquals( "po_key", "1234:701", lDs.getString( "po_key" ) );

      Assert.assertEquals( "plan_by_date", "01-Jan-2000 00:00:00",
            lDs.getString( "plan_by_date" ) );
      Assert.assertEquals( "config_pos_sdesc", "Bot", lDs.getString( "config_pos_sdesc" ) );
      Assert.assertEquals( "prevent_exe_bool", "0", lDs.getString( "prevent_exe_bool" ) );
      Assert.assertEquals( "do_at_next_install_bool", "0",
            lDs.getString( "do_at_next_install_bool" ) );
      Assert.assertEquals( "prevent_exe_review_dt", "02-Jan-2000 00:00:00",
            lDs.getString( "prevent_exe_review_dt" ) );

      // Not checked in this test method
      Assert.assertEquals( "driving_task_key", null, lDs.getString( "driving_task_key" ) );
      Assert.assertEquals( "driving_task_sdesc", null, lDs.getString( "driving_task_sdesc" ) );
      Assert.assertEquals( "driving_barcode_sdesc", null,
            lDs.getString( "driving_barcode_sdesc" ) );

      // Not checked in this test class
      // Testing these fields would involve editing a materialized view
      // (mv_materials_request_status)
      Assert.assertEquals( "request_status_db_id", null, lDs.getString( "request_status_db_id" ) );
      Assert.assertEquals( "request_status_cd", null, lDs.getString( "request_status_cd" ) );
      Assert.assertEquals( "part_request_status", null, lDs.getString( "part_request_status" ) );
      Assert.assertEquals( "part_request_eta", null, lDs.getString( "part_request_eta" ) );
      Assert.assertEquals( "part_request_key", ":", lDs.getString( "part_request_key" ) );
      Assert.assertEquals( "warning_bool", null, lDs.getString( "warning_bool" ) );
      Assert.assertEquals( "material_avail_sort_date", null,
            lDs.getString( "material_avail_sort_date" ) );

   }


   @Test
   public void getFaultByInstalledPartTest() {

      DataSetArgument lArgs = new DataSetArgument();

      // Prepare input arguments
      lArgs.add( "aInvNoDbId", "1234" );
      lArgs.add( "aInvNoId", "0111" );
      lArgs.add( "aShowAssignedFaults", true );
      lArgs.add( "aHideNonExecutableFaults", false );

      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Check dimensions of returned result
      Assert.assertEquals( "RowCount", 1, lDs.getRowCount() );
      Assert.assertEquals( "ColumnCount", COLUMN_COUNT, lDs.getColumnCount() );

      lDs.next();

      Assert.assertEquals( "event_key", "1234:312", lDs.getString( "event_key" ) );
      Assert.assertEquals( "event_sdesc", "InstRootEvent", lDs.getString( "event_sdesc" ) );
      Assert.assertEquals( "sched_priority_cd", "LOW", lDs.getString( "sched_priority_cd" ) );
      Assert.assertEquals( "work_type_cd", null, lDs.getString( "work_type_cd" ) );
      Assert.assertEquals( "barcode_sdesc", "T11", lDs.getString( "barcode_sdesc" ) );
      Assert.assertEquals( "etops_bool", "0", lDs.getString( "etops_bool" ) );
      Assert.assertEquals( "parts_ready_bool", "0", lDs.getString( "parts_ready_bool" ) );
      Assert.assertEquals( "tools_ready_bool", "0", lDs.getString( "tools_ready_bool" ) );
      Assert.assertEquals( "user_status_cd", "CANCEL", lDs.getString( "user_status_cd" ) );
      Assert.assertEquals( "deviation_qt", "2", lDs.getString( "deviation_qt" ) );
      Assert.assertEquals( "usage_rem_qt", "3", lDs.getString( "usage_rem_qt" ) );
      Assert.assertEquals( "sched_dead_dt", "05-Feb-2000 00:00:00",
            lDs.getString( "sched_dead_dt" ) );
      Assert.assertEquals( "domain_type_cd", "US", lDs.getString( "domain_type_cd" ) );
      Assert.assertEquals( "eng_unit_cd", "LNDG", lDs.getString( "eng_unit_cd" ) );
      Assert.assertEquals( "data_type_cd", "LNDG", lDs.getString( "data_type_cd" ) );
      Assert.assertEquals( "precision_qt", "0", lDs.getString( "precision_qt" ) );
      Assert.assertEquals( "eng_unit_mult_qt", "1", lDs.getString( "eng_unit_mult_qt" ) );
      Assert.assertEquals( "ext_sched_dead_dt", "05-Feb-2000 00:00:00",
            lDs.getString( "ext_sched_dead_dt" ) );
      Assert.assertEquals( "sort_due_dt", "01-Feb-2000 00:00:00", lDs.getString( "sort_due_dt" ) );
      Assert.assertEquals( "raised_gdt", "03-Feb-2000 00:00:00", lDs.getString( "raised_gdt" ) );

      Assert.assertEquals( "fail_sev_cd", "SEV", lDs.getString( "fail_sev_cd" ) );
      Assert.assertEquals( "fail_type_cd", "TYPE", lDs.getString( "fail_type_cd" ) );
      Assert.assertEquals( "fail_priority_cd", "PRIORITY", lDs.getString( "fail_priority_cd" ) );
      Assert.assertEquals( "fail_defer_cd", "DEFER", lDs.getString( "fail_defer_cd" ) );
      Assert.assertEquals( "defer_ref_sdesc", "DeferRef", lDs.getString( "defer_ref_sdesc" ) );
      Assert.assertEquals( "op_restriction_ldesc", "OpRestriction",
            lDs.getString( "op_restriction_ldesc" ) );
      Assert.assertEquals( "inventory_key", "1234:111", lDs.getString( "inventory_key" ) );
      Assert.assertEquals( "inv_no_sdesc", "SelInv", lDs.getString( "inv_no_sdesc" ) );

      Assert.assertEquals( "driving_task_key", "1234:314", lDs.getString( "driving_task_key" ) );
      Assert.assertEquals( "driving_task_sdesc", "DrvEvent",
            lDs.getString( "driving_task_sdesc" ) );
      Assert.assertEquals( "driving_barcode_sdesc", "T13",
            lDs.getString( "driving_barcode_sdesc" ) );
      Assert.assertEquals( "plan_by_date", "01-Feb-2000 00:00:00",
            lDs.getString( "plan_by_date" ) );
      Assert.assertEquals( "config_pos_sdesc", "InstRmvd", lDs.getString( "config_pos_sdesc" ) );
      Assert.assertEquals( "prevent_exe_bool", "0", lDs.getString( "prevent_exe_bool" ) );
      Assert.assertEquals( "do_at_next_install_bool", "0",
            lDs.getString( "do_at_next_install_bool" ) );
      Assert.assertEquals( "prevent_exe_review_dt", "02-Feb-2000 00:00:00",
            lDs.getString( "prevent_exe_review_dt" ) );

      // Not checked in this test method
      Assert.assertEquals( "leg_id", 0, Arrays.hashCode( lDs.getBytes( "leg_id" ) ) );
      Assert.assertEquals( "flight_sdesc", null, lDs.getString( "flight_sdesc" ) );

      Assert.assertEquals( "check_key", null, lDs.getString( "check_key" ) );
      Assert.assertEquals( "check_sdesc", null, lDs.getString( "check_sdesc" ) );
      Assert.assertEquals( "check_barcode", null, lDs.getString( "check_barcode" ) );
      Assert.assertEquals( "wo_ref_sdesc", null, lDs.getString( "wo_ref_sdesc" ) );
      Assert.assertEquals( "ro_ref_sdesc", null, lDs.getString( "ro_ref_sdesc" ) );
      Assert.assertEquals( "work_loc_key", null, lDs.getString( "work_loc_key" ) );

      // Not checked in this test class
      // Testing these fields would involve editing a materialized view
      // (mv_materials_request_status)
      Assert.assertEquals( "request_status_db_id", null, lDs.getString( "request_status_db_id" ) );
      Assert.assertEquals( "request_status_cd", null, lDs.getString( "request_status_cd" ) );
      Assert.assertEquals( "part_request_status", null, lDs.getString( "part_request_status" ) );
      Assert.assertEquals( "part_request_eta", null, lDs.getString( "part_request_eta" ) );
      Assert.assertEquals( "part_request_key", ":", lDs.getString( "part_request_key" ) );
      Assert.assertEquals( "warning_bool", null, lDs.getString( "warning_bool" ) );
      Assert.assertEquals( "material_avail_sort_date", null,
            lDs.getString( "material_avail_sort_date" ) );

   }


   /**
    *
    * Test to review open faults against an aircraft.
    *
    */
   @Test
   public void getFaultByAircraft() {

      // Given I have an Aircraft and the Aircraft has Open Faults.
      // (refer to the data setup in OpenFaultsByInventoryForInvTest.sql section: "Test data for
      // fault against aircraft getFaultByAircraft()")

      // When I review the list of Open Faults.

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aInvNoDbId", "20" );
      lArgs.add( "aInvNoId", "123456" );
      lArgs.add( "aShowAssignedFaults", false );
      lArgs.add( "aHideNonExecutableFaults", false );

      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Then I can see the Open Faults
      Assert.assertEquals( "RowCount", 3, lDs.getRowCount() );
      Assert.assertEquals( "ColumnCount", COLUMN_COUNT, lDs.getColumnCount() );

      // Fault 1
      lDs.next();
      Assert.assertEquals( "EVENT_KEY", "0:0", lDs.getString( "EVENT_KEY" ) );
      Assert.assertEquals( "EVENT_SDESC", "NA", lDs.getString( "EVENT_SDESC" ) );
      Assert.assertEquals( "BARCODE_SDESC", "A", lDs.getString( "BARCODE_SDESC" ) );
      Assert.assertEquals( "ETOPS_BOOL", "1", lDs.getString( "ETOPS_BOOL" ) );
      Assert.assertEquals( "USER_STATUS_CD", "OPEN", lDs.getString( "USER_STATUS_CD" ) );
      Assert.assertEquals( "FAIL_SEV_CD", "MINOR", lDs.getString( "FAIL_SEV_CD" ) );
      Assert.assertEquals( "INVENTORY_KEY", "20:123456", lDs.getString( "INVENTORY_KEY" ) );
      Assert.assertEquals( "PLAN_BY_DATE", "05-Sep-2018 00:00:00",
            lDs.getString( "PLAN_BY_DATE" ) );

      // Fault 2
      lDs.next();
      Assert.assertEquals( "event_key", "0:2", lDs.getString( "event_key" ) );
      Assert.assertEquals( "EVENT_SDESC", "NA", lDs.getString( "EVENT_SDESC" ) );
      Assert.assertEquals( "BARCODE_SDESC", "A", lDs.getString( "BARCODE_SDESC" ) );
      Assert.assertEquals( "ETOPS_BOOL", "1", lDs.getString( "ETOPS_BOOL" ) );
      Assert.assertEquals( "USER_STATUS_CD", "OPEN", lDs.getString( "USER_STATUS_CD" ) );
      Assert.assertEquals( "FAIL_SEV_CD", "AOG", lDs.getString( "FAIL_SEV_CD" ) );
      Assert.assertEquals( "INVENTORY_KEY", "20:123456", lDs.getString( "INVENTORY_KEY" ) );
      Assert.assertEquals( "PLAN_BY_DATE", "25-Aug-2018 00:00:00",
            lDs.getString( "PLAN_BY_DATE" ) );

      // Fault 3
      lDs.next();
      Assert.assertEquals( "event_key", "0:1", lDs.getString( "event_key" ) );
      Assert.assertEquals( "EVENT_SDESC", "NA", lDs.getString( "EVENT_SDESC" ) );
      Assert.assertEquals( "BARCODE_SDESC", "A", lDs.getString( "BARCODE_SDESC" ) );
      Assert.assertEquals( "ETOPS_BOOL", "1", lDs.getString( "ETOPS_BOOL" ) );
      Assert.assertEquals( "USER_STATUS_CD", "OPEN", lDs.getString( "USER_STATUS_CD" ) );
      Assert.assertEquals( "FAIL_SEV_CD", "BLKOUT", lDs.getString( "FAIL_SEV_CD" ) );
      Assert.assertEquals( "INVENTORY_KEY", "20:123456", lDs.getString( "INVENTORY_KEY" ) );
      Assert.assertEquals( "PLAN_BY_DATE", "06-Sep-2018 00:00:00",
            lDs.getString( "PLAN_BY_DATE" ) );
   }

}
