--liquibase formatted sql


--changeSet getAvailableLocalForBomPart:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getAvailableLocalForBomPart
(
   aBomPartDbId      eqp_bom_part.bom_part_db_id%TYPE,
   aBomPartId        eqp_bom_part.bom_part_id%TYPE,
   aSupplyLocDbId    inv_loc.supply_loc_db_id%TYPE,
   aSupplyLocId      inv_loc.supply_loc_id%TYPE
) RETURN VARCHAR
IS
   lBinQt FLOAT;
   lReserveQt FLOAT;
   lAvailQt FLOAT;
BEGIN

   SELECT
      SUM( ROUND( DECODE( inv_inv.bin_qt, NULL, 1, inv_inv.bin_qt ), ref_qty_unit.decimal_places_qt ) ) AS bin_qt,
      SUM( ROUND( DECODE( inv_inv.reserve_qt, NULL, DECODE( inv_inv.reserved_bool, NULL, 0, inv_inv.reserved_bool ), inv_inv.reserve_qt ), ref_qty_unit.decimal_places_qt ) ) AS reserve_qt
   INTO
      lBinQt,
      lReserveQt
   FROM
      eqp_part_baseline,
      eqp_part_no,
      ref_qty_unit,
      inv_inv,
      inv_loc
   WHERE
      eqp_part_baseline.bom_part_db_id = aBomPartDbId AND
      eqp_part_baseline.bom_part_id    = aBomPartId
      AND
      eqp_part_no.part_no_db_id = eqp_part_baseline.part_no_db_id AND
      eqp_part_no.part_no_id    = eqp_part_baseline.part_no_id	  AND
      eqp_part_no.rstat_cd	= 0 AND
      inv_inv.rstat_cd = 0
      AND
      ref_qty_unit.qty_unit_db_id = eqp_part_no.qty_unit_db_id AND
      ref_qty_unit.qty_unit_cd    = eqp_part_no.qty_unit_cd
      AND
      inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
      inv_inv.part_no_id    = eqp_part_no.part_no_id
      AND
      inv_inv.nh_inv_no_db_id IS NULL AND
      inv_inv.nh_inv_no_id    IS NULL
      AND
      inv_inv.inv_cond_db_id = 0 AND
      inv_inv.inv_cond_cd    = 'RFI' AND
      inv_inv.not_found_bool = 0
      AND
      inv_inv.issued_bool = 0
      AND
      inv_loc.loc_db_id = inv_inv.loc_db_id AND
      inv_loc.loc_id    = inv_inv.loc_id
      AND
      inv_loc.supply_loc_db_id = aSupplyLocDbId AND
      inv_loc.supply_loc_id    = aSupplyLocId;

   lBinQt := NVL( lBinQt, 0 );

   lReserveQt := NVL( lReserveQt, 0 );

   IF lBinQt - lReserveQt < 0 THEN
      lAvailQt := 0;
   ELSE
      lAvailQt := lBinQt - lReserveQt;
   END IF;

   RETURN lAvailQt || '*' || lBinQt;

END getAvailableLocalForBomPart;
/