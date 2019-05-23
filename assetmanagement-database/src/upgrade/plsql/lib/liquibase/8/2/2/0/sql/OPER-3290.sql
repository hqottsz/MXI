--liquibase formatted sql


--changeSet OPER-3290:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Change the Size of CERT_CD in Table ORG_VENDOR from 8 characters to 25 characters 
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      Alter table ORG_VENDOR modify (
         CERT_CD  VARCHAR2 (25)
      )
   ');
END;
/