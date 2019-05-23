--liquibase formatted sql


--changeSet MX-11229:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EQP_PART_VENDOR_EXCHG_LOC" add Constraint "FK_PARTVENDORXCHG_PARTVENDORXC" foreign key ("VENDOR_DB_ID","VENDOR_ID","PART_NO_DB_ID","PART_NO_ID") references "EQP_PART_VENDOR_EXCHG" ("VENDOR_DB_ID","VENDOR_ID","PART_NO_DB_ID","PART_NO_ID")  DEFERRABLE
');
END;
/