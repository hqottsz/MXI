--liquibase formatted sql

--changeSet OPER-30521:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ALLOW_ADHOC_JOB_STEPS_ON_BASELINE_TASKS',
      'LOGIC',
      'Controls visibility of the Add, Edit, and Remove Job Step buttons on the Task Details page for tasks that are based on a task definition. If this global parameter is true, then in addition to having access to the buttons on the details page of faults and ad hoc tasks, roles that have the ACTION_ADD_TASK_STEP, ACTION_EDIT_TASK_STEP, ACTION_REMOVE_TASK_STEP permissions can also access these buttons on the details page of tasks that are based on task definitions.',
      'GLOBAL',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Core Logic',
      '8.3-SP2',
      0
   );
END;
/