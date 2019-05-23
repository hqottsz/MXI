--liquibase formatted sql


--changeSet acor_repair_orders_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_repair_orders_v1
AS
SELECT
   wp.barcode AS work_package_barcode,
   po_header.wp_id AS work_package_id,
   po_header.order_id,
   po_header.Order_type_code,
   po_header.Priority,
   po_header.vendor_account_code,
   po_header.currency_code,
   po_header.total_amount,
   po_header.Terms_Conditions,
   po_header.Freight_On_Board,
   po_header.Transportation_Type,
   po_header.VENDOR_NOTE,
   po_header.issue_number,
   po_header.issued_date,
   po_header.Issued_by,
   po_header.closed_date,
   po_header.SHIP_TO_CODE,
   po_header.RECEIVE_NOTE,
   po_header.status,
   po_header.Order_Number,
   po_header.Authorization,
   po_header.Authorized_By,
   po_header.authorization_date,
   po_header.vendor_id,
   po_header.Purchasing_Contact,
   po_header.promised_by_date,
   po_header.receiving_organization,
   po_header.ship_loc_id,
   po_header.terms_condition_short_desc,
   po_header.terms_condition_long_desc,
   po_header.needed_by_date,
   po_header.po_db_id,
   po_header.po_id,
   po_header.last_mod_dt,
   po_header.ext_key_sdesc
FROM
   acor_orders_v1 po_header
   LEFT JOIN acor_workpackages_v1 wp ON
      po_header.wp_id = wp.sched_id
WHERE
   po_header.Order_type_code = 'REPAIR'
;