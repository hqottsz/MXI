--liquibase formatted sql


--changeSet QC-7921:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create configuration parameter for Task Summary Labour report
****************************************************************/
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'taskdefinition.TaskLaborSummary',
      'REPORT_ENGINE',
      'Report engine name for generating taskdefinition.TaskLaborSummary report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
       0,
      'Report Parameter',
      '8.1',
       0
   );
END;
/

--changeSet QC-7921:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'taskdefinition.TaskLaborSummary',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/taskdefinition/TaskLaborSummary',
       0,
      'Report Parameter',
      '8.1',
       0
   );
END;
/