--liquibase formatted sql


--changeSet DEV-1270:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_ADD('ALTER TABLE EQP_PART_VENDOR_PRICE ADD ("CONTRACT_REF_SDESC" VARCHAR2 (80))');
END;
/