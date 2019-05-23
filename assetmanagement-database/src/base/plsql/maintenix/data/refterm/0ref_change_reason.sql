--liquibase formatted sql


--changeSet 0ref_change_reason:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_CHANGE_REASON"
** 0-Level
** DATE: 23-JUN-2011 TIME: 17:03:53
*********************************************/
insert into ref_change_reason ( CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('MXRTINE', 'Routine', 'Routine Action', 'Routine Action', 1, 0, 1, 0, '23-JUN-11', 0, '23-JUN-11', 0, 'MXI');

--changeSet 0ref_change_reason:2 stripComments:false
insert into ref_change_reason ( CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('MXACCEPT', 'Accept', 'Acceptance of Quotation', 'Acceptance of Quotation', 2, 0, 1, 0, '23-JUN-11', 0, '23-JUN-11', 0, 'MXI');

--changeSet 0ref_change_reason:3 stripComments:false
insert into ref_change_reason ( CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('MXALTER', 'Alter', 'Alteration', 'Alteration', 3, 0, 1, 0, '23-JUN-11', 0, '23-JUN-11', 0, 'MXI');

--changeSet 0ref_change_reason:4 stripComments:false
insert into ref_change_reason ( CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('MXCNCL', 'Cancel', 'Cancel', 'Cancel/Decrease Quantity', 6, 0, 1, 0, '23-JUN-11', 0, '23-JUN-11', 0, 'MXI');

--changeSet 0ref_change_reason:5 stripComments:false
insert into ref_change_reason ( CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('MXDCRSE', 'Decrease', 'Decrease Quantity', 'Cancel/Decrease Quantity', 6, 0, 1, 0, '23-JUN-11', 0, '23-JUN-11', 0, 'MXI');

--changeSet 0ref_change_reason:6 stripComments:false
insert into ref_change_reason ( CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('MXINCRSE', 'Increase', 'Increase Quantity', 'Increase Quantity', 8, 0, 1, 0, '23-JUN-11', 0, '23-JUN-11', 0, 'MXI');

--changeSet 0ref_change_reason:7 stripComments:false
insert into ref_change_reason ( CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('MXPRTL', 'Partial', 'Request partial ship quantity', 'Request partial Ship Quantity of an order', 9, 0, 1, 0, '23-JUN-11', 0, '23-JUN-11', 0, 'MXI');