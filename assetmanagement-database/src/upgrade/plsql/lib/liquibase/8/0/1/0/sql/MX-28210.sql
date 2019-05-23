--liquibase formatted sql


--changeSet MX-28210:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ISSUE_PART_REQUEST_PRINTER_LOCATION',
      'LOGIC',
      'Controls the location that part request print jobs go. WAREHOUSE will print at the inventory location. WHERE_NEEDED will print at the where-needed location.',
      'GLOBAL',
      'WAREHOUSE/WHERE_NEEDED',
      'WAREHOUSE',
      1,
      'Core Logic',
      '8.0-SP2',
      0
   );
END;
/