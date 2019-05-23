--liquibase formatted sql
 

--changeSet QC-7750:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_GETINVENTORY',
      'Permission to allow get inventory call',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - ASSET',
      '8.0-SP2',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet QC-7750:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_GETAIRCRAFT',
      'Permission to allow get aircraft call',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - ASSET',
      '8.0-SP2',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet QC-7750:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_ARC',
      'Permission to allow ARC API calls.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - ARC',
      '8.0-SP2',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet QC-7750:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_GET_MENU',
      'Permission to allow to request user menu via API.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - SYSTEM',
      '8.0-SP2',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/