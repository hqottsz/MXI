--liquibase formatted sql


--changeSet 0_BLKOUT_evt_event:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE EVT_EVENT
** 0-Level
** DATE: 01-JUN-09
*********************************************/
insert into EVT_EVENT (EVENT_DB_ID, EVENT_ID, EVENT_TYPE_DB_ID, EVENT_TYPE_CD, STAGE_REASON_DB_ID, STAGE_REASON_CD, EDITOR_HR_DB_ID, EDITOR_HR_ID, EVENT_STATUS_DB_ID, EVENT_STATUS_CD, EVENT_REASON_DB_ID, EVENT_REASON_CD, BITMAP_DB_ID, BITMAP_TAG, SCHED_PRIORITY_DB_ID, SCHED_PRIORITY_CD, DATA_SOURCE_DB_ID, DATA_SOURCE_CD, NH_EVENT_DB_ID, NH_EVENT_ID, H_EVENT_DB_ID, H_EVENT_ID, EVENT_SDESC, EXT_KEY_SDESC, HIST_BOOL, CONTACT_INFO_SDESC, SEQ_ERR_BOOL, EVENT_LDESC, EVENT_DT, EVENT_GDT, SCHED_START_DT, SCHED_START_GDT, SCHED_END_DT, SCHED_END_GDT, ACTUAL_START_DT, ACTUAL_START_GDT, DOC_REF_SDESC, SUB_EVENT_ORD, DELTA_QT, ACCOUNT_DB_ID, ACCOUNT_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 1000, 0, 'IX', null, null, null, null, 0, 'IXBLKOUT', null, null, 0, 1, null, null, null, null, null, null, 0, 1000, 'SBLKOUT', null, 1, null, 0, null, to_date('01-03-2009', 'dd-mm-yyyy'), to_date('01-03-2009', 'dd-mm-yyyy'), null, null, null, null, null, null, null, null, null, null, null, 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0_BLKOUT_evt_event:2 stripComments:false
insert into EVT_EVENT (EVENT_DB_ID, EVENT_ID, EVENT_TYPE_DB_ID, EVENT_TYPE_CD, STAGE_REASON_DB_ID, STAGE_REASON_CD, EDITOR_HR_DB_ID, EDITOR_HR_ID, EVENT_STATUS_DB_ID, EVENT_STATUS_CD, EVENT_REASON_DB_ID, EVENT_REASON_CD, BITMAP_DB_ID, BITMAP_TAG, SCHED_PRIORITY_DB_ID, SCHED_PRIORITY_CD, DATA_SOURCE_DB_ID, DATA_SOURCE_CD, NH_EVENT_DB_ID, NH_EVENT_ID, H_EVENT_DB_ID, H_EVENT_ID, EVENT_SDESC, EXT_KEY_SDESC, HIST_BOOL, CONTACT_INFO_SDESC, SEQ_ERR_BOOL, EVENT_LDESC, EVENT_DT, EVENT_GDT, SCHED_START_DT, SCHED_START_GDT, SCHED_END_DT, SCHED_END_GDT, ACTUAL_START_DT, ACTUAL_START_GDT, DOC_REF_SDESC, SUB_EVENT_ORD, DELTA_QT, ACCOUNT_DB_ID, ACCOUNT_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 1002, 0, 'PO', null, null, null, null, 0, 'POBLKOUT', null, null, 0, 1, null, null, null, null, null, null, null, null, 'PBLKOUT', null, 1, null, 0, null, to_date('01-03-2009', 'dd-mm-yyyy'), to_date('01-03-2009', 'dd-mm-yyyy'), null, null, null, null, null, null, null, null, null, null, null, 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0_BLKOUT_evt_event:3 stripComments:false
insert into EVT_EVENT (EVENT_DB_ID, EVENT_ID, EVENT_TYPE_DB_ID, EVENT_TYPE_CD, STAGE_REASON_DB_ID, STAGE_REASON_CD, EDITOR_HR_DB_ID, EDITOR_HR_ID, EVENT_STATUS_DB_ID, EVENT_STATUS_CD, EVENT_REASON_DB_ID, EVENT_REASON_CD, BITMAP_DB_ID, BITMAP_TAG, SCHED_PRIORITY_DB_ID, SCHED_PRIORITY_CD, DATA_SOURCE_DB_ID, DATA_SOURCE_CD, NH_EVENT_DB_ID, NH_EVENT_ID, H_EVENT_DB_ID, H_EVENT_ID, EVENT_SDESC, EXT_KEY_SDESC, HIST_BOOL, CONTACT_INFO_SDESC, SEQ_ERR_BOOL, EVENT_LDESC, EVENT_DT, EVENT_GDT, SCHED_START_DT, SCHED_START_GDT, SCHED_END_DT, SCHED_END_GDT, ACTUAL_START_DT, ACTUAL_START_GDT, DOC_REF_SDESC, SUB_EVENT_ORD, DELTA_QT, ACCOUNT_DB_ID, ACCOUNT_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 1003, 0, 'PR', null, null, null, null, 0, 'PRBLKOUT', null, null, 0, 1, null, null, null, null, null, null, 0, 1003, 'RBLKOUT', null, 1, null, 0, null, to_date('01-03-2009', 'dd-mm-yyyy'), to_date('01-03-2009', 'dd-mm-yyyy'), null, null, null, null, null, null, null, null, null, null, null, 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0_BLKOUT_evt_event:4 stripComments:false
insert into EVT_EVENT (EVENT_DB_ID, EVENT_ID, EVENT_TYPE_DB_ID, EVENT_TYPE_CD, STAGE_REASON_DB_ID, STAGE_REASON_CD, EDITOR_HR_DB_ID, EDITOR_HR_ID, EVENT_STATUS_DB_ID, EVENT_STATUS_CD, EVENT_REASON_DB_ID, EVENT_REASON_CD, BITMAP_DB_ID, BITMAP_TAG, SCHED_PRIORITY_DB_ID, SCHED_PRIORITY_CD, DATA_SOURCE_DB_ID, DATA_SOURCE_CD, NH_EVENT_DB_ID, NH_EVENT_ID, H_EVENT_DB_ID, H_EVENT_ID, EVENT_SDESC, EXT_KEY_SDESC, HIST_BOOL, CONTACT_INFO_SDESC, SEQ_ERR_BOOL, EVENT_LDESC, EVENT_DT, EVENT_GDT, SCHED_START_DT, SCHED_START_GDT, SCHED_END_DT, SCHED_END_GDT, ACTUAL_START_DT, ACTUAL_START_GDT, DOC_REF_SDESC, SUB_EVENT_ORD, DELTA_QT, ACCOUNT_DB_ID, ACCOUNT_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 1004, 0, 'TS', null, null, null, null, 0, 'BLKOUT', null, null, 0, 1, null, null, null, null, null, null, 0, 1004, 'BLKOUT', null, 1, null, 0, null, to_date('01-03-2009', 'dd-mm-yyyy'), to_date('01-03-2009', 'dd-mm-yyyy'), to_date('01-03-2009', 'dd-mm-yyyy'), to_date('01-03-2009', 'dd-mm-yyyy'), to_date('01-03-2009', 'dd-mm-yyyy'), to_date('01-03-2009', 'dd-mm-yyyy'), to_date('01-03-2009', 'dd-mm-yyyy'), to_date('01-03-2009', 'dd-mm-yyyy'), null, null, null, null, null, 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0_BLKOUT_evt_event:5 stripComments:false
insert into EVT_EVENT (EVENT_DB_ID, EVENT_ID, EVENT_TYPE_DB_ID, EVENT_TYPE_CD, STAGE_REASON_DB_ID, STAGE_REASON_CD, EDITOR_HR_DB_ID, EDITOR_HR_ID, EVENT_STATUS_DB_ID, EVENT_STATUS_CD, EVENT_REASON_DB_ID, EVENT_REASON_CD, BITMAP_DB_ID, BITMAP_TAG, SCHED_PRIORITY_DB_ID, SCHED_PRIORITY_CD, DATA_SOURCE_DB_ID, DATA_SOURCE_CD, NH_EVENT_DB_ID, NH_EVENT_ID, H_EVENT_DB_ID, H_EVENT_ID, EVENT_SDESC, EXT_KEY_SDESC, HIST_BOOL, CONTACT_INFO_SDESC, SEQ_ERR_BOOL, EVENT_LDESC, EVENT_DT, EVENT_GDT, SCHED_START_DT, SCHED_START_GDT, SCHED_END_DT, SCHED_END_GDT, ACTUAL_START_DT, ACTUAL_START_GDT, DOC_REF_SDESC, SUB_EVENT_ORD, DELTA_QT, ACCOUNT_DB_ID, ACCOUNT_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 1005, 0, 'CF', null, null, null, null, 0, 'CFBLKOUT', null, null, 0, 1, null, null, null, null, null, null, 0, 1005, 'BLKOUT', null, 1, null, 0, null, to_date('01-03-2009', 'dd-mm-yyyy'), to_date('01-03-2009', 'dd-mm-yyyy'), null, null, null, null, to_date('01-03-2009', 'dd-mm-yyyy'), to_date('01-03-2009', 'dd-mm-yyyy'), null, null, null, null, null, 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');