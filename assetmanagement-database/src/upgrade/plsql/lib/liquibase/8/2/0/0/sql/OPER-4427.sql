--liquibase formatted sql


--changeSet OPER-4427:1 stripComments:false
-- Update the default value from 24 to 72 for PPC_PUBLISH_TASK_CREWHR_RANGE config parm
UPDATE
   utl_config_parm
SET
   parm_value = 72,
   default_value = 72
WHERE
   parm_name = 'PPC_PUBLISH_TASK_CREWHR_RANGE';