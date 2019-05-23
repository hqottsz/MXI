--liquibase formatted sql


--changeSet 0ref_labour_skill:1 stripComments:false
/****************************************************
** INSERT SCRIPT FOR TABLE "REF_LABOUR_SKILL"
** 0-Level
*****************************************************/
insert into ref_labour_skill(labour_skill_db_id, labour_skill_cd, desc_sdesc, desc_ldesc, est_hourly_cost, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PILOT', 'Pilot', 'Pilot', 100, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_labour_skill:2 stripComments:false
insert into ref_labour_skill(labour_skill_db_id, labour_skill_cd, desc_sdesc, desc_ldesc, est_hourly_cost, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LBR', 'Laborer', 'Laborer', 75, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_labour_skill:3 stripComments:false
insert into ref_labour_skill(labour_skill_db_id, labour_skill_cd, desc_sdesc, desc_ldesc, est_hourly_cost, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ENG', 'Engineering', 'Engineering', 75, 0, '27-AUG-02', '27-AUG-02', 100, 'MXI');