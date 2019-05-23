--liquibase formatted sql


--changeSet MTX-749:1 stripComments:false
-- Insert the new parameter to real roster enabling configuration.
INSERT INTO
   UTL_CONFIG_PARM
      ( PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
   SELECT 
       'PPC_REAL_ROSTER_ENABLED', 'PRODUCTION_PLANNING_CONTROL', 'TRUE', 'Real Roster enabled flag', 'GLOBAL', 'TRUE', 'TRUE/FALSE', 1, 'Maint - Production Planning and Control', '8.1', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'PPC_REAL_ROSTER_ENABLED' );   

--changeSet MTX-749:2 stripComments:false
-- Updating existing PPC configuration parameters. Replacing parm_type from LOGIC to PRODUCTION_PLANNING_CONTROL.
UPDATE
   utl_config_parm
SET
   parm_type = 'PRODUCTION_PLANNING_CONTROL'
WHERE
   parm_name = 'DEFAULT_PPC_PLAN_HORIZON'
   AND
   config_type = 'GLOBAL';

--changeSet MTX-749:3 stripComments:false
UPDATE
   utl_config_parm
SET
   parm_type = 'PRODUCTION_PLANNING_CONTROL'
WHERE
   parm_name = 'PPC_PLAN_HORIZON_LIMIT'
   AND
   config_type = 'GLOBAL';

--changeSet MTX-749:4 stripComments:false
UPDATE
   utl_config_parm
SET
   parm_type = 'PRODUCTION_PLANNING_CONTROL'
WHERE
   parm_name = 'PPC_CAPACITY_BUFFER'
   AND
   config_type = 'GLOBAL';

--changeSet MTX-749:5 stripComments:false
UPDATE
   utl_config_parm
SET
   parm_type = 'PRODUCTION_PLANNING_CONTROL'
WHERE
   parm_name = 'PPC_PUBLISH_TASK_CREWHR_RANGE'
   AND
   config_type = 'GLOBAL';