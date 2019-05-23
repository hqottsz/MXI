--liquibase formatted sql


--changeSet aopr_actv_deferred_fault_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_actv_deferred_fault_v1
AS 
SELECT
   fault_id,
   sched_id,
   aircraft_id,
   logbook_reference,
   fault_name,
   event_type_code,
   fault_status_code,
   corr_task_status_code,
   fault_description,
   barcode,
   fault_severity,
   deferral_class,
   fault_source,
   deferral_reference,
   deferral_authorization,
   operational_restrictions,
   found_on_date,
   config_slot_code
FROM
   acor_logbook_fault_v1
WHERE
   event_type_code   = 'CF'
   AND
   fault_status_code = 'CFDEFER'
   AND
   corr_task_status_code    = 'ACTV'
;