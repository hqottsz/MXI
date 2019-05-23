--liquibase formatted sql


--changeSet acor_rfq_filled_requests_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_rfq_filled_requests_v1
AS
SELECT
   rfq_line.line_no_ord AS line_number,
   eqp_part_no.part_no_oem AS requested_part,
   eqp_part_no.alt_id AS part_id,
   req_part.alt_id AS part_request_id
FROM
   rfq_line
   INNER JOIN req_part ON
      req_part.rfq_db_id   = rfq_line.rfq_db_id AND
      req_part.rfq_id      = rfq_line.rfq_id AND
      req_part.rfq_line_id = rfq_line.rfq_line_id
   INNER JOIN eqp_part_no ON
      eqp_part_no.part_no_db_id = rfq_line.part_no_db_id AND
      eqp_part_no.part_no_id    = rfq_line.part_no_id;