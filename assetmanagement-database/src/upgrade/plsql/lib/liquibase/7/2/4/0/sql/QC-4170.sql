--liquibase formatted sql


--changeSet QC-4170:1 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_TOGGLE_ENFORCE_WORKSCOPE_ORDER' and t.db_type_cd = 'MASTER'; 

--changeSet QC-4170:2 stripComments:false
DELETE FROM utl_user_parm t where t.parm_name = 'ACTION_TOGGLE_ENFORCE_WORKSCOPE_ORDER';

--changeSet QC-4170:3 stripComments:false
DELETE FROM utl_role_parm t where t.parm_name = 'ACTION_TOGGLE_ENFORCE_WORKSCOPE_ORDER';

--changeSet QC-4170:4 stripComments:false
DELETE FROM utl_config_parm t where t.parm_name = 'ACTION_TOGGLE_ENFORCE_WORKSCOPE_ORDER';

--changeSet QC-4170:5 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_ENFORCE_WORKSCOPE_ORDER_REQ' and t.db_type_cd = 'MASTER'; 

--changeSet QC-4170:6 stripComments:false
DELETE FROM utl_user_parm t where t.parm_name = 'ACTION_ENFORCE_WORKSCOPE_ORDER_REQ';

--changeSet QC-4170:7 stripComments:false
DELETE FROM utl_role_parm t where t.parm_name = 'ACTION_ENFORCE_WORKSCOPE_ORDER_REQ';

--changeSet QC-4170:8 stripComments:false
DELETE FROM utl_config_parm t where t.parm_name = 'ACTION_ENFORCE_WORKSCOPE_ORDER_REQ';

--changeSet QC-4170:9 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_ENF_WORKSCOPE_ORDER_REQ', 'SECURED_RESOURCE', 'Permission to toggle enforce workscope order.', 'USER', 'TRUE/FALSE', 'TRUE', 1,'Maint Program - Task Definitions', '7.1.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ENF_WORKSCOPE_ORDER_REQ' );                

--changeSet QC-4170:10 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_TOGGLE_ENF_WORKSCOPE_ORDER', 'SECURED_RESOURCE', 'Permission to enable/disable enforce workscope order.', 'USER', 'TRUE/FALSE', 'TRUE', 1,'Maint Program - Task Definitions', '7.1.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_TOGGLE_ENF_WORKSCOPE_ORDER' );              

--changeSet QC-4170:11 stripComments:false
 INSERT INTO
   db_type_config_parm
   (
      PARM_NAME,  PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_TOGGLE_ENF_WORKSCOPE_ORDER', 'SECURED_RESOURCE','MASTER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_TOGGLE_ENF_WORKSCOPE_ORDER' and db_type_cd = 'MASTER' );            

--changeSet QC-4170:12 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME,  PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_ENF_WORKSCOPE_ORDER_REQ', 'SECURED_RESOURCE','MASTER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ENF_WORKSCOPE_ORDER_REQ' and db_type_cd = 'MASTER' );                             