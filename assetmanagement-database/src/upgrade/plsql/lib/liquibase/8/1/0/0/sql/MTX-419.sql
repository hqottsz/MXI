--liquibase formatted sql


--changeSet MTX-419:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'DATASOURCE_INFO',
      'LOGIC',
      'The datasource information that Maintenix connects to',
      'GLOBAL',
      'The datasource information that Maintenix connects to (Oracle thin URL plus schema)',
      '',
      1,
      'Data Volume Management - BLOB segregation',
      '8.1-SP1',
      0
   );
END;
/