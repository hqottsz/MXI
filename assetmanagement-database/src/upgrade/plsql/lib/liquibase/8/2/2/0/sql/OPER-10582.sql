--liquibase formatted sql

--changeSet OPER-10582:1 stripComments:false
-- Adding ALT_ID UUID to INV_IETM
/*************************************************************************
* Addition of UUIDs for entities required by Technical Reference API
**************************************************************************/

ALTER TRIGGER TUBR_INV_IETM DISABLE;

--changeSet OPER-10582:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INV_IETM add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet OPER-10582:3 stripComments:false
UPDATE
   INV_IETM
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet OPER-10582:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table INV_IETM modify (
   ALT_ID Raw(16) NOT NULL
)
');
END;
/

--changeSet OPER-10582:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- constraints
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
        'ALTER TABLE INV_IETM ADD CONSTRAINT IX_INVIETMALTID_UNQ UNIQUE ( ALT_ID )'
   );
  
END;
/      

--changeSet OPER-10582:6 stripComments:false
ALTER TRIGGER TUBR_INV_IETM ENABLE;

--changeSet OPER-10582:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INV_IETM_ALT_ID" BEFORE INSERT
   ON "INV_IETM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/
