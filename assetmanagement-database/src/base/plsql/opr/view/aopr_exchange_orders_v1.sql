--liquibase formatted sql


--changeSet aopr_exchange_orders_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_exchange_orders_v1
AS
SELECT
   po_header.order_id,
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
   org_vendor.Vendor_Code,
   org_vendor.Vendor_Name,
   po_header.Purchasing_Contact,
   po_header.promised_by_date,
   po_header.receiving_organization,
   ship_to_loc.city_name AS ship_to_location,
   ship_to_loc.country_cd AS country_code,
   po_header.vendor_id,
   po_header.terms_condition_short_desc,
   po_header.terms_condition_long_desc,
   po_header.needed_by_date
FROM
   acor_exchange_orders_v1 po_header
   LEFT JOIN acor_vendors_v1 org_vendor ON
      po_header.vendor_id = org_vendor.vendor_id
   LEFT JOIN acor_location_v1 ship_to_loc ON
      po_header.ship_loc_id = ship_to_loc.location_id
;