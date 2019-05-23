/********************************************
** INSERT SCRIPT FOR TABLE "REF_ATTACH_TYPE"
** 10-Level
** DATE: 16-MARCH-2005 TIME: 00:00:00
*********************************************/
insert into ref_attach_type ( ATTACH_TYPE_DB_ID, ATTACH_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'CONTRACT', 0, 1, 'Contract', 'This attachment is the PO contract.',  0, '16-MAR-05', '16-MAR-05', 0, 'MXI');
insert into ref_attach_type ( ATTACH_TYPE_DB_ID, ATTACH_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'WARRANTY', 0, 1, 'Warranty', 'This attachment is the warranty terms ' || chr(38) || ' conditions for the purchased part..',  0, '16-MAR-05', '16-MAR-05', 0, 'MXI');