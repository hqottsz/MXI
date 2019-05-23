--liquibase formatted sql


--changeSet ARC-DEV-2469:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table ARC_RESULT add (
         "ENTITY_TYPE" Varchar2 (20)
      )
   ');
END;
/

--changeSet ARC-DEV-2469:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table ARC_RESULT add (
         "ENTITY_KEY" Raw(16)
      )
   ');
END;
/