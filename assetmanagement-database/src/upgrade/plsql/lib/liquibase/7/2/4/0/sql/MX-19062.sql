--liquibase formatted sql


--changeSet MX-19062:1 stripComments:false
-- Conditional insert
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
   SELECT 215, 'core.alert.TASK_DEFN_OBSOLETE_name', 'core.alert.TASK_DEFN_OBSOLETE_description', 'ROLE', null, 'TASK_DEFN', 'core.alert.TASK_DEFN_OBSOLETE_message', 1, 0, null, 1, 0
   FROM dual WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 215 );