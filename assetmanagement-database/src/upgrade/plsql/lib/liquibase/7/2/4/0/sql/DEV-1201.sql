--liquibase formatted sql


--changeSet DEV-1201:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***********************************************************
 * Add new column PO_INVOICE_LINE.TAX_POINT_DT
 ***********************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_INVOICE_LINE add (
   TAX_POINT_DT Date
)
');
END;
/