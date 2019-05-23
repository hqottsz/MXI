--liquibase formatted sql


--changeSet SWA-885:1 stripComments:false
INSERT INTO 
   utl_alert_type 
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   )
   SELECT 
      602, 'integration.alert.MESSAGE_IS_NOT_DELIVERED_name', 'integration.alert.MESSAGE_IS_NOT_DELIVERED_description', 'ROLE', null, 'PO', 'integration.alert.MESSAGE_IS_NOT_DELIVERED_message', 1, 0, null, 1, 0
   FROM 
      dual
   WHERE 
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 602 ); 