--liquibase formatted sql
 

--changeSet MX-16827:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.table_column_add('
 Alter table INV_LOC_STOCK add (
 	"BATCH_SIZE" Float
 )
 ');
 END;
/