--liquibase formatted sql


--changeSet MX-27601:1 stripComments:false
-- create_purchase_invoice_21
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
   'http://xml.mxi.com/xsd/core/finance/create_purchase_invoice/2.1', 
   'create_purchase_invoice', 
   'PROCESS', 
   'http://www.mxi.com/mx/xml/finance', 
   'create_purchase_invoice_21', 
   'FULL',
   0, 
   to_date('11-04-2013 09:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('11-04-2013 09:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
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
         NAMESPACE = 'http://xml.mxi.com/xsd/core/finance/create_purchase_invoice/2.1'
         AND
         ROOT_NAME = 'create_purchase_invoice'
   );

--changeSet MX-27601:2 stripComments:false
-- create_purchase_invoice_21
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
   'create_purchase_invoice_21', 
   'PROCESS', 
   '<?xml version="1.0"?>
     <proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
      <!--Service Processing-->
      <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdCreatePurchaseInvoice21" method="process"/>
    </proc:process>', 
   0, 
   to_date('11-04-2013 09:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('11-04-2013 09:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
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
         NAME = 'create_purchase_invoice_21'
   );