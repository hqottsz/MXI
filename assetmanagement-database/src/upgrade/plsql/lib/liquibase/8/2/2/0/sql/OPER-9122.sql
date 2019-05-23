--liquibase formatted sql

--changeSet OPER-9122:1 stripComments:false
/**************************************************************************************
* 
* Add a menu item to navigate to the Pending Deferral Requests in LMOC
*
***************************************************************************************/
INSERT INTO 
   utl_menu_item 
      (
         menu_id, todo_list_id, menu_name, menu_link_url, new_window_bool, menu_ldesc, repl_approved, utl_id
      )
   SELECT 
      120947, NULL, 'web.menuitem.DEFERRAL_AUTH', '/lmocweb/pending-deferral-requests/index.html', 0, 'Pending Deferral Request Page', 0, 0
   FROM
      DUAL
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_menu_item WHERE menu_id = 120947);
      
--changeSet OPER-9122:2 stripComments:false
/**************************************************************************************
* 
* Add To Do Button to the Pending Deferral Requests in LMOC
*
***************************************************************************************/
INSERT INTO 
   utl_todo_button 
      (
         todo_button_id, parm_name, button_name, icon, action, tooltip, todo_button_ldesc, utl_id
      )
   SELECT 
      10071, NULL, 'web.todobutton.DEFERRAL_AUTH_NAME', NULL, '/../lmocweb/pending-deferral-requests/index.html', 'web.todobutton.DEFERRAL_AUTH_TOOLTIP', 'web.todobutton.DEFERRAL_AUTH_LDESC', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT * FROM utl_todo_button WHERE todo_button_id = 10071);
      

