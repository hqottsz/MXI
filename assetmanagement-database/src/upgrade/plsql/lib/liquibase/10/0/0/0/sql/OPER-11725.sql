--liquibase formatted sql

--changeSet OPER-11725:1 stripComments:false
--comment removal of not null check constraint on ENTRY_NOTE column of FL_LEG_NOTE table
BEGIN
utl_migr_schema_pkg.table_column_cons_nn_drop('fl_leg_note', 'entry_note');
END;
/