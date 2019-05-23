--liquibase formatted sql


--changeSet 50.0.aopr_rbl_comp_rmvl_fleet_ucl_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_COMP_RMVL_FLT_UCL_MV1');
END;
/

--changeSet 50.0.aopr_rbl_comp_rmvl_fleet_ucl_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_comp_rmvl_flt_ucl_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
WITH
rvw_rmvl_yearly
AS
   ( -- last year 12 months
     -- the count will start from the month of September counting backward.
     -- i.e. 2013 Sept (start) to 2012 Sept (end) which 12 months
      SELECT
         operator_id,
         operator_code,
         fleet_type,
         part_number,
         config_slot,
         TO_NUMBER(SUBSTR(year_month,1,4)) year_code,
         --(quantity_per_acft * number_of_acft) QPA,
         quantity_per_acft QPA,
         SUM(removal_count) removal_count,
         SUM(sched_removal) sched_removal,
         SUM(unsched_removal) unsched_removal,
         SUM(justified_failure) justified_failure
      FROM
         (
            SELECT
               operator_id,
               operator_code,
               fleet_type,
               part_number,
               config_slot,
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
               operator_code,
               fleet_type,
               part_number,
               config_slot,
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
      WHERE  -- from September of last 2 year to September of previous year
         year_month BETWEEN TO_NUMBER(SUBSTR(year_month,1,4)-1 || '09') AND TO_NUMBER(SUBSTR(year_month,1,4) || '09')
         AND -- previous year
         TO_NUMBER(SUBSTR(year_month,1,4)) < TO_NUMBER(TO_CHAR(SYSDATE,'YYYY'))
      GROUP BY
          operator_id,
          operator_code,
          fleet_type,
          part_number,
          config_slot,
          TO_NUMBER(SUBSTR(year_month,1,4)),
          quantity_per_acft
   ),
rvw_usage_yearly
AS
   ( -- get the yearly usage from monthly usage table (aopr_monthly_operator_usage_v1)
      SELECT
         operator_id,
         operator_code,
         fleet_type,
         year_code,
         SUM(flight_hours) flight_hours,
         SUM(cycles) cycles
      FROM
         aopr_monthly_operator_usage_v1
      WHERE -- from September of last 2 year to September of previous year
         TO_NUMBER(year_code || month_code) BETWEEN TO_NUMBER(year_code-1 || '09') AND TO_NUMBER(year_code || '09')
         AND -- previous year
         TO_NUMBER(year_code) < TO_NUMBER(TO_CHAR(SYSDATE,'YYYY'))
      GROUP BY
         operator_id,
         operator_code,
         fleet_type,
         year_code
   ),
rvw_urr_yearly
AS
   ( -- calculate the yearly URR
      SELECT
         rvw_rmvl_yearly.operator_id,
         rvw_rmvl_yearly.operator_code,
         rvw_rmvl_yearly.fleet_type,
         rvw_rmvl_yearly.part_number,
         rvw_rmvl_yearly.config_slot,
         rvw_rmvl_yearly.year_code,
         flight_hours,
         cycles,
         CASE
            WHEN NVL((QPA * flight_hours),0) > 0 THEN
               1000*unsched_removal/((QPA * flight_hours))
            ELSE 0
         END AS urr_hours,
         -- unschedule removal rate - cycles
         CASE
            WHEN NVL((QPA * cycles),0) > 0 THEN
               1000*unsched_removal/((QPA * cycles))
            ELSE 0
         END AS urr_cycles
      FROM
         rvw_rmvl_yearly
         LEFT JOIN rvw_usage_yearly ON
            rvw_rmvl_yearly.operator_id = rvw_usage_yearly.operator_id AND
            rvw_rmvl_yearly.fleet_type  = rvw_usage_yearly.fleet_type  AND
            rvw_rmvl_yearly.year_code   = rvw_usage_yearly.year_code
  )
SELECT
   operator_id,
   operator_code,
   fleet_type,
   config_slot,
   part_number,
   year_code,
   flight_hours,
   cycles,
   -- add the deviation to yearly URR
   urr_hours + (STDDEV_POP(urr_hours)) * MAX(NVL(ucl_dev,2))  AS ucl_hours,
   urr_cycles + (STDDEV_POP(urr_cycles)) * MAX(NVL(ucl_dev,2))  AS ucl_cycles
FROM
   rvw_urr_yearly
   LEFT JOIN ( -- configurable UCL deviation
               SELECT
                  parm_value ucl_dev
               FROM
                  utl_config_parm
               WHERE
                  parm_name = 'OPR_RBL_COMP_RMVL_UCL_DEV' AND
                  parm_type = 'RELIABILITY'
            ) ucl_dev_config ON 1 = 1
GROUP BY
   operator_id,
   operator_code,
   fleet_type,
   config_slot,
   part_number,
   year_code,
   flight_hours,
   cycles,
   urr_hours,
   urr_cycles;   