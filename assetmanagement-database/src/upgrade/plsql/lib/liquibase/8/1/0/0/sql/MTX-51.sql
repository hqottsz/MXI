--liquibase formatted sql


--changeSet MTX-51:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_ACTVTY_SNAPSHOT ADD(
         NAME VARCHAR2(500)
      )
   ');
END;
/