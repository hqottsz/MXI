--liquibase formatted sql


--changeSet 0_BLKOUT_sched_rmvd_part:1 stripComments:false
/**********************************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE SCHED_RMVD_PART FOR BLACKOUT DATA ONLY**
***********************************************************************/
insert into SCHED_RMVD_PART (SCHED_DB_ID, SCHED_ID, SCHED_PART_ID, SCHED_RMVD_PART_ID, INV_NO_DB_ID, INV_NO_ID, PART_NO_DB_ID, PART_NO_ID, SERIAL_NO_OEM, RMVD_QT, REMOVE_REASON_DB_ID, REMOVE_REASON_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 1004, 1, 1, null, null, null, null, null, null, null, null, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');