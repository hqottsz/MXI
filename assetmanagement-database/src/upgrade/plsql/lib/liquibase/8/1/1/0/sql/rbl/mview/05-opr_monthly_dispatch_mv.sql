--liquibase formatted sql


--changeSet 05-opr_monthly_dispatch_mv:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('OPR_RBL_MONTHLY_DISPATCH_MV');
END;
/

--changeSet 05-opr_monthly_dispatch_mv:2 stripComments:false
CREATE MATERIALIZED VIEW OPR_RBL_MONTHLY_DISPATCH_MV
REFRESH COMPLETE ON DEMAND
AS
SELECT
     monthly_usage.year_code                              AS year_code,
     monthly_usage.month_code                             AS month_code,
     monthly_usage.fleet_type                             AS fleet_type,
     monthly_usage.operator_registration_code             AS operator_registration_code,
     monthly_usage.serial_number                          AS serial_number,
     monthly_usage.operator_code                          AS operator_code,
     nvl(monthly_usage.cycles,0)                          AS cycles,
     nvl(monthly_usage.flight_hours,0)                    AS flight_hours,
     nvl(monthly_usage.days_out_of_service,0)             AS days_out_of_service,
     nvl(monthly_usage.completed_departures,0)            AS completed_departures,
     nvl(monthly_delay.delayed_departures,0)              AS delayed_departures,
     nvl(monthly_delay.delayed_departures_gt_15,0)        AS delayed_departures_gt_15,
     nvl(monthly_delay.delay_time,0)                      AS delay_time,
     nvl(monthly_usage.cancelled_departures,0)            AS cancelled_departures,
     nvl(monthly_incident.diverted_departures,0)          AS diverted_departures,
     nvl(monthly_incident.aircraft_on_ground,0)           AS aog_delayed_departures,
     nvl(monthly_usage.mel_delayed_departures,0)          AS mel_delayed_departures,
     nvl(monthly_incident.air_turnbacks,0)                AS air_turnbacks,
     nvl(monthly_incident.aborted_departures,0)           AS aborted_departures,
     nvl(monthly_incident.ground_turn_backs,0)            AS ground_turn_backs,
     nvl(monthly_incident.aborted_approaches,0)           AS aborted_approaches,
     nvl(monthly_incident.emergency_descents,0)           AS emergency_descents,
     nvl(monthly_incident.inflight_shutdowns,0)           AS inflight_shutdowns,
     nvl(monthly_incident.general_air_interruptions,0)    AS general_air_interruptions,
     nvl(monthly_incident.general_ground_interruptions,0) AS general_ground_interruptions
FROM
   opr_rbl_monthly_usage_mv monthly_usage
LEFT JOIN
   opr_rbl_monthly_incident_mv monthly_incident ON
      monthly_incident.year_code                  = monthly_usage.year_code                  AND
      monthly_incident.month_code                 = monthly_usage.month_code                 AND
      monthly_incident.fleet_type                 = monthly_usage.fleet_type                 AND
      monthly_incident.operator_registration_code = monthly_usage.operator_registration_code AND
      monthly_incident.serial_number              = monthly_usage.serial_number              AND
      monthly_incident.operator_code              = monthly_usage.operator_code
LEFT JOIN
   opr_rbl_monthly_delay_mv monthly_delay ON
      monthly_delay.year_code                  = monthly_usage.year_code                  AND
      monthly_delay.month_code                 = monthly_usage.month_code                 AND
      monthly_delay.fleet_type                 = monthly_usage.fleet_type                 AND
      monthly_delay.operator_registration_code = monthly_usage.operator_registration_code AND
      monthly_delay.serial_number              = monthly_usage.serial_number              AND
      monthly_delay.operator_code              = monthly_usage.operator_code;