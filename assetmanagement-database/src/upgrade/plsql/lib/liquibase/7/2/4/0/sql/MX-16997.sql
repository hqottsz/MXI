--liquibase formatted sql


--changeSet MX-16997:1 stripComments:false
UPDATE DB_TYPE_CONFIG_PARM 
SET    DB_TYPE_CD = 'OPER'
WHERE  PARM_NAME = 'ACTION_ASSIGN_FAILURE_EFFECT'
AND    PARM_TYPE = 'SECURED_RESOURCE'
AND    DB_TYPE_CD = 'MASTER';

--changeSet MX-16997:2 stripComments:false
UPDATE DB_TYPE_CONFIG_PARM 
SET    DB_TYPE_CD = 'OPER'
WHERE  PARM_NAME = 'ACTION_UNASSIGN_FAILURE_EFFECT'
AND    PARM_TYPE = 'SECURED_RESOURCE'
AND    DB_TYPE_CD = 'MASTER';