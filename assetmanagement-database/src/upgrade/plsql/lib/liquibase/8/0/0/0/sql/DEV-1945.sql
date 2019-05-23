--liquibase formatted sql


--changeSet DEV-1945:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create new configuration parameter WF_MOBILE_LINE_MAINT
-- to enable user access to the Mobile Line Maintenance
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'WF_MOBILE_LINE_MAINT',
      'LOGIC',
      'This parameter is used to enable user access to the Mobile Line Maintenance.',
      'USER',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Workflow Access',
      '8.0-SP1',
      0
   );
END;
/