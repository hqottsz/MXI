--liquibase formatted sql

--changeSet OPER-15686-1:1 stripComments:false

UPDATE 
   utl_config_parm
SET 
   config_type = 'GLOBAL'
WHERE
   parm_name = 'INCLUDE_IN_WORKING_CAPACITY'
AND
   parm_type = 'LOGIC'
;
