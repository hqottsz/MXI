--liquibase formatted sql


--changeSet DEV-1362:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
       utl_migr_data_pkg.action_parm_insert(
                    'ACTION_MANAGE_TAXES',
                    'Permission to manage taxes.',
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

--changeSet DEV-1362:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
       utl_migr_data_pkg.action_parm_insert(
                    'ACTION_CREATE_TAX',
                    'Permission to define (create) a new tax.',
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

--changeSet DEV-1362:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
       utl_migr_data_pkg.action_parm_insert(
                    'ACTION_MODIFY_TAX',
                    'Permission to modify an existing tax.',
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

--changeSet DEV-1362:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
       utl_migr_data_pkg.action_parm_insert(
                    'ACTION_DELETE_TAX',
                    'Permission to delete an existing tax.',
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

--changeSet DEV-1362:5 stripComments:false
-- Manage taxes menu item
INSERT INTO 
     UTL_MENU_ITEM 
     (
	MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,
	NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID
     )
     SELECT 10155, NULL, 'web.menuitem.MANAGE_TAXES', '/maintenix/web/procurement/ManageTaxCharge.jsp?aWorkflow=TAX',0,'Manage taxes',0,0
     FROM
	dual
     WHERE NOT EXISTS ( SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 10155 );

--changeSet DEV-1362:6 stripComments:false
-- Manage taxes TODO Button
INSERT INTO
   UTL_TODO_BUTTON
   (
      TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID
   )
   SELECT 10061, 'ACTION_MANAGE_TAXES', 'web.todobutton.MANAGE_TAXES_NAME', '/common/images/tracker/tax_prp.gif', '/web/procurement/ManageTaxCharge.jsp?aWorkflow=TAX', 'web.todobutton.MANAGE_TAXES_TOOLTIP', 'web.todobutton.MANAGE_TAXES_LDESC', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_TODO_BUTTON WHERE TODO_BUTTON_ID = 10061 ); 