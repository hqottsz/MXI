--liquibase formatted sql
SET DEFINE OFF

--changeSet 0utl_menu_item:1 stripComments:false
-- Tell Oracle not to prompt for parameter when it encounters ampersands in the string values
-- AircraftConfiguration
/******************************************
** 0-Level INSERT SCRIPT FOR UTL_MENU_ITEM
*******************************************/
/***********************
** Report menus
************************/
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140000,NULL,'Aircraft Configuration', '/maintenix/servlet/report/generate?aTemplate=Compliance.AircraftConfiguration'||chr(38)||'aViewPDF=true',
1,'Compliance Reports',0,0);

--changeSet 0utl_menu_item:2 stripComments:false
-- MajorAssemblyLLP - Compliance
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140001,NULL,'Major Assembly LLP', '/maintenix/servlet/report/generate?aTemplate=Compliance.MajorAssemblyLLP'||chr(38)||'aViewPDF=true',
1,'Compliance Reports',0,0);

--changeSet 0utl_menu_item:3 stripComments:false
-- AMPStatus -Compliance
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140002,NULL,'AMP Status', '/maintenix/servlet/report/generate?aTemplate=Compliance.AMPStatus'||chr(38)||'aViewPDF=true',
1,'Compliance Reports',0,0);

--changeSet 0utl_menu_item:4 stripComments:false
-- AircraftSBStatus - Compliance
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140003,NULL,'Aircraft SB Status', '/maintenix/servlet/report/generate?aTemplate=Compliance.AircraftSBStatus'||chr(38)||'aViewPDF=true',
1,'Compliance Reports',0,0);

--changeSet 0utl_menu_item:5 stripComments:false
-- AircraftMaintenanceProgram -Compliance
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140004,NULL,'Aircraft Maintenance Program', '/maintenix/servlet/report/generate?aTemplate=Compliance.AircraftMaintenanceProgram'||chr(38)||'aViewPDF=true',
1,'Compliance Reports',0,0);

--changeSet 0utl_menu_item:6 stripComments:false
-- AircraftLLP - Compliance
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140005,NULL,'Aircraft LLP', '/maintenix/servlet/report/generate?aTemplate=Compliance.AircraftLLP'||chr(38)||'aViewPDF=true',
1,'Compliance Reports',0,0);

--changeSet 0utl_menu_item:7 stripComments:false
-- ADStatusAirframe - Compliance
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140006,NULL,'AD Status Airframe', '/maintenix/servlet/report/generate?aTemplate=Compliance.ADStatusAirframe'||chr(38)||'aViewPDF=true',
1,'Compliance Reports',0,0);

--changeSet 0utl_menu_item:8 stripComments:false
-- ForecastOfAircraft - Maintenance
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140007,NULL,'Forecast Of Aircraft Maintenance', '/maintenix/servlet/report/generate?aTemplate=Maintenance.ForecastOfAircraftMaintenance'||chr(38)||'aViewPDF=true',
1,'Maintenance Reports',0,0);

--changeSet 0utl_menu_item:9 stripComments:false
-- AircraftDeferredDefects
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140008,NULL,'Aircraft Deferred Defects', '/maintenix/servlet/report/generate?aTemplate=Maintenance.AircraftDeferredDefects'||chr(38)||'aViewPDF=true',
1,'Maintenance Reports',0,0);

--changeSet 0utl_menu_item:10 stripComments:false
-- EASAForm1
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140009,NULL,'EASA Form 1', '/maintenix/servlet/report/generate?aTemplate=Maintenance.EASAForm1'||chr(38)||'aViewPDF=true',
1,'Maintenance Reports',0,0);

--changeSet 0utl_menu_item:11 stripComments:false
-- FAAForm8130
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140010,NULL,'FAA Form 8130', '/maintenix/servlet/report/generate?aTemplate=Maintenance.FAAForm8130'||chr(38)||'aViewPDF=true',
1,'Maintenance Reports',0,0);

--changeSet 0utl_menu_item:12 stripComments:false
-- CertificateofReleasetoService
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140011,NULL,'Certificate of Release to Service', '/maintenix/servlet/report/generate?aTemplate=Maintenance.CertificateofReleasetoService'||chr(38)||'aViewPDF=true',
1,'Maintenance Reports',0,0);

--changeSet 0utl_menu_item:13 stripComments:false
-- Deferred DefectList Report
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140015,NULL,'Fleet Deferred Defects', '/maintenix/servlet/report/generate?aTemplate=Maintenance.DeferredDefectsList'||chr(38)||'aViewPDF=true',
1,'Maintenance Reports',0,0);

--changeSet 0utl_menu_item:14 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- 
-- Rotable Adjustment Archive and Owner Change 
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140018, NULL,'Archive and Owner Change', '/maintenix/servlet/report/generate?aTemplate=Finance.RotableAdjustmentArchiveOwnerChange'||chr(38)||'aViewPDF=true',
1,'Rotable Adjustment Reports',0,0);

--changeSet 0utl_menu_item:15 stripComments:false
-- Rotable Adjustment Create and Scrap Transactions
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140019, NULL,'Create and Scrap Transactions', '/maintenix/servlet/report/generate?aTemplate=Finance.RotableAdjustmentCreateScrapTrx'||chr(38)||'aViewPDF=true',
1,'Rotable Adjustment Reports',0,0);

--changeSet 0utl_menu_item:16 stripComments:false
-- Rotable Adjustment Exchange and Repairs Orders
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140020, NULL,'Exchange and Repairs Orders', '/maintenix/servlet/report/generate?aTemplate=Finance.RotableAdjustmentExchangeRepairsOrders'||chr(38)||'aViewPDF=true',
1,'Rotable Adjustment Reports',0,0);

--changeSet 0utl_menu_item:17 stripComments:false
-- Rotable Adjustment Price Adjustment
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140021, NULL,'Price Adjustment', '/maintenix/servlet/report/generate?aTemplate=Finance.RotableAdjustmentPriceAdjustment'||chr(38)||'aViewPDF=true',
1,'Rotable Adjustment Reports',0,0);

--changeSet 0utl_menu_item:18 stripComments:false
-- Rotable Adjustment Purchases and Returns
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (140022, NULL,'Purchases and Returns', '/maintenix/servlet/report/generate?aTemplate=Finance.RotableAdjustmentPurchasesReturns'||chr(38)||'aViewPDF=true',
1,'Rotable Adjustment Reports',0,0);