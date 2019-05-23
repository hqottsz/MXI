--liquibase formatted sql
 

--changeSet MX-19838:1 stripComments:false
-- update default_value to false for all SECURED_RESOURCE config parm
UPDATE utl_config_parm SET  default_value = 'FALSE'  WHERE parm_type = 'SECURED_RESOURCE' AND  upper(default_value) = 'TRUE';

--changeSet MX-19838:2 stripComments:false
UPDATE utl_action_config_parm SET  default_value = 'FALSE' WHERE  upper(default_value) = 'TRUE';