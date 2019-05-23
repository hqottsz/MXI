--liquibase formatted sql


--changeSet DEV-1280:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM 
     (
       PARM_VALUE, 
       PARM_NAME, 
       PARM_TYPE, 
       PARM_DESC, 
       CONFIG_TYPE, 
       ALLOW_VALUE_DESC, 
       DEFAULT_VALUE, 
       MAND_CONFIG_BOOL, 
       CATEGORY, 
       MODIFIED_IN, 
       UTL_ID
     )
SELECT 'false', 
       'ACTION_ALLOW_OVERLAPPING_PART_PRICE_CONTRACTS', 
       'LOGIC',
       'Indicates if we will allow or disallow the creation/editing of vendor part prices with a contract ref whose effective range overlapps with an existing contract price.',
       'GLOBAL',
       'TRUE/FALSE',
       'FALSE',
       0,
       'Parts - Part Numbers',
       '8.0',
       0
FROM
       dual
WHERE
       NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ALLOW_OVERLAPPING_PART_PRICE_CONTRACTS' AND parm_type = 'LOGIC' );