--liquibase formatted sql

--changeset OPER-9650:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- adding new column for Applicability Range to table TASK_STEP
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table TASK_STEP add (APPL_RANGE_LDESC VARCHAR2(4000))
   ');
END;
/

