--liquibase formatted sql


--changeSet MX-27734:1 stripComments:false
-- Add ORDER_BELOW_MINIMUM_VENDOR_PART_QUANTITY alert
INSERT INTO
   utl_alert_type
   (
     alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id
   )
   SELECT 240, 'core.alert.ORDER_BELOW_MINIMUM_VENDOR_PART_QUANTITY_name', 'core.alert.ORDER_BELOW_MINIMUM_VENDOR_PART_QUANTITY_description', 'ROLE', null, 'PO',
          'core.alert.ORDER_BELOW_MINIMUM_VENDOR_PART_QUANTITY_message', 1, 0, null, 1, 0
   FROM
     dual
   WHERE
     NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 240 );