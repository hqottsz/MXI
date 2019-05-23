--liquibase formatted sql


--changeSet 0_BLKOUT_org_vendor:1 stripComments:false
/**********************************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE ORG_VENDOR FOR BLACKOUT DATA ONLY**
***********************************************************************/
insert into ORG_VENDOR (VENDOR_DB_ID, VENDOR_ID, VENDOR_CD, OWNER_DB_ID, OWNER_ID, CERT_CD, CERT_EXPIRY_DT, VENDOR_TYPE_DB_ID, VENDOR_TYPE_CD, VENDOR_LOC_DB_ID, VENDOR_LOC_ID, CURRENCY_DB_ID, CURRENCY_CD, VENDOR_APPROVAL_DB_ID, VENDOR_APPROVAL_CD, TERMS_CONDITIONS_DB_ID, TERMS_CONDITIONS_CD, BORROW_RATE_DB_ID, BORROW_RATE_CD, VENDOR_NAME, VENDOR_NOTE, NO_PRINT_REQ_BOOL, MIN_PURCHASE_AMOUNT, EXT_KEY_SDESC, RECEIVE_NOTE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
               values  (0, 1000, 'N/A', 0, 1000, null, null, 0, 'BLKOUT', 0, 1004, 0, 'BLK', null, null, null, null, null, null, 'N/A', null, 1, null, null, null, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');