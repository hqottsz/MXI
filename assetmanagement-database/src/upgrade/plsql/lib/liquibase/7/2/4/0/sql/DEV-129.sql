--liquibase formatted sql


--changeSet DEV-129:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'AWTD', 'DEFAULT_EDIT_REASON', 'MXWEB','Default Reason for edit estimated end date of a check.','GLOBAL', 'valid event_stage_cd for Edit Estimated End Date of a check', 'AWTD', 1, 'MXWEB', '7.1', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'DEFAULT_EDIT_REASON' );            

--changeSet DEV-129:2 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_EDIT_ESTIMATED_END_DATE', 'SECURED_RESOURCE','Permission to edit estimated end date of work package','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Work Packages', '7.1.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_EDIT_ESTIMATED_END_DATE' );                 