--liquibase formatted sql


--changeSet QC-6733:1 stripComments:false
--
-- MX-6733 - Remove the config parm ASSIGN_FAULT_DEFINITION.
--           As it was previously renamed to ACTION_ASSIGN_FAULT_DEFINITION and 
--           subsequently moved to the utl_action_config_parm table.
--
-- Delete the config parm from utl_user_parm
DELETE FROM
      utl_user_parm
WHERE
      utl_user_parm.parm_name = 'ASSIGN_FAULT_DEFINITION'AND
      utl_user_parm.parm_type = 'SECURED_RESOURCE'; 

--changeSet QC-6733:2 stripComments:false
-- Delete the config parm from utl_role_parm
DELETE FROM
      utl_role_parm
WHERE
      utl_role_parm.parm_name = 'ASSIGN_FAULT_DEFINITION'AND
      utl_role_parm.parm_type = 'SECURED_RESOURCE'; 

--changeSet QC-6733:3 stripComments:false
-- Delete the config parm from db_type_config_parm
DELETE FROM
      db_type_config_parm
WHERE
      db_type_config_parm.parm_name = 'ASSIGN_FAULT_DEFINITION'; 

--changeSet QC-6733:4 stripComments:false
-- Delete the config parm from utl_config_parm
DELETE FROM
      utl_config_parm
WHERE
      utl_config_parm.parm_name = 'ASSIGN_FAULT_DEFINITION' AND
      utl_config_parm.parm_type = 'SECURED_RESOURCE';