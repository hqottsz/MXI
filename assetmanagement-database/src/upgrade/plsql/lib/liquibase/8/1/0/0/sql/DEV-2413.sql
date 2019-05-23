--liquibase formatted sql


--changeSet DEV-2413:1 stripComments:false
-- Adding ALT_ID UUID to EQP_MANUFACT 
/***************************************************************
* Addition of UUIDs for entities required by Search Part Definition API
****************************************************************/
ALTER TRIGGER TUBR_EQP_MANUFACT DISABLE;

--changeSet DEV-2413:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_MANUFACT add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2413:3 stripComments:false
UPDATE 
   EQP_MANUFACT 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet DEV-2413:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_MANUFACT modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet DEV-2413:5 stripComments:false
ALTER TRIGGER TUBR_EQP_MANUFACT ENABLE;

--changeSet DEV-2413:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_MANUFACT_ALT_ID" BEFORE INSERT
   ON "EQP_MANUFACT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-2413:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PART_DEFINITION_REQUEST',
      'Permission to allow API search and update part definition calls',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - PART DEFINITION',
      '8.1-SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/