-- inv to flight plan for WP 1
INSERT INTO inv_ac_flight_plan (inv_no_db_id,inv_no_id,loc_db_id,loc_id,flight_plan_ord,arr_leg_id,dep_leg_id) VALUES ('4650','21','11','1','10','25','27');

-- inv aircraft registration for WP 1
INSERT INTO inv_ac_reg (inv_no_db_id,inv_no_id,ac_reg_cd,inv_oper_cd) VALUES ('4650','21','N021','AWR');
INSERT INTO eqp_part_no (part_no_db_id,part_no_id) VALUES ('4650','100');

-- aircraft for WP 1
INSERT INTO inv_inv (inv_no_db_id,inv_no_id,assmbl_db_id,assmbl_cd,part_no_db_id,part_no_id,authority_db_id,authority_id,locked_bool,loc_db_id,loc_id,inv_class_db_id,inv_class_cd) VALUES ('4650','21','100','A320','4650','100','33','34','0','11','1','0','ACFT');

-- current_inv_loc
INSERT INTO inv_loc (loc_db_id,loc_id,loc_type_cd,loc_cd) VALUES ('11','1','AIRPORT','EWR');
INSERT INTO inv_loc (nh_loc_db_id,nh_loc_id,loc_db_id,loc_id) VALUES ('11','1','12','2');

-- valid user with access to the WP 1 root location
INSERT INTO org_hr_supply (hr_db_id,hr_id,loc_db_id,loc_id,user_id) VALUES ('1','7','11','1','123');
INSERT INTO org_hr (hr_db_id,hr_id,all_authority_bool) VALUES ('1','7','1');
INSERT INTO org_hr_authority (hr_db_id,hr_id,authority_db_id,authority_id) VALUES ('1','7','33','34');

-- locations assigned to the user with hr_id=7
INSERT INTO org_dept_hr (dept_db_id, dept_id, hr_db_id, hr_id) VALUES ('4650','100015','1','7');
INSERT INTO org_work_dept ( dept_db_id, dept_id,dept_cd,dept_type_db_id, dept_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc ) VALUES ( '4650', '100015', 'MAINT12-CD', '0', 'MAINT', '0', '1', 'dept_sdesc', 'dept_ldesc');
INSERT INTO inv_loc_dept (loc_db_id, loc_id, dept_db_Id, dept_id) VALUES ('11', '1', '4650', '100015');

-- valid user with access to the WP 1 location
INSERT INTO org_hr_supply (hr_db_id,hr_id,loc_db_id,loc_id,user_id) VALUES ('1','8','11','1','123');
INSERT INTO org_hr (hr_db_id,hr_id,all_authority_bool) VALUES ('1','8','1');
INSERT INTO org_hr_authority (hr_db_id,hr_id,authority_db_id,authority_id) VALUES ('1','8','33','34');

-- locations assigned to the user with hr_id=8
INSERT INTO org_dept_hr (dept_db_id, dept_id, hr_db_id, hr_id) VALUES ('4650','100016','1','8');
INSERT INTO org_work_dept ( dept_db_id, dept_id,dept_cd,dept_type_db_id, dept_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc ) VALUES ( '4650', '100016', 'MAINT1-CD', '0', 'MAINT', '0', '1', 'dept_sdesc', 'dept_ldesc');
INSERT INTO inv_loc_dept (loc_db_id, loc_id, dept_db_Id, dept_id) VALUES ('11', '11', '4650', '100016');
INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_cd, loc_cd, nh_loc_db_id, nh_loc_id) VALUES ('11','11', 'LINE', 'EWR/LN1', '11', '1');

-- valid user without access to the WP 1 location
INSERT INTO org_hr_supply (hr_db_id,hr_id,loc_db_id,loc_id,user_id) VALUES ('1','9','11','1','124');
INSERT INTO org_hr (hr_db_id,hr_id,all_authority_bool) VALUES ('1','9','1');
INSERT INTO org_hr_authority (hr_db_id,hr_id,authority_db_id,authority_id) VALUES ('1','9','33','35');

-- arrival flight leg
INSERT INTO fl_leg (leg_id,actual_arrival_dt,flight_leg_status_cd,master_flight_no,sched_arrival_dt,sched_departure_dt,actual_departure_dt,departure_loc_db_id,departure_loc_id,arrival_loc_db_id,arrival_loc_id,hist_bool,departure_gate_cd,arrival_gate_cd) VALUES ('25',TO_DATE('2001-11-10 3:14:00 AM', 'YYYY-MM-DD HH12:MI:SS am'),'MXPLAN','5676',TO_DATE('2001-11-10 3:21:00 AM', 'YYYY-MM-DD HH12:MI:SS am'),TO_DATE('2001-11-10 1:00:00 AM', 'YYYY-MM-DD HH12:MI:SS am'),TO_DATE('2001-11-10 1:00:00 AM', 'YYYY-MM-DD HH12:MI:SS am'),'13','3','14','4','0','31','34');

-- departure flight leg
INSERT INTO fl_leg (leg_id,flight_leg_status_cd,master_flight_no,sched_arrival_dt,sched_departure_dt,actual_departure_dt,arrival_loc_db_id,arrival_loc_id,hist_bool,departure_gate_cd) VALUES ('27','MXPLAN','5677',TO_DATE('2001-11-11 3:21:00 AM', 'YYYY-MM-DD HH12:MI:SS am'),TO_DATE('2001-11-11 1:00:00 AM', 'YYYY-MM-DD HH12:MI:SS am'),TO_DATE('2001-11-11 1:14:00 AM', 'YYYY-MM-DD HH12:MI:SS am'),'15','5','0','35');

-- prev_inv_loc
INSERT INTO inv_loc (loc_db_id,loc_id,loc_cd,loc_type_cd) VALUES ('13','3','EWR/2','AIRPORT');

-- center_inv_loc
INSERT INTO inv_loc (loc_db_id,loc_id,loc_type_cd,loc_cd,timezone_cd) VALUES ('14','4','AIRPORT','EWR/1','America/New_York');

-- next_inv_loc
INSERT INTO inv_loc (loc_db_id,loc_id,loc_cd,loc_type_cd) VALUES ('15','5','EWR/3','AIRPORT');
INSERT INTO sd_fault (leg_id,fault_db_id,fault_id,fail_priority_db_id,fail_priority_cd) VALUES ('25','1000300','32','0','ANALYZE');
INSERT INTO inv_inv (inv_no_db_id,inv_no_id,h_inv_no_db_id,h_inv_no_id) VALUES ('4650','100','4650','21');
INSERT INTO inv_inv (inv_no_db_id,inv_no_id,h_inv_no_db_id,h_inv_no_id) VALUES ('4650','101','4650','21');
INSERT INTO inv_inv (inv_no_db_id,inv_no_id,h_inv_no_db_id,h_inv_no_id) VALUES ('4650','102','4650','21');
INSERT INTO inv_inv (inv_no_db_id,inv_no_id,h_inv_no_db_id,h_inv_no_id) VALUES ('4650','103','4650','21');
INSERT INTO evt_inv (event_db_id,event_id,event_inv_id,inv_no_db_id,inv_no_id) VALUES ('4650','10','1','4650','100');
INSERT INTO evt_inv (event_db_id,event_id,event_inv_id,inv_no_db_id,inv_no_id) VALUES ('4650','20','1','4650','101');
INSERT INTO evt_inv (event_db_id,event_id,event_inv_id,inv_no_db_id,inv_no_id) VALUES ('4650','30','1','4650','102');
INSERT INTO evt_inv (event_db_id,event_id,event_inv_id,inv_no_db_id,inv_no_id) VALUES ('4650','40','1','4650','103');

-- Fault 1: Packaged Fault
INSERT INTO evt_event (event_db_id,event_id,event_type_db_id,event_type_cd,event_status_db_id,event_status_cd,hist_bool) VALUES ('4650','10','0','CF','0','CFACTV','0');

-- Fault 2: Unpackaged Fault
INSERT INTO evt_event (event_db_id,event_id,event_type_db_id,event_type_cd,event_status_db_id,event_status_cd,hist_bool) VALUES ('4650','20','0','CF','0','CFACTV','0');

-- Fault 3: Unpackaged Fault
INSERT INTO evt_event (event_db_id,event_id,event_type_db_id,event_type_cd,event_status_db_id,event_status_cd,hist_bool) VALUES ('4650','30','0','CF','0','CFACTV','0');

-- Fault 4: Historic Fault
INSERT INTO evt_event (event_db_id,event_id,event_type_db_id,event_type_cd,event_status_db_id,event_status_cd,hist_bool) VALUES ('4650','40','0','CF','0','CFCERT','1');
INSERT INTO evt_event_rel (event_db_id,event_id,event_rel_id,rel_event_db_id,rel_event_id,rel_type_db_id,rel_type_cd) VALUES ('4650','10','1','4650','200','0','CORRECT');
INSERT INTO evt_event_rel (event_db_id,event_id,event_rel_id,rel_event_db_id,rel_event_id,rel_type_db_id,rel_type_cd) VALUES ('4650','20','1','4650','201','0','CORRECT');
INSERT INTO evt_event_rel (event_db_id,event_id,event_rel_id,rel_event_db_id,rel_event_id,rel_type_db_id,rel_type_cd) VALUES ('4650','30','1','4650','202','0','CORRECT');
INSERT INTO evt_event_rel (event_db_id,event_id,event_rel_id,rel_event_db_id,rel_event_id,rel_type_db_id,rel_type_cd) VALUES ('4650','40','1','4650','203','0','CORRECT');

-- Fault 1 Corrective Task Event
INSERT INTO evt_event (event_db_id,event_id,h_event_db_id,h_event_id) VALUES ('4650','200','4650','1000');

-- Fault 2 Corrective Task Event
INSERT INTO evt_event (event_db_id,event_id) VALUES ('4650','201');

-- Fault 3 Corrective Task Event
INSERT INTO evt_event (event_db_id,event_id) VALUES ('4650','202');

-- Fault 4 Corrective Task Event
INSERT INTO evt_event (event_db_id,event_id) VALUES ('4650','203');

-- Fault 1 WP Event
INSERT INTO evt_event (event_db_id,event_id,h_event_db_id,h_event_id,event_sdesc) VALUES ('4650','1000','4650','1000','WP 2');

-- Fault 1 WP Task
INSERT INTO sched_stask (sched_db_id,sched_id,main_inv_no_db_id,main_inv_no_id,task_class_db_id,task_class_cd) VALUES ('4650','1000','4650','100','0','CHECK');

-- event for WP 1 (start date is 8 hours from now)
INSERT INTO evt_event (event_status_cd,event_status_db_id,event_db_id,event_id,event_sdesc,sched_start_dt) VALUES ('IN WORK',0,4650,1,'WP 1',SYSDATE + (8/24));

-- line location for WP 1
INSERT INTO evt_loc (event_db_id,event_id,event_loc_id,loc_db_id,loc_id) VALUES ('4650','1','1','11','11');

-- event for task under WP 1
INSERT INTO evt_event (event_status_cd,event_status_db_id,event_db_id,event_id,nh_event_db_id,nh_event_id) VALUES ('IN WORK','0','4650','6','4650','1');

-- WP 1
INSERT INTO sched_stask (sched_db_id, sched_id, main_inv_no_db_id, main_inv_no_id, task_class_cd, task_db_id, task_id, task_class_db_id) VALUES ('4650','1','4650','21','CHECK','4650','1','0');