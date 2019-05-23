--liquibase formatted sql
 

--changeSet OPER-3355:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add field FILTERS into table PPC_WP 
-- to be able to store filters as BLOB object
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table PPC_WP add ( FILTERS Blob )
   ');
END;
/