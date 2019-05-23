--liquibase formatted sql


--changeSet oper-236-004:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY opr_rbl_fault_pkg IS
/******************************************************************************
*
*  Procedure:    opr_rbl_fault_raw_xtract_proc
*  Arguments:    aidt_start_date - start date
*                aidt_end_date   - end date
*  Return:
*  Description:  This is the function that will extract faults.
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
FUNCTION opr_rbl_fault_xtract_proc (
   aidt_start_date    IN   DATE,
   aidt_end_date      IN   DATE
) RETURN NUMBER
AS

BEGIN

   -- merge to the staging table
   MERGE INTO opr_rbl_fault_raw
   USING (
            SELECT
               inv_acft.alt_id            AS aircraft_id,
               inv_inv.alt_id             AS inventory_id,
               eqp_assmbl_bom.alt_id      AS config_slot_id,
               sd_fault.alt_id            AS fault_id,
               sd_fault.leg_id,
               inv_acft.assmbl_cd,
               SUBSTR(eqp_assmbl_bom.assmbl_bom_cd,1,2) config_slot_code,
               NVL(sd_fault.fault_source_cd,'UNKNOWN') fault_source_cd,
               sd_fault.fail_sev_cd,
               fault_event.actual_start_dt,
               TO_NUMBER(TO_CHAR(fault_event.actual_start_dt,'YYYYMM')) AS year_month
            FROM
               sd_fault
               -- found on date
               INNER JOIN evt_event fault_event on
                  sd_fault.fault_db_id = fault_event.event_db_id AND
                  sd_fault.fault_id    = fault_event.event_id
               -- inventory
               INNER JOIN evt_inv ON
                  fault_event.event_db_id = evt_inv.event_db_id AND
                  fault_event.event_id    = evt_inv.event_id
                  AND
                  evt_inv.main_inv_bool = 1
               -- aircraft
               INNER JOIN inv_inv ON
                  evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
                  evt_inv.inv_no_id    = inv_inv.inv_no_id
               INNER JOIN inv_ac_reg ON
                  inv_inv.h_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
                  inv_inv.h_inv_no_id    = inv_ac_reg.inv_no_id
               INNER JOIN inv_inv inv_acft ON
                  inv_ac_reg.inv_no_db_id = inv_acft.inv_no_db_id AND
                  inv_ac_reg.inv_no_id    = inv_acft.inv_no_id
               -- config slot
               INNER JOIN eqp_assmbl_bom ON
                  inv_inv.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id AND
                  inv_inv.assmbl_cd     = eqp_assmbl_bom.assmbl_cd    AND
                  inv_inv.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id
            WHERE
               NOT EXISTS (
                            SELECT
                               1
                            FROM 
                               sched_stask
                               INNER JOIN evt_event ON 
                                  sched_stask.sched_db_id = evt_event.event_db_id AND
                                  sched_stask.sched_id    = evt_event.event_id
                            WHERE
                               sched_stask.fault_db_id = sd_fault.fault_db_id AND
                               sched_stask.fault_id    = sd_fault.fault_id
                               AND
                               evt_event.event_type_db_id = 0 AND
                               evt_event.event_type_cd    = 'TS'
                               AND
                               evt_event.event_status_db_id = 0 AND
                               evt_event.event_status_cd    = 'ERROR'
                               
                           )
               AND -- year and month input param
               TRUNC(fault_event.actual_start_dt) BETWEEN aidt_start_date AND aidt_end_date
         ) src_data
   ON (
         opr_rbl_fault_raw.aircraft_id       = src_data.aircraft_id    AND
         opr_rbl_fault_raw.inventory_id      = src_data.inventory_id   AND
         opr_rbl_fault_raw.config_slot_id    = src_data.config_slot_id AND
         opr_rbl_fault_raw.fault_id          = src_data.fault_id
      )
   WHEN MATCHED
   THEN
      UPDATE
      SET
         opr_rbl_fault_raw.fault_source_code   = src_data.fault_source_cd,
         opr_rbl_fault_raw.fault_severity_code = src_data.fail_sev_cd,
         opr_rbl_fault_raw.found_on_date       = src_data.actual_start_dt
   WHEN NOT MATCHED
   THEN
      INSERT (
               opr_rbl_fault_raw.aircraft_id,
               opr_rbl_fault_raw.inventory_id,
               opr_rbl_fault_raw.config_slot_id,
               opr_rbl_fault_raw.fault_id,
               opr_rbl_fault_raw.leg_id,
               opr_rbl_fault_raw.fleet_type,
               opr_rbl_fault_raw.config_slot_code,
               opr_rbl_fault_raw.fault_source_code,
               opr_rbl_fault_raw.fault_severity_code,
               opr_rbl_fault_raw.found_on_date,
               opr_rbl_fault_raw.year_month
             )
      VALUES (
               src_data.aircraft_id,
               src_data.inventory_id,
               src_data.config_slot_id,
               src_data.fault_id,
               src_data.leg_id,
               src_data.assmbl_cd,
               src_data.config_slot_code,
               src_data.fault_source_cd,
               src_data.fail_sev_cd,
               src_data.actual_start_dt,
               src_data.year_month
             );

    -- number of records extracted
      RETURN SQL%ROWCOUNT;

END opr_rbl_fault_xtract_proc;


/******************************************************************************
*
*  Procedure:    opr_rbl_fault_raw_xform_proc
*
*  Arguments:    ain_year_month    - year and month (ie. 201311)
*
*  Return:
*  Description:  This is the procedure to summarized the fault
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
FUNCTION opr_rbl_fault_xform_proc (
   aidt_start_date    IN   DATE,
   aidt_end_date      IN   DATE
) RETURN NUMBER
AS

   ln_mon_between     NUMBER;
   ln_year_month      NUMBER;
   ln_prev_12mon      NUMBER;
   ln_row_processed   NUMBER  := 0;
   ln_next_date       DATE;

BEGIN

   -- processing monthly fault count
   MERGE INTO opr_rbl_fault_monthly
   USING (
            SELECT
               opr_fleet_movement.operator_id,
               opr_fleet_movement.aircraft_id,
               opr_fleet_movement.operator_code,
               opr_fleet_movement.fleet_type,
               opr_fleet_movement.operator_registration_code,
               opr_rbl_fault_raw.config_slot_code,
               opr_rbl_fault_raw.fault_source_code,
               opr_rbl_fault_raw.year_month,
               NVL(opr_monthly_reliability.flight_hours,0) flight_hours,
               NVL(opr_monthly_reliability.cycles,0) flight_cycles,
               COUNT(1) fault_cnt
            FROM
               opr_rbl_fault_raw
               -- fleet statistic per month
               INNER JOIN opr_fleet_movement ON
                  opr_rbl_fault_raw.aircraft_id = opr_fleet_movement.aircraft_id
                  AND
                  opr_rbl_fault_raw.year_month BETWEEN TO_NUMBER(TO_CHAR(phase_in_date,'YYYYMM')) AND TO_NUMBER(TO_CHAR(NVL(phase_out_date, SYSDATE),'YYYYMM'))
               LEFT JOIN opr_monthly_reliability ON
                  opr_rbl_fault_raw.aircraft_id = opr_monthly_reliability.aircraft_id AND
                  opr_rbl_fault_raw.year_month  = TO_NUMBER(opr_monthly_reliability.year_code || opr_monthly_reliability.month_code)
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
               NVL(opr_monthly_reliability.flight_hours,0),
               NVL(opr_monthly_reliability.cycles,0)
        ) src_data
   ON (
        opr_rbl_fault_monthly.operator_id       = src_data.operator_id AND
        opr_rbl_fault_monthly.aircraft_id       = src_data.aircraft_id AND
        opr_rbl_fault_monthly.config_slot_code  = src_data.config_slot_code AND
        opr_rbl_fault_monthly.fault_source_code = src_data.fault_source_code AND
        opr_rbl_fault_monthly.year_month        = src_data.year_month
      )
   WHEN MATCHED THEN
      UPDATE
      SET
         opr_rbl_fault_monthly.flight_hours = src_data.flight_hours,
         opr_rbl_fault_monthly.fault_cnt    = src_data.fault_cnt
   WHEN NOT MATCHED THEN
      INSERT
         (
            opr_rbl_fault_monthly.operator_id,
            opr_rbl_fault_monthly.aircraft_id,
            opr_rbl_fault_monthly.operator_code,
            opr_rbl_fault_monthly.fleet_type,
            opr_rbl_fault_monthly.tail_number,
            opr_rbl_fault_monthly.config_slot_code,
            opr_rbl_fault_monthly.fault_source_code,
            opr_rbl_fault_monthly.year_month,
            opr_rbl_fault_monthly.flight_hours,
            opr_rbl_fault_monthly.flight_cycles,
            opr_rbl_fault_monthly.fault_cnt
          )
      VALUES
         (
            src_data.operator_id,
            src_data.aircraft_id,
            src_data.operator_code,
            src_data.fleet_type,
            src_data.operator_registration_code,
            src_data.config_slot_code,
            src_data.fault_source_code,
            src_data.year_month,
            src_data.flight_hours,
            src_data.flight_cycles,
            src_data.fault_cnt
         );
     
     -- number of row processed    
     ln_row_processed := SQL%ROWCOUNT;
         

   -- fault summary transformation
   --
   -- number of months between two dates
   ln_mon_between := MONTHS_BETWEEN(TO_DATE(TO_CHAR(aidt_end_date,'YYYYMM'),'YYYYMM'),TO_DATE(TO_CHAR(aidt_start_date,'YYYYMM'),'YYYYMM'));

   -- start processing
   FOR i IN 0..ln_mon_between LOOP

      -- initialize the year month
      ln_next_date  := ADD_MONTHS(aidt_start_date,i);
      ln_year_month := TO_NUMBER(TO_CHAR(ln_next_date,'YYYYMM'));
      -- get the previous 12 months of ln_year_month
      ln_prev_12mon := TO_NUMBER(TO_CHAR(ADD_MONTHS(ln_next_date,-11),'YYYYMM'));

      -- delete all records for the given month

      -- aircraft summary
      DELETE
      FROM
         opr_rbl_fault_summary
      WHERE
         ym_1mon = ln_year_month;

      -- processing fault summary
      MERGE INTO opr_rbl_fault_summary
      USING (
               WITH
               rvw_raw
               AS
               (
                 SELECT
                    operator_id,
                    aircraft_id,
                    operator_code,
                    fleet_type,
                    tail_number,
                    config_slot_code,
                    fault_source_code,
                    year_month,
                    flight_hours,
                    flight_cycles,
                    fault_cnt
                 FROM
                    opr_rbl_fault_monthly
                 WHERE -- from previous 12 month to the given month
                    year_month BETWEEN ln_prev_12mon AND ln_year_month
              ),
              rvw_unq
              AS
              (
                 SELECT
                     operator_id,
                     aircraft_id,
                     operator_code,
                     fleet_type,
                     tail_number,
                     config_slot_code,
                     fault_source_code,
                     year_month,
                     TO_NUMBER(TO_CHAR(add_months(to_date(year_month,'YYYYMM'),-1),'YYYYMM')) ym_2mon,
                     TO_NUMBER(TO_CHAR(add_months(to_date(year_month,'YYYYMM'),-2),'YYYYMM')) ym_3mon,
                     TO_NUMBER(TO_CHAR(add_months(to_date(year_month,'YYYYMM'),-5),'YYYYMM')) ym_6mon,
                     TO_NUMBER(TO_CHAR(add_months(to_date(year_month,'YYYYMM'),-11),'YYYYMM')) ym_12mon
                  FROM
                     rvw_raw
              )
               SELECT
                  rvw_unq.operator_id,
                  rvw_unq.aircraft_id,
                  rvw_unq.operator_code,
                  rvw_unq.fleet_type,
                  rvw_unq.tail_number,
                  rvw_unq.config_slot_code,
                  rvw_unq.fault_source_code,
                  SUBSTR(rvw_unq.year_month,1,4) AS year_code,
                  SUBSTR(rvw_unq.year_month,-2)  AS month_code,
                  rvw_unq.year_month,
                  rvw_unq.ym_2mon,
                  rvw_unq.ym_3mon,
                  rvw_unq.ym_6mon,
                  rvw_unq.ym_12mon,
                  NVL(m1.fault_cnt,0) m1_qt,
                  NVL(m1.flight_hours,0) m1_fh_qt,
                  NVL(m1.flight_cycles,0) m1_fc_qt,
                  NVL(m2.fault_cnt,0) m2_qt,
                  NVL(m2.flight_hours,0) m2_fh_qt,
                  NVL(m2.flight_cycles,0) m2_fc_qt,
                  NVL(m3.fault_cnt,0) m3_qt,
                  NVL(m3.flight_hours,0) m3_fh_qt,
                  NVL(m3.flight_cycles,0) m3_fc_qt,
                 ( -- sum of the fault for the previous 3 months
                    SELECT
                       SUM(fault_cnt)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.aircraft_id       = rvw_unq.aircraft_id       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_3mon AND rvw_unq.year_month
                  ) m3_tot_qt,
                 ( -- sum of the flying hours for the previous 3 months
                    SELECT
                       SUM(flight_hours)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.aircraft_id       = rvw_unq.aircraft_id       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_3mon AND rvw_unq.year_month
                  ) m3_fh_tot_qt,
                 ( -- sum of the flying cycles for the previous 3 months
                    SELECT
                       SUM(flight_cycles)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.aircraft_id       = rvw_unq.aircraft_id       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_3mon AND rvw_unq.year_month
                  ) m3_fc_tot_qt,
                 (-- sum of the fault for the previous 6 months
                    SELECT
                       SUM(fault_cnt)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.aircraft_id       = rvw_unq.aircraft_id       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_6mon AND rvw_unq.year_month
                  ) m6_tot_qt,
                 (-- sum of the flying hours for the previous 6 months
                    SELECT
                       SUM(flight_hours)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.aircraft_id       = rvw_unq.aircraft_id       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_6mon AND rvw_unq.year_month
                  ) m6_fh_tot_qt,
                 (-- sum of the flying cycles for the previous 6 months
                    SELECT
                       SUM(flight_cycles)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.aircraft_id       = rvw_unq.aircraft_id       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_6mon AND rvw_unq.year_month
                  ) m6_fc_tot_qt,
                 (-- sum of the fault for the previous 12 months
                    SELECT
                       SUM(fault_cnt)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.aircraft_id       = rvw_unq.aircraft_id       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_12mon AND rvw_unq.year_month
                  ) m12_tot_qt,
                 (-- sum of the flying hours for the previous 12 months
                    SELECT
                       SUM(flight_hours)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.aircraft_id       = rvw_unq.aircraft_id       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_12mon AND rvw_unq.year_month
                  ) m12_fh_tot_qt,
                 (-- sum of the flying cycles for the previous 12 months
                    SELECT
                       SUM(flight_cycles)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.aircraft_id       = rvw_unq.aircraft_id       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_12mon AND rvw_unq.year_month
                  ) m12_fc_tot_qt
               FROM
                  rvw_unq
                  -- current month
                  LEFT JOIN rvw_raw m1 ON
                     rvw_unq.operator_id       = m1.operator_id       AND
                     rvw_unq.aircraft_id       = m1.aircraft_id       AND
                     rvw_unq.config_slot_code  = m1.config_slot_code  AND
                     rvw_unq.fault_source_code = m1.fault_source_code
                     AND
                     m1.year_month = rvw_unq.year_month
                  -- previous month
                  LEFT JOIN rvw_raw m2 ON
                     rvw_unq.operator_id       = m2.operator_id       AND
                     rvw_unq.aircraft_id       = m2.aircraft_id       AND
                     rvw_unq.config_slot_code  = m2.config_slot_code  AND
                     rvw_unq.fault_source_code = m2.fault_source_code
                     AND
                     m2.year_month = rvw_unq.ym_2mon
                  -- previous 3 month
                  LEFT JOIN rvw_raw m3 ON
                     rvw_unq.operator_id       = m3.operator_id       AND
                     rvw_unq.aircraft_id       = m3.aircraft_id       AND
                     rvw_unq.config_slot_code  = m3.config_slot_code  AND
                     rvw_unq.fault_source_code = m3.fault_source_code
                     AND
                     m3.year_month = rvw_unq.ym_3mon
            ) src_data
      ON (
           opr_rbl_fault_summary.operator_id       = src_data.operator_id AND
           opr_rbl_fault_summary.aircraft_id       = src_data.aircraft_id AND
           opr_rbl_fault_summary.config_slot_code  = src_data.config_slot_code AND
           opr_rbl_fault_summary.fault_source_code = src_data.fault_source_code AND
           opr_rbl_fault_summary.ym_1mon           = src_data.year_month
         )
      WHEN MATCHED THEN
         UPDATE
         SET
            opr_rbl_fault_summary.m1_qt         = src_data.m1_qt,
            opr_rbl_fault_summary.m1_fh_qt      = src_data.m1_fh_qt,
            opr_rbl_fault_summary.m1_fc_qt      = src_data.m1_fc_qt,
            opr_rbl_fault_summary.m2_qt         = src_data.m2_qt,
            opr_rbl_fault_summary.m2_fh_qt      = src_data.m2_fh_qt,
            opr_rbl_fault_summary.m2_fc_qt      = src_data.m2_fc_qt,
            opr_rbl_fault_summary.m3_qt         = src_data.m3_qt,
            opr_rbl_fault_summary.m3_fh_qt      = src_data.m3_fh_qt,
            opr_rbl_fault_summary.m3_fc_qt      = src_data.m3_fc_qt,
            opr_rbl_fault_summary.m3_tot_qt     = src_data.m3_tot_qt,
            opr_rbl_fault_summary.m3_fh_tot_qt  = src_data.m3_fh_tot_qt,
            opr_rbl_fault_summary.m3_fc_tot_qt  = src_data.m3_fc_tot_qt,
            opr_rbl_fault_summary.m6_tot_qt     = src_data.m6_tot_qt,
            opr_rbl_fault_summary.m6_fh_tot_qt  = src_data.m6_fh_tot_qt,
            opr_rbl_fault_summary.m6_fc_tot_qt  = src_data.m6_fc_tot_qt,
            opr_rbl_fault_summary.m12_tot_qt    = src_data.m12_tot_qt,
            opr_rbl_fault_summary.m12_fh_tot_qt = src_data.m12_fh_tot_qt,
            opr_rbl_fault_summary.m12_fc_tot_qt = src_data.m12_fc_tot_qt
      WHEN NOT MATCHED THEN
         INSERT
            (
               opr_rbl_fault_summary.operator_id,
               opr_rbl_fault_summary.aircraft_id,
               opr_rbl_fault_summary.operator_code,
               opr_rbl_fault_summary.fleet_type,
               opr_rbl_fault_summary.tail_number,
               opr_rbl_fault_summary.config_slot_code,
               opr_rbl_fault_summary.fault_source_code,
               opr_rbl_fault_summary.year_code,
               opr_rbl_fault_summary.month_code,
               opr_rbl_fault_summary.ym_1mon,
               opr_rbl_fault_summary.ym_2mon,
               opr_rbl_fault_summary.ym_3mon,
               opr_rbl_fault_summary.ym_6mon,
               opr_rbl_fault_summary.ym_12mon,
               opr_rbl_fault_summary.m1_qt,
               opr_rbl_fault_summary.m1_fh_qt,
               opr_rbl_fault_summary.m1_fc_qt,
               opr_rbl_fault_summary.m2_qt,
               opr_rbl_fault_summary.m2_fh_qt,
               opr_rbl_fault_summary.m2_fc_qt,
               opr_rbl_fault_summary.m3_qt,
               opr_rbl_fault_summary.m3_fh_qt,
               opr_rbl_fault_summary.m3_fc_qt,
               opr_rbl_fault_summary.m3_tot_qt,
               opr_rbl_fault_summary.m3_fh_tot_qt,
               opr_rbl_fault_summary.m3_fc_tot_qt,
               opr_rbl_fault_summary.m6_tot_qt,
               opr_rbl_fault_summary.m6_fh_tot_qt,
               opr_rbl_fault_summary.m6_fc_tot_qt,
               opr_rbl_fault_summary.m12_tot_qt,
               opr_rbl_fault_summary.m12_fh_tot_qt,
               opr_rbl_fault_summary.m12_fc_tot_qt
            )
         VALUES
            (
               src_data.operator_id,
               src_data.aircraft_id,
               src_data.operator_code,
               src_data.fleet_type,
               src_data.tail_number,
               src_data.config_slot_code,
               src_data.fault_source_code,
               src_data.year_code,
               src_data.month_code,
               src_data.year_month,
               src_data.ym_2mon,
               src_data.ym_3mon,
               src_data.ym_6mon,
               src_data.ym_12mon,
               src_data.m1_qt,
               src_data.m1_fh_qt,
               src_data.m1_fc_qt,
               src_data.m2_qt,
               src_data.m2_fh_qt,
               src_data.m2_fc_qt,
               src_data.m3_qt,
               src_data.m3_fh_qt,
               src_data.m3_fc_qt,
               src_data.m3_tot_qt,
               src_data.m3_fh_tot_qt,
               src_data.m3_fc_tot_qt,
               src_data.m6_tot_qt,
               src_data.m6_fh_tot_qt,
               src_data.m6_fc_tot_qt,
               src_data.m12_tot_qt,
               src_data.m12_fh_tot_qt,
               src_data.m12_fc_tot_qt
            );


      -- fleet summary
      DELETE
      FROM
         opr_rbl_fault_fleet_summary
      WHERE
         ym_1mon = ln_year_month;

      -- processing fault fleet summary
      MERGE INTO opr_rbl_fault_fleet_summary
      USING (
               WITH
               rvw_raw
               AS
               (
                SELECT
                   opr_rbl_fault_monthly.operator_id,
                   opr_rbl_fault_monthly.operator_code,
                   opr_rbl_fault_monthly.fleet_type,
                   config_slot_code,
                   fault_source_code,
                   opr_rbl_fault_monthly.year_month,
                   NVL(fleet_usage.flight_hours,0) flight_hours,
                   NVL(fleet_usage.flight_cycles,0) flight_cycles,
                   SUM(fault_cnt) fault_cnt
                FROM
                   opr_rbl_fault_monthly
                   LEFT JOIN (
                               SELECT
                                  operator_id,
                                  operator_code,
                                  TO_NUMBER(year_code || month_code) AS year_month,
                                  fleet_type,
                                  SUM(flight_hours) flight_hours,
                                  SUM(cycles) flight_cycles
                               FROM
                                  opr_monthly_reliability
                               GROUP BY
                                  operator_id,
                                  operator_code,
                                  TO_NUMBER(year_code || month_code),
                                  fleet_type
                              ) fleet_usage ON
                      opr_rbl_fault_monthly.operator_id   = fleet_usage.operator_id   AND
                      opr_rbl_fault_monthly.operator_code = fleet_usage.operator_code AND
                      opr_rbl_fault_monthly.year_month    = fleet_usage.year_month    AND
                      opr_rbl_fault_monthly.fleet_type     = fleet_usage.fleet_type
                 WHERE -- from previous 12 month to the given month
                    opr_rbl_fault_monthly.year_month BETWEEN ln_prev_12mon AND ln_year_month
                GROUP BY
                   opr_rbl_fault_monthly.operator_id,
                   opr_rbl_fault_monthly.operator_code,
                   opr_rbl_fault_monthly.fleet_type,
                   config_slot_code,
                   fault_source_code,
                   opr_rbl_fault_monthly.year_month,
                   NVL(fleet_usage.flight_hours,0),
                   NVL(fleet_usage.flight_cycles,0)
               ),
               rvw_unq
               AS
                 (
                  SELECT
                     operator_id,
                     operator_code,
                     fleet_type,
                     config_slot_code,
                     fault_source_code,
                     year_month,
                     TO_NUMBER(TO_CHAR(add_months(to_date(year_month,'YYYYMM'),-1),'YYYYMM')) ym_2mon,
                     TO_NUMBER(TO_CHAR(add_months(to_date(year_month,'YYYYMM'),-2),'YYYYMM')) ym_3mon,
                     TO_NUMBER(TO_CHAR(add_months(to_date(year_month,'YYYYMM'),-5),'YYYYMM')) ym_6mon,
                     TO_NUMBER(TO_CHAR(add_months(to_date(year_month,'YYYYMM'),-11),'YYYYMM')) ym_12mon
                  FROM
                     rvw_raw
                 )
               SELECT
                  rvw_unq.operator_id,
                  rvw_unq.operator_code,
                  rvw_unq.fleet_type,
                  rvw_unq.config_slot_code,
                  rvw_unq.fault_source_code,
                  SUBSTR(rvw_unq.year_month,1,4) AS year_code,
                  SUBSTR(rvw_unq.year_month,-2)  AS month_code,
                  rvw_unq.year_month,
                  rvw_unq.ym_2mon,
                  rvw_unq.ym_3mon,
                  rvw_unq.ym_6mon,
                  rvw_unq.ym_12mon,
                  NVL(m1.fault_cnt,0) m1_qt,
                  NVL(m1.flight_hours,0) m1_fh_qt,
                  NVL(m1.flight_cycles,0) m1_fc_qt,
                  NVL(m2.fault_cnt,0) m2_qt,
                  NVL(m2.flight_hours,0) m2_fh_qt,
                  NVL(m2.flight_cycles,0) m2_fc_qt,
                  NVL(m3.fault_cnt,0) m3_qt,
                  NVL(m3.flight_hours,0) m3_fh_qt,
                  NVL(m3.flight_cycles,0) m3_fc_qt,
                 ( -- sum of the fault for the previous 3 months
                    SELECT
                       SUM(fault_cnt)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.fleet_type        = rvw_unq.fleet_type       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_3mon AND rvw_unq.year_month
                  ) m3_tot_qt,
                 ( -- sum of the flying hours for the previous 3 months
                    SELECT
                       SUM(flight_hours)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.fleet_type        = rvw_unq.fleet_type       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_3mon AND rvw_unq.year_month
                  ) m3_fh_tot_qt,
                 ( -- sum of the flying cycles for the previous 3 months
                    SELECT
                       SUM(flight_cycles)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.fleet_type        = rvw_unq.fleet_type       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_3mon AND rvw_unq.year_month
                  ) m3_fc_tot_qt,
                 (-- sum of the fault for the previous 6 months
                    SELECT
                       SUM(fault_cnt)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.fleet_type        = rvw_unq.fleet_type       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_6mon AND rvw_unq.year_month
                  ) m6_tot_qt,
                 (-- sum of the flying hours for the previous 6 months
                    SELECT
                       SUM(flight_hours)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.fleet_type        = rvw_unq.fleet_type       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_6mon AND rvw_unq.year_month
                  ) m6_fh_tot_qt,
                 (-- sum of the flying hours for the previous 6 months
                    SELECT
                       SUM(flight_cycles)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.fleet_type        = rvw_unq.fleet_type       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_6mon AND rvw_unq.year_month
                  ) m6_fc_tot_qt,
                 (-- sum of the fault for the previous 12 months
                    SELECT
                       SUM(fault_cnt)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.fleet_type        = rvw_unq.fleet_type       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_12mon AND rvw_unq.year_month
                  ) m12_tot_qt,
                 (-- sum of the flying hours for the previous 12 months
                    SELECT
                       SUM(flight_hours)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.fleet_type        = rvw_unq.fleet_type       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_12mon AND rvw_unq.year_month
                  ) m12_fh_tot_qt,
                 (-- sum of the flying hours for the previous 12 months
                    SELECT
                       SUM(flight_cycles)
                    FROM
                       rvw_raw
                    WHERE
                       rvw_raw.operator_id       = rvw_unq.operator_id       AND
                       rvw_raw.fleet_type        = rvw_unq.fleet_type       AND
                       rvw_raw.config_slot_code  = rvw_unq.config_slot_code  AND
                       rvw_raw.fault_source_code = rvw_unq.fault_source_code
                       AND
                       rvw_raw.year_month BETWEEN rvw_unq.ym_12mon AND rvw_unq.year_month
                  ) m12_fc_tot_qt
               FROM
                  rvw_unq
                  -- current month
                  LEFT JOIN rvw_raw m1 ON
                     rvw_unq.operator_id       = m1.operator_id       AND
                     rvw_unq.fleet_type        = m1.fleet_type       AND
                     rvw_unq.config_slot_code  = m1.config_slot_code  AND
                     rvw_unq.fault_source_code = m1.fault_source_code
                     AND
                     m1.year_month = rvw_unq.year_month
                  -- previous month
                  LEFT JOIN rvw_raw m2 ON
                     rvw_unq.operator_id       = m2.operator_id       AND
                     rvw_unq.fleet_type        = m2.fleet_type       AND
                     rvw_unq.config_slot_code  = m2.config_slot_code  AND
                     rvw_unq.fault_source_code = m2.fault_source_code
                     AND
                     m2.year_month = rvw_unq.ym_2mon
                  -- previous 3 month
                  LEFT JOIN rvw_raw m3 ON
                     rvw_unq.operator_id       = m3.operator_id       AND
                     rvw_unq.fleet_type        = m3.fleet_type       AND
                     rvw_unq.config_slot_code  = m3.config_slot_code  AND
                     rvw_unq.fault_source_code = m3.fault_source_code
                     AND
                     m3.year_month = rvw_unq.ym_3mon
            ) src_data
      ON (
           opr_rbl_fault_fleet_summary.operator_id       = src_data.operator_id AND
           opr_rbl_fault_fleet_summary.fleet_type        = src_data.fleet_type AND
           opr_rbl_fault_fleet_summary.config_slot_code  = src_data.config_slot_code AND
           opr_rbl_fault_fleet_summary.fault_source_code = src_data.fault_source_code AND
           opr_rbl_fault_fleet_summary.ym_1mon           = src_data.year_month
         )
      WHEN MATCHED THEN
         UPDATE
         SET
            opr_rbl_fault_fleet_summary.m1_qt         = src_data.m1_qt,
            opr_rbl_fault_fleet_summary.m1_fh_qt      = src_data.m1_fh_qt,
            opr_rbl_fault_fleet_summary.m1_fc_qt      = src_data.m1_fc_qt,
            opr_rbl_fault_fleet_summary.m2_qt         = src_data.m2_qt,
            opr_rbl_fault_fleet_summary.m2_fh_qt      = src_data.m2_fh_qt,
            opr_rbl_fault_fleet_summary.m2_fc_qt      = src_data.m2_fc_qt,
            opr_rbl_fault_fleet_summary.m3_qt         = src_data.m3_qt,
            opr_rbl_fault_fleet_summary.m3_fh_qt      = src_data.m3_fh_qt,
            opr_rbl_fault_fleet_summary.m3_fc_qt      = src_data.m3_fc_qt,
            opr_rbl_fault_fleet_summary.m3_tot_qt     = src_data.m3_tot_qt,
            opr_rbl_fault_fleet_summary.m3_fh_tot_qt  = src_data.m3_fh_tot_qt,
            opr_rbl_fault_fleet_summary.m3_fc_tot_qt  = src_data.m3_fc_tot_qt,
            opr_rbl_fault_fleet_summary.m6_tot_qt     = src_data.m6_tot_qt,
            opr_rbl_fault_fleet_summary.m6_fh_tot_qt  = src_data.m6_fh_tot_qt,
            opr_rbl_fault_fleet_summary.m6_fc_tot_qt  = src_data.m6_fc_tot_qt,
            opr_rbl_fault_fleet_summary.m12_tot_qt    = src_data.m12_tot_qt,
            opr_rbl_fault_fleet_summary.m12_fh_tot_qt = src_data.m12_fh_tot_qt,
            opr_rbl_fault_fleet_summary.m12_fc_tot_qt = src_data.m12_fc_tot_qt
      WHEN NOT MATCHED THEN
         INSERT
            (
               opr_rbl_fault_fleet_summary.operator_id,
               opr_rbl_fault_fleet_summary.operator_code,
               opr_rbl_fault_fleet_summary.fleet_type,
               opr_rbl_fault_fleet_summary.config_slot_code,
               opr_rbl_fault_fleet_summary.fault_source_code,
               opr_rbl_fault_fleet_summary.year_code,
               opr_rbl_fault_fleet_summary.month_code,
               opr_rbl_fault_fleet_summary.ym_1mon,
               opr_rbl_fault_fleet_summary.ym_2mon,
               opr_rbl_fault_fleet_summary.ym_3mon,
               opr_rbl_fault_fleet_summary.ym_6mon,
               opr_rbl_fault_fleet_summary.ym_12mon,
               opr_rbl_fault_fleet_summary.m1_qt,
               opr_rbl_fault_fleet_summary.m1_fh_qt,
               opr_rbl_fault_fleet_summary.m1_fc_qt,
               opr_rbl_fault_fleet_summary.m2_qt,
               opr_rbl_fault_fleet_summary.m2_fh_qt,
               opr_rbl_fault_fleet_summary.m2_fc_qt,
               opr_rbl_fault_fleet_summary.m3_qt,
               opr_rbl_fault_fleet_summary.m3_fh_qt,
               opr_rbl_fault_fleet_summary.m3_fc_qt,
               opr_rbl_fault_fleet_summary.m3_tot_qt,
               opr_rbl_fault_fleet_summary.m3_fh_tot_qt,
               opr_rbl_fault_fleet_summary.m3_fc_tot_qt,
               opr_rbl_fault_fleet_summary.m6_tot_qt,
               opr_rbl_fault_fleet_summary.m6_fh_tot_qt,
               opr_rbl_fault_fleet_summary.m6_fc_tot_qt,
               opr_rbl_fault_fleet_summary.m12_tot_qt,
               opr_rbl_fault_fleet_summary.m12_fh_tot_qt,
               opr_rbl_fault_fleet_summary.m12_fc_tot_qt
            )
         VALUES
            (
               src_data.operator_id,
               src_data.operator_code,
               src_data.fleet_type,
               src_data.config_slot_code,
               src_data.fault_source_code,
               src_data.year_code,
               src_data.month_code,
               src_data.year_month,
               src_data.ym_2mon,
               src_data.ym_3mon,
               src_data.ym_6mon,
               src_data.ym_12mon,
               src_data.m1_qt,
               src_data.m1_fh_qt,
               src_data.m1_fc_qt,
               src_data.m2_qt,
               src_data.m2_fh_qt,
               src_data.m2_fc_qt,
               src_data.m3_qt,
               src_data.m3_fh_qt,
               src_data.m3_fc_qt,
               src_data.m3_tot_qt,
               src_data.m3_fh_tot_qt,
               src_data.m3_fc_tot_qt,
               src_data.m6_tot_qt,
               src_data.m6_fh_tot_qt,
               src_data.m6_fc_tot_qt,
               src_data.m12_tot_qt,
               src_data.m12_fh_tot_qt,
               src_data.m12_fc_tot_qt
            );

   END LOOP;

   RETURN ln_row_processed;

END opr_rbl_fault_xform_proc;

-- package end
END opr_rbl_fault_pkg;
/