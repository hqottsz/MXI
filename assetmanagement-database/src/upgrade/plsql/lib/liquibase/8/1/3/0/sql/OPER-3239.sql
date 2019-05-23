--liquibase formatted sql


--changeSet OPER-3239:1 stripComments:false
-- part request outbound message
/********************************************
** INSERT SCRIPT FOR TABLE "ASB_ADAPTER_DEST_LOOKUP"
** 0-Level
** DATE: 05/20/2015
*********************************************/
INSERT INTO ASB_ADAPTER_DEST_LOOKUP 
   (NAMESPACE, ROOT_NAME, URL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   SELECT
      'http://xml.mxi.com/xsd/core/matadapter/part-request/2.2', 'part-request', 'jms://com.mxi.mx.jms.integration.supplychain.AsbInboundRequestQueue?connector=scJmsConnector', 0, to_date('20-05-2015 14:09:45', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-05-2015 14:09:02', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
   FROM
      DUAL
   WHERE
      NOT EXISTS
         (SELECT 1 
            FROM 
               ASB_ADAPTER_DEST_LOOKUP 
            WHERE 
               NAMESPACE = 'http://xml.mxi.com/xsd/core/matadapter/part-request/2.2'
            AND 
               ROOT_NAME = 'part-request'
            AND
               URL = 'jms://com.mxi.mx.jms.integration.supplychain.AsbInboundRequestQueue?connector=scJmsConnector'); 