--liquibase formatted sql


--changeSet MTX-1224:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Add Create Logbook Fault API config parm
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_UPDATE_OPEN_LOGBOOK_FAULT_REQUEST', 
	  'Permission to update open logbook faults',
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