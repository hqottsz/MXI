--liquibase formatted sql


--changeSet MX-26150:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE utl_work_item_type ADD (
	retry_interval Number(10,0)
)
');
END;
/ 

--changeSet MX-26150:2 stripComments:false
UPDATE utl_work_item_type SET retry_interval = 30 WHERE retry_interval IS NULL; 

--changeSet MX-26150:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
ALTER TABLE utl_work_item_type MODIFY (
	retry_interval Number(10,0) DEFAULT 30 NOT NULL DEFERRABLE
)
');
END;
/