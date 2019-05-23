--liquibase formatted sql


--changeSet QC-5260:1 stripComments:false
UPDATE utl_config_parm
SET MODIFIED_IN = '7.5'
WHERE PARM_NAME = 'ACTION_EDIT_BLOCK_PLANNING_VALUES'
AND PARM_TYPE = 'SECURED_RESOURCE';

--changeSet QC-5260:2 stripComments:false
UPDATE utl_config_parm
SET MODIFIED_IN = '7.5'
WHERE PARM_NAME = 'ACTION_EDIT_REQ_PLANNING_VALUES'
AND PARM_TYPE = 'SECURED_RESOURCE';

--changeSet QC-5260:3 stripComments:false
UPDATE utl_config_parm
SET MODIFIED_IN = '7.5'
WHERE PARM_NAME = 'ACTION_EDIT_JIC_PLANNING_VALUES'
AND PARM_TYPE = 'SECURED_RESOURCE';

--changeSet QC-5260:4 stripComments:false
UPDATE utl_config_parm
SET MODIFIED_IN = '7.5'
WHERE PARM_NAME = 'sDeferralReferenceOperator'
AND PARM_TYPE = 'SESSION';