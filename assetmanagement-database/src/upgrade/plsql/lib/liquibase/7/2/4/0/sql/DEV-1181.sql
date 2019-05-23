--liquibase formatted sql


--changeSet DEV-1181:1 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- migration script for Spec2K spare invoice adapter alerts
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 223, 'core.alert.SPARE_INVOICE_VENDOR_CD_INVALID_name', 'core.alert.SPARE_INVOICE_VENDOR_CD_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_VENDOR_CD_INVALID_message', 1, 0, null, 1, 0 
FROM dual
WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 223 ); 

--changeSet DEV-1181:2 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT  224, 'core.alert.SPARE_INVOICE_ORDER_N0_INVALID_name', 'core.alert.SPARE_INVOICE_ORDER_N0_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_ORDER_NO_INVALID_message', 1, 0, null, 1, 0 
FROM dual
WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 224 ); 

--changeSet DEV-1181:3 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 233, 'core.alert.SPARE_INVOICE_INVOICE_TYPE_CD_INVALID_name', 'core.alert.SPARE_INVOICE_INVOICE_TYPE_CD_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_INVOICE_TYPE_CD_INVALID_message', 1, 0, null, 1, 0
FROM dual
WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 233 ); 

--changeSet DEV-1181:4 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 226, 'core.alert.SPARE_INVOICE_UOM_INVALID_name', 'core.alert.SPARE_INVOICE_UOM_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_UOM_INVALID_message', 1, 0, null, 1, 0 
FROM dual
WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 226 ); 

--changeSet DEV-1181:5 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 227, 'core.alert.SPARE_INVOICE_CURRENCY_CD_INVALID_name', 'core.alert.SPARE_INVOICE_CURRENCY_CD_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_CURRENCY_CD_INVALID_message', 1, 0, null, 1, 0 
FROM dual
WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 227 ); 

--changeSet DEV-1181:6 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 228, 'core.alert.SPARE_INVOICE_INCONSISTENT_UOM_name', 'core.alert.SPARE_INVOICE_INCONSISTENT_UOM_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_INCONSISTENT_UOM_message', 1, 0, null, 1, 0 
FROM dual
WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 228 ); 

--changeSet DEV-1181:7 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 229, 'core.alert.SPARE_INVOICE_INCONSISTENT_CURRENCY_CD_name', 'core.alert.SPARE_INVOICE_INCONSISTENT_CURRENCY_CD_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_INCONSISTENT_CURRENCY_CD_message', 1, 0, null, 1, 0 
FROM dual
WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 229 );  

--changeSet DEV-1181:8 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 230, 'core.alert.SPARE_INVOICE_OTHER_CHARGE_CD_INVALID_name', 'core.alert.SPARE_INVOICE_OTHER_CHARGE_CD_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_OTHER_CHARGE_CD_INVALID_message', 1, 0, null, 1, 0 
FROM dual
WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 230 ); 

--changeSet DEV-1181:9 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 231, 'core.alert.SPARE_INVOICE_ORDER_STATUS_INVALID_name', 'core.alert.SPARE_INVOICE_ORDER_STATUS_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_ORDER_STATUS_INVALID_message', 1, 0, null, 1, 0 
FROM dual
WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 231 ); 

--changeSet DEV-1181:10 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 232, 'core.alert.SPARE_INVOICE_ORDER_LINE_N0_INVALID_name', 'core.alert.SPARE_INVOICE_ORDER_LINE_N0_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_ORDER_LINE_NO_INVALID_message', 1, 0, null, 1, 0
FROM dual
WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 232 ); 