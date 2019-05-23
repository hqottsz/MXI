--liquibase formatted sql


--changeSet DEV-598:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ORG_CARRIER" (
    "CARRIER_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CARRIER_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CARRIER_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CARRIER_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ORG_DB_ID" Number(10,0) Check (ORG_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ORG_ID" Number(10,0) Check (ORG_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CARRIER_NAME" Varchar2 (40) NOT NULL DEFERRABLE ,
    "IATA_CD" Varchar2 (2),
    "ICAO_CD" Varchar2 (3),
    "CALLSIGN_SDESC" Varchar2 (80),
    "LOGO_BLOB" Blob,
    "LOGO_FILENAME" Varchar2 (255),
    "LOGO_CONTENT_TYPE" Varchar2 (80),
    "COUNTRY_DB_ID" Number(10,0) Check (COUNTRY_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "COUNTRY_CD" Varchar2 (8),
    "STATE_CD" Varchar2 (8),
    "AUTHORITY_DB_ID" Number(10,0) Check (AUTHORITY_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "AUTHORITY_ID" Number(10,0) Check (AUTHORITY_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ADDRESS_PMAIL" Varchar2 (80),
    "CITY_NAME" Varchar2 (40),
    "ZIP_CD" Varchar2 (10),
    "PHONE_PH" Varchar2 (20),
    "FAX_PH" Varchar2 (20),
    "ADDRESS_EMAIL" Varchar2 (40),
    "ADDRESS_PMAIL_1" Varchar2 (4000),
    "ADDRESS_PMAIL_2" Varchar2 (4000),
    "CONTACT_NAME" Varchar2 (40),
    "JOB_TITLE" Varchar2 (80),
    "EXTRN_CTRL_BOOL" Number(1,0) Default 0 Check (EXTRN_CTRL_BOOL IN (0, 1) ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_ORG_CARRIER" primary key ("CARRIER_DB_ID","CARRIER_ID") 
) 
');
END;
/

--changeSet DEV-598:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_CARRIER add (
	"EXTRN_CTRL_BOOL" Number(1,0) 
)
');
END;
/

--changeSet DEV-598:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_ORG_CARRIER DISABLE';
END;
/

--changeSet DEV-598:4 stripComments:false
UPDATE org_carrier SET extrn_ctrl_bool = 0 WHERE extrn_ctrl_bool IS NULL;

--changeSet DEV-598:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_ORG_CARRIER ENABLE';
END;
/

--changeSet DEV-598:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table ORG_CARRIER modify (
	"EXTRN_CTRL_BOOL" Number(1,0) Default 0 Check (EXTRN_CTRL_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet DEV-598:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "TASK_TASK" (
    "TASK_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TASK_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ACTV_HR_DB_ID" Number(10,0) Check (ACTV_HR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ACTV_HR_ID" Number(10,0) Check (ACTV_HR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TASK_DEFN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DEFN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TASK_DEFN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DEFN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TASK_CLASS_DB_ID" Number(10,0) Check (TASK_CLASS_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TASK_CLASS_CD" Varchar2 (16),
    "TASK_PRIORITY_DB_ID" Number(10,0) Check (TASK_PRIORITY_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TASK_PRIORITY_CD" Varchar2 (8),
    "ASSMBL_DB_ID" Number(10,0) Check (ASSMBL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ASSMBL_CD" Varchar2 (8),
    "ASSMBL_BOM_ID" Number(10,0) Check (ASSMBL_BOM_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REPL_ASSMBL_DB_ID" Number(10,0) Check (REPL_ASSMBL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REPL_ASSMBL_CD" Varchar2 (8),
    "REPL_ASSMBL_BOM_ID" Number(10,0) Check (REPL_ASSMBL_BOM_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TASK_SUBCLASS_DB_ID" Number(10,0) Check (TASK_SUBCLASS_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TASK_SUBCLASS_CD" Varchar2 (8),
    "TASK_ORIGINATOR_DB_ID" Number(10,0) Check (TASK_ORIGINATOR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TASK_ORIGINATOR_CD" Varchar2 (8),
    "TASK_DEF_STATUS_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DEF_STATUS_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TASK_DEF_STATUS_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
    "BITMAP_DB_ID" Number(10,0) Check (BITMAP_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "BITMAP_TAG" Number(10,0) Check (BITMAP_TAG BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TASK_CD" Varchar2 (200) NOT NULL DEFERRABLE ,
    "TASK_NAME" Varchar2 (200) NOT NULL DEFERRABLE ,
    "TASK_LDESC" Varchar2 (4000),
    "INSTRUCTION_LDESC" Varchar2 (4000),
    "ISSUE_ACCOUNT_DB_ID" Number(10,0) Check (ISSUE_ACCOUNT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ISSUE_ACCOUNT_ID" Number(10,0) Check (ISSUE_ACCOUNT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RECURRING_TASK_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (RECURRING_TASK_BOOL IN (0, 1) ) DEFERRABLE ,
    "RELATIVE_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (RELATIVE_BOOL IN (0, 1) ) DEFERRABLE ,
    "SCHED_FROM_RECEIVED_DT_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (SCHED_FROM_RECEIVED_DT_BOOL IN (0, 1) ) DEFERRABLE ,
    "TASK_APPL_LDESC" Varchar2 (4000),
    "TASK_APPL_SQL_LDESC" Varchar2 (4000),
    "TASK_REF_SDESC" Varchar2 (80),
    "EST_DURATION_QT" Float NOT NULL DEFERRABLE ,
    "EFFECTIVE_DT" Date,
    "EFFECTIVE_GDT" Date,
    "ROUTINE_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (ROUTINE_BOOL IN (0, 1) ) DEFERRABLE ,
    "FORECAST_RANGE_QT" Float NOT NULL DEFERRABLE ,
    "MIN_PLAN_YIELD_PCT" Float Check (MIN_PLAN_YIELD_PCT BETWEEN 0 AND 1 ) DEFERRABLE ,
    "RESOURCE_SUM_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (RESOURCE_SUM_BOOL IN (0, 1) ) DEFERRABLE ,
    "PKG_NAME" Varchar2 (18),
    "PKG_ORD" Number(10,0) Check (PKG_ORD BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "LR_FORECAST_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (LR_FORECAST_BOOL IN (0, 1) ) DEFERRABLE ,
    "REVISION_ORD" Number(4,0),
    "ACTV_DT" Date,
    "ACTV_LDESC" Varchar2 (4000),
    "ACTV_REF_SDESC" Varchar2 (240),
    "UNIQUE_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (UNIQUE_BOOL IN (0, 1) ) DEFERRABLE ,
    "WORKSCOPE_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (WORKSCOPE_BOOL IN (0, 1) ) DEFERRABLE ,
    "AUTO_COMPLETE_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (AUTO_COMPLETE_BOOL IN (0, 1) ) DEFERRABLE ,
    "LAST_SCHED_DEAD_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (LAST_SCHED_DEAD_BOOL IN (0, 1) ) DEFERRABLE ,
    "PLANNING_TYPE_DB_ID" Number(10,0) Check (PLANNING_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "PLANNING_TYPE_ID" Number(10,0) Check (PLANNING_TYPE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TASK_APPL_EFF_LDESC" Varchar2 (4000),
    "ENGINEERING_LDESC" Varchar2 (4000),
    "CANCEL_ON_AC_INST_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (CANCEL_ON_AC_INST_BOOL IN (0, 1) ) DEFERRABLE ,
    "CANCEL_ON_ANY_INST_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (CANCEL_ON_ANY_INST_BOOL IN (0, 1) ) DEFERRABLE ,
    "CREATE_ON_AC_INST_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (CREATE_ON_AC_INST_BOOL IN (0, 1) ) DEFERRABLE ,
    "CREATE_ON_ANY_INST_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (CREATE_ON_ANY_INST_BOOL IN (0, 1) ) DEFERRABLE ,
    "SOFT_DEADLINE_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (SOFT_DEADLINE_BOOL IN (0, 1) ) DEFERRABLE ,
    "EXT_KEY_SDESC" Varchar2 (80),
    "TASK_REV_REASON_DB_ID" Number(10,0) Check (TASK_REV_REASON_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TASK_REV_REASON_CD" Varchar2 (8),
    "REV_NOTE" Varchar2 (4000),
    "REV_HR_DB_ID" Number(10,0) Check (REV_HR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REV_HR_ID" Number(10,0) Check (REV_HR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REV_DT" Date,
    "BLOCK_CHAIN_SDESC" Varchar2 (200),
    "BLOCK_ORD" Number(4,0),
    "INITIAL_BLOCK_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (INITIAL_BLOCK_BOOL IN (0, 1) ) DEFERRABLE ,
    "ON_CONDITION_BOOL" Number(1,0) Default 1 NOT NULL DEFERRABLE  Check (ON_CONDITION_BOOL IN (0, 1) ) DEFERRABLE ,
    "TEMP_REV_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (TEMP_REV_BOOL IN (0, 1) ) DEFERRABLE ,
    "TEMP_REV_HR_DB_ID" Number(10,0) Check (TEMP_REV_HR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TEMP_REV_HR_ID" Number(10,0) Check (TEMP_REV_HR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TEMP_REV_DT" Date,
    "LOCKED_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (LOCKED_BOOL IN (0, 1) ) DEFERRABLE ,
    "LOCKED_HR_DB_ID" Number(10,0) Check (LOCKED_HR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "LOCKED_HR_ID" Number(10,0) Check (LOCKED_HR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "LOCKED_DT" Date,
    "ETOPS_BOOL" Number(1,0) Default 0 Check (ETOPS_BOOL IN (0, 1) ) DEFERRABLE ,
    "RESCHED_FROM_DB_ID" Number(10,0),
    "RESCHED_FROM_CD" Varchar2 (8),
    "ENG_CONTACT_HR_DB_ID" Number(10,0) Check (ENG_CONTACT_HR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ENG_CONTACT_HR_ID" Number(10,0) Check (ENG_CONTACT_HR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "APPROVED_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (APPROVED_BOOL IN (0, 1) ) DEFERRABLE ,
    "MIN_USAGE_RELEASE_PCT" Float Default 0 NOT NULL DEFERRABLE ,
    "ORG_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ORG_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ORG_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ORG_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ENFORCE_WORKSCOPE_BOOL" Number(1,0) Default 0 Check (ENFORCE_WORKSCOPE_BOOL IN (0, 1) ) DEFERRABLE ,
    "TASK_MUST_REMOVE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_MUST_REMOVE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TASK_MUST_REMOVE_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
    "SCHED_UNKNOWN_BOOL" Number(1,0) Default 0 Check (SCHED_UNKNOWN_BOOL IN (0, 1) ) DEFERRABLE ,
 Constraint "PK_TASK_TASK" primary key ("TASK_DB_ID","TASK_ID") 
) 
');
END;
/

--changeSet DEV-598:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table TASK_TASK add (
	"SCHED_UNKNOWN_BOOL" Number(1,0) 
)
');
END;
/

--changeSet DEV-598:9 stripComments:false
UPDATE TASK_TASK SET SCHED_UNKNOWN_BOOL = 0 WHERE SCHED_UNKNOWN_BOOL IS NULL;

--changeSet DEV-598:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table TASK_TASK modify (
	"SCHED_UNKNOWN_BOOL" Number(1,0) Default 0 Check (SCHED_UNKNOWN_BOOL IN (0, 1) ) DEFERRABLE 
)
');
END;
/

--changeSet DEV-598:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "SCHED_LABOUR_EXT_PART" (
    "LABOUR_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LABOUR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "LABOUR_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LABOUR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "SCHED_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SCHED_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "SCHED_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SCHED_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "SCHED_EXT_PART_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SCHED_EXT_PART_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_SCHED_LABOUR_EXT_PART" primary key ("LABOUR_DB_ID","LABOUR_ID","SCHED_DB_ID","SCHED_ID","SCHED_EXT_PART_ID") 
) 
');
END;
/

--changeSet DEV-598:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "SCHED_EXT_PART" (
    "SCHED_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SCHED_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "SCHED_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SCHED_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "SCHED_EXT_PART_ID" Number(10,0) NOT NULL DEFERRABLE  UNIQUE DEFERRABLE  Check (SCHED_EXT_PART_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "SCHED_SDESC" Varchar2 (80) NOT NULL DEFERRABLE,
    "RMV_PART_NO" Varchar2 (40),
    "RMV_SERIAL_BATCH_NO" Varchar2 (40),
    "RMV_QT" Float,
    "INST_PART_NO" Varchar2 (40),
    "INST_SERIAL_BATCH_NO" Varchar2 (40),
    "INST_QT" Float,
    "INST_SERVICE_TAG_SDESC" Varchar2 (80),
    "NOTES" Varchar2 (200),
    "RSTAT_CD" Number(3,0) NOT NULL  DEFERRABLE Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_SCHED_EXT_PART" primary key ("SCHED_DB_ID","SCHED_ID","SCHED_EXT_PART_ID") 
) 
');
END;
/

--changeSet DEV-598:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_EXT_PART" add Constraint "FK_SCHEDSTASK_SCHEDEXTPART" foreign key ("SCHED_DB_ID","SCHED_ID") references "SCHED_STASK" ("SCHED_DB_ID","SCHED_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-598:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_EXT_PART" add Constraint "FK_MIMRSTAT_SCHEDEXTPART" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-598:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_LABOUR_EXT_PART" add Constraint "FK_MIMRSTAT_SCHEDLABEXTPART" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-598:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_LABOUR_EXT_PART" add Constraint "FK_SCHEDLAB_SCHEDLABEXTPART" foreign key ("LABOUR_DB_ID","LABOUR_ID") references "SCHED_LABOUR" ("LABOUR_DB_ID","LABOUR_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-598:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_LABOUR_EXT_PART" add Constraint "FK_SCHEDEXTPART_SCHEDLABEXT" foreign key ("SCHED_DB_ID","SCHED_ID","SCHED_EXT_PART_ID") references "SCHED_EXT_PART" ("SCHED_DB_ID","SCHED_ID","SCHED_EXT_PART_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-598:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SCHED_EXT_PART" BEFORE INSERT
   ON "SCHED_EXT_PART" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-598:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SCHED_EXT_PART" BEFORE UPDATE
   ON "SCHED_EXT_PART" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-598:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SCHED_LABOUR_EXT_PART" BEFORE INSERT
   ON "SCHED_LABOUR_EXT_PART" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-598:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SCHED_LABOUR_EXT_PART" BEFORE UPDATE
   ON "SCHED_LABOUR_EXT_PART" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-598:22 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'SCHED_EXT_PART_ID_SEQ', 100000, 'SCHED_EXT_PART', 'SCHED_EXT_PART_ID' , 1, 0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'SCHED_EXT_PART_ID_SEQ');   

--changeSet DEV-598:23 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('SCHED_EXT_PART_ID_SEQ', 1);   
END;
/   