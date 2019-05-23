--liquibase formatted sql

--changeSet OPER-30619:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Insert new action parameter
BEGIN
      utl_migr_data_pkg.action_parm_insert
      (
         'API_TASK_STEP_PARM',
         'Permission to access the Task Step API.',
         'TRUE/FALSE',
         'FALSE',
         1,
         'API - TASK',
         '8.3-SP2',
         0,
         0
      );
END;
/