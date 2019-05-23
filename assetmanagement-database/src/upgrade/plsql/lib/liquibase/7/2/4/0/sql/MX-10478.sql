--liquibase formatted sql


--changeSet MX-10478:1 stripComments:false
UPDATE
   utl_config_parm
SET
   utl_config_parm.modified_in = '6.4.2',
   utl_config_parm.parm_desc = 'The maximum number of rows that will be shown on the EditIventorySubItems page before showing a warning'
WHERE
   utl_config_parm.parm_name = 'MAXIMUM_EDITABLE_SUBITEMS';  