--liquibase formatted sql


--changeSet MX-18899:1 stripComments:false
-- Conditional insert
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
   SELECT 218, 'core.alert.NEXT_HIGHEST_LRU_NOT_FOUND_name', 'core.alert.NEXT_HIGHEST_LRU_NOT_FOUND_description', 'ROLE', null, 'INVENTORY', 'core.alert.NEXT_HIGHEST_LRU_NOT_FOUND_message', 1, 0, null, 1, 0
   FROM dual WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 218 );