--liquibase formatted sql


--changeSet 0ref_part_vendor_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_PART_VENDOR_TYPE"
** 0-Level
** DATE: 23-JUL-2009 TIME: 13:21:00
*********************************************/
insert into ref_part_vendor_type ( PART_VENDOR_TYPE_DB_ID, PART_VENDOR_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'PURCHASE', 'Purchase', 'Purchase Vendor-Part', 0, '23-JUL-09', '23-JUL-09', 0, 'MXI');

--changeSet 0ref_part_vendor_type:2 stripComments:false
insert into ref_part_vendor_type ( PART_VENDOR_TYPE_DB_ID, PART_VENDOR_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'REPAIR',   'Repair',   'Repair Vendor-Part',   0, '23-JUL-09', '23-JUL-09', 0, 'MXI');

--changeSet 0ref_part_vendor_type:3 stripComments:false
insert into ref_part_vendor_type ( PART_VENDOR_TYPE_DB_ID, PART_VENDOR_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'EXCHANGE', 'Exchange', 'Exchange Vendor-Part', 0, '23-JUL-09', '23-JUL-09', 0, 'MXI');