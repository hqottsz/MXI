--liquibase formatted sql


--changeSet OPER-22906:5 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_jic_history_v1
AS
SELECT
   sched_stask.alt_id           AS sched_id,
   sched_stask.barcode_sdesc    AS barcode,
   evt_stage.stage_dt           AS recorded_date,
   utl_user.username            AS mx_username,
   utl_user.first_name || ' ' || utl_user.last_name  AS recorded_by,
   evt_stage.stage_note,
   evt_stage.system_bool         AS system_flag,
   evt_stage.event_status_cd     AS status_code
FROM
   sched_stask
   INNER JOIN ref_task_class ON
	  sched_stask.task_class_db_id = ref_task_class.task_class_db_id AND
	  sched_stask.task_class_cd    = ref_task_class.task_class_cd
   INNER JOIN evt_stage ON
	  sched_stask.sched_db_id = evt_stage.event_db_id AND
	  sched_stask.sched_id    = evt_stage.event_id
   INNER JOIN org_hr ON
	  evt_stage.hr_db_id = org_hr.hr_db_id AND
	  evt_stage.hr_id    = org_hr.hr_id
   INNER JOIN utl_user ON
	  org_hr.user_id = utl_user.user_id
WHERE
	ref_task_class.class_mode_cd = 'JIC'
;