--liquibase formatted sql
--changeSet OPER-24797:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$

BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_SUPERVISOR_UNASSIGN', 
      'Unassign a technician of a crew member that has started (IN WORK) a labour row.',
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Maint - Tasks',
      '8.3-SP1',
      0,
      0
      );
END;
/