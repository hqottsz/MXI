--liquibase formatted sql


--changeSet MTX-1047:1 stripComments:false
-- ReliabilityFleetStatistics
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
SELECT 140012,NULL,'Fleet Statistics', '/maintenix/servlet/report/generate?aTemplate=Reliability.ReliabilityFleetStatistics'||chr(38)||'aViewPDF=true',1,'Reliability Reports',0,0
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140012
);

--changeSet MTX-1047:2 stripComments:false
-- ReliabilityPirepMarep
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
SELECT 140013,NULL,'PIREP/MAREP', '/maintenix/servlet/report/generate?aTemplate=Reliability.ReliabilityPirepMarep'||chr(38)||'aViewPDF=true',1,'Reliability Reports',0,0
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140013
);

--changeSet MTX-1047:3 stripComments:false
-- TechnicalReliabilityDispatch
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
SELECT 140014,NULL,'Technical Dispatch', '/maintenix/servlet/report/generate?aTemplate=Reliability.TechnicalReliabilityDispatch'||chr(38)||'aViewPDF=true',1,'Reliability Reports',0,0
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140014
);

--changeSet MTX-1047:4 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Reliability.ReliabilityFleetStatistics', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Reliability/ReliabilityFleetStatistics', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_REPORT_TYPE WHERE REPORT_NAME = 'Reliability.ReliabilityFleetStatistics'
);

--changeSet MTX-1047:5 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Reliability.ReliabilityPirepMarep', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Reliability/ReliabilityPirepMarep', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_REPORT_TYPE WHERE REPORT_NAME = 'Reliability.ReliabilityPirepMarep' 
);

--changeSet MTX-1047:6 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Reliability.TechnicalReliabilityDispatch', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Reliability/TechnicalReliabilityDispatch', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_REPORT_TYPE WHERE REPORT_NAME = 'Reliability.TechnicalReliabilityDispatch'
);