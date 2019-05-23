--liquibase formatted sql


--changeSet OPER-2381:1 stripComments:false
-- Adding ALT_ID UUID to ORG_HR_SHIFT_PLAN
/*************************************************************************
* Addition of UUIDs for entities required by API
**************************************************************************/
ALTER TRIGGER TUBR_ORG_HR_SHIFT_PLAN DISABLE;

--changeSet OPER-2381:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR_SHIFT_PLAN add (
         ALT_ID raw(16)
)
');
END;
/ 

--changeSet OPER-2381:3 stripComments:false
UPDATE ORG_HR_SHIFT_PLAN SET ALT_ID = mx_key_pkg.new_uuid() WHERE ALT_ID IS NULL; 

--changeSet OPER-2381:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table ORG_HR_SHIFT_PLAN modify (
        ALT_ID raw(16) NOT NULL UNIQUE 
)
');
END;
/

--changeSet OPER-2381:5 stripComments:false
ALTER TRIGGER TUBR_ORG_HR_SHIFT_PLAN ENABLE;

--changeSet OPER-2381:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ORG_HR_SHIFT_PLAN_ALT_ID" BEFORE INSERT
   ON "ORG_HR_SHIFT_PLAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/