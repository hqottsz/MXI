--liquibase formatted sql


--changeSet MX-19772:1 stripComments:false
/*
Insert a new row into utl_config_parm which parm_name = 'ACTION_APPROVE_PART'
*/
INSERT INTO utl_config_parm (parm_name, parm_type, parm_value, encrypt_bool, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id)
SELECT 'ACTION_APPROVE_PART', parm_type,parm_value,encrypt_bool,  'Permission to approve a Part Catalog entry (from BUILD)', config_type,allow_value_desc, default_value, mand_config_bool, category, '8.0', utl_id
FROM utl_config_parm
WHERE parm_name = 'ACTION_ACTIVATE_PART' AND parm_type = 'SECURED_RESOURCE'
AND NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_APPROVE_PART' AND parm_type = 'SECURED_RESOURCE');

--changeSet MX-19772:2 stripComments:false
/*
update all related tables 
*/
UPDATE UTL_ROLE_PARM 
   SET parm_name = 'ACTION_APPROVE_PART'
WHERE PARM_NAME = 'ACTION_ACTIVATE_PART';

--changeSet MX-19772:3 stripComments:false
UPDATE UTL_USER_PARM 
   SET parm_name = 'ACTION_APPROVE_PART'
WHERE PARM_NAME = 'ACTION_ACTIVATE_PART';

--changeSet MX-19772:4 stripComments:false
UPDATE UTL_TODO_BUTTON 
   SET parm_name = 'ACTION_APPROVE_PART'
WHERE PARM_NAME = 'ACTION_ACTIVATE_PART';

--changeSet MX-19772:5 stripComments:false
UPDATE DB_TYPE_CONFIG_PARM 
   SET parm_name = 'ACTION_APPROVE_PART'
WHERE PARM_NAME = 'ACTION_ACTIVATE_PART';

--changeSet MX-19772:6 stripComments:false
/*
delete the old row  with parm_name = 'ACTION_ACTIVATE_PART'
*/
DELETE FROM utl_config_parm WHERE parm_name = 'ACTION_ACTIVATE_PART'; 