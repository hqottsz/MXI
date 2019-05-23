--liquibase formatted sql


--changeSet MX-19045:1 stripComments:false
UPDATE UTL_CONFIG_PARM
 SET PARM_DESC = 'Controls both the default automation duration, and the default number of years over which the automation logic should be run on a long range plan.'
WHERE PARM_NAME = 'LRP_DEFAULT_AUTOMATION_DURATION';