--liquibase formatted sql


--changeSet 0ref_ship_segment_status:1 stripComments:false
insert into REF_SHIP_SEGMENT_STATUS (SEGMENT_STATUS_DB_ID, SEGMENT_STATUS_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'PEND', 'Pending Shipment Segment', 'Pending Shipment Segment', 'PENDING', 0, '25-JUL-08', '25-JUL-08', 100, 'MXI');

--changeSet 0ref_ship_segment_status:2 stripComments:false
insert into REF_SHIP_SEGMENT_STATUS (SEGMENT_STATUS_DB_ID, SEGMENT_STATUS_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CMPLT', 'Completed Shipment Segment', 'Completed Shipment Segment', 'COMPLETE', 0, '25-JUL-08', '25-JUL-08', 100, 'MXI');

--changeSet 0ref_ship_segment_status:3 stripComments:false
insert into REF_SHIP_SEGMENT_STATUS (SEGMENT_STATUS_DB_ID, SEGMENT_STATUS_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'INTR', 'In Transit Shipment Segment', 'In Transit Shipment Segment', 'INTRANS', 0, '25-JUL-08', '25-JUL-08', 100, 'MXI');

--changeSet 0ref_ship_segment_status:4 stripComments:false
insert into REF_SHIP_SEGMENT_STATUS (SEGMENT_STATUS_DB_ID, SEGMENT_STATUS_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CANCEL', 'Cancelled Shipment Segment', 'Cancelled Shipment Segment', 'CANCEL', 0, '25-JUL-08', '25-JUL-08', 100, 'MXI');

--changeSet 0ref_ship_segment_status:5 stripComments:false
insert into REF_SHIP_SEGMENT_STATUS (SEGMENT_STATUS_DB_ID, SEGMENT_STATUS_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'PLAN', 'Planned Shipment Segment', 'Planned Shipment Segment', 'PLAN', 0, '25-JUL-08', '25-JUL-08', 100, 'MXI');