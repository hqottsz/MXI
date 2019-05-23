--liquibase formatted sql
 

--changeSet DEV-159:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Commented out due to changes that have already been applied in 7.2.0.0-SP1
-- BEGIN
--  utl_migr_schema_pkg.table_column_modify('
--  Alter table EQP_TASK_PANEL modify (
--    "LABOUR_SKILL_DB_ID" Number(10,0) Check (LABOUR_SKILL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
--  )
--  ')
-- END
-- 
-- Commented out due to changes that have already been applied in 7.2.0.0-SP1
-- BEGIN
--  utl_migr_schema_pkg.table_column_modify('
--  Alter table EQP_TASK_PANEL modify (
--    "LABOUR_SKILL_CD" Varchar2 (8)
--  )
--  ')
-- END
-- 
 BEGIN
 utl_migr_schema_pkg.table_create('
 Create table "REF_RESULT_EVENT" (
 	"RESULT_EVENT_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (RESULT_EVENT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
 	"RESULT_EVENT_CD" Varchar2 (16) NOT NULL DEFERRABLE  UNIQUE DEFERRABLE ,
 	"USER_CD" Varchar2 (16) NOT NULL DEFERRABLE  UNIQUE DEFERRABLE ,
 	"DESC_SDESC" Varchar2 (80),
 	"DESC_LDESC" Varchar2 (4000),
 	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
 	"CREATION_DT" Date NOT NULL DEFERRABLE ,
 	"REVISION_DT" Date NOT NULL DEFERRABLE ,
 	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
 	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
  Constraint "PK_REF_RESULT_EVENT" primary key ("RESULT_EVENT_DB_ID","RESULT_EVENT_CD") 
) 

 ');
 END;
 / 

--changeSet DEV-159:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.table_create('
Create table "SD_FAULT_RESULT" (
	"FAULT_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FAULT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"FAULT_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FAULT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RESULT_EVENT_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (RESULT_EVENT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RESULT_EVENT_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_SD_FAULT_RESULT" primary key ("FAULT_DB_ID","FAULT_ID","RESULT_EVENT_DB_ID","RESULT_EVENT_CD") 
) 
 ');
 END;
/

--changeSet DEV-159:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SD_FAULT_RESULT" add Constraint "FK_SDFAULT_SDFAULTRESULT" foreign key ("FAULT_DB_ID","FAULT_ID") references "SD_FAULT" ("FAULT_DB_ID","FAULT_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-159:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SD_FAULT_RESULT" add Constraint "FK_MIMRSTAT_SDFAULTRESULT" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-159:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_RESULT_EVENT" add Constraint "FK_MIMRSTAT_REFRESULTEVENT" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-159:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SD_FAULT_RESULT" add Constraint "SD_REFRSLTEVENT_SDFAULTRSLT" foreign key ("RESULT_EVENT_DB_ID","RESULT_EVENT_CD") references "REF_RESULT_EVENT" ("RESULT_EVENT_DB_ID","RESULT_EVENT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-159:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SD_FAULT_RESULT" BEFORE INSERT
   ON "SD_FAULT_RESULT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-159:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SD_FAULT_RESULT" BEFORE UPDATE
   ON "SD_FAULT_RESULT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-159:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_RESULT_EVENT" BEFORE INSERT
   ON "REF_RESULT_EVENT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-159:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_RESULT_EVENT" BEFORE UPDATE
   ON "REF_RESULT_EVENT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-159:11 stripComments:false
INSERT INTO
   ref_result_event
   (
      result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0,'ABT','ABT','Aborted Approach','The approach was aborted.',0, TO_DATE('2010-03-13', 'YYYY-MM-DD'), TO_DATE('2010-01-13', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_result_event WHERE result_event_db_id=0 and result_event_cd='ABT' );          

--changeSet DEV-159:12 stripComments:false
INSERT INTO
   ref_result_event
   (
      result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0,'ATB','ATB','Air Turn Back','The aircraft made an air-turn-back.',0, TO_DATE('2010-03-13', 'YYYY-MM-DD'), TO_DATE('2010-01-13', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_result_event WHERE result_event_db_id=0 and result_event_cd='ATB' ); 

--changeSet DEV-159:13 stripComments:false
INSERT INTO
   ref_result_event
   (
      result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0,'CNX','CNX','Cancellation','The flight was cancelled.',0, TO_DATE('2010-03-13', 'YYYY-MM-DD'), TO_DATE('2010-01-13', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_result_event WHERE result_event_db_id=0 and result_event_cd='CNX' ); 

--changeSet DEV-159:14 stripComments:false
INSERT INTO
   ref_result_event
   (
      result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0,'DIV','DIV','Diversion','The aircraft was diverted to a different destination.',0, TO_DATE('2010-03-13', 'YYYY-MM-DD'), TO_DATE('2010-01-13', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_result_event WHERE result_event_db_id=0 and result_event_cd='DIV' );       

--changeSet DEV-159:15 stripComments:false
INSERT INTO
   ref_result_event
   (
      result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0,'DLY','DLY','Delay','The aircraft was delayed.',0, TO_DATE('2010-03-13', 'YYYY-MM-DD'), TO_DATE('2010-01-13', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_result_event WHERE result_event_db_id=0 and result_event_cd='DLY' ); 

--changeSet DEV-159:16 stripComments:false
INSERT INTO
   ref_result_event
   (
      result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0,'EMD','EMD','Emergency Descent','The aircraft made an emergency descent.',0, TO_DATE('2010-03-13', 'YYYY-MM-DD'), TO_DATE('2010-01-13', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_result_event WHERE result_event_db_id=0 and result_event_cd='EMD' ); 

--changeSet DEV-159:17 stripComments:false
INSERT INTO
   ref_result_event
   (
      result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0,'GTB','GTB','Ground Turn Back','The aircraft made a ground-turn-back.',0, TO_DATE('2010-03-13', 'YYYY-MM-DD'), TO_DATE('2010-01-13', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_result_event WHERE result_event_db_id=0 and result_event_cd='GTB' );       

--changeSet DEV-159:18 stripComments:false
INSERT INTO
   ref_result_event
   (
      result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0,'IFD','IFD','In-Flight Shut Down','There was an in-flight shut down of a power plant.',0, TO_DATE('2010-03-13', 'YYYY-MM-DD'), TO_DATE('2010-01-13', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_result_event WHERE result_event_db_id=0 and result_event_cd='IFD' ); 

--changeSet DEV-159:19 stripComments:false
INSERT INTO
   ref_result_event
   (
      result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0,'RTO','RTO','Aborted Take Off','The take-off was aborted.',0, TO_DATE('2010-03-13', 'YYYY-MM-DD'), TO_DATE('2010-01-13', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_result_event WHERE result_event_db_id=0 and result_event_cd='RTO' ); 

--changeSet DEV-159:20 stripComments:false
INSERT INTO
   ref_result_event
   (
      result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0,'TII','TII','Technical Incident','There was a technical incident.',0, TO_DATE('2010-03-13', 'YYYY-MM-DD'), TO_DATE('2010-01-13', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_result_event WHERE result_event_db_id=0 and result_event_cd='TII' );             

--changeSet DEV-159:21 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('EQP_TASK_PANEL', 'FK_IETMTOPIC_EQPTASKPANEL');      
END;
/

--changeSet DEV-159:22 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('EQP_TASK_PANEL', 'FK_REFLABOURSKILL_EQPTASKPANEL');      
END;
/

--changeSet DEV-159:23 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('EQP_TASK_PANEL', 'IETM_DB_ID');
END;
/

--changeSet DEV-159:24 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('EQP_TASK_PANEL', 'IETM_ID');
END;
/

--changeSet DEV-159:25 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('EQP_TASK_PANEL', 'IETM_TOPIC_ID');
END;
/

--changeSet DEV-159:26 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('EQP_TASK_PANEL', 'LABOUR_SKILL_DB_ID');
END;
/

--changeSet DEV-159:27 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('EQP_TASK_PANEL', 'LABOUR_SKILL_CD');
END;
/

--changeSet DEV-159:28 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('EQP_TASK_PANEL', 'ON_HOURS');
END;
/

--changeSet DEV-159:29 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('EQP_TASK_PANEL', 'OFF_HOURS');
END;
/      