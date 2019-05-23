--liquibase formatted sql


--changeSet DEV-460:1 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99979, 'MX_TS_SCHEDULE_INTERNAL', 1, 'COMPONENT', 'part request by work order after internally schedule work package ', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99979);

--changeSet DEV-460:2 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99978, 'MX_TS_SCHEDULE_EXTERNAL', 1, 'COMPONENT', 'part request by work order after externally schedule work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99978);

--changeSet DEV-460:3 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99977, 'MX_TS_COMMIT', 1, 'COMPONENT', 'part request by work order after commit work work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99977);

--changeSet DEV-460:4 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99976, 'MX_TS_UNCOMMIT', 1, 'COMPONENT', 'part request by work order after uncommit work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99976);

--changeSet DEV-460:5 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99975, 'MX_TS_REQUEST_PARTS', 1, 'COMPONENT', 'part request by work order after request parts', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99975);

--changeSet DEV-460:6 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99974, 'MX_TS_CREATE', 1, 'COMPONENT', 'part request by task after initialize task definition which creats task on inventory', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99974);

--changeSet DEV-460:7 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99973, 'MX_TS_TASKASSIGN', 1, 'COMPONENT', 'part request by task after assign task to or unassign task from work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99973);

--changeSet DEV-460:8 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99972, 'MX_TS_ADD_PART_REQUIREMENT', 1, 'COMPONENT', 'part request by part requirement after add part requirement to task', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99972);

--changeSet DEV-460:9 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99971, 'MX_TS_COMMIT', 1, 'COMPONENT', 'part request by work order after commit work package', 'com.mxi.mx.core.adapter.services.material.partrequest.PartRequestOldMessagePublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99971);

--changeSet DEV-460:10 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99970, 'MX_TS_TASKASSIGN', 1, 'COMPONENT', 'part request by task after assign task to or unassign task from work package', 'com.mxi.mx.core.adapter.services.material.partrequest.PartRequestOldMessagePublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99970);

--changeSet DEV-460:11 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99969, 'MX_TS_ADD_PART_REQUIREMENT', 1, 'COMPONENT', 'part request by part requirement after add part requirement to task', 'com.mxi.mx.core.adapter.services.material.partrequest.PartRequestOldMessagePublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99969);

--changeSet DEV-460:12 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99968, 'MX_TS_SCHEDULE_INTERNAL', 1, 'COMPONENT', 'part request by work order after internally schedule work package', 'com.mxi.mx.core.adapter.services.material.partrequest.PartRequestOldMessagePublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99968);

--changeSet DEV-460:13 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99967, 'MX_TS_SCHEDULE_EXTERNAL', 1, 'COMPONENT', 'part request by work order after externally schedule work package', 'com.mxi.mx.core.adapter.services.material.partrequest.PartRequestOldMessagePublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99967);

--changeSet DEV-460:14 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/core/matadapter/part-request-request/1.0', 'part-request-request', 'JAVA', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestEntryPoint', 'coordinate', 0, to_date('07-06-2010 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-06-2009 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM int_bp_lookup WHERE int_bp_lookup.namespace = 'http://xml.mxi.com/xsd/core/matadapter/part-request-request/1.0');

--changeSet DEV-460:15 stripComments:false
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 219, 'core.alert.PART_REQUEST_MESSAGE_BUILD_ERROR_name', 'core.alert.PART_REQUEST_MESSAGE_BUILD_ERROR_description', 'ROLE', 'com.mxi.mx.core.plugin.alert.inventory.InventoryAuthorityFilterRule', 'REQ', 'core.alert.PART_REQUEST_MESSAGE_BUILD_ERROR_message', 1, 0, null, 1, 0 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_alert_type WHERE utl_alert_type.alert_type_id = 219);