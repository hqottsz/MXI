--liquibase formatted sql


--changeSet vw_inv_ac_authority:1 stripComments:false
/**
 * This view creates the full mapping of human resources to inventory over which they have authority.
 *
 * There are three possible cases:
 *  1. The HR has all authority and so has authority over the aircraft
 *  2. The aircraft has no authority defined and so the HR has authority over the aircraft
 *  3. The HR and aircraft have the same authority mapping and so the HR has authority over the aircraft
 */
CREATE OR REPLACE VIEW vw_inv_ac_authority
AS
-- inventory with an authority, hr with the same authority
SELECT
   inv_inv.inv_no_db_id,
   inv_inv.inv_no_id,
   org_hr.hr_db_id,
   org_hr.hr_id,
   inv_inv.authority_db_id,
   inv_inv.authority_id
FROM
   inv_ac_reg
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
   INNER JOIN org_hr_authority ON
      org_hr_authority.authority_db_id = inv_inv.authority_db_id AND
      org_hr_authority.authority_id    = inv_inv.authority_id
   INNER JOIN org_hr ON
      org_hr.hr_db_id = org_hr_authority.hr_db_id AND
      org_hr.hr_id    = org_hr_authority.hr_id
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    = 'ACFT'
   AND
   org_hr.all_authority_bool = 0
UNION ALL
-- inventory with no authority, all hr have authority (exclude those with all_authority_bool)
SELECT
   inv_inv.inv_no_db_id,
   inv_inv.inv_no_id,
   org_hr.hr_db_id,
   org_hr.hr_id,
   null AS authority_db_id,
   null AS authority_id
FROM
   inv_ac_reg
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
   CROSS JOIN org_hr
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    = 'ACFT'
   AND
   inv_inv.authority_db_id IS NULL
   AND
   org_hr.all_authority_bool = 0
UNION ALL
-- hr with all authority have authority over all aircraft
SELECT
   inv_inv.inv_no_db_id,
   inv_inv.inv_no_id,
   org_hr.hr_db_id,
   org_hr.hr_id,
   inv_inv.authority_db_id,
   inv_inv.authority_id
FROM
   inv_ac_reg
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
   CROSS JOIN org_hr
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    = 'ACFT'
   AND
   org_hr.all_authority_bool = 1
;