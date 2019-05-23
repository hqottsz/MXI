--liquibase formatted sql


--changeSet DEV-1445_2:1 stripComments:false
/******************************************************************************
 *
 * Insert alert data for LPA
 *
/*******************************************************************************/
INSERT INTO utl_alert_type 
( 
   alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
)
SELECT 235 , 'core.alert.LPA_RUN_COMPLETE_NO_ISSUES_name', 'core.alert.LPA_RUN_COMPLETE_NO_ISSUES_description', 'ROLE', null, 'PLANNING', 'core.alert.LPA_RUN_COMPLETE_NO_ISSUES_message', 1, 0, null, 1, 0
FROM
  dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 235 );

--changeSet DEV-1445_2:2 stripComments:false
-- Fleet Settings menu item
/******************************************************************************
 *
 * New 0-level data in 0utl_todo_tab.sql for Line Planning Console page
 *
 *******************************************************************************/ 
INSERT INTO UTL_MENU_ITEM 
	(MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
SELECT 
	120932, NULL,'web.menuitem.FLEET_SETTINGS', '/maintenix/web/lpa/FleetSettings.jsp',  0, 0
FROM 
	dual 
WHERE NOT EXISTS 
	(
		SELECT 
			1 
		FROM 
			UTL_MENU_ITEM 
		WHERE 
			UTL_MENU_ITEM.MENU_ID = 120932
	);	      

--changeSet DEV-1445_2:3 stripComments:false
/******************************************************************************
 *
 * New work item type for running basic line planning automation   
 *
 *******************************************************************************/ 	 
      INSERT INTO 
        utl_work_item_type 
        ( 
          name, worker_class, work_manager, enabled, utl_id 
        )
        SELECT 
          'BASIC_LINE_PLANNING_AUTOMATION', 'com.mxi.mx.core.worker.lpa.basic.BasicLinePlanningAutomationWorker', 'wm/Maintenix-BasicLinePlanningAutomationWorkManager', 1, 0
        FROM
          DUAL
        WHERE
    	  NOT EXISTS (SELECT 1 FROM utl_work_item_type WHERE name = 'BASIC_LINE_PLANNING_AUTOMATION');    	              	  

--changeSet DEV-1445_2:4 stripComments:false
/******************************************************************************
 *
 * Line Planning Automation configuration parameter. 
 *
 *******************************************************************************/
INSERT INTO UTL_CONFIG_PARM 
	(
		PARM_NAME, 
		PARM_VALUE, 
		PARM_TYPE, 
		ENCRYPT_BOOL,
		PARM_DESC, 
		CONFIG_TYPE, 
		ALLOW_VALUE_DESC, 
		DEFAULT_VALUE, 
		MAND_CONFIG_BOOL, 
		CATEGORY, 
		MODIFIED_IN, 
		REPL_APPROVED,
		UTL_ID
	)
SELECT 
	'LINE_PLANNING_AUTOMATION_MODE', 
	'ADVANCED',  
	'LOGIC', 
	0, 
	'Specifies the mode in which line planning automation will run. Advanced automation takes into account capacity, whereas basic automation does not.', 
	'GLOBAL', 
	'BASIC/ADVANCED', 
	'ADVANCED', 
	1, 
	'Maint - Planning', 
	'7.2 SP1', 
	0,
	0
FROM 
	dual 
WHERE NOT EXISTS 
	(
		SELECT 
			1 
		FROM 
			UTL_CONFIG_PARM 
		WHERE 
			UTL_CONFIG_PARM.PARM_NAME = 'LINE_PLANNING_AUTOMATION_MODE'
			AND
		    UTL_CONFIG_PARM.PARM_TYPE = 'LOGIC'
	);

--changeSet DEV-1445_2:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/******************************************************************************
 *
 * Secured resource for edit fleet settings page
 *
 *******************************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                'ACTION_EDIT_FLEET_SETTINGS',
                'Permission to edit fleet settings.',
                'TRUE/FALSE',
                'FALSE',
                1,
                'Maint - Planning',
                '7.2 SP1',
                0,
                0,
                utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                );
END;
/			

--changeSet DEV-1445_2:6 stripComments:false
/******************************************************************************
 *
 * FleetSettings page: selected fleet parameter 
 *
 *******************************************************************************/	
INSERT INTO UTL_CONFIG_PARM 
	(PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT
	null, 'sFleet', 'SESSION','Selected Fleet parameter','USER',  '', '', 0, 'Maint - Planning', '7.2 SP1', 0
FROM 
	dual 
WHERE NOT EXISTS 
	(
		SELECT 
			1 
		FROM 
			UTL_CONFIG_PARM 
		WHERE 
			UTL_CONFIG_PARM.PARM_NAME = 'sFleet'
	);	

--changeSet DEV-1445_2:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add new ACTION_CREATE_TURN_CHECK to utl_action_config_parm and db_type_config_parm tables	
/****************************************************************************************
 *
 * Add new ACTION_CREATE_TURN_CHECK action parameter 
 * Add new ACTION_CREATE_SERVICE_CHECK action parameter 
 * Replace ACTION_CREATE_TURN_OVERNIGHT_CHECK action parameter with ACTION_CREATE_TURN_CHECK
 * Delete the ACTION_CREATE_TURN_OVERNIGHT_CHECK action parameter
 *
 ****************************************************************************************/	
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                'ACTION_CREATE_TURN_CHECK',
                'Permission to create a turn check.',
                'TRUE/FALSE',
                'FALSE',
                1,
                'Maint - Work Packages',
                '7.2 SP1',
                0,
                0,
                utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                );
END;
/

--changeSet DEV-1445_2:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add new ACTION_CREATE_SERVICE_CHECK to utl_action_config_parm and db_type_config_parm tables
BEGIN
   utl_migr_data_pkg.action_parm_insert(
                'ACTION_CREATE_SERVICE_CHECK',
                'Permission to create a service check.',
                'TRUE/FALSE',
                'FALSE',
                1,
                'Maint - Work Packages',
                '7.2 SP1',
                0,
                0,
                utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                );
END;
/			

--changeSet DEV-1445_2:9 stripComments:false
-- replace ACTION_CREATE_TURN_OVERNIGHT_CHECK action param with ACTION_CREATE_TURN_CHECK
UPDATE UTL_ACTION_ROLE_PARM 
SET UTL_ACTION_ROLE_PARM.PARM_NAME = 'ACTION_CREATE_TURN_CHECK' 
WHERE UTL_ACTION_ROLE_PARM.PARM_NAME = 'ACTION_CREATE_TURN_OVERNIGHT_CHECK';

--changeSet DEV-1445_2:10 stripComments:false
UPDATE UTL_ACTION_USER_PARM 
SET UTL_ACTION_USER_PARM.PARM_NAME = 'ACTION_CREATE_TURN_CHECK' 
WHERE UTL_ACTION_USER_PARM.PARM_NAME = 'ACTION_CREATE_TURN_OVERNIGHT_CHECK';

--changeSet DEV-1445_2:11 stripComments:false
-- after replacing all references now delete the ACTION_CREATE_TURN_OVERNIGHT_CHECK action param
DELETE FROM DB_TYPE_CONFIG_PARM
WHERE DB_TYPE_CONFIG_PARM.PARM_NAME = 'ACTION_CREATE_TURN_OVERNIGHT_CHECK';

--changeSet DEV-1445_2:12 stripComments:false
DELETE FROM UTL_ACTION_CONFIG_PARM 
WHERE UTL_ACTION_CONFIG_PARM.PARM_NAME = 'ACTION_CREATE_TURN_OVERNIGHT_CHECK'; 

--changeSet DEV-1445_2:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/****************************************************************************************
 *
 * Add new ACTION_LPA_CLEAR_SELECTED_RUNS secured resource
 * 
 ****************************************************************************************/
 BEGIN
    utl_migr_data_pkg.action_parm_insert(
                 'ACTION_LPA_CLEAR_SELECTED_RUNS',
                 'Permission to clear selected runs.',
                 'TRUE/FALSE',
                 'FALSE',
                 1,
                 'Maint - Planning',
                 '7.2 SP1',
                 0,
                 0,
                 utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                 );
 END;
/        

--changeSet DEV-1445_2:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/****************************************************************************************
 *
 * Add new ACTION_RUN_BASIC_LPA secured resource
 * 
 ****************************************************************************************/   
 BEGIN
     utl_migr_data_pkg.action_parm_insert(
                  'ACTION_RUN_BASIC_LPA',
                  'Permission to run basic line planning automation.',
                  'TRUE/FALSE',
                  'FALSE',
                  1,
                  'Maint - Planning',
                  '7.2 SP1',
                  0,
                  0,
                  utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                  );
  END;
/  	  	  