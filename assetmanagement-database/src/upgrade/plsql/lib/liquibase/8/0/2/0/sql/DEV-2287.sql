--liquibase formatted sql


--changeSet DEV-2287:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- adding external_ref column to arc_message 
BEGIN  
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE ARC_MESSAGE ADD(
         EXTERNAL_REF VARCHAR2(200)
      )
   ');
END;
/