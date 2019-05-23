--liquibase formatted sql


--changeSet BAEKSA-49:1 stripComments:false
-- Action Config parm for create history note for repair order
INSERT INTO 
	UTL_ACTION_CONFIG_PARM
	(
		parm_name, parm_value, encrypt_bool, parm_desc, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
	)
  SELECT
  	'API_EXECUTABLE_ITEM_REQUEST', 'FALSE',  0, 'Permission to allow API to search for Executable Item' , 'TRUE/FALSE', 'FALSE', 1, 'API - MATERIALS', '8.2', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM UTL_ACTION_CONFIG_PARM WHERE PARM_NAME = 'API_EXECUTABLE_ITEM_REQUEST' );	

--changeSet BAEKSA-49:2 stripComments:false
-- Addingg ALT_ID UUID to SCHED_PART
ALTER TRIGGER TUBR_SCHED_PART DISABLE;

--changeSet BAEKSA-49:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_PART add (
         ALT_ID raw(16)
)
');
END;
/ 

--changeSet BAEKSA-49:4 stripComments:false
UPDATE SCHED_PART SET ALT_ID = mx_key_pkg.new_uuid() WHERE ALT_ID IS NULL; 

--changeSet BAEKSA-49:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SCHED_PART modify (
        ALT_ID raw(16) NOT NULL UNIQUE
)
');
END;
/

--changeSet BAEKSA-49:6 stripComments:false
ALTER TRIGGER TUBR_SCHED_PART ENABLE;

--changeSet BAEKSA-49:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SCHED_PART_ALT_ID" BEFORE INSERT
   ON "SCHED_PART" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/