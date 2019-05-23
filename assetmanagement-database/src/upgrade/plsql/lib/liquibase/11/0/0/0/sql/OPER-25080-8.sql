--liquibase formatted sql


--changeSet OPER-25080-8:1 stripComments:false
-- view for showing the inventory tree
CREATE OR REPLACE VIEW VW_INV_TREE AS
SELECT
   inv_inv.inv_no_db_id as inv_key_db_id,
   inv_inv.inv_no_id as inv_key_id,
   inv_inv.inv_no_db_id as filter_inv_no_db_id,
   inv_inv.inv_no_id as filter_inv_no_id
FROM
   inv_inv
WHERE
   inv_inv.inv_class_cd NOT IN ('ASSY', 'ACFT')
CONNECT BY
   PRIOR inv_inv.nh_inv_no_db_id = inv_inv.inv_no_db_id AND
   PRIOR inv_inv.nh_inv_no_id = inv_inv.inv_no_id
   AND
   inv_inv.inv_class_cd NOT IN ('ASSY', 'ACFT')
UNION ALL
SELECT
   ass_inv.inv_no_db_id as inv_key_db_id,
   ass_inv.inv_no_id as inv_key_id,
   inv_inv.inv_no_db_id as filter_inv_no_db_id,
   inv_inv.inv_no_id as filter_inv_no_id
FROM
   inv_inv,
   inv_inv ass_inv
WHERE
   ass_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
   ass_inv.inv_no_id = inv_inv.assmbl_inv_no_id;


