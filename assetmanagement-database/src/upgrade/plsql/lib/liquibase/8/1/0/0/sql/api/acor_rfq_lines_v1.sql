--liquibase formatted sql


--changeSet acor_rfq_lines_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_rfq_lines_v1
AS 
SELECT
   rfq_line.line_no_ord AS line_number,
   rfq_line.line_ldesc AS line_description,
   eqp_part_no.alt_id AS part_id,
   rfq_line.rfq_qt AS quantitiy,
   fnc_account.alt_id AS account_id,
   ship_to_loc.alt_id AS ship_to_location_id,
   rfq_line.req_priority_cd AS priority,
   rfq_line.vendor_note,
   po_evt_event.event_sdesc AS resulting_po_number,
   po_line.alt_id AS po_line_id,
   rfq_line.alt_id AS rfq_line_id
FROM
   rfq_line
   LEFT JOIN inv_loc ship_to_loc ON
      ship_to_loc.loc_db_id = rfq_line.ship_to_loc_db_id AND
      ship_to_loc.loc_id    = rfq_line.ship_to_loc_id
   LEFT JOIN evt_event po_evt_event ON
      po_evt_event.event_db_id = rfq_line.po_db_id AND
      po_evt_event.event_id    = rfq_line.po_id
   INNER JOIN fnc_account ON
      fnc_account.account_db_id = rfq_line.account_db_id AND
      fnc_account.account_id    = rfq_line.account_id
   LEFT JOIN po_line ON
      rfq_line.po_db_id = po_line.po_db_id AND
      rfq_line.po_id    = po_line.po_id AND
      rfq_line.po_line_id = po_line.po_line_id
   LEFT JOIN eqp_part_no ON
      rfq_line.part_no_db_id = eqp_part_no.part_no_db_id AND
      rfq_line.part_no_id = eqp_part_no.part_no_id;