--liquibase formatted sql

--changeset OPER-19501:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add column TASK_TASK.USE_SCHED_FROM_BOOL
BEGIN

   upg_migr_schema_v1_pkg.table_column_add('
      ALTER TABLE TASK_TASK ADD USE_SCHED_FROM_BOOL NUMBER(1)
   ');

   application_object_pkg.gv_skip_rstat_upd_errors := TRUE;

END;
/

--changeset OPER-19501:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 utl_migr_schema_pkg.table_constraint_add ('
    ALTER TABLE TASK_TASK ADD CHECK ( USE_SCHED_FROM_BOOL IN (0, 1))
 ');

 application_object_pkg.gv_skip_rstat_upd_errors := FALSE;

END;
/


