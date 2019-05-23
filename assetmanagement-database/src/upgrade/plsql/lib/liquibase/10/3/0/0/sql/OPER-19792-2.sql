--liquibase formatted sql
--changeSet OPER-19792-2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment removal of not null check constraint on step_ldesc column of sched_step table
BEGIN
utl_migr_schema_pkg.table_column_cons_nn_drop('sched_step', 'step_ldesc');
END;
/