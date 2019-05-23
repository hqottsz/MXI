--liquibase formatted sql

--changeSet OPER-31259:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'CHECK_SUP_LOC_STOCK_LEVELS_ON_IMPORT',
      'LOGIC',
      'Permission to automatically trigger the POREQ and SHIPREQ stock low actions on imported supply location stock levels.',
      'GLOBAL',
      'BOOLEAN',
      'FALSE',
      1,
      'Org - Locations',
      '8.3-SP2',
      0
   );

END;
/

--changeSet OPER-31259:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'CHECK_WAREHOUSE_LOC_STOCK_LEVELS_ON_IMPORT',
      'LOGIC',
      'Permission to automatically trigger the DISTREQ stock low action on imported warehouse location stock levels.',
      'GLOBAL',
      'BOOLEAN',
      'FALSE',
      1,
      'Org - Locations',
      '8.3-SP2',
      0
   );

END;
/
