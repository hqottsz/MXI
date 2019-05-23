--liquibase formatted sql

--changeset OPER-19518-1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add column PREVENT_MANUAL_INIT_BOOL to TASK_DEFN

BEGIN

   upg_migr_schema_v1_pkg.table_column_add('
      ALTER TABLE TASK_DEFN ADD PREVENT_MANUAL_INIT_BOOL NUMBER(1)
   ');

   application_object_pkg.gv_skip_rstat_upd_errors := TRUE;
   
END;
/

--changeset OPER-19518-1:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add check constraint

BEGIN
 utl_migr_schema_pkg.table_constraint_add ('
    ALTER TABLE TASK_DEFN ADD CHECK ( PREVENT_MANUAL_INIT_BOOL IN (0,1)) 
 ');
 
 application_object_pkg.gv_skip_rstat_upd_errors := FALSE;
 
END;
/