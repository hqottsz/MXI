--liquibase formatted sql


--changeSet OPER-216:1 stripComments:false
-- Adding ALT_ID UUID to RFQ_HEADER
/*************************************************************************
* Addition of UUIDs for entities required by Operator API view for report
**************************************************************************/
ALTER TRIGGER TUBR_RFQ_HEADER DISABLE;

--changeSet OPER-216:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table RFQ_HEADER add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet OPER-216:3 stripComments:false
UPDATE 
   RFQ_HEADER 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet OPER-216:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table RFQ_HEADER modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet OPER-216:5 stripComments:false
ALTER TRIGGER TUBR_RFQ_HEADER ENABLE;

--changeSet OPER-216:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_RFQ_HEADER_ALT_ID" BEFORE INSERT
   ON "RFQ_HEADER" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet OPER-216:7 stripComments:false
-- Adding ALT_ID UUID to RFQ_LINE
ALTER TRIGGER TUBR_RFQ_LINE DISABLE;

--changeSet OPER-216:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table RFQ_LINE add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet OPER-216:9 stripComments:false
UPDATE 
   RFQ_LINE 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet OPER-216:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table RFQ_LINE modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet OPER-216:11 stripComments:false
ALTER TRIGGER TUBR_RFQ_LINE ENABLE;

--changeSet OPER-216:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_RFQ_LINE_ALT_ID" BEFORE INSERT
   ON "RFQ_LINE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/