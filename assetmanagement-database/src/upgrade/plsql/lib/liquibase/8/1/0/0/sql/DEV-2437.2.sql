--liquibase formatted sql


--changeSet DEV-2437.2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_MAINT_ENG_PROGRAM_REQUEST',
      'Permission to allow API retrieval calls for aspects of the Maintenance Engineering Program',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - MAINTENANCE',
      '8.1-SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet DEV-2437.2:2 stripComments:false
-- adding surrogate key to TASK_DEFN
ALTER TRIGGER TUBR_TASK_DEFN DISABLE;

--changeSet DEV-2437.2:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table TASK_DEFN add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2437.2:4 stripComments:false
UPDATE 
   TASK_DEFN 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet DEV-2437.2:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table TASK_DEFN modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet DEV-2437.2:6 stripComments:false
ALTER TRIGGER TUBR_TASK_DEFN ENABLE;

--changeSet DEV-2437.2:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_TASK_DEFN_ALT_ID" BEFORE INSERT
   ON "TASK_DEFN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/