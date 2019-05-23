--liquibase formatted sql


--changeSet MTX-1559:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- ARC_MESSAGE_PROCESSED_SUCCESSFULLY alert type
/*******************************************
* Add ARC Alert Types
********************************************/
BEGIN
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT  245, 'core.alert.ARC_MESSAGE_PROCESSED_SUCCESSFULLY_name', 'core.alert.ARC_MESSAGE_PROCESSED_SUCCESSFULLY_description', 'ROLE', null , 'ARC', 'core.alert.ARC_MESSAGE_PROCESSED_SUCCESSFULLY_message', 1, 0, null, 0, 0 
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 245);
END;
/

--changeSet MTX-1559:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- ARC_MESSAGE_PROCESSED_WITH_ERROR_OR_WARNINGS alert type
BEGIN
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT  246,'core.alert.ARC_MESSAGE_PROCESSED_WITH_ERROR_OR_WARNINGS_name', 'core.alert.ARC_MESSAGE_PROCESSED_WITH_ERROR_OR_WARNINGS_description', 'ROLE', null, 'ARC', 'core.alert.ARC_MESSAGE_PROCESSED_WITH_ERROR_OR_WARNINGS_message', 1, 0, null, 0, 0 
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 246);
END;
/

--changeSet MTX-1559:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- ARC_MESSAGE_ERROR alert type
BEGIN
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT  247, 'core.alert.ARC_MESSAGE_ERROR_name', 'core.alert.ARC_MESSAGE_ERROR_description', 'ROLE', null, 'ARC', 'core.alert.ARC_MESSAGE_ERROR_message', 1, 1, null, 0, 0 
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 247);
END;
/

--changeSet MTX-1559:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- ARC_MESSAGE_SCHEMA_VALIDATION_ERROR alert type
BEGIN
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT  248, 'core.alert.ARC_MESSAGE_SCHEMA_VALIDATION_ERROR_name', 'core.alert.ARC_MESSAGE_SCHEMA_VALIDATION_ERROR_description', 'ROLE', null, 'ARC', 'core.alert.ARC_MESSAGE_SCHEMA_VALIDATION_ERROR_message', 1, 1, null, 0, 0 
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 248);
END;
/