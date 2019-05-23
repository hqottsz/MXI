--liquibase formatted sql


--changeSet acor_shipments_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_shipments_v1 AS
WITH
   rvw_shipment_routing
AS
  (
   SELECT
      shipment_id,
      route_order,
      ship_from_id,
      ship_from_airport_id,
      ship_from,
      ship_from_type,
      ship_from_airport_code,
      ship_to_id,
      ship_to_airport_id,
      ship_to,
      ship_to_type,
      ship_to_airport_code,
      ship_to_address_pmail_1,
      ship_to_address_pmail_2,
      ship_to_city_name,
      ship_to_state_cd,
      ship_to_country_cd,
      ship_to_zip_cd,
      completion_date
   FROM
      acor_shipments_routing_v1
  )
SELECT
   ship_shipment.alt_id                AS shipment_id,
   evt_event.event_sdesc               AS barcode,
   po_header.alt_id                    AS order_id,
   ship_shipment.shipment_type_cd      AS type_code,
   ship_shipment.shipment_reason_cd    AS reason_code,
   ship_shipment.transport_type_cd     AS tranport_type_code,
   ship_shipment.size_class_cd         AS size_class_code,
   ref_event_status.user_status_cd     AS status,
   ship_shipment.waybill_sdesc         AS waybill_number,
   ship_shipment.customs_declaration   AS customs_declaration,
   ship_shipment.use_flight_desc       AS use_flight_number,
   ship_shipment.carrier_name          AS carrier_name,
   ship_shipment.rma_sdesc             AS rma_number,
   ship_shipment.return_price          AS return_price,
   ship_shipment.return_auth_no        AS return_authorization_number,
   ship_shipment.confirm_receipt_bool  AS receipt_confirmation_flag,
   ship_shipment.req_priority_cd       AS priority,
   ship_shipment.ship_by_dt            AS ship_by_date,
   ship_shipment.ship_after_dt         AS ship_after_date,
   ship_shipment.weight_eng_unit_cd    AS weight_unitofmeasure,
   ship_shipment.weight_qt             AS weight,
   ship_shipment.dimension_eng_unit_cd AS dimension_unitofmeasure,
   ship_shipment.width_qt              AS width,
   ship_shipment.length_qt             AS length,
   ship_shipment.height_qt             AS height,
   ship_shipment.customs_sdesc         AS customs_number,
   ship_shipment.flight_status         AS flight_status,
   sched_stask.alt_id                  AS work_package_id,
   creation_stage.stage_dt             AS creation_date,
   utl_user.username                   AS user_name,
   utl_user.first_name                 AS user_first_name,
   utl_user.last_name                  AS user_last_name,
   -- from
   first_shipment_route.ship_from_id,
   first_shipment_route.ship_from            AS ship_from_code,
   first_shipment_route.ship_from_airport_id,
   first_shipment_route.ship_from_airport_code,
   evt_event.actual_start_dt                AS shipment_date,
   -- to
   first_shipment_route.ship_to_id,
   first_shipment_route.ship_to              AS ship_to_code,
   first_shipment_route.ship_to_airport_id,
   first_shipment_route.ship_to_airport_code,
   -- final destination
   final_shipment_route.ship_to_id           AS final_destination_id,
   final_shipment_route.ship_to              AS final_destination_code,
   final_shipment_route.ship_to_airport_id   AS final_destination_airport_id,
   final_shipment_route.ship_to_airport_code AS final_destination_airport_code,
   final_shipment_route.ship_to_address_pmail_1 AS final_dest_address_pmail_1,
   final_shipment_route.ship_to_address_pmail_2 AS final_dest_address_pmail_2,
   final_shipment_route.ship_to_city_name       AS final_dest_city_name,
   final_shipment_route.ship_to_state_cd        AS final_dest_state_cd,
   final_shipment_route.ship_to_country_cd      AS final_dest_country_cd,
   final_shipment_route.ship_to_zip_cd          AS final_dest_zip_cd,
   final_shipment_route.completion_date      AS received_date
FROM
   ship_shipment
   INNER JOIN evt_event ON
      ship_shipment.shipment_db_id = evt_event.event_db_id AND
      ship_shipment.shipment_id    = evt_event.event_id
   -- shipment was created
   INNER JOIN evt_stage creation_stage ON
      evt_event.event_db_id = creation_stage.event_db_id AND
      evt_event.event_id    = creation_stage.event_id
      AND
      creation_stage.event_status_db_id = 0 AND
      creation_stage.event_status_cd    = 'IXPEND'
      AND
      creation_stage.system_bool = 1
   -- user details
   INNER JOIN org_hr ON
      creation_stage.hr_db_id =  org_hr.hr_db_id AND
      creation_stage.hr_id    =  org_hr.hr_id
   INNER JOIN utl_user ON
      org_hr.user_id = utl_user.user_id
   -- first route
   INNER JOIN rvw_shipment_routing first_shipment_route ON
      ship_shipment.alt_id             = first_shipment_route.shipment_id AND
      first_shipment_route.route_order = 1
   -- final route
   INNER JOIN
      (-- get the maximum route order
         SELECT
            shipment_db_id,
            shipment_id,
            MAX(segment_ord) segment_ord
         FROM
            ship_segment_map
         GROUP BY
            shipment_db_id,
            shipment_id
      ) last_ship_segment ON
      ship_shipment.shipment_db_id = last_ship_segment.shipment_db_id AND
      ship_shipment.shipment_id    = last_ship_segment.shipment_id
   INNER JOIN  rvw_shipment_routing final_shipment_route ON
      ship_shipment.alt_id             = final_shipment_route.shipment_id AND
      final_shipment_route.route_order = last_ship_segment.segment_ord
   LEFT JOIN po_header ON
      ship_shipment.po_db_id = po_header.po_db_id AND
      ship_shipment.po_id    = po_header.po_id
   LEFT JOIN sched_stask ON
      ship_shipment.check_db_id = sched_stask.sched_db_id AND
      ship_shipment.check_id    = sched_stask.sched_id
   LEFT JOIN ref_event_status ON
      evt_event.event_status_db_id = ref_event_status.event_status_db_id AND
      evt_event.event_status_cd    = ref_event_status.event_status_cd
;