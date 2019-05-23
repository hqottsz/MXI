--liquibase formatted sql


--changeSet MX-28961:1 stripComments:false
UPDATE utl_config_parm 
SET parm_value = 'FALSE', default_value = 'FALSE', parm_desc = 'Enable/disable entity hyperlink hiding. This feature should not be enabled in production.', MODIFIED_IN = '8.1-SP1'
WHERE parm_name = 'HYPERLINK_ENABLED';

--changeSet MX-28961:2 stripComments:false
UPDATE utl_config_parm 
SET parm_value = 'FALSE', default_value = 'FALSE', parm_desc = 'Enable/disable actions for entities. This feature should not be enabled in production.', MODIFIED_IN = '8.1-SP1'
WHERE parm_name = 'BUTTON_ENABLED';

--changeSet MX-28961:3 stripComments:false
UPDATE utl_config_parm 
SET parm_value = 'FALSE', default_value = 'FALSE', parm_desc = 'Enable/disable entity row filtering. This feature should not be enabled in production.', MODIFIED_IN = '8.1-SP1'
WHERE parm_name = 'ROWFILTERING_ENABLED';

--changeSet MX-28961:4 stripComments:false
UPDATE utl_config_parm 
SET parm_value = 'FALSE', default_value = 'FALSE', parm_desc = 'Enable/disable page security. This feature should not be enabled in production.', MODIFIED_IN = '8.1-SP1'
WHERE parm_name = 'PAGE_ENABLED';