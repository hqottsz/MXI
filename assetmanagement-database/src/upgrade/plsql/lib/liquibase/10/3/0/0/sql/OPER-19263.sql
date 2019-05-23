--liquibase formatted sql

--changeSet OPER-19263:1 stripComments:false
/**************************************************************************************
* Add a menu item to navigate to the oil uptake recording screen
***************************************************************************************/
INSERT INTO 
   utl_menu_item (
      menu_id, 
	  todo_list_id, 
	  menu_name, 
	  menu_link_url, 
	  new_window_bool, 
	  menu_ldesc, 
	  repl_approved, 
	  utl_id
   )
   SELECT 
      120950, 
	  NULL, 
	  'web.menuitem.RECORD_OIL_UPTAKE', 
	  '/lmocweb/oil-recording/index.html', 
	  0, 
	  'Record Oil Uptake', 
	  0, 
	  0
   FROM
      DUAL
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_menu_item WHERE menu_id = 120950 );