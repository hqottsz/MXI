--liquibase formatted sql


--changeSet 2vw_inv_assmbl:1 stripComments:false
/*
   SQL view for determining the assemblies
*/
CREATE OR REPLACE VIEW VW_INV_ASSMBL AS
SELECT
   inv_inv.assmbl_db_id,
   inv_inv.assmbl_cd,
   inv_inv.assmbl_bom_id,
   inv_inv.inv_no_db_id as filter_inv_no_db_id,
   inv_inv.inv_no_id as filter_inv_no_id
FROM
   inv_inv
UNION
SELECT
   inv_inv.orig_assmbl_db_id,
   inv_inv.orig_assmbl_cd,
   0 as assmbl_bom_id,
   inv_inv.inv_no_db_id as filter_inv_no_db_id,
   inv_inv.inv_no_id as filter_inv_no_id
FROM
   inv_inv;
