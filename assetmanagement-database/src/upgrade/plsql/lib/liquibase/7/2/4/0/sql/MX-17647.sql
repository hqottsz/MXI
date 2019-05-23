--liquibase formatted sql


--changeSet MX-17647:1 stripComments:false
-- Allow synchronization on non-root inventory if the root inventory is allowing synchronization
UPDATE
   inv_inv
SET
   prevent_synch_bool = 0
WHERE
   prevent_synch_bool = 1
   AND
   nh_inv_no_db_id IS NOT NULL
   AND EXISTS
   (
      SELECT
         1
      FROM
         inv_inv h_inv_inv
      WHERE
         h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
         h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id
         AND
         h_inv_inv.prevent_synch_bool = 0
   );   

--changeSet MX-17647:2 stripComments:false
-- Prevent synchronization on non-root inventory if the root inventory is preventing synchronization
UPDATE
   inv_inv
SET
   prevent_synch_bool = 1
WHERE
   prevent_synch_bool = 0
   AND
   nh_inv_no_db_id IS NOT NULL
   AND EXISTS
   (
      SELECT
         1
      FROM
         inv_inv h_inv_inv
      WHERE
         h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
         h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id
         AND
         h_inv_inv.prevent_synch_bool = 1
   );