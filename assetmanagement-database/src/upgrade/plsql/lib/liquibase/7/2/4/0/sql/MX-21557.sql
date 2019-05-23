--liquibase formatted sql


--changeSet MX-21557:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add task_defn columns
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table eqp_part_compat_task add (
  "TASK_DEFN_DB_ID" Number(10,0)
)
');
END;
/

--changeSet MX-21557:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table eqp_part_compat_task add (
	"TASK_DEFN_ID" Number(10,0)
)
');
END;
/

--changeSet MX-21557:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Commented out due to changes that have already been applied in 7.2.0.0-SP1
-- --  Update table to point to task_defn
-- BEGIN
--  MERGE INTO eqp_part_compat_task
--  USING task_task ON
--     (task_task.task_db_id = eqp_part_compat_task.task_db_id AND
--     task_task.task_id = eqp_part_compat_task.task_id)
--  WHEN MATCHED THEN
--     UPDATE SET
--        eqp_part_compat_task.task_defn_db_id = task_task.task_defn_db_id,
--        eqp_part_compat_task.task_defn_id = task_task.task_defn_id
--  
-- END
-- 
-- Add task_defn column constraints
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table eqp_part_compat_task modify (
  "TASK_DEFN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DEFN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet MX-21557:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table eqp_part_compat_task modify (
	"TASK_DEFN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DEFN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet MX-21557:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add foreign key constraint
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EQP_PART_COMPAT_TASK" add Constraint "FK_TASKDEFN_EQPRTCMPATTSK" foreign key ("TASK_DEFN_DB_ID","TASK_DEFN_ID") references "TASK_DEFN" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")  DEFERRABLE
');
END;
/

--changeSet MX-21557:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "EQPPARTCOMPATTSK_TASKDEFN" ON "EQP_PART_COMPAT_TASK" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")
');
END;
/

--changeSet MX-21557:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop foreign key constraint for task_task
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('EQP_PART_COMPAT_TASK', 'FK_TASKTASK_EQPRTCMPTASK');
END;
/

--changeSet MX-21557:8 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('EQPPARTCOMPATTSK_TASKTASK');
END;
/

--changeSet MX-21557:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop primary key constraint
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('EQP_PART_COMPAT_TASK', 'PK_EQP_PART_COMPAT_TASK');
END;
/

--changeSet MX-21557:10 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_EQP_PART_COMPAT_TASK');
END;
/

--changeSet MX-21557:11 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop task_task columns
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('EQP_PART_COMPAT_TASK', 'TASK_DB_ID');
END;
/

--changeSet MX-21557:12 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('EQP_PART_COMPAT_TASK', 'TASK_ID');
END;
/

--changeSet MX-21557:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add new primary key constraint
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table eqp_part_compat_task add constraint "pk_EQP_PART_COMPAT_TASK" Primary key (bom_part_db_id, bom_part_id, part_no_db_id, part_no_id, task_defn_db_id, task_defn_id)
');
END;
/