--liquibase formatted sql


--changeSet oper-237-004:1 stripComments:false
--
--
-- aircraft delays
--
--     by year, month, fleet type, operator, chapter code, delay category
--
--
CREATE OR REPLACE FORCE VIEW aopr_ata_delay_v1
AS 
SELECT
   delay.year_code                        AS year_code,
   delay.month_code                       AS month_code,
   delay.fleet_type_code                  AS fleet_type_code,
   delay.operator_code                    AS operator_code,
   delay.chapter_code                     AS chapter_code,
   delay.delay_category_code              AS delay_category_code,
   delay.delayed_departures               AS delayed_departures,
   delay.delay_time                       AS delay_time,
   opr_delay_category.delay_category_name AS delay_category_name
FROM
   (
      SELECT
         monthly_delay.year_code                 AS year_code,
         monthly_delay.month_code                AS month_code,
         monthly_delay.fleet_type                AS fleet_type_code,
         monthly_delay.operator_code             AS operator_code,
         monthly_delay.chapter_code              AS chapter_code,
         monthly_delay.delay_category_code       AS delay_category_code,
         COUNT(monthly_delay.delayed_departures) AS delayed_departures,
         SUM(monthly_delay.delay_time)           AS delay_time
      FROM
         opr_monthly_delay monthly_delay
      GROUP BY
         monthly_delay.year_code,
         monthly_delay.month_code,
         monthly_delay.fleet_type,
         monthly_delay.operator_code,
         monthly_delay.chapter_code,
         monthly_delay.delay_category_code
   ) delay
   INNER JOIN opr_delay_category ON
     delay.delay_category_code = opr_delay_category.delay_category_code;