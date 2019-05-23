--liquibase formatted sql


--changeSet DEV-454:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Pre migration data model changes.
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ORG_VENDOR_PO_TYPE" (
    "ORG_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ORG_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ORG_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ORG_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "VENDOR_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (VENDOR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "VENDOR_ID" Number(10,0) NOT NULL DEFERRABLE  Check (VENDOR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "PO_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PO_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "PO_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
    "VENDOR_STATUS_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (VENDOR_STATUS_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "VENDOR_STATUS_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
    "APPROVAL_EXPIRY_DT" Date,
    "WARNING_LDESC" Varchar2 (4000),
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_ORG_VENDOR_PO_TYPE" primary key ("ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID","PO_TYPE_DB_ID","PO_TYPE_CD") 
) 
');
END;
/

--changeSet DEV-454:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "REF_SERVICE_TYPE" (
	"SERVICE_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SERVICE_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"SERVICE_TYPE_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"DESC_SDESC" Varchar2 (80),
	"DESC_LDESC" Varchar2 (4000),
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_REF_SERVICE_TYPE" primary key ("SERVICE_TYPE_DB_ID","SERVICE_TYPE_CD") 
) 
');
END;
/

--changeSet DEV-454:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ORG_VENDOR_SERVICE_TYPE" (
	"ORG_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ORG_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"ORG_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ORG_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"VENDOR_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (VENDOR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"VENDOR_ID" Number(10,0) NOT NULL DEFERRABLE  Check (VENDOR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"SERVICE_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SERVICE_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"SERVICE_TYPE_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"VENDOR_STATUS_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (VENDOR_STATUS_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"VENDOR_STATUS_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"APPROVAL_EXPIRY_DT" Date,
	"WARNING_LDESC" Varchar2 (4000),
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_ORG_VENDOR_SERVICE_TYPE" primary key ("ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID","SERVICE_TYPE_DB_ID","SERVICE_TYPE_CD") 
) 
');
END;
/

--changeSet DEV-454:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_VENDOR_PO_TYPE" add Constraint "FK_MIMRSTAT_ORGVENPOTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-454:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_VENDOR_SERVICE_TYPE" add Constraint "FK_MIMRSTAT_ORGVENSRVTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-454:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_SERVICE_TYPE" add Constraint "FK_MIMRSTAT_REFSRVTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-454:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_VENDOR_PO_TYPE" add Constraint "FK_REFVENSTATUS_ORGVENPOTYPE" foreign key ("VENDOR_STATUS_DB_ID","VENDOR_STATUS_CD") references "REF_VENDOR_STATUS" ("VENDOR_STATUS_DB_ID","VENDOR_STATUS_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-454:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_VENDOR_SERVICE_TYPE" add Constraint "FK_REFVENSTAT_ORGVENSRVTYPE" foreign key ("VENDOR_STATUS_DB_ID","VENDOR_STATUS_CD") references "REF_VENDOR_STATUS" ("VENDOR_STATUS_DB_ID","VENDOR_STATUS_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-454:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_VENDOR_PO_TYPE" add Constraint "FK_REFPOTYPE_ORGVENDORPOTYPE" foreign key ("PO_TYPE_DB_ID","PO_TYPE_CD") references "REF_PO_TYPE" ("PO_TYPE_DB_ID","PO_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-454:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_VENDOR_PO_TYPE" add Constraint "FK_ORGORGVENDOR_ORGVENPOTYPE" foreign key ("ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID") references "ORG_ORG_VENDOR" ("ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-454:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_VENDOR_SERVICE_TYPE" add Constraint "FK_ORGORGVEN_ORGVENSRVTYPE" foreign key ("ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID") references "ORG_ORG_VENDOR" ("ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-454:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_VENDOR_SERVICE_TYPE" add Constraint "FK_REFSRVTYPE_ORGVENSRVTYPE" foreign key ("SERVICE_TYPE_DB_ID","SERVICE_TYPE_CD") references "REF_SERVICE_TYPE" ("SERVICE_TYPE_DB_ID","SERVICE_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-454:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "EVT_ORG_VENDOR_PO_TYPE" (
    "EVENT_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (EVENT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "EVENT_ID" Number(10,0) NOT NULL DEFERRABLE  Check (EVENT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ORG_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ORG_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ORG_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ORG_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "VENDOR_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (VENDOR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "VENDOR_ID" Number(10,0) NOT NULL DEFERRABLE  Check (VENDOR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "PO_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PO_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "PO_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_EVT_ORG_VENDOR_PO_TYPE" primary key ("EVENT_DB_ID","EVENT_ID","ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID","PO_TYPE_DB_ID","PO_TYPE_CD") 
) 
');
END;
/

--changeSet DEV-454:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "EVT_ORG_VENDOR_SERVICE_TYPE" (
	"EVENT_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (EVENT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"EVENT_ID" Number(10,0) NOT NULL DEFERRABLE  Check (EVENT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"ORG_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ORG_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"ORG_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ORG_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"VENDOR_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (VENDOR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"VENDOR_ID" Number(10,0) NOT NULL DEFERRABLE  Check (VENDOR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"SERVICE_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SERVICE_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"SERVICE_TYPE_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_EVT_ORG_VENDOR_SERVICE_TYPE" primary key ("EVENT_DB_ID","EVENT_ID","ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID","SERVICE_TYPE_DB_ID","SERVICE_TYPE_CD") 
) 
');
END;
/

--changeSet DEV-454:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "SCHED_SERVICE_TYPE" (
	"SCHED_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SCHED_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"SCHED_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SCHED_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"SERVICE_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SERVICE_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"SERVICE_TYPE_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_SCHED_SERVICE_TYPE" primary key ("SCHED_DB_ID","SCHED_ID","SERVICE_TYPE_DB_ID","SERVICE_TYPE_CD") 
) 
');
END;
/

--changeSet DEV-454:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EVT_ORG_VENDOR_PO_TYPE" add Constraint "FK_EVTEVENT_EVTORGVENPOTYPE" foreign key ("EVENT_DB_ID","EVENT_ID") references "EVT_EVENT" ("EVENT_DB_ID","EVENT_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-454:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EVT_ORG_VENDOR_SERVICE_TYPE" add Constraint "FK_EVTEVENT_EVTORGVENSRVTYPE" foreign key ("EVENT_DB_ID","EVENT_ID") references "EVT_EVENT" ("EVENT_DB_ID","EVENT_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-454:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_SERVICE_TYPE" add Constraint "FK_SCHEDSTASK_SCHEDSRVTYPE" foreign key ("SCHED_DB_ID","SCHED_ID") references "SCHED_STASK" ("SCHED_DB_ID","SCHED_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-454:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EVT_ORG_VENDOR_PO_TYPE" add Constraint "FK_MIMRSTAT_EVTORGVENPOTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-454:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EVT_ORG_VENDOR_SERVICE_TYPE" add Constraint "FK_MIMRSTAT_EVTORGVENSRVTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-454:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_SERVICE_TYPE" add Constraint "FK_MIMRSTAT_SCHEDSRVTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-454:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EVT_ORG_VENDOR_PO_TYPE" add Constraint "FK_ORGVENPOTYPE_EVTORGVENPO" foreign key ("ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID","PO_TYPE_DB_ID","PO_TYPE_CD") references "ORG_VENDOR_PO_TYPE" ("ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID","PO_TYPE_DB_ID","PO_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-454:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_SERVICE_TYPE" add Constraint "FK_REFSRVTYPE_SCHEDSRVTYPE" foreign key ("SERVICE_TYPE_DB_ID","SERVICE_TYPE_CD") references "REF_SERVICE_TYPE" ("SERVICE_TYPE_DB_ID","SERVICE_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-454:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EVT_ORG_VENDOR_SERVICE_TYPE" add Constraint "FK_ORGVENSRVTYPE_EVTORGVENSR" foreign key ("ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID","SERVICE_TYPE_DB_ID","SERVICE_TYPE_CD") references "ORG_VENDOR_SERVICE_TYPE" ("ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID","SERVICE_TYPE_DB_ID","SERVICE_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-454:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_SERVICE_TYPE" BEFORE INSERT
   ON "REF_SERVICE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-454:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_SERVICE_TYPE" BEFORE UPDATE
   ON "REF_SERVICE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-454:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ORG_VENDOR_PO_TYPE" BEFORE INSERT
   ON "ORG_VENDOR_PO_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-454:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_ORG_VENDOR_PO_TYPE" BEFORE UPDATE
   ON "ORG_VENDOR_PO_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-454:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ORG_VENDOR_SERVICE_TYPE" BEFORE INSERT
   ON "ORG_VENDOR_SERVICE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-454:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_ORG_VENDOR_SERVICE_TYPE" BEFORE UPDATE
   ON "ORG_VENDOR_SERVICE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-454:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SCHED_SERVICE_TYPE" BEFORE INSERT
   ON "SCHED_SERVICE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-454:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SCHED_SERVICE_TYPE" BEFORE UPDATE
   ON "SCHED_SERVICE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-454:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EVT_ORG_VENDOR_PO_TYPE" BEFORE INSERT
   ON "EVT_ORG_VENDOR_PO_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-454:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_EVT_ORG_VENDOR_PO_TYPE" BEFORE UPDATE
   ON "EVT_ORG_VENDOR_PO_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-454:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EVT_ORG_VEN_SRV_TYPE" BEFORE INSERT
   ON "EVT_ORG_VENDOR_SERVICE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-454:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_EVT_ORG_VEN_SRV_TYPE" BEFORE UPDATE
   ON "EVT_ORG_VENDOR_SERVICE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-454:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
-- Cursor to hold the list of organizations.
CURSOR lcur_OrganizationList IS
SELECT
org_org.org_db_id,
org_org.org_id
FROM
org_org;
-- Variable to loop throug the organizatin list cursor.
lrec_Organization lcur_OrganizationList%ROWTYPE;
-- Cursor to hold the list of vendors.
CURSOR lcur_VendorList(aOrgDbId org_org.org_db_id%TYPE, aOrgId org_org.org_id%TYPE) IS
SELECT 
org_org_vendor.vendor_db_id,
org_org_vendor.vendor_id,
org_org_vendor.vendor_status_db_id,
org_org_vendor.vendor_status_cd,
org_org_vendor.approval_expiry_dt,
org_org_vendor.warning_ldesc
FROM
org_org_vendor
WHERE
org_org_vendor.org_db_id = aOrgDbId AND
org_org_vendor.org_id = aOrgId AND
org_org_vendor.vendor_status_cd NOT IN ('BLKOUT');
-- Variable to loop through the vendor list.
lrec_Vendor lcur_VendorList%ROWTYPE;
-- Cursor to hold the list of available order types in the system.
CURSOR lcur_PoTypes IS
SELECT
ref_po_type.po_type_db_id,
ref_po_type.po_type_cd
FROM
ref_po_type
WHERE
ref_po_type.rstat_cd = 0;
-- Variable to loop through the order types list.
lrec_Potype lcur_PoTypes%ROWTYPE;
-- Cursor to hold the list of service types in the system.
CURSOR lcur_ServiceTypes IS
SELECT
ref_service_type.service_type_db_id,
ref_service_type.service_type_cd
FROM
ref_service_type
WHERE
ref_service_type.rstat_cd = 0;

CURSOR lcur_EventInfo (aOrgDbId org_org.org_db_id%TYPE, aOrgId org_org.org_id%TYPE, aVendorDbId org_vendor.vendor_db_id%TYPE, aVendorId org_vendor.vendor_id%TYPE) IS
SELECT
        evt_stage.stage_id,
        evt_stage.stage_reason_db_id,
        evt_stage.stage_reason_cd,
        evt_stage.user_stage_note,
        evt_event.event_status_db_id,
        evt_event.event_status_cd,
        evt_event.event_reason_db_id,
        evt_event.event_reason_cd,
        evt_event.event_sdesc,
        evt_event.event_dt,
        evt_event.event_gdt,
        evt_stage.hr_db_id,
        evt_stage.hr_id,
        evt_stage.stage_dt,
        evt_stage.stage_gdt
        FROM evt_org_vendor
        LEFT OUTER JOIN evt_stage ON
        evt_org_vendor.event_db_id = evt_stage.event_db_id AND
        evt_org_vendor.event_id = evt_stage.event_id
        LEFT OUTER JOIN evt_event ON
        evt_stage.event_db_id = evt_event.event_db_id AND
        evt_stage.event_id = evt_event.event_id
        WHERE
        evt_org_vendor.vendor_db_id = aVendorDbId AND
        evt_org_vendor.vendor_id = aVendorId AND
        evt_org_vendor.org_db_id = aOrgDbId AND
        evt_org_vendor.org_id = aOrgId        
        AND
        evt_stage.stage_gdt  = 
        (
        SELECT
        MAX(evt_stage.stage_gdt)
        FROM 
        evt_org_vendor
        LEFT OUTER JOIN evt_stage ON
        evt_org_vendor.event_db_id = evt_stage.event_db_id AND
        evt_org_vendor.event_id = evt_stage.event_id
        WHERE
        evt_org_vendor.vendor_db_id = aVendorDbId AND
        evt_org_vendor.vendor_id = aVendorId AND
        evt_org_vendor.org_db_id = aOrgDbId AND
        evt_org_vendor.org_id = aOrgId);        
        
lrec_EventInfo lcur_EventInfo%ROWTYPE;
-- Variable to loop through the service types.
lrec_serviceType lcur_ServiceTypes%ROWTYPE;
-- Variable to hold the event db id.
ln_eventDbId evt_event.event_db_id%TYPE;
-- Variable to hold the event id.
ln_eventId evt_Event.Event_Id%TYPE;
-- Variable to hold mim_data_type db id;
ln_mimDbId mim_local_db.db_id%TYPE;
-- Variable to hold event indicator
ln_eventBool NUMBER;

BEGIN

SELECT mim_local_db.db_id INTO ln_mimDbId FROM mim_local_db;

-- Loop through the list of organizations in the system.
FOR lrec_Organization IN lcur_OrganizationList
LOOP
    -- For each organization loop through the assigned vendor list.
    FOR lrec_Vendor IN lcur_VendorList(lrec_Organization.org_db_id, lrec_Organization.org_id)
    LOOP
    
       ln_eventBool := 0;
    
       OPEN lcur_EventInfo (lrec_Organization.org_db_id, lrec_Organization.org_id, lrec_Vendor.vendor_db_id, lrec_Vendor.vendor_id);    
       
       FETCH lcur_EventInfo  INTO lrec_EventInfo;
       
       IF lcur_EventInfo%FOUND THEN
       
           ln_eventBool := 1;
              
       END IF;       
       
       CLOSE lcur_EventInfo;

        -- Iterate through the list of order types in the system.
        FOR lrec_Potype IN lcur_PoTypes
        
        LOOP
        
            -- For each order type, create records in org_Vendor_po_type.
            INSERT INTO 
            org_vendor_po_type 
            (org_db_id,org_id,vendor_db_id,vendor_id,po_type_db_id,po_type_cd,
            vendor_status_db_id, vendor_status_cd ,approval_expiry_dt, warning_ldesc)
            VALUES 
            (lrec_Organization.org_db_id, lrec_Organization.org_id, lrec_Vendor.vendor_db_id, 
            lrec_Vendor.vendor_id, lrec_Potype.po_type_db_id, lrec_Potype.po_type_cd, lrec_Vendor.vendor_status_db_id,
            lrec_Vendor.vendor_status_cd, lrec_Vendor.approval_expiry_dt, lrec_Vendor.warning_ldesc);                       
            
            IF (ln_eventBool = 1) THEN
            
                SELECT event_id_seq.nextval INTO ln_eventId FROM DUAL;
            
                -- Create records in evt_event table.
                INSERT INTO 
                evt_event 
                (event_db_id,event_id,event_type_db_id,event_type_cd,
                stage_reason_db_id,stage_reason_cd,editor_hr_db_id, editor_hr_id,
                event_status_db_id,event_status_cd,event_reason_db_id, event_reason_cd,
                bitmap_db_id,bitmap_tag, event_sdesc,event_dt, event_gdt,ctrl_db_id)
                VALUES 
                (ln_mimDbId, ln_eventId, 0 , 'VN', 
                lrec_EventInfo.stage_reason_db_id, lrec_EventInfo.stage_reason_cd , lrec_EventInfo.hr_db_id,lrec_EventInfo.hr_id,
                lrec_EventInfo.event_status_db_id, lrec_EventInfo.event_status_cd, lrec_EventInfo.event_reason_db_id, lrec_EventInfo.event_reason_cd,
                0,1,lrec_EventInfo.event_sdesc,lrec_EventInfo.event_dt,lrec_EventInfo.event_gdt,ln_mimDbId);
                
                -- Create records in evt_stage table.
                INSERT INTO 
                evt_stage 
                (event_db_id,event_id,stage_id,event_status_db_id,
                event_status_cd,stage_reason_db_id,stage_reason_cd ,hr_db_id,
                hr_id,stage_dt,stage_gdt,user_stage_note,
                system_bool)
                VALUES 
                (ln_mimDbId,ln_eventId,lrec_EventInfo.stage_id,lrec_EventInfo.event_status_db_id,
                lrec_EventInfo.event_status_cd,lrec_EventInfo.event_reason_db_id,lrec_EventInfo.stage_reason_cd,lrec_EventInfo.hr_db_id,
                lrec_EventInfo.hr_id,lrec_EventInfo.stage_dt,lrec_EventInfo.stage_gdt,lrec_EventInfo.user_stage_note,
                0);
                
                -- Link the newly created event to the org_vendor_po_type table.
                INSERT INTO 
                evt_org_vendor_po_type 
                (event_db_id,event_id,
                org_db_id,org_id,
                vendor_db_id,vendor_id,
                po_type_db_id,po_type_cd)
                VALUES 
                (ln_mimDbId, ln_eventId, 
                lrec_Organization.org_db_id, lrec_Organization.org_id, 
                lrec_Vendor.vendor_db_id, lrec_Vendor.vendor_id,
                lrec_Potype.po_type_db_id, lrec_Potype.po_type_cd);
            
            END IF;
        
        END LOOP;
        
        -- Iterate through service types.        
        FOR lrec_serviceType IN lcur_ServiceTypes
        LOOP
        -- Create records in org_vendor_service_type table.
        INSERT INTO 
        org_vendor_service_type 
        (org_db_id,org_id,vendor_db_id,vendor_id,
        service_type_db_id,service_type_cd,vendor_status_db_id,vendor_status_cd,
        approval_expiry_dt,warning_ldesc)
        VALUES 
        (lrec_Organization.org_db_id, lrec_Organization.org_id, lrec_Vendor.vendor_db_id, lrec_Vendor.vendor_id, 
        lrec_serviceType.service_type_db_id, lrec_serviceType.service_type_cd, lrec_Vendor.vendor_status_db_id, lrec_Vendor.vendor_status_cd, 
        lrec_Vendor.approval_expiry_dt, lrec_Vendor.warning_ldesc);               
        
        IF (ln_eventBool = 1) THEN
        
            SELECT event_id_seq.nextval INTO ln_eventId FROM DUAL;
        
            -- Create records in evt_org_vendor_service_type table.
            INSERT INTO 
            evt_event 
            (event_db_id,event_id,event_type_db_id,event_type_cd,
            stage_reason_db_id,stage_reason_cd,editor_hr_db_id, editor_hr_id,
            event_status_db_id,event_status_cd, event_reason_db_id, event_reason_cd,
            bitmap_db_id,bitmap_tag, event_sdesc,event_dt, 
            event_gdt,ctrl_db_id)
            VALUES 
            (ln_mimDbId, ln_eventId, 0 , 'VN', 
            lrec_EventInfo.stage_reason_db_id, lrec_EventInfo.stage_reason_cd , lrec_EventInfo.hr_db_id,lrec_EventInfo.hr_id,
            lrec_EventInfo.event_status_db_id, lrec_EventInfo.event_status_cd, lrec_EventInfo.event_reason_db_id, lrec_EventInfo.event_reason_cd,
            0,1,lrec_EventInfo.event_sdesc,lrec_EventInfo.event_dt,lrec_EventInfo.event_gdt,ln_mimDbId);                                
                
            
            -- Create records in evt_stage table.
            INSERT INTO 
            evt_stage 
            (event_db_id,event_id,stage_id,event_status_db_id,
            event_status_cd,stage_reason_db_id,stage_reason_cd,hr_db_id,
            hr_id,stage_dt,stage_gdt,user_stage_note,
            system_bool)
            VALUES 
            (ln_mimDbId,ln_eventId,lrec_EventInfo.stage_id,lrec_EventInfo.event_status_db_id,
            lrec_EventInfo.event_status_cd,lrec_EventInfo.event_reason_db_id,lrec_EventInfo.stage_reason_cd,lrec_EventInfo.hr_db_id,
            lrec_EventInfo.hr_id,lrec_EventInfo.stage_dt,lrec_EventInfo.stage_gdt,lrec_EventInfo.user_stage_note,
            0);                   
                    
            -- Link the newly created event to org_vendor_service_type table.
            INSERT INTO 
            evt_org_vendor_service_type 
            (event_db_id,event_id,
            org_db_id,org_id,
            vendor_db_id,vendor_id,
            service_type_db_id,service_type_cd)
            VALUES 
            (ln_mimDbId,ln_eventId, 
            lrec_Organization.org_db_id, lrec_Organization.org_id, 
            lrec_Vendor.vendor_db_id, lrec_Vendor.vendor_id,
            lrec_serviceType.service_type_db_id, lrec_serviceType.service_type_cd);
        
        END IF;
        
        END LOOP;
    
    END LOOP;

END LOOP;

END;
/

--changeSet DEV-454:38 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Post migration data model changes.
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ORG_VENDOR', 'FK_REFVNDRSTATUS_ORGVENDOR');
END;
/

--changeSet DEV-454:39 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('ORG_VENDOR', 'VENDOR_STATUS_DB_ID');
END;
/

--changeSet DEV-454:40 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('ORG_VENDOR', 'VENDOR_STATUS_CD');
END;
/

--changeSet DEV-454:41 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('ORG_VENDOR', 'APPROVAL_EXPIRY_DT');
END;
/

--changeSet DEV-454:42 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('ORG_VENDOR', 'WARNING_LDESC');
END;
/

--changeSet DEV-454:43 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ORG_ORG_VENDOR', 'FK_REFVENDSTATUS_ORGORGVEND');
END;
/

--changeSet DEV-454:44 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('ORG_ORG_VENDOR', 'VENDOR_STATUS_DB_ID');
END;
/

--changeSet DEV-454:45 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('ORG_ORG_VENDOR', 'VENDOR_STATUS_CD');
END;
/

--changeSet DEV-454:46 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('ORG_ORG_VENDOR', 'APPROVAL_EXPIRY_DT');
END;
/

--changeSet DEV-454:47 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('ORG_ORG_VENDOR', 'WARNING_LDESC');
END;
/

--changeSet DEV-454:48 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('EVT_ORG_VENDOR');
END;
/