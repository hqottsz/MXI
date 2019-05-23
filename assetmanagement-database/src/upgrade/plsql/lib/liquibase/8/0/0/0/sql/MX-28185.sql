--liquibase formatted sql


--changeSet MX-28185:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table UTL_WORK_ITEM_TYPE add (
   SCHEDULED_BUFFER Number(4,0)
)
');
END;
/ 

--changeSet MX-28185:2 stripComments:false
UPDATE utl_work_item_type SET scheduled_buffer = 500 WHERE scheduled_buffer IS NULL; 

--changeSet MX-28185:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table UTL_WORK_ITEM_TYPE modify (
   SCHEDULED_BUFFER Number(4,0) NOT NULL DEFERRABLE
)
');
END;
/