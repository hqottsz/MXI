--liquibase formatted sql


--changeSet acor_inv_serial_cntrl_asset_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW ACOR_INV_SERIAL_CNTRL_ASSET_V1
AS
SELECT
   inventory_id,
   h_inventory_id,
   nh_inventory_id,
   part_id,
   part_number,
   part_number_name,
   oem_serial_number,
   NULL oem_batch_number,
   assmbl_bom_cd,
   position_code,
   manufact_date,
   manufacturer_code,
   received_date,
   inv_class_cd,
   owner_code,
   location_code,
   install_date,
   barcode,
   receive_condition_code,
   receive_condition_name,
   release_number_description,
   release_remarks,
   logcard_form_code,
   inventory_condition,
   po_line_id
FROM
   acor_inv_assembly_v1
UNION
SELECT
   inventory_id,
   h_inventory_id,
   nh_inventory_id,
   part_id,
   part_number,
   part_number_name,
   oem_serial_number,
   oem_batch_number,
   config_slot_code,
   position_code,
   manufact_date,
   manufacturer_code,
   received_date,
   'TRK',
   owner_code,
   location_code,
   install_date,
   barcode,
   receive_condition_code,
   receive_condition_name,
   release_number_description,
   release_remarks,
   logcard_form_code,
   inventory_condition,
   po_line_id
FROM
   acor_inv_tracked_v1
UNION
SELECT
   inventory_id,
   h_inventory_id,
   nh_inventory_id,
   part_id,
   part_number,
   part_number_name,
   oem_serial_number,
   NULL oem_batch_number,
   NULL assmbl_bom_cd,
   NULL position,
   manufact_date,
   manufacturer_code,
   received_date,
   'SER',
   owner_code,
   location_code,
   install_date,
   barcode,
   receive_condition_code,
   receive_condition_name,
   release_number_description,
   release_remarks,
   NULL  logcard_form_code,
   inventory_condition,
   po_line_id
FROM
   acor_inv_serialized_v1
;