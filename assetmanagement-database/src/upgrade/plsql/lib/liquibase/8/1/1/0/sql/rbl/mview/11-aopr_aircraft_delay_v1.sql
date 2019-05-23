--liquibase formatted sql


--changeSet 11-aopr_aircraft_delay_v1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_AIRCRAFT_DELAY_V1');
END;
/

--changeSet 11-aopr_aircraft_delay_v1:2 stripComments:false
CREATE MATERIALIZED VIEW AOPR_AIRCRAFT_DELAY_V1
REFRESH COMPLETE ON DEMAND
AS
SELECT
   delay.year_code                               AS year_code,
   delay.month_code                              AS month_code,
   delay.year_code || '-' || delay.month_code    AS year_month,
   start_date                                    AS start_date,
   trunc(end_date,'DD')                          AS end_date,
   delay.operator_registration_code              AS operator_registration_code,
   delay.serial_number                           AS serial_number,
   delay.operator_code                           AS operator_code,
   delay.fleet_type                              AS fleet_type,
   delay.delay_category_code                     AS delay_category_code,
   delay.delayed_departures                      AS delayed_departures,
   delay.delay_time                              AS delay_time,
   delay.maintenance_delay_time                  AS maintenance_delay_time,
   delay_category.delay_category_name            AS delay_category_name
FROM
   (
      SELECT
         rbl_delay.year_code                   AS year_code,
         rbl_delay.month_code                  AS month_code,
         rbl_delay.operator_registration_code  AS operator_registration_code,
         rbl_delay.serial_number               AS serial_number,
         rbl_delay.operator_code               AS operator_code,
         rbl_delay.fleet_type                  AS fleet_type,
         rbl_delay.delay_category_code         AS delay_category_code,
         COUNT(rbl_delay.delayed_departures)   AS delayed_departures,
         SUM(rbl_delay.delay_time)             AS delay_time,
         SUM(rbl_delay.maintenance_delay_time) AS maintenance_delay_time
      FROM
         opr_rbl_delay_mv rbl_delay
      GROUP BY
         rbl_delay.year_code,
         rbl_delay.month_code,
         rbl_delay.operator_registration_code,
         rbl_delay.serial_number,
         rbl_delay.operator_code,
         rbl_delay.fleet_type,
         rbl_delay.delay_category_code
   ) delay
   INNER JOIN opr_rbl_delay_category delay_category ON
      delay.delay_category_code = delay_category.delay_category_code
   INNER JOIN
     opr_calendar_month calendar_month ON
       calendar_month.year_code  = delay.year_code AND
       calendar_month.month_code = delay.month_code;