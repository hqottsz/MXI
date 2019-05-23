--liquibase formatted sql


--changeSet MX-18901:1 stripComments:false
DELETE FROM utl_role_parm t where t.parm_name = 'ACTION_CREATE_ASSIGNED_FAULT';

--changeSet MX-18901:2 stripComments:false
DELETE FROM utl_user_parm t where t.parm_name = 'ACTION_CREATE_ASSIGNED_FAULT';

--changeSet MX-18901:3 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_CREATE_ASSIGNED_FAULT'; 

--changeSet MX-18901:4 stripComments:false
DELETE FROM utl_config_parm t where t.parm_name = 'ACTION_CREATE_ASSIGNED_FAULT';

--changeSet MX-18901:5 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_CREATE_FAULT_AGAINST_CHECK_OR_WO', 'SECURED_RESOURCE','Permission to create a fault against a check or WO in WebMaintenix','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Faults', '7.5',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_CREATE_FAULT_AGAINST_CHECK_OR_WO' );            

--changeSet MX-18901:6 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_CREATE_FAULT_AGAINST_TASK', 'SECURED_RESOURCE','Permission to create a fault against a task in WebMaintenix','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Faults', '7.5',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_CREATE_FAULT_AGAINST_TASK' );            

--changeSet MX-18901:7 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_CREATE_FAULT_AGAINST_CHECK_OR_WO', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_CREATE_FAULT_AGAINST_CHECK_OR_WO' );            

--changeSet MX-18901:8 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_CREATE_FAULT_AGAINST_TASK', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_CREATE_FAULT_AGAINST_TASK' );             