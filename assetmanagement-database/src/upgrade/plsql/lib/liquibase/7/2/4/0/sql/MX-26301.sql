--liquibase formatted sql


--changeSet MX-26301:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- ADAPTER_XML_VALIDATION
-- Global switches for turning on validation for adapter messages against available schemas.
BEGIN
   utl_migr_data_pkg.config_parm_insert(
         'INCOMING_ADAPTER_XML_VALIDATION',
         'LOGIC',
         'This parameter is used enforce validation of incoming requests.',
         'GLOBAL',
         'TRUE/FALSE',
         'TRUE',
         1,
         'Integration',
         '8.0',
         0
      );
END;
/

--changeSet MX-26301:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
         'OUTGOING_ADAPTER_XML_VALIDATION',
         'LOGIC',
         'This parameter is used enforce validation of outgoing responses.',
         'GLOBAL',
         'TRUE/FALSE',
         'TRUE',
         1,
         'Integration',
         '8.0',
         0
      );
END;
/

--changeSet MX-26301:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- remove FINANCE_ADAPTER_XML_VALIDATION
BEGIN
   utl_migr_data_pkg.config_parm_delete('FINANCE_ADAPTER_XML_VALIDATION');
END;
/

--changeSet MX-26301:4 stripComments:false
-- coordinate method in EntryPoint interface was renamed to process for consitency
UPDATE int_bp_lookup SET method_name = 'process' 
WHERE 
   (namespace, root_name) IN (
      ('http://xml.mxi.com/xsd/core/faults/add-failure-effect/1.0', 'failure-effects'),
      ('http://xml.mxi.com/xsd/core/faults/define-failure-effect/1.0', 'define-failure-effects'),
      ('http://xml.mxi.com/xsd/core/faults/fault-definition/1.0', 'fault-definitions'),
      ('http://xml.mxi.com/xsd/core/faults/faults/1.0', 'faults'),
      ('http://xml.mxi.com/xsd/core/faults/mark-as-obsolete/1.0', 'obsolete-failure-definitions'),
      ('http://xml.mxi.com/xsd/core/flight/flight-disruptions/1.0', 'flight-disruptions'),
      ('http://xml.mxi.com/xsd/core/hr/user-licenses/1.0', 'user-licenses'),
      ('http://xml.mxi.com/xsd/core/hr/user-planned-attendance/1.0', 'user-planned-attendance'),
      ('http://xml.mxi.com/xsd/core/matadapter/inventory-issued-to/1.0', 'inventory-issued-to'),
      ('http://xml.mxi.com/xsd/core/matadapter/part-request-request/1.0', 'part-request-request'),
      ('http://xml.mxi.com/xsd/core/matadapter/update-part-request-request/1.0', 'update-part-request-request'),
      ('http://xml.mxi.com/xsd/core/ppc/optimize/optimize-production-plan-request/1.0', 'optimize-production-plan-request'),
      ('http://xml.mxi.com/xsd/core/ppc/optimize/optimize-production-plan-result/1.0', 'optimize-production-plan-result'),
      ('http://xml.mxi.com/xsd/core/ppc/optimize/optimize-production-plan-status/1.0', 'optimize-production-plan-status'),
      ('http://xml.mxi.com/xsd/core/procurement/ATA_SparesInvoice/1.0', 'ATA_SparesInvoice'),
      ('http://xml.mxi.com/xsd/procurement/1.0', 'MxPartPrices')
   );   

--changeSet MX-26301:5 stripComments:false
-- aircraft-status-request
UPDATE int_bp_lookup 
SET 
   ref_name = 'com.mxi.mx.core.adapter.flight.messages.flight.publish.aircraftstatus.AircraftStatusEntryPoint10', 
   method_name = 'process' 
WHERE
   namespace = 'http://xml.mxi.com/xsd/core/flight/aircraft-status-request/1.0'
   AND
   root_name = 'aircraft-status-request';   

--changeSet MX-26301:6 stripComments:false
-- earliest-deadlines-request v1.0
UPDATE int_bp_lookup 
SET 
   ref_name = 'com.mxi.mx.core.adapter.flight.messages.flight.publish.earliestdeadlines.EarliestDeadlinesEntryPoint10', 
   method_name = 'process' 
WHERE
   namespace = 'http://xml.mxi.com/xsd/core/flight/earliest-deadlines-request/1.0'
   AND
   root_name = 'earliest-deadlines-request';   

--changeSet MX-26301:7 stripComments:false
-- earliest-deadlines-request v1.1
UPDATE int_bp_lookup 
SET 
   ref_name = 'com.mxi.mx.core.adapter.flight.messages.flight.publish.earliestdeadlines.EarliestDeadlinesEntryPoint11', 
   method_name = 'process' 
WHERE
   namespace = 'http://xml.mxi.com/xsd/core/flight/earliest-deadlines-request/1.1'
   AND
   root_name = 'earliest-deadlines-request';   

--changeSet MX-26301:8 stripComments:false
-- fault-request
UPDATE int_bp_lookup 
SET 
   ref_name = 'com.mxi.mx.core.adapter.flight.messages.flight.publish.faultstatus.FaultStatusEntryPoint10', 
   method_name = 'process' 
WHERE
   namespace = 'http://xml.mxi.com/xsd/core/flight/fault-request/1.0'
   AND
   root_name = 'fault-request';   

--changeSet MX-26301:9 stripComments:false
-- flight-identifier-request
UPDATE int_bp_lookup 
SET 
   ref_name = 'com.mxi.mx.core.adapter.flight.messages.flight.publish.flightidentifier.FlightIdentifierEntryPoint10', 
   method_name = 'process' 
WHERE
   namespace = 'http://xml.mxi.com/xsd/core/flight/flight-identifier-request/1.0'
   AND
   root_name = 'flight-identifier-request';   

--changeSet MX-26301:10 stripComments:false
-- inventory-deadlines-updated-request
UPDATE int_bp_lookup 
SET 
   ref_name = 'com.mxi.mx.core.adapter.flight.messages.flight.publish.deadlinesupdate.DeadlinesUpdatedEntryPoint10', 
   method_name = 'process' 
WHERE
   namespace = 'http://xml.mxi.com/xsd/core/flight/inventory-deadlines-updated-request/1.0'
   AND
   root_name = 'inventory-deadlines-updated-request';   

--changeSet MX-26301:11 stripComments:false
-- next-due-maintenance-task-request
UPDATE int_bp_lookup 
SET 
   ref_name = 'com.mxi.mx.core.adapter.flight.messages.flight.publish.nextduemaintenance.NextDueMaintenanceEntryPoint10', 
   method_name = 'process' 
WHERE
   namespace = 'http://xml.mxi.com/xsd/core/flight/next-due-maintenance-task-request/1.0'
   AND
   root_name = 'next-due-maintenance-task-request';

--changeSet MX-26301:12 stripComments:false
-- work-package-request
UPDATE int_bp_lookup 
SET 
   ref_name = 'com.mxi.mx.core.adapter.flight.messages.flight.publish.publisher.wostatus.WorkOrderStatusEntryPoint10', 
   method_name = 'process' 
WHERE
   namespace = 'http://xml.mxi.com/xsd/core/flight/work-package-request/1.0'
   AND
   root_name = 'work-package-request';