--liquibase formatted sql


--changeSet OPER-215:1 stripComments:false
-- Adding ALT_ID UUID to INV_LOC_STOCK
/*************************************************************************
* Addition of UUIDs for entities required by views for report
**************************************************************************/
ALTER TRIGGER TUBR_INV_LOC_STOCK DISABLE;

--changeSet OPER-215:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INV_LOC_STOCK add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet OPER-215:3 stripComments:false
UPDATE 
   INV_LOC_STOCK 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet OPER-215:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table INV_LOC_STOCK modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet OPER-215:5 stripComments:false
ALTER TRIGGER TUBR_INV_LOC_STOCK ENABLE;

--changeSet OPER-215:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INV_LOC_STOCK_ALT_ID" BEFORE INSERT
   ON "INV_LOC_STOCK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/