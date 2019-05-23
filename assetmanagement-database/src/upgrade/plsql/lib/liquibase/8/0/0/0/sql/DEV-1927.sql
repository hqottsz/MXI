--liquibase formatted sql


--changeSet DEV-1927:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
* Create new configuration parameter ENABLE_ADVANCE_SEARCH
**************************************************************************/
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ENABLE_ADVANCED_SEARCH',
      'MXWEB',
      'This parameter is used to enable the barcode field to use advanced search functionality.',
      'GLOBAL',
      'TRUE/FALSE',
      'FALSE',
      1,
      'MXWEB',
      '8.0 SP1',
      0
   );
END;
/