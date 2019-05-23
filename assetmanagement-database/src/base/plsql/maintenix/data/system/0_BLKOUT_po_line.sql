--liquibase formatted sql


--changeSet 0_BLKOUT_po_line:1 stripComments:false
/************************************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE PO_LINE FOR BLACKOUT DATA ONLY**
*************************************************************************/
insert into PO_LINE (PO_DB_ID, PO_ID, PO_LINE_ID, LINE_NO_ORD, LINE_LDESC, PART_NO_DB_ID, PART_NO_ID, SCHED_DB_ID, SCHED_ID, ORDER_QT, RECEIVED_QT, QTY_UNIT_DB_ID, QTY_UNIT_CD, UNIT_PRICE, LINE_PRICE, BASE_UNIT_PRICE, PROMISE_BY_DT, ORIG_PROMISE_BY_DT, ORIG_UNIT_PRICE, RETURN_BY_DT, ACCOUNT_DB_ID, ACCOUNT_ID, VENDOR_NOTE, RECEIVER_NOTE, PO_LINE_EXT_SDESC, CONFIRM_PROMISE_BY_BOOL, REPL_TASK_DB_ID, REPL_TASK_ID, XCHG_SHIPMENT_DB_ID, XCHG_SHIPMENT_ID, OWNER_DB_ID, OWNER_ID, MAINT_PICKUP_BOOL, PO_LINE_TYPE_DB_ID, PO_LINE_TYPE_CD, PRE_INSP_QT, PRICE_TYPE_DB_ID, PRICE_TYPE_CD, WARRANTY_BOOL, DELETED_BOOL, CHANGE_REASON_CD, RSTAT_CD, CREATION_DT, REVISION_USER, REVISION_DT, REVISION_DB_ID, ACCRUED_VALUE, INVOICE_QT)
values (0, 1002, 1, 1, 'N/A', null, null, null, null, 0, null, 0, 'EA', 0, 0, null, null, null, null, null, 0, 50, null, null, null, 0, null, null, null, null, null, null, 0, 0, 'BLKOUT', 0, null, null, 0, 0, 'MXRTINE', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 'MXI', to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0,0,0);