--liquibase formatted sql


--changeSet MX-28044.1:1 stripComments:false
CREATE OR REPLACE VIEW vw_inv_context
AS
SELECT
   inv_inv.inv_no_db_id, 
   inv_inv.inv_no_id,
   inv_inv.h_inv_no_db_id,
   inv_inv.h_inv_no_id,
   inv_inv.assmbl_inv_no_db_id,
   inv_inv.assmbl_inv_no_id,
   inv_inv.inv_class_cd
FROM
   inv_inv
WHERE
   inv_inv.inv_no_db_id = context_package.get_inv_no_db_id() AND
   inv_inv.inv_no_id    = context_package.get_inv_no_id()
;