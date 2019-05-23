--liquibase formatted sql

--changeSet OPER-30522:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'JOB_STEP_DEFINITION_DESCRIPTION_LENGTH_LIMIT',
      'LOGIC',
      'The maximum character length of a job step description.',
      'GLOBAL',
      'Non-negative integer.',
      '4000',
      1,
      'Maint - Tasks',
      '8.3-SP2',
      0
   );
END;
/