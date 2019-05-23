--liquibase formatted sql
--changeSet 0utl_alert_type:269 stripComments:false
INSERT INTO
   utl_alert_type
   (
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority,  active_bool, utl_id
   )
SELECT
     269, 'core.alert.INVENTORY_NOT_REINDUCTED_name', 'core.alert.INVENTORY_NOT_REINDUCTED_description', 'ROLE', null, 'SHIPMENT', 'core.alert.INVENTORY_NOT_REINDUCTED_message', 1, 0, 1, 0
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 269 );