--liquibase formatted sql


--changeSet MX-18128:1 stripComments:false
UPDATE ref_vendor_status
  SET DESC_SDESC = 'Approved' 
WHERE VENDOR_STATUS_DB_ID = 0
  AND VENDOR_STATUS_CD = 'APPROVED';  

--changeSet MX-18128:2 stripComments:false
UPDATE ref_vendor_status
  SET DESC_SDESC = 'Warning' 
WHERE VENDOR_STATUS_DB_ID = 0
  AND VENDOR_STATUS_CD = 'WARNING';  

--changeSet MX-18128:3 stripComments:false
UPDATE ref_vendor_status
  SET DESC_SDESC = 'Unapproved' 
WHERE VENDOR_STATUS_DB_ID = 0
  AND VENDOR_STATUS_CD = 'UNAPPRVD';        