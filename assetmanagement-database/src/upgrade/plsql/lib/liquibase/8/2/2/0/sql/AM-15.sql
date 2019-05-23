--liquibase formatted sql


--changeSet AM-15:1 stripComments:false
/**************************************************************************************
* 
* AM-15 Permission to launch SMA and add button for launching SMA
*
***************************************************************************************/
INSERT INTO 
  utl_action_config_parm
  (
   PARM_VALUE, PARM_NAME, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
    'false', 'ACTION_OPEN_SMA', 'Permission to launch station monitoring application.','TRUE/FALSE', 'FALSE', 1,'Maint - Station Monitoring', '8.2-SP3',0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'ACTION_OPEN_SMA' );	

--changeSet AM-15:2 stripComments:false
INSERT INTO 
  utl_todo_button
  (
    TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID
  )
  SELECT
    10069, 'ACTION_OPEN_SMA', 'web.todobutton.APP_STN_MONITORING', NULL, ':open:/../lmocweb/station-monitoring', 'web.todobutton.APP_STN_MONITORING_TOOL_TIP', 'web.todobutton.APP_STN_MONITORING_LDESC', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_todo_button WHERE parm_name = 'ACTION_OPEN_SMA' );