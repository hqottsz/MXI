--liquibase formatted sql

--changeSet OPER-24794:1 stripComments:false
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, UTL_ID)
SELECT  11027, 'idTabTaskSupervision', 'web.todotab.TASK_SUPERVISION', '/web/todolist/TaskSupervisionTab.jsp', 'web.todotab.TASK_SUPERVISION_TAB', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_TODO_TAB WHERE TODO_TAB_ID = 11027);
 

--changeSet OPER-24794:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_SUPERVISOR_SEARCH_CREW_TASK_LABOR', 
      'In organizations that use PPC, this page for crew leads shows task labor rows for the selected crew and start/end time. Use page to: review actual and scheduled hours for rows and edit these values for job stopped rows; perform proxy actions such as job stops and unassigning technicians from labor rows.',
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Maint - Tasks',
      '8.3-SP1',
      0,
      0,
      utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
      );
END;
/

--changeSet OPER-24794:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_SUPERVISOR_JOB_STOP_BY_PROXY', 
      'Job stop by proxy action.',
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Maint - Tasks',
      '8.3-SP1',
      0,
      0,
      utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
      );
END;
/

--changeSet OPER-24794:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_INTERNAL_CREWS_ACCESS', 
      'Permission to access the internal rest api for crews.', 
      'TRUE/FALSE', 
      'TRUE', 
      1, 
      'HR - Departments', 
      '8.3-SP1', 
      0,
      0,
      utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
      );
END;
/

--changeSet OPER-24794:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_INTERNAL_DATETIME_ACCESS', 
      'Permission to access the internal rest api for dates and time.', 
      'TRUE/FALSE', 
      'TRUE', 
      1, 
      'System - Configuration', 
      '8.3-SP1', 
      0,
      0,
      utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
      );
END;
/

--changeSet OPER-24794:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_INTERNAL_TASKS_ACCESS', 
      'Permission to access the internal rest api for tasks.', 
      'TRUE/FALSE', 
      'TRUE', 
      1, 
      'Maint - Tasks', 
      '8.3-SP1', 
      0,
      0,
      utl_migr_data_pkg.DbTypeCdList( 'MASTER' )
      );
END;
/
