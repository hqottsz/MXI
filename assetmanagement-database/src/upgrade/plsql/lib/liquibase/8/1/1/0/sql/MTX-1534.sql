--liquibase formatted sql


--changeSet MTX-1534:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************************************
* Rename config parm API_UPDATE_LOGBOOK_FAULT_REQUEST to API_UPDATE_OPEN_LOGBOOK_FAULT_REQUEST
***********************************************************************************************/
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

--changeSet MTX-1534:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      	'API_UPDATE_OPEN_LOGBOOK_FAULT_REQUEST', 
	'API_UPDATE_LOGBOOK_FAULT_REQUEST');
END;
/

--changeSet MTX-1534:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_delete(      	
	'API_UPDATE_LOGBOOK_FAULT_REQUEST');
END;
/