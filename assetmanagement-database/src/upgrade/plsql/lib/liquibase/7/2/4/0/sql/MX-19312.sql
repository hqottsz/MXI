--liquibase formatted sql


--changeSet MX-19312:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Finds all open PO shipments with the same fromto locations, then updates the 'from' location to be the PO vendor's location.
DECLARE
   -- Gets open purchase order shipments with the same from/to locations and exactly one segment.
   CURSOR lcur_BadShipments IS
      SELECT
         ship_shipment.shipment_db_id,
         ship_shipment.shipment_id,
         po_header.po_id,
         org_vendor.vendor_loc_db_id AS ship_from_db_id,
         org_vendor.vendor_loc_id    AS ship_from_id,
         ship_segment.segment_db_id,
         ship_segment.segment_id
      FROM
         po_header
         INNER JOIN ship_shipment    ON ship_shipment.po_db_id          = po_header.po_db_id AND
                                        ship_shipment.po_id             = po_header.po_id
         INNER JOIN ship_segment_map ON ship_segment_map.shipment_db_id = ship_shipment.shipment_db_id AND
                                        ship_segment_map.shipment_id    = ship_shipment.shipment_id
         INNER JOIN ship_segment     ON ship_segment.segment_db_id      = ship_segment_map.segment_db_id AND
                                        ship_segment.segment_id         = ship_segment_map.segment_id
         INNER JOIN org_vendor       ON org_vendor.vendor_db_id         = po_header.vendor_db_id AND
                                        org_vendor.vendor_id            = po_header.vendor_id
      WHERE
         -- Purchase order shipments only
         ship_shipment.shipment_type_db_id = 0 AND
         ship_shipment.shipment_type_cd    = 'PURCHASE'
         AND
         -- ... with the same from/to locations
         ship_segment.ship_from_db_id = ship_segment.ship_to_db_id AND
         ship_segment.ship_from_id    = ship_segment.ship_to_id
         AND
         -- ... that have not been sent.
         ship_segment.segment_status_cd IN ( 'PLAN', 'PEND' )
         -- ... and have not had segments added by users
         AND
         (
            SELECT
               MAX( inner_map.segment_ord )
            FROM
               ship_segment_map inner_map
            WHERE
               inner_map.shipment_db_id = ship_shipment.shipment_db_id AND
               inner_map.shipment_id    = ship_shipment.shipment_id
         ) = 1
      ;

   lrec_BadShipment lcur_BadShipments%ROWTYPE;

BEGIN
   FOR lrec_BadShipment IN lcur_BadShipments LOOP

      UPDATE
         evt_loc
      SET
         evt_loc.loc_db_id = lrec_BadShipment.ship_from_db_id,
         evt_loc.loc_id    = lrec_BadShipment.ship_from_id
      WHERE
         evt_loc.event_db_id = lrec_BadShipment.shipment_db_id AND
         evt_loc.event_id    = lrec_BadShipment.shipment_id
         AND
         evt_loc.event_loc_id = 1;

      UPDATE
         ship_segment
      SET
         ship_segment.ship_from_db_id = lrec_BadShipment.ship_from_db_id,
         ship_segment.ship_from_id    = lrec_BadShipment.ship_from_id
      WHERE
         ship_segment.segment_db_id   = lrec_BadShipment.segment_db_id AND
         ship_segment.segment_id      = lrec_BadShipment.segment_id
      ;
   END LOOP;
END;
/