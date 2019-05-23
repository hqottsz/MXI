--liquibase formatted sql


--changeSet 0ref_vendor_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_VENDOR_TYPE"
** 0-Level
** DATE: 09/22/2004 TIME: 00:00:00
*********************************************/
insert into ref_vendor_type(VENDOR_TYPE_DB_ID,VENDOR_TYPE_CD,BITMAP_TAG, BITMAP_DB_ID,DESC_SDESC,DESC_LDESC,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values(0, 'REPAIR', 01, 0, 'REPAIR', 'The Vendor is primarily a provider of maintenance', 0 ,'22-OCT-04', '22-OCT-04', 100, 'MXI');

--changeSet 0ref_vendor_type:2 stripComments:false
insert into ref_vendor_type(VENDOR_TYPE_DB_ID,VENDOR_TYPE_CD,BITMAP_TAG, BITMAP_DB_ID,DESC_SDESC,DESC_LDESC,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values(0, 'PURCHASE', 01, 0, 'PURCHASE', 'The Vendor is primarily a provider of parts', 0 ,'22-OCT-04', '22-OCT-04', 100, 'MXI');

--changeSet 0ref_vendor_type:3 stripComments:false
insert into ref_vendor_type(VENDOR_TYPE_DB_ID,VENDOR_TYPE_CD,BITMAP_TAG, BITMAP_DB_ID,DESC_SDESC,DESC_LDESC,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values(0, 'POOL', 01, 0, 'POOL', 'The Vendor is a parts pool partner of your organization', 0 ,'22-OCT-04', '22-OCT-04', 100, 'MXI');

--changeSet 0ref_vendor_type:4 stripComments:false
insert into ref_vendor_type(VENDOR_TYPE_DB_ID,VENDOR_TYPE_CD,BITMAP_TAG, BITMAP_DB_ID,DESC_SDESC,DESC_LDESC,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values(0, 'BORROW', 01, 0, 'BORROW', 'The Vendor is primarily a source for the borrowing of parts', 0 ,'22-OCT-04', '22-OCT-04', 100, 'MXI');

--changeSet 0ref_vendor_type:5 stripComments:false
insert into REF_VENDOR_TYPE (VENDOR_TYPE_DB_ID, VENDOR_TYPE_CD, BITMAP_TAG, BITMAP_DB_ID, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 1, 0, 'BLKOUT', 'N/A', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_vendor_type:6 stripComments:false
insert into REF_VENDOR_TYPE (VENDOR_TYPE_DB_ID, VENDOR_TYPE_CD, BITMAP_TAG, BITMAP_DB_ID, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BROKER', 1, 0, 'BROKER', 'The Vendor decides which other vendor performs repairs', 0, sysdate, sysdate, 100, 'MXI');