--liquibase formatted sql


-- Adding Inspect Pass Flag For INSPECTION

--changeset OPER-8912:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add new column without default value
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table SCHED_LABOUR_ROLE_STATUS add (PASS_BOOL Number(1, 0))
   ');
END;
/

--changeSet OPER-8912:2 stripComments:false
-- migrate data
update SCHED_LABOUR_ROLE_STATUS set PASS_BOOL = 1 where PASS_BOOL is null;
 
--changeset OPER-8912:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add default value
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      Alter table SCHED_LABOUR_ROLE_STATUS modify (PASS_BOOL Number(1, 0) DEFAULT 1)
   ');
END;
/
