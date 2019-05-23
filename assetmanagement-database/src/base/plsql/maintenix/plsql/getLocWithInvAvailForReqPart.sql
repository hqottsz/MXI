--liquibase formatted sql


--changeSet getLocWithInvAvailForReqPart:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:       getLocWithInvAvailForReqPart
* Arguments:      aReqPartDbId: the DbId of the part request
*                 aReqPartId: the Id of the part request
* Description:    This function looks for the local location with the most inventory
*                 matching the requested part. It handle two cases:
*                 1. Only the part req's part group in known
*                 2. The specified part is known
*                 The return value is the location code (STRING)
*********************************************************************************/
CREATE OR REPLACE FUNCTION getLocWithInvAvailForReqPart
(
   aReqPartDbId req_part.req_part_db_id%TYPE,
   aReqPartId req_part.req_part_id%TYPE
) RETURN VARCHAR
IS
   lSpecPart NUMBER(1);
   lBomPart  NUMBER(1);
   lLocCd    VARCHAR2(4000);
BEGIN

   SELECT
      DECODE( req_part.req_spec_part_no_db_id, NULL, 0, 1 ),
      DECODE( req_part.req_bom_part_db_id, NULL, 0, 1 )
   INTO
      lSpecPart,
      lBomPart
   FROM
      req_part
   WHERE
      req_part.req_part_db_id = aReqPartDbId AND
      req_part.req_part_id    = aReqPartId AND
      req_part.rstat_cd = 0;

   IF lSpecPart = 1 THEN
      SELECT
         loc_cd
      INTO
         lLocCd
      FROM
         (
         SELECT
            inv_loc.loc_cd,
            /* The nested functions below perform the following logic:

               lBinQt := NVL( lBinQt, 0 );

               lReserveQt := NVL( lReserveQt, 0 );

               IF lBinQt - lReserveQt < 0 THEN
                  lAvailQt := 0;
               ELSE
                  lAvailQt := lBinQt - lReserveQt;
               END IF;

               Where:
               lBinQt:= SUM( ROUND( DECODE( inv_inv.bin_qt, NULL, 1, inv_inv.bin_qt ), ref_qty_unit.decimal_places_qt ) )
               lReserveQt:= SUM( ROUND( DECODE( inv_inv.reserve_qt, NULL, DECODE( inv_inv.reserved_bool, NULL, 0, inv_inv.reserved_bool ), inv_inv.reserve_qt ), ref_qty_unit.decimal_places_qt ) )
            */
            DECODE(
               SIGN(
                  NVL( SUM( ROUND( DECODE( inv_inv.bin_qt, NULL, 1, inv_inv.bin_qt ), ref_qty_unit.decimal_places_qt ) ), 0 ) -
                  NVL( SUM( ROUND(
                           DECODE( inv_inv.reserve_qt, NULL,
                              DECODE( inv_inv.reserved_bool, NULL, 0, inv_inv.reserved_bool ),
                              inv_inv.reserve_qt
                           ),
                           ref_qty_unit.decimal_places_qt
                  ) ), 0 )
               ),
               -1,
               0,
               NVL( SUM( ROUND( DECODE( inv_inv.bin_qt, NULL, 1, inv_inv.bin_qt ), ref_qty_unit.decimal_places_qt ) ), 0 ) -
               NVL( SUM( ROUND(
                        DECODE( inv_inv.reserve_qt, NULL,
                           DECODE( inv_inv.reserved_bool, NULL, 0, inv_inv.reserved_bool ),
                           inv_inv.reserve_qt
                        ),
                        ref_qty_unit.decimal_places_qt
               ) ), 0 )
            )
         FROM
            req_part,
            eqp_part_no,
            ref_qty_unit,
            inv_inv,
            inv_loc,
            inv_loc req_inv_loc
         WHERE
            req_part.req_part_db_id = aReqPartDbId AND
            req_part.req_part_id    = aReqPartId   AND
            req_part.rstat_cd	    = 0
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
            inv_inv.inv_cond_cd    = 'RFI'
            AND
            inv_inv.issued_bool = 0 AND
            inv_inv.rstat_cd = 0
            AND
            inv_loc.loc_db_id = inv_inv.loc_db_id AND
            inv_loc.loc_id    = inv_inv.loc_id
            AND
            req_inv_loc.loc_db_id = req_part.req_loc_db_id AND
            req_inv_loc.loc_id    = req_part.req_loc_id
            AND
            req_inv_loc.supply_loc_db_id = inv_loc.supply_loc_db_id AND
            req_inv_loc.supply_loc_id    = inv_loc.supply_loc_id
         GROUP BY
            inv_loc.loc_cd
         ORDER BY
            2 DESC
         )
      WHERE
         ROWNUM = 1;
   END IF;

   IF lBomPart = 1 AND lSpecPart = 0 THEN
      SELECT
         loc_cd
      INTO
         lLocCd
      FROM
         (
         SELECT
            inv_loc.loc_cd,
            /* The nested functions below perform the following logic:

               lBinQt := NVL( lBinQt, 0 );

               lReserveQt := NVL( lReserveQt, 0 );

               IF lBinQt - lReserveQt < 0 THEN
                  lAvailQt := 0;
               ELSE
                  lAvailQt := lBinQt - lReserveQt;
               END IF;

               Where:
               lBinQt:= SUM( ROUND( DECODE( inv_inv.bin_qt, NULL, 1, inv_inv.bin_qt ), ref_qty_unit.decimal_places_qt ) )
               lReserveQt:= SUM( ROUND( DECODE( inv_inv.reserve_qt, NULL, DECODE( inv_inv.reserved_bool, NULL, 0, inv_inv.reserved_bool ), inv_inv.reserve_qt ), ref_qty_unit.decimal_places_qt ) )
            */
            DECODE(
               SIGN(
                  NVL( SUM( ROUND( DECODE( inv_inv.bin_qt, NULL, 1, inv_inv.bin_qt ), ref_qty_unit.decimal_places_qt ) ), 0 ) -
                  NVL( SUM( ROUND(
                           DECODE( inv_inv.reserve_qt, NULL,
                              DECODE( inv_inv.reserved_bool, NULL, 0, inv_inv.reserved_bool ),
                              inv_inv.reserve_qt
                           ),
                           ref_qty_unit.decimal_places_qt
                  ) ), 0 )
               ),
               -1,
               0,
               NVL( SUM( ROUND( DECODE( inv_inv.bin_qt, NULL, 1, inv_inv.bin_qt ), ref_qty_unit.decimal_places_qt ) ), 0 ) -
               NVL( SUM( ROUND(
                        DECODE( inv_inv.reserve_qt, NULL,
                           DECODE( inv_inv.reserved_bool, NULL, 0, inv_inv.reserved_bool ),
                           inv_inv.reserve_qt
                        ),
                        ref_qty_unit.decimal_places_qt
               ) ), 0 )
            )
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
            req_part.req_part_id    = aReqPartId   AND
            req_part.rstat_cd	    = 0
            AND
            eqp_part_baseline.bom_part_db_id = req_part.req_bom_part_db_id AND
            eqp_part_baseline.bom_part_id    = req_part.req_bom_part_id AND
            eqp_part_baseline.standard_bool  = 1
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
            inv_inv.inv_cond_cd    = 'RFI'
            AND
            inv_inv.issued_bool = 0
            AND
            inv_loc.loc_db_id = inv_inv.loc_db_id AND
            inv_loc.loc_id    = inv_inv.loc_id
            AND
            req_inv_loc.loc_db_id = req_part.req_loc_db_id AND
            req_inv_loc.loc_id    = req_part.req_loc_id
            AND
            req_inv_loc.supply_loc_db_id = inv_loc.supply_loc_db_id AND
            req_inv_loc.supply_loc_id    = inv_loc.supply_loc_id
         GROUP BY
            inv_loc.loc_cd
         ORDER BY
            2 DESC
         )
      WHERE
         ROWNUM = 1;
   END IF;

   RETURN lLocCd;

END getLocWithInvAvailForReqPart;
/