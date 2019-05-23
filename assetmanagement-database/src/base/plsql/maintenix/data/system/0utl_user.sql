--liquibase formatted sql


--changeSet 0utl_user:1 stripComments:false
/***********************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE "UTL_USER" **
************************************************/
INSERT INTO UTL_USER (USER_ID, USERNAME, PASSWORD, FIRST_NAME, MIDDLE_NAME, LAST_NAME, ALERT_EMAIL_ADDR, EMAIL_ADDR, LOCKED_BOOL, UTL_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
      VALUES (0, 'ADMIN', 'password', 'Admin'   , NULL, 'Admin', NULL, NULL, 0, 0, 0, 0, '15-JAN-02', '15-JAN-02', 100, 'MXI');

--changeSet 0utl_user:2 stripComments:false
INSERT INTO UTL_USER (USER_ID, USERNAME, PASSWORD, FIRST_NAME, MIDDLE_NAME, LAST_NAME, ALERT_EMAIL_ADDR, EMAIL_ADDR, LOCKED_BOOL, FORCE_PASSWORD_CHANGE_BOOL, UTL_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
      VALUES (4, 'notavailable', 'password', 'N/A', null, 'N/A', null, null, 1, 0, 0, 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0utl_user:3 stripComments:false
INSERT INTO UTL_USER (USER_ID, USERNAME, PASSWORD, FIRST_NAME, MIDDLE_NAME, LAST_NAME, ALERT_EMAIL_ADDR, EMAIL_ADDR, LOCKED_BOOL, UTL_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
      VALUES (5, 'mxglobal', 'password', 'mxglobal', null, 'mxglobal', null, null, 0, 0, 0, 1, '01-JUN-09','01-JUN-09', 0, 'MXI');

--changeSet 0utl_user:4 stripComments:false
INSERT INTO UTL_USER (USER_ID, USERNAME, PASSWORD, FIRST_NAME, MIDDLE_NAME, LAST_NAME, ALERT_EMAIL_ADDR, EMAIL_ADDR, LOCKED_BOOL, UTL_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
      VALUES (10, 'mxfrc', 'password', 'mxfrc'   , NULL, 'frc', NULL, NULL, 0, 0, 0, 0, '15-JAN-02', '15-JAN-02', 100, 'MXI');

--changeSet 0utl_user:5 stripComments:false
INSERT INTO UTL_USER (USER_ID, USERNAME, PASSWORD, FIRST_NAME, MIDDLE_NAME, LAST_NAME, ALERT_EMAIL_ADDR, EMAIL_ADDR, LOCKED_BOOL, UTL_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
      VALUES (11, 'mxsystem', 'password', 'mxsystem',NULL, 'mxsystem', NULL, NULL, 0, 0, 0, 0, '22-JAN-09', '22-JAN-09', 100, 'MXI');

--changeSet 0utl_user:6 stripComments:false
INSERT INTO UTL_USER (USER_ID, USERNAME, PASSWORD, FIRST_NAME, MIDDLE_NAME, LAST_NAME, ALERT_EMAIL_ADDR, EMAIL_ADDR, LOCKED_BOOL, UTL_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
      VALUES (15, 'mxintegration', 'password', 'mxintegration'   , NULL, 'mxintegration', NULL, NULL, 0, 0, 0, 0, '15-JUL-08', '15-JUL-08', 100, 'MXI');

--changeSet 0utl_user:8 stripComments:false
INSERT INTO UTL_USER (USER_ID, USERNAME, PASSWORD, FIRST_NAME, MIDDLE_NAME, LAST_NAME, ALERT_EMAIL_ADDR, EMAIL_ADDR, LOCKED_BOOL, UTL_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
      VALUES (17, 'mxspec2000pointegration', 'password', 'mxspec2000pointegration'   , NULL, 'mxspec2000pointegration', NULL, NULL, 0, 0, 0, 0, to_date('16-07-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-07-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0utl_user:9 stripComments:false
INSERT INTO UTL_USER (USER_ID, USERNAME, PASSWORD, FIRST_NAME, MIDDLE_NAME, LAST_NAME, ALERT_EMAIL_ADDR, EMAIL_ADDR, LOCKED_BOOL, UTL_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
    VALUES (18, 'mxpurchase', 'password', 'mxpurchase', NULL, 'mxpurchase', NULL, NULL, 0, 0, 0, 0, to_date('01-07-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-07-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');