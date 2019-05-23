--liquibase formatted sql


--changeSet DEV-461:1 stripComments:false
INSERT INTO int_bp_lookup (namespace,root_name,ref_type,ref_name,method_name,rstat_cd,creation_dt,revision_dt,revision_db_id,revision_user)
SELECT 'http://xml.mxi.com/xsd/core/matadapter/part_request_status/1.1','part_request_status','EJB','com.mxi.mx.core.ejb.MaterialAdapter','partRequestStatusV1_1', 0, to_date('04-07-2010 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-07-2010 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM int_bp_lookup WHERE int_bp_lookup.namespace = 'http://xml.mxi.com/xsd/core/matadapter/part_request_status/1.1');

--changeSet DEV-461:2 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 167, 'core.alert.ETA_LATER_THAN_TASK_DEADLINE_name', 'core.alert.ETA_LATER_THAN_TASK_DEADLINE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.inventory.InventoryAuthorityFilterRule', 'PART', 'core.alert.ETA_LATER_THAN_TASK_DEADLINE_message', 1, 0, 'com.mxi.mx.core.plugin.alert.fault.FaultPriorityCalculator', 1, 0 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_alert_type WHERE utl_alert_type.alert_type_id = 167);

--changeSet DEV-461:3 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 168, 'core.alert.ETA_LATER_THAN_NEEDEDBY_DATE_name', 'core.alert.ETA_LATER_THAN_NEEDEDBY_DATE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.inventory.InventoryAuthorityFilterRule', 'PART', 'core.alert.ETA_LATER_THAN_NEEDEDBY_DATE_message', 1, 0, 'com.mxi.mx.core.plugin.alert.fault.FaultPriorityCalculator', 1, 0 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_alert_type WHERE utl_alert_type.alert_type_id = 168);