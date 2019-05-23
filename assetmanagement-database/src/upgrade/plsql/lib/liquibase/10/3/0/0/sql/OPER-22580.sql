--liquibase formatted sql

--changeSet OPER-22580:1 stripComments:false
INSERT INTO 
   utl_action_config_parm 
   (
      parm_value, 
      parm_name, 
      parm_desc, 
      allow_value_desc, 
      default_value, 
      mand_config_bool, 
      category, 
      modified_in, 
      utl_id
   )
   SELECT
      'FALSE', 
      'ACTION_MANAGE_QUICK_TEXT', 
      'Permission to manage quick text entries.',
      'TRUE/FALSE', 
      'FALSE', 
      1, 
      'Admin - Quick Text', 
      '8.3', 
      0
   FROM
      dual
   WHERE
      NOT EXISTS (SELECT 1 FROM utl_action_config_parm WHERE parm_name = 'ACTION_MANAGE_QUICK_TEXT');
