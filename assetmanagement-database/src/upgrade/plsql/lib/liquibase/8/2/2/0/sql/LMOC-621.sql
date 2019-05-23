--liquibase formatted sql

--changeSet LMOC-621:1 stripComments:false
/**************************************************************************************
* 
* Insert script for UTL_MENU_ITEM to navigate from Maintenix to Phone Up Deferral in LMOC
*
***************************************************************************************/
INSERT INTO 
   utl_menu_item 
      (
         menu_id, todo_list_id, menu_name, menu_link_url, new_window_bool, menu_ldesc, repl_approved, utl_id
      )
   SELECT 
      120944,NULL,'web.menuitem.PHONE_UP_DEFERRAL','/lmocweb/phone-up-deferral/index.html',0,'Phone Up Deferral',0,0
   FROM
      DUAL
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_menu_item WHERE menu_id = 120944);