--liquibase formatted sql


--changeSet 12-aopr_ata_delay_v1:1 stripComments:false
CREATE MATERIALIZED VIEW AOPR_ATA_DELAY_V1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
   delay.year_code                             AS year_code,
   delay.month_code                            AS month_code,
   min(delay.year_month)                       AS year_month,
   min(start_date)                             AS start_date,
   min(end_date)                               AS end_date,
   delay.fleet_type                            AS fleet_type_code,
   delay.operator_code                         AS operator_code,
   delay.chapter_code                          AS chapter_code,
   delay.delay_category_code                   AS delay_category_code,
   sum(delay.delayed_departures)               AS delayed_departures,
   FLOOR((sum(delay.delay_time))/60) || ':' ||
   LPAD(MOD((sum(delay.delay_time)),60),2,'0') AS delay_time,
   sum(delay.delay_time)                       AS delay_time_min,
   min(delay.delay_category_name  )            AS delay_category_name
FROM
   opr_rbl_delay_mv delay
GROUP BY
   delay.year_code,
   delay.month_code,
   delay.fleet_type,
   delay.operator_code,
   delay.chapter_code,
   delay.delay_category_code;