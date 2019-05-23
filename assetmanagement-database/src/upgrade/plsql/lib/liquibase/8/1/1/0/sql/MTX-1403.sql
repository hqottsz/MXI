--liquibase formatted sql


--changeSet MTX-1403:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*******************************************
* Add SEARCH TASK PRIORITY API config parm
********************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PRIORITY_REQUEST', 
      'Permission to search priority',
      'TRUE/FALSE', 	  
      'FALSE',  
      1, 
      'API - MAINTENANCE', 
      '8.1-SP2', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/