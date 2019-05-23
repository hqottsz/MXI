--liquibase formatted sql

--changeSet OPER-27366:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new action config parm
BEGIN
 
   utl_migr_data_pkg.action_parm_insert(
      'API_WORK_CAPTURE_PARM',
      'Permission to access the Work Capture API.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - MAINTENANCE',
      '8.3-SP2',
      0,
      0
   );

END;
/