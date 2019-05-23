--liquibase formatted sql


--changeSet 01-opr_rbl_delay_mv:1 stripComments:false
CREATE MATERIALIZED VIEW OPR_RBL_DELAY_MV
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
   delay.year_code,
   delay.month_code,
   delay.year_code || '-' || delay.month_code AS year_month,
   calendar_month.start_date,
   trunc(calendar_month.end_date, 'DD')       AS end_date,
   delay.fleet_type,
   delay.serial_number,
   delay.operator_code,
   delay.operator_registration_code,
   delay.chapter_code,
   delay.departure_airport_code,
   delay.delay_category_code,
   delay_category.delay_category_name         AS delay_category_name,
   delay_category.display_order               AS display_order,
   delay.delayed_departures,
   delay.delay_time,
   delay.maintenance_delay_time
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
         delay_category_code
) delay
INNER JOIN opr_rbl_delay_category delay_category ON
   delay.delay_category_code = delay_category.delay_category_code
INNER JOIN
   opr_calendar_month calendar_month ON
      calendar_month.year_code  = delay.year_code AND
      calendar_month.month_code = delay.month_code;