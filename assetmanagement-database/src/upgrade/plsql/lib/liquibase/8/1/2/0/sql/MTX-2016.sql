--liquibase formatted sql


--changeSet MTX-2016:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*******************************************
* Add Flight request config parm
********************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      	'API_FLIGHT_REQUEST', 
	'Permission to retrieve flight information',
        'TRUE/FALSE',     
        'FALSE',  
        1, 
        'API - MAINTENANCE', 
        '8.1-SP3', 
        0, 
        0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/