--liquibase formatted sql


--changeSet 03-opr_rbl_monthly_incident_mv:1 stripComments:false
CREATE MATERIALIZED VIEW OPR_RBL_MONTHLY_INCIDENT_MV
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
  SUM(diverted_departures)          AS diverted_departures,
  SUM(aircraft_on_ground)           AS aircraft_on_ground,
  SUM(air_turnbacks)                AS air_turnbacks,
  SUM(aborted_approaches)           AS aborted_approaches,
  SUM(aborted_departures)           AS aborted_departures,
  SUM(ground_turn_backs)            AS ground_turn_backs,
  SUM(emergency_descents)           AS emergency_descents,
  SUM(inflight_shutdowns)           AS inflight_shutdowns,
  SUM(general_air_interruptions)    AS general_air_interruptions,
  SUM(general_ground_interruptions) AS general_ground_interruptions
FROM
(
   SELECT
      year_code,
      month_code,
      fleet_type,
      operator_registration_code,
      serial_number,
      operator_code,
      NVL(diverted_departures,0)          AS diverted_departures,
      NVL(aircraft_on_ground,0)           AS aircraft_on_ground,
      NVL(air_turnbacks,0)                AS air_turnbacks,
      NVL(aborted_approaches,0)           AS aborted_approaches,
      NVL(aborted_departures,0)           AS aborted_departures,
      NVL(ground_turn_backs,0)            AS ground_turn_backs,
      NVL(emergency_descents,0)           AS emergency_descents,
      NVL(inflight_shutdowns,0)           AS inflight_shutdowns,
      NVL(general_air_interruptions,0)    AS general_air_interruptions,
      NVL(general_ground_interruptions,0) AS general_ground_interruptions
   FROM
      opr_rbl_monthly_incident incident
      PIVOT (
               SUM(number_of_incidents) FOR incident_code IN
                  (
                      'ABT'    AS aborted_approaches,
                      'ATB'    AS air_turnbacks,
                      'DIV'    AS diverted_departures,
                      'EMD'    AS emergency_descents,
                      'GTB'    AS ground_turn_backs,
                      'IFD'    AS inflight_shutdowns,
                      'RTO'    AS aborted_departures,
                      'TII'    AS technical_incidents,
                      'GAI'    AS general_air_interruptions,
                      'GGI'    AS general_ground_interruptions,
                      'AGI'    AS aircraft_on_ground
                  )
             ) monthly_incident
   UNION ALL
   SELECT
      year,
      month,
      fleet_type,
      operator_registration_code,
      serial_number,
      operator_code,
      diversions,
      aircraft_on_ground,
      air_turnbacks,
      aborted_approaches,
      aborted_takeoffs,
      ground_turnbacks,
      emergency_descents,
      inflight_shutdowns,
      air_interruptions,
      ground_interruptions
   FROM
      opr_rbl_hist_incident
)
GROUP BY
  year_code,
  month_code,
  fleet_type,
  operator_registration_code,
  serial_number,
  operator_code;