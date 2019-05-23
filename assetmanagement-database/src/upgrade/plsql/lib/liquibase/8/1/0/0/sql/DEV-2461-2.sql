--liquibase formatted sql


--changeSet DEV-2461-2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for correct usage API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_AIRCRAFT_CORRECT_USAGE_REQUEST', 
	  'Permission to allow API asset retrieval calls',
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