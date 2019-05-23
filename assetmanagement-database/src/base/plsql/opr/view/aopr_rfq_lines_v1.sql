--liquibase formatted sql


--changeSet aopr_rfq_lines_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_rfq_lines_v1
AS
SELECT
   line_number,
   line_description,
   ACOR_RFQ_LINES_V1.part_id,
   acor_part_number_v1.part_number,
   acor_part_number_v1.manufact_cd AS part_manufacturer_code,
   quantitiy,
   acor_accounts_v1.account_cd AS account,
   acor_location_v1.loc_cd AS ship_to_location_code,
   priority,
   vendor_note,
   resulting_po_number,
   po_line_id,
   rfq_line_id,
   acor_accounts_v1.ID AS account_id,
   acor_location_v1.location_id AS ship_to_location_id
FROM
   ACOR_RFQ_LINES_V1
   LEFT JOIN acor_location_v1 ON
      acor_location_v1.location_id = acor_rfq_lines_v1.ship_to_location_id
   INNER JOIN acor_accounts_v1 ON
      acor_accounts_v1.ID = acor_rfq_lines_v1.account_id
   LEFT JOIN acor_part_number_v1 ON
      acor_rfq_lines_v1.part_id = acor_part_number_v1.part_id
;