--liquibase formatted sql


--changeSet MX-16271:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_PART_ADVSRY add (
	"SERIAL_NO_RANGE" Varchar2 (1000) 
)
');
END;
/

--changeSet MX-16271:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_PART_ADVSRY add (
	"LOT_NO_RANGE" Varchar2 (1000)
)
');
END;
/