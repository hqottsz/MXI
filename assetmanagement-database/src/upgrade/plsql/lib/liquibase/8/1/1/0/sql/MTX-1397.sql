--liquibase formatted sql


--changeSet MTX-1397:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*******************************************
* Add SEARCH FAULT SEVERITY API config parm
********************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_FAULT_SEVERITY_REQUEST', 
         'Permission to search fault severity',
	 'TRUE/FALSE', 	  
	 'FALSE',  
	 1, 
	 'API - MATERIALS', 
	 '8.1-SP2', 
         0, 
         0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/