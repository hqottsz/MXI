--liquibase formatted sql


--changeSet OPER-529:1 stripComments:false
-- JASPER REPORTS CONFIG PARAMETERS
-- Rotable Adjustment Archive and Owner Change 
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Finance.RotableAdjustmentArchiveOwnerChange', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Finance/RotableAdjustmentArchiveOwnerChange', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_REPORT_TYPE WHERE REPORT_NAME = 'Finance.RotableAdjustmentArchiveOwnerChange'
);

--changeSet OPER-529:2 stripComments:false
-- Rotable Adjustment Create and Scrap Transactions
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Finance.RotableAdjustmentCreateScrapTrx', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Finance/RotableAdjustmentCreateScrapTrx', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_REPORT_TYPE WHERE REPORT_NAME = 'Finance.RotableAdjustmentCreateScrapTrx'
);

--changeSet OPER-529:3 stripComments:false
-- Rotable Adjustment Exchange and Repairs Orders
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Finance.RotableAdjustmentExchangeRepairsOrders', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Finance/RotableAdjustmentExchangeRepairsOrders', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_REPORT_TYPE WHERE REPORT_NAME = 'Finance.RotableAdjustmentExchangeRepairsOrders'
);

--changeSet OPER-529:4 stripComments:false
-- Rotable Adjustment Price Adjustment
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Finance.RotableAdjustmentPriceAdjustment', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Finance/RotableAdjustmentPriceAdjustment', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_REPORT_TYPE WHERE REPORT_NAME = 'Finance.RotableAdjustmentPriceAdjustment'
);

--changeSet OPER-529:5 stripComments:false
-- Rotable Adjustment Purchases and Returns
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Finance.RotableAdjustmentPurchasesReturns', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Finance/RotableAdjustmentPurchasesReturns', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_REPORT_TYPE WHERE REPORT_NAME = 'Finance.RotableAdjustmentPurchasesReturns'
);

--changeSet OPER-529:6 stripComments:false
-- Rotable Adjustment Turnins and Issues
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'Finance.RotableAdjustmentTurninsIssues', 'JASPER_SSO', '/organizations/Maintenix/Reports/Operator/Finance/RotableAdjustmentTurninsIssues', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (
   SELECT 1 FROM UTL_REPORT_TYPE WHERE REPORT_NAME = 'Finance.RotableAdjustmentTurninsIssues'
);

--changeSet OPER-529:7 stripComments:false
-- JASPER REPORTS MENU ITEMS
-- Rotable Adjustment Archive and Owner Change 
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 140018,NULL,'Archive and Owner Change', '/maintenix/servlet/report/generate?aTemplate=Finance.RotableAdjustmentArchiveOwnerChange'||chr(38)||'aViewPDF=true', 1,'Rotable Adjustment Reports',0,0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140018);

--changeSet OPER-529:8 stripComments:false
-- Rotable Adjustment Create and Scrap Transactions 
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 140019,NULL,'Create and Scrap Transactions', '/maintenix/servlet/report/generate?aTemplate=Finance.RotableAdjustmentCreateScrapTrx'||chr(38)||'aViewPDF=true', 1,'Rotable Adjustment Reports',0,0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140019);

--changeSet OPER-529:9 stripComments:false
-- Rotable Adjustment Exchange and Repairs Orders
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 140020,NULL,'Exchange and Repairs Orders', '/maintenix/servlet/report/generate?aTemplate=Finance.RotableAdjustmentExchangeRepairsOrders'||chr(38)||'aViewPDF=true', 1,'Rotable Adjustment Reports',0,0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140020);

--changeSet OPER-529:10 stripComments:false
-- Rotable Adjustment Price Adjustment
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 140021,NULL,'Price Adjustment', '/maintenix/servlet/report/generate?aTemplate=Finance.RotableAdjustmentPriceAdjustment'||chr(38)||'aViewPDF=true', 1,'Rotable Adjustment Reports',0,0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140021);

--changeSet OPER-529:11 stripComments:false
-- Rotable Adjustment Purchases and Returns
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 140022,NULL,'Purchases and Returns', '/maintenix/servlet/report/generate?aTemplate=Finance.RotableAdjustmentPurchasesReturns'||chr(38)||'aViewPDF=true', 1,'Rotable Adjustment Reports',0,0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140022);

--changeSet OPER-529:12 stripComments:false
-- Rotable Adjustment Turnins and Issues
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
SELECT 140023,NULL,'Turnins and Issues', '/maintenix/servlet/report/generate?aTemplate=Finance.RotableAdjustmentTurninsIssues'||chr(38)||'aViewPDF=true', 1,'Rotable Adjustment Reports',0,0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_MENU_ITEM WHERE MENU_ID = 140023);