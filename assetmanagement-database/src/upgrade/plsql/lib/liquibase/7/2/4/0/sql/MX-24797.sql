--liquibase formatted sql


--changeSet MX-24797:1 stripComments:false
INSERT INTO
 utl_alert_type
 (
    alert_type_id, alert_name, alert_ldesc, notify_cd, category, message, key_bool, priority, active_bool, utl_id
 )
 SELECT 
    225, 
    'core.alert.EXPIRED_INVENTORY_UNRESERVRD_name', 
    'core.alert.EXPIRED_INVENTORY_UNRESERVRD_description', 
    'ROLE', 
    'INVENTORY', 
    'core.alert.EXPIRED_INVENTORY_UNRESERVRD_message', 
    1, 
    0, 
    1, 
    0 
 FROM
    dual
 WHERE
    NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 225 );