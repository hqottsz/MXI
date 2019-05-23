--liquibase formatted sql


--changeSet OPER-2943:1 stripComments:false
-- insert MX_TS_SCHEDULE_INTERNAL trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name,	active_bool, utl_id)
SELECT 99936, 'MX_TS_SCHEDULE_INTERNAL', 1, 'COMPONENT', 'part request by work order after internally schedule work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99936 AND	trigger_cd = 'MX_TS_SCHEDULE_INTERNAL');	

--changeSet OPER-2943:2 stripComments:false
-- Insert MX_TS_SCHEDULE_EXTERNAL trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name, active_bool, utl_id)
SELECT 99935, 'MX_TS_SCHEDULE_EXTERNAL', 1, 'COMPONENT', 'part request by work order after externally schedule work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99935 AND trigger_cd = 'MX_TS_SCHEDULE_EXTERNAL');	

--changeSet OPER-2943:3 stripComments:false
-- Insert MX_TS_COMMIT trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name, active_bool, utl_id)
SELECT 99934, 'MX_TS_COMMIT', 1, 'COMPONENT', 'part request by work order after commit work work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0
FROM dual 
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99934 AND trigger_cd = 'MX_TS_COMMIT');	

--changeSet OPER-2943:4 stripComments:false
-- Insert MX_TS_UNCOMMIT trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name, active_bool, utl_id)
SELECT 99933, 'MX_TS_UNCOMMIT', 1, 'COMPONENT', 'part request by work order after uncommit work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99933 AND trigger_cd = 'MX_TS_UNCOMMIT');	

--changeSet OPER-2943:5 stripComments:false
-- Insert MX_TS_REQUEST_PARTS trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name, active_bool, utl_id)
SELECT 99932, 'MX_TS_REQUEST_PARTS', 1, 'COMPONENT', 'part request by work order after request parts','com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99932 AND trigger_cd = 'MX_TS_REQUEST_PARTS');	

--changeSet OPER-2943:6 stripComments:false
-- Insert MX_TS_CREATE trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name, active_bool, utl_id)
SELECT 99931, 'MX_TS_CREATE', 1, 'COMPONENT', 'part request by task after initialize task definition which creats task on inventory', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0
FROM dual 
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99931 AND trigger_cd = 'MX_TS_CREATE');	

--changeSet OPER-2943:7 stripComments:false
-- Insert MX_TS_TASKASSIGN trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name, active_bool, utl_id)
SELECT 99930, 'MX_TS_TASKASSIGN', 1, 'COMPONENT', 'part request by task after assign task to or unassign task from work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99930 AND trigger_cd = 'MX_TS_TASKASSIGN');	

--changeSet OPER-2943:8 stripComments:false
-- Insert MX_TS_ADD_PART_REQUIREMENT trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name, active_bool, utl_id)
SELECT 99929, 'MX_TS_ADD_PART_REQUIREMENT', 1, 'COMPONENT', 'part request by part requirement after add part requirement to task', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99929 AND trigger_cd = 'MX_TS_ADD_PART_REQUIREMENT');

--changeSet OPER-2943:9 stripComments:false
-- Insert new entry in int_bp_lookup
INSERT INTO int_bp_lookup (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/core/matadapter/part-request-request/2.2', 'part-request-request', 'JAVA', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestEntryPointV2_2', 'process', 'FULL', 0, to_date('06-05-2015 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('06-05-2015 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM int_bp_lookup WHERE namespace = 'http://xml.mxi.com/xsd/core/matadapter/part-request-request/2.2' AND root_name = 'part-request-request');