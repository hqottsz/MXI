--liquibase formatted sql

--changeSet OPER-12747:1 stripComments:false 
UPDATE 
   utl_menu_item
SET
   menu_link_url = '/wpl-web/ui/induction/WorkPackageLoader.html'
   
WHERE
   menu_id = 120949;
   
--changeSet OPER-12747:2 stripComments:false 
UPDATE 
   utl_todo_button
SET
   action = '/../wpl-web/ui/induction/WorkPackageLoader.html'
   
WHERE
   todo_button_id = 10073;   