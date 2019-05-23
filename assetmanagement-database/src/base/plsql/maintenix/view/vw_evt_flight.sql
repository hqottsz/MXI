--liquibase formatted sql


--changeSet vw_evt_flight:1 stripComments:false
CREATE OR REPLACE VIEW vw_evt_flight(
flight_id,
flight_sdesc,
flight_ldesc,
flight_status_cd,
flight_status_user_cd,
flight_reason_cd,
flight_reason_user_cd,
flight_type_db_id,
flight_type_cd,
inv_capability_db_id,
inv_capability_cd,
etops_bool,
sched_dep_dt,
sched_arr_dt,
actual_dep_dt,
actual_arr_dt,
dep_location_db_id,
dep_location_id,
dep_gate_cd,
dep_airport_cd,
arr_location_db_id,
arr_location_id,
arr_gate_cd,
arr_airport_cd,
up_dt,
down_dt,
master_flight_sdesc,
ext_key_sdesc,
doc_ref_sdesc,
hist_bool,
acft_inv_no_db_id,
acft_inv_no_id,
acft_inv_no_sdesc,
acft_assmbl_db_id,
acft_assmbl_cd,
acft_part_no_db_id,
acft_part_no_id,
acft_part_no_sdesc,
acft_part_no_oem
) AS
SELECT
fl_leg.leg_id as flight_id,
fl_leg.leg_no as flight_sdesc,
fl_leg.leg_desc as flight_ldesc,
ref_flight_leg_status.flight_leg_status_cd as flight_status_cd,
ref_flight_leg_status.display_code as flight_status_user_cd,
fl_leg.flight_reason_cd as flight_reason_cd,
ref_flight_reason.display_code as flight_reason_user_cd,
fl_leg.flight_type_db_id,
fl_leg.flight_type_cd,
fl_leg.inv_capability_db_id,
fl_leg.inv_capability_cd,
fl_leg.etops_bool,
fl_leg.sched_departure_dt as sched_dep_dt,
fl_leg.sched_arrival_dt as sched_arr_dt,
fl_leg.actual_departure_dt as actual_dep_dt,
fl_leg.actual_arrival_dt as actual_arr_dt,
fl_leg.departure_loc_db_id,
fl_leg.departure_loc_id,
fl_leg.departure_gate_cd,
dep_loc.loc_cd as dep_airport_cd,
fl_leg.arrival_loc_db_id,
fl_leg.arrival_loc_id,
fl_leg.arrival_gate_cd,
arr_loc.loc_cd as arr_airport_cd,
fl_leg.off_dt as up_dt,
fl_leg.on_dt as down_dt,
fl_leg.master_flight_no as master_flight_sdesc,
fl_leg.ext_key as ext_key_sdesc,
fl_leg.logbook_ref as doc_ref_sdesc,
fl_leg.hist_bool as hist_bool,
fl_leg.aircraft_db_id as acft_inv_no_db_id,
fl_leg.aircraft_id as acft_inv_no_id,
inv_inv.inv_no_sdesc,
inv_inv.assmbl_db_id,
inv_inv.assmbl_cd,
inv_inv.part_no_db_id,
inv_inv.part_no_id,
eqp_part_no.part_no_sdesc,
eqp_part_no.part_no_oem as acft_part_no_oem
FROM
fl_leg
INNER JOIN ref_flight_leg_status ON
fl_leg.flight_leg_status_cd = ref_flight_leg_status.flight_leg_status_cd
INNER JOIN inv_loc dep_loc ON
fl_leg.departure_loc_db_id = dep_loc.loc_db_id AND
fl_leg.departure_loc_id = dep_loc.loc_id
INNER JOIN inv_loc arr_loc ON
fl_leg.arrival_loc_db_id = arr_loc.loc_db_id AND
fl_leg.arrival_loc_id = arr_loc.loc_id
INNER JOIN inv_inv ON
fl_leg.aircraft_db_id = inv_inv.inv_no_db_id AND
fl_leg.aircraft_id = inv_inv.inv_no_id
INNER JOIN eqp_part_no ON
inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
inv_inv.part_no_id = eqp_part_no.part_no_id
LEFT OUTER JOIN ref_flight_reason ON
fl_leg.flight_reason_cd = ref_flight_reason.flight_reason_cd;