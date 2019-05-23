--liquibase formatted sql


--changeSet 0utl_todo_tab:1 stripComments:false
-- Users Alert List Tab
/******************************************
** 0-Level INSERT SCRIPT FOR UTL_TODO_TAB
*******************************************/
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (11000, 'idTabMyAlertList','web.todotab.MY_ALERTS','/web/alert/TabMyAlerts.jsp','web.todotab.USER_ALERT_LIST_TAB', 0,0);

--changeSet 0utl_todo_tab:2 stripComments:false
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (11021, 'idTabUnAssignedAlertList','web.todotab.UNASSIGNED_ALERTS','/web/alert/TabUnAssignedAlerts.jsp','web.todotab.USER_ALERT_LIST_TAB', 0,0);

--changeSet 0utl_todo_tab:3 stripComments:false
-- Fleet List Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL,  UTL_ID)
   VALUES (10000, 'idTabFleetList', 'web.todotab.FLEET_LIST', '/web/todolist/FleetListTab.jsp', 'web.todotab.FLEET_LIST_TAB', 0,0);

--changeSet 0utl_todo_tab:4 stripComments:false
-- Assigned Work List Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL,  UTL_ID)
   VALUES (10001, 'idTabAssignedWorkList', 'web.todotab.ASSIGNED_WORK_LIST', '/web/todolist/AssignedWorkListTab.jsp', 'web.todotab.ASSIGNED_WORK_LIST_TAB', 0,0);

--changeSet 0utl_todo_tab:6 stripComments:false
-- My Tasks Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL,  UTL_ID)
   VALUES (10003, 'idTabMyTasks', 'web.todotab.MY_TASKS', '/web/todolist/MyTasksTab.jsp', 'web.todotab.MY_TASKS_TAB', 0,0);

--changeSet 0utl_todo_tab:7 stripComments:false
-- Assigned To Your Crew Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL,  UTL_ID)
   VALUES (10004, 'idTabAssignedToYourCrew', 'web.todotab.ASSIGNED_TO_YOUR_CREW', '/web/todolist/AssignedToYourCrewTab.jsp', 'web.todotab.ASSIGNED_TO_YOUR_CREW_TAB', 0,0);

--changeSet 0utl_todo_tab:8 stripComments:false
-- Unserviceable Staging To Do Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL,  UTL_ID)
   VALUES (10005, 'idTabUnserviceableStaging', 'web.todotab.UNSERVICEABLE_STAGING', '/web/todolist/UnserviceableStagingTab.jsp', 'web.todotab.UNSERVICEABLE_STAGING_TAB', 0,0);

--changeSet 0utl_todo_tab:9 stripComments:false
-- Storage Maintenance To Do Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL,  UTL_ID)
   VALUES (10006, 'idTabStorageMaintenance', 'web.todotab.STORAGE_MAINTENANCE', '/web/todolist/StorageMaintenanceTab.jsp', 'web.todotab.STORAGE_MAINTENANCE_TAB', 0,0);

--changeSet 0utl_todo_tab:10 stripComments:false
-- Turn Ins Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL,  UTL_ID)
   VALUES (10009, 'idTabTurnIn', 'web.todotab.TURN_IN', '/web/todolist/TurnInTab.jsp', 'web.todotab.TURN_IN_TAB', 0,0);

--changeSet 0utl_todo_tab:11 stripComments:false
-- Inspection Required Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL,  UTL_ID)
   VALUES (10010, 'idTabInspectionRequired', 'web.todotab.INSPECTION_REQUIRED', '/web/todolist/InspectionRequiredTab.jsp', 'web.todotab.INSPECTION_REQUIRED_TAB', 0,0);

--changeSet 0utl_todo_tab:12 stripComments:false
-- Quarantine Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL,  UTL_ID)
   VALUES (10012, 'idTabQuarantine', 'web.todotab.QUARANTINE', '/web/todolist/QuarantineTab.jsp', 'web.todotab.QUARANTINE_TAB', 0,0);

--changeSet 0utl_todo_tab:13 stripComments:false
-- System Engineering Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10013, 'idTabSystemEngineering', 'web.todotab.SYSTEM_ENGINEERING', '/web/todolist/SystemEngineeringTab.jsp', 'web.todotab.SYSTEM_ENGINEERING_TAB', 0,0);

--changeSet 0utl_todo_tab:14 stripComments:false
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10057, 'idTabFleetTaskLabourSummary', 'web.todotab.FLEET_TASK_LABOUR_SUMMARY', '/web/todolist/FleetTaskLabourSummaryTab.jsp', 'web.todotab.FLEET_TASK_LABOUR_SUMMARY_TAB', 0,0);

--changeSet 0utl_todo_tab:15 stripComments:false
-- To Be Shelved Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10016, 'idTabToBeShelved', 'web.todotab.TO_BE_SHELVED', '/web/todolist/ToBeShelvedTab.jsp', 'web.todotab.TO_BE_SHELVED_TAB', 0,0);

--changeSet 0utl_todo_tab:16 stripComments:false
-- Pending Expiry Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10017, 'idTabPendingExpiry', 'web.todotab.PENDING_EXPIRY', '/web/todolist/PendingExpiryTab.jsp', 'web.todotab.PENDING_EXPIRY_TAB', 0,0);

--changeSet 0utl_todo_tab:17 stripComments:false
-- Inbound Shipments Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10018, 'idTabInboundShipments', 'web.todotab.INBOUND_SHIPMENTS', '/web/todolist/InboundShipmentsTab.jsp', 'web.todotab.INBOUND_SHIPMENTS_TAB', 0,0);

--changeSet 0utl_todo_tab:18 stripComments:false
-- Outbound Shipments Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10019, 'idTabOutboundShipments', 'web.todotab.OUTBOUND_SHIPMENTS', '/web/todolist/OutboundShipmentsTab.jsp', 'web.todotab.OUTBOUND_SHIPMENTS_TAB', 0,0);

--changeSet 0utl_todo_tab:19 stripComments:false
-- Stock Lows Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10020, 'idTabStockLows', 'web.todotab.STOCK_LOWS', '/web/todolist/StockLowsTab.jsp', 'web.todotab.STOCK_LOWS_TAB', 0,0);

--changeSet 0utl_todo_tab:20 stripComments:false
-- Condemned Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10021, 'idTabCondemned', 'web.todotab.CONDEMNED', '/web/todolist/CondemnedTab.jsp', 'web.todotab.CONDEMNED_TAB', 0,0);

--changeSet 0utl_todo_tab:21 stripComments:false
-- Purchase Request Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10022, 'idTabPurchaseRequests', 'web.todotab.PURCHASE_REQUESTS', '/web/todolist/PurchaseRequestsTab.jsp', 'web.todotab.PURCHASE_REQUESTS_TAB', 0,0);

--changeSet 0utl_todo_tab:22 stripComments:false
-- Open Purchase Orders Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10023, 'idTabOpenPurchaseOrders', 'web.todotab.OPEN_POS', '/web/todolist/OpenPOTab.jsp', 'web.todotab.OPEN_PURCHASE_ORDERS_TAB', 0,0);

--changeSet 0utl_todo_tab:23 stripComments:false
-- Issued Purchase Orders  Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10024, 'idTabIssuedPurchaseOrders', 'web.todotab.ISSUED_POS', '/web/todolist/IssuedPOTab.jsp', 'web.todotab.ISSUED_PURCHASE_ORDERS_TAB', 0,0);

--changeSet 0utl_todo_tab:24 stripComments:false
-- Authorization Required POs Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10025, 'idTabPOAuthorizationRequired', 'web.todotab.AUTHORIZATION_REQUIRED_POS', '/web/todolist/AuthorizePOTab.jsp', 'web.todotab.AUTHORIZATION_REQUIRED_TAB', 0,0);

--changeSet 0utl_todo_tab:25 stripComments:false
-- Awaiting Issue POs Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10026, 'idTabPOAwaitingIssue', 'web.todotab.AWAITING_ISSUE_POS', '/web/todolist/AwaitingIssuePOTab.jsp', 'web.todotab.AWAITING_ISSUE_TAB', 0,0);

--changeSet 0utl_todo_tab:26 stripComments:false
-- My Part Requests Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10027, 'idTabMyPartRequests', 'web.todotab.MY_PART_REQUESTS', '/web/todolist/MyPartRequestsTab.jsp', 'web.todotab.MY_PART_REQUESTS_TAB', 0,0);

--changeSet 0utl_todo_tab:27 stripComments:false
-- Issue Inventory Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10029, 'idTabInventoryToIssue', 'web.todotab.INVENTORY_TO_ISSUE', '/web/todolist/InventoryToIssueTab.jsp', 'web.todotab.INVENTORY_TO_ISSUE_TAB', 0,0);

--changeSet 0utl_todo_tab:28 stripComments:false
-- Open Part Requests Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10030, 'idTabOpenPartRequests', 'web.todotab.OPEN_PART_REQUESTS', '/web/todolist/OpenPartRequestsTab.jsp', 'web.todotab.OPEN_PART_REQUESTS_TAB', 0,0);

--changeSet 0utl_todo_tab:29 stripComments:false
-- Issue Inventory Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10031, 'idTabIssue', 'web.todotab.ISSUE', '/web/todolist/IssueTab.jsp', 'web.todotab.ISSUE_INVENTORY_TAB', 0,0);

--changeSet 0utl_todo_tab:30 stripComments:false
-- Out For Repair Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10033, 'idTabOutForRepair', 'web.todotab.OUT_FOR_REPAIR', '/web/todolist/OutForRepairTab.jsp', 'web.todotab.OUT_FOR_REPAIR_TAB', 0,0);

--changeSet 0utl_todo_tab:31 stripComments:false
-- Fault Evaluation Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10034, 'idTabFaultEvaluation', 'web.todotab.FAULT_EVALUATION', '/web/todolist/FaultEvaluationTab.jsp', 'web.todotab.FAULT_EVALUATION_TAB', 0,0);

--changeSet 0utl_todo_tab:32 stripComments:false
-- Assembly List Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10035, 'idTabAssemblyList', 'web.todotab.ASSEMBLY_LIST', '/web/todolist/AssemblyListTab.jsp', 'web.todotab.ASSEMBLY_LIST_TAB', 0,0);

--changeSet 0utl_todo_tab:33 stripComments:false
-- Checked Out Tools Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10036, 'idTabCheckedOutTools', 'web.todotab.CHECKED_OUT_TOOLS', '/web/todolist/CheckedOutToolsTab.jsp', 'web.todotab.CHECKED_OUT_TOOLS_TAB', 0,0);

--changeSet 0utl_todo_tab:34 stripComments:false
-- Open MEL Part Requests Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10037, 'idTabOpenMELPartRequests', 'web.todotab.OPEN_MEL_PART_REQUESTS', '/web/todolist/OpenMELPartRequestsTab.jsp', 'web.todotab.OPEN_MEL_PART_REQUESTS_TAB', 0,0);

--changeSet 0utl_todo_tab:35 stripComments:false
-- Cycle Count Discrepancies Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10038, 'idTabCycleCountDiscrepancies', 'web.todotab.CYCLE_COUNT_DISCREPANCIES', '/web/todolist/CycleCountDiscrepanciesTab.jsp', 'web.todotab.CYCLE_COUNT_DISCREPANCIES_TAB', 0,0);

--changeSet 0utl_todo_tab:36 stripComments:false
-- My Open PO Invoices Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10039, 'idTabMyOpenPOInvoices', 'web.todotab.MY_OPEN_PO_INVOICES', '/web/todolist/MyOpenPOInvoicesTab.jsp', 'web.todotab.MY_OPEN_PO_INVOICES_TAB', 0,0);

--changeSet 0utl_todo_tab:37 stripComments:false
-- My Open Orders Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10040, 'idTabMyOpenOrders', 'web.todotab.MY_OPEN_ORDERS', '/web/todolist/MyOpenOrdersTab.jsp', 'web.todotab.MY_OPEN_ORDERS_TAB', 0,0);

--changeSet 0utl_todo_tab:38 stripComments:false
-- Stock Requests Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10041, 'idTabStockRequests', 'web.todotab.STOCK_REQUESTS', '/web/todolist/StockRequestsTab.jsp', 'web.todotab.STOCK_REQUESTS_TAB', 0,0);

--changeSet 0utl_todo_tab:39 stripComments:false
-- Cycle Count Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10042, 'idTabCycleCount', 'web.todotab.CYCLE_COUNT', '/web/todolist/CycleCountTab.jsp', 'web.todotab.CYCLE_COUNT_TAB', 0,0);

--changeSet 0utl_todo_tab:40 stripComments:false
-- Obsolete Inventory Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10043, 'idTabObsoleteInventory', 'web.todotab.OBSOLETE_INVENTORY', '/web/todolist/ObsoleteInventoryTab.jsp', 'web.todotab.OBSOLETE_INVENTORY_TAB', 0,0);

--changeSet 0utl_todo_tab:41 stripComments:false
-- Slow Moving Stock Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10044, 'idTabSlowMovingStock', 'web.todotab.SLOW_MOVING_STOCK', '/web/todolist/SlowMovingStockTab.jsp', 'web.todotab.SLOW_MOVING_STOCK_TAB', 0,0);

--changeSet 0utl_todo_tab:42 stripComments:false
-- Surplus Stock Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10045, 'idTabSurplusStock', 'web.todotab.SURPLUS_STOCK', '/web/todolist/SurplusStockTab.jsp', 'web.todotab.SURPLUS_STOCK_TAB', 0,0);

--changeSet 0utl_todo_tab:43 stripComments:false
-- Shift Schedule Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10048, 'idTabShiftSchedule', 'web.todotab.SHIFT_SCHEDULE', '/web/todolist/ShiftScheduleTab.jsp', 'web.todotab.SHIFT_SCHEDULE_TAB', 0,0);

--changeSet 0utl_todo_tab:44 stripComments:false
-- Capacity Summary Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10049, 'idTabCapacitySummary', 'web.todotab.CAPACITY_SUMMARY', '/web/todolist/CapacitySummaryTab.jsp', 'web.todotab.CAPACITY_SUMMARY_TAB', 0,0);

--changeSet 0utl_todo_tab:45 stripComments:false
-- Fleet Due List Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10050, 'idTabFleetDueList', 'web.todotab.FLEET_DUE_LIST', '/web/todolist/FleetDueListTab.jsp', 'web.todotab.FLEET_DUE_LIST', 0,0);

--changeSet 0utl_todo_tab:46 stripComments:false
-- Forecast Model List Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10051, 'idTabFcModelList', 'web.todotab.FORECAST_MODELS', '/web/todolist/ForecastModelListTab.jsp', 'web.todotab.FORECAST_MODEL_LIST_TAB', 0,0);

--changeSet 0utl_todo_tab:47 stripComments:false
-- My RFQ Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10052, 'idTabMyRFQ', 'web.todotab.MY_RFQS', '/web/todolist/MyRFQTab.jsp', 'web.todotab.MY_RFQS_TAB', 0,0);

--changeSet 0utl_todo_tab:48 stripComments:false
-- Line Planning Automation Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10053, 'idTabLinePlanningAutomation', 'web.todotab.LINE_PLANNING_AUTOMATION', '/web/todolist/LinePlanningAutomationTab.jsp', 'web.todotab.LINE_PLANNING_AUTOMATION_TAB', 0,0);

--changeSet 0utl_todo_tab:49 stripComments:false
-- Consignment Stock Request Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10055, 'idTabConsignStockRequests', 'web.todotab.CONSIGN_STOCK_REQUESTS', '/web/todolist/ConsignStockRequestsTab.jsp', 'web.todotab.CONSIGN_STOCK_REQUESTS_TAB', 0,0);

--changeSet 0utl_todo_tab:50 stripComments:false
-- To Be Returned Consignment Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10056, 'idTabToBeReturnedConsignment', 'web.todotab.TO_BE_RETURNED_CONSIGNMENT', '/web/todolist/ToBeReturnedConsignmentTab.jsp', 'web.todotab.TO_BE_RETURNED_CONSIGNMENT_TAB', 0,0);

--changeSet 0utl_todo_tab:51 stripComments:false
-- Status Boards Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10060, 'idTabStatusBoards', 'web.todotab.STATUS_BOARDS_TAB', '/web/todolist/StatusBoardsTab.jsp', 'web.todotab.STATUS_BOARDS_TAB', 0,0);

--changeSet 0utl_todo_tab:52 stripComments:false
-- Predraw Monitoring Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10067, 'idTabPredrawMonitoring', 'web.todotab.PREDRAW_MONITORING', '/web/todolist/PredrawMonitoringTab.jsp', 'web.todotab.PREDRAW_MONITORING_TAB', 0,0);

--changeSet 0utl_todo_tab:53 stripComments:false
-- Tags Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
   VALUES (10061, 'idTabTags', 'web.todotab.TAGS', '/web/todolist/TagsTab.jsp', 'web.todotab.TAGS_TAB', 0,0);

--changeSet 0utl_todo_tab:54 stripComments:false
-- License Pending Expiry Page
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, REFRESH_INTERVAL, UTL_ID)
VALUES (10062, 'idTabLicensePendingExpiry', 'web.todotab.LICENSE_PENDING_EXPIRY', '/web/todolist/LicensePendingExpiryTab.jsp', 'web.todotab.LICENSE_PENDING_EXPIRY', 0,0);

--changeSet 0utl_todo_tab:55 stripComments:false
-- Print License Card
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, REFRESH_INTERVAL, UTL_ID)
VALUES (10063, 'idTabPrintLicenseCard', 'web.todotab.Print_License_Card', '/web/licensedefn/PrintLicenseCards.jsp', 'web.todotab.Print_License_Card', 0, 0);

--changeSet 0utl_todo_tab:56 stripComments:false
-- Fault Threshold Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, REFRESH_INTERVAL, UTL_ID)
VALUES (10064, 'idTabFaultThreshold', 'web.todotab.FAULT_THRESHOLD_LIST', '/web/todolist/FaultThresholdTab.jsp', 'web.todotab.FAULT_THRESHOLD_TAB', 0, 0);

--changeSet 0utl_todo_tab:57 stripComments:false
-- Deferral Reference Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, REFRESH_INTERVAL, UTL_ID)
VALUES (10065, 'idTabDeferralReference', 'web.todotab.DEFERRAL_REFERENCE_LIST', '/web/todolist/DeferralReferencesTab.jsp', 'web.todotab.DEFERRAL_REFERENCE_LIST_TAB', 0, 0);

--changeSet 0utl_todo_tab:58 stripComments:false
-- Borrow Returns Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
VALUES (11003, 'idTabBorrowReturns', 'web.todotab.BORROW_RETURNS', '/web/todolist/BorrowReturnsTab.jsp','web.todotab.BORROW_RETURNS_TAB', 0,0);

--changeSet 0utl_todo_tab:59 stripComments:false
-- Exchange Returns Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
VALUES (11004, 'idTabExchangeReturns', 'web.todotab.EXCHANGE_RETURNS', '/web/todolist/ExchangeReturnsTab.jsp', 'web.todotab.EXCHANGE_RETURNS_TAB', 0,0);

--changeSet 0utl_todo_tab:60 stripComments:false
-- Item Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
VALUES (11005, 'idTabIETM', 'web.todotab.IETM', '/web/todolist/AssemblyIetmTab.jsp', 'web.todotab.IETM_LIST_TAB', 0,0);

--changeSet 0utl_todo_tab:61 stripComments:false
-- Work Packages Tab (Warranty Agent)
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
VALUES (11006, 'idTabWorkPackages', 'web.todotab.WORK_PACKAGES', '/web/todolist/WorkPackagesTab.jsp', 'web.todotab.WORK_PACKAGES_TAB', 0,0);

--changeSet 0utl_todo_tab:62 stripComments:false
-- Non-Repairable Returns Tab (Warranty Agent)
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
VALUES (11007, 'idTabNonRepairableReturns', 'web.todotab.NONREPAIRABLE_RETURNS', '/web/todolist/NonRepairableReturnsTab.jsp', 'web.todotab.NONREPAIRABLE_RETURNS_TAB', 0,0);

--changeSet 0utl_todo_tab:63 stripComments:false
-- Open Claims Tab (Warranty Agent)
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
VALUES (11008, 'idTabOpenClaims', 'web.todotab.OPEN_CLAIMS', '/web/todolist/OpenClaimsTab.jsp', 'web.todotab.OPEN_CLAIMS_TAB', 0,0);

--changeSet 0utl_todo_tab:64 stripComments:false
-- Submitted Claims Tab (Warranty Agent)
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
VALUES (11009, 'idTabSubmittedClaims', 'web.todotab.SUBMITTED_CLAIMS', '/web/todolist/SubmittedClaimsTab.jsp', 'web.todotab.SUBMITTED_CLAIMS_TAB', 0,0);

--changeSet 0utl_todo_tab:65 stripComments:false
-- Approval Workflows
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
VALUES (11010, 'idTabApprovalWorkflows', 'web.todotab.APPROVAL_WORKFLOWS', '/web/todolist/ApprovalWorkflowsTab.jsp', 'web.todotab.APPROVAL_WORKFLOWS_TAB', 0,0);

--changeSet 0utl_todo_tab:66 stripComments:false
-- Approval Workflows
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
VALUES (11011, 'idTabApprovalLevels', 'web.todotab.APPROVAL_LEVELS', '/web/todolist/ApprovalLevelsTab.jsp', 'web.todotab.APPROVAL_LEVELS_TAB', 0,0);

--changeSet 0utl_todo_tab:67 stripComments:false
-- My Task Definitions Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
VALUES (11012, 'idTabMyTaskDefinitions', 'web.todotab.MY_TASK_DEFINITIONS', '/web/todolist/MyTaskDefinitionsTab.jsp', 'web.todotab.MY_TASK_DEFINITIONS_TAB', 0,0);

--changeSet 0utl_todo_tab:68 stripComments:false
-- Task Definitions To Approve Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
VALUES (11013, 'idTabTaskDefinitionsToApprove', 'web.todotab.TASK_DEFINITIONS_TO_APPROVE', '/web/todolist/TaskDefinitionsToApproveTab.jsp', 'web.todotab.TASK_DEFINITIONS_TO_APPROVE_TAB', 0,0);

--changeSet 0utl_todo_tab:69 stripComments:false
-- Stock Transfer Tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
VALUES (11014, 'idTabStockTransfer', 'web.todotab.STOCK_TRANSFER', '/web/todolist/StockTransferTab.jsp', 'web.todotab.STOCK_TRANSFER_TAB', 0,0);

--changeSet 0utl_todo_tab:70 stripComments:false
-- Extraction Rule List
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH,TODO_TAB_LDESC, REFRESH_INTERVAL, UTL_ID)
VALUES (11015, 'idTabExtractionRuleList', 'web.todotab.EXTRACTION_RULES', '/web/todolist/ExtractionRuleList.jsp', 'web.todotab.EXTRACTION_RULES', 0 , 0);

--changeSet 0utl_todo_tab:71 stripComments:false
-- Loose Inventory Due List
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH,TODO_TAB_LDESC, REFRESH_INTERVAL, UTL_ID)
VALUES (11016, 'idTabLooseInventoryDue', 'web.todotab.LOOSE_INV_DUE', '/web/todolist/LooseInventoryDueTab.jsp', 'web.todotab.LOOSE_INV_DUE_TAB', 0 , 0);

--changeSet 0utl_todo_tab:72 stripComments:false
-- Assigned Quarantine Corrective Actions
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC,REFRESH_INTERVAL, UTL_ID)
VALUES (11017, 'idTabQuarantineCorrectiveActions', 'web.todotab.ASSIGNED_QUARANTINE_ACTIONS', '/web/todolist/QuarantineCorrectiveActionsTab.jsp', 'web.todotab.ASSIGNED_QUARANTINE_ACTIONS_TAB', 0, 0);

--changeSet 0utl_todo_tab:73 stripComments:false
-- Incomplete Kits
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, UTL_ID)
VALUES (11018, 'idTabIncompleteKits', 'web.todotab.INCOMPLETE_KITS', '/web/todolist/IncompleteKitsTab.jsp', 'web.todotab.INCOMPLETE_KITS_TAB', 0);

--changeSet 0utl_todo_tab:74 stripComments:false
-- Over-Complete Kits
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, UTL_ID)
VALUES (11019, 'idTabOverCompleteKits', 'web.todotab.OVER_COMPLETE_KITS', '/web/todolist/OverCompleteKitsTab.jsp', 'web.todotab.OVER_COMPLETE_KITS_TAB', 0);

--changeSet 0utl_todo_tab:75 stripComments:false
-- High Oil Consumption ToDo List
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, UTL_ID)
VALUES (11020, 'idTabHighOilConsumption', 'web.todotab.HIGH_OIL_CONSUMPTION', '/web/todolist/HighOilConsumptionTab.jsp', 'web.todotab.HIGH_OIL_CONSUMPTION_TAB', 0);

--changeSet 0utl_todo_tab:76 stripComments:false
-- Planning Types ToDo List
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, REFRESH_INTERVAL, UTL_ID)
VALUES (11022, 'idTabPlanningTypes', 'web.todotab.PLANNING_TYPES', '/web/todolist/PlanningTypesTab.jsp', 'web.todotab.PLANNING_TYPES_TAB', 0, 0);

--changeSet 0utl_todo_tab:77 stripComments:false
-- Shift Setup ToDo List
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, UTL_ID)
VALUES (11023, 'idTabShiftSetup', 'web.todotab.SHIFT_SETUP', '/web/todolist/ShiftSetupTab.jsp', 'web.todotab.SHIFT_SETUP_TAB', 0);

--changeSet 0utl_todo_tab:78 stripComments:false
-- User Shift Pattern Setup ToDo List
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, UTL_ID)
VALUES (11024, 'idTabUserShiftPatternSetup', 'web.todotab.USER_SHIFT_PATTERN_SETUP', '/web/todolist/UserShiftPatternSetupTab.jsp', 'web.todotab.USER_SHIFT_PATTERN_SETUP_TAB', 0);

--changeSet 0utl_todo_tab:79 stripComments:false
-- Capacity Pattern Setup
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, UTL_ID)
VALUES (11025, 'idTabCapacityPatternSetup', 'web.todotab.CAPACITY_PATTERN_SETUP', '/web/todolist/CapacityPatternSetup.jsp', 'web.todotab.CAPACITY_PATTERN_SETUP_TAB', 0);

--changeSet 0utl_todo_tab:80 stripComments:false
-- Stock Distribution Requests
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, UTL_ID)
VALUES (11026, 'idTabStockDistributionRequests', 'web.todotab.STOCK_DISTRIBUTION_REQUESTS', '/web/todolist/StockDistributionRequestsTab.jsp', 'web.todotab.STOCK_DISTRIBUTION_REQUESTS_TAB', 0);

--changeSet 0utl_todo_tab:81 stripComments:false
-- Task Supervision
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, UTL_ID)
VALUES (11027, 'idTabTaskSupervision', 'web.todotab.TASK_SUPERVISION', '/web/todolist/TaskSupervisionTab.jsp', 'web.todotab.TASK_SUPERVISION_TAB', 0);

--changeSet 0utl_todo_tab:82 stripComments:false
-- Bulk Export and Import tab
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, UTL_ID)
VALUES (11028, 'idTabBulkExportAndImport', 'web.todotab.FILE_STATUS', '/web/todolist/BulkExportAndImportTab.jsp', 'web.todotab.FILE_STATUS_TAB', 0);