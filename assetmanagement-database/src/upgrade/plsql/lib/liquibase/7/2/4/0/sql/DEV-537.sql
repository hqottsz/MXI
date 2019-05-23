--liquibase formatted sql


--changeSet DEV-537:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table IETM_TOPIC add (
	"APPL_EFF_LDESC" Varchar2 (400)
)
');
END;
/

--changeSet DEV-537:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "IETM_TOPIC_CARRIER" (
	"IETM_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (IETM_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"IETM_ID" Number(10,0) NOT NULL DEFERRABLE  Check (IETM_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"IETM_TOPIC_ID" Number(10,0) NOT NULL DEFERRABLE  Check (IETM_TOPIC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CARRIER_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CARRIER_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CARRIER_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CARRIER_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_IETM_TOPIC_CARRIER" primary key ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID","CARRIER_DB_ID","CARRIER_ID") 
) 
');
END;
/

--changeSet DEV-537:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "IETM_TOPIC_CARRIER" add Constraint "FK_IETMTOPIC_IETMTOPICCARRIER" foreign key ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID") references "IETM_TOPIC" ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID")  DEFERRABLE

');
END;
/

--changeSet DEV-537:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "IETM_TOPIC_CARRIER" add Constraint "FK_ORGCARRIER_IETMTOPICCARRIER" foreign key ("CARRIER_DB_ID","CARRIER_ID") references "ORG_CARRIER" ("CARRIER_DB_ID","CARRIER_ID")  DEFERRABLE

');
END;
/

--changeSet DEV-537:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "IETM_TOPIC_CARRIER" add Constraint "FK_MIMRSTAT_IETMTOPICCARRIER" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE

');
END;
/

--changeSet DEV-537:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_IETM_TOPIC_CARRIER" BEFORE INSERT
   ON "IETM_TOPIC_CARRIER" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-537:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_IETM_TOPIC_CARRIER" BEFORE UPDATE
   ON "IETM_TOPIC_CARRIER" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-537:8 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Migration Script for 1012 AFKLM concept - Operator-specific IETM References
-- (section 4).
--
-- Author: Karan Mehta
-- Date:   September 17, 2010
--
-- Objectives:
-- 1) Map every IETM Topic (technical reference and attachment) in Maintenix,
--    to every Operator in Maintenix, i.e., create an entry in the 
--    IETM_TOPIC_CARRIER table for every permutation of the IETM_TOPC and
--    ORG_CARRIER tables.
--
-- This script is conditional such that if at the beginning of the script
-- a row exists in the IETM_TOPIC_CARRIER table, it is not inserted again.
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
INSERT INTO
  ietm_topic_carrier (
    ietm_db_id,
    ietm_id,
    ietm_topic_id,
    carrier_db_id,
    carrier_id
  )
SELECT
  ietm_topic.ietm_db_id,
  ietm_topic.ietm_id,
  ietm_topic.ietm_topic_id,
  org_carrier.carrier_db_id,
  org_carrier.carrier_id
FROM
  ietm_topic,
  org_carrier
WHERE
  -- Ensure we're not inserting a duplicate in the IETM_TOPIC_CARRIER in
  -- case this script is run again (conditionality)
  NOT EXISTS (
    SELECT 
      ietc.ietm_db_id,
      ietc.ietm_id,
      ietc.ietm_topic_id,
      ietc.carrier_db_id,
      ietc.carrier_id
    FROM
      ietm_topic_carrier ietc
    WHERE
      ietc.ietm_db_id = ietm_topic.ietm_db_id AND
      ietc.ietm_id = ietm_topic.ietm_id AND
      ietc.ietm_topic_id = ietm_topic.ietm_topic_id
      AND
      ietc.carrier_db_id = org_carrier.carrier_db_id AND
      ietc.carrier_id = org_carrier.carrier_id
    )
  AND
  ietm_topic.rstat_cd = 0
  AND
  org_carrier.rstat_cd = 0;

--changeSet DEV-537:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'IETM_TOPIC_CARRIER');
END;
/