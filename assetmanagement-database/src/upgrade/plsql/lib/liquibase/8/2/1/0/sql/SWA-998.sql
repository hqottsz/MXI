--liquibase formatted sql
  

--changeSet SWA-998:1 stripComments:false
-- insert the CmdDetailedFinancialLog30 process
/**************************************************************************************
* 
* SWA-998 Insert scripts for the addition of the Request Detailed Financial Log v3.0 
*
***************************************************************************************/
INSERT INTO int_process ( namespace, 
                          name, 
                          type, 
                          process, 
                          rstat_cd, 
                          creation_dt, 
                          revision_dt, 
                          revision_db_id, 
                          revision_user)
SELECT
   'http://www.mxi.com/mx/xml/finance', 
   'request-detailed-inventory-financial-log-30', 
   'PROCESS',
   '<?xml version="1.0"?>
   <proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdDetailedFinancialLog30" method="process"/>
   </proc:process>',
   0, 
   TO_DATE('01-06-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   TO_DATE('01-06-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, '
   MXI'
FROM
   dual
WHERE 
   NOT EXISTS ( 
      SELECT 1
        FROM int_process
       WHERE namespace = 'http://www.mxi.com/mx/xml/finance'      
             AND
             name = 'request-detailed-inventory-financial-log-30'
   );   

--changeSet SWA-998:2 stripComments:false
-- Add the bp_lookup value
INSERT INTO int_bp_lookup (namespace, 
                           root_name, 
                           ref_type, 
                           ref_name, 
                           method_name, 
                           int_logging_type_cd, 
                           rstat_cd, 
                           creation_dt, 
                           revision_dt, 
                           revision_db_id,
                           revision_user)
SELECT 
   'http://xml.mxi.com/xsd/core/finance/request-detailed-inventory-financial-log/3.0',
   'request-detailed-inventory-financial-log',
   'PROCESS',
   'http://www.mxi.com/mx/xml/finance',
   'request-detailed-inventory-financial-log-30', 
   'FULL', 
   0, 
   TO_DATE('01-06-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   TO_DATE('01-06-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM 
   dual
WHERE
   NOT EXISTS (
      SELECT 1
        FROM int_bp_lookup
       WHERE namespace = 'http://xml.mxi.com/xsd/core/finance/request-detailed-inventory-financial-log/3.0'
             AND
             root_name = 'request-detailed-inventory-financial-log'
   );