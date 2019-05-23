--liquibase formatted sql


--changeSet aopr_financial_trx_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_financial_trx_v1
AS 
WITH rvw_inventory
AS (
   SELECT
      inventory_id,
      part_number,
      oem_serial_number,
      null AS oem_batch_number,
      manufacturer_code
   FROM acor_INV_SERIAL_CNTRL_ASSET_V1
   UNION
   SELECT
      aircraft_id,
      part_number,
      oem_serial_number,
      null AS oem_batch_number,
      manufacturer_code
   FROM
      acor_INV_AIRCRAFT_V1
   UNION
   SELECT
      inventory_id,
      part_number,
      oem_serial_number,
      oem_batch_number,
      manufacturer_code
   FROM
      acor_INV_BATCH_V1
      UNION
   SELECT
      inventory_id,
      part_number,
      oem_serial_number,
      null AS oem_batch_number,
      manufacturer_code
   FROM
      acor_INV_KIT_V1
),
rvw_wp_ac_info_issue_type
AS (
   SELECT
      acor_fnc_trx_wp_ac_info_v1.*
   FROM
      acor_fnc_trx_wp_ac_info_v1
   WHERE
      acor_fnc_trx_wp_ac_info_v1.transaction_type = 'ISSUE'
),
rvw_wp_ac_info_turnin_type
AS (
   SELECT
      acor_fnc_trx_wp_ac_info_v1.*
   FROM
      acor_fnc_trx_wp_ac_info_v1
   WHERE
      acor_fnc_trx_wp_ac_info_v1.transaction_type = 'TURN IN'
)
SELECT
   log.Transaction_id,
   log.transaction_type,
   log.transaction_date,
   log.description,
   CASE
       WHEN log.transaction_type = 'ISSUE' THEN
          issue_type.work_package_number
       WHEN log.transaction_type = 'TURN IN' THEN
          turnin_type.work_package_number
       ELSE ''
   END AS work_pacakge_number,
   CASE
       WHEN log.transaction_type = 'ISSUE' THEN
          issue_type.wp_id
       WHEN log.transaction_type = 'TURN IN' THEN
          turnin_type.wp_id
       ELSE null
   END AS work_pacakge_id,
   log.unit_price,
   log.qty               AS quantity,
   from_inv_loc.loc_cd   AS location_code,
   supl_loc.loc_cd       AS supply_location_code,
   vendor.Vendor_Code,
   inv.oem_serial_number AS component_serial_number,
   inv.part_number        AS component_part_number,
   inv.oem_batch_number   AS component_batch_number,
   inv.manufacturer_code,
   inv.inventory_id,
   log.currency,
   log.exchange_rate,
   CASE
      WHEN log.transaction_type = 'TURN IN' THEN
         turnin_type.registration_code
      WHEN log.transaction_type = 'ISSUE' THEN
         issue_type.registration_code
      ELSE ''
   END AS aircraft_registration_code,
   CASE
      WHEN log.transaction_type = 'TURN IN' THEN
         turnin_type.aircraft_id
      WHEN log.transaction_type = 'ISSUE' THEN
         issue_type.aircraft_id
      ELSE null
   END AS aircraft_identifier,
   vendor.vendor_id,
   log.order_id,
   log.order_line_id,
   log.invoice_id,
   log.invoice_number,
   log.invoice_line_id,
   log.order_line_number,
   log.invoice_line_number
FROM
   acor_financial_trx_v1 log
   LEFT JOIN rvw_wp_ac_info_issue_type issue_type ON
      log.transaction_id = issue_type.transaction_id
   LEFT JOIN rvw_wp_ac_info_turnin_type turnin_type ON
      log.transaction_id = turnin_type.transaction_id
   LEFT JOIN acor_location_v1 from_inv_loc ON
      from_inv_loc.location_id = log.inv_from_loc_id
   -- get vendor code
   LEFT OUTER JOIN acor_vendors_v1 vendor ON
      log.vendor_id = vendor.vendor_id
   -- get supply location code
   LEFT JOIN acor_location_v1 supl_loc ON
      log.supply_loc_id = supl_loc.location_id
   -- get inventory info
   LEFT JOIN rvw_inventory inv ON
      log.inventory_id = inv.inventory_id
;