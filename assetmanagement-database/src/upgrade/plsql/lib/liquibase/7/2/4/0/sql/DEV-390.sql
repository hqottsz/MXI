--liquibase formatted sql


--changeSet DEV-390:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_PART_NO add (
	"NOTE" Varchar2 (2000)
)
');
END;
/

--changeSet DEV-390:2 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_EDIT_NOTE', 'SECURED_RESOURCE','Permission to edit note to a part.','USER', 'TRUE/FALSE', 'FALSE', 1,'Parts - Part Numbers', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'ACTION_EDIT_NOTE' AND PARM_TYPE = 'SECURED_RESOURCE');