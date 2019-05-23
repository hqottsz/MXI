--liquibase formatted sql


--changeSet 0ref_spec2k_cmnd:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_SPEC2K_CMND"
** 0-Level
*********************************************/
INSERT INTO REF_SPEC2K_CMND(SPEC2K_CMND_DB_ID, SPEC2K_CMND_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'S1QUOTES', 'Quotation Request', 'Allows IFS Maintenix to send RFQs to and receive RFQ responses from SPEC2000 vendors automatically.', 0, '18-SEP-07', '18-SEP-07', 0, 'MXI');

--changeSet 0ref_spec2k_cmnd:2 stripComments:false
INSERT INTO REF_SPEC2K_CMND(SPEC2K_CMND_DB_ID, SPEC2K_CMND_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'S1BOOKED', 'Purchase Request', 'Purchasing agent has the capability to enter purchase orders directly into a suppliers'' system.', 0, '18-SEP-07', '18-SEP-07', 0, 'MXI');

--changeSet 0ref_spec2k_cmnd:3 stripComments:false
INSERT INTO REF_SPEC2K_CMND(SPEC2K_CMND_DB_ID, SPEC2K_CMND_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'S1ORDEXC', 'Purchase Order Change', 'Supplier has the capability to inform the Purchasing Agent about changes to the previously entered order.', 0, '18-SEP-07', '18-SEP-07', 0, 'MXI');

--changeSet 0ref_spec2k_cmnd:4 stripComments:false
INSERT INTO REF_SPEC2K_CMND(SPEC2K_CMND_DB_ID, SPEC2K_CMND_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'S1SHIPPD', 'Shipping Information', 'Supplier has the capability to give related shipping information for orders previously placed with the supplier.', 0, '18-SEP-07', '18-SEP-07', 0, 'MXI');

--changeSet 0ref_spec2k_cmnd:5 stripComments:false
-- QANTAS CR PROJECT 1109
INSERT INTO REF_SPEC2K_CMND(SPEC2K_CMND_DB_ID, SPEC2K_CMND_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'S1INVCE', 'Invoice Information', 'Supplier has the capability to send Invoices to the Purchasing Agent for previously entered orders', 0, '29-JUN-11', '29-JUN-11', 0, 'MXI');