--liquibase formatted sql


--changeSet MX-23107:1 stripComments:false
-- Update zero level ref term descriptions to replace special characters with standard characters
UPDATE REF_ACCOUNT_TYPE
SET DESC_LDESC = 'This category is used for a part''s fixed asset account (rotables).'
WHERE ACCOUNT_TYPE_DB_ID = 0
AND ACCOUNT_TYPE_CD = 'FIXASSET';

--changeSet MX-23107:2 stripComments:false
UPDATE REF_EVENT_STATUS
SET DESC_LDESC = 'The vendor is, for any number of reasons, considered to be a risk - it has failed to receive regulatory approval, or has a trend of poor performance for your organization. '
WHERE EVENT_STATUS_DB_ID = 0
AND EVENT_STATUS_CD = 'VNUNAPPRVD';

--changeSet MX-23107:3 stripComments:false
UPDATE REF_SPEC2K_CMND
SET DESC_LDESC = 'Purchasing agent has the capability to enter purchase orders directly into a suppliers'' system.'
WHERE SPEC2K_CMND_DB_ID = 0
AND SPEC2K_CMND_CD = 'S1BOOKED';

--changeSet MX-23107:4 stripComments:false
UPDATE REF_VENDOR_STATUS
SET DESC_LDESC = 'The vendor is, for any number of reasons, considered to be a risk - it has failed to receive regulatory approval, or has a trend of poor performance for your organization. '
WHERE VENDOR_STATUS_DB_ID = 0
AND VENDOR_STATUS_CD = 'UNAPPRVD';