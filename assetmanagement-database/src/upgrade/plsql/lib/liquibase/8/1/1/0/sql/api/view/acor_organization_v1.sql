--liquibase formatted sql


--changeSet acor_organization_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_organization_v1
AS 
SELECT
   org_org.alt_id      AS org_id,
   org_cd              AS organization_code,
   org_sdesc           AS organization_name,
   org_type_cd         AS organization_type_code,
   org_sub_type_cd     AS organization_sub_type_code,
   blob_data.blob_data AS logo,
   DECODE(org_address.address_line1,NULL,org_address.address_line2,org_address.address_line1||' '||org_address.address_line2) AS address,
   ref_country.country_name,
   ref_state.state_name,
   org_address.city_name,
   org_address.zip_cd AS zip_code,
   org_contact.contact_name AS contact_name,
   org_contact.job_title AS job_title,
   org_contact.phone_ph AS phone_number,
   org_contact.fax_ph AS fax_number,
   org_contact.address_email AS email_address
FROM
   org_org
   LEFT JOIN org_logo ON
      org_org.org_db_id = org_logo.org_db_id AND
      org_org.org_id    = org_logo.org_id
   LEFT JOIN cor_perm_blob blob_data ON
      org_logo.blob_db_id = blob_data.blob_db_id AND
      org_logo.blob_id    = blob_data.blob_id
   LEFT JOIN org_address_list ON
      org_org.org_db_id = org_address_list.org_db_id AND
      org_org.org_id    = org_address_list.org_id
   LEFT JOIN org_address ON
      org_address_list.address_db_id = org_address.address_db_id AND
      org_address_list.address_id    = org_address.address_id
   LEFT JOIN ref_country ON
      org_address.country_db_id = ref_country.country_db_id AND
      org_address.country_cd    = ref_country.country_cd
   LEFT JOIN ref_state ON
      org_address.country_db_id = ref_state.country_db_id AND
      org_address.country_cd    = ref_state.country_cd AND
      org_address.state_cd      = ref_state.state_cd
   LEFT JOIN org_contact_list ON
      org_org.org_db_id = org_contact_list.org_db_id AND
      org_org.org_id    = org_contact_list.org_id
   LEFT JOIN org_contact ON
      org_contact_list.contact_db_id = org_contact.contact_db_id AND
      org_contact_list.contact_id    = org_contact.contact_id
;