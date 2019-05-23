--liquibase formatted sql


--changeSet DEV-1457:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC,
      DEFAULT_VALUE,  MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID, REPL_APPROVED
   )
   SELECT 'PPC_CAPACITY_BUFFER', 30, 'LOGIC', 0, 'The number of days as a buffer for the capacity loading graph.', 'GLOBAL','NUMBER', 
		 30, 1, 'Production Planning and Control', '8.0', 0, 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'PPC_CAPACITY_BUFFER' AND parm_type = 'LOGIC' );