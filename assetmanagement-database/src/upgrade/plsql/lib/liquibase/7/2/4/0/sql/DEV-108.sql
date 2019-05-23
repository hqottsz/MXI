--liquibase formatted sql


--changeSet DEV-108:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 utl_migr_schema_pkg.table_column_modify('
 Alter table LRP_EVENT modify (
    "TYPE" Varchar2 (40) NOT NULL DEFERRABLE
 )
 ');
 END;
 / 

--changeSet DEV-108:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table LRP_PLAN_CONFIG modify (
     "READONLY_SEV_CD" Varchar2 (8) NOT NULL DEFERRABLE
  )
  ');
 END;
 / 

--changeSet DEV-108:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table LRP_PLAN_CONFIG modify (
     "READONLY_SEV_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (READONLY_SEV_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
  )
  ');
 END;
 /   