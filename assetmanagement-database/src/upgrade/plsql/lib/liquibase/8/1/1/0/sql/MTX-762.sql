--liquibase formatted sql


--changeSet MTX-762:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameters 
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_CREATE_HISTORIC_REQ_REQUEST', 
	  'Permission to create historic requirement tasks',
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

--changeSet MTX-762:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_CREATE_HISTORIC_BLOCK_REQUEST', 
	  'Permission to create historic block tasks',
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

--changeSet MTX-762:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert(
      'CREATE_HISTORIC_TASK_MATCHING_INTERVAL', 
      'LOGIC',
      'The matching interval in hours for comparing historic tasks.',
      'GLOBAL',
      'Hours between 0-24',
      '6',
      1,
      'Core Logic',
      '8.1-SP1',
      0
   );
END;
/