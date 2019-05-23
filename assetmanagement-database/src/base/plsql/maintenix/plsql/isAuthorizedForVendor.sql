--liquibase formatted sql


--changeSet isAuthorizedForVendor:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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