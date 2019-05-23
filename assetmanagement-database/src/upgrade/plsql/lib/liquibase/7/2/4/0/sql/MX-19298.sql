--liquibase formatted sql


--changeSet MX-19298:1 stripComments:false
-- Alert Type For Alert "Unable to Schedule Task from Birth" 
INSERT INTO utl_alert_type (alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id)
SELECT 216, 'core.alert.UNABLE_TO_SCHED_FROM_EFFECTIVE_DATE_name', 'core.alert.UNABLE_TO_SCHED_FROM_EFFECTIVE_DATE_description','ROLE', null, 'TASK', 'core.alert.UNABLE_TO_SCHED_FROM_EFFECTIVE_DATE_message', 1, 0, null, 1, 0  FROM dual WHERE NOT EXISTS(SELECT 1 FROM utl_alert_type WHERE alert_type_id = 216);