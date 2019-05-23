--liquibase formatted sql


--changeSet MX-22124:1 stripComments:false
UPDATE
   db_type_config_parm
SET
   db_type_cd = 'OPER'
WHERE
   parm_name = 'ACTION_CANCEL_BL_TASK'
   AND
   NOT EXISTS(
      SELECT 1 FROM db_type_config_parm
      WHERE
      parm_name = 'ACTION_CANCEL_BL_TASK'
      AND
      db_type_cd = 'OPER'
   );