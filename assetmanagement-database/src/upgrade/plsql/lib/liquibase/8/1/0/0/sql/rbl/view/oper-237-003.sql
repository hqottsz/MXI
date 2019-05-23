--liquibase formatted sql


--changeSet oper-237-003:1 stripComments:false
--
--
-- aircraft delays
--
--     by year, month, tail number, serial number, operator, delay category
--
--
CREATE OR REPLACE FORCE VIEW aopr_aircraft_delay_v1
AS 
SELECT
   delay.year_code                               AS year_code,
   delay.month_code                              AS month_code,
   delay.operator_registration_code              AS operator_registration_code,
   delay.serial_number                           AS serial_number,
   delay.operator_code                           AS operator_code,
   delay.fleet_type                              AS fleet_type,
   delay.delay_category_code                     AS delay_category_code,
   delay.delayed_departures                      AS delayed_departures,
   delay.delay_time                              AS delay_time,
   opr_delay_category.delay_category_name        AS delay_category_name
FROM
   (
      SELECT
         monthly_delay.year_code                  AS year_code,
         monthly_delay.month_code                 AS month_code,
         monthly_delay.operator_registration_code AS operator_registration_code,
         monthly_delay.serial_number              AS serial_number,
         monthly_delay.operator_code              AS operator_code,
         monthly_delay.fleet_type                 AS fleet_type,
         monthly_delay.delay_category_code        AS delay_category_code,
         COUNT(monthly_delay.delayed_departures)  AS delayed_departures,
         SUM(monthly_delay.delay_time)            AS delay_time
      FROM
         opr_monthly_delay monthly_delay
      GROUP BY
         monthly_delay.year_code,
         monthly_delay.month_code,
         monthly_delay.operator_registration_code,
         monthly_delay.serial_number,
         monthly_delay.operator_code,
         monthly_delay.fleet_type,
         monthly_delay.delay_category_code
   ) delay
   INNER JOIN opr_delay_category ON
      delay.delay_category_code = opr_delay_category.delay_category_code;