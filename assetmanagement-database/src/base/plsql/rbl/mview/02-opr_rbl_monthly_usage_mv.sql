--liquibase formatted sql


--changeSet 02-opr_rbl_monthly_usage_mv:1 stripComments:false
CREATE MATERIALIZED VIEW OPR_RBL_MONTHLY_USAGE_MV
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
   year_code,
   month_code,
   fleet_type,
   operator_registration_code,
   serial_number,
   operator_code,
   MIN(aircraft_id)            AS aircraft_id,
   MIN(operator_id)            AS operator_id,
   MIN(work_package_list)      AS work_package_list,
   SUM(flight_hours)           AS flight_hours,
   SUM(cycles)                 AS cycles,
   SUM(revenue_flight_hours)   AS revenue_flight_hours,
   SUM(revenue_cycles)         AS revenue_cycles,
   SUM(cancelled_departures)   AS cancelled_departures,
   SUM(completed_departures)   AS completed_departures,
   SUM(days_out_of_service)    AS days_out_of_service,
   SUM(mel_delayed_departures) AS mel_delayed_departures
FROM
(
  SELECT
     year_code,
     month_code,
     fleet_type,
     operator_registration_code,
     serial_number,
     operator_code,
     aircraft_id,
     operator_id,
     flight_hours,
     cycles,
     revenue_flight_hours,
     revenue_cycles,
     cancelled_departures,
     completed_departures,
     days_out_of_service,
     mel_delayed_departures,
     work_package_list
  FROM
     opr_rbl_monthly_usage
  UNION ALL
  SELECT
     year,
     month,
     fleet_type,
     operator_registration_code,
     serial_number,
     operator_code,
     NULL AS aircraft_id,
     NULL AS operator_id,
     flight_hours,
     cycles,
     revenue_flight_hours,
     revenue_cycles,
     cancelled_flights,
     completed_flights,
     days_out_of_service,
     0    AS mel_delayed_departures,
     NULL AS work_package_list
  FROM
     opr_rbl_hist_usage
)
GROUP BY
   year_code,
   month_code,
   fleet_type,
   operator_registration_code,
   serial_number,
   operator_code;