--liquibase formatted sql


--changeSet OPER-641:1 stripComments:false
-- JASPER REPORTS CONFIG PARAMETERS
-- RELIABILITY - MTBUR Comparison
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Reliability.RELIABILITYMTBURComparison', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Reliability/RELIABILITYMTBURComparison', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_REPORT_TYPE WHERE REPORT_NAME = 'Reliability.RELIABILITYMTBURComparison'
);

--changeSet OPER-641:2 stripComments:false
-- JASPER REPORTS MENU ITEMS
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 140016,NULL,'MTBUR Comparison', '/maintenix/servlet/report/generate?aTemplate=Reliability.RELIABILITYMTBURComparison'||chr(38)||'aViewPDF=true', 1,'Reliability Reports',0,0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140016);