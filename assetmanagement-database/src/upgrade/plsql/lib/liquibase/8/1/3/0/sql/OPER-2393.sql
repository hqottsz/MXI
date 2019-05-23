--liquibase formatted sql


--changeSet OPER-2393:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Add Shift Plan API config parms
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_CREATE_SHIFT_PLAN_REQUEST', 
	  'Permission to allow create shift plan API call',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - ENTERPRISE', 
	  '8.2', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet OPER-2393:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_UPDATE_SHIFT_PLAN_REQUEST', 
	  'Permission to allow update shift plan API call',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - ENTERPRISE', 
	  '8.2', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet OPER-2393:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_DELETE_SHIFT_PLAN_REQUEST', 
	  'Permission to allow delete shift plan API call',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - ENTERPRISE', 
	  '8.2', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet OPER-2393:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_DEPARTMENT_REQUEST', 
	  'Permission to allow API department request',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - ENTERPRISE', 
	  '8.2', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet OPER-2393:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_SHIFT_REQUEST', 
	  'Permission to allow API shift request',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - ENTERPRISE', 
	  '8.2', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet OPER-2393:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_SHIFT_PLAN_REQUEST', 
	  'Permission to allow API shift plan request',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - ENTERPRISE', 
	  '8.2', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/