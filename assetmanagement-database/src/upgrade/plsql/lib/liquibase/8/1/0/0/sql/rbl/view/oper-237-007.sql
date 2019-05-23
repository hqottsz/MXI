--liquibase formatted sql


--changeSet oper-237-007:1 stripComments:false
--
--
-- aircraft delays
--
--     by year, month, tail number, serial number, operator, delay category, departure airport, chapter
--
CREATE OR REPLACE FORCE VIEW aopr_delay_v1
AS 
SELECT
   monthly_delay.year_code,
   monthly_delay.month_code,
   monthly_delay.operator_registration_code,
   monthly_delay.serial_number,
   monthly_delay.operator_code,
   monthly_delay.delay_category_code,
   monthly_delay.departure_airport_code,
   monthly_delay.chapter_code,
   delay_category.delay_category_name,
   monthly_delay.delayed_departures,
   monthly_delay.delay_time
FROM
   opr_monthly_delay monthly_delay
   INNER JOIN opr_delay_category delay_category ON
      monthly_delay.delay_category_code = delay_category.delay_category_code;