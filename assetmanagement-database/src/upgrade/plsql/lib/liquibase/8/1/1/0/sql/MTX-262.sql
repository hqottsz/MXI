--liquibase formatted sql


--changeSet MTX-262:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameters 
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
          'API_OWNER_REQUEST', 
	  'Permission to allow API retrieval call for owner',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - MATERIALS', 
	  '8.1-SP1', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/