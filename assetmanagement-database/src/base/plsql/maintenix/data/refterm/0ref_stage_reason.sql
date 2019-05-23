--liquibase formatted sql


--changeSet 0ref_stage_reason:1 stripComments:false
-- Task
/********************************************
** INSERT SCRIPT FOR TABLE "REF_STAGE_REASON"
** 0-Level
** DATE: 05-05-05 TIME: 00:00:00
*********************************************/
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'OBSOLETE', 0, 'CANCEL', 0, 82, 'Obsolete', 'System generated when task def is selected to obsolete', 'OBSOLETE',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_stage_reason:2 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'BOMTRANS', 0, 'CANCEL', 0, 78, 'A BOM transformation has occured resulting in this task being cancelled.', 'This item has been moved from one maintenance program to another (assembly). The new maintenance program does not require this task.', 'BOMTRANS',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_stage_reason:3 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'DEADEXT', null, null, 0, 78, 'The deadline on this task has been extended.', 'The deadline on this task has been extended.', 'DEADEXT',  0, '05-MAY-06', '05-MAY-06', 100, 'MXI');

--changeSet 0ref_stage_reason:4 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'SETPLNDT', 0, 'SETPLNDT', 0, 78, 'Set Plan By Date', 'Plan By Date has been set.', 'SETPLNDT', 0, '19-MAY-09', '19-MAY-09', 100, 'MXI');

--changeSet 0ref_stage_reason:5 stripComments:false
-- Component Fault
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CORRECT', 0, 'CFCERT', 0, 1, 'Completion of corrective action.', 'Completion of corrective task has caused close off of fault.', 'DEPEND',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_stage_reason:6 stripComments:false
-- Change Inventory Condition
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACRTV', 0, 'ACARCHIVE', 0, 80, 'Return to Vendor', 'Return to Vendor', 'RTV',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_stage_reason:7 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACSHIP', 0, 'ACARCHIVE', 0, 80, 'Shipped', 'Shipped out of the organization', 'SHIP',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_stage_reason:8 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACQBALT', 0, 'ACQUAR', 0, 80, 'Bad Alternate', 'Bad Alternate', 'BADALT',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_stage_reason:9 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACQUVEND', 0, 'ACQUAR', 0, 80, 'Unapproved Vendor', 'Unapproved Vendor', 'UVEND',  0, '03-MAR-06', '03-MAR-06', 100, 'MXI');

--changeSet 0ref_stage_reason:10 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACQUP', 0, 'ACQUAR', 0, 80, 'Unapproved Part', 'Unapproved Part', 'UP',  0, '03-MAR-06', '03-MAR-06', 100, 'MXI');

--changeSet 0ref_stage_reason:11 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACQORCVP', 0, 'ACQUAR', 0, 80, 'Over Received Part', 'Over Received Part', 'ORCVP',  0, '03-MAR-06', '03-MAR-06', 100, 'MXI');

--changeSet 0ref_stage_reason:12 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACQISHLF', 0, 'ACQUAR', 0, 80, 'Insufficient Shelf Life', 'Insufficient Shelf Life', 'ISHLF',  0, '07-MAR-06', '07-MAR-06', 100, 'MXI');

--changeSet 0ref_stage_reason:13 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACRCVSCR', 0, 'ACQUAR', 0, 80, 'Previously Scrapped', 'Previously Scrapped', 'RCVSCRAP',  0, '07-MAR-06', '07-MAR-06', 100, 'MXI');

--changeSet 0ref_stage_reason:14 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACUNAUTH', 0, 'ACQUAR', 0, 80, 'PO is not authorized', 'PO is not authorized', 'UNAUTH',  0, '20-AUG-07', '20-AUG-07', 100, 'MXI');

--changeSet 0ref_stage_reason:15 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values ( 0, 'ACTLFLT', 0, 'ACREPREQ', 0, 1, 'Fault Raised on Tool', 'Fault Raised on Tool', 'TOOLFAULT', 0, '21-MAY-08', '21-MAY-08', 100, 'MXI' );

--changeSet 0ref_stage_reason:16 stripComments:false
-- Transfer
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LXNLN', 0, 'LXCANCEL', 0, 01, 'No longer Needed', 'No longer Needed', 'NLN',  0, '07-JUN-05', '07-JUN-05', 100, 'MXI');

--changeSet 0ref_stage_reason:17 stripComments:false
-- Licensing
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ADALTPRQ', 0, 'LDMOD', 0, 01, 'Added alternate prerequisite license definition.', 'Added alternate prerequisite license definition.', 'ADALTPRQ',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_stage_reason:18 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ADDPRQ', 0, 'LDMOD', 0, 01, 'Added prerequisite license definition.', 'Added prerequisite license definition.', 'ADDPRQ',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_stage_reason:19 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'RMVPRQ', 0, 'LDMOD', 0, 01, 'Removed prerequisite license definition.', 'Removed prerequisite license definition.', 'RMVPRQ',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_stage_reason:20 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LDACTV', 0, 'LDACTV', 0, 01, 'License definition is active.', 'License definition is active.', 'LDACTV',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_stage_reason:21 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LDOBSL', 0, 'LDOBSL', 0, 01, 'License definition is obsolete.', 'License definition is obsolete.', 'LDOBSL',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_stage_reason:22 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'NEW', 0, 'HRACTV', 0, 01, 'New', 'New', 'NEW',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_stage_reason:23 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'READY', 0, 'HRACTV', 0, 01, 'Ready', 'Ready', 'READY',  0, '3-MAR-08', '3-MAR-08', 100, 'MXI');

--changeSet 0ref_stage_reason:24 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'EXP', 0, 'HRINVAL', 0, 01, 'Expired', 'Expired', 'EXPIRED',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_stage_reason:25 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'UNEFF', 0, 'HRINVAL', 0, 01, 'Not in effect.', 'Not in effect.', 'NOTEFFCT',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_stage_reason:26 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRQEXP', 0, 'HRINVAL', 0, 01, 'Prerequisite expired.', 'Prerequisite expired.', 'PRQEXP',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_stage_reason:27 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRQUNEFF', 0, 'HRINVAL', 0, 01, 'Prerequisite not in effect.', 'Prerequisite not in effect.', 'PRQUNEFF',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_stage_reason:28 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRQMISS', 0, 'HRINVAL', 0, 01, 'Prerequisite missing or suspended.', 'Prerequisite missing or suspended.', 'PRQMISS',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_stage_reason:29 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'NOTCMPLT', 0, 'HRINVAL', 0, 01, 'Prerequisite is not complete.', 'Prerequisite is not complete.', 'NOTCMPLT',  0, '10-MAR-08', '10-MAR-08', 100, 'MXI');

--changeSet 0ref_stage_reason:30 stripComments:false
-- Purchase Order Invoice
INSERT INTO ref_stage_reason  (  STAGE_REASON_DB_ID, STAGE_REASON_CD,  EVENT_STATUS_DB_ID, EVENT_STATUS_CD, BITMAP_DB_ID, BITMAP_TAG,   DESC_SDESC, DESC_LDESC ,USER_REASON_CD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES( 0, 'POALTER',NULL, NULL,0, 80,'Order modified', 'The order has been modified from its original state' , 'ALTER', 0,'29-JUN-11', '29-JUN-11', 100, 'MXI');

--changeSet 0ref_stage_reason:31 stripComments:false
-- Electronic Logbook Adapter
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, user_reason_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'DEFER-NA', 0, 'CFDEFER', 0, 80, 'NA', 'Not Applicable', 'Not Applicable', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_stage_reason:32 stripComments:false
-- Operational Restriction note
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'UPDOPSRESNOTE', 0, 'UPDOPSRES', 0, 01, 'Update Operational Restriction Note', 'Update Operational Restriction Note', 'NA',  0, to_date('20-08-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),to_date('20-08-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 100, 'MXI');

--changeSet 0ref_stage_reason:33 stripComments:false
-- Direct matching history note
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACDMCH', NULL, NULL, 0, 80, 'Reinducted', 'Inventory was reinducted and record unarchived because part number and serial number match an archived record.', 'DMCH',  0, TO_DATE('2018-02-07', 'YYYY-MM-DD'), TO_DATE('2018-02-07', 'YYYY-MM-DD'), 100, 'MXI');

--changeSet 0ref_stage_reason:34 stripComments:false
-- Indirect matching history note
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACINDMCH', NULL, NULL, 0, 80, 'Reinducted with new Part Number', 'Inventory was reinducted and record unarchived because serial number, part group, and manufacturer match an archived record.', 'INDMCH',  0, TO_DATE('2018-02-07', 'YYYY-MM-DD'), TO_DATE('2018-02-07', 'YYYY-MM-DD'), 100, 'MXI');

--changeSet 0ref_stage_reason:35 stripComments:false
-- Multiple matches found, no match history note
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACFLDMCH', NULL, NULL, 0, 80, 'Reinduction Attempted', 'New inventory record was created because more than one archived record matched the serial number, part group, and manufacturer.', 'FLDMCH',  0, TO_DATE('2018-02-07', 'YYYY-MM-DD'), TO_DATE('2018-02-07', 'YYYY-MM-DD'), 100, 'MXI');

--changeSet 0ref_stage_reason:36 stripComments:false
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TPBYPROXY', 0, 'PAUSE', 0, 80, 'Paused By Proxy', 'Job Stop was performed by proxy.', 'BYPROXY',  0, TO_DATE('2018-12-18', 'YYYY-MM-DD'), TO_DATE('2018-12-18', 'YYYY-MM-DD'), 100, 'MXI');