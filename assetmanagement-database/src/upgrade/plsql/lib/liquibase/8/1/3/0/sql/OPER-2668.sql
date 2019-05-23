--liquibase formatted sql


--changeSet OPER-2668:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Add API action config parms
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
    'API_CREATE_BATCH_ASSET_REQUEST', 
	  'Permission to allow create-batch-asset api call',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - ASSET', 
	  '8.1-SP3', 
	  0, 
	  0,
    UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/