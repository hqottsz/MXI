--liquibase formatted sql


--Do not specify alert type IDs in the 2000s as these are reserved for AM-extensions.


--changeSet 0utl_alert_type:1 stripComments:false
-- Login alert type
/*************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE UTL_ALERT_TYPE
**************************************************/
/***********************
**  Hard Stop Alerts
************************/
INSERT INTO utl_alert_type (
alert_type_id,
alert_name,
alert_ldesc,
notify_cd,
notify_class,
category,
message,
key_bool,
priority,
priority_calc_class,
active_bool,
 utl_id )
VALUES (
90,
'mxcommonejb.alert.LOGIN_name',
'mxcommonejb.alert.LOGIN_ALERT_description',
'CUSTOM',
'com.mxi.mx.common.services.alert.LoginNotificationRule',
'LOGIN',
'{0}',
1,
1,
null,
1,
0 );

--changeSet 0utl_alert_type:2 stripComments:false
-- JOB_FAILED alert type
/***********************
** Common Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 2, 'mxcommonejb.alert.JOB_FAILED_name', 'mxcommonejb.alert.JOB_FAILED_description', 'ROLE', null, 'JOB', 'mxcommonejb.alert.JOB_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:5 stripComments:false
-- REPORT_GENERATION_COMPLETION alert type
/***********************
** Report Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 3, 'mxreportejb.alert.REPORT_GENERATION_COMPLETION_name', 'mxreportejb.alert.REPORT_GENERATION_COMPLETION_description', 'PRIVATE', null, 'REPORT', 'mxreportejb.alert.REPORT_GENERATION_COMPLETION_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:6 stripComments:false
-- REPORT_GENERATION_ERROR alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 4, 'mxreportejb.alert.REPORT_GENERATION_ERROR_name', 'mxreportejb.alert.REPORT_GENERATION_ERROR_description', 'PRIVATE', null, 'REPORT', 'mxreportejb.alert.REPORT_GENERATION_ERROR_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:7 stripComments:false
-- POSSIBLE_FAULT_ARRIVAL alert type
/***********************
** Fault Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 5, 'core.alert.POSSIBLE_FAULT_ARRIVAL_name', 'core.alert.POSSIBLE_FAULT_ARRIVAL_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.flight.ArrivalAirportFilterRule', 'FAULT', 'core.alert.POSSIBLE_FAULT_ARRIVAL_message', 1, 0, 'com.mxi.mx.core.plugin.alert.fault.FaultPriorityCalculator', 1, 0 );

--changeSet 0utl_alert_type:8 stripComments:false
-- POSSIBLE_FAULT_AUTHORITY alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 6, 'core.alert.POSSIBLE_FAULT_AUTHORITY_name', 'core.alert.POSSIBLE_FAULT_AUTHORITY_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.inventory.InventoryAuthorityFilterRule', 'FAULT', 'core.alert.POSSIBLE_FAULT_AUTHORITY_message', 1, 0, 'com.mxi.mx.core.plugin.alert.fault.FaultPriorityCalculator', 1, 0 );

--changeSet 0utl_alert_type:9 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 106, 'core.alert.NOTIFY_FSR_FAULT_name', 'core.alert.NOTIFY_FSR_FAULT_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.inventory.InventoryAuthorityFilterRule', 'EVENT', 'core.alert.NOTIFY_FSR_FAULT_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:10 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 107, 'core.alert.NOTIFY_ENGG_FAULT_name', 'core.alert.NOTIFY_ENGG_FAULT_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.inventory.InventoryAuthorityFilterRule', 'EVENT', 'core.alert.NOTIFY_ENGG_FAULT_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:11 stripComments:false
-- FAULT_THRESHOLD_EXCEEDED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 115, 'core.alert.FAULT_THRESHOLD_EXCEEDED_name', 'core.alert.FAULT_THRESHOLD_EXCEEDED_description', 'ROLE', null, 'FAULT', 'core.alert.FAULT_THRESHOLD_EXCEEDED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:12 stripComments:false
-- DEFERRAL_REFERENCE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 116, 'core.alert.DEFERRAL_REFERENCE_name', 'core.alert.DEFERRAL_REFERENCE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.fault.DeferRefAlertRoleFilterRule', 'FAULT', 'core.alert.DEFERRAL_REFERENCE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:13 stripComments:false
-- FAULT_THRESHOLD_EXCEEDED_ON_ASSEMBLY alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 220, 'core.alert.FAULT_THRESHOLD_EXCEEDED_ON_ASSEMBLY_name', 'core.alert.FAULT_THRESHOLD_EXCEEDED_ON_ASSEMBLY_description', 'ROLE', null, 'FAULT', 'core.alert.FAULT_THRESHOLD_EXCEEDED_ON_ASSEMBLY_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:14 stripComments:false
-- FAULT_NOT_RAISED_WORK_PACKAGE_NOT_EXISTS alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 257, 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_NOT_EXISTS_name', 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_NOT_EXISTS_description', 'ROLE', null, 'FAULT', 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_NOT_EXISTS_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:15 stripComments:false
-- FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_COMPLETE_STATE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 258, 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_COMPLETE_STATE_name', 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_COMPLETE_STATE_description', 'ROLE', null, 'FAULT', 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_COMPLETE_STATE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:16 stripComments:false
-- FAULT_NOT_RAISED_BARCODE_ALREADY_EXISTS alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 259, 'core.alert.FAULT_NOT_RAISED_BARCODE_ALREADY_EXISTS_name', 'core.alert.FAULT_NOT_RAISED_BARCODE_ALREADY_EXISTS_description', 'ROLE', null, 'FAULT', 'core.alert.FAULT_NOT_RAISED_BARCODE_ALREADY_EXISTS_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:17 stripComments:false
-- FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_CANCEL_STATE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 261, 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_CANCEL_STATE_name', 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_CANCEL_STATE_description', 'ROLE', null, 'FAULT', 'core.alert.FAULT_NOT_RAISED_WORK_PACKAGE_IS_IN_CANCEL_STATE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:18 stripComments:false
/***********************
** Flight Alerts
************************/
-- USAGE_UPDATED_BY_FLIGHT_COMPLETION alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 161, 'core.alert.USAGE_UPDATED_BY_FLIGHT_COMPLETION_name', 'core.alert.USAGE_UPDATED_BY_FLIGHT_COMPLETION_description', 'ROLE', null, 'FLIGHT', 'core.alert.USAGE_UPDATED_BY_FLIGHT_COMPLETION_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:20 stripComments:false
-- USAGE RECORD FAILURE
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES (800, 'core.alert.USAGE_RECORD_MESSAGE_ERROR_name', 'core.alert.USAGE_RECORD_MESSAGE_ERROR_description', 'ROLE', NULL, 'INVENTORY', 'core.alert.USAGE_RECORD_MESSAGE_ERROR_message', 1, 0, NULL, 1, 0);

--changeSet 0utl_alert_type:21 stripComments:false
-- AUTO_RESERVATION_JOB_FAILED alert type
/***********************
** Inventory Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 8, 'core.alert.AUTO_RESERVATION_JOB_FAILED_name', 'core.alert.AUTO_RESERVATION_JOB_FAILED_description', 'ROLE', null, 'INVENTORY', 'core.alert.AUTO_RESERVATION_JOB_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:22 stripComments:false
-- EXPIRED_INVENTORY_JOB_FAILED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 9, 'core.alert.EXPIRED_INVENTORY_JOB_FAILED_name', 'core.alert.EXPIRED_INVENTORY_JOB_FAILED_description', 'ROLE', null, 'INVENTORY', 'core.alert.EXPIRED_INVENTORY_JOB_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:23 stripComments:false
-- INVENTORY_COUNTED_CONDITION alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 10, 'core.alert.INVENTORY_COUNTED_CONDITION_name', 'core.alert.INVENTORY_COUNTED_CONDITION_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVENTORY_COUNTED_CONDITION_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:24 stripComments:false
-- INVENTORY_INSPECTED_AS_SERVICEABLE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 11, 'core.alert.INVENTORY_INSPECTED_AS_SERVICEABLE_name', 'core.alert.INVENTORY_INSPECTED_AS_SERVICEABLE_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVENTORY_INSPECTED_AS_SERVICEABLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:25 stripComments:false
-- INVENTORY_NOT_APPROVED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 69, 'core.alert.INVENTORY_NOT_APPROVED_name', 'core.alert.INVENTORY_NOT_APPROVED_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVENTORY_NOT_APPROVED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:26 stripComments:false
-- INVENTORY_NOT_ARCHIVED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 12, 'core.alert.INVENTORY_NOT_ARCHIVED_name', 'core.alert.INVENTORY_NOT_ARCHIVED_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVENTORY_NOT_ARCHIVED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:27 stripComments:false
-- INVENTORY_OOS_INSTALL alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 13, 'core.alert.INVENTORY_OOS_INSTALL_name', 'core.alert.INVENTORY_OOS_INSTALL_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVENTORY_OOS_INSTALL_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:28 stripComments:false
-- INVENTORY_OOS_REMOVAL alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 14, 'core.alert.INVENTORY_OOS_REMOVAL_name', 'core.alert.INVENTORY_OOS_REMOVAL_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVENTORY_OOS_REMOVAL_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:29 stripComments:false
-- INVENTORY_REPLICA_CREATION alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 15, 'core.alert.INVENTORY_REPLICA_CREATION_name', 'core.alert.INVENTORY_REPLICA_CREATION_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVENTORY_REPLICA_CREATION_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:30 stripComments:false
-- INVENTORY_ROLE_TRANSFORMATION_CHANGE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 16, 'core.alert.INVENTORY_ROLE_TRANSFORMATION_CHANGE_name', 'core.alert.INVENTORY_ROLE_TRANSFORMATION_CHANGE_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVENTORY_ROLE_TRANSFORMATION_CHANGE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:31 stripComments:false
-- INVENTORY_ROLE_TRANSFORMATION_SET alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 17, 'core.alert.INVENTORY_ROLE_TRANSFORMATION_SET_name', 'core.alert.INVENTORY_ROLE_TRANSFORMATION_SET_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVENTORY_ROLE_TRANSFORMATION_SET_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:32 stripComments:false
-- LOAD_CYCLE_COUNT_ERROR alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 18, 'core.alert.LOAD_CYCLE_COUNT_ERROR_name', 'core.alert.LOAD_CYCLE_COUNT_ERROR_description', 'PRIVATE', null, 'INVENTORY', 'core.alert.LOAD_CYCLE_COUNT_ERROR_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:33 stripComments:false
-- LOAD_CYCLE_COUNT_SUCCESS alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 19, 'core.alert.LOAD_CYCLE_COUNT_SUCCESS_name', 'core.alert.LOAD_CYCLE_COUNT_SUCCESS_description', 'PRIVATE', null, 'INVENTORY', 'core.alert.LOAD_CYCLE_COUNT_SUCCESS_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:34 stripComments:false
-- REPLACEMENT_INVENTORY_CREATED alert type
insert into utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd,notify_class, category, message, key_bool, priority, active_bool, utl_id)
values ( 71, 'core.alert.REPLACEMENT_INVENTORY_CREATED_name', 'core.alert.REPLACEMENT_INVENTORY_CREATED_description', 'ROLE','com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'INVENTORY', 'core.alert.REPLACEMENT_INVENTORY_CREATED_message', 1, 0, 1, 0 );

--changeSet 0utl_alert_type:35 stripComments:false
-- COMPLETE_RO_ON_INVENTORY_INSTALL alert type
insert into utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, category, message, key_bool, priority, active_bool, utl_id)
values ( 76, 'core.alert.COMPLETE_RO_ON_INVENTORY_INSTALL_name', 'core.alert.COMPLETE_RO_ON_INVENTORY_INSTALL_description', 'ROLE', 'INVENTORY', 'core.alert.COMPLETE_RO_ON_INVENTORY_INSTALL_message', 1, 0, 1, 0 );

--changeSet 0utl_alert_type:36 stripComments:false
-- INVENTORY_TURNED_IN alert type
insert into utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd,notify_class, category, message, key_bool, priority, active_bool, utl_id)
values ( 120, 'core.alert.INVENTORY_CREATED_OR_UNARCHIVED_name', 'core.alert.INVENTORY_CREATED_OR_UNARCHIVED_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVENTORY_CREATED_OR_UNARCHIVED_message', 1, 0, 1, 0 );

--changeSet 0utl_alert_type:37 stripComments:false
-- INVENTORY_NOT_INSPECTED_AS_SERVICEABLE alert type
insert into utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, category, message, key_bool, priority, active_bool, utl_id)
values ( 152, 'core.alert.INVENTORY_NOT_INSPECTED_name', 'core.alert.INVENTORY_NOT_INSPECTED_description', 'ROLE', 'INVENTORY', 'core.alert.INVENTORY_NOT_INSPECTED_message', 1, 0, 1, 0 );

--changeSet 0utl_alert_type:38 stripComments:false
-- Next Highest LRU Not found
insert into utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, category, message, key_bool, priority, active_bool, utl_id)
values ( 218, 'core.alert.NEXT_HIGHEST_LRU_NOT_FOUND_name', 'core.alert.NEXT_HIGHEST_LRU_NOT_FOUND_description', 'ROLE', 'INVENTORY', 'core.alert.NEXT_HIGHEST_LRU_NOT_FOUND_message', 1, 0, 1, 0 );

--changeSet 0utl_alert_type:39 stripComments:false
-- INVENTORY_CREATED_SUBCOMPONENTS_MISSING_DATE alert type
insert into utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, category, message, key_bool, priority, active_bool, utl_id)
values ( 221, 'core.alert.INVENTORY_CREATED_SUBCOMPONENTS_MISSING_DATE_name', 'core.alert.INVENTORY_CREATED_SUBCOMPONENTS_MISSING_DATE_description', 'ROLE', 'INVENTORY', 'core.alert.INVENTORY_CREATED_SUBCOMPONENTS_MISSING_DATE_message', 1, 0, 1, 0 );

--changeSet 0utl_alert_type:40 stripComments:false
-- EXPIRED_INVENTORY_UNRESERVRD alert type
insert into utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, category, message, key_bool, priority, active_bool, utl_id)
values ( 225, 'core.alert.EXPIRED_INVENTORY_UNRESERVRD_name', 'core.alert.EXPIRED_INVENTORY_UNRESERVRD_description', 'ROLE', 'INVENTORY', 'core.alert.EXPIRED_INVENTORY_UNRESERVRD_message', 1, 0, 1, 0 );

--changeSet 0utl_alert_type:41 stripComments:false
-- INVENTORY_CREATED_IN_QUARANTINE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 244, 'core.alert.INVENTORY_CREATED_IN_QUARANTINE_name', 'core.alert.INVENTORY_CREATED_IN_QUARANTINE_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVENTORY_CREATED_IN_QUARANTINE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:42 stripComments:false
-- ASSING_PART_TO_PART_GROUP alert type
-- INVENTORY_INSPECTED_AS_UNSERVICEABLE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 175, 'core.alert.INVENTORY_INSPECTED_AS_UNSERVICEABLE_name', 'core.alert.INVENTORY_INSPECTED_AS_UNSERVICEABLE_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVENTORY_INSPECTED_AS_UNSERVICEABLE_message', 1, 0, null, 1, 0 );

/***********************
** Part Group Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 73, 'core.alert.ASSIGN_PART_TO_PART_GROUP_name', 'core.alert.ASSIGN_PART_TO_PART_GROUP_description', 'CUSTOM', 'com.mxi.mx.core.plugin.alert.bom.PartGroupAuthorityFilterRule', 'PART', 'core.alert.ASSIGN_PART_TO_PART_GROUP_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:43 stripComments:false
-- ASSIGN_PART_TO_GROUP_IN_KIT alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 105, 'core.alert.ASSIGN_PART_TO_PART_GROUP_IN_KIT_name', 'core.alert.ASSIGN_PART_TO_PART_GROUP_IN_KIT_description', 'ROLE', null, 'PART', 'core.alert.ASSIGN_PART_TO_PART_GROUP_IN_KIT_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:44 stripComments:false
-- UNASSIGN_PART_FROM_GROUP_IN_KIT alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 154, 'core.alert.UNASSIGN_PART_FROM_GROUP_IN_KIT_name', 'core.alert.UNASSIGN_PART_FROM_GROUP_IN_KIT_description', 'ROLE', null, 'PART', 'core.alert.UNASSIGN_PART_FROM_GROUP_IN_KIT_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:45 stripComments:false
-- CONFIG_TREE_MODIFIED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 162, 'core.alert.CONFIG_TREE_MODIFIED_name', 'core.alert.CONFIG_TREE_MODIFIED_description', 'ROLE', null, 'PART', 'core.alert.CONFIG_TREE_MODIFIED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:46 stripComments:false
-- ACTIVATE_PART alert type
/***********************
** Part Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 20, 'core.alert.ACTIVATE_PART_name', 'core.alert.ACTIVATE_PART_description', 'ROLE', null, 'PART', 'core.alert.ACTIVATE_PART_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:47 stripComments:false
-- ASSIGN_ALTERNATE_PART alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 21, 'core.alert.ASSIGN_ALTERNATE_PART_name', 'core.alert.ASSIGN_ALTERNATE_PART_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'PART', 'core.alert.ASSIGN_ALTERNATE_PART_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:48 stripComments:false
-- CREATE_PART alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 22, 'core.alert.CREATE_PART_name', 'core.alert.CREATE_PART_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'PART', 'core.alert.CREATE_PART_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:49 stripComments:false
-- PART_NOT_APPROVED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 70, 'core.alert.PART_NOT_APPROVED_name', 'core.alert.PART_NOT_APPROVED_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'PART', 'core.alert.PART_NOT_APPROVED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:50 stripComments:false
-- Part ETA later than task deadline date alert type.
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 167, 'core.alert.ETA_LATER_THAN_TASK_DEADLINE_name', 'core.alert.ETA_LATER_THAN_TASK_DEADLINE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.inventory.InventoryAuthorityFilterRule', 'PART', 'core.alert.ETA_LATER_THAN_TASK_DEADLINE_message', 1, 0, 'com.mxi.mx.core.plugin.alert.fault.FaultPriorityCalculator', 1, 0 );

--changeSet 0utl_alert_type:51 stripComments:false
-- Part ETA later than task needed by date alert.
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 168, 'core.alert.ETA_LATER_THAN_NEEDEDBY_DATE_name', 'core.alert.ETA_LATER_THAN_NEEDEDBY_DATE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.inventory.InventoryAuthorityFilterRule', 'PART', 'core.alert.ETA_LATER_THAN_NEEDEDBY_DATE_message', 1, 0, 'com.mxi.mx.core.plugin.alert.fault.FaultPriorityCalculator', 1, 0 );

--changeSet 0utl_alert_type:52 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 243, 'core.alert.OBSOLETE_PART_name', 'core.alert.OBSOLETE_PART_description', 'ROLE', null, 'PART', 'core.alert.OBSOLETE_PART_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:53 stripComments:false
-- CONVERT_BORROW_TO_EXCHANGE alert type
/***********************
** Purchase Order Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 23, 'core.alert.CONVERT_BORROW_TO_EXCHANGE_name', 'core.alert.CONVERT_BORROW_TO_EXCHANGE_description', 'CUSTOM', 'com.mxi.mx.core.plugin.alert.po.PurchaseOrderContactHrRule', 'PO', 'core.alert.CONVERT_BORROW_TO_EXCHANGE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:54 stripComments:false
-- CONVERT_BORROW_TO_PURCHASE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 24, 'core.alert.CONVERT_BORROW_TO_PURCHASE_name', 'core.alert.CONVERT_BORROW_TO_PURCHASE_description', 'ROLE', null, 'PO', 'core.alert.CONVERT_BORROW_TO_PURCHASE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:55 stripComments:false
-- PURCHASE_ORDER_EXCEPTION alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 25, 'core.alert.PURCHASE_ORDER_EXCEPTION_name', 'core.alert.PURCHASE_ORDER_EXCEPTION_description', 'ROLE', null, 'PO', 'core.alert.PURCHASE_ORDER_EXCEPTION_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:56 stripComments:false
-- PURCHASE_ORDER_EXCEPTION alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 72, 'core.alert.PURCHASE_ORDER_AUTO_ISSUED_name', 'core.alert.PURCHASE_ORDER_AUTO_ISSUED_description', 'ROLE', null, 'PO', 'core.alert.PURCHASE_ORDER_AUTO_ISSUED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:57 stripComments:false
-- PURCHASE_ORDER_UNAUTHORIZED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 74, 'core.alert.PURCHASE_ORDER_UNAUTHORIZED_name', 'core.alert.PURCHASE_ORDER_UNAUTHORIZED_description', 'CUSTOM', 'com.mxi.mx.core.plugin.alert.po.PurchaseOrderContactHrRule', 'PO', 'core.alert.PURCHASE_ORDER_UNAUTHORIZED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:58 stripComments:false
-- PART_REQUEST_CANCELLED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 117, 'core.alert.PR_CANCELLED_name', 'core.alert.PR_CANCELLED_description', 'CUSTOM', 'com.mxi.mx.core.plugin.alert.req.RequestCancelledContactHrRule', 'PO', 'core.alert.PR_CANCELLED_message', 1, 1, null, 1, 0 );

--changeSet 0utl_alert_type:59 stripComments:false
-- AOG_OVERRIDE_ALERT type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 121, 'core.alert.PO_AOG_AUTH_name', 'core.alert.PO_AOG_AUTH_description', 'ROLE', null, 'PO', 'core.alert.PO_AOG_AUTH_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:60 stripComments:false
-- PO_AUTO_GEN_INVALID_VENDOR alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 78, 'core.alert.PO_AUTO_GEN_INVALID_VENDOR_name', 'core.alert.PO_AUTO_GEN_INVALID_VENDOR_description', 'ROLE', null, 'PO', 'core.alert.PO_AUTO_GEN_INVALID_VENDOR_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:61 stripComments:false
-- ORDER_BELOW_MINIMUM_VENDOR_PART_QUANTITY alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 240, 'core.alert.ORDER_BELOW_MINIMUM_VENDOR_PART_QUANTITY_name', 'core.alert.ORDER_BELOW_MINIMUM_VENDOR_PART_QUANTITY_description', 'ROLE', null, 'PO', 'core.alert.ORDER_BELOW_MINIMUM_VENDOR_PART_QUANTITY_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:63 stripComments:false
-- MESSAGE NOT DELIVERED
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 602, 'integration.alert.MESSAGE_IS_NOT_DELIVERED_name', 'integration.alert.MESSAGE_IS_NOT_DELIVERED_description', 'ROLE', null, 'SYSTEM', 'integration.alert.MESSAGE_IS_NOT_DELIVERED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:67 stripComments:false
-- GENERATE_PART_REQUESTS_JOB_FAILED alert type
/***********************
** Part Request Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 26, 'core.alert.GENERATE_PART_REQUESTS_JOB_FAILED_name', 'core.alert.GENERATE_PART_REQUESTS_JOB_FAILED_description', 'ROLE', null, 'REQ', 'core.alert.GENERATE_PART_REQUESTS_JOB_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:68 stripComments:false
-- PART_REQUEST_INVENTORY_ASSIGNMENT alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 27, 'core.alert.PART_REQUEST_INVENTORY_ASSIGNMENT_name', 'core.alert.PART_REQUEST_INVENTORY_ASSIGNMENT_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_INVENTORY_ASSIGNMENT_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:69 stripComments:false
-- PART_REQUEST_NON_ARCHIVE_INVENTORY_ASSIGNMENT alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 68, 'core.alert.PART_REQUEST_NON_ARCHIVE_INVENTORY_ASSIGNMENT_name', 'core.alert.PART_REQUEST_NON_ARCHIVE_INVENTORY_ASSIGNMENT_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_NON_ARCHIVE_INVENTORY_ASSIGNMENT_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:70 stripComments:false
-- INVENTORY_UNRESERVED_FROM_PART_REQUEST alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 77, 'core.alert.INVENTORY_UNRESERVED_FROM_PART_REQUEST_name', 'core.alert.INVENTORY_UNRESERVED_FROM_PART_REQUEST_description', 'PRIVATE', null, 'REQ', 'core.alert.INVENTORY_UNRESERVED_FROM_PART_REQUEST_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:71 stripComments:false
-- PART_REQUEST_STOLEN alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 114, 'core.alert.PART_REQUEST_STOLEN_name', 'core.alert.PART_REQUEST_STOLEN_description', 'PRIVATE', null, 'REQ', 'core.alert.PART_REQUEST_STOLEN_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:72 stripComments:false
-- Material Adapter for part request alert
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 219, 'core.alert.PART_REQUEST_MESSAGE_BUILD_ERROR_name', 'core.alert.PART_REQUEST_MESSAGE_BUILD_ERROR_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.inventory.InventoryAuthorityFilterRule', 'REQ', 'core.alert.PART_REQUEST_MESSAGE_BUILD_ERROR_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:73 stripComments:false
-- REQUEST_DETAILS_CHANGED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 118, 'core.alert.PART_REQUEST_DETAILS_CHANGE_name', 'core.alert.PART_REQUEST_DETAILS_CHANGE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.req.PartRequestDetailsChangedContactHrRule', 'REQ', 'core.alert.PART_REQUEST_DETAILS_CHANGE_message', 1, 1, null, 1, 0 );

--changeSet 0utl_alert_type:74 stripComments:false
-- PART_NOT_AVAILABLE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 250, 'core.alert.PART_NOT_AVAILABLE_name', 'core.alert.PART_NOT_AVAILABLE_description', 'ROLE', null, 'REQ', 'core.alert.PART_NOT_AVAILABLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:75 stripComments:false
-- PART_REQUEST_ISSUE_LOCKED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 251, 'core.alert.PART_REQUEST_ISSUE_LOCKED_name', 'core.alert.PART_REQUEST_ISSUE_LOCKED_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_ISSUE_LOCKED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:76 stripComments:false
-- PART_REQUEST_ISSUE_SCRAPPED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 252, 'core.alert.PART_REQUEST_ISSUE_SCRAPPED_name', 'core.alert.PART_REQUEST_ISSUE_SCRAPPED_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_ISSUE_SCRAPPED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:77 stripComments:false
-- PART_REQUEST_ISSUE_INV_NOT_EXIST_OR_ARCHIVED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 253, 'core.alert.PART_REQUEST_ISSUE_INV_NOT_EXIST_OR_ARCHIVED_name', 'core.alert.PART_REQUEST_ISSUE_INV_NOT_EXIST_OR_ARCHIVED_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_ISSUE_INV_NOT_EXIST_OR_ARCHIVED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:78 stripComments:false
-- PART_REQUEST_ISSUE_NOT_SERVICEABLE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 254, 'core.alert.PART_REQUEST_ISSUE_NOT_SERVICEABLE_name', 'core.alert.PART_REQUEST_ISSUE_NOT_SERVICEABLE_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_ISSUE_NOT_SERVICEABLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:79 stripComments:false
-- PART_REQUEST_ISSUE_SER_INV_FOUND_IN_SERVICE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 255, 'core.alert.PART_REQUEST_ISSUE_SER_INV_FOUND_IN_SERVICE_name', 'core.alert.PART_REQUEST_ISSUE_SER_INV_FOUND_IN_SERVICE_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_ISSUE_SER_INV_FOUND_IN_SERVICE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:80 stripComments:false
-- PART_REQ_ALREADY_ISSUE_DIFF alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 256, 'core.alert.PART_REQUEST_ALREADY_ISSUED_DIFF_name', 'core.alert.PART_REQUEST_ALREADY_ISSUED_DIFF_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_ALREADY_ISSUED_DIFF_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:81 stripComments:false
-- PART_REQ_ALREADY_ISSUE_DIFF alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 260, 'core.alert.PART_REQUEST_ISSUE_ERROR_name', 'core.alert.PART_REQUEST_ISSUE_ERROR_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_ISSUE_ERROR_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:82 stripComments:false
-- PART_REQ_ALREADY_ISSUE_DIFF alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 262, 'core.alert.PART_REQUEST_ISSUE_INV_ERROR_name', 'core.alert.PART_REQUEST_ISSUE_INV_ERROR_description', 'ROLE', null, 'REQ', 'core.alert.PART_REQUEST_ISSUE_INV_ERROR_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:83 stripComments:false
-- GENERATE ALERT for RFQ actions
/***********************
** RFQ Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 91, 'core.alert.UNAPPROVED_PART_CREATED_BY_RFQ_name', 'core.alert.UNAPPROVED_PART_CREATED_BY_RFQ_description', 'CUSTOM', 'com.mxi.mx.core.plugin.alert.rfq.NotifyEngineering', 'RFQ', 'core.alert.UNAPPROVED_PART_CREATED_BY_RFQ_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:84 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 92, 'core.alert.RFQ_ALL_VENDOR_ALL_PARTS_name', 'core.alert.RFQ_ALL_VENDOR_ALL_PARTS_description', 'CUSTOM', 'com.mxi.mx.core.plugin.alert.rfq.NotifyRfqOwner', 'RFQ', 'core.alert.RFQ_ALL_VENDOR_ALL_PARTS_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:85 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 93, 'core.alert.RFQ_SPEC2K_NOT_COMPLIANT_VENDOR_name', 'core.alert.RFQ_SPEC2K_NOT_COMPLIANT_VENDOR_description', 'ROLE', null, 'RFQ', 'core.alert.RFQ_SPEC2K_NOT_COMPLIANT_VENDOR_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:86 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 94, 'core.alert.RFQ_UNKNOWN_VENDOR_CD_name', 'core.alert.RFQ_UNKNOWN_VENDOR_CD_description', 'ROLE', null, 'RFQ', 'core.alert.RFQ_UNKNOWN_VENDOR_CD_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:87 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 95, 'core.alert.RFQ_LINE_VENDOR_UNKNOWN_name', 'core.alert.RFQ_LINE_VENDOR_UNKNOWN_description', 'ROLE', null, 'RFQ', 'core.alert.RFQ_LINE_VENDOR_UNKNOWN_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:88 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 96, 'core.alert.RFQ_STATUS_INVALID_name', 'core.alert.RFQ_STATUS_INVALID_description', 'ROLE', null, 'RFQ', 'core.alert.RFQ_STATUS_INVALID_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:89 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 97, 'core.alert.RFQ_NUMBER_INVALID_name', 'core.alert.RFQ_NUMBER_INVALID_description', 'ROLE', null, 'RFQ', 'core.alert.RFQ_NUMBER_INVALID_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:90 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 98, 'core.alert.RFQ_SPEC2K_FORMAT_INVALID_name', 'core.alert.RFQ_SPEC2K_FORMAT_INVALID_description', 'ROLE', null, 'RFQ', 'core.alert.RFQ_SPEC2K_FORMAT_INVALID_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:91 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 99, 'core.alert.RFQ_SPEC2K_FORMAT_INVALID_name', 'core.alert.RFQ_SPEC2K_FORMAT_INVALID_description', 'CUSTOM', 'com.mxi.mx.core.plugin.alert.rfq.NotifyRfqOwner', 'RFQ', 'core.alert.RFQ_SPEC2K_FORMAT_INVALID_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:92 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 100, 'core.alert.RFQ_VENDOR_MISMATCH_name', 'core.alert.RFQ_VENDOR_MISMATCH_description', 'ROLE', null, 'RFQ', 'core.alert.RFQ_VENDOR_MISMATCH_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:93 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 101, 'core.alert.RFQ_CUSTOMER_MISMATCH_name', 'core.alert.RFQ_CUSTOMER_MISMATCH_description', 'ROLE', null, 'RFQ', 'core.alert.RFQ_CUSTOMER_MISMATCH_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:94 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 102, 'core.alert.RFQ_RESPOND_BY_DATE_name', 'core.alert.RFQ_RESPOND_BY_DATE_description', 'CUSTOM', 'com.mxi.mx.core.plugin.alert.rfq.NotifyRfqOwner', 'RFQ', 'core.alert.RFQ_RESPOND_BY_DATE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:95 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 104, 'core.alert.RFQ_UNKNOWN_CUSTOMER_CD_name', 'core.alert.RFQ_UNKNOWN_CUSTOMER_CD_description', 'ROLE', null, 'RFQ', 'core.alert.RFQ_UNKNOWN_CUSTOMER_CD_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:96 stripComments:false
-- NO_MATCH_FOR_SHIPMENT_ADVISORY alert type
/***********************
** Shipment Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 28, 'core.alert.NO_MATCH_FOR_SHIPMENT_ADVISORY_name', 'core.alert.NO_MATCH_FOR_SHIPMENT_ADVISORY_description', 'ROLE', null, 'SHIPMENT', 'core.alert.NO_MATCH_FOR_SHIPMENT_ADVISORY_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:97 stripComments:false
-- RECEIVED_INSTALLED_PART alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 29, 'core.alert.RECEIVED_INSTALLED_PART_name', 'core.alert.RECEIVED_INSTALLED_PART_description', 'ROLE', null, 'SHIPMENT', 'core.alert.RECEIVED_INSTALLED_PART_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:98 stripComments:false
-- RECEIVED_INSTALLED_PART_UNDEFINED_REPL alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 30, 'core.alert.RECEIVED_INSTALLED_PART_UNDEFINED_REPL_name', 'core.alert.RECEIVED_INSTALLED_PART_UNDEFINED_REPL_description', 'ROLE', null, 'SHIPMENT', 'core.alert.RECEIVED_INSTALLED_PART_UNDEFINED_REPL_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:99 stripComments:false
-- RECEIVED_LOOSE_PART alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 31, 'core.alert.RECEIVED_LOOSE_PART_name', 'core.alert.RECEIVED_LOOSE_PART_description', 'ROLE', null, 'SHIPMENT', 'core.alert.RECEIVED_LOOSE_PART_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:100 stripComments:false
-- RECEIVED_SCRAPPED_PART alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 32, 'core.alert.RECEIVED_SCRAPPED_PART_name', 'core.alert.RECEIVED_SCRAPPED_PART_description', 'ROLE', null, 'SHIPMENT', 'core.alert.RECEIVED_SCRAPPED_PART_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:101 stripComments:false
-- SHIPMENT_ADVISORY alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 33, 'core.alert.SHIPMENT_ADVISORY_name', 'core.alert.SHIPMENT_ADVISORY_description', 'ROLE', null, 'SHIPMENT', 'core.alert.SHIPMENT_ADVISORY_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:102 stripComments:false
-- REMOTE_RESERVATION_SHIPMENT_FAILED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 217, 'core.alert.REMOTE_RESERVATION_SHIPMENT_FAILED_name', 'core.alert.REMOTE_RESERVATION_SHIPMENT_FAILED_description', 'ROLE', null, 'SHIPMENT', 'core.alert.REMOTE_RESERVATION_SHIPMENT_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:103 stripComments:false
-- RECEIVED_SIMILAR_DUPLICATE_SERIAL_NO alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 249, 'core.alert.RECEIVED_SIMILAR_DUPLICATE_SERIAL_NO_name', 'core.alert.RECEIVED_SIMILAR_DUPLICATE_SERIAL_NO_description', 'ROLE', null, 'SHIPMENT', 'core.alert.RECEIVED_SIMILAR_DUPLICATE_SERIAL_NO_message', 1, 0, null, 0, 0 );

--changeSet 0utl_alert_type:104 stripComments:false
-- DELETE_ORPHANED_FORECASTED_TASKS_JOB_FAILED alert type
/***********************
** Task Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 34, 'core.alert.DELETE_ORPHANED_FORECASTED_TASKS_JOB_FAILED_name', 'core.alert.DELETE_ORPHANED_FORECASTED_TASKS_JOB_FAILED_description', 'ROLE', null, 'TASK', 'core.alert.DELETE_ORPHANED_FORECASTED_TASKS_JOB_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:105 stripComments:false
-- GENERATE_FORECASTED_TASKS_JOB_FAILED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 35, 'core.alert.GENERATE_FORECASTED_TASKS_JOB_FAILED_name', 'core.alert.GENERATE_FORECASTED_TASKS_JOB_FAILED_description', 'ROLE', null, 'TASK', 'core.alert.GENERATE_FORECASTED_TASKS_JOB_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:106 stripComments:false
-- PRINT_TASK_CARDS_JOB_COMPLETED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 36, 'core.alert.PRINT_TASK_CARDS_JOB_COMPLETED_name', 'core.alert.PRINT_TASK_CARDS_JOB_COMPLETED_description', 'PRIVATE', null, 'TASK', 'core.alert.PRINT_TASK_CARDS_JOB_COMPLETED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:107 stripComments:false
-- PRINT_TASK_CARDS_JOB_FAILED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 37, 'core.alert.PRINT_TASK_CARDS_JOB_FAILED_name', 'core.alert.PRINT_TASK_CARDS_JOB_FAILED_description', 'PRIVATE', null, 'TASK', 'core.alert.PRINT_TASK_CARDS_JOB_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:108 stripComments:false
-- TASK_TRANSFORMATION_FOUND alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 38, 'core.alert.TASK_TRANSFORMATION_FOUND_name', 'core.alert.TASK_TRANSFORMATION_FOUND_description', 'ROLE', null, 'TASK', 'core.alert.TASK_TRANSFORMATION_FOUND_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:109 stripComments:false
-- TASK_TRANSFORMATION_NOT_FOUND alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 39, 'core.alert.TASK_TRANSFORMATION_NOT_FOUND_name', 'core.alert.TASK_TRANSFORMATION_NOT_FOUND_description', 'ROLE', null, 'TASK', 'core.alert.TASK_TRANSFORMATION_NOT_FOUND_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:110 stripComments:false
-- TASK_TRANSFORMATION_SCHED_RULE_MISMATCH alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 40, 'core.alert.TASK_TRANSFORMATION_SCHED_RULE_MISMATCH_name', 'core.alert.TASK_TRANSFORMATION_SCHED_RULE_MISMATCH_description', 'ROLE', null, 'TASK', 'core.alert.TASK_TRANSFORMATION_SCHED_RULE_MISMATCH_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:111 stripComments:false
-- INSTALL_BATCH_NOT_ISSUED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 44, 'core.alert.INSTALL_BATCH_NOT_ISSUED_name', 'core.alert.INSTALL_BATCH_NOT_ISSUED_description', 'ROLE', null, 'TASK', 'core.alert.INSTALL_BATCH_NOT_ISSUED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:112 stripComments:false
-- INSTALL_PART_ATTACHED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 45, 'core.alert.INSTALL_PART_ATTACHED_name', 'core.alert.INSTALL_PART_ATTACHED_description', 'ROLE', null, 'TASK', 'core.alert.INSTALL_PART_ATTACHED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:113 stripComments:false
-- INSTALL_PART_ATTACHED_WITH_UNDEFINED_REPLACEMENT alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 75, 'core.alert.INSTALL_PART_ATTACHED_WITH_UNDEFINED_REPLACEMENT_name', 'core.alert.INSTALL_PART_ATTACHED_WITH_UNDEFINED_REPLACEMENT_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'TASK', 'core.alert.INSTALL_PART_ATTACHED_WITH_UNDEFINED_REPLACEMENT_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:114 stripComments:false
-- INSTALL_PART_NOT_APPLICABLE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 46, 'core.alert.INSTALL_PART_NOT_APPLICABLE_name', 'core.alert.INSTALL_PART_NOT_APPLICABLE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'TASK', 'core.alert.INSTALL_PART_NOT_APPLICABLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:115 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 144, 'core.alert.ATTACHED_PART_NOT_APPLICABLE_name', 'core.alert.ATTACHED_PART_NOT_APPLICABLE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'INVENTORY', 'core.alert.ATTACHED_PART_NOT_APPLICABLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:116 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 145, 'core.alert.MODIFIED_PART_NOT_APPLICABLE_name', 'core.alert.MODIFIED_PART_NOT_APPLICABLE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'INVENTORY', 'core.alert.MODIFIED_PART_NOT_APPLICABLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:117 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 146, 'core.alert.ATTACHED_PART_GROUP_NOT_APPLICABLE_name', 'core.alert.ATTACHED_PART_GROUP_NOT_APPLICABLE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'INVENTORY', 'core.alert.ATTACHED_PART_GROUP_NOT_APPLICABLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:118 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 147, 'core.alert.MODIFIED_PART_GROUP_NOT_APPLICABLE_name', 'core.alert.MODIFIED_PART_GROUP_NOT_APPLICABLE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'INVENTORY', 'core.alert.MODIFIED_PART_GROUP_NOT_APPLICABLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:119 stripComments:false
-- INSTALL_PART_NOT_APPROVED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 47, 'core.alert.INSTALL_PART_NOT_APPROVED_name', 'core.alert.INSTALL_PART_NOT_APPROVED_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'TASK', 'core.alert.INSTALL_PART_NOT_APPROVED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:120 stripComments:false
-- INSTALL_PART_NOT_FOUND alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 48, 'core.alert.INSTALL_PART_NOT_FOUND_name', 'core.alert.INSTALL_PART_NOT_FOUND_description', 'ROLE', null, 'TASK', 'core.alert.INSTALL_PART_NOT_FOUND_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:121 stripComments:false
-- INSTALL_PART_NOT_ISSUED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 49, 'core.alert.INSTALL_PART_NOT_ISSUED_name', 'core.alert.INSTALL_PART_NOT_ISSUED_description', 'ROLE', null, 'TASK', 'core.alert.INSTALL_PART_NOT_ISSUED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:122 stripComments:false
-- INSTALL_PART_NOT_PART_COMPATIBLE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 50, 'core.alert.INSTALL_PART_NOT_PART_COMPATIBLE_name', 'core.alert.INSTALL_PART_NOT_PART_COMPATIBLE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'TASK', 'core.alert.INSTALL_PART_NOT_PART_COMPATIBLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:123 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 140, 'core.alert.ATTACHED_PART_NOT_PART_COMPATIBLE_name', 'core.alert.ATTACHED_PART_NOT_PART_COMPATIBLE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'INVENTORY', 'core.alert.ATTACHED_PART_NOT_PART_COMPATIBLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:124 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 141, 'core.alert.MODIFIED_PART_NOT_PART_COMPATIBLE_name', 'core.alert.MODIFIED_PART_NOT_PART_COMPATIBLE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'INVENTORY', 'core.alert.MODIFIED_PART_NOT_PART_COMPATIBLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:125 stripComments:false
-- INSTALL_PART_NOT_RFI alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 51, 'core.alert.INSTALL_PART_NOT_RFI_name', 'core.alert.INSTALL_PART_NOT_RFI_description', 'ROLE', null, 'TASK', 'core.alert.INSTALL_PART_NOT_RFI_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:126 stripComments:false
-- INSTALL_PART_NOT_TASK_COMPATIBLE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 52, 'core.alert.INSTALL_PART_NOT_TASK_COMPATIBLE_name', 'core.alert.INSTALL_PART_NOT_TASK_COMPATIBLE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'TASK', 'core.alert.INSTALL_PART_NOT_TASK_COMPATIBLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:127 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 142, 'core.alert.ATTACHED_PART_NOT_TASK_COMPATIBLE_name', 'core.alert.ATTACHED_PART_NOT_TASK_COMPATIBLE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'INVENTORY', 'core.alert.ATTACHED_PART_NOT_TASK_COMPATIBLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:128 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 143, 'core.alert.MODIFIED_PART_NOT_TASK_COMPATIBLE_name', 'core.alert.MODIFIED_PART_NOT_TASK_COMPATIBLE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'INVENTORY', 'core.alert.MODIFIED_PART_NOT_TASK_COMPATIBLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:129 stripComments:false
-- INSTALL_PART_NOT_APPROVED_IN_GROUP alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 103, 'core.alert.INSTALL_PART_NOT_APPROVED_IN_GROUP_name', 'core.alert.INSTALL_PART_NOT_APPROVED_IN_GROUP_description', 'ROLE', null, 'TASK', 'core.alert.INSTALL_PART_NOT_APPROVED_IN_GROUP_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:130 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 150, 'core.alert.ATTACHED_PART_NOT_APPROVED_IN_GROUP_name', 'core.alert.ATTACHED_PART_NOT_APPROVED_IN_GROUP_description', 'ROLE', null, 'INVENTORY', 'core.alert.ATTACHED_PART_NOT_APPROVED_IN_GROUP_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:131 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 151, 'core.alert.MODIFIED_PART_NOT_APPROVED_IN_GROUP_name', 'core.alert.MODIFIED_PART_NOT_APPROVED_IN_GROUP_description', 'ROLE', null, 'INVENTORY', 'core.alert.MODIFIED_PART_NOT_APPROVED_IN_GROUP_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:132 stripComments:false
-- TOBE_AUTO_RECEIVED_INSPECTED_ISSUED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 153, 'core.alert.TOBE_AUTO_RECEIVED_INSPECTED_ISSUED_name', 'core.alert.TOBE_AUTO_RECEIVED_INSPECTED_ISSUED_description', 'ROLE', null, 'TASK', 'core.alert.TOBE_AUTO_RECEIVED_INSPECTED_ISSUED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:133 stripComments:false
-- TOBE_AUTO_RECEIVED_INSPECTED_ISSUED alert type for batch components
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 163, 'core.alert.TOBE_AUTO_RECEIVED_INSPECTED_ISSUED_BATCH_name', 'core.alert.TOBE_AUTO_RECEIVED_INSPECTED_ISSUED_BATCH_description', 'ROLE', null, 'TASK', 'core.alert.TOBE_AUTO_RECEIVED_INSPECTED_ISSUED_BATCH_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:134 stripComments:false
-- REMOVE_PART_DIFF_POS alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 53, 'core.alert.REMOVE_PART_DIFF_POS_name', 'core.alert.REMOVE_PART_DIFF_POS_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'TASK', 'core.alert.REMOVE_PART_DIFF_POS_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:135 stripComments:false
-- REMOVE_PART_HOLE_EXISTS alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 54, 'core.alert.REMOVE_PART_HOLE_EXISTS_name', 'core.alert.REMOVE_PART_HOLE_EXISTS_description', 'ROLE', null, 'TASK', 'core.alert.REMOVE_PART_HOLE_EXISTS_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:136 stripComments:false
-- REMOVE_PART_HOLE_EXISTS_UNDEFINED_REPL alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 55, 'core.alert.REMOVE_PART_HOLE_EXISTS_UNDEFINED_REPL_name', 'core.alert.REMOVE_PART_HOLE_EXISTS_UNDEFINED_REPL_description', 'ROLE', null, 'TASK', 'core.alert.REMOVE_PART_HOLE_EXISTS_UNDEFINED_REPL_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:137 stripComments:false
-- REMOVE_PART_LOOSE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 56, 'core.alert.REMOVE_PART_LOOSE_name', 'core.alert.REMOVE_PART_LOOSE_description', 'ROLE', null, 'TASK', 'core.alert.REMOVE_PART_LOOSE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:138 stripComments:false
-- REMOVE_PART_MISMATCH alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 57, 'core.alert.REMOVE_PART_MISMATCH_name', 'core.alert.REMOVE_PART_MISMATCH_description', 'ROLE', null, 'TASK', 'core.alert.REMOVE_PART_MISMATCH_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:139 stripComments:false
-- REMOVE_PART_MISMATCH_UNDEFINED_REPL alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 58, 'core.alert.REMOVE_PART_MISMATCH_UNDEFINED_REPL_name', 'core.alert.REMOVE_PART_MISMATCH_UNDEFINED_REPL_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'TASK', 'core.alert.REMOVE_PART_MISMATCH_UNDEFINED_REPL_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:140 stripComments:false
-- REMOVE_PART_NOT_FOUND alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 59, 'core.alert.REMOVE_PART_NOT_FOUND_name', 'core.alert.REMOVE_PART_NOT_FOUND_description', 'ROLE', null, 'TASK', 'core.alert.REMOVE_PART_NOT_FOUND_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:141 stripComments:false
-- REMOVE_PART_NOT_FOUND_HOLE_EXISTS alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 60, 'core.alert.REMOVE_PART_NOT_FOUND_HOLE_EXISTS_name', 'core.alert.REMOVE_PART_NOT_FOUND_HOLE_EXISTS_description', 'ROLE', null, 'TASK', 'core.alert.REMOVE_PART_NOT_FOUND_HOLE_EXISTS_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:142 stripComments:false
-- INSTALL PART NOT INTERCHANGEABLE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 61, 'core.alert.INSTALL_PART_NOT_INTERCHANGEABLE_name', 'core.alert.INSTALL_PART_NOT_INTERCHANGEABLE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'TASK', 'core.alert.INSTALL_PART_NOT_INTERCHANGEABLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:143 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 148, 'core.alert.ATTACHED_PART_NOT_INTERCHANGEABLE_name', 'core.alert.ATTACHED_PART_NOT_INTERCHANGEABLE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'INVENTORY', 'core.alert.ATTACHED_PART_NOT_INTERCHANGEABLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:144 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 149, 'core.alert.MODIFIED_PART_NOT_INTERCHANGEABLE_name', 'core.alert.MODIFIED_PART_NOT_INTERCHANGEABLE_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.org.OrgRoleNotificationRule', 'INVENTORY', 'core.alert.MODIFIED_PART_NOT_INTERCHANGEABLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:145 stripComments:false
-- REPLACE_PART_QTY_MISMATCH alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 62, 'core.alert.REPLACE_PART_QTY_MISMATCH_name', 'core.alert.REPLACE_PART_QTY_MISMATCH_description', 'ROLE', null, 'TASK', 'core.alert.REPLACE_PART_QTY_MISMATCH_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:146 stripComments:false
-- UNABLE_TO_SCHED_FROM_BIRT alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 112, 'core.alert.UNABLE_TO_SCHED_FROM_BIRTH_name', 'core.alert.UNABLE_TO_SCHED_FROM_BIRTH_description', 'ROLE', null, 'TASK', 'core.alert.UNABLE_TO_SCHED_FROM_BIRTH_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:147 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 216, 'core.alert.UNABLE_TO_SCHED_FROM_EFFECTIVE_DATE_name', 'core.alert.UNABLE_TO_SCHED_FROM_EFFECTIVE_DATE_description', 'ROLE', null, 'TASK', 'core.alert.UNABLE_TO_SCHED_FROM_EFFECTIVE_DATE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:148 stripComments:false
-- MAXIMUM MEASUREMENT
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 138, 'core.alert.MAXIMUM_MEASUREMENT_name', 'core.alert.MAXIMUM_MEASUREMENT_description', 'ROLE', null, 'TASK', 'core.alert.MAXIMUM_MEASUREMENT_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:149 stripComments:false
-- MINIMUM MEASUREMENT
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 139, 'core.alert.MINIMUM_MEASUREMENT_name', 'core.alert.MINIMUM_MEASUREMENT_description', 'ROLE', null, 'TASK', 'core.alert.MINIMUM_MEASUREMENT_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:152 stripComments:false
-- UNABLE_TO_SCHEDULE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 222, 'core.alert.UNABLE_TO_SCHEDULE_name', 'core.alert.UNABLE_TO_SCHEDULE_description', 'ROLE', null, 'TASK', 'core.alert.UNABLE_TO_SCHEDULE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:153 stripComments:false
-- REQUIREMENT_COMPLETION_ADVISORY alert type
/***********************
** Task Definition Alerts
************************/
insert into utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, active_bool, utl_id)
values ( 122, 'core.alert.REQUIREMENT_COMPLETION_ADVISORY_name', 'core.alert.REQUIREMENT_COMPLETION_ADVISORY_description', 'CUSTOM', 'com.mxi.mx.core.plugin.alert.taskdefn.RequirementCompletionAdvisoryRule', 'TASKDEFN', 'core.alert.REQUIREMENT_COMPLETION_ADVISORY_message', 1, 0, 1, 0 );

--changeSet 0utl_alert_type:154 stripComments:false
-- FLEET_COMPLETION_ADVISORY alert type
insert into utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, active_bool, utl_id)
values ( 159, 'core.alert.FLEET_COMPLETION_ADVISORY_name', 'core.alert.FLEET_COMPLETION_ADVISORY_description', 'CUSTOM',
'com.mxi.mx.core.plugin.alert.taskdefn.FleetCompletionAdvisoryRule', 'TASKDEFN', 'core.alert.FLEET_COMPLETION_ADVISORY_message', 1, 0, 1, 0 );

--changeSet 0utl_alert_type:155 stripComments:false
-- TASK_DEFN_ACTV_FOR_INV_WITH_MISSING alert type
insert into utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message,
key_bool, priority, active_bool, utl_id)
values ( 214, 'core.alert.TASK_DEFN_ACTV_FOR_INV_WITH_MISSING_DATES_name', 'core.alert.TASK_DEFN_ACTV_FOR_INV_WITH_MISSING_DATES_description', 'ROLE', null, 'TASKDEFN', 'core.alert.TASK_DEFN_ACTV_FOR_INV_WITH_MISSING_DATES_message', 1, 0, 1, 0 );

--changeSet 0utl_alert_type:156 stripComments:false
-- TASK_DEFN_OBSOLETE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 215, 'core.alert.TASK_DEFN_OBSOLETE_name', 'core.alert.TASK_DEFN_OBSOLETE_description', 'ROLE', null, 'TASK_DEFN', 'core.alert.TASK_DEFN_OBSOLETE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:157 stripComments:false
-- GENERATE_ISSUE_TRANSFERS_JOB_FAILED alert type
/***********************
** Transfer Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 63, 'core.alert.GENERATE_ISSUE_TRANSFERS_JOB_FAILED_name', 'core.alert.GENERATE_ISSUE_TRANSFERS_JOB_FAILED_description', 'ROLE', null, 'TRANSFER', 'core.alert.GENERATE_ISSUE_TRANSFERS_JOB_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:158 stripComments:false
-- PRINT_TICKET_FAILURE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 64, 'core.alert.PRINT_TICKET_FAILURE_name', 'core.alert.PRINT_TICKET_FAILURE_description', 'PRIVATE', null, 'TRANSFER', 'core.alert.PRINT_TICKET_FAILURE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:159 stripComments:false
-- PRINT_TICKET_SUCCESS alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 65, 'core.alert.PRINT_TICKET_SUCCESS_name', 'core.alert.PRINT_TICKET_SUCCESS_description', 'PRIVATE', null, 'TRANSFER', 'core.alert.PRINT_TICKET_SUCCESS_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:160 stripComments:false
-- BASELINE_SYNC_FAILED alert type
/***********************
** Baseline Synchronization Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 108, 'core.alert.BASELINE_SYNC_FAILED_name', 'core.alert.BASELINE_SYNC_FAILED_description', 'ROLE', null, 'SYNCH', 'core.alert.BASELINE_SYNC_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:161 stripComments:false
-- BASELINE_SYNC_COMPLETE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 109, 'core.alert.BASELINE_SYNC_COMPLETE_name', 'core.alert.BASELINE_SYNC_COMPLETE_description', 'ROLE', null, 'SYNCH', 'core.alert.BASELINE_SYNC_COMPLETE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:162 stripComments:false
-- ZIP_FAILED alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 130, 'core.alert.ZIP_SYNC_FAILED_name', 'core.alert.ZIP_SYNC_FAILED_description', 'ROLE', null, 'SYNCH', 'core.alert.ZIP_SYNC_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:163 stripComments:false
-- ZIP_COMPLETE alert type
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 131, 'core.alert.ZIP_SYNC_COMPLETE_name', 'core.alert.ZIP_SYNC_COMPLETE_description', 'ROLE', null, 'SYNCH', 'core.alert.ZIP_SYNC_COMPLETE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:164 stripComments:false
/***********************
** Licensing Alerts
************************/
INSERT INTO utl_alert_type( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES(110,'core.alert.LICENSE_WILL_EXPIRE_ALERT_name','core.alert.LICENSE_WILL_EXPIRE_ALERT_description','CUSTOM', 'com.mxi.mx.core.plugin.alert.license.NotifyExpiredLicOwner', 'JOB','core.alert.LICENSE_WILL_EXPIRE_ALERT_message',1,0, null, 1,0);

--changeSet 0utl_alert_type:165 stripComments:false
INSERT INTO utl_alert_type( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES(111,'core.alert.LICENSE_EXPIRED_ALERT_name','core.alert.LICENSE_EXPIRED_ALERT_description','CUSTOM', 'com.mxi.mx.core.plugin.alert.license.NotifyExpiredLicOwner','JOB','core.alert.LICENSE_EXPIRED_ALERT_message',1,0,null, 1,0);

--changeSet 0utl_alert_type:166 stripComments:false
-- LICENSE_EXPIRED_JOB_FAILED alert
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 166, 'core.alert.LICENSE_EXPIRED_JOB_FAILED_name', 'core.alert.LICENSE_EXPIRED_JOB_FAILED_description', 'ROLE', null, 'JOB', 'core.alert.LICENSE_EXPIRED_JOB_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:167 stripComments:false
/***********************
** Status Board Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 113, 'core.alert.STATUS_BOARD_QUERY_name', 'core.alert.STATUS_BOARD_QUERY_description', 'ROLE', null, 'SB', 'core.alert.STATUS_BOARD_QUERY_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:168 stripComments:false
/***********************
** Printing Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 119, 'core.alert.NO_DEFAULT_PRINTER_name', 'core.alert.NO_DEFAULT_PRINTER_description', 'ROLE', null, 'PRINT', 'core.alert.NO_DEFAULT_PRINTER_message', 1, 0, null, 1, 0 );

INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
VALUES(270,'core.alert.PRINT_JOB_COMPLETED_name','core.alert.PRINT_JOB_COMPLETED_description','PRIVATE',NULL,'PRINT','core.alert.PRINT_JOB_COMPLETED_message',1,0,NULL,1,0);

INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
VALUES(271,'core.alert.PRINT_JOB_FAILED_name','core.alert.PRINT_JOB_FAILED_description','PRIVATE',NULL,'PRINT','core.alert.PRINT_JOB_FAILED_message',1,0,NULL,1,0);

--changeSet 0utl_alert_type:169 stripComments:false
/********************
** Warranty Alerts **
*********************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 123, 'core.alert.WARRANTY_VENDOR_LOC_OVERRIDE_name', 'core.alert.WARRANTY_VENDOR_LOC_OVERRIDE_description', 'ROLE', null, 'WARRANTY', 'core.alert.WARRANTY_VENDOR_LOC_OVERRIDE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:170 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 124, 'core.alert.WARRANTY_EVALUATION_FAILED_name', 'core.alert.WARRANTY_EVALUATION_FAILED_description', 'ROLE', null, 'WARRANTY', 'core.alert.WARRANTY_EVALUATION_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:171 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 234, 'core.alert.EXPIRED_WARRANTY_NOT_DEACTIVATED_name', 'core.alert.EXPIRED_WARRANTY_NOT_DEACTIVATED_description', 'ROLE', null, 'WARRANTY', 'core.alert.EXPIRED_WARRANTY_NOT_DEACTIVATED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:172 stripComments:false
/********************
** EMA Alerts **
*********************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 125, 'core.alert.EMA_WORKPACKAGE_CREATED_name', 'core.alert.EMA_WORKPACKAGE_CREATED_description', 'ROLE', null, 'EMA', 'core.alert.EMA_WORKPACKAGE_CREATED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:173 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 126, 'core.alert.EMA_WORKPACKAGE_UPDATED_name', 'core.alert.EMA_WORKPACKAGE_UPDATED_description', 'ROLE', null, 'EMA', 'core.alert.EMA_WORKPACKAGE_UPDATED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:174 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 127, 'core.alert.EMA_TASK_CREATED_name', 'core.alert.EMA_TASK_CREATED_description', 'ROLE', null, 'EMA', 'core.alert.EMA_TASK_CREATED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:175 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 128, 'core.alert.EMA_TASK_UPDATED_name', 'core.alert.EMA_TASK_UPDATED_description', 'ROLE', null, 'EMA', 'core.alert.EMA_TASK_UPDATED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:176 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 129, 'core.alert.EMA_FAULT_CREATED_name', 'core.alert.EMA_FAULT_CREATED_description', 'ROLE', null, 'EMA', 'core.alert.EMA_FAULT_CREATED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:177 stripComments:false
-- Part Transform
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 132, 'core.alert.TOBE_TRANSFORMED_NOT_PART_COMPATIBLE_name', 'core.alert.TOBE_TRANSFORMED_NOT_PART_COMPATIBLE_description', 'ROLE', null, 'TASK', 'core.alert.TOBE_TRANSFORMED_NOT_PART_COMPATIBLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:178 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 133, 'core.alert.TOBE_TRANSFORMED_NOT_TASK_COMPATIBLE_name', 'core.alert.TOBE_TRANSFORMED_NOT_TASK_COMPATIBLE_description', 'ROLE', null, 'TASK', 'core.alert.TOBE_TRANSFORMED_NOT_TASK_COMPATIBLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:179 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 134, 'core.alert.TOBE_TRANSFORMED_NOT_APPLICABLE_name', 'core.alert.TOBE_TRANSFORMED_NOT_APPLICABLE_description', 'ROLE', null, 'TASK', 'core.alert.TOBE_TRANSFORMED_NOT_APPLICABLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:180 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 135, 'core.alert.TOBE_TRANSFORMED_NOT_INTERCHANGEABLE_name', 'core.alert.TOBE_TRANSFORMED_NOT_INTERCHANGEABLE_description', 'ROLE', null, 'TASK', 'core.alert.TOBE_TRANSFORMED_NOT_INTERCHANGEABLE_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:181 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 136, 'core.alert.TOBE_TRANSFORMED_NOT_APPROVED_name', 'core.alert.TOBE_TRANSFORMED_NOT_APPROVED_description', 'ROLE', null, 'TASK', 'core.alert.TOBE_TRANSFORMED_NOT_APPROVED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:182 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 137, 'core.alert.TOBE_TRANSFORMED_NOT_APPROVED_IN_GROUP_name', 'core.alert.TOBE_TRANSFORMED_NOT_APPROVED_IN_GROUP_description', 'ROLE', null, 'TASK', 'core.alert.TOBE_TRANSFORMED_NOT_APPROVED_IN_GROUP_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:183 stripComments:false
/**********************
** Quarantine Alerts **
***********************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 155, 'core.alert.ASSIGNED_USER_TO_QUARANTINE_CORRECTIVE_ACTION_name', 'core.alert.ASSIGNED_USER_TO_QUARANTINE_CORRECTIVE_ACTION_description', 'PRIVATE', null, 'QUARANTINE', 'core.alert.ASSIGNED_USER_TO_QUARANTINE_CORRECTIVE_ACTION_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:184 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 156, 'core.alert.UNASSIGNED_USER_FROM_QUARANTINE_CORRECTIVE_ACTION_name', 'core.alert.UNASSIGNED_USER_FROM_QUARANTINE_CORRECTIVE_ACTION_description', 'PRIVATE', null, 'QUARANTINE', 'core.alert.UNASSIGNED_USER_FROM_QUARANTINE_CORRECTIVE_ACTION_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:185 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 157, 'core.alert.ASSIGNED_DEPARTMENT_TO_QUARANTINE_CORRECTIVE_ACTION_name', 'core.alert.ASSIGNED_DEPARTMENT_TO_QUARANTINE_CORRECTIVE_ACTION_description', 'CUSTOM', 'com.mxi.mx.core.plugin.alert.department.DepartmentFilterRule', 'QUARANTINE', 'core.alert.ASSIGNED_DEPARTMENT_TO_QUARANTINE_CORRECTIVE_ACTION_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:186 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 158, 'core.alert.UNASSIGNED_DEPARTMENT_FROM_QUARANTINE_CORRECTIVE_ACTION_name', 'core.alert.UNASSIGNED_DEPARTMENT_FROM_QUARANTINE_CORRECTIVE_ACTION_description', 'CUSTOM', 'com.mxi.mx.core.plugin.alert.department.DepartmentFilterRule', 'QUARANTINE', 'core.alert.UNASSIGNED_DEPARTMENT_FROM_QUARANTINE_CORRECTIVE_ACTION_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:187 stripComments:false
-- ****************************
-- ** Oil Consumption Alerts **
-- ****************************
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 160, 'core.alert.OIL_STATUS_ESCALATION_name', 'core.alert.OIL_STATUS_ESCALATION_description', 'ROLE', null, 'INVENTORY', 'core.alert.OIL_STATUS_ESCALATION_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:188 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 213, 'core.alert.INVALID_OIL_CONSUMPTION_name', 'core.alert.INVALID_OIL_CONSUMPTION_description', 'ROLE', null, 'INVENTORY', 'core.alert.INVALID_OIL_CONSUMPTION_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:199 stripComments:false
-- ********************
-- ** Oracle Job Engine **
-- *********************
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 210, 'core.alert.ORACLE_SCHEDULE_MODIFIED_name', 'core.alert.ORACLE_SCHEDULE_MODIFIED_description', 'ROLE', null, 'JOB', 'core.alert.ORACLE_SCHEDULE_MODIFIED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:200 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 211, 'core.alert.REMOVAL_REASON_UPDATED_name', 'core.alert.REMOVAL_REASON_UPDATED_description', 'ROLE', null, 'TASK', 'core.alert.REMOVAL_REASON_UPDATED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:201 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- Speck2K Spare Invoice Adapter alerts
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 223, 'core.alert.SPARE_INVOICE_VENDOR_CD_INVALID_name', 'core.alert.SPARE_INVOICE_VENDOR_CD_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_VENDOR_CD_INVALID_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:202 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 224, 'core.alert.SPARE_INVOICE_ORDER_N0_INVALID_name', 'core.alert.SPARE_INVOICE_ORDER_N0_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_ORDER_NO_INVALID_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:203 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 233, 'core.alert.SPARE_INVOICE_INVOICE_TYPE_CD_INVALID_name', 'core.alert.SPARE_INVOICE_INVOICE_TYPE_CD_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_INVOICE_TYPE_CD_INVALID_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:204 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 226, 'core.alert.SPARE_INVOICE_UOM_INVALID_name', 'core.alert.SPARE_INVOICE_UOM_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_UOM_INVALID_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:205 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 227, 'core.alert.SPARE_INVOICE_CURRENCY_CD_INVALID_name', 'core.alert.SPARE_INVOICE_CURRENCY_CD_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_CURRENCY_CD_INVALID_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:206 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 228, 'core.alert.SPARE_INVOICE_INCONSISTENT_UOM_name', 'core.alert.SPARE_INVOICE_INCONSISTENT_UOM_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_INCONSISTENT_UOM_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:207 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 229, 'core.alert.SPARE_INVOICE_INCONSISTENT_CURRENCY_CD_name', 'core.alert.SPARE_INVOICE_INCONSISTENT_CURRENCY_CD_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_INCONSISTENT_CURRENCY_CD_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:208 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 230, 'core.alert.SPARE_INVOICE_OTHER_CHARGE_CD_INVALID_name', 'core.alert.SPARE_INVOICE_OTHER_CHARGE_CD_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_OTHER_CHARGE_CD_INVALID_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:209 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 231, 'core.alert.SPARE_INVOICE_ORDER_STATUS_INVALID_name', 'core.alert.SPARE_INVOICE_ORDER_STATUS_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_ORDER_STATUS_INVALID_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:210 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 232, 'core.alert.SPARE_INVOICE_ORDER_LINE_N0_INVALID_name', 'core.alert.SPARE_INVOICE_ORDER_LINE_N0_INVALID_description', 'PRIVATE', null, 'PO', 'core.alert.SPARE_INVOICE_ORDER_LINE_NO_INVALID_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:211 stripComments:false
-- ********************
-- ** Logbook Adapter Alerts **
-- *********************
INSERT INTO utl_alert_type (alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id)
VALUES (170, 'core.alert.FLIGHT_NOT_FOUND_name', 'core.alert.FLIGHT_NOT_FOUND_description', 'ROLE', null, 'LOGBOOK', 'core.alert.FLIGHT_NOT_FOUND_message', 1, 0, null, 1, 0);

--changeSet 0utl_alert_type:212 stripComments:false
INSERT INTO utl_alert_type (alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id)
VALUES (172, 'core.alert.INVALID_LOGBOOK_PART_REQUIREMENT_name', 'core.alert.INVALID_LOGBOOK_PART_REQUIREMENT_description', 'ROLE', null, 'LOGBOOK', 'core.alert.INVALID_LOGBOOK_PART_REQUIREMENT_message', 1, 0, null, 1, 0);

--changeSet 0utl_alert_type:213 stripComments:false
INSERT INTO utl_alert_type (alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id)
VALUES (173, 'core.alert.REPETITIVE_TASK_name', 'core.alert.REPETITIVE_TASK_description', 'ROLE', null, 'LOGBOOK', 'core.alert.REPETITIVE_TASK_message', 1, 0, null, 1, 0);

--changeSet 0utl_alert_type:214 stripComments:false
INSERT INTO utl_alert_type (alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id)
VALUES (174, 'core.alert.CANCEL_REPETITIVE_TASK_name', 'core.alert.CANCEL_REPETITIVE_TASK_description', 'ROLE', null, 'LOGBOOK', 'core.alert.CANCEL_REPETITIVE_TASK_message', 1, 0, null, 1, 0);

--changeSet 0utl_alert_type:215 stripComments:false
/***********************
** LPA Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 235, 'core.alert.LPA_RUN_COMPLETE_NO_ISSUES_name', 'core.alert.LPA_RUN_COMPLETE_NO_ISSUES_description', 'ROLE', null, 'PLANNING', 'core.alert.LPA_RUN_COMPLETE_NO_ISSUES_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:216 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 236, 'core.alert.LPA_RUN_COMPLETE_WITH_ISSUES_name', 'core.alert.LPA_RUN_COMPLETE_WITH_ISSUES_description', 'ROLE', null, 'PLANNING', 'core.alert.LPA_RUN_COMPLETE_WITH_ISSUES_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:217 stripComments:false
/***********************
** Stock Level Check
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 237, 'core.alert.STOCK_LEVEL_CHECK_FAILED_name', 'core.alert.STOCK_LEVEL_CHECK_FAILED_description', 'ROLE', null, 'STOCK_LEVEL', 'core.alert.STOCK_LEVEL_CHECK_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:218 stripComments:false
/***********************
** System Failure Alerts
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 238, 'mxcommonejb.alert.WORK_ITEM_DISPATCHER_FAILED_name', 'mxcommonejb.alert.WORK_ITEM_DISPATCHER_FAILED_description', 'ROLE', null, 'SYSTEM', 'mxcommonejb.alert.WORK_ITEM_DISPATCHER_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:219 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 239, 'mxcommonejb.alert.WORK_ITEM_TYPE_DISPATCHER_FAILED_name', 'mxcommonejb.alert.WORK_ITEM_TYPE_DISPATCHER_FAILED_description', 'ROLE', null, 'SYSTEM', 'mxcommonejb.alert.WORK_ITEM_TYPE_DISPATCHER_FAILED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:220 stripComments:false
/***********************
** Adhoc Alert
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 241, 'core.alert.ADHOC_ALERT_name', 'core.alert.ADHOC_ALERT_description', 'ROLE', null, 'SYSTEM', 'core.alert.ADHOC_ALERT_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:221 stripComments:false
/***********************
** Data Volume Management Alert
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 242, 'mxcommonejb.alert.ALL_BLOB_IN_BLOB_DATA_ARE_PROCESSED_name', 'mxcommonejb.alert.ALL_BLOB_IN_BLOB_DATA_ARE_PROCESSED_description', 'ROLE', null, 'DVM', 'mxcommonejb.alert.ALL_BLOB_IN_BLOB_DATA_ARE_PROCESSED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:222 stripComments:false
/***********************
** ARC Message Alert
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 245, 'core.alert.ARC_MESSAGE_PROCESSED_SUCCESSFULLY_name', 'core.alert.ARC_MESSAGE_PROCESSED_SUCCESSFULLY_description', 'ROLE', null , 'ARC', 'core.alert.ARC_MESSAGE_PROCESSED_SUCCESSFULLY_message', 1, 0, null, 0, 0 );

--changeSet 0utl_alert_type:223 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 246,'core.alert.ARC_MESSAGE_PROCESSED_WITH_ERROR_OR_WARNINGS_name', 'core.alert.ARC_MESSAGE_PROCESSED_WITH_ERROR_OR_WARNINGS_description', 'ROLE', null, 'ARC', 'core.alert.ARC_MESSAGE_PROCESSED_WITH_ERROR_OR_WARNINGS_message', 1, 0, null, 0, 0 );

--changeSet 0utl_alert_type:224 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 247, 'core.alert.ARC_MESSAGE_ERROR_name', 'core.alert.ARC_MESSAGE_ERROR_description', 'ROLE', null, 'ARC', 'core.alert.ARC_MESSAGE_ERROR_message', 1, 1, null, 0, 0 );

--changeSet 0utl_alert_type:225 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES (248, 'core.alert.ARC_MESSAGE_SCHEMA_VALIDATION_ERROR_name', 'core.alert.ARC_MESSAGE_SCHEMA_VALIDATION_ERROR_description', 'ROLE', null, 'ARC', 'core.alert.ARC_MESSAGE_SCHEMA_VALIDATION_ERROR_message', 1, 1, null, 0, 0 );

--changeSet 0utl_alert_type:226 stripComments:false
/***********************
** Vendor Alert
************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 263, 'core.alert.VENDOR_CREATED_name', 'core.alert.VENDOR_CREATED_description', 'ROLE', null , 'VENDOR', 'core.alert.VENDOR_CREATED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:227 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 264, 'core.alert.VENDOR_UPDATED_name', 'core.alert.VENDOR_UPDATED_description', 'ROLE', null , 'VENDOR', 'core.alert.VENDOR_UPDATED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:228 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 265, 'core.alert.VENDOR_UNAPPROVED_name', 'core.alert.VENDOR_UNAPPROVED_description', 'ROLE', null , 'VENDOR', 'core.alert.VENDOR_UNAPPROVED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:229 stripComments:false
/***************************************
** Finance Adapter/API Message Alerts
****************************************/
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 615, 'integration.alert.ORD_INV_RET_MESSAGE_IS_NOT_PUBLISHED_name', 'integration.alert.ORD_INV_RET_MESSAGE_IS_NOT_PUBLISHED_description', 'ROLE', null, 'SHIPMENT', 'integration.alert.ORD_INV_RET_MESSAGE_IS_NOT_PUBLISHED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:230 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
VALUES ( 616, 'integration.alert.ORD_INV_REC_MESSAGE_IS_NOT_PUBLISHED_name', 'integration.alert.ORD_INV_REC_MESSAGE_IS_NOT_PUBLISHED_description', 'ROLE', null, 'INVENTORY', 'integration.alert.ORD_INV_REC_MESSAGE_IS_NOT_PUBLISHED_message', 1, 0, null, 1, 0 );

--changeSet 0utl_alert_type:269 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, active_bool, utl_id )
VALUES ( 269, 'core.alert.INVENTORY_NOT_REINDUCTED_name', 'core.alert.INVENTORY_NOT_REINDUCTED_description', 'ROLE', null, 'SHIPMENT', 'core.alert.INVENTORY_NOT_REINDUCTED_message', 1, 0, 1, 0 );

--changeSet 0utl_alert_type:272 stripComments:false
INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
VALUES( 272,'core.alert.BATCH_COMPLETE_TASKS_COMPLETED_name','core.alert.BATCH_COMPLETE_TASKS_COMPLETED_description','PRIVATE',NULL,'TASK','core.alert.BATCH_COMPLETE_TASKS_COMPLETED_message',1,0,NULL,1,0);

--changeSet 0utl_alert_type:273 stripComments:false
INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
VALUES( 273,'core.alert.BATCH_COMPLETE_TASKS_FAILED_name','core.alert.BATCH_COMPLETE_TASKS_FAILED_description','PRIVATE',NULL,'TASK','core.alert.BATCH_COMPLETE_TASKS_FAILED_message',1,0,NULL,1,0);

--changeSet 0utl_alert_type:274 stripComments:false
INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
VALUES(274,'core.alert.SYNC_USAGE_PARAMETERS_COMPLETED_name','core.alert.SYNC_USAGE_PARAMETERS_COMPLETED_description','PRIVATE',NULL,'INVENTORY','core.alert.SYNC_USAGE_PARAMETERS_COMPLETED_message',1,0,NULL,1,0);

--changeSet 0utl_alert_type:275 stripComments:false
INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
VALUES(275,'core.alert.SYNC_USAGE_PARAMETERS_FAILED_name','core.alert.SYNC_USAGE_PARAMETERS_FAILED_description','PRIVATE',NULL,'INVENTORY','core.alert.SYNC_USAGE_PARAMETERS_FAILED_message',1,0,NULL,1,0);

--changeSet 0utl_alert_type:276 stripComments:false
INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
VALUES(276,'core.alert.EVALUATE_LICENSE_DEFN_COMPLETED_name','core.alert.EVALUATE_LICENSE_DEFN_COMPLETED_description','PRIVATE',NULL,'LICENSEDEFN','core.alert.EVALUATE_LICENSE_DEFN_COMPLETED_message',1,0,NULL,1,0);

--changeSet 0utl_alert_type:277 stripComments:false
INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
VALUES(277,'core.alert.EVALUATE_LICENSE_DEFN_FAILED_name','core.alert.EVALUATE_LICENSE_DEFN_FAILED_description','PRIVATE',NULL,'LICENSEDEFN','core.alert.EVALUATE_LICENSE_DEFN_FAILED_message',1,0,NULL,1,0);
