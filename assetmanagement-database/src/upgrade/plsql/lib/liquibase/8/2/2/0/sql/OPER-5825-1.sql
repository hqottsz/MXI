--liquibase formatted sql


--changeSet OPER-5825-1:1 stripComments:false
-- update inventory finance status code to RCVD where RO inventory is just received from the vendor
-- And it has not been inspected as serviceable yet
UPDATE
   inv_inv
SET
   inv_inv.finance_status_cd = 'RCVD'   
WHERE
   EXISTS
    (
      SELECT
        1
      FROM
        po_header
        INNER JOIN ship_shipment ON
           ship_shipment.po_db_id = po_header.po_db_id AND
           ship_shipment.po_id = po_header.po_id
        INNER JOIN ship_shipment_line ON
           ship_shipment_line.shipment_db_id = ship_shipment.shipment_db_id AND
           ship_shipment_line.shipment_id = ship_shipment.shipment_id
        INNER JOIN evt_event ro_event ON
           ro_event.event_db_id = po_header.po_db_id AND
           ro_event.event_id = po_header.po_id
        INNER JOIN evt_event ship_event ON
           ship_event.event_db_id = ship_shipment.shipment_db_id AND
           ship_event.event_id = ship_shipment.shipment_id
      WHERE
         -- join order to inventory
         po_header.po_db_id = inv_inv.po_db_id AND
         po_header.po_id = inv_inv.po_id
         AND 
         -- join shipment line to inventory
         inv_inv.inv_no_db_id = ship_shipment_line.inv_no_db_id AND
         inv_inv.inv_no_id = ship_shipment_line.inv_no_id
         -- to get inbound shipments
         AND
         ship_shipment.shipment_type_db_id = 0 AND
         ship_shipment.shipment_type_cd = 'REPAIR'
         AND
         -- to get complete shipments
         ship_event.event_status_db_id = 0 AND
         ship_event.event_status_cd = 'IXCMPLT'
         AND
         -- to get active repair orders
         ro_event.hist_bool = 0
         AND
         -- to get those inventories where finance status code is 'INSP'.
         inv_inv.finance_status_cd = 'INSP'
         AND
         -- to get those inventories where condition is Inspection required
         inv_inv.inv_cond_db_id = 0 AND
         inv_inv.inv_cond_cd = 'INSPREQ'
    );