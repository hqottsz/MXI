--liquibase formatted sql


--changeSet MTX-246:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for detach and archive tracked asset API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_DETACH_TRACKED_REQUEST', 
	  'Permission to allow detach tracked asset calls',
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

--changeSet MTX-246:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_ARCHIVE_TRACKED_REQUEST', 
	  'Permission to allow archive tracked asset calls',
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