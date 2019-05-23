--liquibase formatted sql


--changeSet acor_rfq_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_rfq_v1
AS 
SELECT
   evt_event.event_sdesc AS rfq_number,
   ref_event_status.user_status_cd AS status,
   rfq_header.respond_by_dt AS respond_by_date,
   (
      SELECT
         MIN( req_part.req_by_dt )
      FROM
         req_part
      WHERE
         req_part.rfq_db_id = rfq_header.rfq_db_id AND
         req_part.rfq_id    = rfq_header.rfq_id
   ) AS needed_by_date,
   utl_user.first_name || ' ' || utl_user.last_name AS purchasing_contact,
   evt_event.ext_key_sdesc AS external_ref,
   rfq_header.currency_cd AS desired_currency,
   DECODE( rfq_header.exchg_qt, NULL, NULL, rfq_header.exchg_qt || ' ' || (select currency_cd from ref_currency where default_bool=1) || '/' || rfq_header.currency_cd ) AS exchange_rate,
   rfq_header.terms_conditions_cd AS terms_conditions,
   rfq_header.transport_type_cd AS transportation_type,
   rfq_header.fob_cd AS freight_on_board,
   rfq_header.vendor_note,
   rfq_header.spec2k_cust_cd AS spec2000_customer_code,
   rfq_header.alt_id AS rfq_id
FROM
   rfq_header
   INNER JOIN evt_event ON
      evt_event.event_db_id = rfq_header.rfq_db_id AND
      evt_event.event_id    = rfq_header.rfq_id
   INNER JOIN org_hr ON
      org_hr.hr_db_id = rfq_header.contact_hr_db_id AND
      org_hr.hr_id    = rfq_header.contact_hr_id
   INNER JOIN utl_user ON
      utl_user.user_id = org_hr.user_id
   INNER JOIN ref_event_status ON
      evt_event.event_status_db_id = ref_event_status.event_status_db_id AND
      evt_event.event_status_cd    = ref_event_status.event_status_cd
;