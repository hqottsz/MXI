--liquibase formatted sql


--changeSet DEV-1562:1 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/procurement/1.0', 'sendRequestForVendorApprovalUpdate', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'sendVendorApprovalUpdated', 'FULL', 0, to_date('03-05-2012 08:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('03-05-2012 08:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM dual
WHERE NOT EXISTS (SELECT  1 FROM int_bp_lookup WHERE namespace = 'http://xml.mxi.com/xsd/procurement/1.0' AND root_name ='sendRequestForVendorApprovalUpdate');

--changeSet DEV-1562:2 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99955, 'MX_VENDOR_APPROVAL_UPDATE', 1, 'COMPONENT', 'Vendor Approval Update', 'com.mxi.mx.core.adapter.procurement.trg.SendRequestForVendorApprovalUpdate', 0, 0
FROM dual
WHERE NOT EXISTS (SELECT  1 FROM utl_trigger WHERE TRIGGER_CD = 'MX_VENDOR_APPROVAL_UPDATE' AND CLASS_NAME ='com.mxi.mx.core.adapter.procurement.trg.SendRequestForVendorApprovalUpdate');