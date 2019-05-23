--liquibase formatted sql


--changeSet MTX-1238:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/****************************************************************************************
* Add SEARCH LAST DONE FOR BLOCK, EXECUTABLE REQUIRMENT AND REQUIRMENT API config parm
*****************************************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_TASK_REQUEST', 
         'Permission to search last done for block, executable requirment and requirment',
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