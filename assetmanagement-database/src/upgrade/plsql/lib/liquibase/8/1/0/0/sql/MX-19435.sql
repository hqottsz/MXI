--liquibase formatted sql


--changeSet MX-19435:1 stripComments:false
DELETE FROM utl_menu_group_item 
WHERE menu_id IN ( SELECT menu_id FROM utl_menu_item WHERE menu_name LIKE 'ref_%' );

--changeSet MX-19435:2 stripComments:false
DELETE FROM utl_menu_item WHERE menu_name LIKE 'ref_%';