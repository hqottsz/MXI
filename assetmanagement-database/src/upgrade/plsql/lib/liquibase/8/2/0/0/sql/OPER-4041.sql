--liquibase formatted sql
                   

--changeSet OPER-4041:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--  Add new column to MIM_LOCAL_DB table
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table MIM_LOCAL_DB add (
	"COMPONENT_CD" Varchar2 (8)
)
');
END;
/