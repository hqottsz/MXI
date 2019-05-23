--liquibase formatted sql


--changeSet DEV-2272:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_AIRCRAFT_REQUEST',
      'Permission to allow API aircraft retrieval calls',
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

--changeSet DEV-2272:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'API_AIRCRAFT_REQUEST',
      'ACTION_GETAIRCRAFT'
   );
END;
/

--changeSet DEV-2272:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_delete(
      'ACTION_GETAIRCRAFT'
   );
END;
/