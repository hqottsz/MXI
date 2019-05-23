--liquibase formatted sql


--changeSet DEV-365:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table UTL_ALERT add (
	"PARM_HASH" Varchar2 (32)
)
');
END;
/