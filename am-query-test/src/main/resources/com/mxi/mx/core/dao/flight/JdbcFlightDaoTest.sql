-- Historic Flight
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, aircraft_db_id, aircraft_id, actual_departure_dt, actual_arrival_dt, arrival_loc_db_id, arrival_loc_id)
	VALUES ( '6D859FB370A842B5A4250EA2363782C1', 'MXCMPLT', 4650, 111333, SYSDATE - 6/24, SYSDATE - 1/24, 4650, 111);

-- Active Flight
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, aircraft_db_id, aircraft_id, actual_departure_dt, actual_arrival_dt, arrival_loc_db_id, arrival_loc_id)
	VALUES ( '6D859FB370A842B5A4250EA2363782C2', 'MXOFF', 4650, 111222, SYSDATE - 2/24, SYSDATE + 1/24, 4650, 111);

-- Planned Flights
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, aircraft_db_id, aircraft_id, sched_departure_dt, sched_arrival_dt, arrival_loc_db_id, arrival_loc_id)
	VALUES ( '6D859FB370A842B5A4250EA2363782C4', 'MXPLAN', 4650, 111222, SYSDATE + 12/24, SYSDATE + 14/24, 4650, 111);
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, aircraft_db_id, aircraft_id, sched_departure_dt, sched_arrival_dt, arrival_loc_db_id, arrival_loc_id)
	VALUES ( '6D859FB370A842B5A4250EA2363782C3', 'MXPLAN', 4650, 111222, SYSDATE + 2/24, SYSDATE + 10/24, 4650, 111);

-- The flight plan
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, flight_plan_ord, arr_leg_id, dep_leg_id)
	VALUES (4650, 111222, 1, '6D859FB370A842B5A4250EA2363782C1', '6D859FB370A842B5A4250EA2363782C2');
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, flight_plan_ord, arr_leg_id, dep_leg_id)
	VALUES (4650, 111222, 2, '6D859FB370A842B5A4250EA2363782C2', '6D859FB370A842B5A4250EA2363782C3');
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, flight_plan_ord, arr_leg_id, dep_leg_id)
	VALUES (4650, 111222, 3, '6D859FB370A842B5A4250EA2363782C3', '6D859FB370A842B5A4250EA2363782C4');