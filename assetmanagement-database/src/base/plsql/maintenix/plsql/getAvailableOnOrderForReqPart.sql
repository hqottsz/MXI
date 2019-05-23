--liquibase formatted sql


--changeSet getAvailableOnOrderForReqPart:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getAvailableOnOrderForReqPart
(  aReqPartDbId req_part.req_part_db_id%TYPE,
   aReqPartId req_part.req_part_id%TYPE
) RETURN VARCHAR
IS
   lOrderQt FLOAT;
   lReserveQt FLOAT;
BEGIN

   SELECT
      SUM( on_order ) ordered_qt,
      SUM( reserved ) reserved_qt
   INTO
      lOrderQt,
      lReserveQt
   FROM
      (-- Either a specific part number
         SELECT
            SUM(ship_shipment_line.expect_qt - nvl(ship_shipment_line.receive_qt, 0)) on_order,
            (  SELECT SUM(req_part_ord.req_qt)
               FROM
                  req_part    req_part_ord,
                  evt_event   req_part_event
               WHERE
                  req_part_ord.po_db_id   = po_line.po_db_id AND
                  req_part_ord.po_id      = po_line.po_id AND
                  req_part_ord.po_line_id = po_line.po_line_id  AND
         	  req_part_ord.rstat_cd	  = 0
                  AND
                  req_part_event.event_db_id = req_part_ord.req_part_db_id AND
                  req_part_event.event_id    = req_part_ord.req_part_id
                  AND
                  req_part_event.event_status_cd = 'PRONORDER'
               ) reserved
         FROM
            req_part
            INNER JOIN po_line ON
               po_line.part_no_db_id = req_part.req_spec_part_no_db_id AND
               po_line.part_no_id    = req_part.req_spec_part_no_id
            INNER JOIN ship_shipment_line ON
               ship_shipment_line.po_db_id   = po_line.po_db_id AND
               ship_shipment_line.po_id      = po_line.po_id AND
               ship_shipment_line.po_line_id = po_line.po_line_id
            INNER JOIN evt_event shipment_event ON
               shipment_event.event_db_id = ship_shipment_line.shipment_db_id AND
               shipment_event.event_id    = ship_shipment_line.shipment_id
         WHERE
            req_part.req_part_db_id = aReqPartDbId AND
            req_part.req_part_id    = aReqPartId   AND
            req_part.rstat_cd	    = 0
            AND
            shipment_event.hist_bool = 0
            AND
            po_line.deleted_bool = 0
         GROUP BY
            po_line.po_db_id,
            po_line.po_id,
            po_line.po_line_id
         UNION ALL

         -- Or any alternates if requesting bom part
         SELECT
            SUM(ship_shipment_line.expect_qt - nvl(ship_shipment_line.receive_qt, 0)) on_order,
            (  SELECT SUM(req_part_ord.req_qt)
               FROM
                  req_part    req_part_ord,
                  evt_event   req_part_event
               WHERE
                  req_part_ord.po_db_id   = po_line.po_db_id AND
                  req_part_ord.po_id      = po_line.po_id AND
				          req_part_ord.rstat_cd = 0
                  AND
                  req_part_ord.po_line_id = po_line.po_line_id AND
                  req_part_event.event_db_id = req_part_ord.req_part_db_id
                  AND
                  req_part_event.event_id    = req_part_ord.req_part_id AND
                  req_part_event.event_status_cd = 'PRONORDER'
            ) reserved
         FROM
            req_part
            INNER JOIN eqp_part_baseline ON
               eqp_part_baseline.bom_part_db_id = req_part.req_bom_part_db_id AND
               eqp_part_baseline.bom_part_id    = req_part.req_bom_part_id
            INNER JOIN po_line ON
               po_line.part_no_db_id = eqp_part_baseline.part_no_db_id AND
               po_line.part_no_id    = eqp_part_baseline.part_no_id
            INNER JOIN ship_shipment_line ON
               ship_shipment_line.po_db_id   = po_line.po_db_id AND
               ship_shipment_line.po_id      = po_line.po_id AND
               ship_shipment_line.po_line_id = po_line.po_line_id
            INNER JOIN evt_event shipment_event ON
               shipment_event.event_db_id = ship_shipment_line.shipment_db_id AND
               shipment_event.event_id    = ship_shipment_line.shipment_id
         WHERE
            req_part.req_part_db_id = aReqPartDbId AND
            req_part.req_part_id    = aReqPartId AND
            req_part.rstat_cd	    = 0
            AND
            req_part.req_spec_part_no_db_id IS NULL AND
            req_part.req_spec_part_no_id    IS NULL
            AND
            shipment_event.hist_bool = 0
            AND
            po_line.deleted_bool = 0
         GROUP BY
            po_line.po_db_id,
            po_line.po_id,
            po_line.po_line_id
      );

   lOrderQt   := NVL( lOrderQt, 0 );
   lReserveQt := NVL( lReserveQt, 0 );

   IF lOrderQt - lReserveQt < 0 THEN
      RETURN '0*' || lOrderQt;
   ELSE
      RETURN lOrderQt - lReserveQt || '*' || lOrderQt;
   END IF;

END getAvailableOnOrderForReqPart;
/