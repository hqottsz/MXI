--liquibase formatted sql


--changeSet DEV-1367:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
       utl_migr_data_pkg.action_parm_insert(
                    'ACTION_MANAGE_CHARGES',
                    'Permission to manage charges.',
                    'TRUE/FALSE',
                    'false',
                     1,
                     'Org - Financials',
                     '8.0',
                     0,
                     0,
                    utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                    );
END;
/

--changeSet DEV-1367:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
       utl_migr_data_pkg.action_parm_insert(
                    'ACTION_CREATE_CHARGE',
                    'Permission to define (create) a new charge.',
                    'TRUE/FALSE',
                    'false',
                     1,
                     'Org - Financials',
                     '8.0',
                     0,
                     0,
                    utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                    );
END;
/

--changeSet DEV-1367:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
       utl_migr_data_pkg.action_parm_insert(
                    'ACTION_MODIFY_CHARGE',
                    'Permission to modify an existing charge.',
                    'TRUE/FALSE',
                    'false',
                     1,
                     'Org - Financials',
                     '8.0',
                     0,
                     0,
                    utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                    );
END;
/

--changeSet DEV-1367:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
       utl_migr_data_pkg.action_parm_insert(
                    'ACTION_DELETE_CHARGE',
                    'Permission to delete an existing charge.',
                    'TRUE/FALSE',
                    'false',
                     1,
                     'Org - Financials',
                     '8.0',
                     0,
                     0,
                    utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                    );
END;
/

--changeSet DEV-1367:5 stripComments:false
-- Manage charges menu item
INSERT INTO 
     UTL_MENU_ITEM 
     (
	MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,
	NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID
     )
     SELECT 10156, NULL, 'web.menuitem.MANAGE_CHARGES', '/maintenix/web/procurement/ManageTaxCharge.jsp?aWorkflow=CHARGE',0,'Manage charges',0,0
     FROM
	dual
     WHERE NOT EXISTS ( SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 10156 );

--changeSet DEV-1367:6 stripComments:false
-- Manage charges TODO Button
INSERT INTO
   UTL_TODO_BUTTON
   (
      TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID
   )
   SELECT 10062, 'ACTION_MANAGE_CHARGES', 'web.todobutton.MANAGE_CHARGES_NAME', '/common/images/tracker/tax_prp.gif', '/web/procurement/ManageTaxCharge.jsp?aWorkflow=CHARGE', 'web.todobutton.MANAGE_CHARGES_TOOLTIP', 'web.todobutton.MANAGE_CHARGES_LDESC', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_TODO_BUTTON WHERE TODO_BUTTON_ID = 10062 ); 