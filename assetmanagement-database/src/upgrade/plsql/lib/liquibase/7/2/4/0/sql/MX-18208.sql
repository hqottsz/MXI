--liquibase formatted sql


--changeSet MX-18208:1 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_HIDE_DIFFERENCES', 'SECURED_RESOURCE','MASTER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_HIDE_DIFFERENCES' and db_type_cd = 'MASTER' );      

--changeSet MX-18208:2 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_HIDE_DIFFERENCES', 'SECURED_RESOURCE','OPER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_HIDE_DIFFERENCES' and db_type_cd = 'OPER' );            