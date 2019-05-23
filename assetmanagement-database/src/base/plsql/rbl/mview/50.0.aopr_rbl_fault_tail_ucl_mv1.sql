--liquibase formatted sql


--changeSet 50.0.aopr_rbl_fault_tail_ucl_mv1:1 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_fault_tail_ucl_mv1
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
        tail_number,
        config_slot_code,
        SUBSTR(year_month,1,4) year_code,
        SUM(flight_hours) flight_hours,
        SUM(cycles) cycles,
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
              flight_hours,
              cycles,
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
              flight_hours,
              cycles,
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
        tail_number,
        config_slot_code,
        SUBSTR(year_month,1,4)
   )
SELECT
   operator_id,
   operator_code,
   fleet_type,
   tail_number,
   config_slot_code,
   year_code,
   -- DECODEs were used to replaced 0 divisor to -1 which will result to a negative values
   AVG((fault_cnt/DECODE(flight_hours,0,-1,flight_hours))*1000) + (STDDEV((fault_cnt/DECODE(flight_hours,0,-1,flight_hours))*1000) * MAX(ucl_dev)) ucl_fh,
   AVG((fault_cnt/DECODE(cycles,0,-1,cycles))*100) + (STDDEV((fault_cnt/DECODE(cycles,0,-1,cycles))*100) * MAX(ucl_dev)) ucl_fc
FROM
   rvw_fault_yearly
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
   tail_number,
   config_slot_code,
   year_code;