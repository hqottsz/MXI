--liquibase formatted sql


--changeSet 0ref_xaction_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_XACTION_TYPE"
** 0-Level
** DATE: 03-MARCH-2005 TIME: 00:00:00
*********************************************/
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'ISSUE', 'ISSUE', 'Transaction occurred when a consumable part was issued.', 0, '23-MAR-01', '23-MAR-01', 0, 'MXI');

--changeSet 0ref_xaction_type:2 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'TURN IN', 'TURN IN', 'Transaction occurred when a consumable part was turned in.', 0, '23-MAR-01', '23-MAR-01', 0, 'MXI');

--changeSet 0ref_xaction_type:3 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'QTYADJ', 'QTYADJ', 'Transaction occurred when the on-hand quantity for a consumable part was adjusted.', 0, '23-MAR-01', '23-MAR-01', 0, 'MXI');

--changeSet 0ref_xaction_type:4 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'ADJPRICE', 'ADJPRICE', 'Transaction occurred when the average unit price was adjusted.', 0, '21-JUN-06', '21-JUN-06', 0, 'MXI');

--changeSet 0ref_xaction_type:5 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'INSP', 'INSP', 'Transaction occurred when inventory was inspected as serviceable.', 0, '22-JUN-06', '22-JUN-06', 0, 'MXI');

--changeSet 0ref_xaction_type:6 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'PAYINVC', 'PAYINVC', 'Transaction occurred when invoice was sent for payment.', 0, '23-MAR-01', '23-MAR-01', 0, 'MXI');

--changeSet 0ref_xaction_type:8 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'ARCHIVE', 'ARCHIVE', 'Transaction occurred when Inventory was Archived.', 0, '25-MAY-06', '25-MAY-06', 0, 'MXI');

--changeSet 0ref_xaction_type:9 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CRTINV', 'CRTINV', 'Transaction occurred when Inventory was Created.', 0, '25-MAY-06', '25-MAY-06', 0, 'MXI');

--changeSet 0ref_xaction_type:10 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'SCRAP', 'SCRAP', 'Transaction occurred when Inventory was Scrapped.', 0, '25-MAY-06', '25-MAY-06', 0, 'MXI');

--changeSet 0ref_xaction_type:11 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'RTNVEN', 'RTNVEN', 'Transaction occurred when Inventory was returned to the vendor.', 0, '27-JUN-06', '27-JUN-06', 0, 'MXI');

--changeSet 0ref_xaction_type:12 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'UNARCH', 'UNARCH', 'Transaction occurred when Inventory was Unarchived.', 0, '25-OCT-06', '25-OCT-06', 0, 'MXI');

--changeSet 0ref_xaction_type:13 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CHGOWN', 'CHGOWN', 'Change owner.', 0, '19-MAR-07', '25-OCT-07', 0, 'MXI');

--changeSet 0ref_xaction_type:14 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'UNDOISSUE', 'UNDOISSUE', 'Undo issue financial transaction.', 0, '02-JUN-08', '02-JUN-08', 0, 'MXI');

--changeSet 0ref_xaction_type:15 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'UNDOINSP', 'UNDOINSP', 'Undo inspect as serviceable financial transaction.', 0, '02-JUN-08', '02-JUN-08', 0, 'MXI');

--changeSet 0ref_xaction_type:16 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'SPARESQTYADJ', 'SPARESQTYADJ', 'A transaction occurred when the on-hand quantity for a rotable part was adjusted.', 0, '28-NOV-14', '28-NOV-14', 0, 'MXI');

--changeSet 0ref_xaction_type:17 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CHGFINTP', 'CHGFINTP', 'Transaction occurred when finance type was changed. ', 0, '18-JULY-16', '18-JULY-16', 0, 'MXI');

--changeSet 0ref_xaction_type:18 stripComments:false
insert into ref_xaction_type( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'UNSCRAP', 'UNSCRAP', 'Transaction occurred when Inventory was Unscrapped.', 0, '11-DEC-17', '11-DEC-17', 0, 'MXI');
