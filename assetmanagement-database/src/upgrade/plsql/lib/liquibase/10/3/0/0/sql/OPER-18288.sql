--liquibase formatted sql

--changeSet OPER-18288:1 stripComments:false
INSERT INTO
   utl_config_parm
   (
      parm_value,
      parm_name,
      parm_type,
      parm_desc,
      config_type,
      allow_value_desc,
      default_value,
      mand_config_bool,
      category,
      modified_in,
      utl_id
   )
   SELECT
      'FALSE',
      'ENABLE_MULTIPLE_SKILLS_SIGN_OFF_ON_SAME_STEP',
      'LOGIC',
      'Allows user with multiple skills to sign off on more than one skill on the same step under multiple labor rows.',
      'GLOBAL',
      'TRUE/FALSE',
      'FALSE',
      0,
      'Maint - Tasks',
      '8.3',
      0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ENABLE_MULTIPLE_SKILLS_SIGN_OFF_ON_SAME_STEP' );