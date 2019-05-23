--liquibase formatted sql


--changeSet MX-17691:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create REF_THRESHOLD_MODE with its corresponding trigger
/*******************************************************************************
 * Set up the REF_THRESHOLD_MODE table with its corresponding 0-level data
 ******************************************************************************/
BEGIN
    utl_migr_schema_pkg.table_create('
      Create table "REF_THRESHOLD_MODE" (
              "THRESHOLD_MODE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (THRESHOLD_MODE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
              "THRESHOLD_MODE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
              "DESC_SDESC" Varchar2 (80) NOT NULL DEFERRABLE ,
              "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
              "CREATION_DT" Date NOT NULL DEFERRABLE ,
              "REVISION_DT" Date NOT NULL DEFERRABLE ,
              "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
              "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
       Constraint "PK_REF_THRESHOLD_MODE" primary key ("THRESHOLD_MODE_DB_ID","THRESHOLD_MODE_CD") 
      ) 
    ');
END;
/

--changeSet MX-17691:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_add('
      Alter table "REF_THRESHOLD_MODE" add Constraint "FK_MIMDB_REFTHMODE" foreign key ("THRESHOLD_MODE_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
    ');
END;
/

--changeSet MX-17691:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_add('
      Alter table "REF_THRESHOLD_MODE" add Constraint "FK_MIMRSTAT_REFTHMODE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
    ');
END;
/

--changeSet MX-17691:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_THRESHOLD_MODE" BEFORE INSERT
   ON "REF_THRESHOLD_MODE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MX-17691:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_THRESHOLD_MODE" BEFORE UPDATE
   ON "REF_THRESHOLD_MODE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MX-17691:6 stripComments:false
-- Populate REF_THRESHOLD_MODE
INSERT INTO REF_THRESHOLD_MODE (THRESHOLD_MODE_DB_ID, THRESHOLD_MODE_CD, DESC_SDESC)
SELECT 0, 'FLEET', 'Fleet' FROM dual WHERE
   NOT EXISTS (
      SELECT 1 FROM REF_THRESHOLD_MODE WHERE
         THRESHOLD_MODE_DB_ID = 0 AND
         THRESHOLD_MODE_CD = 'FLEET'
   );

--changeSet MX-17691:7 stripComments:false
INSERT INTO REF_THRESHOLD_MODE (THRESHOLD_MODE_DB_ID, THRESHOLD_MODE_CD, DESC_SDESC)
SELECT 0, 'ASSMBL', 'Assembly' FROM dual WHERE
   NOT EXISTS (
      SELECT 1 FROM REF_THRESHOLD_MODE WHERE
         THRESHOLD_MODE_DB_ID = 0 AND
         THRESHOLD_MODE_CD = 'ASSMBL'
   );

--changeSet MX-17691:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add foreign key to EQP_ASSMBL_BOM_THRESHOLD
/*******************************************************************************
 * Add mandatory foreign key from EQP_ASSMBL_BOM_THRESHOLD to REF_THRESHOLD_MODE
 * and default the value to 0:FLEET
 ******************************************************************************/
BEGIN
    utl_migr_schema_pkg.table_column_add('
      ALTER TABLE "EQP_ASSMBL_BOM_THRESHOLD"
      ADD (
    "THRESHOLD_MODE_DB_ID" Number(10,0) 
      )
    ');
END;
/

--changeSet MX-17691:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_column_add('
      ALTER TABLE "EQP_ASSMBL_BOM_THRESHOLD"
      ADD (
        "THRESHOLD_MODE_CD" Varchar2 (8) 
      )
    ');
END;
/

--changeSet MX-17691:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_add('
      Alter table "EQP_ASSMBL_BOM_THRESHOLD" add Constraint "FK_REFTHMODE_EQPASSMBTH" foreign key ("THRESHOLD_MODE_DB_ID","THRESHOLD_MODE_CD") references "REF_THRESHOLD_MODE" ("THRESHOLD_MODE_DB_ID","THRESHOLD_MODE_CD") DEFERRABLE
    ');
END;
/

--changeSet MX-17691:11 stripComments:false
-- Set default value to 0:FLEET
UPDATE "EQP_ASSMBL_BOM_THRESHOLD" SET
   THRESHOLD_MODE_DB_ID = 0,
   THRESHOLD_MODE_CD = 'FLEET'
WHERE THRESHOLD_MODE_DB_ID IS NULL;

--changeSet MX-17691:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Make the field mandatory
BEGIN
    utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE "EQP_ASSMBL_BOM_THRESHOLD"
      MODIFY (
         "THRESHOLD_MODE_DB_ID" Number(10,0) NOT NULL DEFERRABLE Check (THRESHOLD_MODE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
      )
    ');
END;
/

--changeSet MX-17691:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Make the field mandatory
BEGIN
    utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE "EQP_ASSMBL_BOM_THRESHOLD"
      MODIFY (
         "THRESHOLD_MODE_CD" Varchar2 (8) NOT NULL DEFERRABLE
      )
    ');
END;
/

--changeSet MX-17691:14 stripComments:false
/*******************************************************************************
 * Add FAULT_THRESHOLD_EXCEEDED_ON_ASSEMBLY alert
 ******************************************************************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 220, 'core.alert.FAULT_THRESHOLD_EXCEEDED_ON_ASSEMBLY_name', 'core.alert.FAULT_THRESHOLD_EXCEEDED_ON_ASSEMBLY_description', 'ROLE', null, 'FAULT', 'core.alert.FAULT_THRESHOLD_EXCEEDED_ON_ASSEMBLY_message', 1, 0, null, 1, 0
   FROM DUAL
   WHERE
      NOT EXISTS(
         SELECT 1 FROM utl_alert_type
         WHERE alert_type_id = 220
      );