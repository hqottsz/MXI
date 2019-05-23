--liquibase formatted sql

--changeSet OPER-29683:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      utl_migr_data_pkg.action_parm_insert
		(
         'ACTION_INVENTORY_COUNT',
         'Permission to perform inventory counts.',
         'TRUE/FALSE',
         'FALSE',
         1,
         'Supply - Inventory Count',
         '8.3-SP2',
         0,
         0
      );
END;
/

--changeSet OPER-29683:2 stripComments:false
INSERT INTO 
  utl_todo_button 
  (
    TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID
  )
  SELECT
    10074, 'ACTION_INVENTORY_COUNT', 'web.todobutton.INVENTORY_COUNT_NAME', NULL, '/web/inventorycount/InventoryCount.jsp', 'web.todobutton.INVENTORY_COUNT_TOOLTIP', 'web.todobutton.INVENTORY_COUNT_LDESC', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_todo_button WHERE TODO_BUTTON_ID = 10074 );