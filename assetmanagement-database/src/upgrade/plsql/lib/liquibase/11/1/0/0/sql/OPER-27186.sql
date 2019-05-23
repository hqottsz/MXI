--liquibase formatted sql

--changeset OPER-27186:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE EQP_TASK_PANEL MODIFY PANEL_CD VARCHAR2 (16) NOT NULL
   ');
END;
/