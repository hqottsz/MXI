--liquibase formatted sql

--changeSet OPER-24801:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'LABOR_ROW_ELAPSED_TIME_THRESHOLD',
      'MXWEB',
      'The default value used to indicate on the Task Supervision tab when the time between the start and current time on a labor row has elapsed. This is set as a configurable threshold in hour decimals.',
      'GLOBAL',
      'Number',
      '8.0',
      1,
      'MXWEB',
      '8.3-SP1',
      0
   );

END;
/