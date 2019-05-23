--liquibase formatted sql

--changeset OPER-19365:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add column REF_TASK_CLASS.ASSIGNABLE_TO_BLOCK_BOOL
BEGIN

   upg_migr_schema_v1_pkg.table_column_add('
      ALTER TABLE REF_TASK_CLASS ADD ASSIGNABLE_TO_BLOCK_BOOL NUMBER(1)
   ');

   application_object_pkg.gv_skip_rstat_upd_errors := TRUE;
   
END;
/

--changeset OPER-19365:2 stripComments:false
--comment update column REF_TASK_CLASS.ASSIGNABLE_TO_BLOCK_BOOL
UPDATE
   REF_TASK_CLASS
SET
   ASSIGNABLE_TO_BLOCK_BOOL = CASE WHEN task_class_db_id = 0 AND task_class_cd = 'FOLLOW' THEN 0 ELSE 1 END   
WHERE
   ASSIGNABLE_TO_BLOCK_BOOL IS NULL;

--changeset OPER-19365:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment modify column REF_TASK_CLASS.ASSIGNABLE_TO_BLOCK_BOOL
BEGIN

   upg_migr_schema_v1_pkg.table_column_modify('
      Alter table REF_TASK_CLASS modify (
         ASSIGNABLE_TO_BLOCK_BOOL Number(1) DEFAULT 1 NOT NULL
      )
   ');

END;
/

--changeset OPER-19365:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 utl_migr_schema_pkg.table_constraint_add ('
    ALTER TABLE REF_TASK_CLASS ADD CHECK ( ASSIGNABLE_TO_BLOCK_BOOL IN (0, 1)) 
 ');
 
 application_object_pkg.gv_skip_rstat_upd_errors := FALSE;
 
END;
/


