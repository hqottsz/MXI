--liquibase formatted sql

--changeSet OPER-26050:1 stripComments:false
UPDATE UTL_CONFIG_PARM SET DEFAULT_VALUE = 'TRUE', ALLOW_VALUE_DESC = 'TRUE/FALSE', CATEGORY = 'Core Logic'  WHERE PARM_NAME='ENCRYPT_PARAMETERS';