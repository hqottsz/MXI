--liquibase formatted sql


--changeSet DEV-463:1 stripComments:false
INSERT INTO utl_config_parm (parm_value, parm_name, parm_type, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id)
SELECT 'false', 'ACTION_INCLUDE_ALL_VENDOR_ORGANIZATIONS', 'SECURED_RESOURCE','Permission to view all vendors.','USER', 'TRUE/FALSE', 'FALSE', 1,'Org - Organizations', '7.1',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_INCLUDE_ALL_VENDOR_ORGANIZATIONS');

--changeSet DEV-463:2 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_INCLUDE_ALL_VENDOR_ORGANIZATIONS', 'SECURED_RESOURCE','OPER' 
FROM DUAL WHERE NOT EXISTS (
SELECT 1 FROM db_type_config_parm WHERE 
db_type_config_parm.parm_name = 'ACTION_INCLUDE_ALL_VENDOR_ORGANIZATIONS' AND
db_type_config_parm.db_type_cd = 'OPER');

--changeSet DEV-463:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION isAuthorizedForVendor
(
   aUserId      utl_user.user_id%TYPE,
   aVendorDbId  org_vendor.vendor_db_id%TYPE,
   aVendorId    org_vendor.vendor_id%TYPE
)  RETURN NUMBER
IS
  lAuthorizedForVendor NUMBER;
BEGIN
   -- If the user is allowed seeing all vendors, show all!
   lAuthorizedForVendor := isAuthorizedForResource( aUserId, 'ACTION_INCLUDE_ALL_VENDOR_ORGANIZATIONS');

   -- Otherwise, show vendors that are on the user's organization's vendor list
   IF lAuthorizedForVendor <> 1 THEN
      SELECT
         DECODE( count(*), 0, 0, 1) INTO lAuthorizedForVendor
      FROM
         org_hr
         INNER JOIN org_org_hr ON
            org_org_hr.hr_db_id = org_hr.hr_db_id AND
            org_org_hr.hr_id = org_hr.hr_id
         INNER JOIN org_org_vendor ON
            org_org_vendor.org_db_id = org_org_hr.org_db_id AND
            org_org_vendor.org_id = org_org_hr.org_id
      WHERE
         org_hr.user_id = aUserId
         AND
         org_org_vendor.vendor_db_id = aVendorDbId AND
         org_org_vendor.vendor_id = aVendorId;
   END IF;

   RETURN lAuthorizedForVendor;

END isAuthorizedForVendor;
/