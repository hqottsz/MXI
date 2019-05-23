--liquibase formatted sql


--changeSet 0ref_org_type:1 stripComments:false
-- Special types of organizations
/********************************************
** INSERT SCRIPT FOR TABLE "REF_ORG_TYPE"
** 0-Level
** DATE: 29-APR-08
*********************************************/
insert into ref_org_type(org_type_db_id, org_type_cd, desc_sdesc, desc_ldesc,  company_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            values (0, 'ADMIN','Admin', 'Top level organization, parent of all organizations in Maintenix.', 1, 1, '29-APR-08', '29-APR-08', 0, 'MXI');                 

--changeSet 0ref_org_type:2 stripComments:false
insert into ref_org_type(org_type_db_id, org_type_cd, desc_sdesc, desc_ldesc,  company_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            values (0, 'DEFAULT','Default','Temporary organization for users with no assignments in a company. One exists for every company.', 0, 1, '29-APR-08', '29-APR-08', 0, 'MXI');                 

--changeSet 0ref_org_type:3 stripComments:false
-- Company organizations
insert into ref_org_type(org_type_db_id, org_type_cd, desc_sdesc, desc_ldesc, company_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            values (0, 'OPERATOR','Operator', 'Operator type of organization', 1, 0, '29-APR-08', '29-APR-08', 0, 'MXI');

--changeSet 0ref_org_type:4 stripComments:false
insert into ref_org_type(org_type_db_id, org_type_cd, desc_sdesc, desc_ldesc, company_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            values (0, 'MRO','MRO', 'MRO type of organization', 1, 0, '29-APR-08', '29-APR-08', 0, 'MXI');                 

--changeSet 0ref_org_type:5 stripComments:false
-- Non-company organizations                 
insert into ref_org_type(org_type_db_id, org_type_cd, desc_sdesc, desc_ldesc, company_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            values (0, 'DEPT','Department', 'Department type of organization', 0, 0, '29-APR-08', '29-APR-08', 0, 'MXI');

--changeSet 0ref_org_type:6 stripComments:false
insert into ref_org_type(org_type_db_id, org_type_cd, desc_sdesc, desc_ldesc, company_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            values (0, 'CREW','Crew', 'Crew type of organization', 0, 0, '29-APR-08', '29-APR-08', 0, 'MXI');