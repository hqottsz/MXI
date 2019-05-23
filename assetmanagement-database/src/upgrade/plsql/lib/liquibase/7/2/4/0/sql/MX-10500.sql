--liquibase formatted sql


--changeSet MX-10500:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT null, 'sFaultEvalWorkPackage', 'SESSION','Fault Evaluation Tab search parameters.','USER', '', '', 0, 'SESSION', '6.6.9', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sFaultEvalWorkPackage' );           

--changeSet MX-10500:2 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT null, 'sFaultEvalZone', 'SESSION','Fault Evaluation Tab search parameters.','USER',  '', '', 0, 'SESSION', '6.6.9', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sFaultEvalZone' );   