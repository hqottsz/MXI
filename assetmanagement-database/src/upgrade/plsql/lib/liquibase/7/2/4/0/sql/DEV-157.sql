--liquibase formatted sql


--changeSet DEV-157:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LRP_LOC_CAPACITY" (
	"LRP_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LOC_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LOC_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CAPACITY_PATTERN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CAPACITY_PATTERN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LRP_LOC_CAPACITY" primary key ("LRP_DB_ID","LRP_ID","LOC_DB_ID","LOC_ID","CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID") 
) 
');
END;
/

--changeSet DEV-157:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LRP_LOC_CAP_STD" (
	"LRP_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LOC_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LOC_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CAPACITY_PATTERN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CAPACITY_PATTERN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CAPACITY_STD_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_STD_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"EFFECTIVE_FROM_DT" Date NOT NULL DEFERRABLE ,
	"EFFECTIVE_TO_DT" Date,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LRP_LOC_CAP_STD" primary key ("LRP_DB_ID","LRP_ID","LOC_DB_ID","LOC_ID","CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID","CAPACITY_STD_ID") 
) 
');
END;
/

--changeSet DEV-157:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LRP_LOC_CAP_EXCEPT" (
	"LRP_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LOC_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LOC_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CAPACITY_PATTERN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CAPACITY_PATTERN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_PATTERN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CAPACITY_EXCEPT_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CAPACITY_EXCEPT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"EFFECTIVE_FROM_DT" Date NOT NULL DEFERRABLE ,
	"EFFECTIVE_TO_DT" Date NOT NULL DEFERRABLE ,
	"RECURR_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (RECURR_BOOL IN (0, 1) ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LRP_LOC_CAP_EXCEPT" primary key ("LRP_DB_ID","LRP_ID","LOC_DB_ID","LOC_ID","CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID","CAPACITY_EXCEPT_ID") 
) 
');
END;
/

--changeSet DEV-157:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_LOC_CAP_EXCEPT" add Constraint "FK_MIMRSTAT_LRPLOCCAPACITYEXCE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-157:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_LOC_CAPACITY" add Constraint "FK_MIMRSTAT_LRPLOCCAPACITY" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-157:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_LOC_CAP_STD" add Constraint "FK_MIMRSTAT_LRPLOCCAPACITYSTD" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-157:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_LOC_CAPACITY" add Constraint "FK_LRPLOC_LRPLOCCAPACITY" foreign key ("LRP_DB_ID","LRP_ID","LOC_DB_ID","LOC_ID") references "LRP_LOC" ("LRP_DB_ID","LRP_ID","LOC_DB_ID","LOC_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-157:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_LOC_CAPACITY" add Constraint "FK_CAPACITYPATTERN_LRPLOCCAPAC" foreign key ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID") references "CAPACITY_PATTERN" ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-157:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_LOC_CAP_STD" add Constraint "FK_LRPLOCCAPACITY_LRPLOCCAPSTD" foreign key ("LRP_DB_ID","LRP_ID","LOC_DB_ID","LOC_ID","CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID") references "LRP_LOC_CAPACITY" ("LRP_DB_ID","LRP_ID","LOC_DB_ID","LOC_ID","CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-157:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_LOC_CAP_EXCEPT" add Constraint "FK_LRPLOCCAPACITY_LRPLOCCAPEXC" foreign key ("LRP_DB_ID","LRP_ID","LOC_DB_ID","LOC_ID","CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID") references "LRP_LOC_CAPACITY" ("LRP_DB_ID","LRP_ID","LOC_DB_ID","LOC_ID","CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-157:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LRP_LOC_CAPACITY" BEFORE INSERT
   ON "LRP_LOC_CAPACITY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-157:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LRP_LOC_CAPACITY" BEFORE UPDATE
   ON "LRP_LOC_CAPACITY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-157:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LRP_LOC_CAP_STD" BEFORE INSERT
   ON "LRP_LOC_CAP_STD" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-157:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LRP_LOC_CAP_STD" BEFORE UPDATE
   ON "LRP_LOC_CAP_STD" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-157:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LRP_LOC_CAP_EXCEPT" BEFORE INSERT
   ON "LRP_LOC_CAP_EXCEPT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-157:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LRP_LOC_CAP_EXCEPT" BEFORE UPDATE
   ON "LRP_LOC_CAP_EXCEPT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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