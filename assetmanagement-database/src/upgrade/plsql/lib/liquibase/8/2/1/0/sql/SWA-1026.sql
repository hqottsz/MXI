--liquibase formatted sql


--changeSet SWA-1026:1 stripComments:false
/*
*Insert integration alert for Spec2k Inbound Message failures
*
*/
INSERT INTO 
   utl_alert_type 
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   )
   SELECT 
      513, 'integration.alert.SPEC2K_INBOUND_MSG_FAILED_name', 'integration.alert.SPEC2K_INBOUND_MSG_FAILED_description', 'ROLE', null, 'PO', 'integration.alert.SPEC2K_INBOUND_MSG_FAILED_message', 1, 0, null, 1, 0 
   FROM 
      dual
   WHERE 
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 513 ); 