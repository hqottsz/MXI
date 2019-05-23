--liquibase formatted sql


--changeSet DEV-2434:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for Planning Item assign API
****************************************************************/
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'WF_WORK_PACKAGE_BUILDER',
      'LOGIC',
      'This parameter is used to enable user access to the Work Package Builder.',
      'USER',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Work Package Builder',
      '8.0-SP1',
      0
   );
END;
/