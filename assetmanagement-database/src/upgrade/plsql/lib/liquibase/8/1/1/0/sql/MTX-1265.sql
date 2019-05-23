--liquibase formatted sql


--changeSet MTX-1265:1 stripComments:false
-- PART_REQUEST_DETAILS_CHANGE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT  244, 'core.alert.PART_REQUEST_WHERE_NEEDED_LOCATION_CHANGE_name', 'core.alert.PART_REQUEST_WHERE_NEEDED_LOCATION_CHANGE_description', 'CUSTOM', 'com.mxi.mx.core.plugin.alert.req.PartRequestWhereNeededChangeHrRule', 'REQ', 'core.alert.PART_REQUEST_WHERE_NEEDED_LOCATION_CHANGE_message', 1, 0, null, 1, 0 
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 244);