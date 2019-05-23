--liquibase formatted sql


--changeSet DEV-415:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT '0', 'sWorkscopeSearchSubAssembly', 'SESSION','Workscope search parameters..','USER', 'STRING', '0', 0, 'SESSION', '7.2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sWorkscopeSearchSubAssembly' );                

--changeSet DEV-415:2 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT '0', 'sWorkscopeSearchZone', 'SESSION','Workscope search parameters..','USER', 'STRING', '0', 0, 'SESSION', '7.2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sWorkscopeSearchZone' );         

--changeSet DEV-415:3 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT '0', 'sWorkscopeSearchLaborSkill', 'SESSION','Workscope search parameters..','USER', 'STRING', '0', 0, 'SESSION', '7.2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sWorkscopeSearchLaborSkill' );         

--changeSet DEV-415:4 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT '0', 'sWorkscopeSearchAwaitingInspection', 'SESSION','Workscope search parameters..','USER', 'STRING', '0', 0, 'SESSION', '7.2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sWorkscopeSearchAwaitingInspection' );                

--changeSet DEV-415:5 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT '0', 'sWorkscopeSearchAwaitingCertification', 'SESSION','Workscope search parameters..','USER', 'STRING', '0', 0, 'SESSION', '7.2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sWorkscopeSearchAwaitingCertification' );         

--changeSet DEV-415:6 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT '0', 'sWorkscopeSearchActiveAndInwork', 'SESSION','Workscope search parameters..','USER', 'STRING', '0', 0, 'SESSION', '7.2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sWorkscopeSearchActiveAndInwork' );               

--changeSet DEV-415:7 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT '0', 'sWorkscopeSearchZoneName', 'SESSION','Workscope search parameters..','USER', 'STRING', '0', 0, 'SESSION', '7.2', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sWorkscopeSearchZoneName' );                                                   