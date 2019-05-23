--liquibase formatted sql


--changeSet MXCOM-29.3:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_ROLE_VIEW_REQUEST', 
      'Permission to view roles',
      'TRUE/FALSE', 	  
      'FALSE',  
      1, 
      'API - SYSTEM', 
      '8.1-SP2', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet MXCOM-29.3:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_ROLE_UPDATE_REQUEST', 
      'Permission to update roles',
      'TRUE/FALSE', 	  
      'FALSE',  
      1, 
      'API - SYSTEM', 
      '8.1-SP2', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet MXCOM-29.3:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PERM_SET_VIEW_REQUEST', 
      'Permission to view permission sets',
      'TRUE/FALSE', 	  
      'FALSE',  
      1, 
      'API - SYSTEM', 
      '8.1-SP2', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/