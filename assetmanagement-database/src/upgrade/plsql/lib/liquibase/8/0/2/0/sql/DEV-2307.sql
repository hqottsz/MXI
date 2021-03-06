--liquibase formatted sql


--changeSet DEV-2307:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for Planning Item assign API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PLANNING_ITEM_ASSIGN',
      'Permission to allow API Planning Item assign call',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - MAINTENANCE',
      '8.1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/