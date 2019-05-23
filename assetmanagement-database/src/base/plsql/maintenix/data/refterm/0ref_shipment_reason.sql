--liquibase formatted sql


--changeSet 0ref_shipment_reason:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_SHIPMENT_TYPE"
** 0-Level
** DATE:15-MAR-2005
*********************************************/
insert into ref_shipment_reason (SHIPMENT_REASON_DB_ID, SHIPMENT_REASON_CD, SHIPMENT_TYPE_DB_ID, SHIPMENT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BADALT', 0, 'RTNVEN', 'Bad Alternate', 'The item is being returned because a bad alternate part is has been received', 0, '17-FEB-05', '17-FEB-05', 100, 'MXI');