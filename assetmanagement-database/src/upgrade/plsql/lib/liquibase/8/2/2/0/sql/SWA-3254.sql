--liquibase formatted sql

--changeSet SWA-3254:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add (
        'ALTER TABLE EQP_ASSMBL_SUBTYPE ADD ALT_ID RAW(16)' 
   );
END;
/  

--changeSet SWA-3254:2 stripComments:false
UPDATE 
   EQP_ASSMBL_SUBTYPE 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet SWA-3254:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_modify (
        'ALTER TABLE EQP_ASSMBL_SUBTYPE MODIFY ALT_ID RAW(16) NOT NULL' 
   );
END;
/  

--changeSet SWA-3254:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
     'ALTER TABLE EQP_ASSMBL_SUBTYPE ADD CONSTRAINT IX_EQPASSMBLSUBTYPEALTID_UNQ UNIQUE ( ALT_ID )'
   );
  
END;
/

--changeSet SWA-3254:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_ASSMBL_SUBTYPE_ALT_ID" BEFORE INSERT
   ON "EQP_ASSMBL_SUBTYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/
