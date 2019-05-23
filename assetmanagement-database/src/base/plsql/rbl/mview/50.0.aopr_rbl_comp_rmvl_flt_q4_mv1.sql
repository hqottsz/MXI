--liquibase formatted sql


--changeSet 50.0.aopr_rbl_comp_rmvl_flt_q4_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_COMP_RMVL_Q4_MV1');
END;
/

--changeSet 50.0.aopr_rbl_comp_rmvl_flt_q4_mv1:2 stripComments:false
create materialized view AOPR_RBL_COMP_RMVL_Q4_MV1
build deferred
refresh complete on demand
as
WITH
rvw_rmvl_mon
AS
   (
      SELECT
         operator_id,
         fleet_type,
         config_slot,
         part_number,
         year_month,
         -- number_of_units
         -- (quantity_per_acft * number_of_acft) QPA,
         quantity_per_acft QPA,
         removal_count,
         sched_removal,
         unsched_removal,
         justified_failure
      FROM
         (
            SELECT
               operator_id,
               fleet_type,
               config_slot,
               part_number,
               year_month,
               quantity_per_acft,
               number_of_acft,
               removal_count,
               sched_removal,
               unsched_removal,
               justified_failure
            FROM
               opr_rbl_comp_rmvl_fleet_mon
             -- above Maintenix component removal data
            UNION ALL
             -- to legacy component removal data
            SELECT
               operator_id,
               fleet_type,
               config_slot,
               part_number,
               year_month,
               quantity_per_acft,
               number_of_acft,
               removal_count,
               sched_removal,
               unsched_removal,
               justified_failure
            FROM
               opr_rbl_hist_comp_rmvl_flt_mon
         )
   ),
rvw_rmvl_unq
AS
  (
      SELECT
         operator_id,
         fleet_type,
         config_slot,
         part_number,
         QPA,
         year_month,
         TO_NUMBER(TO_CHAR(ADD_MONTHS(to_date(year_month,'YYYYMM'),-9),'YYYYMM')) ym_10mon,
         TO_NUMBER(TO_CHAR(ADD_MONTHS(to_date(year_month,'YYYYMM'),-11),'YYYYMM')) ym_12mon
      FROM
         rvw_rmvl_mon
  ),
rvw_rmvl_q4
AS
   (
      SELECT
         rvw_rmvl_unq.operator_id,
         rvw_rmvl_unq.fleet_type,
         rvw_rmvl_unq.config_slot,
         rvw_rmvl_unq.part_number,
         rvw_rmvl_unq.year_month,
         rvw_rmvl_unq.QPA,
         SUM(removal_count) removal_count,
         SUM(sched_removal) sched_removal,
         SUM(unsched_removal) unsched_removal,
         SUM(justified_failure) justified_failure
      FROM
         rvw_rmvl_unq
         LEFT JOIN rvw_rmvl_mon ON
              rvw_rmvl_unq.operator_id  = rvw_rmvl_mon.operator_id AND
              rvw_rmvl_unq.fleet_type   = rvw_rmvl_mon.fleet_type AND
              rvw_rmvl_unq.config_slot  = rvw_rmvl_mon.config_slot AND
              rvw_rmvl_unq.part_number  = rvw_rmvl_mon.part_number
              AND -- previous remonths
              rvw_rmvl_mon.year_month BETWEEN rvw_rmvl_unq.ym_12mon AND rvw_rmvl_unq.ym_10mon
      GROUP BY
         rvw_rmvl_unq.operator_id,
         rvw_rmvl_unq.fleet_type,
         rvw_rmvl_unq.config_slot,
         rvw_rmvl_unq.part_number,
         rvw_rmvl_unq.year_month,
         rvw_rmvl_unq.QPA
   ),
rvw_usage_q4
AS
   (
      SELECT
         rvw_rmvl_unq.operator_id,
         rvw_rmvl_unq.fleet_type,
         rvw_rmvl_unq.config_slot,
         rvw_rmvl_unq.part_number,
         rvw_rmvl_unq.year_month,
         SUM(NVL(cycles,0)) cycles,
         SUM(NVL(flight_hours,0)) flight_hours
      FROM
         rvw_rmvl_unq
         LEFT JOIN aopr_monthly_operator_usage_v1 monthly_usage ON
              rvw_rmvl_unq.operator_id = monthly_usage.operator_id AND
              rvw_rmvl_unq.fleet_type  = monthly_usage.fleet_type
              AND -- previous remonths
              TO_NUMBER(monthly_usage.year_code || monthly_usage.month_code) BETWEEN rvw_rmvl_unq.ym_12mon AND rvw_rmvl_unq.ym_10mon
      GROUP BY
         rvw_rmvl_unq.operator_id,
         rvw_rmvl_unq.fleet_type,
         rvw_rmvl_unq.config_slot,
         rvw_rmvl_unq.part_number,
         rvw_rmvl_unq.year_month
   ),
rvw_rmvl_q4_avg
AS
   (
      SELECT
         rvw_rmvl_q4.operator_id,
         rvw_rmvl_q4.fleet_type,
         rvw_rmvl_q4.config_slot,
         rvw_rmvl_q4.part_number,
         rvw_rmvl_q4.year_month,
         removal_count,
         unsched_removal,
         justified_failure,
         cycles,
         flight_hours,
         -- mean time between removal - hours
         CASE
            WHEN NVL(removal_count,0) > 0 THEN
              (QPA * flight_hours)/removal_count
            ELSE 0
         END  AS mtbr,
         -- mean time between removal - cycles
         CASE
            WHEN NVL(removal_count,0) > 0 THEN
              (QPA * cycles)/removal_count
            ELSE 0
         END AS mcbr,
         -- mean time between unschedule removal - hour
         CASE
            WHEN NVL(unsched_removal,0) > 0 THEN
              (QPA * flight_hours)/unsched_removal
            ELSE 0
         END AS mtbur,
         -- mean time between unschedule removal - cycles
         CASE
            WHEN NVL(unsched_removal,0) > 0 THEN
               (QPA * cycles)/unsched_removal
            ELSE 0
         END AS mcbur,
         -- mean time between failure removal - hours
         CASE
            WHEN NVL(justified_failure,0) > 0 THEN
               (QPA * flight_hours)/justified_failure
            ELSE 0
         END AS mtbf,
         -- mean time between failure removal - cycles
         CASE
            WHEN NVL(justified_failure,0) > 0 THEN
               (QPA * cycles)/justified_failure
            ELSE 0
         END AS mcbf,
         -- failure_rate
         CASE
           WHEN NVL(justified_failure,0) > 0 THEN
              CASE
                  WHEN NVL(((QPA * flight_hours)/justified_failure),0) > 0 THEN
                     (1/((QPA * flight_hours)/justified_failure))*1000
                  ELSE 0
               END
           ELSE
              0
         END AS failure_rate,
         -- unschedule removal rate - hours
         CASE
            WHEN NVL((QPA * flight_hours),0) > 0 THEN
               (unsched_removal/(QPA * flight_hours))*1000
            ELSE 0
         END AS urr_hours,
         -- unschedule removal rate - cycles
         CASE
            WHEN NVL((QPA * cycles),0) > 0 THEN
               (unsched_removal/(QPA * cycles))*1000
            ELSE 0
        END AS urr_cycles
     FROM
        rvw_rmvl_q4
        INNER JOIN rvw_usage_q4 ON
           rvw_rmvl_q4.operator_id  = rvw_usage_q4.operator_id AND
           rvw_rmvl_q4.fleet_type   = rvw_usage_q4.fleet_type AND
           rvw_rmvl_q4.config_slot  = rvw_usage_q4.config_slot AND
           rvw_rmvl_q4.part_number  = rvw_usage_q4.part_number AND
           rvw_rmvl_q4.year_month   = rvw_usage_q4.year_month
   )
SELECT
   operator_id,
   fleet_type,
   config_slot,
   part_number,
   year_month,
   -- q4 - between 1st and 3rd month
   NVL(removal_count,0)      AS removal_q4,
   NVL(unsched_removal,0)    AS unsched_removal_q4,
   NVL(justified_failure,0)  AS justified_failure_q4,
   NVL(cycles,0)             AS total_cycle_q4,
   NVL(flight_hours,0)       AS total_hour_q4,
   NVL(mtbr,0)         AS mtbr_q4,
   NVL(mcbr,0)         AS mcbr_q4,
   NVL(mtbur,0)        AS mtbur_q4,
   NVL(mcbur,0)        AS mcbur_q4,
   NVL(mtbf,0)         AS mtbf_q4,
   NVL(mcbf,0)         AS mcbf_q4,
   NVL(failure_rate,0) AS f_rate_q4,
   NVL(urr_hours,0)    AS urr_hour_q4,
   NVL(urr_cycles,0)   AS urr_cycle_q4
FROM
   rvw_rmvl_q4_avg;