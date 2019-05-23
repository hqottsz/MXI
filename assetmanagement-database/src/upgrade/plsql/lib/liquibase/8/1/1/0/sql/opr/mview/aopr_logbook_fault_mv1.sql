--liquibase formatted sql


--changeSet aopr_logbook_fault_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_LOGBOOK_FAULT_MV1');
END;
/

--changeSet aopr_logbook_fault_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_logbook_fault_mv1
BUILD DEFERRED
REFRESH FORCE ON DEMAND
AS
SELECT 
   aircraft.assmbly_code,
   aircraft.operator_code,
   logbook_fault.registration_code,
   aircraft.part_number,
   aircraft.oem_serial_number,
   logbook_fault.logbook_reference,
   logbook_fault.fault_description,
   SUBSTR(logbook_fault.config_slot_code,1,2) AS ata_chapter,
   SUBSTR(logbook_fault.config_slot_code,4,2) AS ata_section,
   logbook_fault.found_on_date,
   logbook_fault.spec2k_fault_source,
   logbook_fault.fault_source,
   logbook_fault.deferral_class,
   logbook_fault.closed_date,
   acor_fault_action_v1.action_description,
   logbook_fault.deferral_reference,
   logbook_fault.flight_number,
   logbook_fault.departure_airport,
   logbook_fault.arrival_airport,   
   logbook_fault.barcode fault_code, 
   logbook_fault.jic_code, 
   logbook_fault.jic_name, 
   logbook_fault.req_code, 
   logbook_fault.req_name, 
   logbook_fault.block_code, 
   logbook_fault.block_name, 
   CASE removal_bool
     WHEN 1 THEN
        logbook_fault.barcode
   END removal_code, 
   logbook_fault.work_order, 
   logbook_fault.doc_reference, 
   logbook_fault.mechanic, 
   logbook_fault.certifier, 
   logbook_fault.inspector,
   zone_list.zone_list,
   --
   logbook_fault.fault_status_code
FROM 
   acor_logbook_fault_v1 logbook_fault
   LEFT JOIN acor_sched_zone_list_v1 zone_list ON
      logbook_fault.sched_id    = zone_list.sched_id
   LEFT JOIN acor_inv_aircraft_v1 aircraft ON 
      logbook_fault.aircraft_id = aircraft.aircraft_id
   LEFT JOIN acor_fault_action_v1 ON 
      logbook_fault.sched_id           = acor_fault_action_v1.sched_id AND 
      acor_fault_action_v1.cancel_flag = 0;      