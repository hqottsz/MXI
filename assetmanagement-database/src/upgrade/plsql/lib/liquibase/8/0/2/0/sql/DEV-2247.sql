--liquibase formatted sql


--changeSet DEV-2247:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_MENU_REQUEST',
      'Permission to allow to request user menu via API.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - SYSTEM',
      '8.1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet DEV-2247:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'API_MENU_REQUEST',
      'ACTION_GET_MENU'
   );
END;
/

--changeSet DEV-2247:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_delete(
      'ACTION_GET_MENU'
   );
END;
/