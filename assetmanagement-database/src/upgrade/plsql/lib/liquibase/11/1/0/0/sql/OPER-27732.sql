--liquibase formatted sql

--changeSet OPER-27732:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'SHOW_PART_REQUIREMENT_REFERENCE',
      'LOGIC',
      'Controls whether the part requirement reference is visible in the UI. If true, the Reference field is visible when you add part requirements to tasks and faults and reference information is displayed on other pages (such as details and work capture pages).',
      'GLOBAL',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Core Logic',
      '8.3-SP1 LAR',
      0
   );

END;
/