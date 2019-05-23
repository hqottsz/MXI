--liquibase formatted sql


--changeSet DEV-355:1 stripComments:false
-- Launch Production Planning and Control Button
INSERT INTO
   UTL_TODO_BUTTON
   (
      TODO_BUTTON_ID, PARM_NAME, PARM_TYPE, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID
   )
   SELECT 10060, 'APP_PRODUCTION_PLAN_CONTROLLER', 'SECURED_RESOURCE' , 'web.todobutton.LAUNCH_PPC_NAME', '/common/images/tracker/projPlan_open.gif', '/web/ppc/PpcApplicationJNLP.jsp', 'web.todobutton.LAUNCH_PPC_TOOLTIP', 'web.todobutton.LAUNCH_PPC_LDESC', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_TODO_BUTTON WHERE TODO_BUTTON_ID = 10060 );