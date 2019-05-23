--liquibase formatted sql

--changeSet OPER-22493:1 stripComments:false 
UPDATE 
   utl_todo_button
SET
   action = '/../lmocweb/pending-reference-approvals/index.html',
   button_name = 'web.todobutton.REFERENCE_APPROVAL_NAME',
   tooltip = 'web.todobutton.REFERENCE_APPROVAL_TOOLTIP',
   todo_button_ldesc = 'web.todobutton.REFERENCE_APPROVAL_LDESC'
WHERE
   todo_button_id = 10071;
   
--changeSet OPER-22493:2 stripComments:false 
UPDATE 
   utl_menu_item
SET
   menu_link_url = '/lmocweb/pending-reference-approvals/index.html',
   menu_name = 'web.menuitem.REFERENCE_APPROVAL',
   menu_ldesc = 'The List of Reference Approval Requests'
WHERE
   menu_id = 120947;