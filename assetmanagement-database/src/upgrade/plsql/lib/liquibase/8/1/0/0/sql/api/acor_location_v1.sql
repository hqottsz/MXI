--liquibase formatted sql


--changeSet acor_location_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_location_v1
AS 
WITH
rvw_location
   (
       loc_db_id,
       loc_id,
       airport_db_id,
       airport_id,
       airport_alt_id,
       nh_loc_db_id,
       nh_loc_id,
       loc_type_db_id,
       loc_type_cd,
       loc_cd,
       timezone_cd,
       supply_loc_db_id,
       supply_loc_id,
       supply_bool,
       vendor_db_id,
       vendor_id,
       country_cd,
       city_name,
       alt_id
  )
AS
  (
    SELECT
       loc_db_id,
       loc_id,
       loc_db_id airport_db_id,
       loc_id airport_id,
       alt_id aiport_alt_id,
       nh_loc_db_id,
       nh_loc_id,
       loc_type_db_id,
       loc_type_cd,
       loc_cd,
       timezone_cd,
       supply_loc_db_id,
       supply_loc_id,
       supply_bool,
       vendor_db_id,
       vendor_id,
       country_cd,
       city_name,
       alt_id
    FROM
      inv_loc
    WHERE
       nh_loc_id IS NULL
    UNION ALL
    SELECT
       sub_loc.loc_db_id,
       sub_loc.loc_id,
       tree_loc.airport_db_id,
       tree_loc.airport_id,
       tree_loc.alt_id airport_alt_id,
       sub_loc.nh_loc_db_id,
       sub_loc.nh_loc_id,
       sub_loc.loc_type_db_id,
       sub_loc.loc_type_cd,
       sub_loc.loc_cd,
       tree_loc.timezone_cd,
       sub_loc.supply_loc_db_id,
       sub_loc.supply_loc_id,
       sub_loc.supply_bool,
       sub_loc.vendor_db_id,
       sub_loc.vendor_id,
       sub_loc.country_cd,
       sub_loc.city_name,
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
      loc_id
   FROM
      inv_loc
)
SELECT
    rvw_location.alt_id          AS location_id,
    rvw_location.airport_alt_id  AS airport_alt_id,
    nh_loc.alt_id                AS nh_location_id,
 --   rvw_location.loc_type_db_id,
    rvw_location.loc_type_cd,
    rvw_location.loc_cd,
    rvw_location.timezone_cd,
    utl_timezone.user_cd,
    supply_loc.alt_id           AS supply_location_id,
    rvw_location.supply_bool,
    org_vendor.alt_id           AS vendor_id,
    country_cd,
    rvw_location.city_name
FROM
   rvw_location
   LEFT JOIN rvw_inv_loc nh_loc ON
      rvw_location.nh_loc_db_id = nh_loc.loc_db_id AND
      rvw_location.nh_loc_id    = nh_loc.loc_id
   LEFT JOIN rvw_inv_loc supply_loc ON
      rvw_location.supply_loc_db_id = supply_loc.loc_db_id AND
      rvw_location.supply_loc_id    = supply_loc.loc_id
   INNER JOIN utl_timezone ON
      rvw_location.timezone_cd = utl_timezone.timezone_cd
   LEFT JOIN org_vendor ON
      rvw_location.vendor_db_id = org_vendor.vendor_db_id AND
      rvw_location.vendor_id    = org_vendor.vendor_id
;