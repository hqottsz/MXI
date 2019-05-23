--liquibase formatted sql


--changeSet 0utl_user_role:1 stripComments:false
/*************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE UTL_USER_ROLE
**************************************************/
INSERT INTO UTL_USER_ROLE( USER_ID, ROLE_ID, ROLE_ORDER, UTL_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0,19000, 0, 0, 0, '22-MAR-06', '22-MAR-06', 100, 'MXI');

--changeSet 0utl_user_role:2 stripComments:false
INSERT INTO UTL_USER_ROLE( USER_ID, ROLE_ID, ROLE_ORDER, UTL_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (17,17000, 1, 0, 0, to_date('16-07-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-07-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0utl_user_role:3 stripComments:false
INSERT INTO UTL_USER_ROLE( USER_ID, ROLE_ID, ROLE_ORDER, UTL_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (15,17001, 0, 0, 0, to_date('09-08-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('09-08-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0utl_user_role:4 stripComments:false
INSERT INTO UTL_USER_ROLE( USER_ID, ROLE_ID, ROLE_ORDER, UTL_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (15,17000, 1, 0, 0, to_date('17-01-2017 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-01-2017 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');