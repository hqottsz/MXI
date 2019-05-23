--liquibase formatted sql


--changeSet OPER-2832:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Add Shipment API config parms
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_SHIPMENT_REQUEST',
	  'Permission to allow API shipment request',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - MATERIALS', 
	  '8.2', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/