--liquibase formatted sql


--changeSet mv_possible_kits_by_location:1 stripComments:false
/********************************************************************************
*
* View:			mv_possible_kits_by_location
*
* Description:	This materialized view contains a listing of the number of 
*				possible kits that can be created at each supply location
*
* Orig.Coder:    cdaley
* Recent Coder:
* Recent Date:   Aug 24th, 2009
*
*********************************************************************************/
CREATE MATERIALIZED VIEW MV_POSSIBLE_KITS_BY_LOCATION
BUILD DEFERRED
REFRESH FORCE ON DEMAND
WITH PRIMARY KEY
AS
   SELECT
      eqp_part_no.part_no_db_id,
      eqp_part_no.part_no_id,
      inv_loc.loc_db_id,
      inv_loc.loc_id,
      kit_pkg.getPossibleKitsForLocation(
         eqp_part_no.part_no_db_id,
         eqp_part_no.part_no_id,
         inv_loc.loc_db_id,
         inv_loc.loc_id
      ) AS possible_kits
   FROM
      eqp_part_no,
      inv_loc
   WHERE
      inv_loc.supply_bool = 1
      AND
      eqp_part_no.inv_class_cd = 'KIT';