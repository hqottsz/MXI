--liquibase formatted sql


--changeSet 10-aopr_technical_delay_v1:1 stripComments:false
CREATE MATERIALIZED VIEW AOPR_TECHNICAL_DELAY_V1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
   year_code,
   month_code,
   year_month,
   start_date,
   end_date,
   fleet_type,
   operator_code,
   delay_category_code,
   delay_category_name,
   display_order,
   delayed_departures                                                                                                             AS m1,
   LAG(delayed_departures, 1,0) OVER (PARTITION BY fleet_type, operator_code, delay_category_code ORDER BY year_code, month_code) AS m2,
   LAG(delayed_departures, 2,0) OVER (PARTITION BY fleet_type, operator_code, delay_category_code ORDER BY year_code, month_code) AS m3,
   LAG(delayed_departures, 3,0) OVER (PARTITION BY fleet_type, operator_code, delay_category_code ORDER BY year_code, month_code) AS m4,
   LAG(delayed_departures, 4,0) OVER (PARTITION BY fleet_type, operator_code, delay_category_code ORDER BY year_code, month_code) AS m5,
   LAG(delayed_departures, 5,0) OVER (PARTITION BY fleet_type, operator_code, delay_category_code ORDER BY year_code, month_code) AS m6,
   LAG(delayed_departures, 6,0) OVER (PARTITION BY fleet_type, operator_code, delay_category_code ORDER BY year_code, month_code) AS m7,
   LAG(delayed_departures, 7,0) OVER (PARTITION BY fleet_type, operator_code, delay_category_code ORDER BY year_code, month_code) AS m8,
   LAG(delayed_departures, 8,0) OVER (PARTITION BY fleet_type, operator_code, delay_category_code ORDER BY year_code, month_code) AS m9,
   LAG(delayed_departures, 9,0) OVER (PARTITION BY fleet_type, operator_code, delay_category_code ORDER BY year_code, month_code) AS m10,
   LAG(delayed_departures,10,0) OVER (PARTITION BY fleet_type, operator_code, delay_category_code ORDER BY year_code, month_code) AS m11,
   LAG(delayed_departures,11,0) OVER (PARTITION BY fleet_type, operator_code, delay_category_code ORDER BY year_code, month_code) AS m12,
   LAG(delayed_departures,12,0) OVER (PARTITION BY fleet_type, operator_code, delay_category_code ORDER BY year_code, month_code) AS m13,
   LAG(delayed_departures,13,0) OVER (PARTITION BY fleet_type, operator_code, delay_category_code ORDER BY year_code, month_code) AS m14,
   LAG(delayed_departures,14,0) OVER (PARTITION BY fleet_type, operator_code, delay_category_code ORDER BY year_code, month_code) AS m15
FROM
(
  SELECT
    year_code,
    month_code,
    fleet_type,
    delay_category_code,
    operator_code,
    SUM(delayed_departures) as     delayed_departures,
    min(display_order)      AS display_order,
    min(delay_category_name) as delay_category_name,
    min(start_date)          as start_date,
    min(end_date)             as end_date,
    min(year_month)           as year_month
  FROM
    opr_rbl_delay_mv
  GROUP BY
    year_code,
    month_code,
    fleet_type,
    operator_code,
    delay_category_code
) delay;