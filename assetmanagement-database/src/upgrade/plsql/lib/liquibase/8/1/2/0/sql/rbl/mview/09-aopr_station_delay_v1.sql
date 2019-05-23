--liquibase formatted sql


--changeSet 09-aopr_station_delay_v1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_STATION_DELAY_V1');
END;
/

--changeSet 09-aopr_station_delay_v1:2 stripComments:false
CREATE MATERIALIZED VIEW AOPR_STATION_DELAY_V1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
   delay.year_code,
   delay.month_code,
   MIN(delay.year_month)           AS year_month,
   MIN(delay.start_date)           AS start_date,
   MIN(delay.end_date)             AS end_date,
   delay.fleet_type,
   delay.operator_code,
   delay.departure_airport_code,
   delay.delay_category_code,
   MIN(delay. delay_category_name) AS delay_category_name,
   SUM(delay.delayed_departures)   AS delayed_departures,
   FLOOR((sum(delay.delay_time))/60) || ':' || 
   LPAD(MOD((sum(delay.delay_time)),60),2,'0') 
                                   AS delay_time,
   SUM(delay.delay_time)           AS delay_time_min
FROM
   opr_rbl_delay_mv delay
GROUP BY
   delay.year_code,
   delay.month_code,
   delay.fleet_type,
   delay.delay_category_code,
   delay.operator_code,
   delay.departure_airport_code;