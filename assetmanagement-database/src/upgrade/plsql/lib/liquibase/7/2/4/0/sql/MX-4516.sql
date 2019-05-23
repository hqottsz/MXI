--liquibase formatted sql


--changeSet MX-4516:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'ENFORCE_OPEN_CLOSE_PANEL_COMPLETE_TASK_ORDER','LOGIC', 'FALSE', 0,'Whether or not ensure the order of Open Panel -> Do Task -> Close panel.', 'GLOBAL', 'BOOLEAN', '0' , 1, 'Maint', '0803', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ENFORCE_OPEN_CLOSE_PANEL_COMPLETE_TASK_ORDER' );     

--changeSet MX-4516:2 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ALLOW_CHECK_MPC_PANELS_ON_COMPLETE_JIC' and t.db_type_cd = 'OPER';     

--changeSet MX-4516:3 stripComments:false
DELETE FROM utl_user_parm WHERE PARM_NAME = 'ALLOW_CHECK_MPC_PANELS_ON_COMPLETE_JIC';

--changeSet MX-4516:4 stripComments:false
DELETE FROM utl_role_parm WHERE PARM_NAME = 'ALLOW_CHECK_MPC_PANELS_ON_COMPLETE_JIC';

--changeSet MX-4516:5 stripComments:false
DELETE FROM utl_config_parm WHERE PARM_NAME = 'ALLOW_CHECK_MPC_PANELS_ON_COMPLETE_JIC';