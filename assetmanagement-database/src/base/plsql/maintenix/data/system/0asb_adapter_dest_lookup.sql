--liquibase formatted sql


--changeSet 0asb_adapter_dest_lookup:1 stripComments:false
-- part request outbound message
/********************************************
** INSERT SCRIPT FOR TABLE "ASB_ADAPTER_DEST_LOOKUP"
** 0-Level
** DATE: 05/20/2015
*********************************************/
INSERT INTO ASB_ADAPTER_DEST_LOOKUP 
   (NAMESPACE, ROOT_NAME, URL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   SELECT
      'http://xml.mxi.com/xsd/core/matadapter/part-request/2.2', 'part-request', 'jms://', 0, to_date('20-05-2015 14:09:45', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-05-2015 14:09:02', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
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
          );               

--changeSet 0asb_adapter_dest_lookup:2 stripComments:false
-- cancel part request outbound message			   
INSERT INTO ASB_ADAPTER_DEST_LOOKUP 
   (NAMESPACE, ROOT_NAME, URL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   SELECT
      'http://xml.mxi.com/xsd/core/matadapter/cancel-part-request/1.0', 'cancel-part-request', 'jms://', 0, to_date('07-07-2015 14:09:45', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-07-2015 14:09:02', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
   FROM
      DUAL
   WHERE
      NOT EXISTS
         (SELECT 1
            FROM
               ASB_ADAPTER_DEST_LOOKUP
            WHERE
               NAMESPACE = 'http://xml.mxi.com/xsd/core/matadapter/cancel-part-request/1.0'
            AND
               ROOT_NAME = 'cancel-part-request'
		 );

--changeSet 0asb_adapter_dest_lookup:3 stripComments:false
-- work package coordination outbound message sending to MRO for integration
INSERT INTO ASB_ADAPTER_DEST_LOOKUP 
   (NAMESPACE, ROOT_NAME, URL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   SELECT
      'http://xml.mxi.com/xsd/core/integration/work-package-coordination/1.0', 'work-package-coordination', 'jms://com.mxi.mx.jms.integration.mro.IntegrationInboundRequestQueue?connector=mroJmsConnector', 0, to_date('20-07-2015 14:09:45', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-07-2015 14:09:02', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
   FROM
      DUAL
   WHERE
      NOT EXISTS
         (SELECT 1
            FROM
               ASB_ADAPTER_DEST_LOOKUP
            WHERE
               NAMESPACE = 'http://xml.mxi.com/xsd/core/integration/work-package-coordination/1.0'
            AND
               ROOT_NAME = 'work-package-coordination');

--changeSet 0asb_adapter_dest_lookup:4 stripComments:false
-- cancel work package outbound message sending to MRO for integration
INSERT INTO ASB_ADAPTER_DEST_LOOKUP 
   (NAMESPACE, ROOT_NAME, URL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   SELECT
      'http://xml.mxi.com/xsd/core/integration/cancel-work-package/1.0', 'cancel-work-package', 'jms://com.mxi.mx.jms.integration.mro.IntegrationInboundRequestQueue?connector=mroJmsConnector', 0, to_date('29-07-2015 14:09:45', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-07-2015 14:09:02', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
   FROM
      DUAL
   WHERE
      NOT EXISTS
         (SELECT 1
            FROM
               ASB_ADAPTER_DEST_LOOKUP
            WHERE
               NAMESPACE = 'http://xml.mxi.com/xsd/core/integration/cancel-work-package/1.0'
            AND
               ROOT_NAME = 'cancel-work-package');

--changeSet 0asb_adapter_dest_lookup:5 stripComments:false
-- assign line number outbound message sending to MRO for integration
INSERT INTO ASB_ADAPTER_DEST_LOOKUP 
   (NAMESPACE, ROOT_NAME, URL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   SELECT
      'http://xml.mxi.com/xsd/core/integration/assign-line-number/1.0', 'assign-line-number', 'jms://com.mxi.mx.jms.integration.mro.IntegrationInboundRequestQueue?connector=mroJmsConnector', 0, to_date('06-11-2015 16:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('06-11-2015 16:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
   FROM
      DUAL
   WHERE
      NOT EXISTS
         (SELECT 1
            FROM
               ASB_ADAPTER_DEST_LOOKUP
            WHERE
               NAMESPACE = 'http://xml.mxi.com/xsd/core/integration/assign-line-number/1.0'
            AND
               ROOT_NAME = 'assign-line-number');