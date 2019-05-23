--liquibase formatted sql


--changeSet SWA-1891:1 stripComments:false
INSERT INTO 
  utl_alert_type
  (
    alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id
  )
  SELECT
    800, 'core.alert.USAGE_RECORD_MESSAGE_ERROR_name', 'core.alert.USAGE_RECORD_MESSAGE_ERROR_description', 'ROLE', NULL, 'INVENTORY', 'core.alert.USAGE_RECORD_MESSAGE_ERROR_message', 1, 0, NULL, 1, 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 800 );