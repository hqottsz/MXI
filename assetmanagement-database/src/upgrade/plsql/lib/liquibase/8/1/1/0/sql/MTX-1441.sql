--liquibase formatted sql


--changeSet MTX-1441:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- ARC Model Schema Changes --		
--
-- Remove two columns:  TSO_QT and TSI_QT
--
BEGIN
   utl_migr_schema_pkg.table_column_drop('ARC_FAULT_USAGE', 'TSO_QT');
END;
/

--changeSet MTX-1441:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_drop('ARC_FAULT_USAGE', 'TSI_QT');
END;
/