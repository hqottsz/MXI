--liquibase formatted sql
 

--changeSet DEV-1372:1 stripComments:false
/*******************************************************
 * Edit Vendor Tax menu item
 *******************************************************/
INSERT INTO 
        utl_menu_item 
     	(
		MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID
      	)
  SELECT 
     	10157,NULL,'web.menuitem.EDIT_VENDOR_TAX', '/maintenix/web/procurement/EditVendorTaxCharge.jsp?aWorkflow=TAX',0,'Edit Vendor Tax',0,0
  FROM
	dual
  WHERE NOT EXISTS ( SELECT 1 FROM utl_menu_item WHERE MENU_ID = 10157 );           

--changeSet DEV-1372:2 stripComments:false
/*******************************************************
 * Edit Vendor Tax action config parm
 *******************************************************/
 INSERT INTO 
	utl_action_config_parm 
	(
		PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
	)
  SELECT
  	'ACTION_EDIT_VENDOR_TAX','false',0,'Permission to edit vendor tax.','TRUE/FALSE', 'false', 1, 'Org - Financials', '8.0', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'ACTION_EDIT_VENDOR_TAX' );

--changeSet DEV-1372:3 stripComments:false
/*******************************************************
 * Edit Vendor tax TODO Button
 *******************************************************/
INSERT INTO
   	utl_todo_button
	(
		TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID
	) 
 SELECT 
	10063, 'ACTION_EDIT_VENDOR_TAX', 'web.todobutton.EDIT_VENDOR_TAX', '/common/images/tracker/pencil.gif', '/web/procurement/EditVendorTaxCharge.jsp?aWorkflow=TAX', 'web.todobutton.EDIT_VENDOR_TAX_TOOLTIP', 'web.todobutton.EDIT_VENDOR_TAX_LDESC', 0
 FROM
	dual
 WHERE
	NOT EXISTS ( SELECT 1 FROM UTL_TODO_BUTTON WHERE TODO_BUTTON_ID = 10063 ); 