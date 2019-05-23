--liquibase formatted sql


--changeSet MTX-1879:1 stripComments:false
-- Add RECEIVED_SIMILAR_DUPLICATE_SERIAL_NO alert
INSERT INTO
   utl_alert_type
   (
     alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id
   )
   SELECT 249, 'core.alert.RECEIVED_SIMILAR_DUPLICATE_SERIAL_NO_name', 'core.alert.RECEIVED_SIMILAR_DUPLICATE_SERIAL_NO_description', 'ROLE', null, 'SHIPMENT',
          'core.alert.RECEIVED_SIMILAR_DUPLICATE_SERIAL_NO_message', 1, 0, null, 0, 0
   FROM
     dual
   WHERE
     NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 249 );