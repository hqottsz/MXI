--liquibase formatted sql

--changeSet OPER-14723:1 stripComments:false
-- add ARC_ALLOW_USAGE_UPDATE_FOR_INREP_ASSET config parameter
INSERT INTO utl_config_parm (
   parm_name, 
   parm_value, 
   parm_type, 
   encrypt_bool,
   parm_desc, 
   config_type, 
   allow_value_desc, 
   default_value,
   mand_config_bool, 
   category, 
   modified_in, 
   repl_approved,
   utl_id
)
SELECT 
   'ARC_ALLOW_USAGE_UPDATE_FOR_INREP_ASSET', 
   'FALSE', 
   'ARC', 
   0,
   'Indicator if ARC allows updating inventory usages for INREP aircraft', 
   'GLOBAL', 
   'TRUE/FALSE', 
   'FALSE', 
   1, 
   'ARC - Configuration', 
   '8.2', 
   0,
   0
FROM 
	dual 
WHERE NOT EXISTS (
   SELECT 1
     FROM utl_config_parm 
		WHERE utl_config_parm.parm_name = 'ARC_ALLOW_USAGE_UPDATE_FOR_INREP_ASSET'
); 