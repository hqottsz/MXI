--liquibase formatted sql


--changeSet DEV-2416:1 stripComments:false
-- Adding ALT_ID UUID to EQP_STOCK_NO
/***************************************************************
* Addition of UUID for Part entities required by update
* Stock No
* Reliability Data Type
****************************************************************/
ALTER TRIGGER TUBR_EQP_STOCK_NO DISABLE;

--changeSet DEV-2416:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_STOCK_NO add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2416:3 stripComments:false
UPDATE 
   EQP_STOCK_NO 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet DEV-2416:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_STOCK_NO modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet DEV-2416:5 stripComments:false
ALTER TRIGGER TUBR_EQP_STOCK_NO ENABLE;

--changeSet DEV-2416:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_STOCK_NO_ALT_ID" BEFORE INSERT
   ON "EQP_STOCK_NO" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-2416:7 stripComments:false
-- Adding ALT_ID UUID to MIM_DATA_TYPE
ALTER TRIGGER TUBR_MIM_DATA_TYPE DISABLE;

--changeSet DEV-2416:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table MIM_DATA_TYPE add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2416:9 stripComments:false
UPDATE 
   MIM_DATA_TYPE 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet DEV-2416:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table MIM_DATA_TYPE modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet DEV-2416:11 stripComments:false
ALTER TRIGGER TUBR_MIM_DATA_TYPE ENABLE;

--changeSet DEV-2416:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_MIM_DATA_TYPE_ALT_ID" BEFORE INSERT
   ON "MIM_DATA_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/