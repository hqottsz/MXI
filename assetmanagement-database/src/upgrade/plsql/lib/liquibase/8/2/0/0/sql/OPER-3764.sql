--liquibase formatted sql


--changeSet OPER-3764:1 stripComments:false
-- add PART_REQ_ALREADY_ISSUE_DIFF alert
INSERT INTO 
   utl_alert_type
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   ) 
   SELECT 260, 'core.alert.PART_REQUEST_ISSUE_ERROR_name', 'core.alert.PART_REQUEST_ISSUE_ERROR_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_ISSUE_ERROR_message', 1, 0, null, 1, 0 
   FROM 
      dual 
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 260 );

--changeSet OPER-3764:2 stripComments:false
INSERT INTO 
   utl_alert_type
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   ) 
   SELECT 262, 'core.alert.PART_REQUEST_ISSUE_INV_ERROR_name', 'core.alert.PART_REQUEST_ISSUE_INV_ERROR_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_ISSUE_INV_ERROR_message', 1, 0, null, 1, 0 
   FROM 
      dual 
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 262 );