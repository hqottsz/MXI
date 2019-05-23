--liquibase formatted sql


--changeSet MX-18387:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_REMOVE_HISTORIC_LABOUR_REQUIREMENT', 'SECURED_RESOURCE', 'Permission to remove a historic labour requirement from a task in WebMaintenix', 'USER', 'TRUE/FALSE', 'FALSE', 1, 'Maint - Tasks', '7.1-SP2', 0
FROM 
   DUAL 
WHERE 
   NOT EXISTS ( 
      SELECT 
         1
      FROM   
         utl_config_parm
      WHERE  
         parm_name = 'ACTION_REMOVE_HISTORIC_LABOUR_REQUIREMENT'
         AND
         parm_type = 'SECURED_RESOURCE'
   )
;   

--changeSet MX-18387:2 stripComments:false
-- Parameter info for deployed ops
INSERT INTO DB_TYPE_CONFIG_PARM (PARM_NAME, PARM_TYPE, DB_TYPE_CD)
SELECT 'ACTION_REMOVE_HISTORIC_LABOUR_REQUIREMENT', 'SECURED_RESOURCE', 'MASTER'
FROM 
   dual
WHERE
   NOT EXISTS ( 
      SELECT 
         1 
      FROM 
         DB_TYPE_CONFIG_PARM 
      WHERE 
         parm_name = 'ACTION_REMOVE_HISTORIC_LABOUR_REQUIREMENT'
         AND
         db_type_cd = 'MASTER'
   );

--changeSet MX-18387:3 stripComments:false
INSERT INTO DB_TYPE_CONFIG_PARM (PARM_NAME, PARM_TYPE, DB_TYPE_CD)
SELECT 'ACTION_REMOVE_HISTORIC_LABOUR_REQUIREMENT', 'SECURED_RESOURCE', 'OPER'
FROM
   dual
WHERE 
   NOT EXISTS ( 
      SELECT 
         1 
      FROM 
         DB_TYPE_CONFIG_PARM 
      WHERE 
         parm_name = 'ACTION_REMOVE_HISTORIC_LABOUR_REQUIREMENT'
         AND
         db_type_cd = 'OPER'
   );