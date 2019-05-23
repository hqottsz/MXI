--liquibase formatted sql


--changeSet MTX-922:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
* Create new API_ASSET_CONDITION_REQUEST action configuration parameter 
***********************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
          'API_ASSET_CONDITION_REQUEST', 
	  'Permission to allow API retrieval call for asset condition',
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