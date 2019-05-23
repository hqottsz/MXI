--liquibase formatted sql


--changeSet OPER-15951-1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_modify('
      ALTER TABLE REF_SENSITIVE_SYSTEM MODIFY ( ACTIVE_BOOL NUMBER(1) DEFAULT 0 )
   ');
END;
/