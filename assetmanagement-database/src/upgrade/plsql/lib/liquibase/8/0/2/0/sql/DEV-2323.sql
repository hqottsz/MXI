--liquibase formatted sql


--changeSet DEV-2323:1 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT
   120938,
   null,
   'web.menuitem.MANAGE_SKILLS',
   '/maintenix/web/org/ManageSkills.jsp',
   0,
   'Manage Skills',
   0,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_item WHERE menu_id = 120938
   )
;