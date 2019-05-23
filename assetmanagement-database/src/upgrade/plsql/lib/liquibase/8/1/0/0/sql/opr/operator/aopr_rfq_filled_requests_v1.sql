--liquibase formatted sql


--changeSet aopr_rfq_filled_requests_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_rfq_filled_requests_v1
AS 
SELECT
   line_number,
   requested_part,
   part_id,
   part_request_id
FROM
   ACOR_RFQ_FILLED_REQUESTS_V1;