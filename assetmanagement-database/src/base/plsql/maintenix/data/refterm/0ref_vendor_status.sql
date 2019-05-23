--liquibase formatted sql


--changeSet 0ref_vendor_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_VENDOR_STATUS"
** 0-Level
** DATE: 09/22/2004 TIME: 00:00:00
*********************************************/
insert into ref_vendor_status(VENDOR_STATUS_DB_ID,VENDOR_STATUS_CD,DESC_SDESC,DESC_LDESC,BITMAP_DB_ID,BITMAP_TAG,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0, 'APPROVED', 'Approved', 'This vendor is an acceptable provider of parts, both on the basis of regulatory approval, and on the basis of vendor performance for your organization', 0, 01, 0 ,'22-OCT-04', '22-OCT-04', 100, 'MXI');

--changeSet 0ref_vendor_status:2 stripComments:false
insert into ref_vendor_status(VENDOR_STATUS_DB_ID,VENDOR_STATUS_CD,DESC_SDESC,DESC_LDESC,BITMAP_DB_ID,BITMAP_TAG,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0, 'WARNING', 'Warning', 'Indicates a potential problem with the vendor that is being investigated.  Likely not a good idea to purchase parts from this vendor at this time', 0, 01, 0 ,'22-OCT-04', '22-OCT-04', 100, 'MXI');

--changeSet 0ref_vendor_status:3 stripComments:false
insert into ref_vendor_status(VENDOR_STATUS_DB_ID,VENDOR_STATUS_CD,DESC_SDESC,DESC_LDESC,BITMAP_DB_ID,BITMAP_TAG,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0, 'UNAPPRVD', 'Unapproved', 'The vendor is, for any number of reasons, considered to be a risk - it has failed to receive regulatory approval, or has a trend of poor performance for your organization. ', 0, 01, 0 ,'22-OCT-04', '22-OCT-04', 100, 'MXI');

--changeSet 0ref_vendor_status:4 stripComments:false
insert into REF_VENDOR_STATUS (VENDOR_STATUS_DB_ID, VENDOR_STATUS_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 'N/A', 'N/A', null, null, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');