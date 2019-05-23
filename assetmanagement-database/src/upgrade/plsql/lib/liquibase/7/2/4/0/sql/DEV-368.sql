--liquibase formatted sql


--changeSet DEV-368:1 stripComments:false
DELETE FROM DB_TYPE_CONFIG_PARM
  WHERE PARM_NAME = 'LRP_VISIT_ADDITIONAL_EFFORT_VALUE' AND DB_TYPE_CD = 'OPER';  

--changeSet DEV-368:2 stripComments:false
DELETE FROM   UTL_CONFIG_PARM
  WHERE PARM_NAME = 'LRP_VISIT_ADDITIONAL_EFFORT_VALUE';