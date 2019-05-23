--liquibase formatted sql


--changeSet acor_vendors_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_vendors_v1
AS 
SELECT
   org_vendor.alt_id         AS vendor_id,
   inv_loc.alt_id            AS location_id,
   inv_owner.alt_id          AS owner_id,
   vendor_cd                 AS Vendor_Code,
   cert_cd                   AS Certification_Code,
   cert_expiry_dt            AS Certification_Expiry_Date,
   vendor_Type_cd            AS Vendor_Type_Code,
   currency_cd               AS Currency_Code,
   vendor_Name,
   vendor_Note,
   org_vendor.rstat_cd       AS RecordStatus,
   DECODE(org_vendor.rstat_cd,0,'Active','Inactive')
                             AS RecordStatus_code,
   no_print_req_bool         AS SPEC2000Status,
   DECODE(no_print_req_bool,0,'Y','N')
                             AS SPEC2000Flag,
   vendor_approval_cd        AS vendor_approval_code,
   terms_conditions_cd       AS terms_conditions_code,
   min_purchase_amount       AS minimum_purchase_amount,
   org_vendor.ext_key_sdesc  AS External_Key,
   borrow_rate_cd,
   receive_note,
   inv_loc.address_pmail_1   AS address_pmail,
   inv_loc.city_name,
   inv_loc.state_cd          AS state_code,
   inv_loc.country_cd        AS country_code,
   ref_country.country_name,
   inv_loc.zip_cd            AS zip_code,
   inv_loc_contact.contact_name AS vendor_contact_name
FROM
   org_vendor
   LEFT JOIN inv_loc ON
      org_vendor.vendor_loc_db_id = inv_loc.loc_db_id AND
      org_vendor.vendor_loc_id    = inv_loc.loc_id
   LEFT JOIN inv_owner ON
      org_vendor.owner_db_id = inv_owner.owner_db_id AND
      org_vendor.owner_id    = inv_owner.owner_id
   LEFT JOIN inv_loc_contact ON
      org_vendor.vendor_loc_db_id = inv_loc_contact.loc_db_id AND
      org_vendor.vendor_loc_id    = inv_loc_contact.loc_id
   LEFT JOIN ref_country ON
      inv_loc.country_db_id = ref_country.country_db_id AND
      inv_loc.country_cd    = ref_country.country_cd
;