--liquibase formatted sql


--changeSet DEV-2420:1 stripComments:false
-- Adding ALT_ID UUID to INV_OWNER 
/***************************************************************
* Addition of UUIDs for entities required by Create Aircraft API
****************************************************************/
ALTER TRIGGER TUBR_INV_OWNER DISABLE;

--changeSet DEV-2420:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INV_OWNER add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2420:3 stripComments:false
UPDATE 
   INV_OWNER
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-2420:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table INV_OWNER modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-2420:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INV_OWNER_ALT_ID" BEFORE INSERT
   ON "INV_OWNER" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-2420:6 stripComments:false
ALTER TRIGGER TUBR_INV_OWNER ENABLE;

--changeSet DEV-2420:7 stripComments:false
-- Adding ALT_ID UUID to FNC_ACCOUNT
ALTER TRIGGER TUBR_FNC_ACCOUNT DISABLE;

--changeSet DEV-2420:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table FNC_ACCOUNT add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2420:9 stripComments:false
UPDATE 
   FNC_ACCOUNT
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-2420:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table FNC_ACCOUNT modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-2420:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FNC_ACCOUNT_ALT_ID" BEFORE INSERT
   ON "FNC_ACCOUNT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-2420:12 stripComments:false
ALTER TRIGGER TUBR_FNC_ACCOUNT ENABLE;

--changeSet DEV-2420:13 stripComments:false
-- Adding ALT_ID UUID to ORG_CARRIER
ALTER TRIGGER TUBR_ORG_CARRIER DISABLE;

--changeSet DEV-2420:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_CARRIER add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2420:15 stripComments:false
UPDATE 
   ORG_CARRIER
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-2420:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table ORG_CARRIER modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-2420:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ORG_CARRIER_ALT_ID" BEFORE INSERT
   ON "ORG_CARRIER" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-2420:18 stripComments:false
ALTER TRIGGER TUBR_ORG_CARRIER ENABLE;

--changeSet DEV-2420:19 stripComments:false
-- Adding ALT_ID UUID to PO_LINE
ALTER TRIGGER TUBR_PO_LINE DISABLE;

--changeSet DEV-2420:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_LINE add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2420:21 stripComments:false
UPDATE 
   PO_LINE
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-2420:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_LINE modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-2420:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_PO_LINE_ALT_ID" BEFORE INSERT
   ON "PO_LINE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-2420:24 stripComments:false
ALTER TRIGGER TUBR_PO_LINE ENABLE;

--changeSet DEV-2420:25 stripComments:false
-- Adding ALT_ID UUID to FC_MODEL
ALTER TRIGGER TUBR_FC_MODEL DISABLE;

--changeSet DEV-2420:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table FC_MODEL add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2420:27 stripComments:false
UPDATE 
   FC_MODEL
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-2420:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table FC_MODEL modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-2420:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FC_MODEL_ALT_ID" BEFORE INSERT
   ON "FC_MODEL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-2420:30 stripComments:false
ALTER TRIGGER TUBR_FC_MODEL ENABLE;

--changeSet DEV-2420:31 stripComments:false
-- Adding ALT_ID UUID to LIC_DEFN
ALTER TRIGGER TUBR_LIC_DEFN DISABLE;

--changeSet DEV-2420:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LIC_DEFN add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2420:33 stripComments:false
UPDATE 
   LIC_DEFN
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-2420:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LIC_DEFN modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-2420:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_LIC_DEFN_ALT_ID" BEFORE INSERT
   ON "LIC_DEFN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-2420:36 stripComments:false
ALTER TRIGGER TUBR_LIC_DEFN ENABLE;

--changeSet DEV-2420:37 stripComments:false
-- Adding ALT_ID UUID to ORG_AUTHORITY
ALTER TRIGGER TUBR_ORG_AUTHORITY DISABLE;

--changeSet DEV-2420:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_AUTHORITY add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-2420:39 stripComments:false
UPDATE 
   ORG_AUTHORITY
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-2420:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table ORG_AUTHORITY modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-2420:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ORG_AUTHORITY_ALT_ID" BEFORE INSERT
   ON "ORG_AUTHORITY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-2420:42 stripComments:false
ALTER TRIGGER TUBR_ORG_AUTHORITY ENABLE;