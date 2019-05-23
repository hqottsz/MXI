--liquibase formatted sql


--changeSet DEV-121:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table ER_HEADER modify (
     "EFFECTIVE_FROM_DT" Date NOT NULL DEFERRABLE
  )
  ');
 END;
 /  

--changeSet DEV-121:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
  BEGIN
   utl_migr_schema_pkg.table_column_modify('
   Alter table ER_HEADER modify (
      "ER_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE
   )
   ');
  END;
  / 