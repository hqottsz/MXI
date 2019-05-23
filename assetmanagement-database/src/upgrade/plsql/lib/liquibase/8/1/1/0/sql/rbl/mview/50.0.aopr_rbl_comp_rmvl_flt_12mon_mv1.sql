--liquibase formatted sql


--changeSet 50.0.aopr_rbl_comp_rmvl_flt_12mon_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_COMP_RMVL_12MON_MV1');
END;
/

--changeSet 50.0.aopr_rbl_comp_rmvl_flt_12mon_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_comp_rmvl_12mon_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
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
         TO_NUMBER(TO_CHAR(ADD_MONTHS(to_date(year_month,'YYYYMM'),-11),'YYYYMM')) ym_12mon
      FROM
         rvw_rmvl_mon  
  ),
rvw_rmvl_12mon
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
              rvw_rmvl_mon.year_month BETWEEN rvw_rmvl_unq.ym_12mon AND rvw_rmvl_unq.year_month
      GROUP BY
         rvw_rmvl_unq.operator_id,
         rvw_rmvl_unq.fleet_type,
         rvw_rmvl_unq.config_slot,
         rvw_rmvl_unq.part_number,
         rvw_rmvl_unq.year_month,
         rvw_rmvl_unq.QPA    
   ),   
rvw_usage_12mon
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
              TO_NUMBER(monthly_usage.year_code || monthly_usage.month_code) BETWEEN rvw_rmvl_unq.ym_12mon AND rvw_rmvl_unq.year_month
      GROUP BY
         rvw_rmvl_unq.operator_id,
         rvw_rmvl_unq.fleet_type,
         rvw_rmvl_unq.config_slot,
         rvw_rmvl_unq.part_number,
         rvw_rmvl_unq.year_month
   ),   
rvw_rmvl_12mon_avg 
AS 
   (
      SELECT
         rvw_rmvl_12mon.operator_id,
         rvw_rmvl_12mon.fleet_type,
         rvw_rmvl_12mon.config_slot,   
         rvw_rmvl_12mon.part_number,
         rvw_rmvl_12mon.year_month,
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
                  WHEN NVL(((QPA * cycles)/justified_failure),0) > 0 THEN
                     (1/((QPA * cycles)/justified_failure))*1000
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
        rvw_rmvl_12mon
        INNER JOIN rvw_usage_12mon ON 
           rvw_rmvl_12mon.operator_id  = rvw_usage_12mon.operator_id AND
           rvw_rmvl_12mon.fleet_type   = rvw_usage_12mon.fleet_type AND
           rvw_rmvl_12mon.config_slot  = rvw_usage_12mon.config_slot AND
           rvw_rmvl_12mon.part_number  = rvw_usage_12mon.part_number AND     
           rvw_rmvl_12mon.year_month   = rvw_usage_12mon.year_month        
   )
SELECT
   operator_id,
   fleet_type,
   config_slot,   
   part_number,
   year_month,
   -- 12mon - between 1st and 3rd month
   NVL(removal_count,0)      AS removal_12mon,
   NVL(unsched_removal,0)    AS unsched_removal_12mon,
   NVL(justified_failure,0)  AS justified_failure_12mon,
   NVL(cycles,0)             AS total_cycle_12mon,
   NVL(flight_hours,0)       AS total_hour_12mon,
   NVL(mtbr,0)         AS mtbr_12mon,
   NVL(mcbr,0)         AS mcbr_12mon,
   NVL(mtbur,0)        AS mtbur_12mon,
   NVL(mcbur,0)        AS mcbur_12mon,
   NVL(mtbf,0)         AS mtbf_12mon,
   NVL(mcbf,0)         AS mcbf_12mon,
   NVL(failure_rate,0) AS f_rate_12mon,   
   NVL(urr_hours,0)    AS urr_hour_12mon,
   NVL(urr_cycles,0)   AS urr_cycle_12mon
FROM 
   rvw_rmvl_12mon_avg;