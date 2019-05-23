--liquibase formatted sql


--changeSet MX-22315:1 stripComments:false
MERGE INTO
  inv_inv
USING
  inv_inv h_inv_inv
ON (
  h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
  h_inv_inv.inv_no_id = inv_inv.h_inv_no_id
)
WHEN MATCHED THEN
  UPDATE SET
    inv_inv.prevent_synch_bool = h_inv_inv.prevent_synch_bool
  WHERE
    inv_inv.prevent_synch_bool != h_inv_inv.prevent_synch_bool
;