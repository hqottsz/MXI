--liquibase formatted sql
 

--changeSet DEV-1374:1 stripComments:false
/*******************************************************
 * Edit Vendor Charge menu item
 *******************************************************/
INSERT INTO 
        utl_menu_item 
     	(
		MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID
      	)
  SELECT 
     	10158,NULL,'web.menuitem.EDIT_VENDOR_CHARGE', '/maintenix/web/procurement/EditVendorTaxCharge.jsp?aWorkflow=CHARGE',0,'Edit Vendor Charge',0,0
  FROM
	dual
  WHERE NOT EXISTS ( SELECT 1 FROM utl_menu_item WHERE MENU_ID = 10158 );           

--changeSet DEV-1374:2 stripComments:false
/*******************************************************
 * Edit Vendor charge action config parm
 *******************************************************/
 INSERT INTO 
	utl_action_config_parm 
	(
		PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
	)
  SELECT
  	'ACTION_EDIT_VENDOR_CHARGE','false',0,'Permission to edit vendor charge.','TRUE/FALSE', 'false', 1, 'Org - Financials', '8.0', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'ACTION_EDIT_VENDOR_CHARGE' );

--changeSet DEV-1374:3 stripComments:false
/*******************************************************
 * Edit Vendor charge TODO Button
 *******************************************************/
INSERT INTO
   	utl_todo_button
	(
		TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID
	) 
 SELECT 
	10064, 'ACTION_EDIT_VENDOR_CHARGE', 'web.todobutton.EDIT_VENDOR_CHARGE', '/common/images/tracker/pencil.gif', '/web/procurement/EditVendorTaxCharge.jsp?aWorkflow=CHARGE', 'web.todobutton.EDIT_VENDOR_CHARGE_TOOLTIP', 'web.todobutton.EDIT_VENDOR_CHARGE_LDESC', 0
 FROM
	dual
 WHERE
	NOT EXISTS ( SELECT 1 FROM UTL_TODO_BUTTON WHERE TODO_BUTTON_ID = 10064 ); 