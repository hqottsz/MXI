--liquibase formatted sql


--changeSet OPER-19112:1 stripComments:false
-- Insert new entry to int_bp_lookup
INSERT INTO int_bp_lookup (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/core/matadapter/cancel-part-request-request/1.1', 'cancel-part-request-request', 'JAVA', 'com.mxi.mx.core.adapter.material.outgoing.cancelpartrequest.CancelPartRequestEntryPointV1_1', 'process', 'FULL', 0, to_date('07-02-2018 11:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-02-2018 11:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM int_bp_lookup WHERE namespace = 'http://xml.mxi.com/xsd/core/matadapter/cancel-part-request-request/1.1' AND root_name = 'cancel-part-request-request');

--changeSet OPER-19112:2 stripComments:false
-- insert MX_TS_CANCEL trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name,	active_bool, utl_id)
SELECT 99900, 'MX_TS_CANCEL', 1, 'COMPONENT', 'publish cancel part request message by part requirement after cancel task', 'com.mxi.mx.core.adapter.material.outgoing.cancelpartrequest.CancelPartRequestPublishTriggerV1_1', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99900);

--changeSet OPER-19112:3 stripComments:false
-- insert MX_PR_CANCEL trigger
INSERT INTO utl_trigger( trigger_id, trigger_cd, exec_order, type_cd, trigger_name, class_name,	active_bool, utl_id)
SELECT 99899, 'MX_PR_CANCEL', 1, 'COMPONENT', 'publish cancel part request message by part requirement after cancel part requirement', 'com.mxi.mx.core.adapter.material.outgoing.cancelpartrequest.CancelPartRequestPublishTriggerV1_1', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99899);
