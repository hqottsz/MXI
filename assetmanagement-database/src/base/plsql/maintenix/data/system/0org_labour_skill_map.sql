--liquibase formatted sql


--changeSet 0org_labour_skill_map:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "ORG_LABOUR_SKILL_MAP"
** 0-Level
** DATE: 17-SEP-13
*********************************************/
insert into org_labour_skill_map(org_db_id, org_id, labour_skill_db_id, labour_skill_cd, est_hourly_cost, esig_req_bool, skill_order, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
	values (0, 1,  0, 'ENG', 75.0, 1, 1, 0, 1, 0, '17-SEP-13', 0, '17-SEP-13', 0, 'MXI');

--changeSet 0org_labour_skill_map:2 stripComments:false
insert into org_labour_skill_map(org_db_id, org_id, labour_skill_db_id, labour_skill_cd, est_hourly_cost, esig_req_bool, skill_order, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
	values (0, 1,  0, 'LBR', 75.0, 1, 2, 0, 1, 0, '17-SEP-13', 0, '17-SEP-13', 0, 'MXI');

--changeSet 0org_labour_skill_map:3 stripComments:false
insert into org_labour_skill_map(org_db_id, org_id, labour_skill_db_id, labour_skill_cd, est_hourly_cost, esig_req_bool, skill_order, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
	values (0, 1,  0, 'PILOT', 100.0, 1, 3, 0, 1, 0, '17-SEP-13', 0, '17-SEP-13', 0, 'MXI');

--changeSet 0org_labour_skill_map:4 stripComments:false
insert into org_labour_skill_map(org_db_id, org_id, labour_skill_db_id, labour_skill_cd, est_hourly_cost, esig_req_bool, skill_order, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
	values (0, 2,  0, 'ENG', 75.0, 1, 1, 0, 1, 0, '17-SEP-13', 0, '17-SEP-13',0, 'MXI');

--changeSet 0org_labour_skill_map:5 stripComments:false
insert into org_labour_skill_map(org_db_id, org_id, labour_skill_db_id, labour_skill_cd, est_hourly_cost, esig_req_bool, skill_order, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
	values (0, 2,  0, 'LBR', 75.0, 1, 2, 0, 1, 0, '17-SEP-13', 0, '17-SEP-13', 0, 'MXI');

--changeSet 0org_labour_skill_map:6 stripComments:false
insert into org_labour_skill_map(org_db_id, org_id, labour_skill_db_id, labour_skill_cd, est_hourly_cost, esig_req_bool, skill_order, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
	values (0, 2,  0, 'PILOT', 100.0, 1, 3, 0, 1, 0, '17-SEP-13', 0, '17-SEP-13', 0, 'MXI');                    