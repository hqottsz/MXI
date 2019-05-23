--liquibase formatted sql


--changeSet MTX-924:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
* Create new API_FORECAST_MODEL_REQUEST action configuration parameter 
***********************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
          'API_FORECAST_MODEL_REQUEST', 
	  'Permission to allow API retrieval call for forecast model',
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

--changeSet MTX-924:2 stripComments:false
/********************************************************************************
* Sets the value of the forecast model configuration parameter 
* to the name of the forecast model from FC_MODEL table
********************************************************************************/
UPDATE utl_config_parm SET
    parm_value = (SELECT desc_sdesc FROM fc_model WHERE default_bool = 1)
 WHERE
    parm_name = 'ARC_DEFAULT_FORECAST_MODEL';