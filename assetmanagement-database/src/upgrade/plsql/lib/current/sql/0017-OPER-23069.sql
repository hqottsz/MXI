--liquibase formatted sql

--changeSet OPER-23069:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'ENABLE_CONTENT_TYPE_OPTIONS_HEADER',
      'HTTP',
      'Controls if the X-Content-Type-Options header is added to each HTTP response. By default, the value of the header is set to "nosniff", which will cause browsers to enforce strict content type checks.',
      'GLOBAL',
      'TRUE/FALSE',
      'TRUE',
      1,
      'Security',
      '8.3-SP2',
      0
   );

END;
/

--changeSet OPER-23069:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'ENABLE_XSS_PROTECTION_HEADER',
      'HTTP',
      'Controls if the X-XSS-Protection header is added to each HTTP response. By default, the value of the header is set to "1; mode=block", which will cause browsers to block any pages where the browser detects a potential XSS attack.',
     'GLOBAL',
      'TRUE/FALSE',
      'TRUE',
      1,
      'Security',
      '8.3-SP2',
      0
   );

END;
/

--changeSet OPER-23069:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'ENABLE_CACHE_CONTROL_HEADER',
      'HTTP',
      'Controls if the Cache-Control header is added to each HTTP response. By default, the value of the header is set to "no-store, no-cache", which will prevent browsers from caching content that is live or likely to be stale.',
     'GLOBAL',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Security',
      '8.3-SP2',
      0
   );

END;
/
