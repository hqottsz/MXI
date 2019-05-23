--liquibase formatted sql


--changeSet DEV-2022:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- migration script for config parameter
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'sAccountSearchAccountStatus',
      'SESSION',
      'Account search parameters.',
      'USER',
      'String',
      null,
      0,
      'Account Search',
      '8.0-SP2',
      0
   );
END;
/