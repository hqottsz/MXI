--liquibase formatted sql


--changeSet MX-17541:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

--changeSet MX-17541:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getAvailableLocalForReqPart
(
   aReqPartDbId req_part.req_part_db_id%TYPE,
   aReqPartId req_part.req_part_id%TYPE
) RETURN VARCHAR
IS
   lSpecPart NUMBER(1);
   lBomPart NUMBER(1);
   lBinQt FLOAT;
   lReserveQt FLOAT;
   lAvailQt FLOAT;
   lLocDbId inv_loc.loc_db_id%TYPE;
   lLocId   inv_loc.loc_id%TYPE;
BEGIN

   SELECT
      DECODE( req_part.req_spec_part_no_db_id, NULL, 0, 1 ),
      DECODE( req_part.req_bom_part_db_id, NULL, 0, 1 ),
      req_part.req_loc_db_id,
      req_part.req_loc_id
   INTO
      lSpecPart,
      lBomPart,
      lLocDbId,
      lLocId
   FROM
      req_part
   WHERE
      req_part.req_part_db_id = aReqPartDbId AND
      req_part.req_part_id    = aReqPartId   AND
      req_part.rstat_cd	      = 0;

   -- If the part request does not have a location, see if the check does
   IF lLocDbId IS NULL THEN
      BEGIN
         SELECT
            evt_loc.loc_db_id,
            evt_loc.loc_id
         INTO
            lLocDbId,
            lLocId
         FROM
            req_part,
            sched_stask,
            evt_event,
            evt_loc
         WHERE
            req_part.req_part_db_id = aReqPartDbId AND
            req_part.req_part_id    = aReqPartId   AND
            req_part.rstat_cd	    = 0
            AND
            sched_stask.sched_db_id = req_part.sched_db_id AND
            sched_stask.sched_id    = req_part.sched_id
            AND
            evt_event.event_db_id  = sched_stask.sched_db_id AND
            evt_event.event_id     = sched_stask.sched_id
            AND
            evt_loc.event_db_id  =  evt_event.h_event_db_id AND
            evt_loc.event_id     =  evt_event.h_event_id;
      -- NO_DATA_FOUND means that the check is not scheduled, so we don't know what is local and just return 0*0
      EXCEPTION
         WHEN NO_DATA_FOUND THEN
            RETURN '0*0';
      END;
   END IF;

   IF lSpecPart = 1 THEN

      SELECT
         SUM( ROUND( NVL( inv_inv.bin_qt, 1 ), ref_qty_unit.decimal_places_qt ) ) AS bin_qt,
         SUM( ROUND( NVL( inv_inv.reserve_qt, inv_inv.reserved_bool ), ref_qty_unit.decimal_places_qt ) ) AS reserve_qt
      INTO
         lBinQt,
         lReserveQt
      FROM
         req_part,
         eqp_part_no,
         ref_qty_unit,
         inv_inv,
         inv_loc,
         inv_loc req_inv_loc
      WHERE
         req_part.req_part_db_id = aReqPartDbId AND
         req_part.req_part_id    = aReqPartId	AND
         req_part.rstat_cd	 = 0
         AND
         eqp_part_no.part_no_db_id = req_part.req_spec_part_no_db_id AND
         eqp_part_no.part_no_id    = req_part.req_spec_part_no_id
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
         req_inv_loc.loc_db_id = lLocDbId AND
         req_inv_loc.loc_id    = lLocId
         AND
         req_inv_loc.supply_loc_db_id = inv_loc.supply_loc_db_id AND
         req_inv_loc.supply_loc_id    = inv_loc.supply_loc_id;

   END IF;

   IF lBomPart = 1 AND lSpecPart = 0 THEN

      SELECT
         SUM( ROUND( NVL( inv_inv.bin_qt, 1 ), ref_qty_unit.decimal_places_qt ) ) AS bin_qt,
         SUM( ROUND( NVL( inv_inv.reserve_qt, inv_inv.reserved_bool ), ref_qty_unit.decimal_places_qt ) ) AS reserve_qt
      INTO
         lBinQt,
         lReserveQt
      FROM
         req_part,
         eqp_part_baseline,
         eqp_part_no,
         ref_qty_unit,
         inv_inv,
         inv_loc,
         inv_loc req_inv_loc
      WHERE
         req_part.req_part_db_id = aReqPartDbId AND
         req_part.req_part_id    = aReqPartId	AND
         req_part.rstat_cd	 = 0
         AND
         eqp_part_baseline.bom_part_db_id = req_part.req_bom_part_db_id AND
         eqp_part_baseline.bom_part_id    = req_part.req_bom_part_id
         AND
         eqp_part_no.part_no_db_id = eqp_part_baseline.part_no_db_id AND
         eqp_part_no.part_no_id    = eqp_part_baseline.part_no_id
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
         req_inv_loc.loc_db_id = lLocDbId AND
         req_inv_loc.loc_id    = lLocId
         AND
         req_inv_loc.supply_loc_db_id = inv_loc.supply_loc_db_id AND
         req_inv_loc.supply_loc_id    = inv_loc.supply_loc_id;

   END IF;

   lBinQt := NVL( lBinQt, 0 );
   lReserveQt := NVL( lReserveQt, 0 );

   IF lBinQt - lReserveQt < 0 THEN
      lAvailQt := 0;
   ELSE
      lAvailQt := lBinQt - lReserveQt;
   END IF;

   RETURN lAvailQt || '*' || lBinQt;

END getAvailableLocalForReqPart;
/