--liquibase formatted sql


--changeSet 14-aopr_dispatch_reliability_v1:1 stripComments:false
CREATE MATERIALIZED VIEW AOPR_DISPATCH_RELIABILITY_V1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
WITH rvw_dispatch_metric AS
(
  SELECT
     year_code,
     month_code,
     fleet_type,
     operator_code,
     column_name,
     column_value
  FROM
    (
       SELECT
          year_code,
          month_code,
          fleet_type,
          operator_code,
          SUM(flight_hours)             AS flight_hours,
          SUM(cycles)                   AS cycles,
          SUM(completed_departures)     AS completed_departures,
          SUM(delayed_departures)       AS delayed_departures,
          SUM(delayed_departures_gt_15) AS delayed_departures_gt_15,
          SUM(delay_time)               AS delay_time,
          SUM(cancelled_departures)     AS cancelled_departures,
          SUM(diverted_departures)      AS diverted_departures,
          SUM(air_turnbacks)            AS air_turnbacks,
          SUM(aborted_departures)       AS aborted_departures,
          SUM(ground_turn_backs)        AS ground_turn_backs,
          SUM(emergency_descents)       AS emergency_descents,
          SUM(inflight_shutdowns)       AS inflight_shutdowns,
          SUM(aog_delayed_departures)   AS aog_delayed_departures
       FROM
         aopr_reliability_v1
       GROUP BY
          year_code,
          month_code,
          fleet_type,
          operator_code
    )
    UNPIVOT
    (
          --
          -- the numeric prefix is used to order the column value. This value is then placed in columnn_order
          -- where a sort can be applied if needed.
          --
       column_value FOR column_name IN
       (
          flight_hours             AS '01:Total Hours Flown',
          cycles                   AS '02:Total Cycles Flown',
          completed_departures     AS '03:Total Completed Flights (incl delayed flights)',
          delayed_departures       AS '04:Total Number of technical delays',
          delayed_departures_gt_15 AS '05:Total Number of technical delays > 15 Minutes',
          delay_time               AS '06:Delays Duration (Total Minutes)',
          cancelled_departures     AS '07:Number of Cancellations',
          diverted_departures      AS '08:Number of Diversions',
          air_turnbacks            AS '09:Number of ATB',
          aborted_departures       AS '10:Number of ABT',
          ground_turn_backs        AS '11:Number of RTG',
          aborted_departures       AS '12:Number of ABA',
          emergency_descents       AS '13:Number of EMD',
          inflight_shutdowns       AS '14:Number of IFSD',
          aog_delayed_departures   AS '15:Number of AOG'
          --tdrl                     AS '9:Technical Dispatch Reliability (TDRL)'
       )
    )
)
SELECT
    rvw_dispatch_metric.year_code,
    rvw_dispatch_metric.month_code,
    rvw_dispatch_metric.year_code || '-' ||  rvw_dispatch_metric.month_code AS year_month,
    rvw_dispatch_metric.fleet_type,
    rvw_dispatch_metric.operator_code,
    start_date,
    trunc(end_date,'DD')    AS end_date,
    SUBSTR(column_name,4)   AS column_name,
    to_number(SUBSTR(column_name,1,2)) AS column_order,
    column_value as m1,
    LAG(column_value, 1,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m2,
    LAG(column_value, 2,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m3,
    LAG(column_value, 3,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m4,
    LAG(column_value, 4,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m5,
    LAG(column_value, 5,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m6,
    LAG(column_value, 6,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m7,
    LAG(column_value, 7,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m8,
    LAG(column_value, 8,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m9,
    LAG(column_value, 9,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m10,
    LAG(column_value,10,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m11,
    LAG(column_value,11,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m12,
    LAG(column_value,12,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m13,
    LAG(column_value,13,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m14,
    LAG(column_value,14,0) OVER (PARTITION BY fleet_type, operator_code, column_name ORDER BY rvw_dispatch_metric.year_code, rvw_dispatch_metric.month_code) as m15
FROM
   rvw_dispatch_metric
   INNER JOIN
      opr_calendar_month calendar_month ON
         calendar_month.year_code  = rvw_dispatch_metric.year_code AND
         calendar_month.month_code = rvw_dispatch_metric.month_code
UNION ALL
SELECT
   tdrl.year_code,
   tdrl.month_code,
   tdrl.year_month,
   fleet_type,
   operator_code,
   start_date,
   end_date,
   SUBSTR(column_name,4)   AS column_name,
   TO_NUMBER(SUBSTR(column_name,1,2)) AS column_order,
   column_value as m1,
   LAG(column_value, 1,0) over (PARTITION BY fleet_type, operator_code, column_name ORDER BY tdrl.year_code, tdrl.month_code) AS m2,
   LAG(column_value, 2,0) over (PARTITION BY fleet_type, operator_code, column_name ORDER BY tdrl.year_code, tdrl.month_code) AS m3,
   LAG(column_value, 3,0) over (PARTITION BY fleet_type, operator_code, column_name ORDER BY tdrl.year_code, tdrl.month_code) AS m4,
   LAG(column_value, 4,0) over (PARTITION BY fleet_type, operator_code, column_name ORDER BY tdrl.year_code, tdrl.month_code) AS m5,
   LAG(column_value, 5,0) over (PARTITION BY fleet_type, operator_code, column_name ORDER BY tdrl.year_code, tdrl.month_code) AS m6,
   LAG(column_value, 6,0) over (PARTITION BY fleet_type, operator_code, column_name ORDER BY tdrl.year_code, tdrl.month_code) AS m7,
   LAG(column_value, 7,0) over (PARTITION BY fleet_type, operator_code, column_name ORDER BY tdrl.year_code, tdrl.month_code) AS m8,
   LAG(column_value, 8,0) over (PARTITION BY fleet_type, operator_code, column_name ORDER BY tdrl.year_code, tdrl.month_code) AS m9,
   LAG(column_value, 9,0) over (PARTITION BY fleet_type, operator_code, column_name ORDER BY tdrl.year_code, tdrl.month_code) AS m10,
   LAG(column_value,10,0) over (PARTITION BY fleet_type, operator_code, column_name ORDER BY tdrl.year_code, tdrl.month_code) AS m11,
   LAG(column_value,11,0) over (PARTITION BY fleet_type, operator_code, column_name ORDER BY tdrl.year_code, tdrl.month_code) AS m12,
   LAG(column_value,12,0) over (PARTITION BY fleet_type, operator_code, column_name ORDER BY tdrl.year_code, tdrl.month_code) AS m13,
   LAG(column_value,13,0) over (PARTITION BY fleet_type, operator_code, column_name ORDER BY tdrl.year_code, tdrl.month_code) AS m14,
   LAG(column_value,14,0) over (PARTITION BY fleet_type, operator_code, column_name ORDER BY tdrl.year_code, tdrl.month_code) AS m15
FROM
   aopr_tdrl_v1 tdrl;