--liquibase formatted sql


--changeSet DEV-374:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_EVENT add (
	"CALC_DUR_BUFFER" Float
)
');
END;
/

--changeSet DEV-374:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_EVENT add (
	"ACTUAL_DUR_BUFFER_DAYS" Float
)
');
END;
/

--changeSet DEV-374:3 stripComments:false
UPDATE LRP_EVENT SET ACTUAL_DUR_BUFFER_DAYS = 0 WHERE ACTUAL_DUR_BUFFER_DAYS IS NULL;

--changeSet DEV-374:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_EVENT modify (
	"ACTUAL_DUR_BUFFER_DAYS" Float Default 0 NOT NULL DEFERRABLE 
)
');
END;
/

--changeSet DEV-374:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_EVENT add (
	"ACTUAL_DUR_BUFFER_PCT" Float 
)
');
END;
/

--changeSet DEV-374:6 stripComments:false
UPDATE LRP_EVENT SET ACTUAL_DUR_BUFFER_PCT = 0 WHERE ACTUAL_DUR_BUFFER_PCT IS NULL;

--changeSet DEV-374:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_EVENT modify (
	"ACTUAL_DUR_BUFFER_PCT" Float Default 0 NOT NULL DEFERRABLE  Check (ACTUAL_DUR_BUFFER_PCT BETWEEN 0 AND 1 ) DEFERRABLE 
)
');
END;
/

--changeSet DEV-374:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_EVENT add (
	"BUCKET_CALC_BOOL" Number(1,0) 
)
');
END;
/

--changeSet DEV-374:9 stripComments:false
UPDATE LRP_EVENT SET BUCKET_CALC_BOOL = 1 WHERE BUCKET_CALC_BOOL IS NULL;

--changeSet DEV-374:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_EVENT modify (
	"BUCKET_CALC_BOOL" Number(1,0) Default 1 NOT NULL DEFERRABLE  Check (BUCKET_CALC_BOOL IN (0, 1) ) DEFERRABLE 
)
');
END;
/

--changeSet DEV-374:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_TASK_DEFN add (
	"DUR_BUFFER_PCT" Float 
)
');
END;
/

--changeSet DEV-374:12 stripComments:false
UPDATE LRP_TASK_DEFN SET DUR_BUFFER_PCT = 0 WHERE DUR_BUFFER_PCT IS NULL;

--changeSet DEV-374:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_TASK_DEFN modify (
	"DUR_BUFFER_PCT" Float Default 0 NOT NULL DEFERRABLE 
)
');
END;
/

--changeSet DEV-374:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_TASK_DEFN add (
	"ROUTINE_HRS" Number(8,2) 
)
');
END;
/

--changeSet DEV-374:15 stripComments:false
UPDATE LRP_TASK_DEFN SET ROUTINE_HRS = 0 WHERE ROUTINE_HRS IS NULL;

--changeSet DEV-374:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_TASK_DEFN modify (
	"ROUTINE_HRS" Number(8,2) Default 0 NOT NULL DEFERRABLE 
)
');
END;
/

--changeSet DEV-374:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_TASK_DEFN add (
	"NR_HRS" Number(8,2) 
)
');
END;
/

--changeSet DEV-374:18 stripComments:false
UPDATE LRP_TASK_DEFN SET NR_HRS = 0 WHERE NR_HRS IS NULL;

--changeSet DEV-374:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_TASK_DEFN modify (
	"NR_HRS" Number(8,2) Default 0 NOT NULL DEFERRABLE 
)
');
END;
/

--changeSet DEV-374:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_TASK_DEFN add (
	"TOTAL_HRS" Number(10,2) 
)
');
END;
/

--changeSet DEV-374:21 stripComments:false
UPDATE LRP_TASK_DEFN SET TOTAL_HRS = 0 WHERE TOTAL_HRS IS NULL;

--changeSet DEV-374:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_TASK_DEFN modify (
	"TOTAL_HRS" Number(10,2) Default 0 NOT NULL DEFERRABLE 
)
');
END;
/

--changeSet DEV-374:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LRP_TASK_PLAN_RANGE" (
	"TASK_PLAN_RANGE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_PLAN_RANGE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TASK_PLAN_RANGE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_PLAN_RANGE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TASK_DEFN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DEFN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TASK_DEFN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DEFN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"EFFECTIVE_FROM_DT" Date NOT NULL DEFERRABLE ,
	"EFFECTIVE_TO_DT" Date,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LRP_TASK_PLAN_RANGE" primary key ("TASK_PLAN_RANGE_DB_ID","TASK_PLAN_RANGE_ID") 
) 
');
END;
/

--changeSet DEV-374:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LRP_TASK_BUCKET" (
	"TASK_PLAN_RANGE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_PLAN_RANGE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TASK_PLAN_RANGE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_PLAN_RANGE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_PLAN_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_PLAN_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_PLAN_TYPE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_PLAN_TYPE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"ROUTINE_HRS" Number(8,2) NOT NULL DEFERRABLE ,
	"NR_FACTOR" Number(3,2) NOT NULL DEFERRABLE ,
	"NR_HRS" Number(8,2) NOT NULL DEFERRABLE ,
	"TOTAL_HRS" Number(10,2) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LRP_TASK_BUCKET" primary key ("TASK_PLAN_RANGE_DB_ID","TASK_PLAN_RANGE_ID","LRP_PLAN_TYPE_DB_ID","LRP_PLAN_TYPE_ID") 
) 
');
END;
/

--changeSet DEV-374:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LRP_PLAN_TYPE" (
	"LRP_PLAN_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_PLAN_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_PLAN_TYPE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_PLAN_TYPE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"PLANNING_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PLANNING_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"PLANNING_TYPE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PLANNING_TYPE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LRP_PLAN_TYPE" primary key ("LRP_PLAN_TYPE_DB_ID","LRP_PLAN_TYPE_ID") 
) 
');
END;
/

--changeSet DEV-374:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- The LRP_EVENT_BUCKET table will be altered in the DEV-376.sql migration script to add NOT NULL constraints to the
-- ROUTINE_HRS, NR_FACTOR, NR_HRS, TOTAL_HRS, and ASSIGNED_HRS columns after the table has been populated.
BEGIN
utl_migr_schema_pkg.table_create('
Create table "LRP_EVENT_BUCKET" (
	"LRP_EVENT_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_EVENT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_EVENT_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_EVENT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_PLAN_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_PLAN_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"LRP_PLAN_TYPE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LRP_PLAN_TYPE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"ROUTINE_HRS" Number(8,2) ,
	"NR_FACTOR" Number(3,2) ,
	"NR_HRS" Number(8,2) ,
	"TOTAL_HRS" Number(10,2) ,
	"ASSIGNED_HRS" Number(8,2) ,
	"FILL_PCT" Number(10,0),
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_LRP_EVENT_BUCKET" primary key ("LRP_EVENT_DB_ID","LRP_EVENT_ID","LRP_PLAN_TYPE_DB_ID","LRP_PLAN_TYPE_ID") 
) 
');
END;
/

--changeSet DEV-374:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_TASK_PLAN_RANGE" add Constraint "FK_MIMRSTAT_TSKPRNG" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-374:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_PLAN_TYPE" add Constraint "FK_MIMRSTAT_LRPPTYP" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-374:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_TASK_BUCKET" add Constraint "FK_MIMRSTAT_TSKBKT" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-374:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_EVENT_BUCKET" add Constraint "FK_MIMRSTAT_LRPEBKT" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-374:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_PLAN_TYPE" add Constraint "FK_LRPPLN_LRPPLNTYP" foreign key ("LRP_DB_ID","LRP_ID") references "LRP_PLAN" ("LRP_DB_ID","LRP_ID")  DEFERRABLE


');
END;
/

--changeSet DEV-374:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_TASK_PLAN_RANGE" add Constraint "FK_LRPTDFN_TSKPRNG" foreign key ("LRP_DB_ID","LRP_ID","TASK_DEFN_DB_ID","TASK_DEFN_ID") references "LRP_TASK_DEFN" ("LRP_DB_ID","LRP_ID","TASK_DEFN_DB_ID","TASK_DEFN_ID")  DEFERRABLE

');
END;
/

--changeSet DEV-374:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_EVENT_BUCKET" add Constraint "FK_LRPEVT_EVTBKT" foreign key ("LRP_EVENT_DB_ID","LRP_EVENT_ID") references "LRP_EVENT" ("LRP_EVENT_DB_ID","LRP_EVENT_ID")  DEFERRABLE


');
END;
/

--changeSet DEV-374:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_PLAN_TYPE" add Constraint "FK_LRPPTYP_EQPPTYPE" foreign key ("PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID") references "EQP_PLANNING_TYPE" ("PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID")  DEFERRABLE

');
END;
/

--changeSet DEV-374:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_TASK_BUCKET" add Constraint "FK_LRPTSKPLRNG_TBKT" foreign key ("TASK_PLAN_RANGE_DB_ID","TASK_PLAN_RANGE_ID") references "LRP_TASK_PLAN_RANGE" ("TASK_PLAN_RANGE_DB_ID","TASK_PLAN_RANGE_ID")  DEFERRABLE

');
END;
/

--changeSet DEV-374:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_TASK_BUCKET" add Constraint "FK_LRPPTYP_LRPTBKT" foreign key ("LRP_PLAN_TYPE_DB_ID","LRP_PLAN_TYPE_ID") references "LRP_PLAN_TYPE" ("LRP_PLAN_TYPE_DB_ID","LRP_PLAN_TYPE_ID")  DEFERRABLE


');
END;
/

--changeSet DEV-374:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "LRP_EVENT_BUCKET" add Constraint "FK_LRPPTYP_EVTBKT" foreign key ("LRP_PLAN_TYPE_DB_ID","LRP_PLAN_TYPE_ID") references "LRP_PLAN_TYPE" ("LRP_PLAN_TYPE_DB_ID","LRP_PLAN_TYPE_ID")  DEFERRABLE

');
END;
/

--changeSet DEV-374:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LRP_TASK_PLAN_RANGE" BEFORE INSERT
   ON "LRP_TASK_PLAN_RANGE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-374:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LRP_TASK_PLAN_RANGE" BEFORE UPDATE
   ON "LRP_TASK_PLAN_RANGE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-374:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LRP_TASK_BUCKET" BEFORE INSERT
   ON "LRP_TASK_BUCKET" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-374:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LRP_TASK_BUCKET" BEFORE UPDATE
   ON "LRP_TASK_BUCKET" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-374:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LRP_PLAN_TYPE" BEFORE INSERT
   ON "LRP_PLAN_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-374:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LRP_PLAN_TYPE" BEFORE UPDATE
   ON "LRP_PLAN_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-374:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LRP_EVENT_BUCKET" BEFORE INSERT
   ON "LRP_EVENT_BUCKET" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-374:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_LRP_EVENT_BUCKET" BEFORE UPDATE
   ON "LRP_EVENT_BUCKET" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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