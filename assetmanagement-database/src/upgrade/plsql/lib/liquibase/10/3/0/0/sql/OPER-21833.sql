--liquibase formatted sql

--changeSet OPER-21833:1 stripComments:false
INSERT INTO
   utl_action_config_parm
   (
      parm_name, parm_value, encrypt_bool, parm_desc, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id
   )
   SELECT
      'ACTION_PACKAGE_OPEN_FAULTS', 'FALSE',  0, 'Permission to package open faults.' , 'TRUE/FALSE', 'FALSE', 1, 'Maint - Tasks', '8.3', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'ACTION_PACKAGE_OPEN_FAULTS');
