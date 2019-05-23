--liquibase formatted sql


--changeSet OPER-475:1 stripComments:false
-- JASPER REPORTS CONFIG PARAMETERS
-- Fleet Deferred Defects
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Maintenance.DeferredDefectsList', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Maintenance/DeferredDefectsList', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_report_type WHERE REPORT_NAME = 'Maintenance.DeferredDefectsList');

--changeSet OPER-475:2 stripComments:false
-- JASPER REPORTS MENU ITEMS
-- Fleet Deferred Defects
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 140015,NULL,'Fleet Deferred Defects', '/maintenix/servlet/report/generate?aTemplate=Maintenance.DeferredDefectsList'||chr(38)||'aViewPDF=true', 1,'Maintenance Reports',0,0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140015);