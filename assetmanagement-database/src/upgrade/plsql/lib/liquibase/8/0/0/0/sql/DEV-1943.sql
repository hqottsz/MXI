--liquibase formatted sql


--changeSet DEV-1943:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create new configuration parameter MOBILE_REGEXP
-- to identify iPad, using parm_value .*(iPad).*
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'MOBILE_REGEXP',
      'MXCOMMONWEB',
      'This parameter is used to identify httprequest from mobile device.',
      'USER',
      '',
      '.*(iPad).*',
      1,
      'HTML Parameter',
      '8.0-SP1',
      0
   );
END;
/

--changeSet DEV-1943:2 stripComments:false
-- Add menu item of mobile line maintenance
INSERT INTO 
     UTL_MENU_ITEM 
     (
	MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,
	NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID
     )
     SELECT 120936, NULL, 'web.menuitem.LINE_MAINTENANCE', '/maintenix/mobile/linemaint/app.html',0,'Mobile Line Maintenance',0,0
     FROM
	dual
     WHERE NOT EXISTS ( SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 120936 );