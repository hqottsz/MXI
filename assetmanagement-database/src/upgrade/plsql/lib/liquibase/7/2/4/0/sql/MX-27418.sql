--liquibase formatted sql


--changeSet MX-27418:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add ACTION_EDIT_WORK_CAPTURED_AFTER_TASK_COMPLETE
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_EDIT_WORK_CAPTURED_AFTER_TASK_COMPLETE',
      'Permission to edit work captured after task has completed and before work package has completed.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Tasks',
      '8.0',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet MX-27418:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'ACTION_EDIT_WORK_CAPTURED',
      'ACTION_EDIT_WORK_CAPTURED_AFTER_TASK_COMPLETE'
   );
END;
/

--changeSet MX-27418:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add ACTION_INSPECT_TASK_AFTER_TASK_COMPLETE
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_INSPECT_TASK_AFTER_TASK_COMPLETE',
      'Permission to edit inspection after task has completed and before work package has completed.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Tasks',
      '8.0',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet MX-27418:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'ACTION_INSPECT_TASK',
      'ACTION_INSPECT_TASK_AFTER_TASK_COMPLETE'
   );
END;
/

--changeSet MX-27418:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add ACTION_CERTIFY_TASK_AFTER_TASK_COMPLETE
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_CERTIFY_TASK_AFTER_TASK_COMPLETE', 
      'Permission to edit certification after task has completed and before work package has completed.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Tasks',
      '8.0',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet MX-27418:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'ACTION_CERTIFY_TASK',
      'ACTION_CERTIFY_TASK_AFTER_TASK_COMPLETE'
   );
END;
/