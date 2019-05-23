--liquibase formatted sql


--changeSet 0_BLKOUT_sd_fault:1 stripComments:false
-- Blackout data
/**********************************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE SD_FAULT FOR BLACKOUT DATA ONLY**
***********************************************************************/
insert into SD_FAULT (FAULT_DB_ID, FAULT_ID, FAIL_MODE_DB_ID, FAIL_MODE_ID, FAIL_CATGRY_DB_ID, FAIL_CATGRY_CD, FLIGHT_STAGE_DB_ID, FLIGHT_STAGE_CD, FAIL_DEFER_DB_ID, FAIL_DEFER_CD, FAIL_PRIORITY_DB_ID, FAIL_PRIORITY_CD, FAIL_TYPE_DB_ID, FAIL_TYPE_CD, FAULT_SOURCE_DB_ID, FAULT_SOURCE_CD, FOUND_BY_HR_DB_ID, FOUND_BY_HR_ID, FAIL_SEV_DB_ID, FAIL_SEV_CD, DEFER_REF_SDESC, PREC_PROC_LDESC, SDR_BOOL, OP_RESTRICTION_LDESC, DEFER_CD_SDESC, EVAL_BOOL, MAINT_EVT_BOOL,  EXT_RAISED_BOOL, EXT_CONTROLLED_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 1005, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 'BLKOUT', null, null, 0, null, null, 0, 0, 0, 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');