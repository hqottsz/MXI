--liquibase formatted sql


--changeSet MX-21959:1 stripComments:false
-- Insert UNABLE_TO_SCHED alert
INSERT INTO
   utl_alert_type
   (
     alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool,
     utl_id
   )
   SELECT 222, 'core.alert.UNABLE_TO_SCHEDULE_name', 'core.alert.UNABLE_TO_SCHEDULE_description', 'ROLE', null, 'TASK',
          'core.alert.UNABLE_TO_SCHEDULE_message', 1, 0, null, 1, 0
   FROM
     dual
   WHERE
     NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 222 );