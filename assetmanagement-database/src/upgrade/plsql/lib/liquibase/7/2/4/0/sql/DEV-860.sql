--liquibase formatted sql
		

--changeSet DEV-860:1 stripComments:false
-- Data Changes
-- Add config mxi_integration as the default ela user in utl_config_parm
INSERT INTO utl_config_parm 
(parm_name, parm_type, parm_value, encrypt_bool, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id)
SELECT 'ELA_USERNAME', 'ELA' , 'mxintegration', 0, 'The utl_user.username of the designated ELA authorization system user', 'GLOBAL' ,'String', '',  1, 'ELA', '7.2', 0
FROM
    dual
WHERE
    NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE parm_name = 'ELA_USERNAME' AND parm_type = 'ELA');