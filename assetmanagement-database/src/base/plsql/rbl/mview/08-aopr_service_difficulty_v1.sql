--liquibase formatted sql


--changeSet 08-aopr_service_difficulty_v1:1 stripComments:false
CREATE MATERIALIZED VIEW aopr_service_difficulty_v1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
WITH rvw_dispatch AS
(
  SELECT
     year_code,
     month_code,
     year_month,
     end_date,
     start_date,
     operator_code,
     fleet_type,
     column_name,
     column_value
  FROM
     (
       SELECT
          year_code,
          month_code,
          operator_code,
          fleet_type,
          year_month,
          start_date,
          end_date,
          cycles,
          --
          diverted_departures,
          --
          -- diverted departure rate
          CASE WHEN cycles = 0
          THEN
             0
          ELSE
             ROUND(diverted_departures /cycles * 1000,2)
          END AS DVRT,
          --
          air_turnbacks,
          -- air turnback rate
          CASE WHEN cycles = 0
          THEN
             0
          ELSE
             ROUND(air_turnbacks /cycles * 1000,2)
          END AS ATBR,
          --
          ground_turn_backs,
          -- Aborted Approach Rate
          CASE WHEN cycles = 0
            THEN 0
          ELSE
             ROUND(ground_turn_backs / cycles * 1000,2)
          END AS RTGR,
          --
          aborted_approaches,
          -- Aborted Approach Rate
          CASE WHEN cycles = 0
            THEN 0
          ELSE
             ROUND(aborted_approaches / cycles * 1000,2)
          END AS ABAR,
           --
           --
          aborted_takeoffs,
          -- aborted takeoff rate
          CASE WHEN cycles = 0
          THEN
             0
          ELSE
             ROUND(aborted_takeoffs /cycles * 1000,2)
          END AS ABTR,
          --
          emergency_descents,
          -- emergency descent rate,
          CASE WHEN cycles = 0
          THEN
             0
          ELSE
             ROUND(emergency_descents /cycles * 1000,2)
          END AS EMDR,
          --
          inflight_shutdowns,
          -- inflight shutdown rate
          CASE WHEN cycles = 0
          THEN
             0
          ELSE
             ROUND(inflight_shutdowns /cycles * 1000,2)
          END AS IFSDR
       FROM
       (
         SELECT
              year_code,
              month_code,
              operator_code,
              fleet_type,
              MIN (year_month)          AS year_month,
              MIN (start_date)          AS start_date,
              MIN (end_date)            AS end_date,
              SUM (cycles)              AS cycles,
              SUM (inflight_shutdowns)  AS inflight_shutdowns,
              SUM (air_turnbacks)       AS air_turnbacks,
              SUM (aborted_departures)  AS aborted_takeoffs,
              SUM (aborted_approaches)  AS aborted_approaches,
              SUM (emergency_descents)  AS emergency_descents,
              SUM (ground_turn_backs)   AS ground_turn_backs,
              SUM (diverted_departures) AS diverted_departures
           FROM
             opr_rbl_monthly_dispatch_mv
           GROUP BY
              year_code,
              month_code,
              operator_code,
              fleet_type
        )
     )
  UNPIVOT
  (
      column_value FOR column_name IN
      (
         --
         -- the numeric prefix is used to order the column value. This value is then placed in column_order
         -- where a sort can be applied if needed.
         --
         dvrt  AS '1:Diversion Rate (DVRT)',
         atbr  AS '2:Air Turnback Rate (ATBR)',
         abtr  AS '3:Aborted Takeoff Rate (ABTR)',
         rtgr  AS '4:Return to Gate Rate(RTGR)',
         abar  AS '5:Aborted Approach Rate (ABAR)',
         emdr  AS '6:Emergency Descent Rate (EMDR)',
         ifsdr AS '7:In Flight Shutdown Rate (IFSDR)'
      )
   )
)
SELECT
   year_code,
   month_code,
   year_month,
   start_date,
   end_date,
   fleet_type,
   operator_code,
   SUBSTR(column_name,3)   AS column_name,
   SUBSTR(column_name,1,1) AS column_order,
   column_value            AS m1,
   LAG(column_value, 1,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m2,
   LAG(column_value, 2,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m3,
   LAG(column_value, 3,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m4,
   LAG(column_value, 4,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m5,
   LAG(column_value, 5,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m6,
   LAG(column_value, 6,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m7,
   LAG(column_value, 7,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m8,
   LAG(column_value, 8,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m9,
   LAG(column_value, 9,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m10,
   LAG(column_value,10,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m11,
   LAG(column_value,11,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m12,
   LAG(column_value,12,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m13,
   LAG(column_value,13,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m14,
   LAG(column_value,14,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch.year_code, rvw_dispatch.month_code) AS m15
FROM
   rvw_dispatch;