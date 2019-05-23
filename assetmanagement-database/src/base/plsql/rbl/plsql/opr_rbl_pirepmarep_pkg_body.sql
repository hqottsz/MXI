--liquibase formatted sql


--changeSet opr_rbl_pirepmarep_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY opr_rbl_pirepmarep_pkg IS
/******************************************************************************
*  Procedure:    opr_rbl_fault_raw_xtract
*
*  Arguments:    aidt_start_date - start date
*                aidt_end_date   - end date
*
*  Return:       ln_row_processed - number of rows processed
*
*  Description:  Function that will extract fault
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  28-Nov-2013
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
FUNCTION opr_rbl_pirepmarep_xtract (
   aidt_start_date    IN   DATE,
   aidt_end_date      IN   DATE
) RETURN NUMBER
AS

BEGIN

   -- # faults raw data
   --
   DELETE
   FROM
      opr_rbl_fault_raw
   WHERE -- year and month input param
      TRUNC(found_on_date) BETWEEN aidt_start_date AND aidt_end_date;

   --
   INSERT /* + APPEND */
   INTO
      opr_rbl_fault_raw
      (
         aircraft_id,
         inventory_id,
         config_slot_id,
         fault_id,
         leg_id,
         fleet_type,
         config_slot_code,
         fault_source_code,
         fault_severity_code,
         found_on_date,
         year_month
      )
   SELECT
      aircraft_id,
      inventory_id,
      config_slot_id,
      fault_id,
      leg_id,
      assmbl_cd,
      config_slot_code,
      fault_source_cd,
      fail_sev_cd,
      actual_start_dt,
      year_month
   FROM
     acor_rbl_pirepmarep_v1
   WHERE -- year and month input param
      TRUNC(actual_start_dt) BETWEEN aidt_start_date AND aidt_end_date;

    -- number of records extracted
      RETURN SQL%ROWCOUNT;

END opr_rbl_pirepmarep_xtract;


/******************************************************************************
*  Procedure:    opr_rbl_fault_raw_xform
*
*  Arguments:    aidt_start_date - start date
*                aidt_end_date   - end date
*
*  Return:       ln_row_processed - number of rows processed
*
*  Description:  Function that will summarize monthly fault per aircraft and fleet
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  28-Nov-2013
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
FUNCTION opr_rbl_pirepmarep_xform (
   aidt_start_date    IN   DATE,
   aidt_end_date      IN   DATE
) RETURN NUMBER
AS

   ln_row_processed   NUMBER  := 0;
   ln_return          NUMBER;

BEGIN

   -- processing monthly fault count
   --
   -- delete the existing record with the given range
   --
   DELETE
   FROM
      opr_rbl_fault_monthly
   WHERE
      -- year and month input param
      TO_DATE(year_month,'YYYYMM') BETWEEN aidt_start_date AND aidt_end_date;

   -- do the insert
   --
   INSERT /* + APPEND */
   INTO
      opr_rbl_fault_monthly
         (
            operator_id,
            aircraft_id,
            operator_code,
            fleet_type,
            tail_number,
            config_slot_code,
            fault_source_code,
            year_month,
            flight_hours,
            cycles,
            fault_cnt
          )
   SELECT
      opr_fleet_movement.operator_id,
      opr_fleet_movement.aircraft_id,
      opr_fleet_movement.operator_code,
      opr_fleet_movement.fleet_type,
      opr_fleet_movement.operator_registration_code,
      opr_rbl_fault_raw.config_slot_code,
      opr_rbl_fault_raw.fault_source_code,
      opr_rbl_fault_raw.year_month,
      NVL(monthly_usage.flight_hours,0) flight_hours,
      NVL(monthly_usage.cycles,0) cycles,
      COUNT(1) fault_cnt
   FROM
      opr_rbl_fault_raw
      INNER JOIN opr_fleet_movement ON
         opr_rbl_fault_raw.aircraft_id = opr_fleet_movement.aircraft_id AND
         TRUNC(opr_rbl_fault_raw.found_on_date) BETWEEN opr_fleet_movement.phase_in_date AND NVL(opr_fleet_movement.phase_out_date,opr_rbl_fault_raw.found_on_date)
      INNER JOIN aopr_monthly_operator_usage_v1  monthly_usage ON
         monthly_usage.operator_id = opr_fleet_movement.operator_id
         AND
         monthly_usage.aircraft_id = opr_fleet_movement.aircraft_id
         AND
         TO_NUMBER(monthly_usage.year_code || monthly_usage.month_code) = opr_rbl_fault_raw.year_month
   WHERE
      TRUNC(opr_rbl_fault_raw.found_on_date) BETWEEN aidt_start_date AND aidt_end_date
   GROUP BY
      opr_fleet_movement.operator_id,
      opr_fleet_movement.aircraft_id,
      opr_fleet_movement.operator_code,
      opr_fleet_movement.fleet_type,
      opr_fleet_movement.operator_registration_code,
      opr_rbl_fault_raw.config_slot_code,
      opr_rbl_fault_raw.fault_source_code,
      opr_rbl_fault_raw.year_month,
      NVL(monthly_usage.flight_hours,0),
      NVL(monthly_usage.cycles,0);


   -- number of row processed
   ln_row_processed := SQL%ROWCOUNT;


   DELETE
   FROM
      opr_rbl_fault_fleet_monthly
   WHERE
      -- year and month input param
      TO_DATE(year_month,'YYYYMM') BETWEEN aidt_start_date AND aidt_end_date;

   -- do the insert
   --
   INSERT /* + APPEND */
   INTO
      opr_rbl_fault_fleet_monthly
         (
           operator_id,
           operator_code,
           fleet_type,
           config_slot_code,
           fault_source_code,
           year_month,
           flight_hours,
           cycles,
           fault_cnt
         )
   WITH
   rvw_monthly_usage
   AS
     (
         SELECT
            operator_id,
            operator_code,
            fleet_type,
            TO_NUMBER(year_code || month_code) year_month,
            SUM(flight_hours) flight_hours,
            SUM(cycles) cycles
         FROM
            aopr_monthly_operator_usage_v1
         GROUP BY
            operator_id,
            operator_code,
            fleet_type,
            year_code || month_code
      )
   SELECT
      opr_rbl_fault_monthly.operator_id,
      opr_rbl_fault_monthly.operator_code,
      opr_rbl_fault_monthly.fleet_type,
      config_slot_code,
      fault_source_code,
      opr_rbl_fault_monthly.year_month,
      NVL(rvw_monthly_usage.flight_hours,0) flight_hours,
      NVL(rvw_monthly_usage.cycles,0) cycles,
      SUM(fault_cnt) fault_cnt
   FROM
      opr_rbl_fault_monthly
      INNER JOIN rvw_monthly_usage ON
         opr_rbl_fault_monthly.operator_id = rvw_monthly_usage.operator_id AND
         opr_rbl_fault_monthly.fleet_type  = rvw_monthly_usage.fleet_type
         AND
         opr_rbl_fault_monthly.year_month  = rvw_monthly_usage.year_month
     WHERE
        TO_DATE(opr_rbl_fault_monthly.year_month,'YYYYMM') BETWEEN aidt_start_date AND aidt_end_date
   GROUP BY
      opr_rbl_fault_monthly.operator_id,
      opr_rbl_fault_monthly.operator_code,
      opr_rbl_fault_monthly.fleet_type,
      config_slot_code,
      fault_source_code,
      opr_rbl_fault_monthly.year_month,
      NVL(rvw_monthly_usage.flight_hours,0),
      NVL(rvw_monthly_usage.cycles,0);


   -- refresh relevant materialized view
   ln_return := opr_rbl_utility_pkg.opr_refresh_mview( aiv_category_or_frequency => 'PIREPMAREP',
                                                       aiv_refresh_type_code     => 'CATEGORY'
                                                     );

   --
   RETURN ln_row_processed;

END opr_rbl_pirepmarep_xform;

/******************************************************************************
*
*  Procedure:    opr_rbl_hist_fault_xform
*
*  Arguments:    No arguments. All the data from the staging table will be processed
*
*  Return:
*  Description:  Function that will summarize legacy historical fault
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  17-Max-2014
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
FUNCTION opr_rbl_hist_pirepmarep_xform
RETURN NUMBER
AS

  ln_row_processed     NUMBER := 0;
  ln_return          NUMBER;

BEGIN

   --
   -- aircraft  montly fault
   --
   DELETE
   FROM
      opr_rbl_hist_fault_monthly;

   -- do the insert
   --
   INSERT /* + APPEND */
   INTO
      opr_rbl_hist_fault_monthly
         (
           operator_id,
           operator_code,
           fleet_type,
           acft_serial_number,
           tail_number,
           config_slot_code,
           fault_source_code,
           year_month,
           flight_hours,
           cycles,
           fault_cnt
         )
   SELECT
      operator.operator_id,
      monthly_usage.operator_code,
      lgcy_acft_fault.fleet  fleet_type,
      lgcy_acft_fault.ac_sn  acft_serial_number,
      lgcy_acft_fault.ac_reg_code tail_number,
      lgcy_acft_fault.ata_chapter config_slot_code,
      lgcy_acft_fault.fault_source fault_source_code,
      TO_NUMBER(TO_CHAR(lgcy_acft_fault.found_on_date,'YYYYMM')) year_month,
      monthly_usage.flight_hours,
      monthly_usage.cycles,
      COUNT(1) fault_cnt
   FROM
      opr_rbl_hist_acft_fault lgcy_acft_fault
      INNER JOIN acor_operator_v1 operator ON
         lgcy_acft_fault.OPERATOR = operator.operator_code
      -- fleet statistic per month
      INNER JOIN aopr_monthly_operator_usage_v1 monthly_usage ON
         lgcy_acft_fault.OPERATOR = monthly_usage.operator_code AND
         lgcy_acft_fault.ac_sn    = monthly_usage.serial_number
         AND
         TO_NUMBER(TO_CHAR(lgcy_acft_fault.found_on_date,'YYYYMM')) = TO_NUMBER(monthly_usage.year_code|| monthly_usage.month_code)
   GROUP BY
      operator.operator_id,
      monthly_usage.operator_code,
      lgcy_acft_fault.fleet,
      lgcy_acft_fault.ac_sn,
      lgcy_acft_fault.ac_reg_code,
      lgcy_acft_fault.ata_chapter,
      lgcy_acft_fault.fault_source,
      TO_NUMBER(TO_CHAR(lgcy_acft_fault.found_on_date,'YYYYMM')),
      monthly_usage.flight_hours,
      monthly_usage.cycles;

   -- number of row processed
   ln_row_processed := SQL%ROWCOUNT;

   --
   -- fleet monthly fault
   --
   DELETE
   FROM
      opr_rbl_hist_fault_flt_monthly;

   -- do the insert
   --
   INSERT /* + APPEND */
   INTO
      opr_rbl_hist_fault_flt_monthly
         (
           operator_id,
           operator_code,
           fleet_type,
           config_slot_code,
           fault_source_code,
           year_month,
           flight_hours,
           cycles,
           fault_cnt
         )
   SELECT
      lgcy_acft_fault_monthly.operator_id,
      lgcy_acft_fault_monthly.operator_code,
      lgcy_acft_fault_monthly.fleet_type,
      lgcy_acft_fault_monthly.config_slot_code,
      lgcy_acft_fault_monthly.fault_source_code,
      lgcy_acft_fault_monthly.year_month,
      monthly_usage.flight_hours,
      monthly_usage.cycles,
      SUM(lgcy_acft_fault_monthly.fault_cnt) fault_cnt
   FROM
      opr_rbl_hist_fault_monthly lgcy_acft_fault_monthly
      -- fleet statistic per month
      INNER JOIN ( SELECT
                      operator_code,
                      fleet_type,
                      year_code,
                      month_code,
                      SUM(flight_hours) flight_hours,
                      SUM(cycles) cycles
                   FROM
                      aopr_monthly_operator_usage_v1
                   GROUP BY
                      operator_code,
                      fleet_type,
                      year_code,
                      month_code
                 ) monthly_usage ON
         lgcy_acft_fault_monthly.operator_code = monthly_usage.operator_code AND
         lgcy_acft_fault_monthly.fleet_type    = monthly_usage.fleet_type
         AND
         lgcy_acft_fault_monthly.year_month = TO_NUMBER(monthly_usage.year_code|| monthly_usage.month_code)
   GROUP BY
      lgcy_acft_fault_monthly.operator_id,
      lgcy_acft_fault_monthly.operator_code,
      lgcy_acft_fault_monthly.fleet_type,
      lgcy_acft_fault_monthly.config_slot_code,
      lgcy_acft_fault_monthly.fault_source_code,
      lgcy_acft_fault_monthly.year_month,
      monthly_usage.flight_hours,
      monthly_usage.cycles;


    -- refresh relevant materialized view
    ln_return := opr_rbl_utility_pkg.opr_refresh_mview( aiv_category_or_frequency => 'PIREPMAREP',
                                                        aiv_refresh_type_code     => 'CATEGORY'
                                                      );

    --
    RETURN  ln_row_processed;

END opr_rbl_hist_pirepmarep_xform;

-- package end
END opr_rbl_pirepmarep_pkg;
/