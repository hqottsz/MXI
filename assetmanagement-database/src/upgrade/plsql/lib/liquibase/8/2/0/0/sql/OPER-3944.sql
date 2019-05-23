--liquibase formatted sql


--changeSet OPER-3944:1 stripComments:false
UPDATE
   utl_trigger
SET
   class_name = 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger'
WHERE
   class_name = 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishExtSupChainTrigger'
   AND
   trigger_cd IN ('MX_PR_PRIORITY', 'MX_PR_EXTERNAL_REFERENCE', 'MX_PR_NEEDED_BY_DATE');

--changeSet OPER-3944:2 stripComments:false
UPDATE
   utl_trigger
SET
   class_name = 'com.mxi.mx.core.adapter.material.outgoing.cancelpartrequest.CancelPartRequestPublishExtSupChainTrigger'
WHERE
   class_name = 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishExtSupChainTrigger'
   AND
   trigger_cd IN ('MX_TS_CANCEL', 'MX_PR_CANCEL');

--changeSet OPER-3944:3 stripComments:false
UPDATE
   int_bp_lookup
SET
   namespace = 'http://xml.mxi.com/xsd/core/matadapter/cancel-part-request-request/1.0',
   root_name = 'cancel-part-request-request',
   ref_name = 'com.mxi.mx.core.adapter.material.outgoing.cancelpartrequest.CancelPartRequestEntryPointV1_0'
WHERE
   namespace = 'http://xml.mxi.com/xsd/core/matadapter/update-part-request-request/2.2'
   AND 
   root_name = 'update-part-request-request';

--changeSet OPER-3944:4 stripComments:false
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

--changeSet OPER-3944:5 stripComments:false
UPDATE 
   ASB_ADAPTER_DEST_LOOKUP 
SET 
   URL = 'jms://'
WHERE 
   NAMESPACE = 'http://xml.mxi.com/xsd/core/matadapter/part-request/2.2'
   AND 
   ROOT_NAME = 'part-request';