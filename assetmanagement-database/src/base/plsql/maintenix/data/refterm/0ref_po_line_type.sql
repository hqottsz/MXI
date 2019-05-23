--liquibase formatted sql


--changeSet 0ref_po_line_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_PO_LINE_TYPE"
** 0-Level
** DATE: 14-MAY-2008
*********************************************/
insert into ref_po_line_type ( PO_LINE_TYPE_DB_ID, PO_LINE_TYPE_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) values (0, 'REPAIR', 'Repair Line', 'Repair Line', 'R', 0, '14-MAY-08', '14-MAY-08', 100, 'MXI');

--changeSet 0ref_po_line_type:2 stripComments:false
insert into ref_po_line_type ( PO_LINE_TYPE_DB_ID, PO_LINE_TYPE_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) values (0, 'PURCHASE', 'Purchase Line', 'Purchase Line', 'P', 0, '14-MAY-08', '14-MAY-08', 100, 'MXI');

--changeSet 0ref_po_line_type:3 stripComments:false
insert into ref_po_line_type ( PO_LINE_TYPE_DB_ID, PO_LINE_TYPE_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) values (0, 'EXCHANGE', 'Exchange','Exchange', 'X', 0, '14-MAY-08', '14-MAY-08', 100, 'MXI');

--changeSet 0ref_po_line_type:4 stripComments:false
insert into ref_po_line_type ( PO_LINE_TYPE_DB_ID, PO_LINE_TYPE_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) values (0, 'BORROW', 'Borrow Line', 'Borrow Line', 'B', 0, '14-MAY-08', '14-MAY-08', 100, 'MXI');

--changeSet 0ref_po_line_type:5 stripComments:false
insert into ref_po_line_type ( PO_LINE_TYPE_DB_ID, PO_LINE_TYPE_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) values (0, 'CONSIGN', 'Consignment Line','Consignment Line','C', 0, '14-MAY-08', '14-MAY-08', 100, 'MXI');

--changeSet 0ref_po_line_type:6 stripComments:false
insert into ref_po_line_type ( PO_LINE_TYPE_DB_ID, PO_LINE_TYPE_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) values (0, 'CSGNXCHG', 'Consignment Exchange Line','Consignment Exchange Line', 'CX', 0, '14-MAY-08', '14-MAY-08', 100, 'MXI');

--changeSet 0ref_po_line_type:7 stripComments:false
insert into ref_po_line_type ( PO_LINE_TYPE_DB_ID, PO_LINE_TYPE_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) values (0, 'MISC', 'Miscellaneous Line','Miscellaneous Line', 'M', 0, '14-MAY-08', '14-MAY-08', 100, 'MXI');

--changeSet 0ref_po_line_type:8 stripComments:false
insert into REF_PO_LINE_TYPE (PO_LINE_TYPE_DB_ID, PO_LINE_TYPE_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 'N/A', 'N/A', 'BLK', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');