--liquibase formatted sql


--changeSet MTX-1558:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*******************************************
* Add Defer MINOR Fault API config parm
********************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      	'API_DEFER_MINOR_LOGBOOK_FAULT_REQUEST', 
	'Permission to defer faults with severity type MINOR.',
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