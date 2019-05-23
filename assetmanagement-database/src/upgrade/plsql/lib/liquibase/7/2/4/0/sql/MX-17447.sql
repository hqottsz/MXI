--liquibase formatted sql


--changeSet MX-17447:1 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 213, 'core.alert.INVALID_OIL_CONSUMPTION_name', 'core.alert.INVALID_OIL_CONSUMPTION_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVALID_OIL_CONSUMPTION_message', 1, 0, null, 1, 0 
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM UTL_ALERT_TYPE WHERE ALERT_TYPE_ID = 213);