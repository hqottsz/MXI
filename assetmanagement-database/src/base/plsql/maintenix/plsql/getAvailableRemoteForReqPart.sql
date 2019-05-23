--liquibase formatted sql


--changeSet getAvailableRemoteForReqPart:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getAvailableRemoteForReqPart
(
   aReqPartDbId   req_part.req_part_db_id%TYPE,
   aReqPartId     req_part.req_part_id%TYPE
) RETURN VARCHAR
IS
   lSpecPart   NUMBER(1);
   lBomPart    NUMBER(1);
   lBinQt   FLOAT;
   lReserveQt  FLOAT;
   lAvailQt    FLOAT;
   lLocDbId inv_loc.supply_loc_db_id%TYPE;
   lLocId inv_loc.supply_loc_id%TYPE;
BEGIN

   -- Determine whether the Part or the Part Group is specified by the Request
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
           inv_loc.supply_loc_db_id,
           inv_loc.supply_loc_id
      INTO
           lLocDbId,
            lLocId
      FROM
            req_part,
            sched_stask,
            evt_event,
            evt_loc,
            inv_loc
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
            evt_loc.event_id     =  evt_event.h_event_id
            AND
            evt_loc.loc_db_id  = inv_loc.loc_db_id AND
            evt_loc.loc_id  = inv_loc.loc_id;

      -- NO_DATA_FOUND means that the check is not scheduled, so we don't know what the local location is
      EXCEPTION
         WHEN NO_DATA_FOUND THEN
           lLocDbId := NULL;
           lLocId   := NULL;

      END;
   END IF;


   -- If the Part is Specified, get the Available Remote Inv Quantity for the Part
   IF lSpecPart = 1 THEN

      SELECT
         SUM( ROUND( DECODE( inv_inv.bin_qt, NULL, 1, inv_inv.bin_qt ), ref_qty_unit.decimal_places_qt ) ) AS bin_qt,
         SUM( ROUND( DECODE( inv_inv.reserve_qt, NULL, DECODE( inv_inv.reserved_bool, NULL, 0, inv_inv.reserved_bool ), inv_inv.reserve_qt ), ref_qty_unit.decimal_places_qt ) ) AS reserve_qt
      INTO
         lBinQt,
         lReserveQt
      FROM
         req_part,
         eqp_part_no,
         ref_qty_unit,
         inv_inv,
         inv_loc
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
         -- Limit to Loose Inventory that is RFI and not Issued
         inv_inv.nh_inv_no_db_id IS NULL AND
         inv_inv.nh_inv_no_id    IS NULL
         AND
         inv_inv.inv_cond_db_id = 0 AND
         inv_inv.inv_cond_cd    = 'RFI'
         AND
         inv_inv.issued_bool = 0 AND
         inv_inv.rstat_cd = 0
         AND
         inv_loc.loc_db_id = inv_inv.loc_db_id AND
         inv_loc.loc_id    = inv_inv.loc_id
         AND
    (
       (
          lLocDbId IS NULL AND
          lLocId IS NULL
       )
       OR
       (inv_loc.supply_loc_db_id, inv_loc.supply_loc_id)
       NOT IN
       (
          SELECT
        inv_loc.supply_loc_db_id, inv_loc.supply_loc_id
          FROM
        inv_loc
          WHERE
        inv_loc.loc_db_id = lLocDbId AND
        inv_loc.loc_id    = lLocId
        )

        );
   END IF;

   -- If the Part Group is Specified, get the Available Remote Inv Quantity for the Part Group
   IF lBomPart = 1 AND lSpecPart = 0 THEN

      SELECT
         SUM( ROUND( DECODE( inv_inv.bin_qt, NULL, 1, inv_inv.bin_qt ), ref_qty_unit.decimal_places_qt ) ) AS bin_qt,
         SUM( ROUND( DECODE( inv_inv.reserve_qt, NULL, DECODE( inv_inv.reserved_bool, NULL, 0, inv_inv.reserved_bool ), inv_inv.reserve_qt ), ref_qty_unit.decimal_places_qt ) ) AS reserve_qt
      INTO
         lBinQt,
         lReserveQt
      FROM
         req_part,
         eqp_part_baseline,
         eqp_part_no,
         ref_qty_unit,
         inv_inv,
         inv_loc
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
         -- Limit to Loose Inventory that is RFI and not Issued
         inv_inv.nh_inv_no_db_id IS NULL AND
         inv_inv.nh_inv_no_id    IS NULL
         AND
         inv_inv.inv_cond_db_id = 0 AND
         inv_inv.inv_cond_cd    = 'RFI'
         AND
         inv_inv.issued_bool = 0 AND
         inv_inv.rstat_cd = 0
         AND
         inv_loc.loc_db_id = inv_inv.loc_db_id AND
         inv_loc.loc_id    = inv_inv.loc_id
         AND
         (
          (
             lLocDbId IS NULL AND
             lLocId IS NULL
          )
          OR
          (inv_loc.supply_loc_db_id, inv_loc.supply_loc_id)
          NOT IN
          (
             SELECT
                inv_loc.supply_loc_db_id, inv_loc.supply_loc_id
             FROM
                 inv_loc
             WHERE
                inv_loc.loc_db_id = lLocDbId AND
                inv_loc.loc_id    = lLocId
           )

        );
   END IF;

   -- Calculate the Quantity still Available
   lBinQt := NVL( lBinQt, 0 );
   lReserveQt := NVL( lReserveQt, 0 );

   IF lBinQt - lReserveQt < 0 THEN
      lAvailQt := 0;
   ELSE
      lAvailQt := lBinQt - lReserveQt;
   END IF;

   RETURN lAvailQt || '*' || lBinQt;

END getAvailableRemoteForReqPart;
/