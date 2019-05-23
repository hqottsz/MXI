--liquibase formatted sql


--changeSet MX-15376:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'MAX_DURATION',
      'PLANNING_VIEWER',
      'The maximum duration in days that the planning viewer can be set to.',
      'GLOBAL',
      'NUMBER',
      '30',
      0,
      'Planning Viewer',
      '8.0',
      0
   );
END;
/