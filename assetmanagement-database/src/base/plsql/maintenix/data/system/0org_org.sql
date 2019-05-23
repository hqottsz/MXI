--liquibase formatted sql


--changeSet 0org_org:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "ORG_ORG"
** 0-Level
** DATE: 29-APR-08
*********************************************/
insert into org_org(org_db_id, org_id, org_type_db_id, org_type_cd,  nh_org_db_id, nh_org_id, org_cd, code_mdesc, org_sdesc, org_ldesc,company_org_db_id, company_org_id,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            values (0, 1,  0, 'ADMIN',  NULL, NULL, 'MXI', '', 'Mxi Technologies', 'The root organization',0, 1, 0, '29-APR-08', '29-APR-08', 0, 'MXI');                   

--changeSet 0org_org:2 stripComments:false
insert into org_org(org_db_id, org_id, org_type_db_id, org_type_cd, nh_org_db_id, nh_org_id, org_cd, code_mdesc, org_sdesc, org_ldesc, company_org_db_id, company_org_id,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            values (0, 2, 0, 'DEFAULT', 0, 1, 'DEFAULT', 'MXI/', 'Default', 'The Restricted sub-organization for root organization ', 0, 1, 0, '29-APR-08', '29-APR-08', 0, 'MXI');