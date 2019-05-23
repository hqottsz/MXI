--liquibase formatted sql


--changeSet DEV-485:1 stripComments:false
-- Conditional insert
INSERT INTO
 utl_work_item_type
(
  name, worker_class, work_manager, enabled, utl_id
)
SELECT 'CALCULATE_AIRCRAFT_OPERATING_STATUS', 'com.mxi.mx.core.services.inventory.oper.CalculateAircraftOperatingStatusWorker', 'wm/Maintenix-CalculateAircraftOperatingStatusWorkManager', 1, 0
FROM
  dual
WHERE
  NOT EXISTS ( SELECT 1 FROM utl_work_item_type WHERE name = 'CALCULATE_AIRCRAFT_OPERATING_STATUS' );  