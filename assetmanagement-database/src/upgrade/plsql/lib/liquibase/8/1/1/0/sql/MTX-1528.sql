--liquibase formatted sql


--changeSet MTX-1528:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- ARC Model Schema Changes --		
--
-- Change ARC_FAULT.DEFER_CD to ARC_FAULT.FAIL_DEFER_CD 
--
BEGIN
   utl_migr_schema_pkg.table_column_rename('ARC_FAULT', 'DEFER_CD', 'FAIL_DEFER_CD');
END;
/