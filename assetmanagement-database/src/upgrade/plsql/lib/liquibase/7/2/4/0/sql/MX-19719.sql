--liquibase formatted sql


--changeSet MX-19719:1 stripComments:false
UPDATE utl_config_parm t SET t.default_value = 100, t.allow_value_desc = 'Number' WHERE t.parm_name = 'DEFAULT_USAGE_DEADLINE_INTERVAL' AND NOT (to_char(t.default_value) = '100' AND t.allow_value_desc = 'Number');

--changeSet MX-19719:2 stripComments:false
UPDATE utl_config_parm t SET t.default_value = 4 WHERE t.parm_name = 'MO_FLAG_THRESHOLD' AND NOT to_char(t.default_value) = '4';

--changeSet MX-19719:3 stripComments:false
UPDATE utl_config_parm t SET t.default_value = 0 WHERE t.parm_name = 'CAPACITY_OFFSET_HOURS' AND NOT to_char(t.default_value) = '0';