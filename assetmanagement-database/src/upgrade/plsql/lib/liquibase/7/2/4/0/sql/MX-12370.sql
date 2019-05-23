--liquibase formatted sql


--changeSet MX-12370:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create REF_FINANCE_TYPE with keys and its corresponding trigger
BEGIN
    utl_migr_schema_pkg.table_create('
      Create table "REF_FINANCE_TYPE" (
      	"FINANCE_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FINANCE_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      	"FINANCE_TYPE_CD" Varchar2 (8) Constraint "NN_FINANTYPECD" NOT NULL DEFERRABLE ,
      	"DESC_SDESC" Varchar2 (80) NOT NULL DEFERRABLE ,
      	"DESC_LDESC" Varchar2 (4000),
      	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      	"CREATION_DT" Date NOT NULL DEFERRABLE ,
      	"REVISION_DT" Date NOT NULL DEFERRABLE ,
      	"REVISION_DB_ID" Number(10,0) Constraint "NN_REFFNTYPEDBID" NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      	"REVISION_USER" Varchar2 (30) Constraint "NN_REFFNTYPEUSER" NOT NULL DEFERRABLE ,
       Constraint "PK_REF_FINANCE_TYPE" primary key ("FINANCE_TYPE_DB_ID","FINANCE_TYPE_CD") 
      )
    ');
END;
/

--changeSet MX-12370:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_add('
      Alter table "REF_FINANCE_TYPE" add Constraint "FK_MIMDB_REFFINANCETYPE" foreign key ("FINANCE_TYPE_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
    ');
END;
/

--changeSet MX-12370:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_add('
      Alter table "REF_FINANCE_TYPE" add Constraint "FK_MIMRSTAT_REFFINANCETYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
    ');
END;
/

--changeSet MX-12370:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE  OR REPLACE TRIGGER "TIBR_REF_FINANCE_TYPE" BEFORE INSERT
   ON "REF_FINANCE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MX-12370:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE  OR REPLACE TRIGGER "TUBR_REF_FINANCE_TYPE" BEFORE UPDATE
   ON "REF_FINANCE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MX-12370:6 stripComments:false
-- Populate REF_FINANCE_TYPE
insert into REF_FINANCE_TYPE (FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'ROTABLE', 'ROTABLE', 'ROTABLE', 0, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI' FROM dual WHERE
   NOT EXISTS (
      SELECT 1 FROM REF_FINANCE_TYPE WHERE
         FINANCE_TYPE_DB_ID = 0 AND
         FINANCE_TYPE_CD = 'ROTABLE'
   );   

--changeSet MX-12370:7 stripComments:false
insert into REF_FINANCE_TYPE (FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'CONSUM', 'CONSUMABLE', 'CONSUMABLE', 0, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI' FROM dual WHERE
   NOT EXISTS (
      SELECT 1 FROM REF_FINANCE_TYPE WHERE
         FINANCE_TYPE_DB_ID = 0 AND
         FINANCE_TYPE_CD = 'CONSUM'
   );   

--changeSet MX-12370:8 stripComments:false
insert into REF_FINANCE_TYPE (FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'EXPENSE', 'EXPENDABLE', 'EXPENDABLE', 0, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI' FROM dual WHERE
   NOT EXISTS (
      SELECT 1 FROM REF_FINANCE_TYPE WHERE
         FINANCE_TYPE_DB_ID = 0 AND
         FINANCE_TYPE_CD = 'EXPENSE'
   );   

--changeSet MX-12370:9 stripComments:false
insert into REF_FINANCE_TYPE (FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'KIT', 'KIT', 'KIT', 0, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI' FROM dual WHERE
   NOT EXISTS (
      SELECT 1 FROM REF_FINANCE_TYPE WHERE
         FINANCE_TYPE_DB_ID = 0 AND
         FINANCE_TYPE_CD = 'KIT'
   );   

--changeSet MX-12370:10 stripComments:false
insert into REF_FINANCE_TYPE (FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'BLKOUT', 'BLKOUT', 'N/A', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI' FROM dual WHERE
   NOT EXISTS (
      SELECT 1 FROM REF_FINANCE_TYPE WHERE
         FINANCE_TYPE_DB_ID = 0 AND
         FINANCE_TYPE_CD = 'BLKOUT'
   );   

--changeSet MX-12370:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add FINANCE_TYPE_DB_ID column to REF_FINANCIAL_CLASS and add foreign key
BEGIN
    utl_migr_schema_pkg.table_column_add('
      Alter table "REF_FINANCIAL_CLASS" 
      ADD (
         "FINANCE_TYPE_DB_ID" Number(10,0)
      )
    ');
END;
/

--changeSet MX-12370:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
  EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_REF_FINANCIAL_CLASS DISABLE';
END;
/

--changeSet MX-12370:13 stripComments:false
update REF_FINANCIAL_CLASS set FINANCE_TYPE_DB_ID = 0 where FINANCE_TYPE_DB_ID is null;

--changeSet MX-12370:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
  EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_REF_FINANCIAL_CLASS ENABLE';
END;
/

--changeSet MX-12370:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_column_modify('
      Alter table "REF_FINANCIAL_CLASS" 
      MODIFY (
         "FINANCE_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FINANCE_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
      )
    ');
END;
/

--changeSet MX-12370:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_add('
      Alter table "REF_FINANCIAL_CLASS" add Constraint "FK_REFFINANCETYPE_REFFINANCECL" foreign key ("FINANCE_TYPE_DB_ID","FINANCE_TYPE_CD") references "REF_FINANCE_TYPE" ("FINANCE_TYPE_DB_ID","FINANCE_TYPE_CD")  DEFERRABLE
    ');
END;
/