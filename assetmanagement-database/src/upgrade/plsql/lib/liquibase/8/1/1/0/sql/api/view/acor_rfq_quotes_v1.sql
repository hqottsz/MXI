--liquibase formatted sql


--changeSet acor_rfq_quotes_v1:1 stripComments:false
CREATE OR REPLACE VIEW ACOR_RFQ_QUOTES_V1 AS
SELECT
   rfq_line.line_no_ord AS line_number,
   org_vendor.alt_id AS vendor_id,
   rfq_line_vendor.quote_qt AS quote_qty,
   part_price.unit_price,
   rfq_line_vendor.line_price,
   part_price.lead_time,
   part_price.effective_to_dt AS effective_to_date,
   rfq_line_vendor.vendor_note,
   rfq_line_vendor.quote_dt AS quote_date,
   rfq_line_vendor.doc_ref_sdesc AS quote_number,
   CASE
      WHEN
         DECODE( part_price.part_vendor_price_db_id, NULL, NULL,eqp_part_vendor.pref_bool) = 0
      THEN
         'Y'
      WHEN
         DECODE( part_price.part_vendor_price_db_id, NULL, NULL,eqp_part_vendor.pref_bool) = 1
      THEN
         'N'
      ELSE
         ''
   END AS quote_detail_prefered,
   DECODE( part_price.part_vendor_price_db_id, NULL, NULL,eqp_part_vendor.vendor_status_cd) AS quote_detail_vendor_status,
   rfq_header.alt_id AS rfq_id
FROM
   rfq_vendor,
   rfq_line,
   rfq_header,
   eqp_part_no,
   rfq_line_vendor,
   org_vendor,
   eqp_part_vendor,
   TABLE( part_price_pkg.getlowestpartprice( rfq_line_vendor.vendor_db_id,
                                             rfq_line_vendor.vendor_id,
                                             rfq_line_vendor.part_no_db_id,
                                             rfq_line_vendor.part_no_id,
                                             NULL,
                                             NULL,
                                             NULL
                                           )
   ) part_price
WHERE
   rfq_line.rfq_db_id = rfq_vendor.rfq_db_id AND
   rfq_line.rfq_id    = rfq_vendor.rfq_id
   AND
   rfq_line.part_no_db_id IS NOT NULL
   AND
   eqp_part_no.part_no_db_id = rfq_line.part_no_db_id AND
   eqp_part_no.part_no_id    = rfq_line.part_no_id
   AND
   rfq_line_vendor.rfq_db_id   = rfq_line.rfq_db_id AND
   rfq_line_vendor.rfq_id      = rfq_line.rfq_id AND
   rfq_line_vendor.rfq_line_id = rfq_line.rfq_line_id
   AND
   rfq_line_vendor.vendor_db_id = rfq_vendor.vendor_db_id AND
   rfq_line_vendor.vendor_id    = rfq_vendor.vendor_id
   AND
   org_vendor.vendor_db_id = rfq_vendor.vendor_db_id AND
   org_vendor.vendor_id    = rfq_vendor.vendor_id
   AND
   eqp_part_vendor.vendor_db_id  (+)= rfq_line_vendor.purch_vendor_db_id AND
   eqp_part_vendor.vendor_id     (+)= rfq_line_vendor.purch_vendor_id AND
   eqp_part_vendor.part_no_db_id (+)= rfq_line_vendor.purch_part_no_db_id AND
   eqp_part_vendor.part_no_id    (+)= rfq_line_vendor.purch_part_no_id
   AND
   part_price.part_vendor_price_db_id (+)= rfq_line_vendor.part_vendor_price_db_id AND
   part_price.part_vendor_price_id    (+)= rfq_line_vendor.part_vendor_price_id
   AND
   rfq_line.rfq_db_id = rfq_header.rfq_db_id AND
   rfq_line.rfq_id    = rfq_header.rfq_id
UNION ALL
SELECT
   rfq_line.line_no_ord AS line_number,
   org_vendor.alt_id AS vendor_id,
   rfq_line_vendor.quote_qt AS quote_qty,
   rfq_line_vendor.unit_price,
   rfq_line_vendor.line_price,
   rfq_line_vendor.lead_time,
   rfq_line_vendor.effective_to_dt AS effective_to_date,
   rfq_line_vendor.vendor_note,
   rfq_line_vendor.quote_dt AS quote_date,
   rfq_line_vendor.doc_ref_sdesc AS quote_number,
   NULL AS quote_detail_prefered,
   NULL AS quote_detail_vendor_status,
   rfq_header.alt_id AS rfq_id
FROM
   rfq_vendor,
   rfq_line,
   rfq_header,
   rfq_line_vendor,
   org_vendor
WHERE
   rfq_line.rfq_db_id = rfq_vendor.rfq_db_id AND
   rfq_line.rfq_id    = rfq_vendor.rfq_id
   AND
   rfq_line.part_no_db_id IS NULL
   AND
   rfq_line_vendor.rfq_db_id   = rfq_line.rfq_db_id AND
   rfq_line_vendor.rfq_id      = rfq_line.rfq_id AND
   rfq_line_vendor.rfq_line_id = rfq_line.rfq_line_id
   AND
   rfq_line_vendor.vendor_db_id = rfq_vendor.vendor_db_id AND
   rfq_line_vendor.vendor_id    = rfq_vendor.vendor_id
   AND
   org_vendor.vendor_db_id = rfq_vendor.vendor_db_id AND
   org_vendor.vendor_id    = rfq_vendor.vendor_id
   AND
   rfq_line.rfq_db_id = rfq_header.rfq_db_id AND
   rfq_line.rfq_id    = rfq_header.rfq_id;