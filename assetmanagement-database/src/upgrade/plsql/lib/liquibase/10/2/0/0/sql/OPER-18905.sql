--liquibase formatted sql
--changeSet OPER-18905:1 stripComments:false
-- Remove the Visible Crews
DELETE FROM
   utl_config_parm 
WHERE
   utl_config_parm.parm_name = 'sVisibleCrews';
         
--changeSet OPER-18905:2 stripComments:false\
-- Remove the Visible Tasks
DELETE FROM
   utl_config_parm 
WHERE
   utl_config_parm.parm_name = 'sVisibleTasks';