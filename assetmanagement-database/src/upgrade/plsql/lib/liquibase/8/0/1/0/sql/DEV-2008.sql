--liquibase formatted sql


--changeSet DEV-2008:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************************
* Create new configuration parameters ACTION_CLOSE_ACCOUNT and ACTION_REOPEN_ACCOUNT
************************************************************************************/
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_insert(
      'ACTION_CLOSE_ACCOUNT', 
      'Permission to close an existing financial account.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Org - Financials',
      '8.0-SP2',
      0,
      0, 
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   );
END;
/

--changeSet DEV-2008:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_insert(
      'ACTION_REOPEN_ACCOUNT', 
      'Permission to reopen a closed financial account.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Org - Financials',
      '8.0-SP2',
      0,
      0, 
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   );
END;
/