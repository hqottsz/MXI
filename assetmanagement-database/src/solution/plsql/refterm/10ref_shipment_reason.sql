/********************************************
** INSERT SCRIPT FOR TABLE "REF_SHIPMENT_TYPE"
** 10-Level
** DATE:15-MAR-2005
*********************************************/
insert into ref_shipment_reason (SHIPMENT_REASON_DB_ID, SHIPMENT_REASON_CD, SHIPMENT_TYPE_DB_ID, SHIPMENT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'OVERMAX', 0, 'STKTRN', 'OVERMAX', 'The item is being shipped because you have surpassed the maximum stock level', 0, '17-FEB-05', '17-FEB-05', 100, 'MXI');
insert into ref_shipment_reason (SHIPMENT_REASON_DB_ID, SHIPMENT_REASON_CD, SHIPMENT_TYPE_DB_ID, SHIPMENT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'REQUEST', 0, 'STKTRN', 'REQUEST', 'The item is being shipped in response to a part request at a remote airport', 0, '17-FEB-05', '17-FEB-05', 100, 'MXI');
insert into ref_shipment_reason (SHIPMENT_REASON_DB_ID, SHIPMENT_REASON_CD, SHIPMENT_TYPE_DB_ID, SHIPMENT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'RELEVEL', 0, 'STKTRN', 'RELEVEL', 'The item is being shipped in response to an adjustment of global stock levels', 0, '17-FEB-05', '17-FEB-05', 100, 'MXI');
insert into ref_shipment_reason (SHIPMENT_REASON_DB_ID, SHIPMENT_REASON_CD, SHIPMENT_TYPE_DB_ID, SHIPMENT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'BROKEN', 0, 'RTNVEN', 'BROKEN', 'The item is being returned because it is broken', 0, '17-FEB-05', '17-FEB-05', 100, 'MXI');
insert into ref_shipment_reason (SHIPMENT_REASON_DB_ID, SHIPMENT_REASON_CD, SHIPMENT_TYPE_DB_ID, SHIPMENT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'NODOCS', 0, 'RTNVEN', 'NODOCS', 'The item is being returned because it was missing mandatory documentation', 0, '17-FEB-05', '17-FEB-05', 100, 'MXI');
