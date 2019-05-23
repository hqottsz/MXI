--liquibase formatted sql


--changeSet 0ref_maint_prgm_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_MAINT_PRGM_STATUS"
** 0-Level
** DATE: 09/17/07 TIME: 00:00:00
*********************************************/
insert into ref_maint_prgm_status (maint_prgm_status_db_id, maint_prgm_status_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'BUILD', 'Under Construction', 'The maintenance program is under construction.', 0, '17-SEP-007', '17-SEP-007', 0, 'MXI');

--changeSet 0ref_maint_prgm_status:2 stripComments:false
insert into ref_maint_prgm_status (maint_prgm_status_db_id, maint_prgm_status_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'REVISION', 'In Revision', 'The maintenance program is in revision.', 0, '17-SEP-007', '17-SEP-007', 0, 'MXI');

--changeSet 0ref_maint_prgm_status:3 stripComments:false
insert into ref_maint_prgm_status (maint_prgm_status_db_id, maint_prgm_status_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'ACTV', 'Activated', 'The maintenance program is active.', 0, '17-SEP-007', '17-SEP-007', 0, 'MXI');

--changeSet 0ref_maint_prgm_status:4 stripComments:false
insert into ref_maint_prgm_status (maint_prgm_status_db_id, maint_prgm_status_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'SUPRSEDE', 'Superseded', 'The maintenance program is superseded.', 0, '17-SEP-007', '17-SEP-007', 0, 'MXI');