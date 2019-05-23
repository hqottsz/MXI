--liquibase formatted sql


--changeSet DEV-1560:1 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/procurement/1.0', 'sendRequestForVendorCreation', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'sendVendorCreation', 'FULL', 0, to_date('27-04-2012 08:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-04-2012 08:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT  1 FROM int_bp_lookup WHERE namespace = 'http://xml.mxi.com/xsd/procurement/1.0' AND root_name ='sendRequestForVendorCreation');

--changeSet DEV-1560:2 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/procurement/update_vendor_external_key/1.0', 'update_vendor_external_key', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'processVendorExtKey', 'FULL', 0, to_date('27-04-2012 08:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-05-2012 14:00:52', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT  1 FROM int_bp_lookup WHERE namespace = 'http://xml.mxi.com/xsd/procurement/update_vendor_external_key/1.0' AND root_name ='update_vendor_external_key');

--changeSet DEV-1560:3 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99956, 'MX_VENDOR_CREATE', 1, 'COMPONENT', 'Vendor Creation', 'com.mxi.mx.core.adapter.procurement.trg.SendRequestForVendorCreation', 0, 0
FROM dual
WHERE NOT EXISTS (SELECT  1 FROM utl_trigger WHERE TRIGGER_CD = 'MX_VENDOR_CREATE' AND CLASS_NAME ='com.mxi.mx.core.adapter.procurement.trg.SendRequestForVendorCreation');