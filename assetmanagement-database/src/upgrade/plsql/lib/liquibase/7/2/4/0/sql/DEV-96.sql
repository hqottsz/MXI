--liquibase formatted sql


--changeSet DEV-96:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table REF_WORK_TYPE add (
	"WORK_TYPE_ORD" Number(4,0)
)
');
END;
/

--changeSet DEV-96:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ER_HEADER add (
	"LOC_DB_ID" Number(10,0) Check (LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE 
)
');
END;
/

--changeSet DEV-96:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ER_HEADER add (
	"LOC_ID" Number(10,0) Check (LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE 
)
');
END;
/

--changeSet DEV-96:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ER_HEADER add (
	"EFFECTIVE_FROM_DT" Date 
)
');
END;
/

--changeSet DEV-96:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ER_HEADER add (
	"EFFECTIVE_TO_DT" Date 
)
');
END;
/

--changeSet DEV-96:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ER_HEADER add (
	"ER_TYPE_CD" Varchar2 (8)
)
');
END;
/

--changeSet DEV-96:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ER_WORK_TYPE" (
	"RULE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (RULE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RULE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (RULE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"WORK_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (WORK_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"WORK_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_ER_WORK_TYPE" primary key ("RULE_DB_ID","RULE_ID","WORK_TYPE_DB_ID","WORK_TYPE_CD") 
) 
');
END;
/

--changeSet DEV-96:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ER_WEEKLY_RANGE" (
	"RULE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (RULE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RULE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (RULE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"WEEKLY_RANGE_ID" Number(10,0) NOT NULL DEFERRABLE ,
	"START_DAY" Number(10,0) NOT NULL DEFERRABLE ,
	"START_HOUR" Number(10,0) NOT NULL DEFERRABLE ,
	"START_MINUTE" Number(10,0) NOT NULL DEFERRABLE ,
	"MAX_AIRCRAFT" Number(10,0),
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_ER_WEEKLY_RANGE" primary key ("RULE_DB_ID","RULE_ID","WEEKLY_RANGE_ID") 
) 
');
END;
/

--changeSet DEV-96:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ER_HEADER" add Constraint "FK_INVLOC_ERHEADER" foreign key ("LOC_DB_ID","LOC_ID") references "INV_LOC" ("LOC_DB_ID","LOC_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-96:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ER_WORK_TYPE" add Constraint "FK_MIMRSTAT_ERWORKTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-96:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ER_WEEKLY_RANGE" add Constraint "FK_MIMRSTAT_ERWEEKLYRANGE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-96:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ER_WORK_TYPE" add Constraint "FK_ERHEADER_ERWORKTYPE" foreign key ("RULE_DB_ID","RULE_ID") references "ER_HEADER" ("RULE_DB_ID","RULE_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-96:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ER_WEEKLY_RANGE" add Constraint "FK_ERHEADER_ERWEEKLYRANGE" foreign key ("RULE_DB_ID","RULE_ID") references "ER_HEADER" ("RULE_DB_ID","RULE_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-96:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ER_WEEKLY_RANGE" BEFORE INSERT
   ON "ER_WEEKLY_RANGE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-96:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_ER_WEEKLY_RANGE" BEFORE UPDATE
   ON "ER_WEEKLY_RANGE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-96:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ER_WORK_TYPE" BEFORE INSERT
   ON "ER_WORK_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-96:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_ER_WORK_TYPE" BEFORE UPDATE
   ON "ER_WORK_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-96:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ER_WORK_TYPE" add Constraint "FK_REFWORKTYPE_ERWORKTYPE" foreign key ("WORK_TYPE_DB_ID","WORK_TYPE_CD") references "REF_WORK_TYPE" ("WORK_TYPE_DB_ID","WORK_TYPE_CD")  DEFERRABLE
');
END;
/