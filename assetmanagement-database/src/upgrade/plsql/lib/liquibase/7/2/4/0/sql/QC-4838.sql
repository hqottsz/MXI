--liquibase formatted sql


--changeSet QC-4838:1 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 219, 'core.alert.PART_REQUEST_MESSAGE_BUILD_ERROR_name', 'core.alert.PART_REQUEST_MESSAGE_BUILD_ERROR_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.inventory.InventoryAuthorityFilterRule', 'REQ', 'core.alert.PART_REQUEST_MESSAGE_BUILD_ERROR_message', 1, 0, null, 1, 0 
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM UTL_ALERT_TYPE WHERE ALERT_TYPE_ID = 219);      