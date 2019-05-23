--liquibase formatted sql


--changeSet MX-26440:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*****************************************************************************
 * Create new action configuration parameter ACTION_EDIT_WORK_CAPTURED_AFTER_RELEASE
 *****************************************************************************/
BEGIN
   UTL_MIGR_DATA_PKG.action_parm_insert( 
      'ACTION_EDIT_WORK_CAPTURED_AFTER_RELEASE',
      'Permission to edit work capture after the work package has been completed.', 
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Maint - Tasks', 
      '8.0', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER'));
END;
/  