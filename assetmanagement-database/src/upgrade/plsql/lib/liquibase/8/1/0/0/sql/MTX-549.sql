--liquibase formatted sql


--changeSet MTX-549:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_UPDATE_ACFT_REQUEST',
      'Permission to allow update aircraft call',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - ASSET',
      '8.1 SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/ 