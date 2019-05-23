--liquibase formatted sql


--changeSet DEV-88:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "SCHED_KIT_MAP" (
	"SCHED_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SCHED_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"SCHED_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SCHED_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"KIT_INV_NO_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (KIT_INV_NO_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"KIT_INV_NO_ID" Number(10,0) NOT NULL DEFERRABLE  Check (KIT_INV_NO_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"INV_NO_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"INV_NO_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_SCHED_KIT_MAP" primary key ("SCHED_DB_ID","SCHED_ID","KIT_INV_NO_DB_ID","KIT_INV_NO_ID","INV_NO_DB_ID","INV_NO_ID") 
) 
');
END;
/

--changeSet DEV-88:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- The primary key changed since the previous branch, therefore, drop and recreate the primary key if it exists
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_KIT_MAP', 'PK_SCHED_KIT_MAP');
END;
/

--changeSet DEV-88:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_SCHED_KIT_MAP');
END;
/

--changeSet DEV-88:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table SCHED_KIT_MAP add Constraint "PK_SCHED_KIT_MAP" primary key ("SCHED_DB_ID","SCHED_ID","KIT_INV_NO_DB_ID","KIT_INV_NO_ID","INV_NO_DB_ID","INV_NO_ID") 
');
END;
/

--changeSet DEV-88:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_KIT_MAP" add Constraint "FK_INVINV_SCHEDKITMAP" foreign key ("INV_NO_DB_ID","INV_NO_ID") references "INV_INV" ("INV_NO_DB_ID","INV_NO_ID") 
');
END;
/

--changeSet DEV-88:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_KIT_MAP" add Constraint "FK_SCHEDSTASK_SCHEDKITMAP" foreign key ("SCHED_DB_ID","SCHED_ID") references "SCHED_STASK" ("SCHED_DB_ID","SCHED_ID") 
');
END;
/

--changeSet DEV-88:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_KIT_MAP" add Constraint "FK_MIMRSTAT_SCHEDKITMAP" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-88:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_KIT_MAP" add Constraint "FK_INVKIT_SCHEDKITMAP" foreign key ("KIT_INV_NO_DB_ID","KIT_INV_NO_ID") references "INV_KIT" ("INV_NO_DB_ID","INV_NO_ID") 
');
END;
/

--changeSet DEV-88:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SCHED_KIT_MAP" BEFORE INSERT
   ON "SCHED_KIT_MAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-88:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SCHED_KIT_MAP" BEFORE UPDATE
   ON "SCHED_KIT_MAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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