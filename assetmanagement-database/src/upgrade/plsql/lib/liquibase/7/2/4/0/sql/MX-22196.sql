--liquibase formatted sql


--changeSet MX-22196:1 stripComments:false
INSERT INTO 
UTL_MENU_ITEM 
     (
	MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,
	NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID
      )
SELECT  
	10150,NULL,'web.menuitem.MPC_SEARCH_BY_TYPE', 
	'/maintenix/web/taskdefn/MPCSearchByType.jsp',0,'Master Panel Card Search By Type',0,0
FROM
	dual
WHERE
	NOT EXISTS
	(
		SELECT
			1
		FROM
			UTL_MENU_ITEM
		WHERE
			MENU_ID = 10150
	)
;