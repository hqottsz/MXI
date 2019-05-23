--liquibase formatted sql


--changeSet MTX-562:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table ARC_INV_MAP add (
         "INV_CLASS_CD" Varchar2 (8)
      )
   ');
END;
/