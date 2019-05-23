--liquibase formatted sql


--changeSet acor_flights_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_flights_v1
AS
WITH
rvw_inv_loc
AS
   (
     SELECT
        alt_id,
        loc_db_id,
        loc_id,
        loc_cd,
        loc_name
     FROM
        inv_loc
   ),
rvw_usg_usage_record
AS
   (
     SELECT
        usage_record_id,
        usg_usage_data.inv_no_db_id,
        usg_usage_data.inv_no_id,
        data_type_db_id,
        data_type_id,
        tsn_qt,
        tsn_delta_qt
     FROM
        usg_usage_data
        INNER JOIN inv_ac_reg ON
            usg_usage_data.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
            usg_usage_data.inv_no_id    = inv_ac_reg.inv_no_id
   )
SELECT
   fl_leg.leg_id,
   fl_leg.leg_no              AS flight_number,
   dep_loc.alt_id             AS depart_location_id,
   dep_loc.loc_cd             AS depart_airport,
   arr_loc.alt_id             AS arrival_location_id,
   arr_loc.loc_cd             AS arrival_aiport,
   fl_leg.sched_departure_dt  AS schedule_departure_date,
   fl_leg.sched_arrival_dt    AS schedule_arrival_date,
   fl_leg.actual_departure_dt AS actual_departure_date,
   fl_leg.actual_arrival_dt   AS actual_arrival_date,
   fl_leg.off_dt              AS up_date,
   fl_leg.on_dt               AS down_date,
   delta_usg_hr.tsn_delta_qt  AS delta_hourr,
   delta_usg_cyc.tsn_delta_qt AS delta_cycle,
   acft_inv.alt_id            AS aircraft_id,
   inv_ac_reg.ac_reg_cd       AS registration_code,
   acft_inv.serial_no_oem     AS aircraft_serial_number,
   ac_usg_cyc.tsn_qt          AS aircraft_cycle,
   ac_usg_hr.tsn_qt           AS aircraft_hour,
   flight_type_cd             AS flight_type_code,
   flight_reason_cd           AS flight_reason_code,
   flight_leg_status_cd       AS flight_status_code,
   fl_leg.usage_record_id     AS usage_record_id
FROM
   fl_leg
  -- departure location
  INNER JOIN rvw_inv_loc dep_loc ON
     fl_leg.departure_loc_db_id = dep_loc.loc_db_id AND
     fl_leg.departure_loc_id    = dep_loc.loc_id
  -- arrival location
  INNER JOIN rvw_inv_loc arr_loc ON
     fl_leg.arrival_loc_db_id = arr_loc.loc_db_id AND
     fl_leg.arrival_loc_id    = arr_loc.loc_id
  -- aircraft registration
  INNER JOIN inv_ac_reg ON
     fl_leg.aircraft_db_id = inv_ac_reg.inv_no_db_id AND
     fl_leg.aircraft_id    = inv_ac_reg.inv_no_id
  INNER JOIN inv_inv acft_inv ON
     inv_ac_reg.inv_no_db_id = acft_inv.inv_no_db_id AND
     inv_ac_reg.inv_no_id    = acft_inv.inv_no_id
  -- delta usage
  LEFT JOIN rvw_usg_usage_record delta_usg_hr ON
     fl_leg.usage_record_id = delta_usg_hr.usage_record_id
     AND
     fl_leg.aircraft_db_id = delta_usg_hr.inv_no_db_id AND
     fl_leg.aircraft_id    = delta_usg_hr.inv_no_id
     AND -- hour
     delta_usg_hr.data_type_db_id = 0 AND
     delta_usg_hr.data_type_id    = 1
  LEFT JOIN rvw_usg_usage_record delta_usg_cyc ON
     fl_leg.usage_record_id = delta_usg_cyc.usage_record_id
     AND
     fl_leg.aircraft_db_id = delta_usg_cyc.inv_no_db_id AND
     fl_leg.aircraft_id    = delta_usg_cyc.inv_no_id
     AND -- cycle
     delta_usg_cyc.data_type_db_id = 0 AND
     delta_usg_cyc.data_type_id    = 10
  -- aircraft usage
  LEFT JOIN rvw_usg_usage_record ac_usg_hr ON
     fl_leg.usage_record_id = ac_usg_hr.usage_record_id
     AND
     fl_leg.aircraft_db_id = ac_usg_hr.inv_no_db_id AND
     fl_leg.aircraft_id    = ac_usg_hr.inv_no_id
     AND -- hour
     ac_usg_hr.data_type_db_id = 0 AND
     ac_usg_hr.data_type_id    = 1
  LEFT JOIN rvw_usg_usage_record ac_usg_cyc ON
     fl_leg.usage_record_id = ac_usg_cyc.usage_record_id
     AND
     fl_leg.aircraft_db_id = ac_usg_hr.inv_no_db_id AND
     fl_leg.aircraft_id    = ac_usg_hr.inv_no_id
     AND -- cycle
     ac_usg_cyc.data_type_db_id = 0 AND
     ac_usg_cyc.data_type_id    = 10
;