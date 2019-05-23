--liquibase formatted sql


--changeSet MTX-852-6:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Rename the FAIL_MODE.ALT column.  This will only apply to development 
-- environments that were created before the column name was fixed.
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('FAIL_MODE', 'ALT', 'ALT_ID');
END;
/