--liquibase formatted sql


--changeSet MTX-853:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Update Create Historic Adhoc API config parm
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_CREATE_HISTORIC_ADHOC_REQUEST', 
	  'Permission to create historic adhoc tasks',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - MAINTENANCE', 
	  '8.1-SP1', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/