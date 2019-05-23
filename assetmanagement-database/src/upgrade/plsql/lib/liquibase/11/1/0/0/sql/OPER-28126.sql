--changeSet OPER-28126:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment delete config parm
BEGIN
   utl_migr_data_pkg.config_parm_delete(
      'ALLOW_BASELINE_TASK_JOB_STEP_UPDATES' 
   );
END;
/  