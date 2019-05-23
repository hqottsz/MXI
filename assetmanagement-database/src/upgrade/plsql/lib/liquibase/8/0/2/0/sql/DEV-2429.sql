--liquibase formatted sql


--changeSet DEV-2429:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for Planning Item assign API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_ENGINEERING_UNIT_REQUEST',
      'Permission to allow API retrieval call for engineering units.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - MATERIALS',
      '8.1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/