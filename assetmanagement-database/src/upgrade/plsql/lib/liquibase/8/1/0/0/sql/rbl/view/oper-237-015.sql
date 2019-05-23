--liquibase formatted sql


--changeSet oper-237-015:1 stripComments:false
--
--
-- station delay
--
--     by year, month, fleet type, tail number, serial number, operator, fleet type
--
--
CREATE OR REPLACE FORCE VIEW aopr_station_delay_v1
AS 
SELECT 
   delay.year_code,
   delay.month_code,
   delay.fleet_type,
   delay.operator_code,
   delay.departure_airport_code,
   delay.delay_category_code,
   delay.delayed_departures,
   delay.delay_time,
   opr_delay_category.delay_category_name
FROM
   (
      SELECT
         monthly_delay.year_code,                   
         monthly_delay.month_code,       
         monthly_delay.fleet_type,
         monthly_delay.operator_code,
         monthly_delay.delay_category_code,
         COUNT(monthly_delay.delayed_departures)   AS delayed_departures,
         SUM(monthly_delay.delay_time)             AS delay_time,
         monthly_delay.departure_airport_code
      FROM
         opr_monthly_delay monthly_delay
      GROUP BY
         monthly_delay.year_code,
         monthly_delay.month_code,
         monthly_delay.fleet_type,
         monthly_delay.delay_category_code,
         monthly_delay.operator_code,
         monthly_delay.departure_airport_code
  ) delay
  INNER JOIN opr_delay_category ON
     delay.delay_category_code = opr_delay_category.delay_category_code
;