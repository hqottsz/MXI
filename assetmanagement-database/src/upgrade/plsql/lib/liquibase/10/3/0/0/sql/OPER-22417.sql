--liquibase formatted sql

--changeSet OPER-22417:1 stripComments:false
UPDATE utl_config_parm 
SET
   parm_desc = 'Checks if there are existing serial numbers that match the one being created. Special characters and spaces are ignored and only alphanumeric characters (a-z, regardless of case and 0-9) are considered for the comparison.'
WHERE 
   parm_name = 'CHECK_DUPLICATE_SERIAL_NO';
