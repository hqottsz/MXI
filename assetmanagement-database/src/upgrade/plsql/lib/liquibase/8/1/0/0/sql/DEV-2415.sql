--liquibase formatted sql


--changeSet DEV-2415:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_RAISE_ADHOC_ALERT_REQUEST',
      'Permission to allow API raise adhoc alerts',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - SYSTEM',
      '8.1-SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet DEV-2415:2 stripComments:false
-- Add Adhoc alert
INSERT INTO
   utl_alert_type
   (
     alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id
   )
   SELECT 241, 'core.alert.ADHOC_ALERT_name', 'core.alert.ADHOC_ALERT_description', 'ROLE', null, 'SYSTEM',
          'core.alert.ADHOC_ALERT_message', 1, 0, null, 1, 0
   FROM
     dual
   WHERE
     NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 241 );