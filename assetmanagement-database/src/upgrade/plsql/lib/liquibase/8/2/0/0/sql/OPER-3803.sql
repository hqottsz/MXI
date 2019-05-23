--liquibase formatted sql


--changeSet OPER-3803:1 stripComments:false
-- add PART_REQ_ALREADY_ISSUE_DIFF alert
INSERT INTO 
   utl_alert_type
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   ) 
   SELECT 256, 'core.alert.PART_REQUEST_ALREADY_ISSUED_DIFF_name', 'core.alert.PART_REQUEST_ALREADY_ISSUED_DIFF_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_ALREADY_ISSUED_DIFF_message', 1, 0, null, 1, 0 
   FROM 
      dual 
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 256 );