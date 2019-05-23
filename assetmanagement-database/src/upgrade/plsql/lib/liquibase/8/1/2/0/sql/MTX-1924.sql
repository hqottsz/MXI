--liquibase formatted sql


--changeSet MTX-1924:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*****************************************
* Increase the length of destination URL
*****************************************/
BEGIN
   utl_migr_schema_pkg.table_column_modify('
   Alter table ASB_ADAPTER_DEST_LOOKUP modify (
   URL Varchar2 (500) NOT NULL DEFERRABLE
   )
   ');
END;
/

--changeSet MTX-1924:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
   Alter table ASB_INBOUND_LOG modify (
   MSG_SOURCE Varchar2 (600) NOT NULL DEFERRABLE
   )
   ');
END;
/

--changeSet MTX-1924:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
   Alter table ASB_OUTBOUND_LOG modify (
   MSG_DEST Varchar2 (600) NOT NULL DEFERRABLE
   )
   ');	
END;
/