--liquibase formatted sql


--changeSet DEV-2295:1 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
SELECT
   11011,
   null,
   'web.menuitem.BUSINESS_INTELLIGENCE',
   '/maintenix/servlet/BusinessIntelligenceRedirect',
   1,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_item WHERE menu_id = 11011
   )
;