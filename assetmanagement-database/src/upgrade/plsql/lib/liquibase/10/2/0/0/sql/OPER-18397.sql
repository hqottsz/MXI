--liquibase formatted sql

--changeSet OPER-18397:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'ALLOW_BATCH_COMPLETE_BEFORE_WORK_PACKAGE_ACTUAL_START_DT',
 'LOGIC',
 'Determines the severity of the validation for the user when attempting to batch complete tasks with a complete date earlier than the work package actual start date.',
 'GLOBAL',
 'ERROR/WARN',
 'ERROR',
 1,
 'Maint - Tasks',
 '8.2-SP3u11',
 0
);
 
END;
/