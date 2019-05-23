--liquibase formatted sql

--changeSet OPER-22555:1 stripComments:false
/**************************************************************************************
* Add a menu item to navigate to the quick text management(admin) screen
***************************************************************************************/
INSERT INTO
   utl_menu_item
      (
         menu_id, todo_list_id, menu_name, menu_link_url, new_window_bool, menu_ldesc, repl_approved, utl_id
      )
   SELECT
      120951, NULL, 'web.menuitem.QUICK_TEXT', '/lmocweb/quick-text/index.html', 0, 'Manage Quick Text', 0, 0
   FROM
      DUAL
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_menu_item WHERE menu_id = 120951);