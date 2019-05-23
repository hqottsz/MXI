--liquibase formatted sql


--changeSet MX-17388:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table INV_INV modify (
	"FINANCE_STATUS_CD" Varchar2 (8) Default ''INSP'' NOT NULL DEFERRABLE  Check (FINANCE_STATUS_CD IN (''NEW'', ''INSP'') ) DEFERRABLE 
)
');
END;
/