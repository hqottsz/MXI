--liquibase formatted sql


--changeSet acor_location_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_location_v1
AS 
WITH
rvw_location
   (
       loc_db_id,
       loc_id,
       nh_loc_db_id,
       nh_loc_id,
       loc_type_db_id,
       loc_type_cd,
       loc_cd,
       timezone_cd,
       supply_loc_db_id,
       supply_loc_id,
       supply_bool,
       hub_loc_db_id,
       hub_loc_id,
       vendor_db_id,
       vendor_id,
       country_db_id,
       country_cd,
       city_name,
       zip_cd,
       address_pmail_1,
       address_pmail_2,
       shipping_time,
       track_capacity_qt,
       alt_id
  )
AS
  (
    SELECT
       loc_db_id,
       loc_id,
       nh_loc_db_id,
       nh_loc_id,
       loc_type_db_id,
       loc_type_cd,
       loc_cd,
       timezone_cd,
       supply_loc_db_id,
       supply_loc_id,
       supply_bool,
       hub_loc_db_id,
       hub_loc_id,
       vendor_db_id,
       vendor_id,
       country_db_id,       
       country_cd,
       city_name,
       zip_cd,
       address_pmail_1,
       address_pmail_2,
       shipping_time,
       track_capacity_qt,
       alt_id
    FROM
      inv_loc     
    WHERE
       nh_loc_id IS NULL       
    UNION ALL
    SELECT
       sub_loc.loc_db_id,
       sub_loc.loc_id,
       sub_loc.nh_loc_db_id,
       sub_loc.nh_loc_id,
       sub_loc.loc_type_db_id,
       sub_loc.loc_type_cd,
       sub_loc.loc_cd,
       tree_loc.timezone_cd,
       sub_loc.supply_loc_db_id,
       sub_loc.supply_loc_id,
       sub_loc.supply_bool,
       sub_loc.hub_loc_db_id,
       sub_loc.hub_loc_id,
       sub_loc.vendor_db_id,
       sub_loc.vendor_id,
       sub_loc.country_db_id,
       sub_loc.country_cd,
       sub_loc.city_name,
       sub_loc.zip_cd,
       sub_loc.address_pmail_1,
       sub_loc.address_pmail_2,
       sub_loc.shipping_time,
       sub_loc.track_capacity_qt,       
       sub_loc.alt_id
    FROM
      inv_loc sub_loc
      INNER JOIN rvw_location tree_loc ON
         sub_loc.nh_loc_db_id = tree_loc.loc_db_id AND
         sub_loc.nh_loc_id    = tree_loc.loc_id
  ),
rvw_inv_loc
AS
(
   SELECT
      alt_id,
      loc_db_id,
      loc_id,
      loc_cd
   FROM
      inv_loc
)
SELECT
    rvw_location.alt_id          AS location_id,
    supply_loc.alt_id            AS airport_alt_id,
    nh_loc.alt_id                AS nh_location_id,
    rvw_location.loc_type_cd,
    rvw_location.loc_cd,
    rvw_location.timezone_cd,
    utl_timezone.user_cd,
    supply_loc.alt_id           AS supply_location_id,
    supply_loc.loc_cd           AS supply_location_cd,
    hub_loc.alt_id              AS supplier_location_id,
    hub_loc.loc_cd              AS supplier_location_cd,    
    rvw_location.supply_bool,
    org_vendor.alt_id           AS vendor_id,
    ref_country.country_cd,
    ref_country.country_name,
    rvw_location.city_name,
    (rvw_location.address_pmail_1 || ' ' || rvw_location.address_pmail_2) address,
    rvw_location.zip_cd,
    rvw_location.shipping_time,
    rvw_location.track_capacity_qt
FROM
   rvw_location
   LEFT JOIN rvw_inv_loc nh_loc ON
      rvw_location.nh_loc_db_id = nh_loc.loc_db_id AND
      rvw_location.nh_loc_id    = nh_loc.loc_id
    -- supply location
   LEFT JOIN rvw_inv_loc supply_loc ON
      rvw_location.supply_loc_db_id = supply_loc.loc_db_id AND
      rvw_location.supply_loc_id    = supply_loc.loc_id
   -- hub/supplier location 
   LEFT JOIN rvw_inv_loc hub_loc ON
      rvw_location.hub_loc_db_id = hub_loc.loc_db_id AND
      rvw_location.hub_loc_id    = hub_loc.loc_id   
   INNER JOIN utl_timezone ON
      rvw_location.timezone_cd = utl_timezone.timezone_cd
   LEFT JOIN org_vendor ON
      rvw_location.vendor_db_id = org_vendor.vendor_db_id AND
      rvw_location.vendor_id    = org_vendor.vendor_id
   LEFT JOIN ref_country ON
      rvw_location.country_db_id = ref_country.country_db_id AND
      rvw_location.country_cd    = ref_country.country_cd     
;            