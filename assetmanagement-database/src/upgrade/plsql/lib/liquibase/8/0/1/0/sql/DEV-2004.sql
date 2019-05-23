--liquibase formatted sql


--changeSet DEV-2004:1 stripComments:false
-- Supply Chain Adapter
INSERT INTO INT_BP_LOOKUP (
   NAMESPACE, ROOT_NAME, REF_TYPE, 
   REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, 
   RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
)
SELECT 
   'http://xml.mxi.com/xsd/core/supplychain/get-upcoming-part-requests-request/1.0', 'get-upcoming-part-requests-request', 'JAVA',
   'com.mxi.mx.core.adapter.supplychain.GetUpcomingPartRequestsEntryPoint', 'process', 'FULL', 
   0, to_date('20-03-2013 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2013 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_BP_LOOKUP 
      WHERE 
         NAMESPACE = 'http://xml.mxi.com/xsd/core/supplychain/get-upcoming-part-requests-request/1.0'
   );