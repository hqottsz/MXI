--liquibase formatted sql


--changeSet MTX-294:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_NN_DROP('ARC_CONFIG', 'INVENTORY_MAP_ID');
END;
/