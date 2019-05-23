--liquibase formatted sql


--changeSet MTX-1774:1 stripComments:false
-- JASPER REPORTS CONFIG PARAMETERS
-- Maintenance.ForecastOfAircraftMaintenance
-- ForecastOfAircraftMaintenance
UPDATE 
   utl_report_type 
SET 
   REPORT_NAME = 'Maintenance.AircraftDeferredDefects'
WHERE
   REPORT_NAME = 'Compliance.AircraftDeferredDefects'
;

--changeSet MTX-1774:2 stripComments:false
UPDATE 
   utl_report_type 
SET 
   REPORT_NAME = 'Maintenance.ForecastOfAircraftMaintenance'
WHERE
   REPORT_NAME = 'Compliance.ForecastOfAircraftMaintenance'
;