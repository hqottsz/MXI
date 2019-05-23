--liquibase formatted sql


--changeSet 01-opr_rbl_delay_mv:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('OPR_RBL_DELAY_MV');
END;
/

--changeSet 01-opr_rbl_delay_mv:2 stripComments:false
CREATE MATERIALIZED VIEW OPR_RBL_DELAY_MV
REFRESH COMPLETE ON DEMAND
AS
SELECT
   year_code,
   month_code,
   fleet_type,
   serial_number,
   operator_code,
   operator_registration_code,
   chapter_code,
   departure_airport_code,
   delay_category_code,
   SUM(delayed_departures)        AS delayed_departures,
   SUM(delay_time)                AS delay_time,
   SUM(maintenance_delay_time)    AS maintenance_delay_time
FROM
   (
      SELECT
         year_code,
         month_code,
         fleet_type,
         serial_number,
         operator_code,
         operator_registration_code,
         chapter_code,
         departure_airport_code,
         delay_category_code,
         delayed_departures,
         delay_time,
         maintenance_delay_time,
         aircraft_id,
         operator_id
      FROM
         opr_rbl_monthly_delay
      UNION ALL
      SELECT
         year                 AS year_code,
         month                AS month_code,
         fleet_type,
         serial_number,
         operator_code,
         operator_registration_code,
         chapter,
         departure_airport,
         delay_category_code,
         number_of_delays     AS delayed_departures,
         total_delay_time     AS delay_time,
         total_maintenance_delay_time maintnenance_delay_time,
         null                 AS aircraft_id,
         null                 AS operator_id
      FROM
        opr_rbl_hist_delay
   )
GROUP BY
   year_code,
   month_code,
   fleet_type,
   serial_number,
   operator_code,
   operator_registration_code,
   chapter_code,
   departure_airport_code,
   delay_category_code;