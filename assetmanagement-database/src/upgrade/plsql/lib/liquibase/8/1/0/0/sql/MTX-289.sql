--liquibase formatted sql


--changeSet MTX-289:1 stripComments:false
UPDATE utl_menu_item
SET    utl_menu_item.menu_ldesc = 'Production Planning and Control Management Page'
WHERE  utl_menu_item.menu_id = '120939';