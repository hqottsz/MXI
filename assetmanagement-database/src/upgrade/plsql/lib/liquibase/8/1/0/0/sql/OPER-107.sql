--liquibase formatted sql


--changeSet OPER-107:1 stripComments:false
-- Adding ALT_ID UUID to PO_HEADER
/*************************************************************************
* Addition of UUIDs for entities required by Operator API view for report
**************************************************************************/
ALTER TRIGGER TUBR_PO_HEADER DISABLE;

--changeSet OPER-107:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_HEADER add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet OPER-107:3 stripComments:false
UPDATE 
   PO_HEADER 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet OPER-107:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_HEADER modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet OPER-107:5 stripComments:false
ALTER TRIGGER TUBR_PO_HEADER ENABLE;

--changeSet OPER-107:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_PO_HEADER_ALT_ID" BEFORE INSERT
   ON "PO_HEADER" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet OPER-107:7 stripComments:false
-- Adding ALT_ID UUID to PO_INVOICE
ALTER TRIGGER TUBR_PO_INVOICE DISABLE;

--changeSet OPER-107:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_INVOICE add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet OPER-107:9 stripComments:false
UPDATE 
   PO_INVOICE 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet OPER-107:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_INVOICE modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet OPER-107:11 stripComments:false
ALTER TRIGGER TUBR_PO_INVOICE ENABLE;

--changeSet OPER-107:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_PO_INVOICE_ALT_ID" BEFORE INSERT
   ON "PO_INVOICE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/