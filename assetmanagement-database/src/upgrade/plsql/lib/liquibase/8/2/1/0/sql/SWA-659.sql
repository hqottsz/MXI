--liquibase formatted sql


--changeSet SWA-659:1 stripComments:false
/*
*Insert integration alert for Spares Order Message is not published
*
*/
INSERT INTO 
   utl_alert_type 
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   )
   SELECT 
      512, 'integration.alert.SPARES_ORDER_MESSAGE_IS_NOT_PUBLISHED_name', 'integration.alert.SPARES_ORDER_MESSAGE_IS_NOT_PUBLISHED_description', 'ROLE', null, 'PO', 'integration.alert.SPARES_ORDER_MESSAGE_IS_NOT_PUBLISHED_message', 1, 0, null, 1, 0 
   FROM 
      dual
   WHERE 
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 512 ); 