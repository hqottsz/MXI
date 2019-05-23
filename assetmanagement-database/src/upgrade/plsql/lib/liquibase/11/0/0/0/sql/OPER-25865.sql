--liquibase formatted sql

--changeSet OPER-25865:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN
 
   utl_migr_data_pkg.config_parm_insert(
      'DEFER_ON_APPROVAL',
      'LOGIC',
      'Controls whether faults are automatically deferred after deferral reference requests are approved. If true, faults are deferred automatically after the deferral reference request is approved. If false, after the deferral reference request is approved, technicians must perform a Job Stop before faults are deferred.',
      'GLOBAL',
      'TRUE/FALSE',
      'TRUE',
      1,
      'Core Logic',
      '8.3-SP1 LAR',
      0
   );

END;
/