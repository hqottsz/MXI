--liquibase formatted sql


--changeSet DEV-26:1 stripComments:false
/*
   This script is to facilitate the migration from the previous version to the new version for single-organization systems
   by providing default values (old vendor approvals) for the new vendor approval system.
   
   The new vendor approval system uses organization-specific vendor approvals. In order to facilitate migration for
   single-organization systems, we assign all vendors to the ADMIN organization in the system. This makes
   the vendor approval system work in the same way that it worked prior to the migration process.
   
   Note this script will assign all vendors to the ADMIN organization.

   This script must execute after the creation of
    - Table: ORG_ORG_VENDOR
*/  
INSERT INTO
   org_org_vendor
   (
      org_db_id,
      org_id,
      vendor_db_id,
      vendor_id,
      vendor_status_db_id,
      vendor_status_cd,
      approval_expiry_dt,
      warning_ldesc,
      vendor_note
   )
SELECT
   org_org.org_db_id,
   org_org.org_id,
   org_vendor.vendor_db_id,
   org_vendor.vendor_id,
   org_vendor.vendor_status_db_id,
   org_vendor.vendor_status_cd,
   org_vendor.approval_expiry_dt,
   org_vendor.warning_ldesc,
   org_vendor.vendor_note
FROM
   org_org,
   org_vendor
WHERE
   org_org.org_db_id=0
  AND
   (org_org.org_id=1 
   OR
    org_org.org_id=2)
   AND NOT EXISTS
    (
     SELECT 1
     FROM org_org_vendor
     WHERE
         org_org_vendor.org_db_id = org_org.org_db_id AND
         org_org_vendor.org_id    = org_org.org_id
         AND
         org_org_vendor.vendor_db_id = org_vendor.vendor_db_id AND
         org_org_vendor.vendor_id    = org_vendor.vendor_id 
    ) ;

--changeSet DEV-26:2 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_ASSIGN_VENDOR_TO_ORGANIZATION', 'SECURED_RESOURCE','Permission required to assign a vendor to an organization.','USER', 'TRUE/FALSE', 'FALSE', 1,'Org - Organizations', '7.1', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ASSIGN_VENDOR_TO_ORGANIZATION' );                

--changeSet DEV-26:3 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_ENABLE_VIEW_ALL_VENDORS', 'SECURED_RESOURCE','Permission to view all vendors.','USER', 'TRUE/FALSE', 'TRUE', 1,'Org - Organizations', '7.1',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ENABLE_VIEW_ALL_VENDORS' );      

--changeSet DEV-26:4 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_VIEW_ORGANIZATION_VENDOR_APPROVALS' and t.db_type_cd = 'MASTER'; 

--changeSet DEV-26:5 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_VIEW_ORGANIZATION_VENDOR_APPROVALS' and t.db_type_cd = 'OPER'; 

--changeSet DEV-26:6 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_VIEW_ORGANIZATION_VENDOR_APPROVALS'; 

--changeSet DEV-26:7 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_VIEW_ORGANIZATION_VENDOR_APPROVALS'; 

--changeSet DEV-26:8 stripComments:false
DELETE FROM utl_config_parm t where t.parm_name = 'ACTION_VIEW_ORGANIZATION_VENDOR_APPROVALS';

--changeSet DEV-26:9 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_DEFAULT_APPROVAL_VENDOR' and t.db_type_cd = 'OPER'; 

--changeSet DEV-26:10 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name = 'ACTION_DEFAULT_APPROVAL_VENDOR'; 

--changeSet DEV-26:11 stripComments:false
DELETE FROM utl_role_parm WHERE parm_name = 'ACTION_DEFAULT_APPROVAL_VENDOR'; 

--changeSet DEV-26:12 stripComments:false
DELETE FROM utl_config_parm t where t.parm_name = 'ACTION_DEFAULT_APPROVAL_VENDOR';

--changeSet DEV-26:13 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_UNASSIGN_VENDOR_FROM_ORGANIZATION', 'SECURED_RESOURCE','Permission required to unassign a vendor to an organization.','USER', 'TRUE/FALSE', 'FALSE', 1,'Org - Organizations', '7.1', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_UNASSIGN_VENDOR_FROM_ORGANIZATION' );            

--changeSet DEV-26:14 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'true', 'ACTION_VIEW_ORGANIZATION_VENDOR_LIST', 'SECURED_RESOURCE','Permission required to view the vendor list tab on the organization details page.','USER', 'TRUE/FALSE', 'TRUE', 1,'Org - Organizations', '7.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_VIEW_ORGANIZATION_VENDOR_LIST' );               

--changeSet DEV-26:15 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_VIEW_ORGANIZATION_VENDOR_LIST', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_VIEW_ORGANIZATION_VENDOR_LIST' and db_type_cd = 'OPER' );      

--changeSet DEV-26:16 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_ASSIGN_VENDOR_TO_ORGANIZATION', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ASSIGN_VENDOR_TO_ORGANIZATION' and db_type_cd = 'OPER' );      

--changeSet DEV-26:17 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_ENABLE_VIEW_ALL_VENDORS', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ENABLE_VIEW_ALL_VENDORS' and db_type_cd = 'OPER' );         

--changeSet DEV-26:18 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_UNASSIGN_VENDOR_FROM_ORGANIZATION', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_UNASSIGN_VENDOR_FROM_ORGANIZATION' and db_type_cd = 'OPER' );            

--changeSet DEV-26:19 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_VIEW_ORGANIZATION_VENDOR_LIST', 'MASTER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_VIEW_ORGANIZATION_VENDOR_LIST' and db_type_cd = 'MASTER' );                    

--changeSet DEV-26:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isAuthorizedForResource
* Arguments:     aUserId   - user id
*                aParmName - config parm name
* Description:   This function determines if the user has authority
*                over the config parm.
*
* Orig.Coder:    Wayne Leroux
* Recent Coder:  wleroux
* Recent Date:   January 6th, 2010
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isAuthorizedForResource
(
   aUserId      utl_user.user_id%TYPE,
   aParmName    utl_config_parm.parm_name%TYPE
)  RETURN NUMBER
IS

  lParmValue utl_config_parm.parm_value%TYPE;
  lAuthorizedForResource NUMBER;
  
  CURSOR cur_user_parm(aUserId utl_user.user_id%TYPE, aParmName utl_config_parm.parm_name%TYPE) IS
  SELECT
     utl_user_parm.parm_value
  FROM
     utl_user
     INNER JOIN utl_user_parm ON
        utl_user_parm.user_id = utl_user.user_id
  WHERE
     utl_user.user_id = aUserId
     AND
     utl_user_parm.parm_name = aParmName;

  CURSOR cur_role_parm(aUserId utl_user.user_id%TYPE, aParmName utl_config_parm.parm_name%TYPE) IS
  SELECT
      utl_role_parm.parm_value
   FROM
      utl_user_role
      INNER JOIN utl_role_parm ON
         utl_role_parm.role_id = utl_user_role.role_id
   WHERE
      utl_user_role.user_id = aUserId
      AND
      utl_role_parm.parm_name = aParmName;
  
  CURSOR cur_config_parm(aParmName utl_config_parm.parm_name%TYPE) IS
  SELECT
     utl_config_parm.parm_value
  FROM
     utl_config_parm
  WHERE
     utl_config_parm.parm_name = aParmName;
BEGIN
  lParmValue := NULL;

  -- Get User Parm Value
  FOR r IN cur_user_parm( aUserId, aParmName ) LOOP
     lParmValue := r.parm_value;
  END LOOP;

  -- If User Parm Value is not set, use Role Parm Value
  IF lParmValue IS NULL THEN
    FOR r IN cur_role_parm(aUserId, aParmName) LOOP
       lParmValue := r.parm_value;
    END LOOP;
  END IF;
  
  -- If Role Parm Value is not set, use Default Parm Value
  IF lParmValue IS NULL THEN
    FOR r IN cur_config_parm(aParmName) LOOP
       lParmValue := r.parm_value;
    END LOOP;
  END IF;

  IF upper(lParmValue) = 'TRUE' THEN
     lAuthorizedForResource := 1;
  ELSIF lParmValue = '1' THEN
     lAuthorizedForResource := 1;
  ELSE
     lAuthorizedForResource := 0;
  END IF;
  
  RETURN lAuthorizedForResource;
  
END isAuthorizedForResource;
/

--changeSet DEV-26:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isAuthorizedForVendor
* Arguments:     aUserId     - user id
*                aVendorDbId - vendor db id
*                aVendorId   - vendor id
* Description:   This function determines if the user has authority
*                over the specified vendor.
*
* Orig.Coder:    Wayne Leroux
* Recent Coder:  wleroux
* Recent Date:   January 6th, 2010
*
*********************************************************************************/
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
   lAuthorizedForVendor := isAuthorizedForResource( aUserId, 'ACTION_ENABLE_VIEW_ALL_VENDORS');

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

--changeSet DEV-26:22 stripComments:false
UPDATE utl_config_parm 
  SET parm_value = 'true',
      default_value = 'TRUE'
  WHERE parm_name = 'ACTION_VIEW_ORGANIZATION_VENDOR_LIST';      