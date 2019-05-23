--liquibase formatted sql

--changeset OPER-28093:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ACFT_CAP_LEVELS MODIFY CUSTOM_LEVEL NUMBER (10)
   ');
END;
/