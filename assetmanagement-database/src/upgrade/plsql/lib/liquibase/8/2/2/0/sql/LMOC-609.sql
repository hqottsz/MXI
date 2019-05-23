--liquibase formatted sql

--changeSet LMOC-609:1 stripComments:false
/**************************************************************************************
* 
* Add To Do Button to Lauch Phone Up Deferral Application
*
***************************************************************************************/
INSERT INTO 
   utl_todo_button 
      (
         todo_button_id, parm_name, button_name, icon, action, tooltip, todo_button_ldesc, utl_id
      )
   SELECT 
      10067, NULL, 'web.todobutton.PHONE_UP_DEFERRAL_NAME', NULL, '/../lmocweb/phone-up-deferral/index.html', 'web.todobutton.PHONE_UP_DEFERRAL_TOOLTIP', 'web.todobutton.PHONE_UP_DEFERRAL_LDESC', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT * FROM utl_todo_button WHERE todo_button_id = 10067);