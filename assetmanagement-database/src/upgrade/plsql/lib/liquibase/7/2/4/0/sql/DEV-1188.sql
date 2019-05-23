--liquibase formatted sql


--changeSet DEV-1188:1 stripComments:false
/******************************************************************************************************
 * Delete the following records from the db_type_config_parm table:
 *    ACTION_ASSIGN_TASK_OR_FAULT_TO_CHECK
 *    ACTION_ASSIGN_TASK_OR_FAULT_TO_COMPLETED_CHECK
 *    ACTION_ASSIGN_TASK_OR_FAULT_TO_COMPONENT_WORK_ORDER
 *    ACTION_UNASSIGN_TASK
 ******************************************************************************************************/
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ASSIGN_TASK_OR_FAULT_TO_CHECK' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER';

--changeSet DEV-1188:2 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ASSIGN_TASK_OR_FAULT_TO_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER';

--changeSet DEV-1188:3 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_ASSIGN_TASK_OR_FAULT_TO_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER';

--changeSet DEV-1188:4 stripComments:false
DELETE FROM db_type_config_parm WHERE parm_name = 'ACTION_UNASSIGN_TASK' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER';

--changeSet DEV-1188:5 stripComments:false
-- disable foreign key constraint from utl_user_parm
/******************************************************************************************************
 * Change the following parameters in the utl_config_parm, utl_user_parm and utl_role_parm tables:
 *    change ACTION_ASSIGN_TASK_OR_FAULT_TO_CHECK parameter to ACTION_ASSIGN_TASK_TO_CHECK
 *    change ACTION_ASSIGN_TASK_OR_FAULT_TO_COMPLETED_CHECK parameter to ACTION_ASSIGN_TASK_TO_COMPLETED_CHECK
 *    change ACTION_ASSIGN_TASK_OR_FAULT_TO_COMPONENT_WORK_ORDER parameter to ACTION_ASSIGN_TASK_TO_COMPONENT_WORK_ORDER 
 *    change ACTION_UNASSIGN_TASK parameter to ACTION_UNASSIGN_TASK_FROM_CHECK  
 ******************************************************************************************************/
ALTER TABLE UTL_USER_PARM DISABLE CONSTRAINT FK_UTLCONFIGPARM_UTLUSERPARM;

--changeSet DEV-1188:6 stripComments:false
-- disable foreign key constraint from utl_role_parm
ALTER TABLE UTL_ROLE_PARM DISABLE CONSTRAINT FK_UTLCONFIGPARM_UTLROLEPARM;

--changeSet DEV-1188:7 stripComments:false
-- update the config parm type in utl_user_parm
UPDATE utl_user_parm SET parm_name = 'ACTION_ASSIGN_TASK_TO_CHECK' 
WHERE parm_name = 'ACTION_ASSIGN_TASK_OR_FAULT_TO_CHECK' AND parm_type = 'SECURED_RESOURCE';

--changeSet DEV-1188:8 stripComments:false
UPDATE utl_user_parm SET parm_name = 'ACTION_ASSIGN_TASK_TO_COMPLETED_CHECK' 
WHERE parm_name = 'ACTION_ASSIGN_TASK_OR_FAULT_TO_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE';

--changeSet DEV-1188:9 stripComments:false
UPDATE utl_user_parm SET parm_name = 'ACTION_ASSIGN_TASK_TO_COMPONENT_WORK_ORDER' 
WHERE parm_name = 'ACTION_ASSIGN_TASK_OR_FAULT_TO_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE';

--changeSet DEV-1188:10 stripComments:false
UPDATE utl_user_parm SET parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' 
WHERE parm_name = 'ACTION_UNASSIGN_TASK' AND parm_type = 'SECURED_RESOURCE';

--changeSet DEV-1188:11 stripComments:false
-- update the config parm type in utl_role_parm
UPDATE utl_role_parm SET parm_name = 'ACTION_ASSIGN_TASK_TO_CHECK'
WHERE parm_name = 'ACTION_ASSIGN_TASK_OR_FAULT_TO_CHECK' AND parm_type = 'SECURED_RESOURCE';

--changeSet DEV-1188:12 stripComments:false
UPDATE utl_role_parm SET parm_name = 'ACTION_ASSIGN_TASK_TO_COMPLETED_CHECK'
WHERE parm_name = 'ACTION_ASSIGN_TASK_OR_FAULT_TO_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE';

--changeSet DEV-1188:13 stripComments:false
UPDATE utl_role_parm SET parm_name = 'ACTION_ASSIGN_TASK_TO_COMPONENT_WORK_ORDER'
WHERE parm_name = 'ACTION_ASSIGN_TASK_OR_FAULT_TO_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE';

--changeSet DEV-1188:14 stripComments:false
UPDATE utl_role_parm SET parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK'
WHERE parm_name = 'ACTION_UNASSIGN_TASK' AND parm_type = 'SECURED_RESOURCE';

--changeSet DEV-1188:15 stripComments:false
-- update the config parm type in utl_config_parm
UPDATE utl_config_parm SET parm_name = 'ACTION_ASSIGN_TASK_TO_CHECK', CATEGORY = 'Maint - Tasks', MODIFIED_IN = '7.2', PARM_DESC = 'Permission to assign a task into a work package' 
WHERE parm_name = 'ACTION_ASSIGN_TASK_OR_FAULT_TO_CHECK' AND parm_type = 'SECURED_RESOURCE';

--changeSet DEV-1188:16 stripComments:false
UPDATE utl_config_parm SET parm_name = 'ACTION_ASSIGN_TASK_TO_COMPLETED_CHECK', MODIFIED_IN = '7.2', PARM_DESC = 'Permission to assign a task into a completed work package'
WHERE parm_name = 'ACTION_ASSIGN_TASK_OR_FAULT_TO_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE';

--changeSet DEV-1188:17 stripComments:false
UPDATE utl_config_parm SET parm_name = 'ACTION_ASSIGN_TASK_TO_COMPONENT_WORK_ORDER', CATEGORY = 'Maint - Tasks', MODIFIED_IN = '7.2', PARM_DESC = 'Permission to assign a task into a work order on a sub-component'
WHERE parm_name = 'ACTION_ASSIGN_TASK_OR_FAULT_TO_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE';

--changeSet DEV-1188:18 stripComments:false
UPDATE utl_config_parm SET parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK', CATEGORY = 'Maint - Tasks', MODIFIED_IN = '7.2', PARM_DESC = 'Permission to unassign a task from a work package'
WHERE parm_name = 'ACTION_UNASSIGN_TASK' AND parm_type = 'SECURED_RESOURCE';

--changeSet DEV-1188:19 stripComments:false
-- enable the foreign key constraints in the utl_user_parm table
ALTER TABLE UTL_USER_PARM ENABLE CONSTRAINT FK_UTLCONFIGPARM_UTLUSERPARM;

--changeSet DEV-1188:20 stripComments:false
-- enable the foreign key constraints in the utl_role_parm table
ALTER TABLE UTL_ROLE_PARM ENABLE CONSTRAINT FK_UTLCONFIGPARM_UTLROLEPARM;

--changeSet DEV-1188:21 stripComments:false
-- add assign in utl_config_parm
/******************************************************************************************************
 * Create the following parameters by duplicating the records for the smilimar parameters
 * in the utl_config_parm, utl_user_parm, utl_role_parm, and db_type_config_parm tables:
 *    ACTION_ASSIGN_FAULT_TO_CHECK parameter by duplicating ACTION_ASSIGN_TASK_TO_CHECK records 
 *    ACTION_ASSIGN_FAULT_TO_COMPLETED_CHECK parameter by duplicating ACTION_ASSIGN_TASK_TO_COMPLETED_CHECK records 
 *    ACTION_ASSIGN_FAULT_TO_COMPONENT_WORK_ORDER parameter by duplicating ACTION_ASSIGN_TASK_TO_COMPONENT_WORK_ORDER records 
 *    ACTION_UNASSIGN_FAULT_FROM_CHECK parameter by duplicating ACTION_UNASSIGN_TASK_FROM_CHECK records 
 *    ACTION_UNASSIGN_TASK_FROM_COMPLETED_CHECK parameter by duplicating ACTION_UNASSIGN_TASK_FROM_CHECK records
 *    ACTION_UNASSIGN_FAULT_FROM_COMPLETED_CHECK parameter by duplicating ACTION_UNASSIGN_TASK_FROM_CHECK records
 *    ACTION_UNASSIGN_TASK_FROM_COMPONENT_WORK_ORDER parameter by duplicating ACTION_UNASSIGN_TASK_FROM_CHECK records
 *    ACTION_UNASSIGN_FAULT_FROM_COMPONENT_WORK_ORDER parameter by duplicating ACTION_UNASSIGN_TASK_FROM_CHECK records
 ******************************************************************************************************/
INSERT INTO utl_config_parm (parm_value, parm_name, parm_type, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id)
SELECT parm_value, 'ACTION_ASSIGN_FAULT_TO_CHECK', parm_type, 'Permission to assign a fault into a work package', config_type, allow_value_desc, default_value, mand_config_bool, 'Maint - Faults', modified_in, utl_id
FROM utl_config_parm
WHERE parm_name = 'ACTION_ASSIGN_TASK_TO_CHECK' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ASSIGN_FAULT_TO_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:22 stripComments:false
INSERT INTO utl_config_parm (parm_value, parm_name, parm_type, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id)
SELECT parm_value, 'ACTION_ASSIGN_FAULT_TO_COMPLETED_CHECK', parm_type, 'Permission to assign a fault into a completed work package', config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
FROM utl_config_parm
WHERE parm_name = 'ACTION_ASSIGN_TASK_TO_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ASSIGN_FAULT_TO_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:23 stripComments:false
INSERT INTO utl_config_parm (parm_value, parm_name, parm_type, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id)
SELECT parm_value, 'ACTION_ASSIGN_FAULT_TO_COMPONENT_WORK_ORDER', parm_type, 'Permission to assign a fault into a work order on a sub-component', config_type, allow_value_desc, default_value, mand_config_bool, 'Maint - Faults', modified_in, utl_id
FROM utl_config_parm
WHERE parm_name = 'ACTION_ASSIGN_TASK_TO_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ASSIGN_FAULT_TO_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:24 stripComments:false
-- add unassign in utl_config_parm
INSERT INTO utl_config_parm (parm_value, parm_name, parm_type, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id)
SELECT parm_value, 'ACTION_UNASSIGN_FAULT_FROM_CHECK', parm_type, 'Permission to unassign a fault from a work package', config_type, allow_value_desc, default_value, mand_config_bool, 'Maint - Faults', modified_in, utl_id
FROM utl_config_parm
WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_UNASSIGN_FAULT_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:25 stripComments:false
INSERT INTO utl_config_parm (parm_value, parm_name, parm_type, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id)
SELECT parm_value, 'ACTION_UNASSIGN_TASK_FROM_COMPLETED_CHECK', parm_type, 'Permission to unassign a task from a completed work package', config_type, allow_value_desc, default_value, mand_config_bool, 'Maint - Historic Editing', modified_in, utl_id
FROM utl_config_parm
WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:26 stripComments:false
INSERT INTO utl_config_parm (parm_value, parm_name, parm_type, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id)
SELECT parm_value, 'ACTION_UNASSIGN_FAULT_FROM_COMPLETED_CHECK', parm_type, 'Permission to unassign a fault from a completed work package', config_type, allow_value_desc, default_value, mand_config_bool, 'Maint - Historic Editing', modified_in, utl_id
FROM utl_config_parm
WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_UNASSIGN_FAULT_FROM_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:27 stripComments:false
INSERT INTO utl_config_parm (parm_value, parm_name, parm_type, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id)
SELECT parm_value, 'ACTION_UNASSIGN_TASK_FROM_COMPONENT_WORK_ORDER', parm_type, 'Permission to unassign a task from a work order on a sub-component', config_type, allow_value_desc, default_value, mand_config_bool, 'Maint - Tasks', modified_in, utl_id
FROM utl_config_parm
WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:28 stripComments:false
INSERT INTO utl_config_parm (parm_value, parm_name, parm_type, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id)
SELECT parm_value, 'ACTION_UNASSIGN_FAULT_FROM_COMPONENT_WORK_ORDER', parm_type, 'Permission to unassign a fault from a work order on a sub-component', config_type, allow_value_desc, default_value, mand_config_bool, 'Maint - Faults', modified_in, utl_id
FROM utl_config_parm
WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_UNASSIGN_FAULT_FROM_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:29 stripComments:false
-- add assign in utl_role_parm
INSERT INTO utl_role_parm (role_id, parm_name, parm_type, parm_value, utl_id)
SELECT old_role_parm.role_id, 'ACTION_ASSIGN_FAULT_TO_CHECK', old_role_parm.parm_type, old_role_parm.parm_value, old_role_parm.utl_id
FROM utl_role_parm old_role_parm
WHERE old_role_parm.parm_name = 'ACTION_ASSIGN_TASK_TO_CHECK' AND old_role_parm.parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_role_parm WHERE role_id = old_role_parm.role_id AND parm_name = 'ACTION_ASSIGN_FAULT_TO_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:30 stripComments:false
INSERT INTO utl_role_parm (role_id, parm_name, parm_type, parm_value, utl_id)
SELECT old_role_parm.role_id, 'ACTION_ASSIGN_FAULT_TO_COMPLETED_CHECK', old_role_parm.parm_type, old_role_parm.parm_value, old_role_parm.utl_id
FROM utl_role_parm old_role_parm
WHERE old_role_parm.parm_name = 'ACTION_ASSIGN_TASK_TO_COMPLETED_CHECK' AND old_role_parm.parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_role_parm WHERE role_id = old_role_parm.role_id AND parm_name = 'ACTION_ASSIGN_FAULT_TO_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:31 stripComments:false
INSERT INTO utl_role_parm (role_id, parm_name, parm_type, parm_value, utl_id)
SELECT old_role_parm.role_id, 'ACTION_ASSIGN_FAULT_TO_COMPONENT_WORK_ORDER', old_role_parm.parm_type, old_role_parm.parm_value, old_role_parm.utl_id
FROM utl_role_parm old_role_parm
WHERE old_role_parm.parm_name = 'ACTION_ASSIGN_TASK_TO_COMPONENT_WORK_ORDER' AND old_role_parm.parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_role_parm WHERE role_id = old_role_parm.role_id AND parm_name = 'ACTION_ASSIGN_FAULT_TO_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:32 stripComments:false
-- add unassign in utl_role_parm
INSERT INTO utl_role_parm (role_id, parm_name, parm_type, parm_value, utl_id)
SELECT old_role_parm.role_id, 'ACTION_UNASSIGN_FAULT_FROM_CHECK', old_role_parm.parm_type, old_role_parm.parm_value, old_role_parm.utl_id
FROM utl_role_parm old_role_parm
WHERE old_role_parm.parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND old_role_parm.parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_role_parm WHERE role_id = old_role_parm.role_id AND parm_name = 'ACTION_UNASSIGN_FAULT_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:33 stripComments:false
INSERT INTO utl_role_parm (role_id, parm_name, parm_type, parm_value, utl_id)
SELECT old_role_parm.role_id, 'ACTION_UNASSIGN_TASK_FROM_COMPLETED_CHECK', old_role_parm.parm_type, old_role_parm.parm_value, old_role_parm.utl_id
FROM utl_role_parm old_role_parm
WHERE old_role_parm.parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND old_role_parm.parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_role_parm WHERE role_id = old_role_parm.role_id AND parm_name = 'ACTION_UNASSIGN_TASK_FROM_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:34 stripComments:false
INSERT INTO utl_role_parm (role_id, parm_name, parm_type, parm_value, utl_id)
SELECT old_role_parm.role_id, 'ACTION_UNASSIGN_FAULT_FROM_COMPLETED_CHECK', old_role_parm.parm_type, old_role_parm.parm_value, old_role_parm.utl_id
FROM utl_role_parm old_role_parm
WHERE old_role_parm.parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND old_role_parm.parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_role_parm WHERE role_id = old_role_parm.role_id AND parm_name = 'ACTION_UNASSIGN_FAULT_FROM_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:35 stripComments:false
INSERT INTO utl_role_parm (role_id, parm_name, parm_type, parm_value, utl_id)
SELECT old_role_parm.role_id, 'ACTION_UNASSIGN_TASK_FROM_COMPONENT_WORK_ORDER', old_role_parm.parm_type, old_role_parm.parm_value, old_role_parm.utl_id
FROM utl_role_parm old_role_parm
WHERE old_role_parm.parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND old_role_parm.parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_role_parm WHERE role_id = old_role_parm.role_id AND parm_name = 'ACTION_UNASSIGN_TASK_FROM_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:36 stripComments:false
INSERT INTO utl_role_parm (role_id, parm_name, parm_type, parm_value, utl_id)
SELECT old_role_parm.role_id, 'ACTION_UNASSIGN_FAULT_FROM_COMPONENT_WORK_ORDER', old_role_parm.parm_type, old_role_parm.parm_value, old_role_parm.utl_id
FROM utl_role_parm old_role_parm
WHERE old_role_parm.parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND old_role_parm.parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_role_parm WHERE role_id = old_role_parm.role_id AND parm_name = 'ACTION_UNASSIGN_FAULT_FROM_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:37 stripComments:false
-- add assign in utl_user_parm
INSERT INTO utl_user_parm (user_id, parm_name, parm_type, parm_value, utl_id, rstat_cd, creation_dt, revision_db_id, revision_user)
SELECT old_user_parm.user_id, 'ACTION_ASSIGN_FAULT_TO_CHECK', old_user_parm.parm_type, old_user_parm.parm_value, old_user_parm.utl_id, rstat_cd, SYSDATE, old_user_parm.revision_db_id, old_user_parm.revision_user
FROM utl_user_parm old_user_parm
WHERE parm_name = 'ACTION_ASSIGN_TASK_TO_CHECK' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_user_parm WHERE user_id = old_user_parm.user_id AND parm_name = 'ACTION_ASSIGN_FAULT_TO_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:38 stripComments:false
INSERT INTO utl_user_parm (user_id, parm_name, parm_type, parm_value, utl_id, rstat_cd, creation_dt, revision_db_id, revision_user)
SELECT old_user_parm.user_id, 'ACTION_ASSIGN_FAULT_TO_COMPLETED_CHECK', old_user_parm.parm_type, old_user_parm.parm_value, old_user_parm.utl_id, rstat_cd, SYSDATE, old_user_parm.revision_db_id, old_user_parm.revision_user
FROM utl_user_parm old_user_parm
WHERE parm_name = 'ACTION_ASSIGN_TASK_TO_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_user_parm WHERE user_id = old_user_parm.user_id AND parm_name = 'ACTION_ASSIGN_FAULT_TO_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:39 stripComments:false
INSERT INTO utl_user_parm (user_id, parm_name, parm_type, parm_value, utl_id, rstat_cd, creation_dt, revision_db_id, revision_user)
SELECT old_user_parm.user_id, 'ACTION_ASSIGN_FAULT_TO_COMPONENT_WORK_ORDER', old_user_parm.parm_type, old_user_parm.parm_value, old_user_parm.utl_id, rstat_cd, SYSDATE, old_user_parm.revision_db_id, old_user_parm.revision_user
FROM utl_user_parm old_user_parm
WHERE parm_name = 'ACTION_ASSIGN_TASK_TO_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_user_parm WHERE user_id = old_user_parm.user_id AND parm_name = 'ACTION_ASSIGN_FAULT_TO_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:40 stripComments:false
-- add unassign in utl_user_parm
INSERT INTO utl_user_parm (user_id, parm_name, parm_type, parm_value, utl_id, rstat_cd, creation_dt, revision_db_id, revision_user)
SELECT old_user_parm.user_id, 'ACTION_UNASSIGN_FAULT_FROM_CHECK', old_user_parm.parm_type, old_user_parm.parm_value, old_user_parm.utl_id, rstat_cd, SYSDATE, old_user_parm.revision_db_id, old_user_parm.revision_user
FROM utl_user_parm old_user_parm
WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_user_parm WHERE user_id = old_user_parm.user_id AND parm_name = 'ACTION_UNASSIGN_FAULT_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:41 stripComments:false
INSERT INTO utl_user_parm (user_id, parm_name, parm_type, parm_value, utl_id, rstat_cd, creation_dt, revision_db_id, revision_user)
SELECT old_user_parm.user_id, 'ACTION_UNASSIGN_TASK_FROM_COMPLETED_CHECK', old_user_parm.parm_type, old_user_parm.parm_value, old_user_parm.utl_id, rstat_cd, SYSDATE, old_user_parm.revision_db_id, old_user_parm.revision_user
FROM utl_user_parm old_user_parm
WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_user_parm WHERE user_id = old_user_parm.user_id AND parm_name = 'ACTION_UNASSIGN_TASK_FROM_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:42 stripComments:false
INSERT INTO utl_user_parm (user_id, parm_name, parm_type, parm_value, utl_id, rstat_cd, creation_dt, revision_db_id, revision_user)
SELECT old_user_parm.user_id, 'ACTION_UNASSIGN_FAULT_FROM_COMPLETED_CHECK', old_user_parm.parm_type, old_user_parm.parm_value, old_user_parm.utl_id, rstat_cd, SYSDATE, old_user_parm.revision_db_id, old_user_parm.revision_user
FROM utl_user_parm old_user_parm
WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_user_parm WHERE user_id = old_user_parm.user_id AND parm_name = 'ACTION_UNASSIGN_FAULT_FROM_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:43 stripComments:false
INSERT INTO utl_user_parm (user_id, parm_name, parm_type, parm_value, utl_id, rstat_cd, creation_dt, revision_db_id, revision_user)
SELECT old_user_parm.user_id, 'ACTION_UNASSIGN_TASK_FROM_COMPONENT_WORK_ORDER', old_user_parm.parm_type, old_user_parm.parm_value, old_user_parm.utl_id, rstat_cd, SYSDATE, old_user_parm.revision_db_id, old_user_parm.revision_user
FROM utl_user_parm old_user_parm
WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_user_parm WHERE user_id = old_user_parm.user_id AND parm_name = 'ACTION_UNASSIGN_TASK_FROM_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:44 stripComments:false
INSERT INTO utl_user_parm (user_id, parm_name, parm_type, parm_value, utl_id, rstat_cd, creation_dt, revision_db_id, revision_user)
SELECT old_user_parm.user_id, 'ACTION_UNASSIGN_FAULT_FROM_COMPONENT_WORK_ORDER', old_user_parm.parm_type, old_user_parm.parm_value, old_user_parm.utl_id, rstat_cd, SYSDATE, old_user_parm.revision_db_id, old_user_parm.revision_user
FROM utl_user_parm old_user_parm
WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_user_parm WHERE user_id = old_user_parm.user_id AND parm_name = 'ACTION_UNASSIGN_FAULT_FROM_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE');

--changeSet DEV-1188:45 stripComments:false
-- assign
/******************************************************************************************************
 * Insert the new records into the db_type_config_parm table
 ******************************************************************************************************/
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_ASSIGN_TASK_TO_CHECK', 'SECURED_RESOURCE', 'OPER'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ASSIGN_TASK_TO_CHECK' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER');

--changeSet DEV-1188:46 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_ASSIGN_FAULT_TO_CHECK', 'SECURED_RESOURCE', 'OPER'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ASSIGN_FAULT_TO_CHECK' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER');

--changeSet DEV-1188:47 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_ASSIGN_TASK_TO_COMPLETED_CHECK', 'SECURED_RESOURCE', 'OPER'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ASSIGN_TASK_TO_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER');

--changeSet DEV-1188:48 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_ASSIGN_FAULT_TO_COMPLETED_CHECK', 'SECURED_RESOURCE', 'OPER'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ASSIGN_FAULT_TO_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER');

--changeSet DEV-1188:49 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_ASSIGN_TASK_TO_COMPONENT_WORK_ORDER', 'SECURED_RESOURCE', 'OPER'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ASSIGN_TASK_TO_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER');

--changeSet DEV-1188:50 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_ASSIGN_FAULT_TO_COMPONENT_WORK_ORDER', 'SECURED_RESOURCE', 'OPER'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ASSIGN_FAULT_TO_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER');

--changeSet DEV-1188:51 stripComments:false
-- unassign
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_UNASSIGN_TASK_FROM_CHECK', 'SECURED_RESOURCE', 'OPER'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER');

--changeSet DEV-1188:52 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_UNASSIGN_FAULT_FROM_CHECK', 'SECURED_RESOURCE', 'OPER'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_UNASSIGN_FAULT_FROM_CHECK' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER');

--changeSet DEV-1188:53 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_UNASSIGN_TASK_FROM_COMPLETED_CHECK', 'SECURED_RESOURCE', 'OPER'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER');

--changeSet DEV-1188:54 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_UNASSIGN_FAULT_FROM_COMPLETED_CHECK', 'SECURED_RESOURCE', 'OPER'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_UNASSIGN_FAULT_FROM_COMPLETED_CHECK' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER');

--changeSet DEV-1188:55 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_UNASSIGN_TASK_FROM_COMPONENT_WORK_ORDER', 'SECURED_RESOURCE', 'OPER'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER');

--changeSet DEV-1188:56 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_UNASSIGN_FAULT_FROM_COMPONENT_WORK_ORDER', 'SECURED_RESOURCE', 'OPER'
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_UNASSIGN_FAULT_FROM_COMPONENT_WORK_ORDER' AND parm_type = 'SECURED_RESOURCE' AND db_type_cd = 'OPER');