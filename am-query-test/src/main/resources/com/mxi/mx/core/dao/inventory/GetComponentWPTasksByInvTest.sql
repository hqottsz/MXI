
delete from eqp_bom_part t where t.bom_part_db_id = 777;
delete from inv_inv t where t.inv_no_db_id = 777;
delete from task_task t where t.task_db_id = 777;
delete from sched_stask t where t.sched_db_id = 777;
delete from evt_event t where t.event_db_id = 777;


--bom part ENGINE
insert into eqp_bom_part ( bom_part_db_id, bom_part_id, lru_bool ) values ( 777, 100, 1);

--bom part MODULE
insert into eqp_bom_part ( bom_part_db_id, bom_part_id, lru_bool ) values ( 777, 101, 1);

--bom part DISC
insert into eqp_bom_part ( bom_part_db_id, bom_part_id, lru_bool ) values ( 777, 102, 1);

--bom part BLADE
insert into eqp_bom_part ( bom_part_db_id, bom_part_id, lru_bool ) values ( 777, 103, 0);

--main inventory MODULE
insert into inv_inv ( inv_no_db_id, inv_no_id, nh_inv_no_db_id, nh_inv_no_id, bom_part_db_id,  bom_part_id ) values ( 777, 101, 777, 100, 777, 101 );

--main inventory DISC
insert into inv_inv ( inv_no_db_id, inv_no_id, nh_inv_no_db_id, nh_inv_no_id, bom_part_db_id,  bom_part_id ) values ( 777, 102, 777, 101, 777, 102 );


--main inventory BLADE
insert into inv_inv ( inv_no_db_id, inv_no_id, nh_inv_no_db_id, nh_inv_no_id, bom_part_db_id,  bom_part_id ) values ( 777, 103, 777, 102, 777, 103 );

--off-wing task 1 with hard deadline
insert into task_task ( task_db_id, task_id, soft_deadline_bool, task_must_remove_db_id, task_must_remove_cd, task_class_db_id, task_class_cd ) values ( 777, 101, 0, 0, 'OFF-PARENT', 0, 'REQ' );

-- off-wing task 2 with soft deadline
insert into task_task ( task_db_id, task_id, soft_deadline_bool, task_must_remove_db_id, task_must_remove_cd, task_class_db_id, task_class_cd ) values ( 777, 102, 0, 0, 'OFF-PARENT', 0, 'REQ' );

--on-wing task 3 with hard deadline
insert into task_task ( task_db_id, task_id, soft_deadline_bool, task_must_remove_db_id, task_must_remove_cd, task_class_db_id, task_class_cd ) values ( 777, 103, 0, 0, 'OFF-PARENT', 0, 'REQ' );

-- off-wing task 4 with hard deadline
insert into task_task ( task_db_id, task_id, soft_deadline_bool, task_must_remove_db_id, task_must_remove_cd, task_class_db_id, task_class_cd ) values ( 777, 104, 0, 0, 'OFF-PARENT', 0, 'REQ' );

--actual tasks
insert into sched_stask ( sched_db_id, sched_id, task_db_id,  task_id, main_inv_no_db_id, main_inv_no_id ) values ( 777, 101, 777, 101, 777, 101 );
insert into sched_stask ( sched_db_id, sched_id, task_db_id,  task_id, main_inv_no_db_id, main_inv_no_id ) values ( 777, 102, 777, 102, 777, 103 );
insert into sched_stask ( sched_db_id, sched_id, task_db_id,  task_id, main_inv_no_db_id, main_inv_no_id ) values ( 777, 103, 777, 103, 777, 103 );
insert into sched_stask ( sched_db_id, sched_id, task_db_id,  task_id ) values ( 777, 104, 777, 104 );

--work order package:
insert into sched_stask ( sched_db_id, sched_id, task_class_db_id,  task_class_cd, main_inv_no_db_id, main_inv_no_id ) values ( 777, 91, 0, 'RO', 777, 101 );

--check work package:
insert into sched_stask ( sched_db_id, sched_id, task_class_db_id,  task_class_cd, main_inv_no_db_id, main_inv_no_id ) values ( 777, 92, 0, 'CHECK', 777, 101 );


insert into evt_event ( event_db_id, event_id, h_event_db_id, h_event_id, hist_bool, event_status_cd ) values ( 777, 101, 777, 91, 0, 'ACTV' );
insert into evt_event ( event_db_id, event_id, h_event_db_id, h_event_id, hist_bool, event_status_cd ) values ( 777, 102, 777, 92, 0, 'ACTV' );
insert into evt_event ( event_db_id, event_id, h_event_db_id, h_event_id, hist_bool, event_status_cd ) values ( 777, 103, 777, 91, 0, 'ACTV' );

-- already under work order
insert into evt_event ( event_db_id, event_id, h_event_db_id, h_event_id, hist_bool, event_status_cd ) values ( 777, 104, 777, 92, 0, 'ACTV' );
