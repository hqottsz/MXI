--liquibase formatted sql


--changeSet 0_BLKOUT_ship_shipment:1 stripComments:false
/**********************************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE SHIP_SHIPMENT FOR BLACKOUT DATA ONLY**
***********************************************************************/
insert into SHIP_SHIPMENT (SHIPMENT_DB_ID, SHIPMENT_ID, SHIPMENT_TYPE_DB_ID, SHIPMENT_TYPE_CD, SHIPMENT_REASON_DB_ID, SHIPMENT_REASON_CD, TRANSPORT_TYPE_DB_ID, TRANSPORT_TYPE_CD, REQ_PRIORITY_DB_ID, REQ_PRIORITY_CD, SIZE_CLASS_DB_ID, SIZE_CLASS_CD, CHECK_DB_ID, CHECK_ID, PO_DB_ID, PO_ID, WAYBILL_SDESC, CUSTOMS_DECLARATION, SHIP_BY_DT, SHIP_AFTER_DT, WEIGHT_ENG_UNIT_DB_ID, WEIGHT_ENG_UNIT_CD, WEIGHT_QT, DIMENSION_ENG_UNIT_DB_ID, DIMENSION_ENG_UNIT_CD, WIDTH_QT, HEIGHT_QT, LENGTH_QT, USE_FLIGHT_DESC, CARRIER_NAME, RETURN_PRICE, RETURN_AUTH_NO, RETURN_ACCOUNT_DB_ID, RETURN_ACCOUNT_ID, RMA_SDESC, CONFIRM_RECEIPT_BOOL, CUSTOMS_SDESC, FLIGHT_STATUS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 1000, 0, 'BLKOUT', null, null, null, null, 0, 'BLKOUT', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, null, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');