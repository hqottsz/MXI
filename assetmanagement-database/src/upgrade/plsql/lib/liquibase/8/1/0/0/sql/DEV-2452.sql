--liquibase formatted sql


--changeSet DEV-2452:1 stripComments:false
-- Adding ALT_ID UUID to EQP_ASSMBL
/***************************************************************
* Addition of UUID for Assembly entities required by search 
* Positions by Assembly Id
****************************************************************/
ALTER TRIGGER TUBR_EQP_ASSMBL DISABLE;

--changeSet DEV-2452:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_ASSMBL add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2452:3 stripComments:false
UPDATE 
   EQP_ASSMBL 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet DEV-2452:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_ASSMBL modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet DEV-2452:5 stripComments:false
ALTER TRIGGER TUBR_EQP_ASSMBL ENABLE;

--changeSet DEV-2452:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_ASSMBL_ALT_ID" BEFORE INSERT
   ON "EQP_ASSMBL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/