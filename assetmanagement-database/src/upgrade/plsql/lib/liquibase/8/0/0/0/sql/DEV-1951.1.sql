--liquibase formatted sql


--changeSet DEV-1951.1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
* Create new configuration parameter ACTION_ENABLE_ADVANCED_SEARCH
**************************************************************************/
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_insert(
      'ACTION_ENABLE_ADVANCED_SEARCH', 
      'Permission to access advanced search page.',
      'TRUE/FALSE',
      'false',
      1,
      'Advanced Search',
      '8.0-SP1',
      0,
      0, 
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   );
END;
/ 