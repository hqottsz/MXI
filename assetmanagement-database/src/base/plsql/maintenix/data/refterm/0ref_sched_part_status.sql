--liquibase formatted sql


--changeSet 0ref_sched_part_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_SCHED_PART_STATUS"
** 0-Level
** DATE: 29-JUL-03
*********************************************/
insert into ref_sched_part_status(sched_part_status_db_id, sched_part_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'ACTV', 0, 80, 'Active', 'When a part requirement is added to a task ', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_sched_part_status:2 stripComments:false
insert into ref_sched_part_status(sched_part_status_db_id, sched_part_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'COMPLETE', 0, 83, 'Complete ', 'When the part requirement is used by the task', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_sched_part_status:3 stripComments:false
insert into ref_sched_part_status(sched_part_status_db_id, sched_part_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'CANCEL', 0, 82, 'Cancelled', 'The part requirement is no longer required for the task', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_sched_part_status:4 stripComments:false
insert into ref_sched_part_status(sched_part_status_db_id, sched_part_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'TERMINATE', 0, 82, 'Terminated', 'The part requirement is no longer required for the task', 0, '22-FEB-08', '22-FEB-08', 100, 'MXI');

--changeSet 0ref_sched_part_status:5 stripComments:false
insert into REF_SCHED_PART_STATUS (SCHED_PART_STATUS_DB_ID, SCHED_PART_STATUS_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 0, 1, 'N/A', 'N/A', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');