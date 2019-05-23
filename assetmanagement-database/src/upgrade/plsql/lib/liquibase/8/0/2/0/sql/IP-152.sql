--liquibase formatted sql


--changeSet IP-152:1 stripComments:false
--
-- IP-152
--
-- Remove the menu item for the misnamed Users Guide.
--
delete from utl_menu_group_item where menu_id = 10902;

--changeSet IP-152:2 stripComments:false
delete from utl_menu_item_arg where menu_id = 10902;

--changeSet IP-152:3 stripComments:false
delete from utl_menu_item where menu_id = 10902;