--liquibase formatted sql


--changeSet MX-24517:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
CREATE INDEX "MAINT_PRGM_TASK_TASK_FK" ON "MAINT_PRGM_TEMP_TASK" ("MAINT_PRGM_DB_ID","MAINT_PRGM_ID","TASK_DB_ID","TASK_ID") 
');
END;
/