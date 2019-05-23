--liquibase formatted sql
SET SERVEROUTPUT ON SIZE UNLIMITEDSET TIMING ON

--changeSet DEV-1904:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- DEV1904: START OF SCRIPT
-- DEV1904: UPDATE EVT_EVENT.EVENT_LDESC
DECLARE

   -- declare variables for the audit columns
   l_rev_db_id EVT_EVENT.REVISION_DB_ID%TYPE;
   l_rev_user  EVT_EVENT.REVISION_USER%TYPE;

BEGIN

   UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_BEGIN('EVT_EVENT');

   --get the db_id and user for the audit columns
   SELECT
      db_id, user
   INTO
      l_rev_db_id, l_rev_user
   FROM
      mim_local_db;

   UPDATE /*+ parallel index(evt_event ix_evt_event_typestatus) */
      evt_event
   SET
      --replace html tags if exists
      evt_event.event_ldesc = REPLACE(
                                 REPLACE(
                                    REPLACE(
                                       REPLACE(
                                          REPLACE(
                                             evt_event.event_ldesc
                                          ,'<b>', '')
                                       ,'</b>', '')
                                    ,'<u>', '')
                                 ,'</u>', '')
                              ,'<br>', chr(13)),
      evt_event.revision_dt = SYSDATE,
      evt_event.revision_db_id = l_rev_db_id,
      evt_event.revision_user = l_rev_user
   WHERE
      --only faults with long description set
      evt_event.event_type_db_id = 0 AND
      evt_event.event_type_cd    = 'CF'
      AND
      evt_event.event_ldesc IS NOT NULL;

   --log the number of rows that were updated by the previous SQL statement
   DBMS_OUTPUT.PUT_LINE('INFO: Updated ' || SQL%ROWCOUNT ||
                        ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) ||
                        ' in the evt_event table.');

   -- No need to validate constraints since we're only updating the ldesc and audit columns
   UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_END('EVT_EVENT', FALSE);

END;
/

--changeSet DEV-1904:2 stripComments:false
-- DEV1904: INSERT INTO EVT_STAGE
ALTER TRIGGER tibr_evt_stage DISABLE;

--changeSet DEV-1904:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE

   -- declare variables for the audit columns
   l_rev_db_id EVT_EVENT.REVISION_DB_ID%TYPE;
   l_rev_user  EVT_EVENT.REVISION_USER%TYPE;

BEGIN

   --get the db_id and user for the audit columns
   SELECT
      db_id, user
   INTO
      l_rev_db_id, l_rev_user
   FROM
      mim_local_db;

   --insert all the evt_event.event_ldesc that are not null and evt_event.event_type_cd = 'CF'
   --it is a mandatory requisite to insert those values in evt_stage, since they were not originally saved.
   INSERT INTO
      evt_stage
   (
      event_db_id,
      event_id,
      stage_id,
      event_status_db_id,
      event_status_cd,
      hr_db_id,
      hr_id,
      stage_reason_db_id,
      stage_reason_cd,
      stage_dt,
      stage_gdt,
      user_stage_note,
      system_bool,
      rstat_cd,
      creation_dt,
      revision_dt,
      revision_db_id,
      revision_user
   )
   SELECT /*+ index(evt_event ix_evt_event_typestatus) */
      evt_event.event_db_id,
      evt_event.event_id,
      EVT_STAGE_ID_SEQ.nextval,
      evt_event.event_status_db_id,
      evt_event.event_status_cd,
      0, -- system Hr db id
      3, -- system Hr id
      NULL,
      NULL,
      SYSDATE,
      SYSDATE,
      evt_event.event_ldesc,
      1,
      0,
      SYSDATE,
      SYSDATE,
      l_rev_db_id,
      l_rev_user
   FROM
      evt_event
   WHERE
      -- only faults with long description set
      evt_event.event_type_db_id = 0 AND
      evt_event.event_type_cd    = 'CF'
      AND
      evt_event.event_ldesc IS NOT NULL;

   -- Log the number of rows that were updated by the previous SQL statement
   DBMS_OUTPUT.PUT_LINE('INFO: Inserted ' || SQL%ROWCOUNT ||
                        ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) ||
                        ' in the evt_stage table.');

END;
/

--changeSet DEV-1904:4 stripComments:false
ALTER TRIGGER tibr_evt_stage ENABLE;
