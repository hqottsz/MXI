--liquibase formatted sql


--changeSet OPER-3784:1 stripComments:false
-- add PART_NOT_AVAILABLE alert
INSERT INTO 
   utl_alert_type
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   ) 
   SELECT 250, 'core.alert.PART_NOT_AVAILABLE_name', 'core.alert.PART_NOT_AVAILABLE_description', 'ROLE', null, 'REQ', 'core.alert.PART_NOT_AVAILABLE_message', 1, 0, null, 1, 0 
   FROM 
      dual 
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 250 );