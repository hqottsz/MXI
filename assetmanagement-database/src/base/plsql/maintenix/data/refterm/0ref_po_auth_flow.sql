--liquibase formatted sql


--changeSet 0ref_po_auth_flow:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_PO_AUTH_FLOW"
** DATE: 03-MARCH-2005 TIME: 00:00:00
*********************************************/
insert into REF_PO_AUTH_FLOW (PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, PO_TYPE_DB_ID, PO_TYPE_CD, DESC_SDESC, DESC_LDESC, DEFAULT_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 0, 'BLKOUT', 'N/A', 'N/A', 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');