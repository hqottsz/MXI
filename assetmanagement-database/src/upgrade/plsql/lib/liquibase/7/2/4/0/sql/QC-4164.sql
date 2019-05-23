--liquibase formatted sql


--changeSet QC-4164:1 stripComments:false
UPDATE utl_config_parm
  SET PARM_VALUE = 'false',
      DEFAULT_VALUE = 'FALSE'
WHERE PARM_NAME = 'ACTION_ENABLE_VIEW_ALL_VENDORS';