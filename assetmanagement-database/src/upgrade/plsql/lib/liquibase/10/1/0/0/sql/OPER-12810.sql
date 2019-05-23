--liquibase formatted sql

--changeSet OPER-12810:1 stripComments:false
UPDATE
   utl_config_parm
SET
   parm_desc = 'Indicates whether the HOUR usages will be shown as decimals or HH:MM format. The HH:MM format is supported on the following pages: Create/Edit Flight, Inventory Details, Work Package Details, and Task Details.',
   modified_in = '10.2.0.0'
WHERE
   parm_name = 'USAGE_HOURS_AS_DECIMAL';