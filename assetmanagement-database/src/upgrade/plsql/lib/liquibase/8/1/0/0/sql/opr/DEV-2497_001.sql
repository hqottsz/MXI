--liquibase formatted sql


--changeSet DEV-2497_001:1 stripComments:false
-- AircraftConfiguration
/***************************Insert menu items********************************/
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140000,NULL,'Aircraft Configuration', '/maintenix/servlet/report/generate?aTemplate=Compliance.AircraftConfiguration'||chr(38)||'aViewPDF=true',
1,'Compliance Reports',0,0);

--changeSet DEV-2497_001:2 stripComments:false
-- MajorAssemblyLLP - Compliance
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140001,NULL,'Major Assembly LLP', '/maintenix/servlet/report/generate?aTemplate=Compliance.MajorAssemblyLLP'||chr(38)||'aViewPDF=true',
1,'Compliance Reports',0,0);

--changeSet DEV-2497_001:3 stripComments:false
-- AMPStatus -Compliance
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140002,NULL,'AMP Status', '/maintenix/servlet/report/generate?aTemplate=Compliance.AMPStatus'||chr(38)||'aViewPDF=true',
1,'Compliance Reports',0,0);

--changeSet DEV-2497_001:4 stripComments:false
-- AircraftSBStatus - Compliance
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140003,NULL,'Aircraft SB Status', '/maintenix/servlet/report/generate?aTemplate=Compliance.AircraftSBStatus'||chr(38)||'aViewPDF=true',
1,'Compliance Reports',0,0);

--changeSet DEV-2497_001:5 stripComments:false
-- AircraftMaintenanceProgram -Compliance
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140004,NULL,'Aircraft Maintenance Program', '/maintenix/servlet/report/generate?aTemplate=Compliance.AircraftMaintenanceProgram'||chr(38)||'aViewPDF=true',
1,'Compliance Reports',0,0);

--changeSet DEV-2497_001:6 stripComments:false
-- AircraftLLP - Compliance
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140005,NULL,'Aircraft LLP', '/maintenix/servlet/report/generate?aTemplate=Compliance.AircraftLLP'||chr(38)||'aViewPDF=true',
1,'Compliance Reports',0,0);

--changeSet DEV-2497_001:7 stripComments:false
-- ADStatusAirframe - Compliance
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140006,NULL,'AD Status Airframe', '/maintenix/servlet/report/generate?aTemplate=Compliance.ADStatusAirframe'||chr(38)||'aViewPDF=true',
1,'Compliance Reports',0,0);

--changeSet DEV-2497_001:8 stripComments:false
-- ForecastOfAircraft - Maintenance
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140007,NULL,'Forecast Of Aircraft Maintenance', '/maintenix/servlet/report/generate?aTemplate=Maintenance.ForecastOfAircraftMaintenance'||chr(38)||'aViewPDF=true',
1,'Maintenance Reports',0,0);

--changeSet DEV-2497_001:9 stripComments:false
-- AircraftDeferredDefects
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140008,NULL,'Aircraft Deferred Defects', '/maintenix/servlet/report/generate?aTemplate=Maintenance.AircraftDeferredDefects'||chr(38)||'aViewPDF=true',
1,'Maintenance Reports',0,0);

--changeSet DEV-2497_001:10 stripComments:false
-- Aircraft Configuration  - 'JASPER_SSO' for Custom reports with arguments
/***************************Insert report configurations*********************/
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Compliance.AircraftConfiguration', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Compliance/AircraftConfiguration', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'Compliance.AircraftConfiguration');

--changeSet DEV-2497_001:11 stripComments:false
-- ADStatusAirframe
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Compliance.ADStatusAirframe', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Compliance/ADStatusAircraft', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'Compliance.ADStatusAirframe');

--changeSet DEV-2497_001:12 stripComments:false
-- AircraftLLP
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Compliance.AircraftLLP', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Compliance/AircraftLLP', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'Compliance.AircraftLLP');

--changeSet DEV-2497_001:13 stripComments:false
-- -AircraftMaintenanceProgram
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Compliance.AircraftMaintenanceProgram', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Compliance/AircraftMaintenanceProgram', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'Compliance.AircraftMaintenanceProgram');

--changeSet DEV-2497_001:14 stripComments:false
-- AircraftSBStatus
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Compliance.AircraftSBStatus', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Compliance/AircraftSBStatus', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'Compliance.AircraftSBStatus');

--changeSet DEV-2497_001:15 stripComments:false
-- -AMPStatus
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Compliance.AMPStatus', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Compliance/AMPStatus', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'Compliance.AMPStatus');

--changeSet DEV-2497_001:16 stripComments:false
-- -MajorAssemblyLLP
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Compliance.MajorAssemblyLLP', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Compliance/MajorAssemblyLLP', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'Compliance.MajorAssemblyLLP');

--changeSet DEV-2497_001:17 stripComments:false
-- AircraftDeferredDefects
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Compliance.AircraftDeferredDefects', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Maintenance/AircraftDeferredDefects', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'Compliance.AircraftDeferredDefects');

--changeSet DEV-2497_001:18 stripComments:false
-- ForecastOfAircraftMaintenance
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Compliance.ForecastOfAircraftMaintenance', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Maintenance/ForecastOfAircraftMaintenance', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'Compliance.ForecastOfAircraftMaintenance');