--liquibase formatted sql


--changeSet OPER-2701:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_CREATE_AND_STORE_INVENTORY', 
      'Permission to bulk create Inventory items.',
      'TRUE/FALSE',    
      'FALSE',  
      1, 
      'Supply - Inventory', 
      '8.2', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );
END;
/

--changeSet OPER-2701:2 stripComments:false
-- Add 0 level data to UTL_ALERT_TYPE
-- INVENTORY_CREATED_IN_QUARANTINE alert type
INSERT INTO 
   utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT
   244, 'core.alert.INVENTORY_CREATED_IN_QUARANTINE_name', 'core.alert.INVENTORY_CREATED_IN_QUARANTINE_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVENTORY_CREATED_IN_QUARANTINE_message', 1, 0, null, 1, 0
FROM 
   DUAL
WHERE 
   NOT EXISTS (SELECT 1 FROM utl_alert_type WHERE alert_type_id = 244);      

--changeSet OPER-2701:3 stripComments:false
-- Add 0 level data to REF_EVENT_STATUS
-- ICRQUAR status
INSERT INTO 
   ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT
   0, 'ICRQUAR', 0, 'ICR', 0, 80, 'Quarantine', 'Quarantine', 'QUAR', '200', '0',  0, TO_DATE('2015-03-15', 'YYYY-MM-DD'), TO_DATE('2015-03-15', 'YYYY-MM-DD'), 100, 'MXI'
FROM 
   DUAL
WHERE 
   NOT EXISTS (SELECT 1 FROM ref_event_status WHERE event_status_db_id = 0 and event_status_cd = 'ICRQUAR');