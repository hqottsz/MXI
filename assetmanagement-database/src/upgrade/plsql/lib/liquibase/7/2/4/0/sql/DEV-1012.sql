--liquibase formatted sql


--changeSet DEV-1012:1 stripComments:false
-- Data Changes
-- Add 0 level data to INT_BP_LOOKUP
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'noNamespace', 'ATA_DSE_Logbook', 'EJB', 'com.mxi.mx.core.ejb.ELA', 'coordinate', 'FULL', 0, to_date('04-05-2011 10:30:30', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-05-2011 10:30:30', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM INT_BP_LOOKUP WHERE NAMESPACE = 'noNamespace' AND ROOT_NAME = 'ATA_DSE_Logbook');