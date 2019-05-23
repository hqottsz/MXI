--liquibase formatted sql


--changeSet MX-14530:1 stripComments:false
-- Move PPAAUP to REF_LOG_ACTION
INSERT INTO ref_log_action(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT
   0,'PPAAUP',
   'Average Unit Price Adjustment', 'The average unit price for this part has been adjusted',
   'PPAAUP',
   0 , TO_DATE('2012-11-02', 'YYYY-MM-DD'), TO_DATE('2012-11-02', 'YYYY-MM-DD'), 0 , 'MXI'
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT
         1
      FROM
         ref_log_action
      WHERE
         log_action_db_id = 0 AND
         log_action_cd = 'PPAAUP'
   )
;

--changeSet MX-14530:2 stripComments:false
-- Add PT to REF_LOG_ACTION
INSERT INTO ref_log_action(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT
   0,'PT',
   'Part Type Change', 'Part Type Change',
   'PTYPE',
   0 , TO_DATE('2012-11-02', 'YYYY-MM-DD'), TO_DATE('2012-11-02', 'YYYY-MM-DD'), 0 , 'MXI'
FROM
   dual
WHERE
   NOT EXISTS (
      SELECT
         1
      FROM
         ref_log_action
      WHERE
         log_action_db_id = 0 AND
         log_action_cd = 'PT'
   )
;

--changeSet MX-14530:3 stripComments:false
-- Convert customer-specific log reasons
INSERT INTO ref_log_reason (
  log_reason_db_id, log_reason_cd,
  log_action_db_id, log_action_cd,
  desc_sdesc, desc_ldesc,
  user_cd
)
SELECT
   ref_stage_reason.stage_reason_db_id, ref_stage_reason.stage_reason_cd,
   ref_stage_reason.event_status_db_id, ref_stage_reason.event_status_cd,
   desc_sdesc, desc_ldesc,
   user_reason_cd
FROM
   ref_stage_reason
WHERE
   ref_stage_reason.event_status_db_id = 0 AND
   ref_stage_reason.event_status_cd IN ('PT', 'PPAAUP')
   AND
   NOT EXISTS (
      SELECT 1 FROM ref_log_reason WHERE
         ref_log_reason.log_reason_db_id = ref_stage_reason.stage_reason_db_id AND
         ref_log_reason.log_reason_cd    = ref_stage_reason.stage_reason_cd
         AND
         ref_log_reason.log_action_db_id = ref_stage_reason.event_status_db_id AND
         ref_log_reason.log_action_cd    = ref_stage_reason.event_status_cd
   )
;

--changeSet MX-14530:4 stripComments:false
-- Move data from EVT_PART_NO to EQP_PART_NO_LOG
INSERT INTO eqp_part_no_log (
   part_no_db_id, part_no_id, part_no_log_id,
   log_action_db_id, log_action_cd,
   hr_db_id, hr_id,
   log_dt,
   log_reason_db_id, log_reason_cd,
   user_note,
   system_note
)
SELECT
   part_no_db_id, part_no_id, PART_NO_LOG_ID_SEQ.NEXTVAL,
   0, 'PPAAUP',
   0, 5, -- notavailable (original implementation did not log hr)
   evt_event.event_dt,
   evt_event.stage_reason_db_id, evt_event.stage_reason_cd,
   evt_stage.user_stage_note,
   DECODE(event_ldesc, NULL, event_sdesc, event_ldesc)
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
   evt_event.event_type_cd    = 'PPA'
;

--changeSet MX-14530:5 stripComments:false
INSERT INTO eqp_part_no_log (
   part_no_db_id, part_no_id, part_no_log_id,
   log_action_db_id, log_action_cd,
   hr_db_id, hr_id,
   log_dt,
   log_reason_db_id, log_reason_cd,
   user_note,
   system_note
)
SELECT
   evt_part_no.part_no_db_id, part_no_id, PART_NO_LOG_ID_SEQ.NEXTVAL,
   evt_event.event_type_db_id, evt_event.event_type_cd,
   evt_event.editor_hr_db_id, evt_event.editor_hr_id,
   evt_event.event_dt,
   evt_event.stage_reason_db_id, evt_event.stage_reason_cd,
   evt_stage.user_stage_note,
   DECODE(event_ldesc, NULL, event_ldesc, event_sdesc)
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
   evt_event.event_type_cd    = 'PT'
;

--changeSet MX-14530:6 stripComments:false
-- Remove EVT_STAGE associated with EVT_PART_NO
DELETE FROM
   evt_stage
WHERE
   (evt_stage.event_db_id, evt_stage.event_id) IN (
      SELECT
         evt_event.event_db_id,
         evt_event.event_id
      FROM
         evt_event
      WHERE
         evt_event.event_type_db_id = 0 AND
         evt_event.event_type_cd IN ('PT', 'PPA')
   )
;

--changeSet MX-14530:7 stripComments:false
-- Disassociate FNC_XACTION from EVT_PART_NO
UPDATE
   fnc_xaction_log
SET
   fnc_xaction_log.event_db_id = NULL,
   fnc_xaction_log.event_id    = NULL
WHERE
   (fnc_xaction_log.event_db_id, fnc_xaction_log.event_id) IN (
      SELECT
         evt_part_no.event_db_id,
         evt_part_no.event_id
      FROM
         evt_part_no
         INNER JOIN evt_event ON
            evt_event.event_db_id = evt_part_no.event_db_id AND
            evt_event.event_id    = evt_part_no.event_id
      WHERE
         evt_event.event_type_db_id = 0 AND
         evt_event.event_type_cd IN ('PT', 'PPA')
   )
;

--changeSet MX-14530:8 stripComments:false
-- Remove EVT_PART_NO with associated PPA and PT events
DELETE FROM
   evt_part_no
WHERE
   (evt_part_no.event_db_id, evt_part_no.event_id) IN (
      SELECT
         evt_event.event_db_id,
         evt_event.event_id
      FROM
         evt_event
      WHERE
         evt_event.event_type_db_id = 0 AND
         evt_event.event_type_cd IN ('PT', 'PPA')
   )
;

--changeSet MX-14530:9 stripComments:false
-- Remove EVT_EVENT with associated PPA and PT events
DELETE FROM
   evt_event
WHERE
   evt_event.event_type_db_id = 0 AND
   evt_event.event_type_cd IN ('PT', 'PPA')
;

--changeSet MX-14530:10 stripComments:false
-- Remove PPA and PT from REF_EVENT_*
DELETE FROM
   ref_stage_reason
WHERE
   ref_stage_reason.event_status_db_id = 0 AND
   ref_stage_reason.event_status_cd    IN ('PT', 'PPAAUP')
;

--changeSet MX-14530:11 stripComments:false
DELETE FROM
   ref_event_status
WHERE
   event_status_db_id = 0 AND
   event_status_cd    IN ('PT', 'PPAAUP')
;

--changeSet MX-14530:12 stripComments:false
DELETE FROM
   ref_event_type
WHERE
   event_type_db_id = 0 AND
   event_type_cd    IN ('PT', 'PPA')
;