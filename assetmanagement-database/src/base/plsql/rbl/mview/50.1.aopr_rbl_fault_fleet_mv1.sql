--liquibase formatted sql


--changeSet 50.1.aopr_rbl_fault_fleet_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_FAULT_FLEET_MV1');
END;
/

--changeSet 50.1.aopr_rbl_fault_fleet_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_fault_fleet_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
WITH
   rvw_raw
   AS
   (
     SELECT
        operator_id,
        operator_code,
        fleet_type,
        config_slot_code,
        fault_source_code,
        year_month,
        flight_hours,
        cycles,
        fault_cnt
     FROM
       aopr_rbl_fault_fleet_mon_mv1
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
         TO_NUMBER(TO_CHAR(add_months(to_date(year_month,'YYYYMM'),-11),'YYYYMM')) ym_12mon,
         TO_NUMBER(TO_CHAR(add_months(to_date(year_month,'YYYYMM'),-14),'YYYYMM')) ym_15mon
      FROM
         rvw_raw
   ),
   rvw_fault_m3
   AS
   ( -- previous 3 months
     SELECT
        rvw_unq.operator_id,
        rvw_unq.fleet_type,
        rvw_unq.config_slot_code,
        rvw_unq.fault_source_code,
        rvw_unq.year_month,
        SUM(rvw_raw.fault_cnt) m3_tot_qt,
        SUM(rvw_raw.flight_hours) m3_fh_tot_qt,
        SUM(rvw_raw.cycles) m3_fc_tot_qt
     FROM
        rvw_unq
        LEFT JOIN rvw_raw ON
           rvw_unq.operator_id       = rvw_raw.operator_id AND
           rvw_unq.fleet_type        = rvw_raw.fleet_type AND
           rvw_unq.config_slot_code  = rvw_raw.config_slot_code AND
           rvw_unq.fault_source_code = rvw_raw.fault_source_code
           AND -- 3 months
           rvw_raw.year_month BETWEEN rvw_unq.ym_3mon AND rvw_unq.year_month
     GROUP BY
        rvw_unq.operator_id,
        rvw_unq.fleet_type,
        rvw_unq.config_slot_code,
        rvw_unq.fault_source_code,
        rvw_unq.year_month
   ),
   rvw_fault_m6
   AS
   ( -- previous 6 months
     SELECT
        rvw_unq.operator_id,
        rvw_unq.fleet_type,
        rvw_unq.config_slot_code,
        rvw_unq.fault_source_code,
        rvw_unq.year_month,
        SUM(rvw_raw.fault_cnt) m6_tot_qt,
        SUM(rvw_raw.flight_hours) m6_fh_tot_qt,
        SUM(rvw_raw.cycles) m6_fc_tot_qt
     FROM
        rvw_unq
        LEFT JOIN rvw_raw ON
           rvw_unq.operator_id       = rvw_raw.operator_id AND
           rvw_unq.fleet_type        = rvw_raw.fleet_type AND
           rvw_unq.config_slot_code  = rvw_raw.config_slot_code AND
           rvw_unq.fault_source_code = rvw_raw.fault_source_code
           AND -- 6 months
           rvw_raw.year_month BETWEEN rvw_unq.ym_6mon AND rvw_unq.year_month
     GROUP BY
        rvw_unq.operator_id,
        rvw_unq.fleet_type,
        rvw_unq.config_slot_code,
        rvw_unq.fault_source_code,
        rvw_unq.year_month
   ),
   rvw_fault_m12
   AS
   ( -- previous 12 months
     SELECT
        rvw_unq.operator_id,
        rvw_unq.fleet_type,
        rvw_unq.config_slot_code,
        rvw_unq.fault_source_code,
        rvw_unq.year_month,
        SUM(rvw_raw.fault_cnt) m12_tot_qt,
        SUM(rvw_raw.flight_hours) m12_fh_tot_qt,
        SUM(rvw_raw.cycles) m12_fc_tot_qt
     FROM
        rvw_unq
        left JOIN rvw_raw  ON
           rvw_unq.operator_id       = rvw_raw.operator_id AND
           rvw_unq.fleet_type        = rvw_raw.fleet_type AND
           rvw_unq.config_slot_code  = rvw_raw.config_slot_code  AND
           rvw_unq.fault_source_code = rvw_raw.fault_source_code
           AND -- 12 months
           rvw_raw.year_month BETWEEN rvw_unq.ym_12mon AND rvw_unq.year_month
     GROUP BY
        rvw_unq.operator_id,
        rvw_unq.fleet_type,
        rvw_unq.config_slot_code,
        rvw_unq.fault_source_code,
        rvw_unq.year_month
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
      -- quantity
      NVL(m1.fault_cnt,0) m1_qt,
      NVL(m2.fault_cnt,0) m2_qt,
      NVL(m3.fault_cnt,0) m3_qt,
      NVL(rvw_fault_m3.m3_tot_qt,0) m3_tot_qt,
      NVL(rvw_fault_m6.m6_tot_qt,0) m6_tot_qt,
      NVL(rvw_fault_m12.m12_tot_qt,0) m12_tot_qt,
      -- rates: current month
      CASE
        WHEN NVL(m1.flight_hours,0) > 0 THEN
           (m1.fault_cnt/m1.flight_hours)*1000
        ELSE 0
      END AS m1_avg_qt,
      CASE
        WHEN NVL(m1.cycles,0) > 0 THEN
           (m1.fault_cnt/m1.cycles)*100
        ELSE 0
      END AS m1_fc_avg_qt,
      -- rates: last month
      CASE
        WHEN NVL(m2.flight_hours,0) > 0 THEN
           (m2.fault_cnt/m2.flight_hours)*1000
        ELSE 0
      END AS m2_avg_qt,
      CASE
        WHEN NVL(m2.cycles,0) > 0 THEN
           (m2.fault_cnt/m2.cycles)*100
        ELSE 0
      END AS m2_fc_avg_qt,
      -- rates : previous 3 month
      CASE
        WHEN NVL(m3.flight_hours,0) > 0 THEN
           (m3.fault_cnt/m3.flight_hours)*1000
        ELSE 0
      END AS m3_avg_qt,
      CASE
        WHEN NVL(m3.cycles,0) > 0 THEN
           (m3.fault_cnt/m3.cycles)*100
        ELSE 0
      END AS m3_fc_avg_qt,
      -- rates: last 3 months
      CASE
        WHEN NVL(rvw_fault_m3.m3_fh_tot_qt,0) > 0 THEN
           (rvw_fault_m3.m3_tot_qt/rvw_fault_m3.m3_fh_tot_qt)*1000
        ELSE 0
      END AS m3_tot_avg_qt,
      CASE
        WHEN NVL(rvw_fault_m3.m3_fc_tot_qt,0) > 0 THEN
           (rvw_fault_m3.m3_tot_qt/rvw_fault_m3.m3_fc_tot_qt)*100
        ELSE 0
      END AS m3_tot_fc_avg_qt,
      -- rates: last 6 months
      CASE
        WHEN NVL(rvw_fault_m6.m6_fh_tot_qt,0) > 0 THEN
           (rvw_fault_m6.m6_tot_qt/rvw_fault_m6.m6_fh_tot_qt)*1000
        ELSE 0
      END AS m6_tot_avg_qt,
      CASE
        WHEN NVL(rvw_fault_m6.m6_fc_tot_qt,0) > 0 THEN
           (rvw_fault_m6.m6_tot_qt/rvw_fault_m6.m6_fc_tot_qt)*100
        ELSE 0
      END AS m6_tot_fc_avg_qt,
      -- rates: last 12 months
      CASE
        WHEN NVL(rvw_fault_m12.m12_fh_tot_qt,0) > 0 THEN
           (rvw_fault_m12.m12_tot_qt/rvw_fault_m12.m12_fh_tot_qt)*1000
        ELSE 0
      END AS m12_tot_avg_qt,
      CASE
        WHEN NVL(rvw_fault_m12.m12_fc_tot_qt,0) > 0 THEN
           (rvw_fault_m12.m12_tot_qt/rvw_fault_m12.m12_fc_tot_qt)*100
        ELSE 0
      END AS m12_tot_fc_avg_qt,
      -- rates: last 12 (between 3rd and 15th) months
      NVL(fleet_ucl.ucl_fh,0)  AS m12_m3to15_ucl_fh,
      NVL(fleet_ucl.ucl_fc,0)  AS m12_m3to15_ucl_fc
   FROM
      rvw_unq
      LEFT JOIN rvw_raw m1 ON
         -- current month
         rvw_unq.operator_id       = m1.operator_id AND
         rvw_unq.fleet_type        = m1.fleet_type AND
         rvw_unq.config_slot_code  = m1.config_slot_code AND
         rvw_unq.fault_source_code = m1.fault_source_code
         AND
         rvw_unq.year_month        = m1.year_month
      LEFT JOIN rvw_raw m2 ON
         -- last month
         rvw_unq.operator_id       = m2.operator_id AND
         rvw_unq.fleet_type        = m2.fleet_type AND
         rvw_unq.config_slot_code  = m2.config_slot_code AND
         rvw_unq.fault_source_code = m2.fault_source_code
         AND
         rvw_unq.ym_2mon           = m2.year_month
      LEFT JOIN rvw_raw m3 ON
         -- previous 2 months
         rvw_unq.operator_id       = m3.operator_id  AND
         rvw_unq.fleet_type        = m3.fleet_type AND
         rvw_unq.config_slot_code  = m3.config_slot_code AND
         rvw_unq.fault_source_code = m3.fault_source_code
         AND
         rvw_unq.ym_3mon           = m3.year_month
      INNER JOIN rvw_fault_m3 ON
         -- total of last 3 months
         rvw_unq.operator_id       = rvw_fault_m3.operator_id AND
         rvw_unq.fleet_type        = rvw_fault_m3.fleet_type  AND
         rvw_unq.config_slot_code  = rvw_fault_m3.config_slot_code AND
         rvw_unq.fault_source_code = rvw_fault_m3.fault_source_code
         AND
         rvw_unq.year_month        = rvw_fault_m3.year_month
      INNER JOIN rvw_fault_m6 ON
         -- total of last 6 months
         rvw_unq.operator_id       = rvw_fault_m6.operator_id AND
         rvw_unq.fleet_type        = rvw_fault_m6.fleet_type AND
         rvw_unq.config_slot_code  = rvw_fault_m6.config_slot_code AND
         rvw_unq.fault_source_code = rvw_fault_m6.fault_source_code
         AND
         rvw_unq.year_month        = rvw_fault_m6.year_month
      INNER JOIN rvw_fault_m12 ON
         -- total of last 12 months
         rvw_unq.operator_id       = rvw_fault_m12.operator_id AND
         rvw_unq.fleet_type        = rvw_fault_m12.fleet_type AND
         rvw_unq.config_slot_code  = rvw_fault_m12.config_slot_code AND
         rvw_unq.fault_source_code = rvw_fault_m12.fault_source_code
         AND
         rvw_unq.year_month        = rvw_fault_m12.year_month
      LEFT JOIN aopr_rbl_fault_fleet_ucl_mv1 fleet_ucl ON
         -- check the materialized view opr_rbl_fault_fleet_ucl_mv1 for computation
         rvw_unq.operator_id       = fleet_ucl.operator_id AND
         rvw_unq.fleet_type        = fleet_ucl.fleet_type AND
         rvw_unq.config_slot_code  = fleet_ucl.config_slot_code
         AND -- previous year
         TO_NUMBER(SUBSTR(rvw_unq.year_month,1,4)-1) = fleet_ucl.year_code;
