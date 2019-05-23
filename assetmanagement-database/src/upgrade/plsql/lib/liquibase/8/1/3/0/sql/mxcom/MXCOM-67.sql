--liquibase formatted sql


--changeSet MXCOM-67:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for accessing the work item admin console
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_WORK_ITEM_ADMIN_CONSOLE',
      'Permission to use the work item administration console.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Admin - Work Items',
      '8.2',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('MASTER', 'OPER')
   );
END;
/