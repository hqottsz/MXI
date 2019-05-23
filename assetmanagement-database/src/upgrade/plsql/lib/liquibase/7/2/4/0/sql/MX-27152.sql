--liquibase formatted sql


--changeSet MX-27152:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert( 
      'WORK_ITEM_IDLE_SLEEP_TIME_MILLISECONDS',
      'WORK_MANAGER',
      'The amount of time in milliseconds that the work item scheduler sleeps if there was nothing to do in the previous iteration.', 
      'GLOBAL', 
      'Non-negative integer.', 
      '10000', 
      1, 
      'Work Manager Parameter', 
      '8.0', 
      0);
END;
/

--changeSet MX-27152:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert( 
      'WORK_ITEM_BATCH_SLEEP_TIME_MILLISECONDS',
      'WORK_MANAGER',
      'The amount of time in milliseconds that the scheduler sleeps between scheduling batches of work items.  This value should be 0 or small in a single server environment. It should be large enough in a multi-server environment to give all servers a fair chance of scheduling work. Starvation will occur in a multi-server environment as this value approaches zero or if inactive sleep time is significantly larger than sleep time between batches', 
      'GLOBAL', 
      'Non-negative integer.', 
      '500', 
      1, 
      'Work Manager Parameter', 
      '8.0', 
      0);
END;
/

--changeSet MX-27152:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.config_parm_insert( 
      'WORK_ITEM_ERROR_SLEEP_TIME_MILLISECONDS',
      'WORK_MANAGER',
      'The amount of time in milliseconds that the scheduler sleeps in case of an infrastructure error such as temporary db outage.', 
      'GLOBAL', 
      'Non-negative integer.', 
      '10000', 
      1, 
      'Work Manager Parameter', 
      '8.0', 
      0);
END;
/