--liquibase formatted sql


--changeSet 09-aopr_station_delay_v1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_STATION_DELAY_V1');
END;
/

--changeSet 09-aopr_station_delay_v1:2 stripComments:false
--
--
-- station delay
--
--     by year, month, fleet type, tail number, serial number, operator, fleet type
--
--
CREATE MATERIALIZED VIEW AOPR_STATION_DELAY_V1
REFRESH COMPLETE ON DEMAND
AS
SELECT
   delay.year_code,
   delay.month_code,
   delay.year_code || '-' || delay.month_code AS year_month,
   start_date,
   trunc(end_date,'DD')    AS end_date,
   delay.fleet_type,
   delay.operator_code,
   delay.departure_airport_code,
   delay.delay_category_code,
   delay.delayed_departures,
   delay.delay_time,
   opr_rbl_delay_category.delay_category_name
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
         opr_rbl_delay_mv monthly_delay
      GROUP BY
         monthly_delay.year_code,
         monthly_delay.month_code,
         monthly_delay.fleet_type,
         monthly_delay.delay_category_code,
         monthly_delay.operator_code,
         monthly_delay.departure_airport_code
  ) delay
  INNER JOIN opr_rbl_delay_category ON
     delay.delay_category_code = opr_rbl_delay_category.delay_category_code
   INNER JOIN
      opr_calendar_month calendar_month ON
         calendar_month.year_code  = delay.year_code AND
         calendar_month.month_code = delay.month_code;