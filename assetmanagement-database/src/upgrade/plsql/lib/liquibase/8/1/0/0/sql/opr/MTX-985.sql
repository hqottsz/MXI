--liquibase formatted sql


--changeSet MTX-985:1 stripComments:false
-- JASPER REPORTS CONFIG PARAMETERS
-- EASAForm1
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Maintenance.EASAForm1', 'JASPER_REST', '/organizations/Maintenix/Reports/Operator/Maintenance/EASAForm1', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_report_type WHERE REPORT_NAME = 'Maintenance.EASAForm1');

--changeSet MTX-985:2 stripComments:false
-- FAAForm8130
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Maintenance.FAAForm8130', 'JASPER_REST', '/organizations/Maintenix/Reports/Operator/Maintenance/FAAForm8130', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_report_type WHERE REPORT_NAME = 'Maintenance.FAAForm8130');

--changeSet MTX-985:3 stripComments:false
-- CertificateofReleasetoService
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Maintenance.CertificateofReleasetoService', 'JASPER_REST', '/organizations/Maintenix/Reports/Operator/Maintenance/CertificateofReleasetoService', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_report_type WHERE REPORT_NAME = 'Maintenance.CertificateofReleasetoService');

--changeSet MTX-985:4 stripComments:false
-- JASPER REPORTS MENU ITEMS
-- EASAForm1
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 140009,NULL,'EASA Form 1', '/maintenix/servlet/report/generate?aTemplate=Maintenance.EASAForm1'||chr(38)||'aViewPDF=true', 1,'Maintenance Reports',0,0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140009);

--changeSet MTX-985:5 stripComments:false
-- FAAForm8130
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 140010,NULL,'FAA Form 8130', '/maintenix/servlet/report/generate?aTemplate=Maintenance.FAAForm8130'||chr(38)||'aViewPDF=true', 1,'Maintenance Reports',0,0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140010);

--changeSet MTX-985:6 stripComments:false
-- CertificateofReleasetoService
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 140011,NULL,'Certificate of Release to Service', '/maintenix/servlet/report/generate?aTemplate=Maintenance.CertificateofReleasetoService'||chr(38)||'aViewPDF=true', 1,'Maintenance Reports',0,0                   
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140011);

--changeSet MTX-985:7 stripComments:false
-- JASPER REPORTS MENU ITEM ARGS
-- EASAForm1 Reports
INSERT INTO UTL_MENU_ITEM_ARG (MENU_ID, ARG_CD, UTL_ID)
SELECT 140009,'aInventoryKey',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM_ARG WHERE MENU_ID = 140009 AND ARG_CD = 'aInventoryKey');

--changeSet MTX-985:8 stripComments:false
-- EASAForm1 Reports
INSERT INTO UTL_MENU_ITEM_ARG (MENU_ID, ARG_CD, UTL_ID)
SELECT 140010,'aInventoryKey',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM_ARG WHERE MENU_ID = 140010 AND ARG_CD = 'aInventoryKey');

--changeSet MTX-985:9 stripComments:false
-- EASAForm1 Reports
INSERT INTO UTL_MENU_ITEM_ARG (MENU_ID, ARG_CD, UTL_ID)
SELECT 140011,'aWorkPackageKey',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM_ARG WHERE MENU_ID = 140011 AND ARG_CD = 'aWorkPackageKey');