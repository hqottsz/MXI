--liquibase formatted sql


--changeSet DEV-395:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "UTL_PURGE_GROUP" (
	"PURGE_GROUP_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"GROUP_NAME" Varchar2 (40) NOT NULL DEFERRABLE ,
	"GROUP_LDESC" Varchar2 (4000),
	"UTL_ID" Number(10,0) NOT NULL DEFERRABLE  Check (UTL_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_UTL_PURGE_GROUP" primary key ("PURGE_GROUP_CD") 
) 
');
END;
/

--changeSet DEV-395:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "UTL_PURGE_POLICY" (
	"PURGE_POLICY_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"PURGE_GROUP_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"POLICY_NAME" Varchar2 (40) NOT NULL DEFERRABLE ,
	"POLICY_LDESC" Varchar2 (4000),
	"RETENTION_PERIOD" Number(5,0) NOT NULL DEFERRABLE ,
	"ACTIVE_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (ACTIVE_BOOL IN (0, 1) ) DEFERRABLE ,
	"UTL_ID" Number(10,0) NOT NULL DEFERRABLE  Check (UTL_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_UTL_PURGE_POLICY" primary key ("PURGE_POLICY_CD") 
) 
');
END;
/

--changeSet DEV-395:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "UTL_PURGE_STRATEGY" (
	"PURGE_POLICY_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"PURGE_TABLE_CD" Varchar2 (30) NOT NULL DEFERRABLE ,
	"PURGE_ORD" Varchar2 (20) NOT NULL DEFERRABLE ,
	"PREDICATE_SQL" Varchar2 (4000) NOT NULL DEFERRABLE ,
	"ARCHIVE_TABLE" Varchar2 (30),
	"UTL_ID" Number(10,0) NOT NULL DEFERRABLE  Check (UTL_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_UTL_PURGE_STRATEGY" primary key ("PURGE_POLICY_CD","PURGE_TABLE_CD") 
) 
');
END;
/

--changeSet DEV-395:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "UTL_PURGE_TABLE" (
	"PURGE_TABLE_CD" Varchar2 (30) NOT NULL DEFERRABLE ,
	"UTL_ID" Number(10,0) NOT NULL DEFERRABLE  Check (UTL_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_UTL_PURGE_TABLE" primary key ("PURGE_TABLE_CD") 
) 
');
END;
/

--changeSet DEV-395:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_PURGE_GROUP" add Constraint "FK_MIMDB_UTLPURGEGRP" foreign key ("UTL_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE

');
END;
/

--changeSet DEV-395:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_PURGE_POLICY" add Constraint "FK_MIMDB_UTLPURGEPOL" foreign key ("UTL_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE

');
END;
/

--changeSet DEV-395:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_PURGE_STRATEGY" add Constraint "FK_MIMDB_UTLPURGESTR" foreign key ("UTL_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE

');
END;
/

--changeSet DEV-395:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_PURGE_TABLE" add Constraint "FK_MIMDB_UTLPURGETBL" foreign key ("UTL_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE

');
END;
/

--changeSet DEV-395:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_PURGE_GROUP" add Constraint "FK_MIMRSTAT_UTLPURGEGRP" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-395:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_PURGE_POLICY" add Constraint "FK_MIMRSTAT_UTLPURGEPOL" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-395:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_PURGE_STRATEGY" add Constraint "FK_MIMRSTAT_UTLPURGESTR" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-395:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_PURGE_TABLE" add Constraint "FK_MIMRSTAT_UTLPURGETBL" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-395:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_PURGE_POLICY" add Constraint "FK_UTLPURGEGRP_UTLPURGEPOL" foreign key ("PURGE_GROUP_CD") references "UTL_PURGE_GROUP" ("PURGE_GROUP_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-395:14 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Foreign key constraint created in 7.x with a different name, therefore, 
-- conditionally drop the existing foreign key before creating the new constraint.
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('UTL_PURGE_STRATEGY', 'FK_UTLPURGEPOL_UTL_PURGESTR');
END;
/

--changeSet DEV-395:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_PURGE_STRATEGY" add Constraint "FK_UTLPURGEPOL_UTLPURGESTR" foreign key ("PURGE_POLICY_CD") references "UTL_PURGE_POLICY" ("PURGE_POLICY_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-395:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_PURGE_STRATEGY" add Constraint "FK_UTLPURGETBL_UTLPURGESTR" foreign key ("PURGE_TABLE_CD") references "UTL_PURGE_TABLE" ("PURGE_TABLE_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-395:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_UTL_PURGE_GROUP" BEFORE INSERT
   ON "UTL_PURGE_GROUP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-395:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_UTL_PURGE_GROUP" BEFORE UPDATE
   ON "UTL_PURGE_GROUP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-395:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_UTL_PURGE_POLICY" BEFORE INSERT
   ON "UTL_PURGE_POLICY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-395:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_UTL_PURGE_POLICY" BEFORE UPDATE
   ON "UTL_PURGE_POLICY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-395:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_UTL_PURGE_STRATEGY" BEFORE INSERT
   ON "UTL_PURGE_STRATEGY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-395:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_UTL_PURGE_STRATEGY" BEFORE UPDATE
   ON "UTL_PURGE_STRATEGY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-395:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_UTL_PURGE_TABLE" BEFORE INSERT
   ON "UTL_PURGE_TABLE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-395:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_UTL_PURGE_TABLE" BEFORE UPDATE
   ON "UTL_PURGE_TABLE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-395:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table UTL_PURGE_STRATEGY modify (
    "PURGE_ORD" Number(5,0) NOT NULL DEFERRABLE
  )
  ');
 END;
 /   

--changeSet DEV-395:26 stripComments:false
INSERT INTO
   utl_purge_policy
   (
      purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 'ALUNARCHIVED', 'ALERTS', 'Unarchived Alerts', 'Purges any alerts that exceed the retention period and do not have a status of ARCHIVE', 30, 1, 0, 0, sysdate, sysdate, 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_purge_policy WHERE purge_policy_cd = 'ALUNARCHIVED'  );          

--changeSet DEV-395:27 stripComments:false
INSERT INTO
   utl_purge_group
   (
      purge_group_cd, group_name, group_ldesc, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 'ALERTS', 'Alerts', 'A set of purging policies related to alerts', 0, 0, sysdate, sysdate, 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_purge_policy WHERE purge_group_cd = 'ALERTS'  );         

--changeSet DEV-395:28 stripComments:false
INSERT INTO
   utl_purge_table
   (
      purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 'UTL_ALERT', 0, 0, sysdate, sysdate, 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_purge_table WHERE purge_table_cd = 'UTL_ALERT'  );               

--changeSet DEV-395:29 stripComments:false
INSERT INTO
   utl_purge_table
   (
      purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 'UTL_ALERT_LOG', 0, 0, sysdate, sysdate, 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_purge_table WHERE purge_table_cd = 'UTL_ALERT_LOG'  );        

--changeSet DEV-395:30 stripComments:false
INSERT INTO
   utl_purge_table
   (
      purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 'UTL_ALERT_PARM', 0, 0, sysdate, sysdate, 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_purge_table WHERE purge_table_cd = 'UTL_ALERT_PARM'  );        

--changeSet DEV-395:31 stripComments:false
INSERT INTO
   utl_purge_table
   (
      purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 'UTL_ALERT_STATUS_LOG', 0, 0, sysdate, sysdate, 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_purge_table WHERE purge_table_cd = 'UTL_ALERT_STATUS_LOG'  );        

--changeSet DEV-395:32 stripComments:false
INSERT INTO
   utl_purge_table
   (
      purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 'UTL_USER_ALERT', 0, 0, sysdate, sysdate, 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_purge_table WHERE purge_table_cd = 'UTL_USER_ALERT'  );                    

--changeSet DEV-395:33 stripComments:false
INSERT INTO
   utl_purge_strategy
   (
      purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 'ALUNARCHIVED', 'UTL_ALERT_LOG',10, 'alert_id IN (SELECT alert_id FROM utl_alert WHERE alert_timestamp <= TRUNC( sysdate ) - :1)',0, 0, sysdate, sysdate, 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'ALUNARCHIVED' and  purge_table_cd = 'UTL_ALERT_LOG' );         

--changeSet DEV-395:34 stripComments:false
INSERT INTO
   utl_purge_strategy
   (
      purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 'ALUNARCHIVED', 'UTL_ALERT_PARM',20, 'alert_id IN (SELECT alert_id FROM utl_alert WHERE alert_timestamp <= TRUNC( sysdate ) - :1)',0, 0, sysdate, sysdate, 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'ALUNARCHIVED' and  purge_table_cd = 'UTL_ALERT_PARM' );   

--changeSet DEV-395:35 stripComments:false
INSERT INTO
   utl_purge_strategy
   (
      purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 'ALUNARCHIVED', 'UTL_ALERT_STATUS_LOG',30, 'alert_id IN (SELECT alert_id FROM utl_alert WHERE alert_timestamp <= TRUNC( sysdate ) - :1)',0, 0, sysdate, sysdate, 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'ALUNARCHIVED' and  purge_table_cd = 'UTL_ALERT_STATUS_LOG' );   

--changeSet DEV-395:36 stripComments:false
INSERT INTO
   utl_purge_strategy
   (
      purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 'ALUNARCHIVED', 'UTL_USER_ALERT',40, 'alert_id IN (SELECT alert_id FROM utl_alert WHERE alert_timestamp <= TRUNC( sysdate ) - :1)',0, 0, sysdate, sysdate, 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'ALUNARCHIVED' and  purge_table_cd = 'UTL_USER_ALERT' );   

--changeSet DEV-395:37 stripComments:false
INSERT INTO
   utl_purge_strategy
   (
      purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 'ALUNARCHIVED', 'UTL_ALERT',50, 'alert_timestamp <= TRUNC( sysdate ) - :1',0, 0, sysdate, sysdate, 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_purge_strategy WHERE purge_policy_cd = 'ALUNARCHIVED' and  purge_table_cd = 'UTL_ALERT' );         