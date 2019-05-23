--liquibase formatted sql


--changeSet OPER-19092:1 stripComments:false
-- Insert new entry to int_bp_lookup
INSERT INTO int_bp_lookup (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/core/matadapter/part-request-request/2.3', 'part-request-request', 'JAVA', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestEntryPointV2_3', 'process', 'FULL', 0, to_date('02-02-2018 11:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('02-02-2018 11:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM int_bp_lookup WHERE namespace = 'http://xml.mxi.com/xsd/core/matadapter/part-request-request/2.3' AND root_name = 'part-request-request');


--changeSet OPER-19092:2 stripComments:false
-- insert MX_TS_SCHEDULE_INTERNAL trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name,	active_bool, utl_id)
SELECT 99911, 'MX_TS_SCHEDULE_INTERNAL', 1, 'COMPONENT', 'publish part request message by work order after internally schedule work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99911);

--changeSet OPER-19092:3 stripComments:false
-- insert MX_TS_SCHEDULE_EXTERNAL trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name,	active_bool, utl_id)
SELECT 99910, 'MX_TS_SCHEDULE_EXTERNAL', 1, 'COMPONENT', 'publish part request message by work order after externally schedule work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99910);

--changeSet OPER-19092:4 stripComments:false
-- insert MX_TS_COMMIT trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name,	active_bool, utl_id)
SELECT 99909, 'MX_TS_COMMIT', 1, 'COMPONENT', 'publish part request message by work order after commit work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99909);

--changeSet OPER-19092:5 stripComments:false
-- insert MX_TS_UNCOMMIT trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name,	active_bool, utl_id)
SELECT 99908, 'MX_TS_UNCOMMIT', 1, 'COMPONENT', 'publish part request message by work order after uncommit work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99908);

--changeSet OPER-19092:6 stripComments:false
-- insert MX_TS_REQUEST_PARTS trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name,	active_bool, utl_id)
SELECT 99907, 'MX_TS_REQUEST_PARTS', 1, 'COMPONENT', 'publish part request message by work order after request parts', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99907);

--changeSet OPER-19092:7 stripComments:false
-- insert MX_TS_CREATE trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name,	active_bool, utl_id)
SELECT 99906, 'MX_TS_CREATE', 1, 'COMPONENT', 'publish part request message by task after initialize task definition which creats task on inventory', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99906);

--changeSet OPER-19092:8 stripComments:false
-- insert MX_TS_TASKASSIGN trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name,	active_bool, utl_id)
SELECT 99905, 'MX_TS_TASKASSIGN', 1, 'COMPONENT', 'publish part request message by task after assign task to or unassign task from work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99905);

--changeSet OPER-19092:9 stripComments:false
-- insert MX_TS_ADD_PART_REQUIREMENT trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name,	active_bool, utl_id)
SELECT 99904, 'MX_TS_ADD_PART_REQUIREMENT', 1, 'COMPONENT', 'publish part request message by part requirement after add part requirement to task', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99904);

--changeSet OPER-19092:10 stripComments:false
-- insert MX_PR_PRIORITY trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name,	active_bool, utl_id)
SELECT 99903, 'MX_PR_PRIORITY', 1, 'COMPONENT', 'publish part request message by part requirement after edit request priority', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99903);

--changeSet OPER-19092:11 stripComments:false
-- insert MX_PR_EXTERNAL_REFERENCE trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name,	active_bool, utl_id)
SELECT 99902, 'MX_PR_EXTERNAL_REFERENCE', 1, 'COMPONENT', 'publish part request message by part requirement after edit external reference', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99902);

--changeSet OPER-19092:12 stripComments:false
-- insert MX_PR_NEEDED_BY_DATE trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name,	active_bool, utl_id)
SELECT 99901, 'MX_PR_NEEDED_BY_DATE', 1, 'COMPONENT', 'publish part request message by part requirement after edit needed by date', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99901);
