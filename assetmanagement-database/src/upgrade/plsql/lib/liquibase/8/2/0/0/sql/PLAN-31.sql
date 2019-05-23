--liquibase formatted sql


--changeSet PLAN-31:1 stripComments:false
-- add FAULT_NOT_RAISED_WORK_PACKAGE_NOT_EXISTS alert
INSERT INTO 
   utl_alert_type
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   ) 
   SELECT 257, 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_NOT_EXISTS_name', 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_NOT_EXISTS_description', 'ROLE', null, 'FAULT', 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_NOT_EXISTS_message', 1, 0, null, 1, 0 
   FROM 
      dual 
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 257 );

--changeSet PLAN-31:2 stripComments:false
-- add FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_CANCEL_STATE alert
INSERT INTO 
   utl_alert_type
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   ) 
   SELECT 261, 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_CANCEL_STATE_name', 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_CANCEL_STATE_description', 'ROLE', null, 'FAULT', 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_CANCEL_STATE_message', 1, 0, null, 1, 0 
   FROM 
      dual 
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 261 );

--changeSet PLAN-31:3 stripComments:false
-- add FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_COMPLETE_STATE alert
INSERT INTO 
   utl_alert_type
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   ) 
   SELECT 258, 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_COMPLETE_STATE_name', 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_COMPLETE_STATE_description', 'ROLE', null, 'FAULT', 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_COMPLETE_STATE_message', 1, 0, null, 1, 0 
   FROM 
      dual 
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 258 );

--changeSet PLAN-31:4 stripComments:false
-- add FAULT_NOT_RAISED_BARCODE_ALREADY_EXISTS alert
INSERT INTO 
   utl_alert_type
   ( 
      alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id 
   ) 
   SELECT 259, 'core.alert.FAULT_NOT_RAISED_BARCODE_ALREADY_EXISTS_name', 'core.alert.FAULT_NOT_RAISED_BARCODE_ALREADY_EXISTS_description', 'ROLE', null, 'FAULT', 'core.alert.FAULT_NOT_RAISED_BARCODE_ALREADY_EXISTS_message', 1, 0, null, 1, 0 
   FROM 
      dual 
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 259 );

--changeSet PLAN-31:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_CREATE_EVALUATED_FAULT_REQUEST', 
      'Permission to raise evaluated fault',
      'TRUE/FALSE',    
      'FALSE',  
      1, 
      'API - MAINTENANCE', 
      '8.2', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );
END;
/