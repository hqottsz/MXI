--liquibase formatted sql


--changeSet 0org_hr:1 stripComments:false
-- mxfrc user, linked to row in utl_user.user_id = 10
/********************************************
** INSERT SCRIPT FOR TABLE "ORG_HR"
** DATE: 12-JULY-05
*********************************************/
INSERT INTO ORG_HR (HR_DB_ID, HR_ID, USER_ID, PAY_METHOD_DB_ID, PAY_METHOD_CD, RSTAT_CD, ALL_AUTHORITY_BOOL,HR_CD, ACTUAL_HOURLY_COST, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0,2, 10, 0, 'SALARY',0, 0,'FRC_SYSTEM_USER', NULL,'02-JUN-04', '02-JUN-04', 10 , 'MXI');

--changeSet 0org_hr:2 stripComments:false
-- admin user, linked to row in utl_user.user_id = 0
INSERT INTO ORG_HR (HR_DB_ID, HR_ID, USER_ID, PAY_METHOD_DB_ID, PAY_METHOD_CD, RSTAT_CD, ALL_AUTHORITY_BOOL,HR_CD, ACTUAL_HOURLY_COST, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 3, 0, 0, 'SALARY',0, 0,'SYSTEM', NULL,'26-JUL-04', '26-JUL-04',10, 'MXI');

--changeSet 0org_hr:3 stripComments:false
-- mxintegration user, linked to row in utl_user.user_id =15
INSERT INTO ORG_HR (HR_DB_ID, HR_ID, USER_ID, PAY_METHOD_DB_ID, PAY_METHOD_CD, RSTAT_CD, ALL_AUTHORITY_BOOL,HR_CD, ACTUAL_HOURLY_COST, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 4, 15, 0, 'SALARY',0, 0,'SYSTEM', NULL,'15-JUL-08', '15-JUL-08',10, 'MXI');

--changeSet 0org_hr:5 stripComments:false
-- deployed ops global data
insert into ORG_HR (HR_DB_ID, HR_ID, USER_ID, HR_CD, PAY_METHOD_DB_ID, PAY_METHOD_CD, ALL_AUTHORITY_BOOL, ALL_LOCATIONS_BOOL, ACTUAL_HOURLY_COST, LIC_CARD_ISSUE_DT, LIC_CARD_PRINT_DT, LIC_CARD_CHANGE_DT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 5, 4, 'N/A', 0, 'SALARY', 0, 0, null, null, null, null, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0org_hr:6 stripComments:false
INSERT INTO ORG_HR (HR_DB_ID, HR_ID, HR_CD, USER_ID, PAY_METHOD_DB_ID, PAY_METHOD_CD, RSTAT_CD, ALL_AUTHORITY_BOOL, ACTUAL_HOURLY_COST, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 6, 'GLOBAL',5, null, null, 1,null, null, '01-JUN-09','01-JUN-09', 0, 'MXI');

--changeSet 0org_hr:7 stripComments:false
INSERT INTO ORG_HR (HR_DB_ID, HR_ID, USER_ID, PAY_METHOD_DB_ID, PAY_METHOD_CD, RSTAT_CD, ALL_AUTHORITY_BOOL,HR_CD, ACTUAL_HOURLY_COST, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 10, 17, 0, 'SALARY',0, 0,'SYSTEM', NULL,'15-JUL-08', '15-JUL-08',10, 'MXI');

--changeSet 0org_hr:8 stripComments:false
-- mxpurchase user, user as purchasing contact on auto generated POs, linked to row in utl_user.user_id =18
INSERT INTO ORG_HR (HR_DB_ID, HR_ID, USER_ID, PAY_METHOD_DB_ID, PAY_METHOD_CD, RSTAT_CD, ALL_AUTHORITY_BOOL,HR_CD, ACTUAL_HOURLY_COST, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 11, 18, 0, 'SALARY',0, 0,'SYSTEM', NULL,to_date('01-07-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-07-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),0, 'MXI');