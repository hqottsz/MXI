--liquibase formatted sql


--changeSet OPER-4229:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_ADD_PART_REQUIREMENT_ADVANCED', 
      'Permission to add part requirements to a task in WebMaintenix with advanced part search',
      'TRUE/FALSE',    
      'FALSE',  
      1, 
      'Maint - Tasks', 
      '8.2-SP1', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );
END;
/