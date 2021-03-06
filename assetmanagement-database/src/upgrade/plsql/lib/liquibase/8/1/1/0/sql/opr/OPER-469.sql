--liquibase formatted sql


--changeSet OPER-469:1 stripComments:false
-- JASPER REPORTS CONFIG PARAMETERS
-- Component Reliability
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Reliability.ComponentReliability', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Reliability/ComponentReliability', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_REPORT_TYPE WHERE REPORT_NAME = 'Reliability.ComponentReliability'
);

--changeSet OPER-469:2 stripComments:false
-- JASPER REPORTS MENU ITEMS
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 140017,NULL,'Component Reliability', '/maintenix/servlet/report/generate?aTemplate=Reliability.ComponentReliability'||chr(38)||'aViewPDF=true', 1,'Reliability Reports',0,0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140017);

--changeSet OPER-469:3 stripComments:false
-- Component Reliability
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Reliability.ReliabilityTechnicalDifficulty', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Reliability/ReliabilityTechnicalDifficulty', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_REPORT_TYPE WHERE REPORT_NAME = 'Reliability.ReliabilityTechnicalDifficulty'
);

--changeSet OPER-469:4 stripComments:false
-- JASPER REPORTS MENU ITEMS
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 140025,NULL,'Technical Difficulty', '/maintenix/servlet/report/generate?aTemplate=Reliability.ReliabilityTechnicalDifficulty'||chr(38)||'aViewPDF=true', 1,'Reliability Reports',0,0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140025);