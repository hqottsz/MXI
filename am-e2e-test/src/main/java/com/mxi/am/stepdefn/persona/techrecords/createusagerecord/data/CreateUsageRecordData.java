package com.mxi.am.stepdefn.persona.techrecords.createusagerecord.data;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public final class CreateUsageRecordData {

   public static final String TECHNICAL_RECORDS = "Technical Records";
   public static final String INVENTORY_SEARCH = "Inventory Search";
   public static final String PAGE_TITLE = "Inventory Details";
   public static final String APU_PART_NUMBER = "APU_ASSY_PN1";
   public static final String APU_INVENTORY_SERIAL_NO = "CUR-001";
   public static final String USAGE_RECORD_NAME = "UR-PARAMETER-WITH-UNDERSCORE";
   public static final String USAGE_TIME_STRING = "01:00";
   public static final String USAGE_PARAMETER = "APUH_AT_READING";
   public static final BigDecimal EXPECTED_USAGE_TSN_VALUE = new BigDecimal( 1100 );
   public static final BigDecimal EXPECTED_USAGE_DELTA_VALUE = new BigDecimal( 100 );
   public static final BigDecimal INITIAL_USAGE_TSN_VALUE = new BigDecimal( 1000 );
   public static final String COLUMN_TSN = "TSN";
   public static final String COLUMN_DELTA = "Delta";

   public static final DateFormat DATE_FORMAT = new SimpleDateFormat( "dd-MMM-yyyy" );

   public static final String UPDATE_INV_CURR_USAGE = "UPDATE inv_curr_usage SET tsn_qt = "
         + INITIAL_USAGE_TSN_VALUE.doubleValue()
         + "WHERE (inv_no_db_id, inv_no_id) = (SELECT inv_no_db_id, inv_no_id FROM inv_inv WHERE serial_no_oem = ?) and (data_type_db_id, data_type_id) = (SELECT data_type_db_id, data_type_id FROM mim_data_type WHERE data_type_cd = ?)";
   public static final String DELETE_USAG_USAGE_DATA =
         "DELETE FROM usg_usage_data WHERE usage_record_id = (SELECT usage_record_id FROM usg_usage_record WHERE usage_name = ?)";
   public static final String DELETE_USAG_USAGE_RECORD =
         "DELETE FROM usg_usage_record WHERE usage_name = ?";


   private CreateUsageRecordData() {
   }

}
