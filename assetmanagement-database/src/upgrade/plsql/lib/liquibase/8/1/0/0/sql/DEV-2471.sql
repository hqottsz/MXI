--liquibase formatted sql


--changeSet DEV-2471:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for attach tracked asset API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_ATTACH_TRACKED_REQUEST', 
	  'Permission to allow create and attach tracked asset calls',
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