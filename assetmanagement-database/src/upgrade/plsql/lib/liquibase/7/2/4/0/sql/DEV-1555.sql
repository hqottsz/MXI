--liquibase formatted sql


--changeSet DEV-1555:1 stripComments:false
UPDATE
   utl_config_parm
SET
   parm_desc = 'Minimum default value in hours to shift event start date by.',
   modified_in = '8.0'
WHERE 
   parm_name = 'LRP_AUTOMATION_SHIFT_VALUE';