--liquibase formatted sql


--changeSet SWA-1250:1 stripComments:false
/*
* Insert integration alert for Aeroexchage Rejection Inbound Message failures
*
*/
INSERT INTO
   utl_alert_type
   (
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id
   )
   SELECT
      514, 'integration.alert.AEROEXCHANGE_REJECTION_name', 'integration.alert.AEROEXCHANGE_REJECTION_description', 'ROLE', null, 'PO', 'integration.alert.AEROEXCHANGE_REJECTION_message', 1, 0, null, 1, 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 514 );