/********************************************
** INSERT SCRIPT FOR TABLE "REF_PO_AUTH_FLOW"
** 10-Level
** DATE: 6-JUN-2006
*********************************************/

insert into ref_po_auth_flow( PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, PO_TYPE_DB_ID, PO_TYPE_CD, DEFAULT_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'PURCHASE', 'Purchasing Authorization', 'Purchasing authorization workflow.', 0, 'PURCHASE', 1, 0, '6-JUN-06', '6-JUN-06', 0, 'MXI');

insert into ref_po_auth_flow( PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, PO_TYPE_DB_ID, PO_TYPE_CD, DEFAULT_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'REPAIR', 'Repair Authorization', 'Repair authorization workflow.', 0, 'REPAIR', 1, 0, '6-JUN-06', '6-JUN-06', 0, 'MXI');

insert into ref_po_auth_flow( PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, PO_TYPE_DB_ID, PO_TYPE_CD, DEFAULT_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'EXCHANGE', 'Exchange Authorization', 'Exchange authorization workflow.', 0, 'EXCHANGE', 1, 0, '19-MAR-06', '19-MAR-06', 0, 'MXI');

insert into ref_po_auth_flow( PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, PO_TYPE_DB_ID, PO_TYPE_CD, DEFAULT_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'BORROW', 'Borrow Authorization', 'Borrow authorization workflow.', 0, 'BORROW', 1, 0, '19-MAR-06', '19-MAR-06', 0, 'MXI');

insert into ref_po_auth_flow( PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, PO_TYPE_DB_ID, PO_TYPE_CD, DEFAULT_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'CONSIGN', 'Consignment Authorization', 'Consignment authorization workflow.', 0, 'CONSIGN', 1, 0, '24-SEP-07', '24-SEP-07', 0, 'MXI');

insert into ref_po_auth_flow( PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, PO_TYPE_DB_ID, PO_TYPE_CD, DEFAULT_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'CSGNXCHG', 'Consignment Exchange Authorization', 'Consignment Exchange authorization workflow.', 0, 'CSGNXCHG', 1, 0, '08-NOV-07', '08-NOV-07', 0, 'MXI');
