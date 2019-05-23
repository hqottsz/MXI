--liquibase formatted sql


--changeSet DEV-2437:1 stripComments:false
ALTER TRIGGER TUBR_EQP_ASSMBL_POS DISABLE;

--changeSet DEV-2437:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_ASSMBL_POS add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2437:3 stripComments:false
UPDATE 
   EQP_ASSMBL_POS
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-2437:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_ASSMBL_POS modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-2437:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_ASSMBL_POS_ALT_ID" BEFORE INSERT
   ON "EQP_ASSMBL_POS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-2437:6 stripComments:false
ALTER TRIGGER TUBR_EQP_ASSMBL_POS ENABLE;