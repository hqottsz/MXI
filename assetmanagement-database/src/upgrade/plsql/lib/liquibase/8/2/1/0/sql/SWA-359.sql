--liquibase formatted sql


--changeSet SWA-359:1 stripComments:false
-- Add PO Exception Management menu item
INSERT INTO 
   utl_menu_item 
   (
      menu_id, todo_list_id, menu_name, menu_link_url, new_window_bool, utl_id
   )
   SELECT 
      120943, NULL, 'web.menuitem.SPEC2K_PO_EXCEPTION_SEARCH', '/integrationweb/spec2000poexceptionsearch/index.html', 0, 0 
   FROM
      dual
   WHERE 
      NOT EXISTS ( SELECT 1 FROM utl_menu_item WHERE menu_id = 120943 );