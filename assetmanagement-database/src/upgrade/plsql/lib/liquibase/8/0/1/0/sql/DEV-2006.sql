--liquibase formatted sql


--changeSet DEV-2006:1 stripComments:false
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
   'http://xml.mxi.com/xsd/core/finance/update_financial_accounts/1.0', 
   'update-financial-accounts-request', 
   'PROCESS', 
   'http://www.mxi.com/mx/xml/finance', 
   'updateFinancialAccountsRequest', 
   'FULL',
   0, 
   to_date('28-03-2013 19:03:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('28-03-2013 19:03:00', 'dd-mm-yyyy hh24:mi:ss'), 
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
         NAMESPACE = 'http://xml.mxi.com/xsd/core/finance/update_financial_accounts/1.0'
         AND
         ROOT_NAME = 'update-financial-accounts-request'
   );

--changeSet DEV-2006:2 stripComments:false
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
   'updateFinancialAccountsRequest', 
   'PROCESS', 
   '<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdUpdateFinancialAccountsRequest" method="process"/>
</proc:process>', 
   0, 
   to_date('28-03-2013 19:06:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('28-03-2013 19:06:00', 'dd-mm-yyyy hh24:mi:ss'), 
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
         NAME = 'updateFinancialAccountsRequest'
   );