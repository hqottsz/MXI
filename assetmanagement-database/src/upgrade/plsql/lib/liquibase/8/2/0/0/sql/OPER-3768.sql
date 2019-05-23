--liquibase formatted sql


--changeSet OPER-3768:1 stripComments:false
INSERT INTO INT_BP_LOOKUP (
   NAMESPACE,
   ROOT_NAME,
   REF_TYPE, 
   REF_NAME,
   METHOD_NAME,
   INT_LOGGING_TYPE_CD
)
SELECT 
   'http://xml.mxi.com/xsd/core/flight/work-package-request/1.2',
   'work-package-request',
   'JAVA', 
   'com.mxi.mx.core.adapter.flight.messages.flight.publish.wostatus.WorkOrderStatusEntryPoint12',
   'process',
   'FULL' 
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_BP_LOOKUP 
      WHERE 
         NAMESPACE = 'http://xml.mxi.com/xsd/core/flight/work-package-request/1.2'
   );

--changeSet OPER-3768:2 stripComments:false
UPDATE
   utl_trigger
SET
   class_name = 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.WOStatus12PublishTrigger'
WHERE
   class_name = 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.WOStatus11PublishTrigger'
;