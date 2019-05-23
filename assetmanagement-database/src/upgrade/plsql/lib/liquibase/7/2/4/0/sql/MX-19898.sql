--liquibase formatted sql


--changeSet MX-19898:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   -- Cursor containing all shipments with:
   -- * A final segment that does not match the destination of the shipment order.
   -- * No in-progress or historic segments.
   CURSOR lcur_Mismatches IS
      SELECT
         ship_shipment.shipment_db_id,
         ship_shipment.shipment_id,
         final_segment.segment_db_id,
         final_segment.segment_id,
         order_dest.loc_db_id,
         order_dest.loc_id
      FROM
         po_header
         INNER JOIN ship_shipment ON ship_shipment.po_db_id = po_header.po_db_id AND
                                     ship_shipment.po_id    = po_header.po_id
         -- Find the shipment's final destination.
         INNER JOIN (
               SELECT DISTINCT
                  ship_segment_map.shipment_db_id,
                  ship_segment_map.shipment_id,
                  FIRST_VALUE(ship_segment.segment_db_id) OVER(PARTITION BY ship_segment_map.shipment_db_id, ship_segment_map.shipment_id ORDER BY ship_segment_map.segment_ord DESC) AS segment_db_id,
                  FIRST_VALUE(ship_segment.segment_id   ) OVER(PARTITION BY ship_segment_map.shipment_db_id, ship_segment_map.shipment_id ORDER BY ship_segment_map.segment_ord DESC) AS segment_id,
                  FIRST_VALUE(ship_segment.ship_to_db_id) OVER(PARTITION BY ship_segment_map.shipment_db_id, ship_segment_map.shipment_id ORDER BY ship_segment_map.segment_ord DESC) AS loc_db_id,
                  FIRST_VALUE(ship_segment.ship_to_id   ) OVER(PARTITION BY ship_segment_map.shipment_db_id, ship_segment_map.shipment_id ORDER BY ship_segment_map.segment_ord DESC) AS loc_id
               FROM
                  ship_segment_map
                  JOIN ship_segment ON ship_segment.segment_db_id = ship_segment_map.segment_db_id AND
                                       ship_segment.segment_id    = ship_segment_map.segment_id
               WHERE
                  -- Only unsent shipments should be updated.
                  ship_segment.segment_status_db_id = 0 AND
                  ship_segment.segment_status_cd IN ( 'PLAN', 'PEND' )
         ) final_segment    ON final_segment.shipment_db_id = ship_shipment.shipment_db_id AND
                               final_segment.shipment_id    = ship_shipment.shipment_id
         -- Get the final segment destination location
         INNER JOIN inv_loc segment_dest ON segment_dest.loc_db_id = final_segment.loc_db_id AND
                                            segment_dest.loc_id    = final_segment.loc_id
         -- Get the order destination location
         INNER JOIN evt_loc              ON evt_loc.event_db_id    = ship_shipment.shipment_db_id AND
                                            evt_loc.event_id       = ship_shipment.shipment_id    AND
                                            evt_loc.event_loc_id   = 2
         INNER JOIN inv_loc order_dest   ON order_dest.loc_db_id   = evt_loc.loc_db_id AND
                                            order_dest.loc_id      = evt_loc.loc_id
      WHERE
         -- Ensure sure both locations are VENDOR
         segment_dest.loc_type_db_id = 0 AND
         segment_dest.loc_type_cd    = 'VENDOR'
         AND
         order_dest.loc_type_db_id   = 0 AND
         order_dest.loc_type_cd      = 'VENDOR'
         -- Only update segments that do not match their order destination.
         AND
         (
            segment_dest.loc_db_id != order_dest.loc_db_id OR
            segment_dest.loc_id    != order_dest.loc_id
         )
         AND
         -- Exclude shipments that have in progress/historic segments.
         ( ship_shipment.shipment_db_id, ship_shipment.shipment_id ) NOT IN (

            SELECT DISTINCT
               ship_shipment.shipment_db_id,
               ship_shipment.shipment_id
            FROM
               ship_shipment
               INNER JOIN ship_segment_map ON ship_segment_map.shipment_db_id = ship_shipment.shipment_db_id AND
                                              ship_segment_map.shipment_id    = ship_shipment.shipment_id
               INNER JOIN ship_segment     ON ship_segment.segment_db_id = ship_segment_map.segment_db_id AND
                                              ship_segment.segment_id    = ship_segment_map.segment_id
            WHERE
               ship_segment.segment_status_db_id = 0 AND
               ship_segment.segment_status_cd NOT IN ( 'PEND', 'PLAN' )
         );

   lrec_Mismatch lcur_Mismatches%ROWTYPE;

BEGIN

   FOR lrec_Mismatch IN lcur_Mismatches LOOP
      --dbms_output.put_line( 'ship: ' || lrec_Mismatch.Shipment_Db_Id || ':' || lrec_Mismatch.Shipment_Id || ', seg: ' || lrec_Mismatch.Segment_Db_Id || ':' || lrec_Mismatch.segment_id || ', loc: ' || lrec_Mismatch.Loc_Db_Id || ':' || lrec_Mismatch.loc_id );
      UPDATE
         ship_segment
      SET
         ship_to_db_id = lrec_Mismatch.loc_db_id,
         ship_to_id    = lrec_Mismatch.loc_id
      WHERE
         ship_segment.segment_db_id = lrec_Mismatch.segment_db_id AND
         ship_segment.segment_id    = lrec_Mismatch.segment_id;

   END LOOP;
END;
/