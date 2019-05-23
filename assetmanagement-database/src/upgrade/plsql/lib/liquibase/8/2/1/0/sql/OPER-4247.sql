--liquibase formatted sql


--changeSet OPER-4247:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_SELECT_ALTERNATE_PART', 
      'Permission to select alternate parts when adding part requirements to a task.',
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