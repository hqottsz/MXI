--liquibase formatted sql


--changeSet DEV-1929:1 stripComments:false
-- Migration for adding surrogate keys to maintenix entities
-- The following tables are having the surrogate key added:
-- EQP_PART_NO
-- INV_INV
-- INV_LOC
-- ORG_HR
-- REQ_PART
-- SCHED_STASK
-- SD_FAULT
-- SHIP_SHIPMENT
-- TASK_TASK
-- adding surrogate key to EQP_PART_NO
ALTER TRIGGER TUBR_EQP_PART_NO DISABLE;

--changeSet DEV-1929:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_PART_NO add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-1929:3 stripComments:false
UPDATE 
   EQP_PART_NO
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-1929:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_PART_NO modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-1929:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_PART_NO_ALT_ID" BEFORE INSERT
   ON "EQP_PART_NO" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-1929:6 stripComments:false
ALTER TRIGGER TUBR_EQP_PART_NO ENABLE;

--changeSet DEV-1929:7 stripComments:false
-- adding surrogate key to INV_INV
ALTER TRIGGER TUBR_INV_INV DISABLE;

--changeSet DEV-1929:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INV_INV add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-1929:9 stripComments:false
UPDATE 
   INV_INV
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-1929:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table INV_INV modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-1929:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INV_INV_ALT_ID" BEFORE INSERT
   ON "INV_INV" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-1929:12 stripComments:false
ALTER TRIGGER TUBR_INV_INV ENABLE;

--changeSet DEV-1929:13 stripComments:false
-- adding surrogate key to INV_LOC
ALTER TRIGGER TUBR_INV_LOC DISABLE;

--changeSet DEV-1929:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INV_LOC add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-1929:15 stripComments:false
UPDATE 
   INV_LOC
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-1929:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table INV_LOC modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-1929:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INV_LOC_ALT_ID" BEFORE INSERT
   ON "INV_LOC" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-1929:18 stripComments:false
ALTER TRIGGER TUBR_INV_LOC ENABLE;

--changeSet DEV-1929:19 stripComments:false
-- adding surrogate key to ORG_HR
ALTER TRIGGER TUBR_ORG_HR DISABLE;

--changeSet DEV-1929:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_HR add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-1929:21 stripComments:false
UPDATE 
   ORG_HR
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-1929:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table ORG_HR modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-1929:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ORG_HR_ALT_ID" BEFORE INSERT
   ON "ORG_HR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-1929:24 stripComments:false
ALTER TRIGGER TUBR_ORG_HR ENABLE;

--changeSet DEV-1929:25 stripComments:false
-- adding surrogate key to REQ_PART
ALTER TRIGGER TUBR_REQ_PART DISABLE;

--changeSet DEV-1929:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table REQ_PART add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-1929:27 stripComments:false
UPDATE 
   REQ_PART
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-1929:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REQ_PART modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-1929:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REQ_PART_ALT_ID" BEFORE INSERT
   ON "REQ_PART" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-1929:30 stripComments:false
ALTER TRIGGER TUBR_REQ_PART ENABLE;

--changeSet DEV-1929:31 stripComments:false
-- adding surrogate key to SCHED_STASK
ALTER TRIGGER TUBR_SCHED_STASK DISABLE;

--changeSet DEV-1929:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_STASK add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-1929:33 stripComments:false
UPDATE 
   SCHED_STASK
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-1929:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SCHED_STASK modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-1929:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SCHED_STASK_ALT_ID" BEFORE INSERT
   ON "SCHED_STASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-1929:36 stripComments:false
ALTER TRIGGER TUBR_SCHED_STASK ENABLE;

--changeSet DEV-1929:37 stripComments:false
-- adding surrogate key to SD_FAULT
ALTER TRIGGER TUBR_SD_FAULT DISABLE;

--changeSet DEV-1929:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SD_FAULT add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-1929:39 stripComments:false
UPDATE 
   SD_FAULT
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-1929:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SD_FAULT modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-1929:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SD_FAULT_ALT_ID" BEFORE INSERT
   ON "SD_FAULT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-1929:42 stripComments:false
ALTER TRIGGER TUBR_SD_FAULT ENABLE;

--changeSet DEV-1929:43 stripComments:false
-- adding surrogate key to SHIP_SHIPMENT
ALTER TRIGGER TUBR_SHIP_SHIPMENT DISABLE;

--changeSet DEV-1929:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SHIP_SHIPMENT add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-1929:45 stripComments:false
UPDATE 
   SHIP_SHIPMENT
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-1929:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SHIP_SHIPMENT modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-1929:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SHIP_SHIPMENT_ALT_ID" BEFORE INSERT
   ON "SHIP_SHIPMENT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-1929:48 stripComments:false
ALTER TRIGGER TUBR_SHIP_SHIPMENT ENABLE;

--changeSet DEV-1929:49 stripComments:false
-- adding surrogate key to TASK_TASK
ALTER TRIGGER TUBR_TASK_TASK DISABLE;

--changeSet DEV-1929:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table TASK_TASK add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet DEV-1929:51 stripComments:false
UPDATE 
   TASK_TASK
SET
   alt_id = mx_key_pkg.new_uuid()
WHERE
   alt_id IS NULL
;

--changeSet DEV-1929:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table TASK_TASK modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet DEV-1929:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_TASK_TASK_ALT_ID" BEFORE INSERT
   ON "TASK_TASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet DEV-1929:54 stripComments:false
ALTER TRIGGER TUBR_TASK_TASK ENABLE;