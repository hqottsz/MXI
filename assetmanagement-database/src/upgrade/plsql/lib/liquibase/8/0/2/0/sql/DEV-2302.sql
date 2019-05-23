--liquibase formatted sql


--changeSet DEV-2302:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for Aicraft Work Package API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_AIRCRAFT_WORK_PACKAGE_REQUEST',
      'Permission to allow API retrieval call for aircraft work package.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - MAINTENANCE',
      '8.1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet DEV-2302:2 stripComments:false
-- Migration for adding a surrogate key ( alt_id) to maintenix entity ORG_VENDOR
-- adding surrogate key to ORG_VENDOR
ALTER TRIGGER TUBR_ORG_VENDOR DISABLE;

--changeSet DEV-2302:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_VENDOR add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2302:4 stripComments:false
UPDATE 
   ORG_VENDOR
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-2302:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table ORG_VENDOR modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-2302:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ORG_VENDOR_ALT_ID" BEFORE INSERT
   ON "ORG_VENDOR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-2302:7 stripComments:false
ALTER TRIGGER TUBR_ORG_VENDOR ENABLE;