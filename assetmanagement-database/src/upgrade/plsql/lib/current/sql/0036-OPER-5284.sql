--liquibase formatted sql
--changeset OPER-5284:1 stripComments:false
UPDATE
   utl_action_config_parm
SET
   parm_desc = 'Permission to package and complete tasks by clicking the Package And Complete Task button on either the Open Tasks tab of the Inventory Details page or the Storage Maintenance tab. When you click the button, the Complete Task page opens and you can enter location, date, and usage information for the parent and all sub-assemblies. Visibility of the button depends on the context (aircraft or uninstalled component) and the status of the ACTION_CREATE_COMPONENT_WORK_ORDER and ACTION_CREATE_BLANK_CHECK permissions.'
WHERE
   parm_name = 'ACTION_PACKAGE_AND_COMPLETE_TASK';