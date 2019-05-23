--liquibase formatted sql


--changeSet MX-13757:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 utl_migr_schema_pkg.table_column_modify('
 Alter table DWT_TASK_LABOUR_SUMMARY modify (
    "ASSMBL_INV_NO_SDESC" Varchar2 (240) NOT NULL DEFERRABLE
 )
 ');
 END;
 /