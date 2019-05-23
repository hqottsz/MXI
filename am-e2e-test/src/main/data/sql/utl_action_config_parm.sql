UPDATE utl_action_config_parm
SET parm_value = 'true'
WHERE parm_name IN ('API_LOCATION_PARM',
                    'API_EVENT_CONFIGURATION_PARM');
-- Enable Temporary Role Assignment feature
UPDATE utl_action_config_parm
SET parm_value = 'true'
WHERE parm_name in ('ACTION_ASSIGN_TEMPORARY_ROLE',
                    'ACTION_JSP_WEB_USER_ASSIGN_TEMPORARY_ROLE',
                    'ACTION_UNASSIGN_TEMPORARY_ROLE');