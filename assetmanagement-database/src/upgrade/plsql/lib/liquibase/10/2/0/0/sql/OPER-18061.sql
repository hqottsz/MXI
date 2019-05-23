--liquibase formatted sql

--changeSet OPER-18061:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add APP_ID column
BEGIN
utl_migr_schema_pkg.table_column_add('
	Alter table PUBSUB_SUBSCRIBER add (
	APP_ID VARCHAR2(80)
	)
');
END;
/