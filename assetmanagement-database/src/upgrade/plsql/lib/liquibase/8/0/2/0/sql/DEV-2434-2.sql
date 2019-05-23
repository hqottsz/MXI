--liquibase formatted sql


--changeSet DEV-2434-2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for WPB_MAX_DUE_LIMIT
****************************************************************/
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'WPB_MAX_DUE_LIMIT',
      'MXWEB',
      'This parameter sets the maximum amount of days into the future a planning item can be due.',
      'USER',
      '14',
      '14',
      1,
      'Maint - Work Package Builder',
      '8.0-SP1',
      0
   );
END;
/