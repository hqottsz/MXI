--liquibase formatted sql

--changeSet OPER-29772:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'ENABLE_FRAME_OPTIONS_HEADER',
      'HTTP',
      'Controls if the X-Frame-Options header is added to each HTTP response. By default, the value of the header is set to "sameorigin", which will protect Maintenix from being placed within an iframe element loaded from another site, while still allowing embedded iframes to display content.',
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
