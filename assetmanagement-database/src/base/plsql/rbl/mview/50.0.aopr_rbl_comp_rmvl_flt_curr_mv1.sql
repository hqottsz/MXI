--liquibase formatted sql


--changeSet 50.0.aopr_rbl_comp_rmvl_flt_curr_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_COMP_RMVL_CURR_MV1');
END;
/

--changeSet 50.0.aopr_rbl_comp_rmvl_flt_curr_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_comp_rmvl_curr_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
WITH
rvw_rmvl_mon
AS
   (
      SELECT
         operator_id,
         operator_code,
         fleet_type,
         config_slot,
         part_number,
         part_name,
         inventory_class,
         year_month,
         cycles,
         flight_hours,
         quantity_per_acft,
         number_of_acft,
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
               operator_code,
               fleet_type,
               config_slot,
               part_number,
               part_name,
               inventory_class,
               year_month,
               cycles,
               flight_hours,
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
               operator_code,
               fleet_type,
               config_slot,
               part_number,
               part_name,
               inventory_class,
               year_month,
               cycles,
               flight_hours,
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
rvw_rmvl_mon_avg
AS
   (
      SELECT
         operator_id,
         operator_code,
         fleet_type,
         config_slot,
         part_number,
         part_name,
         inventory_class,
         unsched_removal,
         removal_count,
         number_of_acft,
         quantity_per_acft,
         QPA,
         year_month,
         cycles,
         flight_hours,
         justified_failure,
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
               (unsched_removal / (QPA * flight_hours))*1000
            ELSE 0
         END AS urr_hours,
         -- unschedule removal rate - cycles
         CASE
            WHEN NVL((QPA * cycles),0) > 0 THEN
               (unsched_removal/(QPA * cycles))*1000
            ELSE 0
        END AS urr_cycles
     FROM
        rvw_rmvl_mon
   )
SELECT
   rvw_rmvl_mon_avg.operator_id,
   rvw_rmvl_mon_avg.operator_code,
   rvw_rmvl_mon_avg.fleet_type,
   rvw_rmvl_mon_avg.config_slot,
   rvw_rmvl_mon_avg.part_number,
   rvw_rmvl_mon_avg.part_name,
   rvw_rmvl_mon_avg.year_month,
   -- current month
   NVL(rvw_rmvl_mon_avg.removal_count,0)      AS removal_curr,
   NVL(rvw_rmvl_mon_avg.unsched_removal,0)    AS unsched_removal_curr,
   NVL(rvw_rmvl_mon_avg.justified_failure,0)  AS justified_failure_curr,
   NVL(rvw_rmvl_mon_avg.cycles,0)             AS total_cycle_curr,
   NVL(rvw_rmvl_mon_avg.flight_hours,0)       AS total_hour_curr,
   NVL(rvw_rmvl_mon_avg.mtbr,0)         AS mtbr_curr,
   NVL(rvw_rmvl_mon_avg.mcbr,0)         AS mcbr_curr,
   NVL(rvw_rmvl_mon_avg.mtbur,0)        AS mtbur_curr,
   NVL(rvw_rmvl_mon_avg.mcbur,0)        AS mcbur_curr,
   NVL(rvw_rmvl_mon_avg.mtbf,0)         AS mtbf_curr,
   NVL(rvw_rmvl_mon_avg.mcbf,0)         AS mcbf_curr,
   NVL(rvw_rmvl_mon_avg.failure_rate,0) AS f_rate_curr,
   NVL(rvw_rmvl_mon_avg.urr_hours,0)    AS urr_hour_curr,
   NVL(rvw_rmvl_mon_avg.urr_cycles,0)   AS urr_cycle_curr,
   -- last 12 months UCL
   NVL(last_12mon_ucl.ucl_hours,0)      AS ucl_hour_12mon,
   NVL(last_12mon_ucl.ucl_cycles,0)     AS ucl_cycle_12mon,
   -- aircraft count
   rvw_rmvl_mon_avg.inventory_class,
   rvw_rmvl_mon_avg.quantity_per_acft   AS quantity_per_acft,
   rvw_rmvl_mon_avg.number_of_acft      AS number_of_acft,
   QPA AS number_of_units
FROM
   rvw_rmvl_mon_avg
   LEFT JOIN aopr_rbl_comp_rmvl_flt_ucl_mv1 last_12mon_ucl ON
      rvw_rmvl_mon_avg.operator_code      = last_12mon_ucl.operator_code AND
      rvw_rmvl_mon_avg.fleet_type         = last_12mon_ucl.fleet_type AND
      rvw_rmvl_mon_avg.config_slot        = last_12mon_ucl.config_slot AND
      rvw_rmvl_mon_avg.part_number        = last_12mon_ucl.part_number AND
      TO_NUMBER(SUBSTR(rvw_rmvl_mon_avg.year_month,1,4)-1) = last_12mon_ucl.year_code;