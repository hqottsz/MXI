--liquibase formatted sql


--changeSet DEV-1006:1 stripComments:false
-- Data Changes
-- Add 0 level data to UTL_ALERT_TYPE
INSERT INTO utl_alert_type (alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id)
SELECT 170, 'core.alert.FLIGHT_NOT_FOUND_name', 'core.alert.FLIGHT_NOT_FOUND_description', 'ROLE', null, 'LOGBOOK', 'core.alert.FLIGHT_NOT_FOUND_message', 1, 0, null, 1, 0
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_alert_type WHERE alert_type_id = 170);

--changeSet DEV-1006:2 stripComments:false
INSERT INTO utl_alert_type (alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id)
SELECT 172, 'core.alert.INVALID_LOGBOOK_PART_REQUIREMENT_name', 'core.alert.INVALID_LOGBOOK_PART_REQUIREMENT_description', 'ROLE', null, 'LOGBOOK', 'core.alert.INVALID_LOGBOOK_PART_REQUIREMENT_message', 1, 0, null, 1, 0
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_alert_type WHERE alert_type_id = 172);

--changeSet DEV-1006:3 stripComments:false
INSERT INTO utl_alert_type (alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id)
SELECT 173, 'core.alert.REPETITIVE_TASK_name', 'core.alert.REPETITIVE_TASK_description', 'ROLE', null, 'LOGBOOK', 'core.alert.REPETITIVE_TASK_message', 1, 0, null, 1, 0
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_alert_type WHERE alert_type_id = 173);

--changeSet DEV-1006:4 stripComments:false
INSERT INTO utl_alert_type (alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id)
SELECT 174, 'core.alert.CANCEL_REPETITIVE_TASK_name', 'core.alert.CANCEL_REPETITIVE_TASK_description', 'ROLE', null, 'LOGBOOK', 'core.alert.CANCEL_REPETITIVE_TASK_message', 1, 0, null, 1, 0
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM utl_alert_type WHERE alert_type_id = 174);