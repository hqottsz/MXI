--liquibase formatted sql


--changeSet aopr_airport_inv_rfi_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_airport_inv_rfi_v1 
AS
SELECT
 -- find all serviceable (RFI) inventory located in the airport and its sublocations
      acor_location_v1.airport_alt_id,
      acor_location_v1.location_id,
      acor_inv_info_v1.inventory_id,
      acor_inv_info_v1.part_id,
      acor_inv_info_v1.inventory_condition_code
  FROM
      acor_inv_info_v1
      INNER JOIN acor_location_v1 ON
      acor_inv_info_v1.location_id = acor_location_v1.location_id
  WHERE
      acor_location_v1.airport_alt_id IS NOT NULL
      AND
      acor_inv_info_v1.inventory_condition_code = 'RFI'
;