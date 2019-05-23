--liquibase formatted sql


--changeSet OPER-8565:1 stripComments:false
--Add INVENTORY_INSPECTED_AS_UNSERVICEABLE alert type
INSERT INTO utl_alert_type 
  (alert_type_id, alert_name, alert_ldesc, notify_cd, category, message, key_bool, priority, active_bool, utl_id)
SELECT
   175, 'core.alert.INVENTORY_INSPECTED_AS_UNSERVICEABLE_name', 'core.alert.INVENTORY_INSPECTED_AS_UNSERVICEABLE_description', 'ROLE', 'INVENTORY', 'core.alert.INVENTORY_INSPECTED_AS_UNSERVICEABLE_message', 1, 0, 1, 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 175 ); 

--changeSet OPER-8565:2 stripComments:false
--update the alter name,desc and message for the common use for inspect as serviceable and unserviceable
UPDATE
   utl_alert_type
SET
   alert_name = 'core.alert.INVENTORY_NOT_INSPECTED_name',
   alert_ldesc = 'core.alert.INVENTORY_NOT_INSPECTED_description',
   message = 'core.alert.INVENTORY_NOT_INSPECTED_message'
WHERE
   alert_type_id = 152;