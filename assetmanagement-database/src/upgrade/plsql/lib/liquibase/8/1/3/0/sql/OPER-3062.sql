--liquibase formatted sql


--changeSet OPER-3062:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_REPAIR_ORDER_REQUEST', 
      'Permission to allow API repair order request',
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

--changeSet OPER-3062:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_UPDATE_REPAIR_ORDER_REQUEST', 
      'Permission to allow API to update repair order request',
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