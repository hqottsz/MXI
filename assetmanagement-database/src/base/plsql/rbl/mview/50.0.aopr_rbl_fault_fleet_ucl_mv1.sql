--liquibase formatted sql


--changeSet 50.0.aopr_rbl_fault_fleet_ucl_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_FAULT_FLEET_UCL_MV1');
END;
/

--changeSet 50.0.aopr_rbl_fault_fleet_ucl_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_fault_fleet_ucl_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
WITH
   rvw_fault_yearly
   AS
   ( -- last year 12 months
     -- the count will start from the month of September counting backward.
     -- i.e. 2013 Sept (start) to 2012 Sept (end) which 12 months
     SELECT
        operator_id,
        operator_code,
        fleet_type,
        config_slot_code,
        SUBSTR(year_month,1,4) year_code,
        SUM(fault_cnt) fault_cnt
     FROM
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
              fault_cnt
           FROM
              opr_rbl_fault_monthly
           -- the above are maintenix data
           UNION ALL
           -- to legacy data
           SELECT
              operator_id,
              NULL aircraft_id,
              operator_code,
              fleet_type,
              acft_serial_number,
              config_slot_code,
              fault_source_code,
              year_month,
              fault_cnt
           FROM
              opr_rbl_hist_fault_monthly
        )
     WHERE
        year_month BETWEEN TO_NUMBER(SUBSTR(year_month,1,4)-1 || '07') and TO_NUMBER(SUBSTR(year_month,1,4) || '09')
        AND
        SUBSTR(year_month,1,4) < TO_NUMBER(TO_CHAR(SYSDATE,'YYYY'))
     GROUP BY
        operator_id,
        operator_code,
        fleet_type,
        config_slot_code,
        SUBSTR(year_month,1,4)
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
rvw_rate_yearly
AS
   ( -- calculate the yearly URR
      SELECT
         rvw_fault_yearly.operator_id,
         rvw_fault_yearly.operator_code,
         rvw_fault_yearly.fleet_type,
         rvw_fault_yearly.year_code,
         rvw_fault_yearly.config_slot_code,
         flight_hours,
         cycles,
         CASE
            WHEN NVL(flight_hours,0) > 0 THEN
               (fault_cnt/flight_hours)*1000
            ELSE 0
         END AS rate_hours,
         -- unschedule removal rate - cycles
         CASE
            WHEN NVL(cycles,0) > 0 THEN
               (fault_cnt/cycles)*100
            ELSE 0
         END AS rate_cycles
      FROM
         rvw_fault_yearly
         LEFT JOIN rvw_usage_yearly ON
            rvw_fault_yearly.operator_id = rvw_usage_yearly.operator_id AND
            rvw_fault_yearly.fleet_type  = rvw_usage_yearly.fleet_type  AND
            rvw_fault_yearly.year_code   = rvw_usage_yearly.year_code
  )
SELECT
   operator_id,
   operator_code,
   fleet_type,
   config_slot_code,
   year_code,
   rate_hours + (STDDEV(rate_hours) * MAX(NVL(ucl_dev,2))) ucl_fh,
   rate_cycles + (STDDEV(rate_cycles) * MAX(NVL(ucl_dev,2))) ucl_fc
FROM
   rvw_rate_yearly
   LEFT JOIN ( -- configurable UCL deviation
               SELECT
                  parm_value ucl_dev
               FROM
                  utl_config_parm
               WHERE
                  parm_name = 'OPR_RBL_FAULT_UCL_DEV' AND
                  parm_type = 'RELIABILITY'
            ) ucl_dev_config ON 1 = 1
GROUP BY
   operator_id,
   operator_code,
   fleet_type,
   config_slot_code,
   year_code,
   rate_hours,
   rate_cycles;   