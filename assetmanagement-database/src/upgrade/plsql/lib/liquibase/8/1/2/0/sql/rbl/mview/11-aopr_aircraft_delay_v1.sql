--liquibase formatted sql


--changeSet 11-aopr_aircraft_delay_v1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_AIRCRAFT_DELAY_V1');
END;
/

--changeSet 11-aopr_aircraft_delay_v1:2 stripComments:false
CREATE MATERIALIZED VIEW AOPR_AIRCRAFT_DELAY_V1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
   delay.year_code,
   delay.month_code,
   min(delay.year_month) as year_month,
   min(delay.start_date) as start_date,
   min(delay.end_date) as end_date,
   delay.operator_registration_code,
   delay.serial_number,
   delay.operator_code,
   delay.fleet_type,
   delay.delay_category_code,
   sum(delay.delayed_departures) as delayed_departures,
   FLOOR((sum(delay.delay_time))/60) || ':' || LPAD(MOD((sum(delay.delay_time)),60),2,'0') AS delay_time,
   sum(delay.delay_time) as delay_time_min,
   FLOOR((sum(delay.maintenance_delay_time))/60) || ':' || LPAD(MOD((sum(delay.maintenance_delay_time)),60),2,'0') AS maintenance_delay_time,
   sum(delay.maintenance_delay_time) as maintenance_delay_time_min,
   min(delay.delay_category_name) as delay_category_name
FROM
   opr_rbl_delay_mv delay
GROUP BY
   delay.year_code,
   delay.month_code,
   delay.operator_registration_code,
   delay.serial_number,
   delay.operator_code,
   delay.fleet_type,
   delay.delay_category_code;   