--liquibase formatted sql


--changeSet opr_rbl_comp_rmvl_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY opr_rbl_comp_rmvl_pkg IS
/******************************************************************************
*
*  Function:     opr_rbl_comp_rmvl_xtract
*  Arguments:    aidt_start_date - start date
*                aidt_end_date   - end date
*  Return:
*  Description:  Function that will extract the component removal.
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  05-Mar-2014
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
FUNCTION opr_rbl_comp_rmvl_xtract (
   aidt_start_date    IN   DATE,
   aidt_end_date      IN   DATE
) RETURN NUMBER
AS

BEGIN

   DELETE
   FROM
      opr_rbl_comp_rmvl_raw
   WHERE
      -- year and month input param
      TRUNC(removal_date) BETWEEN aidt_start_date AND aidt_end_date;

   INSERT /*+ APPEND */
   INTO
      opr_rbl_comp_rmvl_raw
      (
         aircraft_id,
         inventory_id,
         config_slot_id,
         sched_id,
         sched_part_id,
         sched_rmvd_part_id,
         fleet_type,
         registration_code,
         acft_serial_number,
         bom_class_cd,
         config_slot,
         manufacturer_code,
         part_number,
         part_name,
         removed_serial_number,
         inventory_class,
         removal_reason,
         removal_date,
         year_month,
         removal_type,
         justified_failure,
         quantity_per_acft
      )
   SELECT
      aircraft_id,
      inventory_id,
      config_slot_id,
      sched_id,
      sched_part_id,
      sched_rmvd_part_id,
      fleet_type,
      registration_code,
      acft_serial_number,
      bom_class_cd,
      config_slot,
      manufacturer_code,
      part_number,
      part_name,
      removed_serial_number,
      inventory_class,
      removal_reason,
      removal_date,
      year_month,
      removal_type,
      justified_failure,
      quantity_per_acft
   FROM
      acor_rbl_comp_removal_v1
   WHERE
      -- year and month input param
      TRUNC(removal_date) BETWEEN aidt_start_date AND aidt_end_date;

    -- number of records extracted
    RETURN SQL%ROWCOUNT;

END opr_rbl_comp_rmvl_xtract;

/******************************************************************************
*
*  Function:    opr_rbl_comp_rmvl_xform
*  Arguments:    aidt_start_date - start date
*                aidt_end_date   - end date
*  Return:
  Description:  Function that will transform the component removal
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  05-Mar-2014
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
FUNCTION opr_rbl_comp_rmvl_xform (
   aidt_start_date    IN   DATE,
   aidt_end_date      IN   DATE
) RETURN NUMBER
AS

   ln_row_processed   NUMBER  := 0;
   ln_return          NUMBER;

BEGIN

   -- # monthy removal
   --
   DELETE
   FROM
      opr_rbl_comp_rmvl_fleet_mon
   WHERE
      -- year and month input param
      TO_DATE(year_month,'YYYYMM') BETWEEN aidt_start_date AND aidt_end_date;

   -- monthly removal insert
   --
   INSERT /*+ APPEND */
   INTO
   opr_rbl_comp_rmvl_fleet_mon
   (
      operator_id,
      operator_code,
      fleet_type,
      part_number,
      part_name,
      inventory_class,
      config_slot,
      year_month,
      cycles,
      flight_hours,
      quantity_per_acft,
      number_of_acft,
      removal_count,
      sched_removal,
      unsched_removal,
      justified_failure
   )
   WITH
   rvw_fleet_usage
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
      ),
   rvw_fleet_acft_count
   AS
      (
         SELECT
            assmbly_code AS fleet_type,
            COUNT(1) number_of_acft
         FROM
            acor_inv_aircraft_v1
         GROUP BY
            assmbly_code
      ),
   rvw_summ
   AS
      (
         SELECT
            opr_fleet_movement.operator_id,
            opr_fleet_movement.operator_code,
            opr_rbl_comp_rmvl_raw.fleet_type,
            opr_rbl_comp_rmvl_raw.part_number,
            opr_rbl_comp_rmvl_raw.inventory_class,
            SUBSTR(opr_rbl_comp_rmvl_raw.config_slot,1,2) config_slot,
            opr_rbl_comp_rmvl_raw.year_month,
            NVL(opr_rbl_comp_rmvl_raw.quantity_per_acft,0) quantity_per_acft,
            COUNT(1) removal_count,
            SUM(DECODE(removal_type,'S',1,0)) sched_removal,
            SUM(DECODE(removal_type,'U',1,0)) unsched_removal,
            SUM(justified_failure) justified_failure
         FROM
            opr_rbl_comp_rmvl_raw
            INNER JOIN opr_fleet_movement ON
               opr_rbl_comp_rmvl_raw.aircraft_id = opr_fleet_movement.aircraft_id AND
               TRUNC(opr_rbl_comp_rmvl_raw.removal_date) BETWEEN opr_fleet_movement.phase_in_date AND NVL(opr_fleet_movement.phase_out_date,opr_rbl_comp_rmvl_raw.removal_date)
         WHERE
            -- year and month input param
            TO_DATE(opr_rbl_comp_rmvl_raw.year_month,'YYYYMM') BETWEEN aidt_start_date AND aidt_end_date
         GROUP BY
            opr_fleet_movement.operator_id,
            opr_fleet_movement.operator_code,
            opr_rbl_comp_rmvl_raw.fleet_type,
            opr_rbl_comp_rmvl_raw.part_number,
            opr_rbl_comp_rmvl_raw.inventory_class,
            SUBSTR(opr_rbl_comp_rmvl_raw.config_slot,1,2),
            opr_rbl_comp_rmvl_raw.year_month,
            NVL(opr_rbl_comp_rmvl_raw.quantity_per_acft,0)
      )
   SELECT
      rvw_summ.operator_id,
      rvw_summ.operator_code,
      rvw_summ.fleet_type,
      part_number,
      (SELECT
          part_number_name
       FROM
          acor_part_number_v1 inner_part
       WHERE
          inner_part.part_number = rvw_summ.part_number
          AND
          ROWNUM = 1
      ) AS part_name,
      inventory_class,
      config_slot,
      rvw_summ.year_month,
      cycles,
      flight_hours,
      quantity_per_acft,
      number_of_acft,
      removal_count,
      sched_removal,
      unsched_removal,
      justified_failure
   FROM
     rvw_summ
     INNER JOIN rvw_fleet_usage ON
        rvw_summ.operator_id = rvw_fleet_usage.operator_id AND
        rvw_summ.fleet_type  = rvw_fleet_usage.fleet_type AND
        rvw_summ.year_month  = rvw_fleet_usage.year_month
     INNER JOIN rvw_fleet_acft_count ON
        rvw_summ.fleet_type = rvw_fleet_acft_count.fleet_type;

      -- number of row processed
      ln_row_processed := SQL%ROWCOUNT;


    -- refresh the materialized view
    ln_return := opr_rbl_utility_pkg.opr_refresh_mview( aiv_category_or_frequency => 'COMPONENT',
                                                        aiv_refresh_type_code     => 'CATEGORY'
                                                      );

   --
   RETURN ln_row_processed;

END opr_rbl_comp_rmvl_xform;

/******************************************************************************
*  Function:    opr_rbl_hist_comp_rmvl_xform
*
*  Return:       ln_row_processed - number of processed rows
*
*  Description:  Function that will transform the legacy data component removal
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  18-Mar-2014
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
FUNCTION opr_rbl_hist_comp_rmvl_xform
RETURN NUMBER
AS

   ln_row_processed   NUMBER  := 0;
   ln_return          NUMBER;

BEGIN

   -- # monthy removal
   --
   DELETE
   FROM
      opr_rbl_hist_comp_rmvl_flt_mon;

   --
   -- insert data
   INSERT /*+ APPEND */
   INTO
      opr_rbl_hist_comp_rmvl_flt_mon
      (
         operator_id,
         operator_code,
         fleet_type,
         part_number,
         part_name,
         inventory_class,
         config_slot,
         year_month,
         cycles,
         flight_hours,
         quantity_per_acft,
         number_of_acft,
         removal_count,
         sched_removal,
         unsched_removal,
         justified_failure
      )
   WITH
   rvw_acft_part_qty
   AS
      (
         SELECT
            assmbl_cd,
            manufact_cd,
            part_no_oem,
            SUM(acft_part_qty) acft_part_qty
         FROM
            acor_rbl_acft_part_qty_v1
         GROUP BY
            assmbl_cd,
            manufact_cd,
            part_no_oem
      )
   SELECT
      acor_operator_v1.operator_id,
      acor_operator_v1.operator_code,
      lgcy_comp_removal.fleet,
      lgcy_comp_removal.pn,
      acor_part_number_v1.part_number_name,
      eqp_part_no.inv_class_cd,
      lgcy_comp_removal.ata_chapter,
      TO_NUMBER(TO_CHAR(lgcy_comp_removal.rmvl_date,'YYYYMM')) year_month,
      NVL(monthly_usage.cycles,0) cycles,
      NVL(monthly_usage.flight_hours,0) flight_hours,
      NVL(rvw_acft_part_qty.acft_part_qty,0) acft_part_qty,
      NVL(fleet_acft_count.number_of_acft,0) number_of_acft,
      COUNT(1) removal_count,
      SUM(DECODE(lgcy_comp_removal.scheduled,'Y',1,0)) sched_removal,
      SUM(DECODE(lgcy_comp_removal.scheduled,'N',1,0)) unsched_removal,
      -- only consider justified failure when it is unscheduled
      --SUM(DECODE(justified_failure,'Y',1,0)) justified_failure
      sum (case when lgcy_comp_removal.scheduled = 'Y' and justified_failure = 'Y'  then 1 else 0 end) as justified_failure
   FROM
      opr_rbl_hist_comp_removal lgcy_comp_removal
      INNER JOIN eqp_part_no ON
         lgcy_comp_removal.vendor_code = eqp_part_no.manufact_cd AND
         lgcy_comp_removal.pn          = eqp_part_no.part_no_oem
      -- operator
      INNER JOIN acor_operator_v1 ON
         lgcy_comp_removal.operator = acor_operator_v1.operator_code
      -- part description
      INNER JOIN acor_part_number_v1 ON
         lgcy_comp_removal.vendor_code = acor_part_number_v1.manufact_cd AND
         lgcy_comp_removal.pn          = acor_part_number_v1.part_number
      -- quantity of parts in an aircraft
      LEFT JOIN rvw_acft_part_qty ON
         rvw_acft_part_qty.assmbl_cd   = lgcy_comp_removal.fleet AND
         rvw_acft_part_qty.manufact_cd = lgcy_comp_removal.vendor_code AND
         rvw_acft_part_qty.part_no_oem = lgcy_comp_removal.pn
      -- monthly aircraft reliability
      LEFT JOIN (
                  SELECT
                     operator_code,
                     fleet_type,
                     year_code || month_code year_month,
                     SUM(flight_hours) flight_hours,
                     SUM(cycles) cycles
                  FROM
                     aopr_monthly_operator_usage_v1
                  GROUP BY
                      operator_code,
                      fleet_type,
                     year_code || month_code
                )  monthly_usage ON
         lgcy_comp_removal.operator                    = monthly_usage.operator_code AND
         lgcy_comp_removal.fleet                       = monthly_usage.fleet_type AND
         TO_CHAR(lgcy_comp_removal.rmvl_date,'YYYYMM') = monthly_usage.year_month
      -- number of aircraft
      INNER JOIN (
                   SELECT
                      assmbly_code AS fleet_type,
                      COUNT(1) number_of_acft
                   FROM
                      acor_inv_aircraft_v1
                   GROUP BY
                      assmbly_code
                 ) fleet_acft_count ON
         lgcy_comp_removal.fleet = fleet_acft_count.fleet_type
   GROUP BY
      acor_operator_v1.operator_id,
      acor_operator_v1.operator_code,
      lgcy_comp_removal.fleet,
      lgcy_comp_removal.pn,
      acor_part_number_v1.part_number_name,
      eqp_part_no.inv_class_cd,
      lgcy_comp_removal.ata_chapter,
      TO_NUMBER(TO_CHAR(lgcy_comp_removal.rmvl_date,'YYYYMM')),
      NVL(monthly_usage.cycles,0),
      NVL(monthly_usage.flight_hours,0),
      NVL(rvw_acft_part_qty.acft_part_qty,0),
      NVL(fleet_acft_count.number_of_acft,0);

    -- number of row processed
    ln_row_processed := SQL%ROWCOUNT;

    -- refresh the materialized view
    ln_return := opr_rbl_utility_pkg.opr_refresh_mview( aiv_category_or_frequency => 'COMPONENT',
                                                        aiv_refresh_type_code     => 'CATEGORY'
                                                      );

    RETURN ln_row_processed;

END opr_rbl_hist_comp_rmvl_xform;

-- package end
END opr_rbl_comp_rmvl_pkg;
/