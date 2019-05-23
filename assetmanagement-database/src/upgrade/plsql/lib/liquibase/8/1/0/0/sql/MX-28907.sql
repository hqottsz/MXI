--liquibase formatted sql


--changeSet MX-28907:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- MX-28907 migration script
--
-- Add a unique constraint on the task defn key and revision order values to ensure that 
-- a new task definition version can not have the same revision order number as another 
-- version of that same task definition.
--
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('TASK_TASK', 'UK_TASKDEFN_REVISIONORD');
END;
/

--changeSet MX-28907:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('UK_TASKDEFN_REVISIONORD');
END;
/

--changeSet MX-28907:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE task_task ADD CONSTRAINT uk_taskdefn_revisionord UNIQUE (task_defn_db_id, task_defn_id, revision_ord)
   ');
END;
/