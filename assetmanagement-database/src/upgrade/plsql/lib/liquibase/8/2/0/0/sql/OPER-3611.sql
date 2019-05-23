--liquibase formatted sql


--changeSet OPER-3611:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_UPDATE_PART_REQUEST_REQUEST', 
      'Permission to allow API to update part request',
      'TRUE/FALSE',    
      'FALSE',  
      1, 
      'API - MATERIALS', 
      '8.1-SP3', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );
END;
/