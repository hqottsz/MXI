--liquibase formatted sql

--changeSet OPER-27733:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'PART_REQUIREMENT_REFERENCE_MANDATORY_INV_CLASSES',
      'LOGIC',
      'Controls whether entering an IPC or other technical manual reference is mandatory for specific inventory classes when technicians add part requirements to tasks or faults in the user interface.',
      'GLOBAL',
      'BATCH/SER/KIT/TRK',
      '',
      1,
      'Tasks - Part Requirements',
      '8.3-SP1 LAR',
      0
   );

END;
/