--liquibase formatted sql

--changeSet OPER-25604:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'CONTENT_SECURITY_POLICY_MIXED_CONTENT_DIRECTIVE',
      'HTTP',
      'Declares which mixed-content directive to append to the Content-Security-Policy response header when HTTPS is used. A value of UPGRADE will tell the browser to transparently change HTTP resource URLs to HTTPS, while a value of BLOCK will tell the browser to prevent any resources from being fetched over HTTP.',
      'GLOBAL',
      'UPGRADE/BLOCK',
      'UPGRADE',
      1,
      'Security',
      '8.3-SP2',
      0
   );

END;
/

--changeSet OPER-25604:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'ENABLE_STRICT_TRANSPORT_SECURITY_HEADER',
      'HTTP',
      'Controls if the Strict-Transport-Security header is added to each HTTP response. This header tells the browser that it should only access Maintenix using HTTPS, instead of using HTTP.',
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

--changeSet OPER-25604:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'STRICT_TRANSPORT_SECURITY_MAX_AGE',
      'HTTP',
      'The time, in seconds, that the browser should remember that a site is only to be accessed using HTTPS. This value is used to populate the max-age attribute of the Strict-Transport-Security header, the presence of which is controlled by the ENABLE_STRICT_TRANSPORT_SECURITY_HEADER parameter. The default value is 86400 seconds (24 hours).',
      'GLOBAL',
      'INTEGER',
      '86400',
      1,
      'Security',
      '8.3-SP2',
      0
   );

END;
/
