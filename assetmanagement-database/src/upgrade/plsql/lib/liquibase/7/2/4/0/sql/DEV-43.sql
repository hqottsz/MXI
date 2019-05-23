--liquibase formatted sql


--changeSet DEV-43:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_INV_INV add (
	"READ_ONLY_BOOL" Number(1,0)  
)
');
END;
/

--changeSet DEV-43:2 stripComments:false
UPDATE LRP_INV_INV SET READ_ONLY_BOOL = 0 WHERE READ_ONLY_BOOL IS NULL;

--changeSet DEV-43:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_INV_INV modify (
	"READ_ONLY_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (READ_ONLY_BOOL IN (0, 1) ) DEFERRABLE 
)
');
END;
/

--changeSet DEV-43:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_INV_INV add (
	"AD_EVT_CTRL_BOOL" Number(1,0) 
)
');
END;
/

--changeSet DEV-43:5 stripComments:false
UPDATE LRP_INV_INV SET AD_EVT_CTRL_BOOL = 0 WHERE AD_EVT_CTRL_BOOL IS NULL;

--changeSet DEV-43:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_INV_INV modify (
	"AD_EVT_CTRL_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (AD_EVT_CTRL_BOOL IN (0, 1) ) DEFERRABLE 
)
');
END;
/

--changeSet DEV-43:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_LOC add (
	"AH_EVT_CTRL_BOOL" Number(1,0) 
)
');
END;
/

--changeSet DEV-43:8 stripComments:false
UPDATE LRP_LOC SET AH_EVT_CTRL_BOOL = 0 WHERE AH_EVT_CTRL_BOOL IS NULL;

--changeSet DEV-43:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_LOC modify (
	"AH_EVT_CTRL_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (AH_EVT_CTRL_BOOL IN (0, 1) ) DEFERRABLE 
)
');
END;
/

--changeSet DEV-43:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_TASK_DEFN add (
	"READ_ONLY_BOOL" Number(1,0) 
)
');
END;
/

--changeSet DEV-43:11 stripComments:false
UPDATE LRP_TASK_DEFN SET READ_ONLY_BOOL = 0 WHERE READ_ONLY_BOOL IS NULL;

--changeSet DEV-43:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_TASK_DEFN modify (
	"READ_ONLY_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (READ_ONLY_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet DEV-43:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Constraints will be added in DEV-108.sql
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_PLAN_CONFIG add (
	"READONLY_SEV_DB_ID" Number(10,0) 
)
');
END;
/

--changeSet DEV-43:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Constraints will be added in DEV-108.sql
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_PLAN_CONFIG add (
	"READONLY_SEV_CD" Varchar2 (8) 
)
');
END;
/

--changeSet DEV-43:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LRP_INV_TASK_PLAN" (
	"INV_NO_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"INV_NO_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TASK_DEFN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DEFN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TASK_DEFN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DEFN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"PUBLISHED_DT" Date,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LRP_INV_TASK_PLAN" primary key ("INV_NO_DB_ID","INV_NO_ID","TASK_DEFN_DB_ID","TASK_DEFN_ID") 
) 
');
END;
/

--changeSet DEV-43:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LRP_INV_ADHOC_PLAN" (
	"INV_NO_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"INV_NO_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"PUBLISHED_DT" Date,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LRP_INV_ADHOC_PLAN" primary key ("INV_NO_DB_ID","INV_NO_ID") 
) 
');
END;
/

--changeSet DEV-43:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LRP_LOC_ADHOC_PLAN" (
	"LOC_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LOC_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"PUBLISHED_DT" Date,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LRP_LOC_ADHOC_PLAN" primary key ("LOC_DB_ID","LOC_ID") 
) 
');
END;
/

--changeSet DEV-43:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_INV_TASK_PLAN" add Constraint "FK_INVINV_LRPINVTASKPLAN" foreign key ("INV_NO_DB_ID","INV_NO_ID") references "INV_INV" ("INV_NO_DB_ID","INV_NO_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-43:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_INV_ADHOC_PLAN" add Constraint "FK_INVINV_LRPINVADHOCPLAN" foreign key ("INV_NO_DB_ID","INV_NO_ID") references "INV_INV" ("INV_NO_DB_ID","INV_NO_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-43:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_INV_TASK_PLAN" add Constraint "FK_TASKDEFN_LRPINVTASKPLAN" foreign key ("TASK_DEFN_DB_ID","TASK_DEFN_ID") references "TASK_DEFN" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-43:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_INV_TASK_PLAN" add Constraint "FK_MIMRSTAT_LRPINVTASKPLAN" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-43:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_LOC_ADHOC_PLAN" add Constraint "FK_MIMRSTAT_LRPLOCADHOCPLAN" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-43:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_INV_TASK_PLAN" add Constraint "FK_LRPPLAN_LRPINVTASKPLAN" foreign key ("LRP_DB_ID","LRP_ID") references "LRP_PLAN" ("LRP_DB_ID","LRP_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-43:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_INV_ADHOC_PLAN" add Constraint "FK_LRPPLAN_LRPINVADHOCPLAN" foreign key ("LRP_DB_ID","LRP_ID") references "LRP_PLAN" ("LRP_DB_ID","LRP_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-43:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_LOC_ADHOC_PLAN" add Constraint "FK_LRPPLAN_LRPLOCADHOCPLAN" foreign key ("LRP_DB_ID","LRP_ID") references "LRP_PLAN" ("LRP_DB_ID","LRP_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-43:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_PLAN_CONFIG" add Constraint "FK_READONLYSEV_LRPPLANCONFIG" foreign key ("READONLY_SEV_DB_ID","READONLY_SEV_CD") references "REF_LRP_CONFIG_SEV" ("LRP_CONFIG_SEV_DB_ID","LRP_CONFIG_SEV_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-43:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBUR_LRP_INV_TASK_PLAN" BEFORE INSERT
   ON "LRP_INV_TASK_PLAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-43:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LRP_INV_TASK_PLAN" BEFORE UPDATE
   ON "LRP_INV_TASK_PLAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-43:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBUR_LRP_INV_ADHOC_PLAN" BEFORE INSERT
   ON "LRP_INV_ADHOC_PLAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-43:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LRP_INV_ADHOC_PLAN" BEFORE UPDATE
   ON "LRP_INV_ADHOC_PLAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-43:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBUR_LRP_LOC_ADHOC_PLAN" BEFORE INSERT
   ON "LRP_LOC_ADHOC_PLAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-43:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LRP_LOC_ADHOC_PLAN" BEFORE UPDATE
   ON "LRP_LOC_ADHOC_PLAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-43:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table LRP_EVENT modify (
    "TYPE" Varchar2 (40) 
  )
  ');
 END;
 / 

--changeSet DEV-43:34 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('LRP_INV_INV', 'AD_EVT_CTRL_BOOL');
END;
/

--changeSet DEV-43:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_INV_INV add (
	"AH_EVT_CTRL_BOOL" Number(1,0) 
)
');
END;
/

--changeSet DEV-43:36 stripComments:false
UPDATE LRP_INV_INV SET AH_EVT_CTRL_BOOL = 0 WHERE AH_EVT_CTRL_BOOL IS NULL;

--changeSet DEV-43:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_INV_INV modify (
	"AH_EVT_CTRL_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (AH_EVT_CTRL_BOOL IN (0, 1) ) DEFERRABLE 
)
');
END;
/

--changeSet DEV-43:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table LRP_PLAN_CONFIG modify (
    "READONLY_SEV_DB_ID" Number(10,0) Check (READONLY_SEV_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE 
  )
  ');
 END;
 /

--changeSet DEV-43:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table LRP_PLAN_CONFIG modify (
    "READONLY_SEV_CD" Varchar2 (8)
  )
  ');
 END;
 /

--changeSet DEV-43:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_LOC_ADHOC_PLAN" add Constraint "FK_INVLOC_LRPLOCADHOCPLAN" foreign key ("LOC_DB_ID","LOC_ID") references "INV_LOC" ("LOC_DB_ID","LOC_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-43:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_INV_ADHOC_PLAN" add Constraint "FK_MIMRSTAT_LRPINVADHOCPLAN" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-43:42 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBUR_LRP_PLAN');
END;
/

--changeSet DEV-43:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LRP_PLAN" BEFORE INSERT
   ON "LRP_PLAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-43:44 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBUR_LRP_INV_INV');
END;
/

--changeSet DEV-43:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LRP_INV_INV" BEFORE INSERT
   ON "LRP_INV_INV" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-43:46 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBUR_LRP_TASK_DEFN');
END;
/

--changeSet DEV-43:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LRP_TASK_DEFN" BEFORE INSERT
   ON "LRP_TASK_DEFN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-43:48 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBUR_LRP_INV_TASK_PLAN');
END;
/

--changeSet DEV-43:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LRP_INV_TASK_PLAN" BEFORE INSERT
   ON "LRP_INV_TASK_PLAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-43:50 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBUR_LRP_INV_ADHOC_PLAN');
END;
/

--changeSet DEV-43:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LRP_INV_ADHOC_PLAN" BEFORE INSERT
   ON "LRP_INV_ADHOC_PLAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-43:52 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBUR_LRP_LOC_ADHOC_PLAN');
END;
/

--changeSet DEV-43:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LRP_LOC_ADHOC_PLAN" BEFORE INSERT
   ON "LRP_LOC_ADHOC_PLAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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