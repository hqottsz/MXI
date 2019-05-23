--liquibase formatted sql


--changeSet 0utl_role:1 stripComments:false
/******************************************
** 0-Level INSERT SCRIPT FOR UTL_ROLE
*******************************************/
INSERT INTO UTL_ROLE (ROLE_ID, ROLE_CD, ROLE_NAME, UTL_ID) 
VALUES (19000, 'ADMIN', 'Administrator', 0);

--changeSet 0utl_role:2 stripComments:false
insert into UTL_ROLE (ROLE_ID, ROLE_CD, ROLE_NAME, UTL_ID)
values (0, 'N/A', 'N/A', 0);

--changeSet 0utl_role:3 stripComments:false
INSERT INTO UTL_ROLE (ROLE_ID, ROLE_CD, ROLE_NAME, UTL_ID) 
VALUES (17000, 'SPEC2K', 'Spec 2000 Integration', 0);

--changeSet 0utl_role:4 stripComments:false
INSERT INTO UTL_ROLE (ROLE_ID, ROLE_CD, ROLE_NAME, UTL_ID) 
VALUES (17001, 'INTEGRA', 'System Integrator', 0);