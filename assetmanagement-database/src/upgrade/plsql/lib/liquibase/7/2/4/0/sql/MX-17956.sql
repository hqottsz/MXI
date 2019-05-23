--liquibase formatted sql


--changeSet MX-17956:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_PART_NO add (
	"ECCN_CD" Varchar2 (20)
)
');
END;
/