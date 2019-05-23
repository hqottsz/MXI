--liquibase formatted sql


--changeSet getAvailableLocalTools:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getAvailableLocalTools
(
   aBomPartDbId eqp_bom_part.bom_part_db_id%TYPE,
   aBomPartId eqp_bom_part.bom_part_id%TYPE,
   aSupplyLocDbId inv_loc.supply_loc_db_id%TYPE,
   aSupplyLocId inv_loc.supply_loc_id%TYPE
) RETURN VARCHAR
IS
   lToolCount NUMBER;
   lAvailToolCount NUMBER;
BEGIN

   SELECT
      COUNT(*)
   INTO
      lToolCount
   FROM
      eqp_part_baseline,
      eqp_part_no,
      inv_inv tool_inv_inv,
      inv_loc tool_inv_loc
   WHERE
      eqp_part_baseline.bom_part_db_id = aBomPartDbId AND
      eqp_part_baseline.bom_part_id    = aBomPartId
      AND
      eqp_part_no.part_no_db_id = eqp_part_baseline.part_no_db_id AND
      eqp_part_no.part_no_id    = eqp_part_baseline.part_no_id	  AND
      eqp_part_no.rstat_cd	= 0
      AND
      tool_inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
      tool_inv_inv.part_no_id    = eqp_part_no.part_no_id
      AND
      tool_inv_loc.loc_db_id = tool_inv_inv.loc_db_id AND
      tool_inv_loc.loc_id    = tool_inv_inv.loc_id
      AND
      tool_inv_loc.supply_loc_db_id = aSupplyLocDbId AND
      tool_inv_loc.supply_loc_id    = aSupplyLocId;

   SELECT
      COUNT(*)
   INTO
      lAvailToolCount
   FROM
      eqp_part_baseline,
      eqp_part_no,
      inv_inv tool_inv_inv,
      inv_loc tool_inv_loc
   WHERE
      eqp_part_baseline.bom_part_db_id = aBomPartDbId AND
      eqp_part_baseline.bom_part_id    = aBomPartId
      AND
      eqp_part_no.part_no_db_id = eqp_part_baseline.part_no_db_id AND
      eqp_part_no.part_no_id    = eqp_part_baseline.part_no_id	  AND
      eqp_part_no.rstat_cd	= 0
      AND
      tool_inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
      tool_inv_inv.part_no_id    = eqp_part_no.part_no_id
      AND
      tool_inv_inv.inv_cond_cd = 'RFI'
      AND
      tool_inv_loc.loc_db_id = tool_inv_inv.loc_db_id AND
      tool_inv_loc.loc_id    = tool_inv_inv.loc_id
      AND
      tool_inv_loc.supply_loc_db_id = aSupplyLocDbId AND
      tool_inv_loc.supply_loc_id    = aSupplyLocId;

   IF lToolCount IS NULL THEN
      lToolCount := 0;
   END IF;

   IF lAvailToolCount IS NULL THEN
      lAvailToolCount := 0;
   END IF;

   RETURN lAvailToolCount || '*' || lToolCount;

END getAvailableLocalTools;

/