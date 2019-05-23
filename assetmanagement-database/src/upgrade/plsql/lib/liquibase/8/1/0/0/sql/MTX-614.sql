--liquibase formatted sql


--changeSet MTX-614:1 stripComments:false
-- Copy over records from EVT_PART_NO to EQP_PART_NO_LOG with event type code = 'PE'
INSERT INTO eqp_part_no_log ( 
   part_no_db_id, 
   part_no_id, 
   part_no_log_id, 
   log_action_db_id, 
   log_action_cd, 
   hr_db_id, 
   hr_id, 
   log_dt, 
   log_reason_db_id, 
   log_reason_cd, 
   user_note, 
   system_note 
) 
SELECT 
   evt_part_no.part_no_db_id, 
   evt_part_no.part_no_id, 
   part_no_log_id_seq.NEXTVAL, 
   0, 
   'PSEDIT', 
   evt_event.editor_hr_db_id,
   evt_event.editor_hr_id,
   evt_event.event_dt, 
   evt_event.stage_reason_db_id, 
   evt_event.stage_reason_cd, 
   evt_stage.user_stage_note, 
   evt_event.event_sdesc 
FROM 
   evt_part_no 
   INNER JOIN evt_event ON 
      evt_event.event_db_id = evt_part_no.event_db_id AND 
      evt_event.event_id    = evt_part_no.event_id 
   LEFT OUTER JOIN evt_stage ON
      evt_stage.event_db_id = evt_event.event_db_id AND
      evt_stage.event_id    = evt_event.event_id
WHERE 
   evt_event.event_type_db_id = 0 AND 
   evt_event.event_type_cd = 'PE' 
;

--changeSet MTX-614:2 stripComments:false
-- Delete EVT_PART_NO records with event type code = 'PE'
DELETE FROM
   evt_part_no
WHERE
   (evt_part_no.event_db_id, evt_part_no.event_id) IN 
      (
         SELECT
            evt_event.event_db_id,
            evt_event.event_id
         FROM
            evt_event
         WHERE
            evt_event.event_type_db_id = 0 AND
            evt_event.event_type_cd = 'PE'
      )
;

--changeSet MTX-614:3 stripComments:false
-- Delete EVT_STAGE records with event type code = 'PE'
DELETE FROM
   evt_stage
WHERE
   (evt_stage.event_db_id, evt_stage.event_id) IN 
      (
         SELECT
            evt_event.event_db_id,
            evt_event.event_id
         FROM
            evt_event
         WHERE
            evt_event.event_type_db_id = 0 AND
            evt_event.event_type_cd = 'PE'
   )
;

--changeSet MTX-614:4 stripComments:false
-- Delete EVT_EVENT records with event type code = 'PE'
DELETE FROM
   evt_event
WHERE
   evt_event.event_type_db_id = 0 AND
   evt_event.event_type_cd = 'PE'
;