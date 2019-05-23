--liquibase formatted sql


--changeSet SWA-546:1 stripComments:false
/**************************************************************************************
* 
* SWA-546: Add vendor alert types into database
*
***************************************************************************************/
INSERT INTO 
   utl_alert_type 
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   ) 
SELECT 
   263, 'core.alert.VENDOR_CREATED_name', 'core.alert.VENDOR_CREATED_description', 'ROLE', null , 'VENDOR', 'core.alert.VENDOR_CREATED_message', 1, 0, null, 1, 0
FROM
   dual
WHERE 
   NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 263 );   

--changeSet SWA-546:2 stripComments:false
INSERT INTO 
   utl_alert_type 
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   ) 
SELECT 
   264, 'core.alert.VENDOR_UPDATED_name', 'core.alert.VENDOR_UPDATED_description', 'ROLE', null , 'VENDOR', 'core.alert.VENDOR_UPDATED_message', 1, 0, null, 1, 0
FROM
   dual
WHERE 
   NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 264  );      

--changeSet SWA-546:3 stripComments:false
INSERT INTO 
   utl_alert_type 
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   ) 
SELECT 
   265, 'core.alert.VENDOR_UNAPPROVED_name', 'core.alert.VENDOR_UNAPPROVED_description', 'ROLE', null , 'VENDOR', 'core.alert.VENDOR_UNAPPROVED_message', 1, 0, null, 1, 0
FROM
   dual
WHERE 
   NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 265 );