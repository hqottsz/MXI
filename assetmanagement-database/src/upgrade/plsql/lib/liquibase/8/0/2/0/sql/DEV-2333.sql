--liquibase formatted sql


--changeSet DEV-2333:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- dropping the FK ref, sched_from_db_id and sched_from_cd from task_task table (Note: this clean-up is only for internal purpose)
BEGIN  
   utl_migr_schema_pkg.table_constraint_drop('TASK_TASK', 'FK_REFTASKSCHEDFROM_TASKTASK');
END;
/

--changeSet DEV-2333:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN  
   utl_migr_schema_pkg.table_column_drop('TASK_TASK', 'SCHED_FROM_DB_ID');
END;
/

--changeSet DEV-2333:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN  
   utl_migr_schema_pkg.table_column_drop('TASK_TASK', 'SCHED_FROM_CD');
END;
/

--changeSet DEV-2333:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- dropping the ref-term table
BEGIN  
   utl_migr_schema_pkg.table_drop('REF_TASK_SCHED_FROM');
END;
/

--changeSet DEV-2333:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- create the ref-table
BEGIN
   utl_migr_schema_pkg.table_create('
    Create table "REF_TASK_SCHED_FROM" (
	"TASK_SCHED_FROM_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_SCHED_FROM_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TASK_SCHED_FROM_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"DISPLAY_CODE" Varchar2 (16) NOT NULL DEFERRABLE ,
	"DISPLAY_NAME" Varchar2 (80) NOT NULL DEFERRABLE ,
	"DISPLAY_DESC" Varchar2 (4000) NOT NULL DEFERRABLE ,
	"DISPLAY_ORD" Number(5,0) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_REF_TASK_SCHED_FROM" primary key ("TASK_SCHED_FROM_DB_ID","TASK_SCHED_FROM_CD") 
)
');
END;
/

--changeSet DEV-2333:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "REF_TASK_SCHED_FROM" add Constraint "FK_MIMDB_TASKSCHEDFROM" foreign key ("TASK_SCHED_FROM_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet DEV-2333:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "REF_TASK_SCHED_FROM" add Constraint "FK_RVMIMDB_TASKSCHEDFROM" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet DEV-2333:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "REF_TASK_SCHED_FROM" add Constraint "FK_MIMRSTAT_TASKSCHEDFROM" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
   ');
END;
/

--changeSet DEV-2333:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
	Alter table "TASK_TASK" add (
	"TASK_SCHED_FROM_DB_ID" Number(10,0) Check (TASK_SCHED_FROM_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-2333:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
	Alter table "TASK_TASK" add (
	"TASK_SCHED_FROM_CD" Varchar2 (16)
)
');
END;
/

--changeSet DEV-2333:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- audit triggers for REF_TASK_SCHED_FROM table
CREATE OR REPLACE TRIGGER "TIBR_REF_TASK_SCHED_FROM" BEFORE INSERT
   ON "REF_TASK_SCHED_FROM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
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
END;
/

--changeSet DEV-2333:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_TASK_SCHED_FROM" BEFORE UPDATE
   ON "REF_TASK_SCHED_FROM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
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
END;
/

--changeSet DEV-2333:13 stripComments:false
-- insert 0-level data in REF_TASK_SCHED_FROM table
INSERT INTO REF_TASK_SCHED_FROM
   ( 
     TASK_SCHED_FROM_DB_ID, TASK_SCHED_FROM_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
SELECT 0, 'EFFECTIVE_DT', 'EFFECTIVE_DT', 'Effective Date', 'Schedule from the effective date of the task definition', 10, 0, TO_DATE('2013-07-19', 'YYYY-MM-DD'), TO_DATE('2013-07-19', 'YYYY-MM-DD'), 0, 'MXI' 
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM REF_TASK_SCHED_FROM WHERE TASK_SCHED_FROM_DB_ID = 0 AND TASK_SCHED_FROM_CD = 'EFFECTIVE_DT' );   

--changeSet DEV-2333:14 stripComments:false
INSERT INTO REF_TASK_SCHED_FROM
   ( 
     TASK_SCHED_FROM_DB_ID, TASK_SCHED_FROM_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
SELECT 0, 'MP_ACTV_DT', 'MP_ACTV_DT', 'Maintenance Program Activation Date', 'Schedule from the maintenance program activation date', 20, 0, TO_DATE('2013-07-19', 'YYYY-MM-DD'), TO_DATE('2013-07-19', 'YYYY-MM-DD'), 0, 'MXI' 
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM REF_TASK_SCHED_FROM WHERE TASK_SCHED_FROM_DB_ID = 0 AND TASK_SCHED_FROM_CD = 'MP_ACTV_DT' );  

--changeSet DEV-2333:15 stripComments:false
INSERT INTO REF_TASK_SCHED_FROM
   ( 
     TASK_SCHED_FROM_DB_ID, TASK_SCHED_FROM_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
SELECT 0, 'MANUFACT_DT', 'MANUFACT_DT', 'Manufactured Date', 'Schedule from the manufactured date', 30, 0, TO_DATE('2013-07-19', 'YYYY-MM-DD'), TO_DATE('2013-07-19', 'YYYY-MM-DD'), 0, 'MXI' 
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM REF_TASK_SCHED_FROM WHERE TASK_SCHED_FROM_DB_ID = 0 AND TASK_SCHED_FROM_CD = 'MANUFACT_DT' );   

--changeSet DEV-2333:16 stripComments:false
INSERT INTO REF_TASK_SCHED_FROM
   ( 
     TASK_SCHED_FROM_DB_ID, TASK_SCHED_FROM_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER 
   )
SELECT 0, 'RECEIVED_DT', 'RECEIVED_DT', 'Received Date', 'Schedule from the received date', 40, 0, TO_DATE('2013-07-19', 'YYYY-MM-DD'), TO_DATE('2013-07-19', 'YYYY-MM-DD'), 0, 'MXI' 
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM REF_TASK_SCHED_FROM WHERE TASK_SCHED_FROM_DB_ID = 0 AND TASK_SCHED_FROM_CD = 'RECEIVED_DT' );   

--changeSet DEV-2333:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add foreign key reference
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "TASK_TASK" add Constraint "FK_REFTASKSCHEDFROM_TASKTASK" foreign key ("TASK_SCHED_FROM_DB_ID","TASK_SCHED_FROM_CD") references "REF_TASK_SCHED_FROM" ("TASK_SCHED_FROM_DB_ID","TASK_SCHED_FROM_CD")  DEFERRABLE
   ');
END;
/

--changeSet DEV-2333:18 stripComments:false
-- data migration for task_task table to populate the TASK_SCHED_FROM_DB_ID and TASK_SCHED_FROM_CD columns
UPDATE task_task 
SET task_sched_from_db_id = 0, 
  task_sched_from_cd = CASE WHEN task_task.relative_bool = 1 THEN 'EFFECTIVE_DT'
                       WHEN task_task.sched_from_received_dt_bool = 1 THEN 'RECEIVED_DT'
                       ELSE 'MANUFACT_DT'
                       END
WHERE task_sched_from_db_id IS NULL;