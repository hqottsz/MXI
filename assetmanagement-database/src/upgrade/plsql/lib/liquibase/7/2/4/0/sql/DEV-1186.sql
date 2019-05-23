--liquibase formatted sql


--changeSet DEV-1186:1 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- migration for ATA Spares Invoice Adapter
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
INSERT INTO int_bp_lookup ( NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/core/procurement/ATA_SparesInvoice/1.0','ATA_SparesInvoice','JAVA', 'com.mxi.mx.core.adapter.procurement.atasparesinvoice.SparesInvoiceEntryPoint10', 'coordinate', 'FULL', 0, to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM INT_BP_LOOKUP WHERE NAMESPACE = 'http://xml.mxi.com/xsd/core/procurement/ATA_SparesInvoice/1.0' AND ROOT_NAME = 'ATA_SparesInvoice');