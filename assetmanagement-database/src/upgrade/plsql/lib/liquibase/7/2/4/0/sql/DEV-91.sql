--liquibase formatted sql


--changeSet DEV-91:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Not null constraint will be added in DEV-108.sql
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table LRP_EVENT add (
	"TYPE" Varchar2 (40) 
)
');
END;
/