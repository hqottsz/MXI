--liquibase formatted sql


--changeSet MX-19601:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table DIM_OIL_STATUS modify (
   OIL_STATUS_CD Varchar2 (16)
)
');
END;
/