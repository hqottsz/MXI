--liquibase formatted sql


--changeSet OPER-3352:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add field FLAGS into table PPC_ACTIVITY 
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PPC_ACTIVITY add (
   "FLAGS" Number(10,0)
)
');
END;
/

--changeSet OPER-3352:2 stripComments:false
UPDATE PPC_ACTIVITY SET FLAGS = 0 WHERE FLAGS IS NULL;

--changeSet OPER-3352:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PPC_ACTIVITY modify (
   "FLAGS" Number(10,0) Default 0 NOT NULL DEFERRABLE
)
');
END;
/