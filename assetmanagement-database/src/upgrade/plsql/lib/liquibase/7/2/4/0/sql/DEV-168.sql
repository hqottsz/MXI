--liquibase formatted sql


--changeSet DEV-168:1 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_CREATE_EXTRACTION_RULE' and t.db_type_cd = 'MASTER'; 

--changeSet DEV-168:2 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_CREATE_EXTRACTION_RULE'; 

--changeSet DEV-168:3 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_CREATE_EXTRACTION_RULE'; 

--changeSet DEV-168:4 stripComments:false
DELETE FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_CREATE_EXTRACTION_RULE';

--changeSet DEV-168:5 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'ACTION_CREATE_SEASONAL_EXTRACTION_RULE','SECURED_RESOURCE','true', 0,'Determines whether a user is allowed to create a seasonal extraction rule.','USER', 'TRUE/FALSE', 'TRUE', 1, 'Ops - Extraction Rules', '7.5', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'ACTION_CREATE_SEASONAL_EXTRACTION_RULE' AND PARM_TYPE = 'SECURED_RESOURCE');

--changeSet DEV-168:6 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_COPY_EXTRACTION_RULE' and t.db_type_cd = 'MASTER'; 

--changeSet DEV-168:7 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_COPY_EXTRACTION_RULE', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_COPY_EXTRACTION_RULE' and db_type_cd = 'OPER' );      

--changeSet DEV-168:8 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_CREATE_SEASONAL_EXTRACTION_RULE','OPER','SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_CREATE_SEASONAL_EXTRACTION_RULE' and db_type_cd = 'OPER' );       

--changeSet DEV-168:9 stripComments:false
 DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_CREATE_WEEKLY_EXTRACTION_RULE' and t.db_type_cd = 'MASTER';  

--changeSet DEV-168:10 stripComments:false
 INSERT INTO
    db_type_config_parm
    (
       PARM_NAME, DB_TYPE_CD, PARM_TYPE
    )
    SELECT 'ACTION_CREATE_WEEKLY_EXTRACTION_RULE', 'OPER', 'SECURED_RESOURCE'
    FROM
       dual
    WHERE
       NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_CREATE_WEEKLY_EXTRACTION_RULE' and db_type_cd = 'OPER' );        

--changeSet DEV-168:11 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_EDIT_EXTRACTION_RULE' and t.db_type_cd = 'MASTER'; 

--changeSet DEV-168:12 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_EDIT_EXTRACTION_RULE', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_EDIT_EXTRACTION_RULE' and db_type_cd = 'OPER' );       

--changeSet DEV-168:13 stripComments:false
 DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_REMOVE_EXTRACTION_RULE' and t.db_type_cd = 'MASTER';  

--changeSet DEV-168:14 stripComments:false
 INSERT INTO
    db_type_config_parm
    (
       PARM_NAME, DB_TYPE_CD, PARM_TYPE
    )
    SELECT 'ACTION_REMOVE_EXTRACTION_RULE', 'OPER', 'SECURED_RESOURCE'
    FROM
       dual
    WHERE
       NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_EXTRACTION_RULE' and db_type_cd = 'OPER' );              