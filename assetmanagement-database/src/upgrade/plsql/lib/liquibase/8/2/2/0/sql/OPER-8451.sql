--liquibase formatted sql


--changeSet OPER-8451:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ALLOW_EDIT_TSN_ON_USAGE_ENTRY',
      'MXWEB',
      'Allows users to edit usage TSN/TSO/TSI values on the create and edit flight pages.',
      'GLOBAL',
      'TRUE/FALSE',
      'TRUE',
      1,
      'MXWEB',
      '8.2-SP3',
      0
   );
END;
/