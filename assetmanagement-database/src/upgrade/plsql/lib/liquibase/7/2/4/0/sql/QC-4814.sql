--liquibase formatted sql


--changeSet QC-4814:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBR_REF_MATERIAL_REQ_STATUS');
END;
/

--changeSet QC-4814:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TUBR_REF_MATERIAL_REQ_STATUS');
END;
/

--changeSet QC-4814:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Commenting out because changes were applied in DEV-459 in 7.1-SP4
-- exec utl_migr_schema_pkg.table_constraint_drop('REF_MATERIAL_REQ_STATUS', 'PK_REF_MATERIAL_REQ_STATUS')
-- exec utl_migr_schema_pkg.index_drop('PK_REF_MATERIAL_REQ_STATUS')
-- exec utl_migr_schema_pkg.table_constraint_drop('REF_MATERIALS_REQUEST_STATUS', 'FK_MIMRSTAT_REFMATREQSTATUS')
-- exec utl_migr_schema_pkg.table_rename('REF_MATERIALS_REQUEST_STATUS', 'REF_MATERIAL_REQ_STATUS')
BEGIN
utl_migr_schema_pkg.table_create('

Create table "REF_MATERIAL_REQ_STATUS" (
	"REQUEST_STATUS_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REQUEST_STATUS_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REQUEST_STATUS_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"USER_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"REQUEST_STATUS_SDESC" Varchar2 (80),
	"REQUEST_STATUS_LDESC" Varchar2 (4000),
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_REF_MATERIAL_REQ_STATUS" primary key ("REQUEST_STATUS_DB_ID","REQUEST_STATUS_CD") 
)
');
END;
/

--changeSet QC-4814:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_MATERIAL_REQ_STATUS" add Constraint "FK_MIMRSTAT_REFMATREQSTATUS" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet QC-4814:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_MAT_REQ_STATUS" BEFORE INSERT
   ON "REF_MATERIAL_REQ_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet QC-4814:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_MAT_REQ_STATUS" BEFORE UPDATE
   ON "REF_MATERIAL_REQ_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet QC-4814:7 stripComments:false
INSERT INTO
   REF_MATERIAL_REQ_STATUS
   (
      request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0, 'READY', 'READY', 'All requested parts for this task and subtasks are available.', 'Indicates that all the requested parts for this task and subtasks are available.',0, TO_DATE('2010-06-29', 'YYYY-MM-DD'), TO_DATE('2010-06-29', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_MATERIAL_REQ_STATUS WHERE request_status_db_id = 0 and request_status_cd = 'READY' );         

--changeSet QC-4814:8 stripComments:false
INSERT INTO
   REF_MATERIAL_REQ_STATUS
   (
      request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0, 'NOT READY', 'NOT READY', 'One/more requested parts for the task/subtask are not available.', 'Indicates that one or more requested parts for this task and subtasks are not available.',0, TO_DATE('2010-06-29', 'YYYY-MM-DD'), TO_DATE('2010-06-29', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_MATERIAL_REQ_STATUS WHERE request_status_db_id = 0 and request_status_cd = 'NOT READY' );         

--changeSet QC-4814:9 stripComments:false
INSERT INTO
   REF_MATERIAL_REQ_STATUS
   (
      request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0, 'AWAITING', 'AWAITING', 'One/more requested parts for the task/subtask has an expected time of arrival.', 'Indicates that one or more requested parts for the task/subtask has an expected time of arrival.  Used for internal logic and not for display purposes.',0, TO_DATE('2010-06-29', 'YYYY-MM-DD'), TO_DATE('2010-06-29', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_MATERIAL_REQ_STATUS WHERE request_status_db_id = 0 and request_status_cd = 'AWAITING' );         

--changeSet QC-4814:10 stripComments:false
INSERT INTO
   REF_MATERIAL_REQ_STATUS
   (
      request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0, 'NOT KNOWN', 'N/A', 'The status of part request is not known.', 'Indicates that the status of part request is not known.',0, TO_DATE('2010-06-29', 'YYYY-MM-DD'), TO_DATE('2010-06-29', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_MATERIAL_REQ_STATUS WHERE request_status_db_id = 0 and request_status_cd = 'NOT KNOWN' );         

--changeSet QC-4814:11 stripComments:false
INSERT INTO
   REF_MATERIAL_REQ_STATUS
   (
      request_status_db_id,request_status_cd,user_cd,request_status_sdesc,request_status_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0, 'N/A', 'N/A', 'Material request status not applicable for the task.', 'Indicates that material request status is not applicable for the task.',0, TO_DATE('2010-06-29', 'YYYY-MM-DD'), TO_DATE('2010-06-29', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_MATERIAL_REQ_STATUS WHERE request_status_db_id = 0 and request_status_cd = 'N/A' );                     