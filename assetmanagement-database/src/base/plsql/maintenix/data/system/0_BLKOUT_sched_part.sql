--liquibase formatted sql


--changeSet 0_BLKOUT_sched_part:1 stripComments:false
/**********************************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE SCHED_PART FOR BLACKOUT DATA ONLY**
***********************************************************************/
insert into SCHED_PART (SCHED_DB_ID, SCHED_ID, SCHED_PART_ID, SCHED_BOM_PART_DB_ID, SCHED_BOM_PART_ID, SCHED_PART_STATUS_DB_ID, SCHED_PART_STATUS_CD, SPEC_PART_NO_DB_ID, SPEC_PART_NO_ID, NH_ASSMBL_DB_ID, NH_ASSMBL_CD, NH_ASSMBL_BOM_ID, NH_ASSMBL_POS_ID, ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID, ASSMBL_POS_ID, SCHED_QT, PART_COST, PART_NOTE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 1004, 1, null, null, 0, 'BLKOUT', null, null, null, null, null, null, null, null, null, null, 0, null, null, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');