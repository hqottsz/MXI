--liquibase formatted sql


--changeSet acor_inv_system_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_inv_system_v1
AS 
SELECT
   inv_inv.alt_id            AS inventory_id,
   h_inv.alt_id              AS h_inventory_id,
   -- identity
   inv_inv.barcode_sdesc     AS barcode,
   inv_inv.inv_no_sdesc      AS description
FROM
   inv_inv
   -- highest inventory
   INNER JOIN inv_inv h_inv ON
      inv_inv.h_inv_no_db_id = h_inv.inv_no_db_id AND
      inv_inv.h_inv_no_id    = h_inv.inv_no_id
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    = 'SYS';