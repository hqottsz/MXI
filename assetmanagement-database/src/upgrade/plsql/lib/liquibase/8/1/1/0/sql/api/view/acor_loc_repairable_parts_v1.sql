--liquibase formatted sql


--changeSet acor_loc_repairable_parts_v1:1 stripComments:false
CREATE OR REPLACE VIEW acor_loc_repairable_parts_v1 
AS
SELECT
   inv_loc.loc_cd AS location_code,
   inv_loc.alt_id AS location_id,
   eqp_part_no.part_no_oem AS part_number,
   eqp_part_no.alt_id AS part_id
FROM
   inv_loc
   INNER JOIN inv_loc_repair ON
     inv_loc.loc_db_id = inv_loc_repair.loc_db_id   AND
     inv_loc.loc_id    = inv_loc_repair.loc_id
   INNER JOIN eqp_part_no ON
     eqp_part_no.part_no_db_id = inv_loc_repair.part_no_db_id AND
     eqp_part_no.part_no_id    = inv_loc_repair.part_no_id
;