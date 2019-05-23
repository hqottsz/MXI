--liquibase formatted sql


--changeSet 12-aopr_ata_delay_v1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_ATA_DELAY_V1');
END;
/

--changeSet 12-aopr_ata_delay_v1:2 stripComments:false
CREATE MATERIALIZED VIEW AOPR_ATA_DELAY_V1
REFRESH COMPLETE ON DEMAND
AS
SELECT
   delay.year_code                            AS year_code,
   delay.month_code                           AS month_code,
   delay.year_code || '-' || delay.month_code AS year_month,
   start_date,
   trunc(end_date,'DD')                       AS end_date,
   delay.fleet_type_code                      AS fleet_type_code,
   delay.operator_code                        AS operator_code,
   delay.chapter_code                         AS chapter_code,
   delay.delay_category_code                  AS delay_category_code,
   delay.delayed_departures                   AS delayed_departures,
   delay.delay_time                           AS delay_time,
   opr_rbl_delay_category.delay_category_name AS delay_category_name
FROM
   (
      SELECT
         rbl_delay.year_code                 AS year_code,
         rbl_delay.month_code                AS month_code,
         rbl_delay.fleet_type                AS fleet_type_code,
         rbl_delay.operator_code             AS operator_code,
         rbl_delay.chapter_code              AS chapter_code,
         rbl_delay.delay_category_code       AS delay_category_code,
         COUNT(rbl_delay.delayed_departures) AS delayed_departures,
         SUM(rbl_delay.delay_time)           AS delay_time
      FROM
         opr_rbl_delay_mv rbl_delay
      GROUP BY
         rbl_delay.year_code,
         rbl_delay.month_code,
         rbl_delay.fleet_type,
         rbl_delay.operator_code,
         rbl_delay.chapter_code,
         rbl_delay.delay_category_code
   ) delay
   INNER JOIN opr_rbl_delay_category ON
     delay.delay_category_code = opr_rbl_delay_category.delay_category_code
   INNER JOIN
     opr_calendar_month calendar_month ON
       calendar_month.year_code  = delay.year_code AND
       calendar_month.month_code = delay.month_code;