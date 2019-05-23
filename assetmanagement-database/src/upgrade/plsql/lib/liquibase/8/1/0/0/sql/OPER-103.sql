--liquibase formatted sql


--changeSet OPER-103:1 stripComments:false
-- Adding ALT_ID UUID to FNC_XACTION_LOG
/*************************************************************************
* Addition of UUIDs for entities required by Operator API view for report
**************************************************************************/
ALTER TRIGGER TUBR_FNC_XACTION_LOG DISABLE;

--changeSet OPER-103:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table FNC_XACTION_LOG add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet OPER-103:3 stripComments:false
UPDATE 
   FNC_XACTION_LOG 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet OPER-103:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table FNC_XACTION_LOG modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet OPER-103:5 stripComments:false
ALTER TRIGGER TUBR_FNC_XACTION_LOG ENABLE;

--changeSet OPER-103:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FNC_XACTION_LOG_ALT_ID" BEFORE INSERT
   ON "FNC_XACTION_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet OPER-103:7 stripComments:false
-- Adding ALT_ID UUID to PO_INVOICE_LINE
ALTER TRIGGER TUBR_PO_INVOICE_LINE DISABLE;

--changeSet OPER-103:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_INVOICE_LINE add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet OPER-103:9 stripComments:false
UPDATE 
   PO_INVOICE_LINE 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet OPER-103:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_INVOICE_LINE modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet OPER-103:11 stripComments:false
ALTER TRIGGER TUBR_PO_INVOICE_LINE ENABLE;

--changeSet OPER-103:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_PO_INVOICE_LINE_ALT_ID" BEFORE INSERT
   ON "PO_INVOICE_LINE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/