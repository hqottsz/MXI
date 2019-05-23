--liquibase formatted sql


--changeSet RND-2222:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Migrations changes to the TASK_TASK table moved into DEV-92.sql
BEGIN
utl_migr_schema_pkg.table_create('
Create table "EQP_PLANNING_TYPE" (
	"PLANNING_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PLANNING_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"PLANNING_TYPE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PLANNING_TYPE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"ASSMBL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ASSMBL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"ASSMBL_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"PLANNING_TYPE_CD" Varchar2 (80) NOT NULL DEFERRABLE ,
	"DESC_SDESC" Varchar2 (200) NOT NULL DEFERRABLE ,
	"DESC_LDESC" Varchar2 (4000),
	"NR_FACTOR" Number(3,2) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_EQP_PLANNING_TYPE" primary key ("PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID") 
) 
');
END;
/

--changeSet RND-2222:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "EQP_PLANNING_TYPE_SKILL" (
	"PLANNING_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PLANNING_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"PLANNING_TYPE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PLANNING_TYPE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LABOUR_SKILL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LABOUR_SKILL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LABOUR_SKILL_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"EFFORT_PCT" Float NOT NULL DEFERRABLE  Check (EFFORT_PCT BETWEEN 0 AND 1 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_EQP_PLANNING_TYPE_SKILL" primary key ("PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID","LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD") 
) 
');
END;
/

--changeSet RND-2222:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "TASK_PLANNING_TYPE_SKILL" (
	"TASK_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TASK_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LABOUR_SKILL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LABOUR_SKILL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LABOUR_SKILL_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"EFFORT_HR" Number(6,2) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_TASK_PLANNING_TYPE_SKILL" primary key ("TASK_DB_ID","TASK_ID","LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD") 
) 
');
END;
/

--changeSet RND-2222:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_PLANNING_TYPE" BEFORE INSERT
   ON "EQP_PLANNING_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-2222:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_EQP_PLANNING_TYPE" BEFORE UPDATE
   ON "EQP_PLANNING_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-2222:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_PLANNING_TYPE_SKILL" BEFORE INSERT
   ON "EQP_PLANNING_TYPE_SKILL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-2222:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_EQP_PLANNING_TYPE_SKILL" BEFORE UPDATE
   ON "EQP_PLANNING_TYPE_SKILL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-2222:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_TASK_PLANNING_TYPE_SKILL" BEFORE INSERT
   ON "TASK_PLANNING_TYPE_SKILL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-2222:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_TASK_PLANNING_TYPE_SKILL" BEFORE UPDATE
   ON "TASK_PLANNING_TYPE_SKILL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet RND-2222:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EQP_PLANNING_TYPE" add Constraint "FK_EQPASSMBL_EQPPLANNINGTYPE" foreign key ("ASSMBL_DB_ID","ASSMBL_CD") references "EQP_ASSMBL" ("ASSMBL_DB_ID","ASSMBL_CD")  DEFERRABLE
');
END;
/

--changeSet RND-2222:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EQP_PLANNING_TYPE" add Constraint "FK_MIMDB_EQPPLANNINGTYPE" foreign key ("PLANNING_TYPE_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet RND-2222:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EQP_PLANNING_TYPE_SKILL" add Constraint "FK_REFLABOURSKILL_EQPPLANNINGT" foreign key ("LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD") references "REF_LABOUR_SKILL" ("LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD")  DEFERRABLE
');
END;
/

--changeSet RND-2222:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TASK_PLANNING_TYPE_SKILL" add Constraint "FK_REFLABOURSKILL_TASKPLANNING" foreign key ("LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD") references "REF_LABOUR_SKILL" ("LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD")  DEFERRABLE
');
END;
/

--changeSet RND-2222:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TASK_PLANNING_TYPE_SKILL" add Constraint "FK_TASKTASK_TASKPLANNINGTYPESK" foreign key ("TASK_DB_ID","TASK_ID") references "TASK_TASK" ("TASK_DB_ID","TASK_ID")  DEFERRABLE
');
END;
/

--changeSet RND-2222:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "TASK_PLANNING_TYPE_SKILL" add Constraint "FK_MIMRSTAT_TASKPLANNINGTYPESK" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet RND-2222:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EQP_PLANNING_TYPE_SKILL" add Constraint "FK_MIMRSTAT_EQPPLANNINGTYPESKI" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet RND-2222:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EQP_PLANNING_TYPE" add Constraint "FK_MIMRSTAT_EQPPLANNINGTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet RND-2222:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EQP_PLANNING_TYPE_SKILL" add Constraint "FK_EQPPLANNINGTYPE_LABOURSKILL" foreign key ("PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID") references "EQP_PLANNING_TYPE" ("PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID")  DEFERRABLE
');
END;
/

--changeSet RND-2222:19 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'ACTION_EDIT_BLOCK_PLANNING_VALUES','SECURED_RESOURCE','false', 0,'Visibility control for Edit Planning Values button.','USER', 'true/false', 'false', 1, 'Maint Program - Blocks', '1003', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'ACTION_EDIT_BLOCK_PLANNING_VALUES' AND PARM_TYPE = 'SECURED_RESOURCE');

--changeSet RND-2222:20 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'ACTION_EDIT_REQ_PLANNING_VALUES','SECURED_RESOURCE','false', 0,'Visibility control for Edit Planning Values button.','USER', 'true/false', 'false', 1, 'Maint Program - Requirements', '1003', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'ACTION_EDIT_REQ_PLANNING_VALUES' AND PARM_TYPE = 'SECURED_RESOURCE');

--changeSet RND-2222:21 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'ACTION_EDIT_JIC_PLANNING_VALUES','SECURED_RESOURCE','false', 0,'Visibility control for Edit Planning Values button.','USER', 'true/false', 'false', 1, 'Maint Program - Job Cards', '1003', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'ACTION_EDIT_JIC_PLANNING_VALUES' AND PARM_TYPE = 'SECURED_RESOURCE');

--changeSet RND-2222:22 stripComments:false
insert into ref_dept_type(dept_type_db_id, dept_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
SELECT 0, 'ENDSTRT', 0, 1, 'A-FS->B means B does not start before A ends.', 'A-FS->B means B does not start before A ends.', 0, TO_DATE('2008-07-30', 'YYYY-MM-DD'), TO_DATE('2008-07-30', 'YYYY-MM-DD'), 100, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM REF_DEPT_TYPE WHERE DEPT_TYPE_DB_ID = 0 AND DEPT_TYPE_CD = 'ENDSTRT');

--changeSet RND-2222:23 stripComments:false
insert into ref_dept_type(dept_type_db_id, dept_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
SELECT 0, 'STRTSTRT', 0, 1, 'A-SS->B means B does not start before A has started.', 'A-SS->B means B does not start before A has started.', 0, TO_DATE('2008-07-30', 'YYYY-MM-DD'), TO_DATE('2008-07-30', 'YYYY-MM-DD'), 100, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM REF_DEPT_TYPE WHERE DEPT_TYPE_DB_ID = 0 AND DEPT_TYPE_CD = 'STRTSTRT');