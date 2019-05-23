--liquibase formatted sql


--changeSet MX-17105:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_ARCHIVE_MESSAGE', 'SECURED_RESOURCE','Permission to archive an Integration message.','USER', 'TRUE/FALSE', 'FALSE', 1,'Common - Integration', '6.8.x',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ARCHIVE_MESSAGE' );   

--changeSet MX-17105:2 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_RESEND_MESSAGE', 'SECURED_RESOURCE','Permission to re-send an Integration message.','USER', 'TRUE/FALSE', 'FALSE', 1,'Common - Integration', '6.8.x',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_RESEND_MESSAGE' );    

--changeSet MX-17105:3 stripComments:false
 INSERT INTO
    UTL_CONFIG_PARM
    (
       PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
    )
    SELECT 'false', 'ACTION_UNARCHIVE_MESSAGE', 'SECURED_RESOURCE','Permission to unarchive an Integration message.','USER', 'TRUE/FALSE', 'FALSE', 1,'Common - Integration', '6.8.x',0
    FROM
       dual
    WHERE
       NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_UNARCHIVE_MESSAGE' );    

--changeSet MX-17105:4 stripComments:false
 INSERT INTO
    UTL_CONFIG_PARM
    (
       PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
    )
    SELECT 'false', 'ACTION_SEND_MESSAGE', 'SECURED_RESOURCE','Permission to send an Integration message.','USER', 'TRUE/FALSE', 'FALSE', 1,'Common - Integration', '6.8.x',0
    FROM
       dual
    WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_SEND_MESSAGE' );   