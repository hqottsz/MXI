--liquibase formatted sql


--changeSet MX-27060:1 stripComments:false
INSERT INTO int_bp_lookup (
   namespace, root_name,
   ref_type, ref_name, method_name,
   int_logging_type_cd,
   rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
)
SELECT
   'http://xml.mxi.com/xsd/core/flight/work-package-request/1.1', 'work-package-request',
   'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.wostatus.WorkOrderStatusEntryPoint11', 'process',
   'FULL',
   0, to_date('09-10-2012 13:40:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('09-10-2012 13:40:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM
         int_bp_lookup
      WHERE
         namespace = 'http://xml.mxi.com/xsd/core/flight/work-package-request/1.1' AND
         root_name = 'work-package-request'
   )
;

--changeSet MX-27060:2 stripComments:false
UPDATE
   int_bp_lookup
SET
   ref_name = 'com.mxi.mx.core.adapter.flight.messages.flight.publish.wostatus.WorkOrderStatusEntryPoint10'
WHERE
   ref_name = 'com.mxi.mx.core.adapter.flight.messages.flight.publish.publisher.wostatus.WorkOrderStatusEntryPoint10'
;

--changeSet MX-27060:3 stripComments:false
UPDATE
   utl_trigger
SET
   class_name = 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.WOStatus11PublishTrigger'
WHERE
   class_name = 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.WOStatusPublishTrigger'
;