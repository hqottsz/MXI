--liquibase formatted sql


--changeSet DEV-1190:1 stripComments:false
-- migration script to update VW_EVT_PO view query to use isOrderAuthorized() function and remove reference to PO_HEADER.AUTH_BOOL column
CREATE OR REPLACE VIEW vw_evt_po (
   po_db_id,
   po_id,
   po_number,
   po_type_db_id,
   po_type_cd,
   po_type_sdesc,
   vendor_db_id,
   vendor_id,
   vendor_cd,
   event_status_db_id,
   event_status_cd,
   user_status_cd,
   event_status_sdesc,
   issued_dt,
   closed_dt,
   hist_dt,
   hist_bool,
   needed_by_dt,
   promised_by_dt,
   contact_hr_db_id,
   contact_hr_id,
   contact_user_id,
   contact_hr_sdesc,
   contact_username,
   req_priority_db_id,
   req_priority_cd,
   req_priority_sdesc,
   currency_db_id,
   currency_cd,
   currency_sdesc,
   currency_bitmap_db_id,
   currency_bitmap_tag,
   total_price_qt,
   exchg_qt,
   terms_conditions_db_id,
   terms_conditions_cd,
   terms_conditions_sdesc,
   fob_db_id,
   fob_cd,
   fob_sdesc,
   vendor_account_db_id,
   vendor_account_id,
   vendor_account_cd,
   vendor_loc_db_id,
   vendor_loc_id,
   vendor_loc_cd,
   vendor_contact_name,
   vendor_phone_ph,
   org_db_id,
   org_id,
   created_by_org_db_id,
   created_by_org_id,
   ship_to_loc_db_id,
   ship_to_loc_id,
   ship_to_loc_cd,
   ship_to_address_pmail_1,
   ship_to_address_pmail_2,
   ship_to_city_name,
   ship_to_state_cd,
   ship_to_country_db_id,
   ship_to_country_cd,
   ship_to_zip_cd,
   ship_to_code,
   spec2k_cust_db_id,
   spec2k_cust_cd,
   transport_type_db_id,
   transport_type_cd,
   transport_type_sdesc,
   vendor_note,
   ext_key_sdesc, 
   po_auth_flow_db_id, 
   po_auth_flow_cd, 
   po_auth_flow_sdesc,
   borrow_rate_db_id, 
   borrow_rate_cd, 
   borrow_rate_sdesc,
   last_mod_dt,
   receipt_insp_bool,
   auth_bool
) AS
SELECT
      po_header.po_db_id,
      po_header.po_id,
      evt_event.event_sdesc,
      po_header.po_type_db_id,
      po_header.po_type_cd,
      ref_po_type.desc_sdesc,
      po_header.vendor_db_id,
      po_header.vendor_id,
      org_vendor.vendor_cd,
      evt_event.event_status_db_id,
      evt_event.event_status_cd,
      ref_event_status.user_status_cd,
      ref_event_status.desc_sdesc,
      po_header.issued_dt,
      po_header.closed_dt,
      evt_event.event_dt,
      evt_event.hist_bool,
      (
         SELECT
            MIN( req_part.req_by_dt )
         FROM
            req_part
         WHERE
            req_part.po_db_id = po_header.po_db_id AND
            req_part.po_id    = po_header.po_id
      ),
      (
         SELECT
            MAX( po_line.promise_by_dt )
         FROM
            po_line
         WHERE
            po_line.po_db_id = po_header.po_db_id AND
            po_line.po_id    = po_header.po_id AND
            po_line.deleted_bool = 0
      ),
      po_header.contact_hr_db_id,
      po_header.contact_hr_id,
      utl_user.user_id,
      utl_user.last_name || ', ' || utl_user.first_name,
      utl_user.username,
      po_header.req_priority_db_id,
      po_header.req_priority_cd,
      ref_req_priority.desc_sdesc,
      po_header.currency_db_id,
      po_header.currency_cd,
      ref_currency.desc_sdesc,
      ref_currency.bitmap_db_id,
      ref_currency.bitmap_tag,
      (
         SELECT
            SUM( po_line.line_price )
         FROM
            po_line
         WHERE
            po_line.po_db_id = po_header.po_db_id AND
            po_line.po_id    = po_header.po_id AND
            po_line.deleted_bool = 0
      ),
      po_header.exchg_qt,
      po_header.terms_conditions_db_id,
      po_header.terms_conditions_cd,
      ref_terms_conditions.desc_sdesc,
      po_header.fob_db_id,
      po_header.fob_cd,
      ref_fob.desc_sdesc,
      po_header.vendor_account_db_id,
      po_header.vendor_account_id,
      po_header.vendor_account_cd,
      po_header.vendor_loc_db_id,
      po_header.vendor_loc_id,
      vendor_loc.loc_cd,
      vendor_contact.contact_name,
      vendor_contact.phone_ph,
      po_header.org_db_id,
      po_header.org_id,
      po_header.created_by_org_db_id,
      po_header.created_by_org_id,
      po_header.ship_to_loc_db_id,
      po_header.ship_to_loc_id,
      ship_to_loc.loc_cd,
      ship_to_loc.address_pmail_1,
      ship_to_loc.address_pmail_2,
      ship_to_loc.city_name,
      ship_to_loc.state_cd,
      ship_to_loc.country_db_id,
      ship_to_loc.country_cd,
      ship_to_loc.zip_cd,
      po_header.ship_to_code,
      po_header.spec2k_cust_db_id,
      po_header.spec2k_cust_cd,
      po_header.transport_type_db_id,
      po_header.transport_type_cd,
      ref_transport_type.desc_sdesc,
      po_header.vendor_note,
      evt_event.ext_key_sdesc,
      po_header.po_auth_flow_db_id,
      po_header.po_auth_flow_cd,
      ref_po_auth_flow.desc_sdesc,
      po_header.borrow_rate_db_id,
      po_header.borrow_rate_cd,
      ref_borrow_rate.desc_sdesc,
      po_header.last_mod_dt,
      po_header.receipt_insp_bool,
      isOrderAuthorized( po_header.po_db_id, po_header.po_id ) AS auth_bool
   FROM
      evt_event,
      po_header,
      ref_event_status,
      ref_po_type,
      ref_req_priority,
      inv_loc ship_to_loc,
      org_vendor,
      inv_loc vendor_loc,
      inv_loc_contact vendor_contact,
      ref_currency,
      org_hr,
      utl_user,
      ref_terms_conditions,
      ref_fob,
      ref_transport_type,
      ref_po_auth_flow,
      ref_borrow_rate
   WHERE
      evt_event.event_db_id = po_header.po_db_id AND
      evt_event.event_id    = po_header.po_id
      AND
      ref_event_status.event_status_db_id = evt_event.event_status_db_id AND
      ref_event_status.event_status_cd    = evt_event.event_status_cd
      AND
      ref_po_type.po_type_db_id = po_header.po_type_db_id AND
      ref_po_type.po_type_cd    = po_header.po_type_cd
      AND
      ref_req_priority.req_priority_db_id = po_header.req_priority_db_id AND
      ref_req_priority.req_priority_cd    = po_header.req_priority_cd
      AND
      ship_to_loc.loc_db_id = po_header.ship_to_loc_db_id AND
      ship_to_loc.loc_id    = po_header.ship_to_loc_id
      AND
      org_vendor.vendor_db_id = po_header.vendor_db_id AND
      org_vendor.vendor_id    = po_header.vendor_id
      AND
      vendor_loc.loc_db_id = po_header.vendor_loc_db_id AND
      vendor_loc.loc_id    = po_header.vendor_loc_id
      AND
      vendor_contact.loc_db_id    (+)= vendor_loc.loc_db_id AND
      vendor_contact.loc_id       (+)= vendor_loc.loc_id AND
      vendor_contact.default_bool (+)= 1
      AND
      ref_currency.currency_db_id = po_header.currency_db_id AND
      ref_currency.currency_cd    = po_header.currency_cd
      AND
      org_hr.hr_db_id  = po_header.contact_hr_db_id AND
      org_hr.hr_id     = po_header.contact_hr_id AND
      utl_user.user_id = org_hr.user_id
      AND
      ref_terms_conditions.terms_conditions_db_id (+)= po_header.terms_conditions_db_id AND
      ref_terms_conditions.terms_conditions_cd    (+)= po_header.terms_conditions_cd
      AND
      ref_fob.fob_db_id (+)= po_header.fob_db_id AND
      ref_fob.fob_cd    (+)= po_header.fob_cd
      AND
      ref_transport_type.transport_type_db_id (+)= po_header.transport_type_db_id AND
      ref_transport_type.transport_type_cd    (+)= po_header.transport_type_cd
      AND
      ref_po_auth_flow.po_auth_flow_db_id (+)= po_header.po_auth_flow_db_id AND
      ref_po_auth_flow.po_auth_flow_cd    (+)= po_header.po_auth_flow_cd
      AND
      ref_borrow_rate.borrow_rate_db_id (+)= po_header.borrow_rate_db_id AND
      ref_borrow_rate.borrow_rate_cd    (+)= po_header.borrow_rate_cd
;

--changeSet DEV-1190:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- migration script to remove the AUTH_BOOL column from PO_HEADER table
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PO_HEADER', 'AUTH_BOOL');
END;
/