--liquibase formatted sql

--changeSet OPER-27508:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'ENABLE_CONTENT_SECURITY_POLICY_HEADER',
      'HTTP',
      'Controls if the Content-Security-Policy header is added to each HTTP response. By default, the value of the header is set to "default-src ''self''; style-src ''self'' ''unsafe-inline'' ''unsafe-eval''; script-src ''self'' ''unsafe-inline'' ''unsafe-eval'';", which will allow only css, images, scripts, and other resources to be displayed only if they are present on the same host as Maintenix.',
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
