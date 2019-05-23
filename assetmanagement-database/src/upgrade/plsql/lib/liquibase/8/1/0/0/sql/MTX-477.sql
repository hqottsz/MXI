--liquibase formatted sql


--changeSet MTX-477:1 stripComments:false
--
-- Remove entries from utl_trigger and int_bp_lookup
--
-- MTX-477
-- Remove old logic for sending part request version 1.0 and update part request version 1.0 messages
-- 
DELETE FROM
   utl_trigger
WHERE 
   trigger_cd IN ('MX_TS_COMMIT', 'MX_TS_TASKASSIGN', 'MX_TS_ADD_PART_REQUIREMENT', 'MX_TS_SCHEDULE_INTERNAL', 'MX_TS_SCHEDULE_EXTERNAL') AND
   class_name = 'com.mxi.mx.core.adapter.services.material.partrequest.PartRequestOldMessagePublishTrigger';   

--changeSet MTX-477:2 stripComments:false
DELETE FROM
   int_bp_lookup
WHERE
   namespace = 'http://xml.mxi.com/xsd/core/matadapter/update_part_request/1.0' AND
   root_name = 'update_part_request';   

--changeSet MTX-477:3 stripComments:false
DELETE FROM
   int_bp_lookup
WHERE
   namespace = 'http://xml.mxi.com/xsd/core/matadapter/part_request/1.0' AND
   root_name = 'part_request';   

--changeSet MTX-477:4 stripComments:false
DELETE FROM
   int_bp_lookup
WHERE
   namespace = 'http://xml.mxi.com/xsd/core/matadapter/send_part_request_wo/1.0' AND
   root_name = 'send_part_request_wo';   

--changeSet MTX-477:5 stripComments:false
DELETE FROM
   int_bp_lookup
WHERE
   namespace = 'http://xml.mxi.com/xsd/core/matadapter/send_part_request_task/1.0' AND
   root_name = 'send_part_request_task';   

--changeSet MTX-477:6 stripComments:false
DELETE FROM
   int_bp_lookup
WHERE
   namespace = 'http://xml.mxi.com/xsd/core/matadapter/send_part_request_reqpart/1.0' AND
   root_name = 'send_part_request_reqpart';