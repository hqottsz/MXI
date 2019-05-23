--liquibase formatted sql


--changeSet MX-28261:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table UTL_WORK_ITEM_TYPE modify (
   SCHEDULED_BUFFER DEFAULT 500
)
');
END;
/