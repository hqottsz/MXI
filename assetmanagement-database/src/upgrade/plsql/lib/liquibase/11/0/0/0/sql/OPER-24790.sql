--liquibase formatted sql

--changeSet OPER-24790:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN
 
   utl_migr_data_pkg.config_parm_insert(
      'JOB_STOP_REMAINING_HOURS',
      'LOGIC',
      'Controls the default value of the Remaining Hours field on the Work Capture page when a technician performs a job stop.  Configuration options are: calculate a default value and display it; calculate a value and hide it; or provide a null value as default.',
      'GLOBAL',
      'CALCULATED/CALCULATED_HIDDEN/BLANK_REMAINING_HOURS',
      'CALCULATED',
      1,
      'Maint - Tasks',
      '8.3-SP1',
      0
   );

END;
/