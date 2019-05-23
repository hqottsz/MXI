--liquibase formatted sql


--changeSet OPER-8448:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert action parameter ALLOW_START_HISTORIC_CHECK into UTL_ACTION_CONFIG_PARM
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_START_HISTORIC_CHECK', 
      'Permission to start a historic check or work order in WebMaintenix',
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Maint - Work Packages',
      '8.2-SP3',
      0,
      0,
      utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
      );
END;
/