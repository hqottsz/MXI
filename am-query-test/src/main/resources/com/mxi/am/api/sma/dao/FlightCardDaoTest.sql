-- LOCATIONS
--
-- YOW (AIRPORT)
-- \__YOW/LINE1 (LINE)
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd, loc_cd, timezone_cd)
	VALUES (4650, 1, NULL, NULL, 0, 'AIRPORT', 'YOW', 'America/New_York');
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd, loc_cd)
	VALUES (4650, 2, 4650, 1, 0, 'LINE', 'YOW/LINE1');
-- YYZ (AIRPORT)
-- \__YYZ/LINE1 (LINE)
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd, loc_cd, timezone_cd)
	VALUES (4650, 10, NULL, NULL, 0, 'AIRPORT', 'YYZ', 'America/New_York');
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd, loc_cd)
	VALUES (4650, 11, 4650, 10, 0, 'LINE', 'YYZ/LINE1');
-- LAX (AIRPORT)
-- \__LAX/LINE1 (LINE)
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd, loc_cd, timezone_cd)
	VALUES (4650, 30, NULL, NULL, 0, 'AIRPORT', 'LAX', 'America/Los_Angeles');
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd, loc_cd)
    VALUES (4650, 33, 4650, 30, 0, 'LINE', 'LAX/LINE1');
-- YYG used for filter test
-- YYG (AIRPORT)
-- \__YOW/LINE1 (LINE)
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd, loc_cd, timezone_cd)
    VALUES (4650, 71, NULL, NULL, 0, 'AIRPORT', 'YYG', 'Canada/Charlottetown');
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd, loc_cd)
    VALUES (4650, 72, 4650, 71, 0, 'LINE', 'YYG/LINE1');
-- FAE (AIRPORT)
-- \__FAE/LINE1 (LINE)
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd, loc_cd, timezone_cd)
    VALUES (4650, 91, NULL, NULL, 0, 'AIRPORT', 'FAE', 'FORREST/FAEHALL');
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd, loc_cd)
    VALUES (4650, 92, 4650, 91, 0, 'LINE', 'FAE/LINE1');

-- DEPARTMENTS
--
-- YOW Line Maintenance Department with access to:
--    YOW/LINE1
INSERT INTO org_work_dept (dept_db_id, dept_id, dept_cd, dept_type_db_id, dept_type_cd)
	VALUES ( 4650, 100, 'YOW-LINE-MAINT', 0, 'MAINT');
INSERT INTO inv_loc_dept (loc_db_id, loc_id, dept_db_id, dept_id)
	VALUES (4650, 2, 4650, 100);
-- YYZ Line Maintenance Department with access to:
--    YYZ/LINE1
INSERT INTO org_work_dept (dept_db_id, dept_id, dept_cd, dept_type_db_id, dept_type_cd)
	VALUES ( 4650, 200, 'YYZ-LINE-MAINT', 0, 'MAINT');
INSERT INTO inv_loc_dept (loc_db_id, loc_id, dept_db_id, dept_id)
	VALUES (4650, 11, 4650, 200);
-- YYG Line Maintenance Department with access to:
--    YYG/LINE1
INSERT INTO org_work_dept (dept_db_id, dept_id, dept_cd, dept_type_db_id, dept_type_cd)
    VALUES ( 4650, 7100, 'YOW-LINE-MAINT', 0, 'MAINT');
INSERT INTO inv_loc_dept (loc_db_id, loc_id, dept_db_id, dept_id)
    VALUES (4650, 72, 4650, 7100);
-- LAX Line Maintenance Department with access to: LAX/LINE1
INSERT INTO org_work_dept (dept_db_id, dept_id, dept_cd, dept_type_db_id, dept_type_cd)
    VALUES ( 4650, 66, 'lax-LINE-MAINT', 0, 'MAINT');
INSERT INTO inv_loc_dept (loc_db_id, loc_id, dept_db_id, dept_id)
    VALUES (4650, 30, 4650, 33);

-- USERS
--
-- 4650:10 is a Line Supervisor @ YOW
INSERT INTO org_hr (hr_db_id, hr_id, all_authority_bool) VALUES (4650, 10, 1);
INSERT INTO org_dept_hr (dept_db_id, dept_id, hr_db_id, hr_id) VALUES (4650, 100, 4650, 10);
-- 4650:20 is a Line Supervisor @ YYZ
INSERT INTO org_hr (hr_db_id, hr_id, all_authority_bool) VALUES (4650, 20, 1);
INSERT INTO org_dept_hr (dept_db_id, dept_id, hr_db_id, hr_id) VALUES (4650, 200, 4650, 20);
-- 4650:7100 is a Line Supervisor @ YOW
INSERT INTO org_hr (hr_db_id, hr_id, all_authority_bool) VALUES (4650, 710, 1);
INSERT INTO org_dept_hr (dept_db_id, dept_id, hr_db_id, hr_id) VALUES (4650, 7100, 4650, 710);
-- 4650:610 is a Line Supervisor @ LAX
INSERT INTO org_hr (hr_db_id, hr_id, all_authority_bool) VALUES (4650, 610, 1);
INSERT INTO org_dept_hr (dept_db_id, dept_id, hr_db_id, hr_id) VALUES (4650, 33, 4650, 610);


-- AIRCRAFT
--
-- AC-AIR
-- [YYZ -> YOW (MXOFF), YOW -> YYZ (MXPLAN)]
INSERT INTO inv_ac_reg (inv_no_db_id, inv_no_id, ac_reg_cd, inv_oper_db_id, inv_oper_cd)
	VALUES (4650, 1000, 'AC-AIR', 0, 'NORM');
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, assmbl_db_id, assmbl_cd, authority_db_id, authority_id, locked_bool, loc_db_id, loc_id)
	VALUES (4650, 1000, 4650, 'ACFT1', 4650, 10000, 0, 0, 1000);
-- Sub-inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd)
	VALUES (4650, 1001, 4650, 1000, 0, 'TRK');
-- Flight Plan
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
	VALUES (4650, 1000, 4650, 10, 1, NULL, 'ABCDEF0123456789ABCDEF0123456789');
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
	VALUES (4650, 1000, 4650, 1, 2, 'ABCDEF0123456789ABCDEF0123456789', 'ABCDEF012345678AABCDEF012345678A');
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
	VALUES (4650, 1000, 4650, 10, 3, 'ABCDEF012345678AABCDEF012345678A', NULL);
-- MXOFF leg YYZ -> YOW
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, master_flight_no, sched_departure_dt, actual_departure_dt, sched_arrival_dt, actual_arrival_dt, departure_loc_db_id, departure_loc_id, arrival_loc_db_id, arrival_loc_id, departure_gate_cd, arrival_gate_cd, hist_bool)
	VALUES ('ABCDEF0123456789ABCDEF0123456789', 'MXOFF', 1234, SYSDATE - 1/24, SYSDATE - 1/24, SYSDATE + 1/24, SYSDATE + 1/24, 4650, 10, 4650, 1, '1', '2', 0);
-- MXPLAN leg YOW -> YYZ
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, master_flight_no, sched_departure_dt, actual_departure_dt, sched_arrival_dt, actual_arrival_dt, departure_loc_db_id, departure_loc_id, arrival_loc_db_id, arrival_loc_id, departure_gate_cd, arrival_gate_cd, hist_bool)
	VALUES ('ABCDEF012345678AABCDEF012345678A', 'MXPLAN', 1235, SYSDATE + 1/4, SYSDATE + 1/4, SYSDATE + 1/2, SYSDATE + 1/2, 4650, 1, 4650, 10, '2', '3', 0);

-- AC-GRND
-- [YOW -> YYZ (MXPLAN), YYZ -> YOW (MXPLAN)]
INSERT INTO inv_ac_reg (inv_no_db_id, inv_no_id, ac_reg_cd, inv_oper_db_id, inv_oper_cd)
	VALUES (4650, 2000, 'AC-GRND', 0, 'NORM');
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, assmbl_db_id, assmbl_cd, authority_db_id, authority_id, locked_bool, loc_db_id, loc_id)
	VALUES (4650, 2000, 4650, 'ACFT1', 4650, 20000, 0, 4650, 2);
-- Sub-inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd)
	VALUES (4650, 2001, 4650, 2000, 0, 'SYS');
-- Flight Plan
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
	VALUES (4650, 2000, 4650, 1, 1, NULL, '0123456789ABCDEF0123456789ABCDEF');
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
	VALUES (4650, 2000, 4650, 10, 2, '0123456789ABCDEF0123456789ABCDEF', '0123456789ABCDEF0123456789ABCDF0');
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
	VALUES (4650, 2000, 4650, 1, 3, '0123456789ABCDEF0123456789ABCDF0', NULL);
-- MXPLAN leg YOW -> YYZ
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, master_flight_no, sched_departure_dt, actual_departure_dt, sched_arrival_dt, actual_arrival_dt, departure_loc_db_id, departure_loc_id, arrival_loc_db_id, arrival_loc_id, departure_gate_cd, arrival_gate_cd, hist_bool)
	VALUES ('0123456789ABCDEF0123456789ABCDEF', 'MXPLAN', 5678, SYSDATE + 1/12, SYSDATE + 1/12, SYSDATE + 1/6, SYSDATE + 1/6, 4650, 1, 4650, 10, '2', '1', 0);
-- MXPLAN leg YYZ -> YOW
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, master_flight_no, sched_departure_dt, actual_departure_dt, sched_arrival_dt, actual_arrival_dt, departure_loc_db_id, departure_loc_id, arrival_loc_db_id, arrival_loc_id, departure_gate_cd, arrival_gate_cd, hist_bool)
	VALUES ('0123456789ABCDEF0123456789ABCDF0', 'MXPLAN', 5678, SYSDATE + 1/12, SYSDATE + 1/12, SYSDATE + 1/6, SYSDATE + 1/6, 4650, 10, 4650, 1, '2', '1', 0);


-- AC-GONE
-- This aircraft has departed from YOW and should not be visible to anyone @ YOW or YYZ
--
-- [YOW -> LAX (MXOFF)]
INSERT INTO inv_ac_reg (inv_no_db_id, inv_no_id, ac_reg_cd, inv_oper_db_id, inv_oper_cd)
	VALUES (4650, 3000, 'AC-GONE', 0, 'NORM');
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, assmbl_db_id, assmbl_cd, authority_db_id, authority_id, locked_bool, loc_db_id, loc_id)
	VALUES (4650, 3000, 4650, 'ACFT1', 4650, 10000, 0, 0, 1000);
-- Flight Plan
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
	VALUES (4650, 3000, 4650, 1, 1, 'AABBCC1234567890AABBCC1234567880', 'AABBCC1234567890AABBCC1234567890');
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
	VALUES (4650, 3000, 4650, 30, 2, 'AABBCC1234567890AABBCC1234567890', NULL);
-- MXCOMPLETE leg YYZ -> YOW
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, master_flight_no, sched_departure_dt, actual_departure_dt, sched_arrival_dt, actual_arrival_dt, departure_loc_db_id, departure_loc_id, arrival_loc_db_id, arrival_loc_id, departure_gate_cd, arrival_gate_cd, hist_bool)
	VALUES ('AABBCC1234567890AABBCC1234567880', 'MXCMPLT', 9999, SYSDATE - 1/2, SYSDATE - 1/2, SYSDATE - 1/4, SYSDATE - 1/4, 4650, 10, 4650, 1, '5', '3', 1);
-- MXOFF leg YOW -> LAX
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, master_flight_no, sched_departure_dt, actual_departure_dt, sched_arrival_dt, actual_arrival_dt, departure_loc_db_id, departure_loc_id, arrival_loc_db_id, arrival_loc_id, departure_gate_cd, arrival_gate_cd, hist_bool)
	VALUES ('AABBCC1234567890AABBCC1234567890', 'MXOFF', 9999, SYSDATE - 1/24, SYSDATE - 1/24, SYSDATE + 1/6, SYSDATE + 1/6, 4650, 1, 4650, 30, '3', '8', 0);


/**
 * The AC-AIR Work Package Data
 * Scheduled at +2 HRs
 * It includes a COMMIT work package at the time it arrives at YOW.
 * The work package has 3 tasks in it:
 * 	- 1 open fault
 * 	- 2 open tasks
 */
-- AC-AIR WP [COMMIT, YOW/LINE1]
INSERT INTO evt_event (event_db_id, event_id, h_event_db_id, h_event_id, event_sdesc, sched_start_dt, actual_start_dt, event_status_db_id, event_status_cd, hist_bool)
	VALUES (4650, 1234, 4650, 1234, 'AC-AIR WP', SYSDATE + 1/12, SYSDATE + 1/12, 0, 'COMMIT', 0);
INSERT INTO evt_loc (event_db_id, event_id, event_loc_id, loc_db_id, loc_id)
	VALUES (4650, 1234, 1, 4650, 2);
INSERT INTO sched_stask (sched_db_id, sched_id, task_class_db_id, task_class_cd, main_inv_no_db_id, main_inv_no_id, hist_bool_ro)
	VALUES (4650, 1234, 0, 'CHECK', 4650, 1000, 0);
-- 1 Packaged fault and 2 tasks in workscope
-- fault (1)
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, event_status_db_id, event_status_cd, hist_bool)
	VALUES (4650, 1, 0, 'CF', 0, 'CFACTV', 0);
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, h_event_db_id, h_event_id, nh_event_db_id, nh_event_id, hist_bool)
	VALUES (4650, 2, 0, 'TS', 4650, 1234, 4650, 1234, 0);
INSERT INTO evt_event_rel (event_db_id, event_id, event_rel_id, rel_event_db_id, rel_event_id, rel_type_db_id, rel_type_cd)
	VALUES (4650, 1, 1, 4650, 2, 0, 'CORRECT');
INSERT INTO sched_stask (sched_db_id, sched_id, fault_db_id, fault_id, main_inv_no_db_id, main_inv_no_id, hist_bool_ro)
	VALUES (4650, 2, 4650, 1, 4650, 1001, 0);
-- tasks (2)
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, h_event_db_id, h_event_id, nh_event_db_id, nh_event_id, hist_bool)
	VALUES (4650, 3, 0, 'TS', 4650, 1234, 4650, 1234, 0);
INSERT INTO sched_stask (sched_db_id, sched_id, main_inv_no_db_id, main_inv_no_id, hist_bool_ro)
	VALUES (4650, 3, 4650, 1001, 0);
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, h_event_db_id, h_event_id, nh_event_db_id, nh_event_id, hist_bool)
	VALUES (4650, 4, 0, 'TS', 4650, 1234, 4650, 1234, 0);
INSERT INTO sched_stask (sched_db_id, sched_id, main_inv_no_db_id, main_inv_no_id, hist_bool_ro)
	VALUES (4650, 4, 4650, 1001, 0);


/**
 * The AC-GRND Work Package Data
 * Scheduled at -1 HR
 * It includes an IN WORK work package that is under way at YOW/LINE1.
 * The work package has 4 tasks in it:
 * 	- 2 open faults
 * 	- 1 open task
 *  - 1 completed task
 */
-- AC-GRND WP [IN WORK, YOW/LINE1]
INSERT INTO evt_event (event_db_id, event_id, h_event_db_id, h_event_id, event_sdesc, sched_start_dt, actual_start_dt, event_status_db_id, event_status_cd, hist_bool)
	VALUES (4650, 5678, 4650, 5678, 'AC-GRND WP', SYSDATE - 1/24, SYSDATE - 1/24, 0, 'IN WORK', 0);
INSERT INTO evt_loc (event_db_id, event_id, event_loc_id, loc_db_id, loc_id)
	VALUES (4650, 5678, 1, 4650, 2);
INSERT INTO sched_stask (sched_db_id, sched_id, task_class_db_id, task_class_cd, main_inv_no_db_id, main_inv_no_id, hist_bool_ro)
	VALUES (4650, 5678, 0, 'CHECK', 4650, 2000, 0);
-- 2 Packaged faults and 1 task (completed) in workscope
-- faults (2)
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, event_status_db_id, event_status_cd, hist_bool)
	VALUES (4650, 5, 0, 'CF', 0, 'CFACTV', 0);
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, h_event_db_id, h_event_id, nh_event_db_id, nh_event_id, hist_bool)
	VALUES (4650, 6, 0, 'TS', 4650, 5678, 4650, 5678, 0);
INSERT INTO evt_event_rel (event_db_id, event_id, event_rel_id, rel_event_db_id, rel_event_id, rel_type_db_id, rel_type_cd)
	VALUES (4650, 5, 1, 4650, 6, 0, 'CORRECT');
INSERT INTO sched_stask (sched_db_id, sched_id, fault_db_id, fault_id, main_inv_no_db_id, main_inv_no_id, hist_bool_ro)
	VALUES (4650, 6, 4650, 5, 4650, 2001, 0);

INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, event_status_db_id, event_status_cd, hist_bool)
	VALUES (4650, 7, 0, 'CF', 0, 'CFACTV', 0);
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, h_event_db_id, h_event_id, nh_event_db_id, nh_event_id, hist_bool)
	VALUES (4650, 8, 0, 'TS', 4650, 5678, 4650, 5678, 0);
INSERT INTO evt_event_rel (event_db_id, event_id, event_rel_id, rel_event_db_id, rel_event_id, rel_type_db_id, rel_type_cd)
	VALUES (4650, 7, 1, 4650, 8, 0, 'CORRECT');
INSERT INTO sched_stask (sched_db_id, sched_id, fault_db_id, fault_id, main_inv_no_db_id, main_inv_no_id, hist_bool_ro)
	VALUES (4650, 8, 4650, 7, 4650, 2001, 0);
-- tasks (2)
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, h_event_db_id, h_event_id, nh_event_db_id, nh_event_id, hist_bool)
	VALUES (4650, 9, 0, 'TS', 4650, 5678, 4650, 5678, 0);
INSERT INTO sched_stask (sched_db_id, sched_id, main_inv_no_db_id, main_inv_no_id, hist_bool_ro)
	VALUES (4650, 9, 4650, 2001, 0);
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, h_event_db_id, h_event_id, nh_event_db_id, nh_event_id, hist_bool)
	VALUES (4650, 10, 0, 'TS', 4650, 5678, 4650, 5678, 1);
INSERT INTO sched_stask (sched_db_id, sched_id, main_inv_no_db_id, main_inv_no_id, hist_bool_ro)
	VALUES (4650, 10, 4650, 2001, 1);

/**
 * AC-AIR Open faults
 * There are 2 open unpackaged faults (found during current flight from YYZ -> YOW)
 */
-- fault 1
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, event_status_db_id, event_status_cd, hist_bool)
	VALUES (4650, 11, 0, 'CF', 0, 'CFACTV', 0);
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, h_event_db_id, h_event_id, nh_event_db_id, nh_event_id, hist_bool)
	VALUES (4650, 12, 0, 'TS', 4650, 12, 4650, 12, 0);
INSERT INTO evt_event_rel (event_db_id, event_id, event_rel_id, rel_event_db_id, rel_event_id, rel_type_db_id, rel_type_cd)
	VALUES (4650, 11, 1, 4650, 12, 0, 'CORRECT');
INSERT INTO sched_stask (sched_db_id, sched_id, fault_db_id, fault_id, main_inv_no_db_id, main_inv_no_id, hist_bool_ro)
	VALUES (4650, 12, 4650, 11, 4650, 1001, 0);
-- fault 2
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, event_status_db_id, event_status_cd, hist_bool)
	VALUES (4650, 13, 0, 'CF', 0, 'CFACTV', 0);
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, h_event_db_id, h_event_id, nh_event_db_id, nh_event_id, hist_bool)
	VALUES (4650, 14, 0, 'TS', 4650, 14, 4650, 14, 0);
INSERT INTO evt_event_rel (event_db_id, event_id, event_rel_id, rel_event_db_id, rel_event_id, rel_type_db_id, rel_type_cd)
	VALUES (4650, 13, 1, 4650, 14, 0, 'CORRECT');
INSERT INTO sched_stask (sched_db_id, sched_id, fault_db_id, fault_id, main_inv_no_db_id, main_inv_no_id, hist_bool_ro)
	VALUES (4650, 14, 4650, 13, 4650, 1001, 0);

/**
 * AC-GRND Open fault
 * There is 1 open unpackaged fault (found on ground during A check) and 1 deferred fault (which should not be counted)
 */
-- fault 1 (ACTIVE)
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, event_status_db_id, event_status_cd, hist_bool)
	VALUES (4650, 15, 0, 'CF', 0, 'CFACTV', 0);
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, h_event_db_id, h_event_id, nh_event_db_id, nh_event_id, hist_bool)
	VALUES (4650, 16, 0, 'TS', 4650, 16, 4650, 16, 0);
INSERT INTO evt_event_rel (event_db_id, event_id, event_rel_id, rel_event_db_id, rel_event_id, rel_type_db_id, rel_type_cd)
	VALUES (4650, 15, 1, 4650, 16, 0, 'CORRECT');
INSERT INTO sched_stask (sched_db_id, sched_id, fault_db_id, fault_id, main_inv_no_db_id, main_inv_no_id, hist_bool_ro)
	VALUES (4650, 16, 4650, 15, 4650, 2001, 0);
-- fault 2 (DEFERRED)
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, event_status_db_id, event_status_cd, hist_bool)
	VALUES (4650, 17, 0, 'CF', 0, 'CFDEFER', 0);
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, h_event_db_id, h_event_id, nh_event_db_id, nh_event_id, hist_bool)
	VALUES (4650, 18, 0, 'TS', 4650, 18, 4650, 18, 0);
INSERT INTO evt_event_rel (event_db_id, event_id, event_rel_id, rel_event_db_id, rel_event_id, rel_type_db_id, rel_type_cd)
	VALUES (4650, 17, 1, 4650, 18, 0, 'CORRECT');
INSERT INTO sched_stask (sched_db_id, sched_id, fault_db_id, fault_id, main_inv_no_db_id, main_inv_no_id, hist_bool_ro)
	VALUES (4650, 18, 4650, 17, 4650, 2001, 0);

-- AIRCRAFT
--
-- AC-IN
-- [Planned arrival to YYG]
INSERT INTO inv_ac_reg (inv_no_db_id, inv_no_id, ac_reg_cd, inv_oper_db_id, inv_oper_cd)
    VALUES (4650, 71000, 'AC-IN', 0, 'NORM');
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, assmbl_db_id, assmbl_cd, authority_db_id, authority_id, locked_bool, loc_db_id, loc_id)
    VALUES (4650, 71000, 4650, 'ACFT1', 4650, 710000, 0, 0, 1000);
-- Sub-inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd)
    VALUES (4650, 71001, 4650, 71000, 0, 'TRK');
    -- Flight Plan
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
    VALUES (4650, 71000, 4650, 710, 1, 'ABCDEF0123456789ABCDEF0123459999', NULL);
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
    VALUES (4650, 71000, 4650, 71, 2, NULL, 'ABCDEF0123456789ABCDEF0123459999');
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, master_flight_no, sched_departure_dt, actual_departure_dt, sched_arrival_dt, actual_arrival_dt, departure_loc_db_id, departure_loc_id, arrival_loc_db_id, arrival_loc_id, departure_gate_cd, arrival_gate_cd, hist_bool)
    VALUES ('ABCDEF0123456789ABCDEF0123459999', 'MXPLAN', 71234, SYSDATE + 1/24, SYSDATE + 1/24, SYSDATE + 4/24, SYSDATE + 4/24, 4650, 91, 4650, 71, '71', '72', 0);


-- AIRCRAFT
--
-- AC-OUT
-- [Planned departure from YYG]
INSERT INTO inv_ac_reg (inv_no_db_id, inv_no_id, ac_reg_cd, inv_oper_db_id, inv_oper_cd)
    VALUES (4650, 81000, 'AC-OUT', 0, 'NORM');
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, assmbl_db_id, assmbl_cd, authority_db_id, authority_id, locked_bool, loc_db_id, loc_id)
    VALUES (4650, 81000, 4650, 'ACFT1', 4650, 710000, 0, 0, 1000);
-- Sub-inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd)
    VALUES (4650, 81001, 4650, 81000, 0, 'TRK');
    -- Flight Plan
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
    VALUES (4650, 81000, 4650, 710, 1, NULL, 'CCCDEF0123456789ABCDEF0123456789');
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
    VALUES (4650, 81000, 4650, 71, 2, 'CCCDEF0123456789ABCDEF0123456789', NULL);
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, master_flight_no, sched_departure_dt, actual_departure_dt, sched_arrival_dt, actual_arrival_dt, departure_loc_db_id, departure_loc_id, arrival_loc_db_id, arrival_loc_id, departure_gate_cd, arrival_gate_cd, hist_bool)
    VALUES ('CCCDEF0123456789ABCDEF0123456789', 'MXPLAN', 81234, SYSDATE + 11/24, SYSDATE + 11/24, SYSDATE + 13/24, SYSDATE + 13/24, 4650, 71, 4650, 91, '81', '82', 0);


-- AIRCRAFT
--
-- AC-IN-LEFT
-- [Planned departure and arrival from YYG ]
INSERT INTO inv_ac_reg (inv_no_db_id, inv_no_id, ac_reg_cd, inv_oper_db_id, inv_oper_cd)
    VALUES (4650, 91000, 'AC-INLEFT', 0, 'NORM');
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, assmbl_db_id, assmbl_cd, authority_db_id, authority_id, locked_bool, loc_db_id, loc_id)
    VALUES (4650, 91000, 4650, 'ACFT1', 4650, 710000, 0, 0, 1000);
-- Sub-inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd)
    VALUES (4650, 91001, 4650, 91000, 0, 'TRK');
    -- Flight Plan
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
    VALUES (4650, 91000, 4650, 710, 1, 'BBCDEF0123456789ABCDEF0123459999', 'DDCDEF0123456789ABCDEF0123456789');
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
    VALUES (4650, 91000, 4650, 71, 2, 'DDCDEF0123456789ABCDEF0123456789', null);
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, master_flight_no, sched_departure_dt, actual_departure_dt, sched_arrival_dt, actual_arrival_dt, departure_loc_db_id, departure_loc_id, arrival_loc_db_id, arrival_loc_id, departure_gate_cd, arrival_gate_cd, hist_bool)
    VALUES ('BBCDEF0123456789ABCDEF0123459999', 'MXPLAN', 40234, SYSDATE + 17/24, SYSDATE + 17/24, SYSDATE + 19/24, SYSDATE + 19/24, 4650, 91, 4650, 71, '71', '72', 0);
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, master_flight_no, sched_departure_dt, actual_departure_dt, sched_arrival_dt, actual_arrival_dt, departure_loc_db_id, departure_loc_id, arrival_loc_db_id, arrival_loc_id, departure_gate_cd, arrival_gate_cd, hist_bool)
    VALUES ('DDCDEF0123456789ABCDEF0123456789', 'MXPLAN', 40234, SYSDATE + 25/24, SYSDATE + 25/24, SYSDATE + 18/24, SYSDATE + 28/24, 4650, 71, 4650, 91, '81', '82', 0);


INSERT INTO inv_ac_reg (inv_no_db_id, inv_no_id, ac_reg_cd, inv_oper_db_id, inv_oper_cd)
    VALUES (4650, 77777, 'AC-LAX', 0, 'NORM');
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, assmbl_db_id, assmbl_cd, authority_db_id, authority_id, locked_bool, loc_db_id, loc_id)
    VALUES (4650, 77777, 4650, 'ACFT1', 4650, 710000, 0, 0, 1000);
-- Sub-inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd)
    VALUES (4650, 77776, 4650, 77777, 0, 'TRK');
    -- Flight Plan
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
    VALUES (4650, 77777, 4650, 710, 1, 'BBCDEF0123456789ABCDEF0123666666', 'DDCDEF0123456789ABCDEF0123666666');
INSERT INTO inv_ac_flight_plan (inv_no_db_id, inv_no_id, loc_db_id, loc_id, flight_plan_ord, arr_leg_id, dep_leg_id)
    VALUES (4650, 77777, 4650, 71, 2, 'DDCDEF0123456789ABCDEF0123666666', null);

INSERT INTO fl_leg (leg_id, flight_leg_status_cd, master_flight_no, sched_departure_dt, actual_departure_dt, sched_arrival_dt, actual_arrival_dt, departure_loc_db_id, departure_loc_id, arrival_loc_db_id, arrival_loc_id, departure_gate_cd, arrival_gate_cd, hist_bool)
    VALUES ('BBCDEF0123456789ABCDEF0123666666', 'MXCMPLT', 40234, SYSDATE - 1, SYSDATE - 1 , SYSDATE , SYSDATE , 4650, 91, 4650, 30, '71', '72', 0);
INSERT INTO fl_leg (leg_id, flight_leg_status_cd, master_flight_no, sched_departure_dt, actual_departure_dt, sched_arrival_dt, actual_arrival_dt, departure_loc_db_id, departure_loc_id, arrival_loc_db_id, arrival_loc_id, departure_gate_cd, arrival_gate_cd, hist_bool)
    VALUES ('DDCDEF0123456789ABCDEF0123666666', 'MXPLAN', 40234, SYSDATE + 3/24, SYSDATE + 3/24, SYSDATE + 4/24, SYSDATE + 4/24, 4650, 30, 4650, 91, '81', '82', 0);
