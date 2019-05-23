--liquibase formatted sql


--changeSet DEV-2441:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for Fault retrieval API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_FAULT_REQUEST',
      'Permission to allow API retrieval call for faults.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - ASSET',
      '8.1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/