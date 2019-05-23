--liquibase formatted sql


--changeSet OPER-6087:1 stripComments:false
/*************** Button **************/
INSERT INTO
   UTL_ACTION_CONFIG_PARM
   (
      PARM_NAME, PARM_VALUE, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'APP_PLANNING_VIEWER', 'false', 'Permission to access the Planning Viewer button' , 'TRUE/FALSE', 'FALSE', 1, 'Maint - Planning Viewer', '0806', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_ACTION_CONFIG_PARM WHERE PARM_NAME LIKE '%APP_PLANNING_VIEWER%' );

--changeSet OPER-6087:2 stripComments:false
INSERT INTO
   UTL_TODO_BUTTON
   (
      TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID
   )
   SELECT 10068, 'APP_PLANNING_VIEWER', 'web.todobutton.LAUNCH_PV_NAME', '/common/images/tracker/projPlan_open.gif', '/web/pv/PlanningViewerJNLP.jsp', 'web.todobutton.LAUNCH_PV_TOOLTIP', 'web.todobutton.LAUNCH_PV_LDESC', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_TODO_BUTTON WHERE TODO_BUTTON_ID = 10068 );

--changeSet OPER-6087:3 stripComments:false
/*************** Secured Resource **************/
INSERT INTO
   UTL_ACTION_CONFIG_PARM
   (
      PARM_NAME, PARM_VALUE, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_LOAD_PLANNING_VIEWER', 'FALSE', 'Permission to access the Planning Viewer data' , 'TRUE/FALSE', 'FALSE', 1, 'Maint - Planning Viewer', '0806', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_ACTION_CONFIG_PARM WHERE PARM_NAME = 'ACTION_LOAD_PLANNING_VIEWER' );

--changeSet OPER-6087:4 stripComments:false
INSERT INTO
   UTL_ACTION_CONFIG_PARM
   (
      PARM_NAME, PARM_VALUE, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ACTION_UPDATE_PREDICTED_DEADLINE', 'FALSE', 'Permission to update the Planning Viewer predicted deadline' , 'TRUE/FALSE', 'FALSE', 1, 'Maint - Planning Viewer', '0806', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_ACTION_CONFIG_PARM WHERE PARM_NAME = 'ACTION_UPDATE_PREDICTED_DEADLINE' );