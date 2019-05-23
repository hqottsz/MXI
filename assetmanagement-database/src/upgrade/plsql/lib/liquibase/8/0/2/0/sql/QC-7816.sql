--liquibase formatted sql


--changeSet QC-7816:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_LOCALE_REQUEST',
      'Permission to request locale via the API.',
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