--liquibase formatted sql


--changeSet MX-24291:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Create new configuration parameter LABOR_IN_WORK_IN_COMPLETE_TASK
 **************************************************************************/
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert( 
      'LABOR_IN_WORK_IN_COMPLETE_TASK',
      'LOGIC', 
      'The labor is in work on a completed task.', 
      'USER', 
      'ERROR', 
      'ERROR', 
      1, 
      'Maint - Tasks', 
      '7.2-SP1', 
      0);
END;
/