--liquibase formatted sql


--changeSet OPER-3152:1 stripComments:false
-- Insert triggers for firing off update-part-request message v2.2 
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99928, 'MX_TS_CANCEL', 1, 'COMPONENT', 'update part request by part requirement after cancel task', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishExtSupChainTrigger', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99928 AND trigger_cd = 'MX_TS_CANCEL');

--changeSet OPER-3152:2 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99927, 'MX_PR_CANCEL', 1, 'COMPONENT', 'update part request by part requirement after cancel part requirement', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishExtSupChainTrigger', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99927 AND trigger_cd = 'MX_PR_CANCEL');

--changeSet OPER-3152:3 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99926, 'MX_PR_PRIORITY', 1, 'COMPONENT', 'update part request by part requirement after edit request priority', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishExtSupChainTrigger', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99926 AND trigger_cd = 'MX_PR_PRIORITY');

--changeSet OPER-3152:4 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99925, 'MX_PR_EXTERNAL_REFERENCE', 1, 'COMPONENT', 'update part request by part requirement after edit external reference', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishExtSupChainTrigger', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99925 AND trigger_cd = 'MX_PR_EXTERNAL_REFERENCE');

--changeSet OPER-3152:5 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99924, 'MX_PR_NEEDED_BY_DATE', 1, 'COMPONENT', 'update part request by part requirement after edit needed by date', 
'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishExtSupChainTrigger', 0, 0
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM utl_trigger WHERE trigger_id = 99924 AND trigger_cd = 'MX_PR_NEEDED_BY_DATE');

--changeSet OPER-3152:6 stripComments:false
-- insert entry in int_bp_lookup
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/core/matadapter/update-part-request-request/2.2', 'update-part-request-request', 'JAVA', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestEntryPointV2_2', 'process', 'FULL', 0, to_date('07-05-2015 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-05-2015 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS(SELECT 1 FROM INT_BP_LOOKUP WHERE NAMESPACE = 'http://xml.mxi.com/xsd/core/matadapter/update-part-request-request/2.2' AND ROOT_NAME = 'update-part-request-request');