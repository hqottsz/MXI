--liquibase formatted sql


--changeSet MX-20720:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Remove the default from the UNIT_PRICE column of the CLAIM_PART_LINE table
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table CLAIM_PART_LINE modify ("UNIT_PRICE" Number(15,5) Default NULL)
');
END;
/

--changeSet MX-20720:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add a default to the UNIT_PRICE column of the CLAIM_LABOUR_LINE table
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table CLAIM_LABOUR_LINE modify ("UNIT_PRICE" Number(15,5) Default 0.00 NOT NULL DEFERRABLE)
');
END;
/