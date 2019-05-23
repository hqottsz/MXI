--liquibase formatted sql


--changeSet OPER-3153:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Add Supply Chain Indicator for Aircraft Operator
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Create table REF_SUPPLY_CHAIN
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
BEGIN
utl_migr_schema_pkg.table_create('
 Create table "REF_SUPPLY_CHAIN" (
	"SUPPLY_CHAIN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (SUPPLY_CHAIN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"SUPPLY_CHAIN_CD" Varchar2 (8) NOT NULL DEFERRABLE  UNIQUE DEFERRABLE ,
	"DESC_SDESC" Varchar2 (80),
	"DESC_LDESC" Varchar2 (4000),
	"RSTAT_CD" Number(3,0) NOT NULL  Check (RSTAT_CD IN (0, 1, 2, 3) ) ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_REF_SUPPLY_CHAIN" primary key ("SUPPLY_CHAIN_DB_ID","SUPPLY_CHAIN_CD") 
) 
');
END;
/

--changeSet OPER-3153:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_SUPPLY_CHAIN" add Constraint "FK_MIMDB_REFSUPPLYCHAIN" foreign key ("SUPPLY_CHAIN_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet OPER-3153:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_SUPPLY_CHAIN" add Constraint "FK_MIMRSTAT_REFSUPPLYCHAIN" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet OPER-3153:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add audit triggers
CREATE OR REPLACE TRIGGER "TIBR_REF_SUPPLY_CHAIN" BEFORE INSERT
   ON "REF_SUPPLY_CHAIN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-3153:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_SUPPLY_CHAIN" BEFORE UPDATE
   ON "REF_SUPPLY_CHAIN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-3153:6 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Insert 0-level data to REF_SUPPLY_CHAIN table
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
INSERT INTO REF_SUPPLY_CHAIN( SUPPLY_CHAIN_DB_ID, SUPPLY_CHAIN_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'DEFAULT', 'Material requests fulfilled internally', 'The material requests are fulfilled by Maintenix Operator Edition.', 0, TO_DATE('2015-04-21','YYYY-MM-DD'), TO_DATE('2015-04-21','YYYY-MM-DD'), 100, 'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM REF_SUPPLY_CHAIN WHERE SUPPLY_CHAIN_CD = 'DEFAULT');

--changeSet OPER-3153:7 stripComments:false
INSERT INTO REF_SUPPLY_CHAIN( SUPPLY_CHAIN_DB_ID, SUPPLY_CHAIN_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 0, 'EXTERNAL', 'Material requests fulfilled externally', 'The material requests are fulfilled by an external supply chain.', 0, TO_DATE('2015-04-21','YYYY-MM-DD'), TO_DATE('2015-04-21','YYYY-MM-DD'), 100, 'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM REF_SUPPLY_CHAIN WHERE SUPPLY_CHAIN_CD = 'EXTERNAL');

--changeSet OPER-3153:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Add new supply chain columns to ORG_CARRIER
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- add foreign key "SUPPLY_CHAIN_DB_ID","SUPPLY_CHAIN_CD" to ORG_CARRIER table
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table "ORG_CARRIER" add (
   "SUPPLY_CHAIN_DB_ID" Number(10,0) Check (SUPPLY_CHAIN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet OPER-3153:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table "ORG_CARRIER" add (
   "SUPPLY_CHAIN_CD" Varchar2 (8) )
');
END;
/

--changeSet OPER-3153:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add foreign key constraint from REF_SUPPLY_CHAIN to ORG_CARRIER FK_SUPPLYCHAIN_ORGCARRIER
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_CARRIER" add Constraint "FK_SUPPLYCHAIN_ORGCARRIER" foreign key ("SUPPLY_CHAIN_DB_ID","SUPPLY_CHAIN_CD") references "REF_SUPPLY_CHAIN" ("SUPPLY_CHAIN_DB_ID","SUPPLY_CHAIN_CD")  DEFERRABLE
');
END;
/

--changeSet OPER-3153:11 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Populate the default values for the new supply chain columns in ORG_CARRIER
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
UPDATE org_carrier
SET supply_chain_db_id = 0,
    supply_chain_cd = 'DEFAULT'
WHERE
    supply_chain_db_id IS NULL
	AND
	(carrier_db_id, carrier_id) NOT IN ( (0, 1000) );   