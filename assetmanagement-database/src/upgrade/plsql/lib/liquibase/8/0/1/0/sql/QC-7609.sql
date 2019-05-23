--liquibase formatted sql


--changeSet QC-7609:1 stripComments:false
INSERT INTO 
        utl_menu_item 
     	(
	   MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID
      	)
  SELECT 
     	10153,NULL,'web.menuitem.ADVANCED_SEARCH', '/maintenix/web/search/AdvancedSearch.jsp',0,'Advanced Search',0,0
  FROM
	dual
  WHERE NOT EXISTS ( SELECT 1 FROM utl_menu_item WHERE MENU_ID = 10153 ); 