--liquibase formatted sql


--changeSet MTX-428:1 stripComments:false
-- Adding ALT_ID UUID to EQP_ASSMBL_BOM
/***************************************************************************
* Add all missing ALT_ID to driving tables for Maintenix business entities
****************************************************************************/
ALTER TRIGGER TUBR_EQP_ASSMBL_BOM DISABLE;

--changeSet MTX-428:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_ASSMBL_BOM add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:3 stripComments:false
UPDATE 
   EQP_ASSMBL_BOM 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_ASSMBL_BOM modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:5 stripComments:false
ALTER TRIGGER TUBR_EQP_ASSMBL_BOM ENABLE;

--changeSet MTX-428:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_ASSMBL_BOM_ALT_ID" BEFORE INSERT
   ON "EQP_ASSMBL_BOM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:7 stripComments:false
-- Adding ALT_ID UUID to MAINT_PRGM
ALTER TRIGGER TUBR_MAINT_PRGM DISABLE;

--changeSet MTX-428:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table MAINT_PRGM add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:9 stripComments:false
UPDATE 
   MAINT_PRGM 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table MAINT_PRGM modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:11 stripComments:false
ALTER TRIGGER TUBR_MAINT_PRGM ENABLE;

--changeSet MTX-428:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_MAINT_PRGM_ALT_ID" BEFORE INSERT
   ON "MAINT_PRGM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:13 stripComments:false
-- Adding ALT_ID UUID to ORG_ORG
ALTER TRIGGER TUBR_ORG_ORG DISABLE;

--changeSet MTX-428:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_ORG add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:15 stripComments:false
UPDATE 
   ORG_ORG 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table ORG_ORG modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:17 stripComments:false
ALTER TRIGGER TUBR_ORG_ORG ENABLE;

--changeSet MTX-428:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ORG_ORG_ALT_ID" BEFORE INSERT
   ON "ORG_ORG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:19 stripComments:false
-- Adding ALT_ID UUID to EQP_ADVSRY
ALTER TRIGGER TUBR_EQP_ADVSRY DISABLE;

--changeSet MTX-428:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_ADVSRY add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:21 stripComments:false
UPDATE 
   EQP_ADVSRY 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_ADVSRY modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:23 stripComments:false
ALTER TRIGGER TUBR_EQP_ADVSRY ENABLE;

--changeSet MTX-428:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_ADVSRY_ALT_ID" BEFORE INSERT
   ON "EQP_ADVSRY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:25 stripComments:false
-- Adding ALT_ID UUID to EQP_FINDING
ALTER TRIGGER TUBR_EQP_FINDING DISABLE;

--changeSet MTX-428:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_FINDING add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:27 stripComments:false
UPDATE 
   EQP_FINDING 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_FINDING modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:29 stripComments:false
ALTER TRIGGER TUBR_EQP_FINDING ENABLE;

--changeSet MTX-428:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_FINDING_ALT_ID" BEFORE INSERT
   ON "EQP_FINDING" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:31 stripComments:false
-- Adding ALT_ID UUID to EQP_TASK_PANEL
ALTER TRIGGER TUBR_EQP_TASK_PANEL DISABLE;

--changeSet MTX-428:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_TASK_PANEL add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:33 stripComments:false
UPDATE 
   EQP_TASK_PANEL 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_TASK_PANEL modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:35 stripComments:false
ALTER TRIGGER TUBR_EQP_TASK_PANEL ENABLE;

--changeSet MTX-428:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_TASK_PANEL_ALT_ID" BEFORE INSERT
   ON "EQP_TASK_PANEL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:37 stripComments:false
-- Adding ALT_ID UUID to EQP_TASK_ZONE
ALTER TRIGGER TUBR_EQP_TASK_ZONE DISABLE;

--changeSet MTX-428:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_TASK_ZONE add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:39 stripComments:false
UPDATE 
   EQP_TASK_ZONE 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_TASK_ZONE modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:41 stripComments:false
ALTER TRIGGER TUBR_EQP_TASK_ZONE ENABLE;

--changeSet MTX-428:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_TASK_ZONE_ALT_ID" BEFORE INSERT
   ON "EQP_TASK_ZONE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:43 stripComments:false
-- Adding ALT_ID UUID to CAPACITY_PATTERN
ALTER TRIGGER TUBR_CAPACITY_PATTERN DISABLE;

--changeSet MTX-428:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table CAPACITY_PATTERN add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:45 stripComments:false
UPDATE 
   CAPACITY_PATTERN 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table CAPACITY_PATTERN modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:47 stripComments:false
ALTER TRIGGER TUBR_CAPACITY_PATTERN ENABLE;

--changeSet MTX-428:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_CAPACITY_PATTERN_ALT_ID" BEFORE INSERT
   ON "CAPACITY_PATTERN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:49 stripComments:false
-- Adding ALT_ID UUID to FAIL_DEFER_REF
ALTER TRIGGER TUBR_FAIL_DEFER_REF DISABLE;

--changeSet MTX-428:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table FAIL_DEFER_REF add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:51 stripComments:false
UPDATE 
   FAIL_DEFER_REF 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table FAIL_DEFER_REF modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:53 stripComments:false
ALTER TRIGGER TUBR_FAIL_DEFER_REF ENABLE;

--changeSet MTX-428:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FAIL_DEFER_REF_ALT_ID" BEFORE INSERT
   ON "FAIL_DEFER_REF" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:55 stripComments:false
-- Adding ALT_ID UUID to ORG_WORK_DEPT
ALTER TRIGGER TUBR_ORG_WORK_DEPT DISABLE;

--changeSet MTX-428:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ORG_WORK_DEPT add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:57 stripComments:false
UPDATE 
   ORG_WORK_DEPT 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table ORG_WORK_DEPT modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:59 stripComments:false
ALTER TRIGGER TUBR_ORG_WORK_DEPT ENABLE;

--changeSet MTX-428:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ORG_WORK_DEPT_ALT_ID" BEFORE INSERT
   ON "ORG_WORK_DEPT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:61 stripComments:false
-- Adding ALT_ID UUID to FAIL_EFFECT
ALTER TRIGGER TUBR_FAIL_EFFECT DISABLE;

--changeSet MTX-428:62 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table FAIL_EFFECT add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:63 stripComments:false
UPDATE 
   FAIL_EFFECT 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:64 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table FAIL_EFFECT modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:65 stripComments:false
ALTER TRIGGER TUBR_FAIL_EFFECT ENABLE;

--changeSet MTX-428:66 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FAIL_EFFECT_ALT_ID" BEFORE INSERT
   ON "FAIL_EFFECT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:67 stripComments:false
-- Adding ALT_ID UUID to FAIL_MODE
ALTER TRIGGER TUBR_FAIL_MODE DISABLE;

--changeSet MTX-428:68 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table FAIL_MODE add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:69 stripComments:false
UPDATE 
   FAIL_MODE 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:70 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table FAIL_MODE modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:71 stripComments:false
ALTER TRIGGER TUBR_FAIL_MODE ENABLE;

--changeSet MTX-428:72 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FAIL_MODE_ALT_ID" BEFORE INSERT
   ON "FAIL_MODE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:73 stripComments:false
-- Adding ALT_ID UUID to TASK_FAIL_MODE
ALTER TRIGGER TUBR_TASK_FAIL_MODE DISABLE;

--changeSet MTX-428:74 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table TASK_FAIL_MODE add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:75 stripComments:false
UPDATE 
   TASK_FAIL_MODE 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:76 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table TASK_FAIL_MODE modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:77 stripComments:false
ALTER TRIGGER TUBR_TASK_FAIL_MODE ENABLE;

--changeSet MTX-428:78 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_TASK_FAIL_MODE_ALT_ID" BEFORE INSERT
   ON "TASK_FAIL_MODE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:79 stripComments:false
-- Adding ALT_ID UUID to EVT_BAND
ALTER TRIGGER TUBR_EVT_BAND DISABLE;

--changeSet MTX-428:80 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EVT_BAND add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:81 stripComments:false
UPDATE 
   EVT_BAND 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:82 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EVT_BAND modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:83 stripComments:false
ALTER TRIGGER TUBR_EVT_BAND ENABLE;

--changeSet MTX-428:84 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EVT_BAND_ALT_ID" BEFORE INSERT
   ON "EVT_BAND" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:85 stripComments:false
-- Adding ALT_ID UUID to FNC_TCODE
ALTER TRIGGER TUBR_FNC_TCODE DISABLE;

--changeSet MTX-428:86 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table FNC_TCODE add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:87 stripComments:false
UPDATE 
   FNC_TCODE 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:88 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table FNC_TCODE modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:89 stripComments:false
ALTER TRIGGER TUBR_FNC_TCODE ENABLE;

--changeSet MTX-428:90 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FNC_TCODE_ALT_ID" BEFORE INSERT
   ON "FNC_TCODE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:91 stripComments:false
-- Adding ALT_ID UUID to GRP_DEFN
ALTER TRIGGER TUBR_GRP_DEFN DISABLE;

--changeSet MTX-428:92 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table GRP_DEFN add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:93 stripComments:false
UPDATE 
   GRP_DEFN 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:94 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table GRP_DEFN modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:95 stripComments:false
ALTER TRIGGER TUBR_GRP_DEFN ENABLE;

--changeSet MTX-428:96 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_GRP_DEFN_ALT_ID" BEFORE INSERT
   ON "GRP_DEFN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:97 stripComments:false
-- Adding ALT_ID UUID to IETM_IETM
ALTER TRIGGER TUBR_IETM_IETM DISABLE;

--changeSet MTX-428:98 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table IETM_IETM add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:99 stripComments:false
UPDATE 
   IETM_IETM 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:100 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table IETM_IETM modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:101 stripComments:false
ALTER TRIGGER TUBR_IETM_IETM ENABLE;

--changeSet MTX-428:102 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_IETM_IETM_ALT_ID" BEFORE INSERT
   ON "IETM_IETM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:103 stripComments:false
-- Adding ALT_ID UUID to EQP_KIT_PART_GROUPS
ALTER TRIGGER TUBR_EQP_KIT_PART_GROUPS DISABLE;

--changeSet MTX-428:104 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_KIT_PART_GROUPS add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:105 stripComments:false
UPDATE 
   EQP_KIT_PART_GROUPS 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:106 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_KIT_PART_GROUPS modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:107 stripComments:false
ALTER TRIGGER TUBR_EQP_KIT_PART_GROUPS ENABLE;

--changeSet MTX-428:108 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_KIT_PGRPS_ALT_ID" BEFORE INSERT
   ON "EQP_KIT_PART_GROUPS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:109 stripComments:false
-- Adding ALT_ID UUID to INV_OIL_STATUS_LOG
ALTER TRIGGER TUBR_INV_OIL_STATUS_LOG DISABLE;

--changeSet MTX-428:110 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INV_OIL_STATUS_LOG add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:111 stripComments:false
UPDATE 
   INV_OIL_STATUS_LOG 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:112 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table INV_OIL_STATUS_LOG modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:113 stripComments:false
ALTER TRIGGER TUBR_INV_OIL_STATUS_LOG ENABLE;

--changeSet MTX-428:114 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INV_OIL_STATUS_LOG_ALT_ID" BEFORE INSERT
   ON "INV_OIL_STATUS_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:115 stripComments:false
-- Adding ALT_ID UUID to EQP_BOM_PART
ALTER TRIGGER TUBR_EQP_BOM_PART DISABLE;

--changeSet MTX-428:116 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_BOM_PART add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:117 stripComments:false
UPDATE 
   EQP_BOM_PART 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:118 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_BOM_PART modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:119 stripComments:false
ALTER TRIGGER TUBR_EQP_BOM_PART ENABLE;

--changeSet MTX-428:120 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_BOM_PART_ALT_ID" BEFORE INSERT
   ON "EQP_BOM_PART" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:121 stripComments:false
-- Adding ALT_ID UUID to EQP_PLANNING_TYPE
ALTER TRIGGER TUBR_EQP_PLANNING_TYPE DISABLE;

--changeSet MTX-428:122 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_PLANNING_TYPE add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:123 stripComments:false
UPDATE 
   EQP_PLANNING_TYPE 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:124 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_PLANNING_TYPE modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:125 stripComments:false
ALTER TRIGGER TUBR_EQP_PLANNING_TYPE ENABLE;

--changeSet MTX-428:126 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_PLANNING_TYPE_ALT_ID" BEFORE INSERT
   ON "EQP_PLANNING_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:127 stripComments:false
-- Adding ALT_ID UUID to QUAR_QUAR
ALTER TRIGGER TUBR_QUAR_QUAR DISABLE;

--changeSet MTX-428:128 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table QUAR_QUAR add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:129 stripComments:false
UPDATE 
   QUAR_QUAR 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:130 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table QUAR_QUAR modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:131 stripComments:false
ALTER TRIGGER TUBR_QUAR_QUAR ENABLE;

--changeSet MTX-428:132 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_QUAR_QUAR_ALT_ID" BEFORE INSERT
   ON "QUAR_QUAR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:133 stripComments:false
-- Adding ALT_ID UUID to SHIFT_SHIFT
ALTER TRIGGER TUBR_SHIFT_SHIFT DISABLE;

--changeSet MTX-428:134 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SHIFT_SHIFT add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:135 stripComments:false
UPDATE 
   SHIFT_SHIFT 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:136 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SHIFT_SHIFT modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:137 stripComments:false
ALTER TRIGGER TUBR_SHIFT_SHIFT ENABLE;

--changeSet MTX-428:138 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SHIFT_SHIFT_ALT_ID" BEFORE INSERT
   ON "SHIFT_SHIFT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:139 stripComments:false
-- Adding ALT_ID UUID to USER_SHIFT_PATTERN
ALTER TRIGGER TUBR_USER_SHIFT_PATTERN DISABLE;

--changeSet MTX-428:140 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table USER_SHIFT_PATTERN add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:141 stripComments:false
UPDATE 
   USER_SHIFT_PATTERN 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:142 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table USER_SHIFT_PATTERN modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:143 stripComments:false
ALTER TRIGGER TUBR_USER_SHIFT_PATTERN ENABLE;

--changeSet MTX-428:144 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_USER_SHIFT_PATTERN_ALT_ID" BEFORE INSERT
   ON "USER_SHIFT_PATTERN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:145 stripComments:false
-- Adding ALT_ID UUID to TAG_TAG
ALTER TRIGGER TUBR_TAG_TAG DISABLE;

--changeSet MTX-428:146 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table TAG_TAG add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:147 stripComments:false
UPDATE 
   TAG_TAG 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:148 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table TAG_TAG modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:149 stripComments:false
ALTER TRIGGER TUBR_TAG_TAG ENABLE;

--changeSet MTX-428:150 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_TAG_TAG_ALT_ID" BEFORE INSERT
   ON "TAG_TAG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:151 stripComments:false
-- Adding ALT_ID UUID to EVT_EVENT (for tool checkout)
ALTER TRIGGER TUBR_EVT_EVENT DISABLE;

--changeSet MTX-428:152 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EVT_EVENT add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:153 stripComments:false
UPDATE 
   EVT_EVENT 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:154 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EVT_EVENT modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:155 stripComments:false
ALTER TRIGGER TUBR_EVT_EVENT ENABLE;

--changeSet MTX-428:156 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EVT_EVENT_ALT_ID" BEFORE INSERT
   ON "EVT_EVENT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:157 stripComments:false
-- Adding ALT_ID UUID to INV_XFER
ALTER TRIGGER TUBR_INV_XFER DISABLE;

--changeSet MTX-428:158 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INV_XFER add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:159 stripComments:false
UPDATE 
   INV_XFER 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:160 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table INV_XFER modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:161 stripComments:false
ALTER TRIGGER TUBR_INV_XFER ENABLE;

--changeSet MTX-428:162 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INV_XFER_ALT_ID" BEFORE INSERT
   ON "INV_XFER" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:163 stripComments:false
-- Adding ALT_ID UUID to WARRANTY_INIT
ALTER TRIGGER TUBR_WARRANTY_INIT DISABLE;

--changeSet MTX-428:164 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table WARRANTY_INIT add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:165 stripComments:false
UPDATE 
   WARRANTY_INIT 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:166 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table WARRANTY_INIT modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:167 stripComments:false
ALTER TRIGGER TUBR_WARRANTY_INIT ENABLE;

--changeSet MTX-428:168 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_WARRANTY_INIT_ALT_ID" BEFORE INSERT
   ON "WARRANTY_INIT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:169 stripComments:false
-- Adding ALT_ID UUID to CLAIM
ALTER TRIGGER TUBR_CLAIM DISABLE;

--changeSet MTX-428:170 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table CLAIM add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:171 stripComments:false
UPDATE 
   CLAIM 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:172 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table CLAIM modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:173 stripComments:false
ALTER TRIGGER TUBR_CLAIM ENABLE;

--changeSet MTX-428:174 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_CLAIM_ALT_ID" BEFORE INSERT
   ON "CLAIM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:175 stripComments:false
-- Adding ALT_ID UUID to WARRANTY_DEFN
ALTER TRIGGER TUBR_WARRANTY_DEFN DISABLE;

--changeSet MTX-428:176 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table WARRANTY_DEFN add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:177 stripComments:false
UPDATE 
   WARRANTY_DEFN 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:178 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table WARRANTY_DEFN modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:179 stripComments:false
ALTER TRIGGER TUBR_WARRANTY_DEFN ENABLE;

--changeSet MTX-428:180 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_WARRANTY_DEFN_ALT_ID" BEFORE INSERT
   ON "WARRANTY_DEFN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:181 stripComments:false
-- Adding ALT_ID UUID to WF_WF
ALTER TRIGGER TUBR_WF_WF DISABLE;

--changeSet MTX-428:182 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table WF_WF add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:183 stripComments:false
UPDATE 
   WF_WF 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:184 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table WF_WF modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:185 stripComments:false
ALTER TRIGGER TUBR_WF_WF ENABLE;

--changeSet MTX-428:186 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_WF_WF_ALT_ID" BEFORE INSERT
   ON "WF_WF" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:187 stripComments:false
-- Adding ALT_ID UUID to WF_DEFN
ALTER TRIGGER TUBR_WF_DEFN DISABLE;

--changeSet MTX-428:188 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table WF_DEFN add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:189 stripComments:false
UPDATE 
   WF_DEFN 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:190 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table WF_DEFN modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:191 stripComments:false
ALTER TRIGGER TUBR_WF_DEFN ENABLE;

--changeSet MTX-428:192 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_WF_DEFN_ALT_ID" BEFORE INSERT
   ON "WF_DEFN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-428:193 stripComments:false
-- Adding ALT_ID UUID to WF_LEVEL
ALTER TRIGGER TUBR_WF_LEVEL DISABLE;

--changeSet MTX-428:194 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table WF_LEVEL add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-428:195 stripComments:false
UPDATE 
   WF_LEVEL 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-428:196 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table WF_LEVEL modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-428:197 stripComments:false
ALTER TRIGGER TUBR_WF_LEVEL ENABLE;

--changeSet MTX-428:198 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_WF_LEVEL_ALT_ID" BEFORE INSERT
   ON "WF_LEVEL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/