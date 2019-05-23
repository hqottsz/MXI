--liquibase formatted sql


--changeSet 0ref_xfer_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_XFER_TYPE"
** 0-Level
** DATE: 07-JAN-2005 TIME: 11:15:54
*********************************************/
insert into ref_xfer_type ( XFER_TYPE_DB_ID, XFER_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, DESC_SDESC, DESC_LDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'STKTRN', 0, 1, 0, 'Stock Transfer', 'Stock Transfer', '07-JAN-05', '07-JAN-05', 100, 'MXI');

--changeSet 0ref_xfer_type:2 stripComments:false
insert into ref_xfer_type ( XFER_TYPE_DB_ID, XFER_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, DESC_SDESC, DESC_LDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'ISSUE', 0, 1, 0, 'Issue Transfer', 'Issue Transfer', '07-JAN-05', '07-JAN-05', 100, 'MXI');

--changeSet 0ref_xfer_type:3 stripComments:false
insert into ref_xfer_type ( XFER_TYPE_DB_ID, XFER_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, DESC_SDESC, DESC_LDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'PUTAWAY', 0, 1, 0, 'Put Away Transfer', 'Put Away Transfer', '07-JAN-05', '07-JAN-05', 100, 'MXI');

--changeSet 0ref_xfer_type:4 stripComments:false
insert into ref_xfer_type ( XFER_TYPE_DB_ID, XFER_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, DESC_SDESC, DESC_LDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'TURNIN', 0, 1, 0, 'Transfer', 'Creation of transfer tickets to unserviceable staging location', '23-FEB-05', '23-FEB-05', 100, 'MXI');

--changeSet 0ref_xfer_type:5 stripComments:false
insert into ref_xfer_type ( XFER_TYPE_DB_ID, XFER_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, DESC_SDESC, DESC_LDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'KITPICK', 0, 1, 1, 'Kit Pick Transfer', 'WIP - KITS - DO NOT USE - Kit Pick Transfer', '11-JAN-08', '11-JAN-08', 100, 'MXI');

--changeSet 0ref_xfer_type:6 stripComments:false
insert into REF_XFER_TYPE (XFER_TYPE_DB_ID, XFER_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, DESC_SDESC, DESC_LDESC, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 0, 1, 3, 'N/A', 'N/A', to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');