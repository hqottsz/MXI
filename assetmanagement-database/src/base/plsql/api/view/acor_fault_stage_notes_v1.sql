--liquibase formatted sql


--changeSet acor_fault_stage_notes_v1:1 stripComments:false
CREATE OR REPLACE VIEW acor_fault_stage_notes_v1
AS
SELECT
   sd_fault.alt_id              AS fault_id,
   org_hr.alt_id                AS user_id,
   evt_stage.system_bool        AS system_flag,
   evt_stage.event_status_cd    AS status_code,
   evt_stage.stage_reason_cd    AS reason_code,
   evt_stage.stage_dt           AS stage_date,
   evt_stage.stage_note         AS user_note
FROM
   sd_fault
   INNER JOIN evt_stage ON
	  sd_fault.fault_db_id = evt_stage.event_db_id AND
	  sd_fault.fault_id    = evt_stage.event_id
   INNER JOIN org_hr ON
	  evt_stage.hr_db_id    = org_hr.hr_db_id AND
	  evt_stage.hr_id       = org_hr.hr_id
;