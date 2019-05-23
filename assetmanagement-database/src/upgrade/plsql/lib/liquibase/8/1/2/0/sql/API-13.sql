--liquibase formatted sql


--changeSet API-13:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_VIEW_REQUEST', 
      'Permission to view apis',
      'TRUE/FALSE',    
      'FALSE',  
      1, 
      'API - SYSTEM', 
      '8.1-SP3', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER', 'MASTER')
   );
END;
/