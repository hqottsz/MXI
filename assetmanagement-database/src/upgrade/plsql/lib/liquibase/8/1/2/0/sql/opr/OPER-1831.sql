--liquibase formatted sql


--changeSet OPER-1831:1 stripComments:false
/*******************************************************
 * Edit Manage AD Status button and Manage SB Status button after webapps were migrated from opr to eng
 *******************************************************/
 UPDATE UTL_TODO_BUTTON 
   SET ACTION = '/../eng/ui/maint/exe/workpackage/ManageADStatusOptions.html'
WHERE TODO_BUTTON_ID = 10065;

--changeSet OPER-1831:2 stripComments:false
UPDATE UTL_TODO_BUTTON 
   SET ACTION = '/../eng/ui/maint/exe/workpackage/ManageSBStatusOptions.html'
WHERE TODO_BUTTON_ID = 10066;