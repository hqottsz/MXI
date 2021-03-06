--liquibase formatted sql


--changeSet MX-22512:1 stripComments:false
UPDATE UTL_CONFIG_PARM
SET    DEFAULT_VALUE = 'FALSE',
       MODIFIED_IN = '8.0'
WHERE  PARM_NAME = 'ALLOW_ACTIVATE_TASK_DEFN_W_INV_MISSING_MANUFACT_RECEIVE_DATE'
AND    PARM_TYPE = 'LOGIC';