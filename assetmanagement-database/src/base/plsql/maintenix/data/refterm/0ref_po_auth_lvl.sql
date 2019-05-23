--liquibase formatted sql


--changeSet 0ref_po_auth_lvl:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_PO_AUTH_LVL"
** 0-Level
** DATE: 18-JAN-2012 TIME: 11:15:54
*********************************************/
INSERT INTO REF_PO_AUTH_LVL (PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'BUDGET', 0, 'BLKOUT', 'Budget', 'Requires an external budgeting system to authorize this order.', 'BUDGET', 0, 0, '05-JAN-12', '05-JAN-12', 0, 'MXI');