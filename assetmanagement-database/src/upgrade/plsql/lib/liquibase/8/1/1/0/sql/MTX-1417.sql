--liquibase formatted sql


--changeSet MTX-1417:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- ARC Model Schema Changes --		
--
-- Add new column to ARC_FAULT
--
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ARC_FAULT add ( FAIL_TYPE_CD  VARCHAR2(8) )
');
END;
/

--changeSet MTX-1417:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table ARC_FAULT add ( EX_FAIL_SEV_CD   VARCHAR2(8) )
');
END;
/