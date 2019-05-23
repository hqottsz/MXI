--liquibase formatted sql


--changeSet acor_shipments_lines_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_shipments_lines_v1
AS
SELECT
   ship_shipment.ALT_ID                   AS SHIPMENT_ID,
   ship_shipment_line.line_no_ord         AS Line_Number,
   ship_shipment_line.rcv_priority_cd     AS Receveid_Priority_Code,
   inv_inv.Alt_Id                         AS Inventory_ID,
   inv_inv.serial_no_oem                  AS Serial_Number,
   inv_inv.inv_class_cd                   AS inventory_class,
   ship_shipment_line.serial_no_oem       AS Serial_Number_Received,
   eqp_part_no.Part_No_Oem                AS Part_Number,
   eqp_part_no.manufact_cd                AS Manufacturer_code,
   eqp_part_no.financial_class_cd         AS financial_class,
   ship_shipment_line.receive_qt          AS Received_Quantity,
   ship_shipment_line.expect_qt           AS Expected_Quantity,
   ship_shipment_line.receive_doc_bool    AS Received_Docs_Flag,
   ship_shipment_line.shipment_line_notes AS Line_Notes,
   ship_shipment_line.route_cond_cd       AS Routing_Code,
   po_header.alt_id                       AS Order_ID,
   ship_shipment_line.Po_Line_Id          AS Order_Line_Number
FROM
   ship_shipment_line
   INNER JOIN ship_shipment ON
      ship_shipment_line.shipment_db_id = ship_shipment.shipment_db_id AND
      ship_shipment_line.shipment_id    = ship_shipment.shipment_id
   LEFT JOIN inv_inv ON
      ship_shipment_line.Inv_No_Db_Id = inv_inv.Inv_No_Db_Id AND
      ship_shipment_line.inv_no_id    = inv_inv.inv_no_id
   LEFT JOIN eqp_part_no ON
      ship_shipment_line.Part_No_Db_Id = eqp_part_no.Part_No_Db_Id AND
      ship_shipment_line.Part_No_Id    = eqp_part_no.Part_No_Id
   LEFT JOIN po_header ON
      ship_shipment_line.Po_Db_Id = po_header.po_db_id AND
      ship_shipment_line.Po_Id    = po_header.po_id;