--liquibase formatted sql


--changeSet 0ref_lpa_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_PART_STATUS"
** 0-Level
** DATE: Mar 8, 2007
*********************************************/
insert into ref_lpa_status(lpa_status_db_id, lpa_status_cd, desc_sdesc, desc_ldesc, user_status_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'RUNNING', 'Line planning automation in running state.', 'This status shows that line planning automation is executing.', 'RUNNING', 0, '08-MAR-07', '08-MAR-07', 100, 'MXI');

--changeSet 0ref_lpa_status:2 stripComments:false
insert into ref_lpa_status(lpa_status_db_id, lpa_status_cd, desc_sdesc, desc_ldesc, user_status_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'READY', 'Line planning automation in ready state.', 'This status shows that line planning automation stopped executing.', 'READY', 0, '08-MAR-07', '14-MAR-07', 100, 'MXI');

--changeSet 0ref_lpa_status:3 stripComments:false
insert into ref_lpa_status(lpa_status_db_id, lpa_status_cd, desc_sdesc, desc_ldesc, user_status_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'FAILED', 'Line planning automation has failed.', 'This status shows that line planning automation stopped executing due to an error.', 'FAILED', 0, '08-MAR-07', '28-MAR-07', 100, 'MXI');