--liquibase formatted sql


--changeSet MTX-561:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for attach SER asset API
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_ATTACH_SER_REQUEST', 
	  'Permission to allow create and attach SER asset calls',
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