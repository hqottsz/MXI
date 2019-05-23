--liquibase formatted sql


--changeSet isVendorApprovedExpired:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
* Function:     isVendorApprovedExpired
* Arguments:    aOrgDbId, aOrgId    - pk for the organization
*               aVendorDbId, aVendorId  - pk for the vendor
* Description:  This function determines if any vendor order type or service type
*               is approved expired for the organization
*********************************************************************************/
create or replace FUNCTION isVendorApprovedExpired
(
  aOrgDbId org_vendor_po_type.org_db_id%TYPE,
  aOrgId org_vendor_po_type.org_id%TYPE,
  aVendorDbId org_vendor_po_type.vendor_db_id%TYPE,
  aVendorId org_vendor_po_type.vendor_id%TYPE
) RETURN NUMBER
IS
   ln_IsApproveExpired NUMBER;
BEGIN

   -- Return 1 if atleast one ref_po_type is Approved Expired
   SELECT
      count(*) INTO ln_IsApproveExpired
   FROM
      org_vendor_po_type
   WHERE
       org_vendor_po_type.org_db_id = aOrgDbId
       AND
       org_vendor_po_type.org_id = aOrgId
       AND
       org_vendor_po_type.vendor_db_id = aVendorDbId
       AND
       org_vendor_po_type.vendor_id = aVendorId
       AND
       org_vendor_po_type.vendor_status_cd = 'APPROVED'
       AND
       org_vendor_po_type.approval_expiry_dt < SYSDATE;

   IF ln_IsApproveExpired <> 0 THEN
      RETURN 1;
   END IF;
   -- Return 1 if atleast one ref_service_type is Approved Expired
   SELECT
      count(*) INTO ln_IsApproveExpired
   FROM
      org_vendor_service_type
   WHERE
      org_vendor_service_type.org_db_id = aOrgDbId
      AND
      org_vendor_service_type.org_id = aOrgId
      AND
      org_vendor_service_type.vendor_db_id = aVendorDbId
      AND
      org_vendor_service_type.vendor_id = aVendorId
      AND
      org_vendor_service_type.vendor_status_cd = 'APPROVED'
      AND
      org_vendor_service_type.approval_expiry_dt < SYSDATE;

   IF ln_IsApproveExpired <> 0 THEN
      RETURN 1;
   ELSE
      RETURN 0;
   END IF;
END isVendorApprovedExpired;
/