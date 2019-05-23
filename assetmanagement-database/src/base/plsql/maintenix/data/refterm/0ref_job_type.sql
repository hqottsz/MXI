--liquibase formatted sql


--changeSet 0ref_job_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_JOB_TYPE"
** 0-Level
*********************************************/
INSERT INTO ref_job_type ( job_type_db_id, job_type_cd, desc_sdesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
VALUES ( 0, 'TASK_CRD', 'Task Card job type', 0, '23-MAR-01', '23-MAR-01', 0, 'MXI' );

--changeSet 0ref_job_type:2 stripComments:false
INSERT INTO ref_job_type ( job_type_db_id, job_type_cd, desc_sdesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
VALUES ( 0, 'ISSUE_TX', 'Issue ticket job type', 0, '23-MAR-01', '23-MAR-01', 0, 'MXI' );

--changeSet 0ref_job_type:3 stripComments:false
INSERT INTO ref_job_type ( job_type_db_id, job_type_cd, desc_sdesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
VALUES ( 0, 'PAWAY_TX', 'Put away ticket job type', 0, '23-MAR-01', '23-MAR-01', 0, 'MXI' );