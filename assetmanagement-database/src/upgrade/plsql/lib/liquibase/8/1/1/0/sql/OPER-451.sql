--liquibase formatted sql


--changeSet OPER-451:1 stripComments:false
-- Storage Maintenance To Do Tab
INSERT INTO 
   UTL_TODO_TAB 
   (
      TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL,  UTL_ID
   )
   SELECT 10006, 'idTabStorageMaintenance', 'web.todotab.STORAGE_MAINTENANCE', '/web/todolist/StorageMaintenanceTab.jsp', 'web.todotab.STORAGE_MAINTENANCE_TAB', 0, 0
   FROM
      DUAL
   WHERE NOT EXISTS (SELECT 1 FROM UTL_TODO_TAB WHERE TODO_TAB_ID = 10006);

--changeSet OPER-451:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'sTaskDueListDayCount',
      'SESSION',
      'Stores the last value entered in the "Show tasks due within the next" options filter on the Storage Maintenance to-do list.',
      'USER',
      'Number',
      '30',
      0,
      'SESSION',
      '8.1-SP2',
      0
   );
END;
/

--changeSet OPER-451:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'sSTMTPartType',
      'SESSION',
      'Stores the last selected value for the part type options filter on the Storage Maintenance to-do list.',
      'USER',
      'STRING',
      null,
      0,
      'SESSION',
      '8.1-SP2',
      0
   );
END;
/