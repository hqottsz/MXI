--liquibase formatted sql


--changeSet MX-18293:1 stripComments:false
UPDATE
   inv_inv
SET
   locked_bool = 0,
   inv_cond_db_id = 0,	
   inv_cond_cd = 'INSRV'
WHERE
   inv_inv.inv_cond_cd = 'ARCHIVE'
   AND
   EXISTS
   (
      SELECT
         1
      FROM
         inv_inv parent_inv_inv
      WHERE
         parent_inv_inv.inv_no_db_id = inv_inv.nh_inv_no_db_id AND
         parent_inv_inv.inv_no_id    = inv_inv.nh_inv_no_id
         AND
         parent_inv_inv.inv_cond_cd <> 'ARCHIVE'
   );