--liquibase formatted sql


--changeSet DEV-2107:1 stripComments:false
INSERT INTO 
        utl_menu_item 
     	(
	   MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID
      	)
SELECT 
     	120937,NULL,'web.menuitem.ARC_MESSAGE_MANAGEMENT', '/arc',0,'Manage ARC Messages',0,0
FROM
	dual
WHERE NOT EXISTS ( SELECT 1 FROM utl_menu_item WHERE MENU_ID = 120937 ); 