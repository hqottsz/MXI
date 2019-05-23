--liquibase formatted sql


--changeSet MX-26566:1 stripComments:false
UPDATE utl_menu_item
SET    utl_menu_item.TODO_LIST_ID = NULL
WHERE  utl_menu_item.menu_name = 'web.dev.menuitem.PLAN_COMPARISION_REPORT';