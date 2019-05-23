--liquibase formatted sql


--changeSet MX-19867:1 stripComments:false
-- Delete the config parm from utl_user_parm
/* *** Delete ACTION_ISSUE_INVENTORY_TO_TASK_WITH_WARNINGS configuration parameter *** */
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_ISSUE_INVENTORY_TO_TASK_WITH_WARNINGS'AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet MX-19867:2 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_ISSUE_INVENTORY_TO_TASK_WITH_WARNINGS'AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet MX-19867:3 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_ISSUE_INVENTORY_TO_TASK_WITH_WARNINGS' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet MX-19867:4 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_ISSUE_INVENTORY_TO_TASK_WITH_WARNINGS' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';                  

--changeSet MX-19867:5 stripComments:false
-- Delete the config parm from utl_user_parm
/* *** Delete ACTION_ISSUE_INVENTORY_TO_FAULT_WITH_WARNINGS configuration parameter *** */
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ACTION_ISSUE_INVENTORY_TO_FAULT_WITH_WARNINGS'AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE';

--changeSet MX-19867:6 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ACTION_ISSUE_INVENTORY_TO_FAULT_WITH_WARNINGS'AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE';

--changeSet MX-19867:7 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ACTION_ISSUE_INVENTORY_TO_FAULT_WITH_WARNINGS' AND
      db_type_config_parm.parm_type = 'SECURED_RESOURCE';

--changeSet MX-19867:8 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ACTION_ISSUE_INVENTORY_TO_FAULT_WITH_WARNINGS' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';      