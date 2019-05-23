--liquibase formatted sql


--changeSet DEV-2005:1 stripComments:false
-- create-financial-accounts-request
INSERT INTO INT_BP_LOOKUP (
   NAMESPACE, 
   ROOT_NAME, 
   REF_TYPE, 
   REF_NAME, 
   METHOD_NAME, 
   INT_LOGGING_TYPE_CD,
   RSTAT_CD, 
   CREATION_DT, 
   REVISION_DT, 
   REVISION_DB_ID, 
   REVISION_USER
)
SELECT 
   'http://xml.mxi.com/xsd/core/finance/create-financial-accounts-request/1.0', 
   'create-financial-accounts-request', 
   'PROCESS', 
   'http://www.mxi.com/mx/xml/finance', 
   'createFinancialAccountsRequest', 
   'FULL',
   0, 
   to_date('20-03-2013 19:03:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('20-03-2013 19:03:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_BP_LOOKUP 
      WHERE 
         NAMESPACE = 'http://xml.mxi.com/xsd/core/finance/create-financial-accounts-request/1.0'
         AND
         ROOT_NAME = 'create-financial-accounts-request'
   );

--changeSet DEV-2005:2 stripComments:false
-- create-financial-accounts-request
INSERT INTO INT_PROCESS (
   NAMESPACE, 
   NAME, 
   TYPE, 
   PROCESS,
   RSTAT_CD, 
   CREATION_DT, 
   REVISION_DT, 
   REVISION_DB_ID, 
   REVISION_USER
)
SELECT 
   'http://www.mxi.com/mx/xml/finance', 
   'createFinancialAccountsRequest', 
   'PROCESS', 
   '<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdCreateFinancialAccountsRequest" method="process"/>
</proc:process>', 
   0, 
   to_date('20-03-2013 19:06:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('20-03-2013 19:06:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_PROCESS 
      WHERE 
         NAMESPACE = 'http://www.mxi.com/mx/xml/finance'
         AND
         NAME = 'createFinancialAccountsRequest'
   );