--liquibase formatted sql


--changeSet acor_shipments_routing_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_shipments_routing_v1
AS 
WITH
rvw_location
AS
   (
     SELECT
        location_id,
        airport_alt_id,
        loc_cd,
        loc_type_cd
     FROM
        acor_location_v1
   ),
rvw_inv_loc
AS
   (
      SELECT
         alt_id,
         loc_db_id,
         loc_id,
         loc_cd,
         loc_type_cd
      FROM
         inv_loc
   )
SELECT
   ship_shipment.alt_id             AS shipment_id,
   ship_segment_map.segment_ord     AS route_order,
   ship_from_loc.alt_id             AS ship_from_id,
   CASE
      WHEN highest_from_type.loc_type_cd = 'AIRPORT' THEN
         highest_from_type.alt_id
   END                              AS ship_from_airport_id,
   ship_from_loc.loc_cd             AS ship_from,
   highest_from_type.loc_type_cd    AS ship_from_type,
   CASE
      WHEN highest_from_type.loc_type_cd = 'AIRPORT' THEN
         highest_from_type.loc_cd
   END                              AS ship_from_airport_code,
   --
   ship_to_loc.alt_id               AS ship_to_id,
   CASE
      WHEN highest_to_type.loc_type_cd = 'AIRPORT' THEN
         highest_to_type.alt_id
   END                              AS ship_to_airport_id,
   ship_to_loc.loc_cd               AS ship_to,
   highest_to_type.loc_type_cd    AS ship_to_type,
   CASE
      WHEN highest_to_type.loc_type_cd = 'AIRPORT' THEN
         highest_to_type.loc_cd
   END                              AS ship_to_airport_code,
   --
   ship_segment.segment_status_cd   AS status,
   -- completion information
   ship_segment.complete_dt         AS completion_date,
   org_hr.alt_id                    AS user_id,
   CASE
      WHEN ship_segment.complete_dt IS NOT NULL THEN
       (utl_user.last_name ||', '|| utl_user.first_name)
   END                              AS completion_user,
   ship_segment.waybill_sdesc       AS waybill_number,
   ship_segment.customs_sdesc       AS customs_number
FROM
   ship_shipment
   INNER JOIN ship_segment_map ON
      ship_shipment.shipment_db_id = ship_segment_map.shipment_db_id AND
      ship_shipment.shipment_id   = ship_segment_map.shipment_id
   INNER JOIN ship_segment ON
      ship_segment_map.segment_db_id = ship_segment.segment_db_id AND
      ship_segment_map.segment_id    = ship_segment.segment_id
   -- ship from
   INNER JOIN rvw_inv_loc ship_from_loc ON
      ship_segment.ship_from_db_id = ship_from_loc.loc_db_id AND
      ship_segment.ship_from_id    = ship_from_loc.loc_id
   INNER JOIN rvw_location highest_from ON
      ship_from_loc.alt_id = highest_from.location_id
   INNER JOIN rvw_inv_loc highest_from_type ON
      highest_from.airport_alt_id = highest_from_type.alt_id
   -- ship to
   INNER JOIN rvw_inv_loc ship_to_loc ON
      ship_segment.ship_to_db_id = ship_to_loc.loc_db_id AND
      ship_segment.ship_to_id    = ship_to_loc.loc_id
   INNER JOIN rvw_location highest_to ON
      ship_to_loc.alt_id = highest_to.location_id
   INNER JOIN rvw_inv_loc highest_to_type ON
      highest_to.airport_alt_id = highest_to_type.alt_id
   -- completion user
   LEFT JOIN org_hr ON
      ship_segment.complete_hr_db_id = org_hr.hr_db_id AND
      ship_segment.complete_hr_id    = org_hr.hr_id
   LEFT JOIN utl_user ON
      org_hr.user_id = utl_user.user_id
;