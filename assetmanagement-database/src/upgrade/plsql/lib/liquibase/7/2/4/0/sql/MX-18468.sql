--liquibase formatted sql


--changeSet MX-18468:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Deletes invalid rows from ship_segment_map and ship_segment.
--
-- The cursor fetches:
--    The first segment of every SENDXCHG shipment that arrives at a vendor location
--    ... then returns all following segments (there should never be a segment following a vendor delivery.)
-- The FOR loop then deletes rows from both tables based on the cursor contents.
DECLARE
   -- Cursor that finds segments following vendor delivery segments.
   CURSOR lcur_after_vendor_segments IS
      SELECT 
         ship_segment_map.segment_db_id,
         ship_segment_map.segment_id
      FROM 
         (
            -- Find the first segment of a SENDXCHG that arrives at a vendor location.
            SELECT 
               inner_shipment.shipment_db_id, 
               inner_shipment.shipment_id,         
               MIN( inner_map.segment_ord ) AS segment_ord
            FROM 
               ship_shipment               inner_shipment
               INNER JOIN ship_segment_map inner_map     ON inner_map.shipment_db_id    = inner_shipment.shipment_db_id AND
                                                            inner_map.shipment_id       = inner_shipment.shipment_id
               INNER JOIN ship_segment     inner_segment ON inner_segment.segment_db_id = inner_map.segment_db_id AND
                                                            inner_segment.segment_id    = inner_map.segment_id
               INNER JOIN inv_loc          dest_loc      ON dest_loc.loc_db_id          = inner_segment.ship_to_db_id AND
                                                            dest_loc.loc_id             = inner_segment.ship_to_id
            WHERE
               inner_shipment.shipment_type_db_id = 0 AND
               inner_shipment.shipment_type_cd    = 'SENDXCHG'
               AND
               dest_loc.loc_type_db_id            = 0 AND
               dest_loc.loc_type_cd               = 'VENDOR'         
            GROUP BY
               inner_shipment.shipment_db_id,
               inner_shipment.shipment_id
         ) shipment_to_vendor_segment
         INNER JOIN ship_segment_map ON ship_segment_map.shipment_db_id = shipment_to_vendor_segment.shipment_db_id AND
                                        ship_segment_map.shipment_id    = shipment_to_vendor_segment.shipment_id
      WHERE
         -- Return segments that follow the vendor arrival segment.
         ship_segment_map.segment_ord > shipment_to_vendor_segment.segment_ord
      ;

      lrec_invalid_segment lcur_after_vendor_segments%ROWTYPE;

BEGIN
      FOR lrec_invalid_segment IN lcur_after_vendor_segments LOOP  
         
         dbms_output.put_line( 'Removing segment ' || lrec_invalid_segment.segment_db_id || ':' || lrec_invalid_segment.segment_id );
         
         DELETE FROM ship_segment_map m 
         WHERE  m.segment_db_id = lrec_invalid_segment.segment_db_id AND
                m.segment_id    = lrec_invalid_segment.segment_id
         ;
         
         DELETE FROM ship_segment s
         WHERE  s.segment_db_id = lrec_invalid_segment.segment_db_id AND
                s.segment_id    = lrec_invalid_segment.segment_id
         ;
      END LOOP;
END;
/