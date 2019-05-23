--liquibase formatted sql


--changeSet QC-6939:1 stripComments:false
INSERT INTO 
        utl_menu_item 
     	(
		MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL, UTL_ID
      	)
  SELECT 
     	11009,NULL,'web.menuitem.FUNCTION_ACTION_ACCESS', '/maintenix/servlet/report/generate?aTemplate=utility.FunctionActionAccess&aViewPDF=true',0 ,0
  FROM
	dual
  WHERE NOT EXISTS ( SELECT 1 FROM utl_menu_item WHERE MENU_ID = 11009 );    

--changeSet QC-6939:2 stripComments:false
INSERT INTO 
        utl_menu_item 
     	(
		MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL, UTL_ID
      	)
  SELECT 
     	11010,NULL,'web.menuitem.FUNCTION_ACTION_ACCESS_ROLE', '/maintenix/servlet/report/generate?aTemplate=utility.FunctionActionAccessRole&aViewPDF=true',0,0
  FROM
	dual
  WHERE NOT EXISTS ( SELECT 1 FROM utl_menu_item WHERE MENU_ID = 11010 );                 