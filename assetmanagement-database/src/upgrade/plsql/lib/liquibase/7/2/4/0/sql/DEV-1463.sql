--liquibase formatted sql


--changeSet DEV-1463:1 stripComments:false
-- **********************************
-- Remove tab idTabProductionPlanList
-- **********************************
DELETE FROM utl_todo_list_tab t WHERE t.todo_tab_id=10046;

--changeSet DEV-1463:2 stripComments:false
DELETE FROM utl_todo_tab t WHERE t.todo_tab_id=10046;

--changeSet DEV-1463:3 stripComments:false
-- **********************************
-- Migrate ref_task_class 0:NREST
-- **********************************
INSERT INTO ref_task_class
	(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, auto_complete_bool, unique_bool, workscope_bool, class_mode_cd, nr_est_bool, rstat_cd )
	SELECT 
		m.db_id, r.task_class_cd, r.bitmap_db_id, r.bitmap_tag, r.desc_sdesc, r.desc_ldesc, r.auto_complete_bool, r.unique_bool, r.workscope_bool, r.class_mode_cd, r.nr_est_bool, r.rstat_cd
		FROM mim_local_db m, ref_task_class r
		WHERE 
			r.task_class_db_id=0 AND r.task_class_cd='NREST'
			AND ( 
				EXISTS (
					SELECT 1 FROM ppc_phase_class p 
					WHERE 
						p.task_class_db_id=r.task_class_db_id AND 
						p.task_class_cd=r.task_class_cd ) 
				OR EXISTS (
					SELECT 1 FROM ref_task_subclass rt 
					WHERE 
						rt.task_class_db_id=r.task_class_db_id AND 
						rt.task_class_cd=r.task_class_cd ) 
				OR EXISTS ( 
					SELECT 1 FROM sched_stask s 
					WHERE 
						s.task_class_db_id=r.task_class_db_id AND 
						s.task_class_cd=r.task_class_cd ) 
			    OR EXISTS (
					SELECT 1 FROM task_task t 
					WHERE 
						t.task_class_db_id=r.task_class_db_id AND 
						t.task_class_cd=r.task_class_cd
				));		   

--changeSet DEV-1463:4 stripComments:false
-- migrate any ppc_phase_class rows
UPDATE ppc_phase_class p 
SET 
   p.task_class_db_id = (SELECT m.db_id from mim_local_db m)
WHERE
	p.task_class_db_id = 0 AND p.task_class_cd = 'NREST';

--changeSet DEV-1463:5 stripComments:false
-- migrate any ref_task_subclass rows
UPDATE ref_task_subclass r 
SET 
   r.task_class_db_id = (SELECT m.db_id from mim_local_db m)
WHERE 
	 r.task_class_db_id = 0 AND r.task_class_cd = 'NREST';

--changeSet DEV-1463:6 stripComments:false
-- migrate any sched_stask rows
UPDATE sched_stask s 
SET 
   s.task_class_db_id = (SELECT m.db_id from mim_local_db m)
WHERE
	s.task_class_db_id = 0 AND s.task_class_cd = 'NREST';

--changeSet DEV-1463:7 stripComments:false
-- migrate any task_task rows
UPDATE task_task t 
SET 
   t.task_class_db_id = (SELECT m.db_id from mim_local_db m)
WHERE 
	 t.task_class_db_id = 0 AND t.task_class_cd = 'NREST';		   

--changeSet DEV-1463:8 stripComments:false
-- delete ref term
DELETE FROM ref_task_class t where t.task_class_db_id = 0 AND t.task_class_cd = 'NREST';

--changeSet DEV-1463:9 stripComments:false
-- *************************************
-- Migrate ref_task_dep_action 0:NREST
-- *************************************
-- insert a new mim_local_db:NREST record to ref_task_dep_action 
INSERT INTO ref_task_dep_action
	( task_dep_action_db_id, task_dep_action_cd, desc_sdesc, desc_ldesc, rstat_cd )
	SELECT 
		m.db_id, r.task_dep_action_cd, r.desc_sdesc, r.desc_ldesc, r.rstat_cd
	FROM mim_local_db m, ref_task_dep_action r
	WHERE 
		r.task_dep_action_db_id = 0 AND r.task_dep_action_cd = 'NREST'
		AND EXISTS 
		(
			SELECT 1 FROM task_task_dep d 
			WHERE 
				d.task_dep_action_db_id = r.task_dep_action_db_id AND 
				d.task_dep_action_cd = r.task_dep_action_cd
		);

--changeSet DEV-1463:10 stripComments:false
-- migrate any task_task_dep rows
UPDATE task_task_dep d 
SET 
   d.task_dep_action_db_id = (SELECT m.db_id from mim_local_db m)
WHERE
	d.task_dep_action_db_id = 0 AND d.task_dep_action_cd = 'NREST';	

--changeSet DEV-1463:11 stripComments:false
-- delete ref term
DELETE FROM ref_task_dep_action t where t.task_dep_action_db_id = 0 AND t.task_dep_action_cd = 'NREST';