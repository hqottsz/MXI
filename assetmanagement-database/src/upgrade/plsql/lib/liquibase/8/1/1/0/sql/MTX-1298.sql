--liquibase formatted sql


--changeSet MTX-1298:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'WORK_ITEM_MAX_PROCESSING_TIME_IN_MINUTES',
      'WORK_MANAGER',
      'Maximum time that may be spent processing a work item before the server is deemed off line.',
      'GLOBAL',
      'Non-negative integer.',
      '60',
      1,
      'Work Manager Parameter',
      '8.1-SP2',
      0
   );
END;
/