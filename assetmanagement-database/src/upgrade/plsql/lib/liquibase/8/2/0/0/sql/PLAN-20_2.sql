--liquibase formatted sql


--changeSet PLAN-20_2:1 stripComments:false
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