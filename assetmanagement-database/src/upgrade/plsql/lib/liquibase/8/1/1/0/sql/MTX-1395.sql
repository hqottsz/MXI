--liquibase formatted sql


--changeSet MTX-1395:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*******************************************
* Add SEARCH FAULT SOURCE API config parm
********************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_FAULT_SOURCE_REQUEST', 
      'Permission to search fault source',
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