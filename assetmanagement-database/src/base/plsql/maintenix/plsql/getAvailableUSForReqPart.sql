--liquibase formatted sql


--changeSet getAvailableUSForReqPart:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getAvailableUSForReqPart
(
   aReqPartDbId req_part.req_part_db_id%TYPE,
   aReqPartId req_part.req_part_id%TYPE
) RETURN VARCHAR
IS
   lSpecPart NUMBER(1);
   lBomPart NUMBER(1);
   lBinQt FLOAT;
   lReserveQt FLOAT;
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
         SUM( ROUND( DECODE( inv_inv.bin_qt, NULL, 1, inv_inv.bin_qt ), ref_qty_unit.decimal_places_qt ) ) AS bin_qt,
         SUM( ROUND( DECODE( inv_inv.reserve_qt, NULL, DECODE( inv_inv.reserved_bool, NULL, 0, inv_inv.reserved_bool ), inv_inv.reserve_qt ), ref_qty_unit.decimal_places_qt ) ) AS reserve_qt
      INTO
         lBinQt,
         lReserveQt
      FROM
         req_part,
         eqp_part_no,
         ref_qty_unit,
         inv_inv
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
         inv_inv.rstat_cd = 0
         AND
         inv_inv.nh_inv_no_db_id IS NULL AND
         inv_inv.nh_inv_no_id IS NULL
         AND
         inv_inv.inv_cond_cd IN ('INREP', 'INSPREQ', 'REPREQ');

   END IF;

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
         inv_inv
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
         inv_inv.rstat_cd = 0
         AND
         inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
         inv_inv.part_no_id    = eqp_part_no.part_no_id
         AND
         inv_inv.nh_inv_no_db_id IS NULL AND
         inv_inv.nh_inv_no_id IS NULL
         AND
         inv_inv.inv_cond_cd IN ('INREP', 'INSPREQ', 'REPREQ');

   END IF;

   RETURN ( NVL( lBinQt, 0 ) - NVL( lReserveQt, 0 ) ) || '*' || NVL( lBinQt, 0 );

END getAvailableUSForReqPart;
/