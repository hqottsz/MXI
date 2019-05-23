--liquibase formatted sql

--changeSet OPER-14625:1 stripComments:false 
-- add new AeroBuy invoice alert types
INSERT INTO 
  utl_alert_type
  (
    alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id
  )
  SELECT
    266, 'integration.alert.AEROBUY_INV_FATAL_ERROR_name', 'integration.alert.AEROBUY_INV_FATAL_ERROR_description', 'ROLE', null, 'INVOICE', 'integration.alert.AEROBUY_INV_FATAL_ERROR_message', 1, 0, null, 1, 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 266 );

INSERT INTO 
  utl_alert_type
  (
    alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id
  )
  SELECT
    267, 'integration.alert.AEROBUY_INV_WARN_name', 'integration.alert.AEROBUY_INV_WARN_description', 'ROLE', null, 'INVOICE', 'integration.alert.AEROBUY_INV_WARN_message', 1, 0, null, 1, 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 267 );

INSERT INTO 
  utl_alert_type
  (
    alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id
  )
  SELECT
     268, 'integration.alert.AEROBUY_INV_INTERNAL_ERROR_name', 'integration.alert.AEROBUY_INV_INTERNAL_ERROR_description', 'ROLE', null, 'INVOICE', 'integration.alert.AEROBUY_INV_INTERNAL_ERROR_message', 1, 0, null, 1, 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 268 );
