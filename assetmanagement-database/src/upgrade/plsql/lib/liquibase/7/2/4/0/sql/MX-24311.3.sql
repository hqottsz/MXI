--liquibase formatted sql


--changeSet MX-24311.3:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
CREATE INDEX "REPL_ASSMBL_BOM_TASK_TASK" ON "TASK_TASK" ("REPL_ASSMBL_DB_ID","REPL_ASSMBL_CD","REPL_ASSMBL_BOM_ID") 
');
END;
/

--changeSet MX-24311.3:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
CREATE INDEX "INV_INV_ORGCARRIER" ON "INV_INV" ("CARRIER_DB_ID","CARRIER_ID") 
');
END;
/