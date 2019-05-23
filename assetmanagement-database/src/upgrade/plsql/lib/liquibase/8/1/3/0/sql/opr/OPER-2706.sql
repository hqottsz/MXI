--liquibase formatted sql


--changeSet OPER-2706:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Add PurchaseOrder API config parms
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PURCHASE_ORDER_REQUEST', 
	  'Permission to allow API purchase order request',
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