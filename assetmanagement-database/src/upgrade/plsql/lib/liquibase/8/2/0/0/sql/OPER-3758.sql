--liquibase formatted sql


--changeSet OPER-3758:1 stripComments:false
-- add PART_REQUEST_ISSUE_INV_NOT_EXIST_OR_ARCHIVED alert
INSERT INTO 
   utl_alert_type
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   ) 
   SELECT 253, 'core.alert.PART_REQUEST_ISSUE_INV_NOT_EXIST_OR_ARCHIVED_name', 'core.alert.PART_REQUEST_ISSUE_INV_NOT_EXIST_OR_ARCHIVED_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_ISSUE_INV_NOT_EXIST_OR_ARCHIVED_message', 1, 0, null, 1, 0 
   FROM 
      dual 
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 253 );