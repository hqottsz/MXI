--liquibase formatted sql
 

--changeSet OPER-2096_2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- 
-- Change column to mandatory
-- -- 
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table org_labour_skill_map modify (
   "SKILL_ORDER" Number(4,0) NOT NULL DEFERRABLE
)
');
END;
/