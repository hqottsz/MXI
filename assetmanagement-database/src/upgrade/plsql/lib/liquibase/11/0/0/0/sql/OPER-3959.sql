--liquibase formatted sql


--changeSet OPER-3959:1 stripComments:false
DELETE FROM
  inv_kit_map
WHERE
  ( inv_kit_map.inv_no_db_id, inv_kit_map.inv_no_id )	  
  IN
  (
        SELECT
            inv_inv.inv_no_db_id,
            inv_inv.inv_no_id
        FROM
            inv_inv
        WHERE
            inv_inv.inv_class_db_id = 0 AND
            inv_inv.inv_class_cd    = 'BATCH'
            AND
            inv_inv.bin_qt = 0
  );