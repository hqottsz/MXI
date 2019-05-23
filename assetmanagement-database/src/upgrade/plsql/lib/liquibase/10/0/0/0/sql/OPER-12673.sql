--liquibase formatted sql

--changeSet OPER-12673:1 stripComments:false 
UPDATE 
   utl_config_parm
SET
   parm_desc = 'Allow auto completion of open task and fault when inspecting inventory as serviceable',
   modified_in = '8.2sp3'
WHERE
   parm_name = 'ALLOW_AUTO_COMPLETION';