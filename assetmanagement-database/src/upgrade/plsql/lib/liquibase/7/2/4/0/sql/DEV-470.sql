--liquibase formatted sql


--changeSet DEV-470:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "MAINT_PRGM_LOG" (
	"MAINT_PRGM_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (MAINT_PRGM_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"MAINT_PRGM_ID" Number(10,0) NOT NULL DEFERRABLE  Check (MAINT_PRGM_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"MAINT_LOG_ID" Number(10,0) NOT NULL DEFERRABLE  UNIQUE DEFERRABLE  Check (MAINT_LOG_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"HR_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (HR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"HR_ID" Number(10,0) NOT NULL DEFERRABLE  Check (HR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LOG_DT" Date NOT NULL DEFERRABLE ,
	"LOG_ACTION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LOG_ACTION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LOG_ACTION_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"LOG_REASON_DB_ID" Number(10,0) Check (LOG_REASON_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LOG_REASON_CD" Varchar2 (16),
	"USER_NOTE" Varchar2 (4000),
	"SYSTEM_NOTE" Varchar2 (4000),
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_MAINT_PRGM_LOG" primary key ("MAINT_PRGM_DB_ID","MAINT_PRGM_ID","MAINT_LOG_ID") 
) 
');
END;
/

--changeSet DEV-470:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "MAINT_PRGM_LOG" add Constraint "FK_ORGHR_MAINTPRGMLOG" foreign key ("HR_DB_ID","HR_ID") references "ORG_HR" ("HR_DB_ID","HR_ID")  DEFERRABLE

');
END;
/

--changeSet DEV-470:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "MAINT_PRGM_LOG" add Constraint "FK_MIMRSTAT_MAINTPRGMLOG" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-470:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "MAINT_PRGM_LOG" add Constraint "FK_MAINTPRGM_MAINTPRGMLOG" foreign key ("MAINT_PRGM_DB_ID","MAINT_PRGM_ID") references "MAINT_PRGM" ("MAINT_PRGM_DB_ID","MAINT_PRGM_ID")  DEFERRABLE

');
END;
/

--changeSet DEV-470:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "MAINT_PRGM_LOG" add Constraint "FK_REFLOGREASON_MAINTPRGMLOG" foreign key ("LOG_REASON_DB_ID","LOG_REASON_CD") references "REF_LOG_REASON" ("LOG_REASON_DB_ID","LOG_REASON_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-470:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "MAINT_PRGM_LOG" add Constraint "FK_REFLOGACTION_MAINTPRGMLOG" foreign key ("LOG_ACTION_DB_ID","LOG_ACTION_CD") references "REF_LOG_ACTION" ("LOG_ACTION_DB_ID","LOG_ACTION_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-470:7 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Migration script for 1012 AFKLM concept (section 3.2)
-- Author: Karan Mehta
-- Date:   9162010
-- Objectives:
-- 1) Create an entry in the MAINT_PRGM_LOG table for each 
--    Maintenance Program, based on the current state of
--    the maintenance program. Basically, for any maintenance
--    program that is not in the ACTV or SUPERSEDE status, and
--    that is locked, this migration script creates a row in the
--    MAINT_PRGM_LOG table to indicate who locked the maintenace
--    program and when.
-- 2) Drop the obsolete columns from MAINT_PRGM table
-- Note: This script does NOT create a row in the MAINT_PRGM_LOG
--       table if that table already has rows for a maintenance
--       program where the LOG_ACTION_CD of the row with the 
--       highest MAINT_LOG_ID is MP_LOCK, i.e., this script is 
--       conditional.
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
INSERT INTO
  maint_prgm_log (
    maint_prgm_db_id,
    maint_prgm_id,
    maint_log_id,
    hr_db_id,
    hr_id,
    log_dt,
    log_action_db_id,
    log_action_cd,
    log_reason_db_id,
    log_reason_cd,
    user_note,
    system_note
  )
SELECT
  mp.maint_prgm_db_id,
  mp.maint_prgm_id,
  GetSequenceNextvalue( 'MAINT_PRGM_LOG_SEQ' ),
  mp.locked_hr_db_id,
  mp.locked_hr_id,
  mp.locked_dt,
  refact.log_action_db_id,
  refact.log_action_cd,
  null,
  null,
  null,
  'Locked by ' || uuser.first_name || ' ' || uuser.last_name || '.'
FROM
  maint_prgm mp,
  ref_log_action refact,
  org_hr hr,
  utl_user uuser,
  ref_maint_prgm_status refmpstat
WHERE
  mp.locked_bool = 1
  AND
  refact.log_action_cd = 'MP_LOCK'
  AND
  hr.hr_db_id = mp.locked_hr_db_id AND
  hr.hr_id = mp.locked_hr_id
  AND
  hr.user_id = uuser.user_id
  AND
  mp.maint_prgm_status_db_id = refmpstat.maint_prgm_status_db_id AND
  mp.maint_prgm_status_cd = refmpstat.maint_prgm_status_cd
  AND
  -- User is not able to lock a maintenance program that is in ACTV or SUPRSEDE status
  ( refmpstat.maint_prgm_status_cd != 'ACTV' AND refmpstat.maint_prgm_status_cd != 'SUPRSEDE' )
  AND
  -- Do NOT create a row in the MAINT_PRGM_LOG table if that table already has rows for a maintenance
  -- program where the LOG_ACTION_CD of the row with the max LOG_DT is MP_LOCK, i.e., this script is 
  -- conditional.
  NOT EXISTS (
    SELECT
      mplog2.maint_prgm_db_id,
      mplog2.maint_prgm_id
    FROM
      maint_prgm mp2,
      maint_prgm_log mplog2,
      ref_log_action refact2
    WHERE
      mp2.maint_prgm_db_id = mplog2.maint_prgm_db_id AND
      mp2.maint_prgm_id = mplog2.maint_prgm_id 
      AND
      mplog2.log_action_db_id = refact2.log_action_db_id AND
      mplog2.log_action_cd = refact2.log_action_cd
      AND
      refact2.log_action_cd = 'MP_LOCK'
      AND
      mplog2.log_dt = ( SELECT MAX( mplog3.log_dt ) FROM maint_prgm_log mplog3 WHERE mplog2.maint_prgm_db_id = mplog3.maint_prgm_db_id AND mplog2.maint_prgm_id = mplog3.maint_prgm_id )
      AND
      mp2.rstat_cd = 0
      AND
      mplog2.rstat_cd = 0
      AND
      refact2.rstat_cd = 0
  )
  AND
  hr.rstat_cd = 0
  AND
  uuser.rstat_cd = 0
  AND
  refact.rstat_cd = 0
  AND
  refmpstat.rstat_cd = 0
  AND
  mp.rstat_cd = 0;

--changeSet DEV-470:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop the now obsolete columns LOCKED_HR_DB_ID, LOCKED_HR_ID and LOCKED_DT from the MAINT_PRGM table
BEGIN
utl_migr_schema_pkg.table_constraint_drop('MAINT_PRGM', 'FK_ORGHR_MAINTPRGMLOCK');
utl_migr_schema_pkg.table_column_drop('MAINT_PRGM', 'LOCKED_HR_DB_ID');
utl_migr_schema_pkg.table_column_drop('MAINT_PRGM', 'LOCKED_HR_ID');
utl_migr_schema_pkg.table_column_drop('MAINT_PRGM', 'LOCKED_DT');
END;
/

--changeSet DEV-470:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_MAINT_PRGM_LOG" BEFORE INSERT
   ON "MAINT_PRGM_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-470:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_MAINT_PRGM_LOG" BEFORE UPDATE
   ON "MAINT_PRGM_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/