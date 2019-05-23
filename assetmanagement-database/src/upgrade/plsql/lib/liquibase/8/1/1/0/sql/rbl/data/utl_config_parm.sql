--liquibase formatted sql


--changeSet utl_config_parm:1 stripComments:false
-- fault UCL deviation
INSERT INTO utl_config_parm ( 
   parm_name, 
   parm_type, 
   parm_value, 
   encrypt_bool, 
   parm_desc, 
   config_type, 
   default_value, 
   allow_value_desc, 
   mand_config_bool, 
   category, 
   modified_in, 
   repl_approved, 
   utl_id
) 
SELECT
   'OPR_RBL_FAULT_UCL_DEV',
   'RELIABILITY',
   2,
   0,
   'Pirep/Marep UCL deviation',
   'USER',
   2,
   NULL,
   0,
   'RELIABILITY',
   0,
   0,
   0
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   utl_config_parm
                WHERE
                   parm_name = 'OPR_RBL_FAULT_UCL_DEV' AND
                   Parm_type = 'RELIABILITY'
              );                            

--changeSet utl_config_parm:2 stripComments:false
-- component removal UCL deviation
INSERT INTO utl_config_parm ( 
   parm_name, 
   parm_type, 
   parm_value, 
   encrypt_bool, 
   parm_desc, 
   config_type, 
   default_value, 
   allow_value_desc, 
   mand_config_bool, 
   category, 
   modified_in, 
   repl_approved, 
   utl_id
) 
SELECT
   'OPR_RBL_COMP_RMVL_UCL_DEV',
   'RELIABILITY',
   2,
   0,
   'Component Removal UCL deviation',
   'USER',
   2,
   NULL,
   0,
   'RELIABILITY',
   0,
   0,
   0
FROM 
   dual
WHERE   
   NOT EXISTS (
                SELECT
                   1
                FROM 
                   utl_config_parm
                WHERE
                   parm_name = 'OPR_RBL_COMP_RMVL_UCL_DEV' AND
                   Parm_type = 'RELIABILITY'
              );              