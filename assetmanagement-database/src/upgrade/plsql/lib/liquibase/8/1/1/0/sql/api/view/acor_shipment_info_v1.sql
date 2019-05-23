--liquibase formatted sql


--changeSet acor_shipment_info_v1:1 stripComments:false
CREATE OR REPLACE VIEW acor_shipment_info_v1 
AS
SELECT
    ship_shipment.alt_id                 AS shipment_id,
    evt_event.event_sdesc                AS barcode,
    ship_shipment.shipment_type_cd       AS shipment_type_code,
    ship_shipment.shipment_reason_cd     AS shipment_reason_code,
    ship_shipment.transport_type_cd      AS transport_type_code,
    ship_shipment.req_priority_cd        AS priority_code,
    ship_shipment.ship_by_dt             AS ship_by_date,
    ship_shipment.ship_after_dt          AS ship_after_date,
    evt_event.event_status_cd            AS shipment_status_code
FROM
    ship_shipment
    INNER JOIN evt_event ON
    evt_event.event_db_id = ship_shipment.shipment_db_id AND
    evt_event.event_id    = ship_shipment.shipment_id
;