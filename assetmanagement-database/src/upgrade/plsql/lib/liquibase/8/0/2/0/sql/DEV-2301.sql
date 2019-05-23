--liquibase formatted sql


--changeSet DEV-2301:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new configuration parameter Work Package Builder page
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_WORK_PACKAGE_BUILDER',
      'Permission to open the Work Package Builder page.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Work Packages',
      '8.1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/