--liquibase formatted sql


--changeSet OPER-3631:1 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRERROR', 0, 'PR', 0, 01, 'Part request is in error.', 'Part request is in error', 'ERROR', '10', '0',  0, TO_DATE('2015-SEP-21', 'YYYY-MM-DD'), TO_DATE('2015-SEP-21', 'YYYY-MM-DD'), 100, 'MXI');

--changeSet OPER-3631:2 stripComments:false
-- add PART_REQUEST_ISSUE_LOCKED alert
INSERT INTO 
   utl_alert_type
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   ) 
   SELECT 251, 'core.alert.PART_REQUEST_ISSUE_LOCKED_name', 'core.alert.PART_REQUEST_ISSUE_LOCKED_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_ISSUE_LOCKED_message', 1, 0, null, 1, 0 
   FROM 
      dual 
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 251 );

--changeSet OPER-3631:3 stripComments:false
-- add PART_REQUEST_ISSUE_SCRAPPED alert
INSERT INTO 
   utl_alert_type
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   ) 
   SELECT 252, 'core.alert.PART_REQUEST_ISSUE_SCRAPPED_name', 'core.alert.PART_REQUEST_ISSUE_SCRAPPED_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_ISSUE_SCRAPPED_message', 1, 0, null, 1, 0 
   FROM 
      dual 
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 252 );