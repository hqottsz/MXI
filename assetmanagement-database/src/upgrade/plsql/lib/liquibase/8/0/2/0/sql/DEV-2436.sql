--liquibase formatted sql


--changeSet DEV-2436:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/* Remove existing reference to the ARC Menu Item.*/
BEGIN
	DELETE FROM utl_menu_item_arg WHERE menu_id = 120937;
	DELETE FROM utl_menu_group_item WHERE menu_id = 120937;
	DELETE FROM utl_menu_item WHERE menu_id = 120937;
END;
/