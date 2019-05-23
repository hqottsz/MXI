--liquibase formatted sql


--changeSet MX-18883:1 stripComments:false
-- REMOTE_RESERVATION_SHIPMENT_FAILED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 217, 'core.alert.REMOTE_RESERVATION_SHIPMENT_FAILED_name', 'core.alert.REMOTE_RESERVATION_SHIPMENT_FAILED_description', 'ROLE', null, 'SHIPMENT', 'core.alert.REMOTE_RESERVATION_SHIPMENT_FAILED_message', 1, 0, null, 1, 0
FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_alert_type WHERE alert_type_id = 217);