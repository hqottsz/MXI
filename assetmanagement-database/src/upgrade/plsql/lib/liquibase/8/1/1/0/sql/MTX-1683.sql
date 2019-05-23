--liquibase formatted sql


--changeSet MTX-1683:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Add Raise Predefined Alert API config parm
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_RAISE_ALERT_REQUEST', 
	  'Permission to raise predefined alerts',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - SYSTEM', 
	  '8.1-SP2', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/