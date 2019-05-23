--liquibase formatted sql


--changeSet 0ref_po_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_PO_TYPE"
** 0-Level
** DATE: 16-MARCH-2005 TIME: 00:00:00
*********************************************/
insert into ref_po_type( PO_TYPE_DB_ID, PO_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'PURCHASE', 'PURCHASE', 'This is a standard purchase order used to acquire parts or services.', 0, '16-MAR-05', '16-MAR-05', 0, 'MXI');

--changeSet 0ref_po_type:2 stripComments:false
insert into ref_po_type( PO_TYPE_DB_ID, PO_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'REPAIR', 'REPAIR', 'This is a repair purchase order used to purchase repair services.', 0, '16-MAR-05', '16-MAR-05', 0, 'MXI');

--changeSet 0ref_po_type:3 stripComments:false
insert into ref_po_type( PO_TYPE_DB_ID, PO_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'EXCHANGE', 'EXCHANGE', 'This is an exchange purchase order used to exchange parts.', 0, '14-MAR-07', '04-MAR-07', 0, 'MXI');

--changeSet 0ref_po_type:4 stripComments:false
insert into ref_po_type( PO_TYPE_DB_ID, PO_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BORROW', 'BORROW', 'This is a borrow purchase order used to borrow parts.', 0, '14-MAR-07', '04-MAR-07', 0, 'MXI');

--changeSet 0ref_po_type:5 stripComments:false
insert into ref_po_type( PO_TYPE_DB_ID, PO_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CONSIGN', 'CONSIGN', 'This is a consignment order for consignment inventory.', 0, '24-SEP-07', '24-SEP-07', 0, 'MXI');

--changeSet 0ref_po_type:6 stripComments:false
insert into ref_po_type( PO_TYPE_DB_ID, PO_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CSGNXCHG', 'CSGNXCHG', 'This is a consignment exchange order.', 0, '24-SEP-07', '24-SEP-07', 0, 'MXI');

--changeSet 0ref_po_type:7 stripComments:false
insert into REF_PO_TYPE (PO_TYPE_DB_ID, PO_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 'BLKOUT', 'N/A', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');