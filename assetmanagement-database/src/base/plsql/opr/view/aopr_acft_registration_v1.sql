--liquibase formatted sql


--changeSet aopr_acft_registration_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_acft_registration_v1
AS
WITH rvw_acor_current_usage
AS
  (
    SELECT
      inventory_id,
      data_type_id,
      data_type_code,
      time_since_new_quantity
    FROM
      acor_inv_current_usage_v1
  )
SELECT
   inv_aircraft.aircraft_id,
   inv_aircraft.assmbly_code,
   inv_aircraft.registration_code,
   inv_aircraft.part_number,
   inv_aircraft.part_name,
   inv_aircraft.oem_serial_number,
   inv_aircraft.condition_code,
   inv_aircraft.oem_line_number,
   inv_aircraft.oem_var_number,
   inv_aircraft.operator_id,
   inv_aircraft.operator_code,
   inv_aircraft.organization_name,
   inv_aircraft.install_date,
   inv_aircraft.release_date,
   inv_aircraft.manufact_date,
   inv_aircraft.received_date,
   inv_aircraft.manufacturer_code,
   inv_aircraft.inv_oper_cd,
   inv_aircraft.applicability_range,
   inv_aircraft.capability_code,
   inv_aircraft.owner_code,
   inv_aircraft.regulatory_body,
   inv_aircraft.release_number_description,
   inv_aircraft.fin_number,
   inv_aircraft.airworth_cd,
   inv_aircraft.assembly_sub_type_code,
   usage_hour.time_since_new_quantity tsn,
   usage_cycle.time_since_new_quantity csn
FROM
   acor_inv_aircraft_v1 inv_aircraft
   -- hours
   INNER JOIN rvw_acor_current_usage usage_hour ON
      inv_aircraft.aircraft_id = usage_hour.inventory_id
      AND
      usage_hour.data_type_code = 'HOURS'
   -- cycles
   INNER JOIN rvw_acor_current_usage usage_cycle ON
      inv_aircraft.aircraft_id = usage_cycle.inventory_id
      AND
      usage_cycle.data_type_code = 'CYCLES'
;