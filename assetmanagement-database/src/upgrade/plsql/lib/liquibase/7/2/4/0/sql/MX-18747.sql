--liquibase formatted sql


--changeSet MX-18747:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table UTL_JOB modify (
    "REPEAT_INTERVAL" Number(10,0)
  )
  ');
 END;
 /