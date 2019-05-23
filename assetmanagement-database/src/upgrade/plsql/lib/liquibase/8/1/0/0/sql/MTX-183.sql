--liquibase formatted sql


--changeSet MTX-183:1 stripComments:false
-- PPC Management Console
INSERT INTO 
   UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 
   120939,NULL,'web.menuitem.PPC_MGMT', '/maintenix/ui/maint/planning/ppc/MgmtConsole.jsp',0,'PPC Management',0,0
  FROM DUAL
 WHERE NOT EXISTS ( SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 120939 );