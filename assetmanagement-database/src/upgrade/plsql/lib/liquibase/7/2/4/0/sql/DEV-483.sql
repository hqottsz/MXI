--liquibase formatted sql


--changeSet DEV-483:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Migration script for 1012 AFKLM concept (section 3.2)
-- Author: Karan Mehta
-- Date:   October 1, 2010
--
-- Conditional migration script to for new data-model changes in 
-- DEV-483. New table was added - FAIL_DEFER_CARRIER
-- Also, all operators in the database are mapped to all the entries
-- in FAIL_DEFER_REF to populate the FAIL_DEFER_CARRIER table
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
BEGIN

-- Create the new table FAIL_DEFER_CARRIER
utl_migr_schema_pkg.table_create('
Create table "FAIL_DEFER_CARRIER" (
	"FAIL_DEFER_REF_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FAIL_DEFER_REF_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"FAIL_DEFER_REF_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FAIL_DEFER_REF_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CARRIER_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CARRIER_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CARRIER_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CARRIER_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_FAIL_DEFER_CARRIER" primary key ("FAIL_DEFER_REF_DB_ID","FAIL_DEFER_REF_ID","CARRIER_DB_ID","CARRIER_ID") 
)
');


-- Add the foreign key constraints
utl_migr_schema_pkg.table_constraint_add('
Alter table "FAIL_DEFER_CARRIER" add Constraint "FK_ORGCARRIER_FAILDEFCARRIER" foreign key ("CARRIER_DB_ID","CARRIER_ID") references "ORG_CARRIER" ("CARRIER_DB_ID","CARRIER_ID")  DEFERRABLE
');
utl_migr_schema_pkg.table_constraint_add('
Alter table "FAIL_DEFER_CARRIER" add Constraint "FK_MIMRSTAT_FAILDEFCARRIER" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
utl_migr_schema_pkg.table_constraint_add('
Alter table "FAIL_DEFER_CARRIER" add Constraint "FK_FAILDEFREF_FAILDEFCARRIER" foreign key ("FAIL_DEFER_REF_DB_ID","FAIL_DEFER_REF_ID") references "FAIL_DEFER_REF" ("FAIL_DEFER_REF_DB_ID","FAIL_DEFER_REF_ID")  DEFERRABLE
');

END;
/

--changeSet DEV-483:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create the new triggers for the new table
CREATE OR REPLACE TRIGGER "TIBR_FAIL_DEFER_CARRIER" BEFORE INSERT
   ON "FAIL_DEFER_CARRIER" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-483:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_FAIL_DEFER_CARRIER" BEFORE UPDATE
   ON "FAIL_DEFER_CARRIER" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-483:4 stripComments:false
-- All operators in the database are mapped to all the entries
-- in FAIL_DEFER_REF to populate the FAIL_DEFER_CARRIER table
INSERT INTO
  fail_defer_carrier (
    fail_defer_ref_db_id,
    fail_defer_ref_id,
    carrier_db_id,
    carrier_id
  )
SELECT
  fail_defer_ref.fail_defer_ref_db_id,
  fail_defer_ref.fail_defer_ref_id,
  org_carrier.carrier_db_id,
  org_carrier.carrier_id
FROM
  fail_defer_ref,
  org_carrier
WHERE
  -- Ensure we're not inserting a duplicate in the FAIL_DEFER_CARRIER in
  -- case this script is run again (conditionality)
  NOT EXISTS (
    SELECT 
      failc.fail_defer_ref_db_id,
      failc.fail_defer_ref_id,
      failc.carrier_db_id,
      failc.carrier_id
    FROM
      fail_defer_carrier failc
    WHERE
      failc.fail_defer_ref_db_id = fail_defer_ref.fail_defer_ref_db_id AND
      failc.fail_defer_ref_id = fail_defer_ref.fail_defer_ref_id
      AND
      failc.carrier_db_id = org_carrier.carrier_db_id AND
      failc.carrier_id = org_carrier.carrier_id
    )
  AND
  fail_defer_ref.rstat_cd = 0
  AND
  org_carrier.rstat_cd = 0;

--changeSet DEV-483:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'FAIL_DEFER_CARRIER');
END;
/