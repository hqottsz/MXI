--liquibase formatted sql
SET DEFINE OFF

--changeSet 0utl_menu_item:1 stripComments:false
-- Tell Oracle not to prompt for parameter when it encounters ampersands in the string values
-- Five Day Planner Page
/******************************************
** 0-Level INSERT SCRIPT FOR UTL_MENU_ITEM
*******************************************/
/***********************
** Miscellaneous
************************/
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10013, NULL,'web.menuitem.FIVE_DAY_PLANNER', '/maintenix/web/todolist/AircraftPlanner.jsp',  0,0);

--changeSet 0utl_menu_item:2 stripComments:false
-- Alert List
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10101, NULL, 'web.menuitem.ALERTS', '/maintenix/common/alert/UserAlerts.jsp', 0,0);

--changeSet 0utl_menu_item:3 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10138, NULL, 'web.menuitem.ALERT_SETUP', '/maintenix/web/alert/AlertSetup.jsp', 0,0);

--changeSet 0utl_menu_item:5 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10903, NULL,'web.menuitem.MY_USER_DETAILS', '/maintenix/web/user/UserDetails.jsp',  0, 0);

--changeSet 0utl_menu_item:6 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10901, NULL,'web.menuitem.IFS_WEBSITE', 'http://www.ifsworld.com',  1, 0);

--changeSet 0utl_menu_item:7 stripComments:false
--Administrative Reports
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (11001, NULL,'web.menuitem.FUNCTION_ACCESS', '/maintenix/servlet/report/generate?aTemplate=utility.FunctionAccess&aViewPDF=true',  0, 0);

--changeSet 0utl_menu_item:8 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (11002, NULL, 'web.menuitem.FUNCTION_ACCESS_ROLE', '/maintenix/servlet/report/generate?aTemplate=utility.FunctionAccessRole&aViewPDF=true',  0, 0);

--changeSet 0utl_menu_item:9 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (11003, NULL,'web.menuitem.SYSTEM_INFORMATION', '/maintenix/servlet/report/generate?aTemplate=utility.SystemInformation&aViewPDF=true', 0, 0);

--changeSet 0utl_menu_item:10 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (11004, NULL,'web.menuitem.UI_AND_LOGIC_SETTINGS', '/maintenix/servlet/report/generate?aTemplate=utility.UIandLogicSettings&aViewPDF=true',  0, 0);

--changeSet 0utl_menu_item:11 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (11005, NULL,'web.menuitem.USER_ROLE', '/maintenix/servlet/report/generate?aTemplate=utility.UserRole&aViewPDF=true', 0, 0);

--changeSet 0utl_menu_item:12 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (11006, NULL,'web.menuitem.DEPARTMENT_MEMBER_LIST', '/maintenix/servlet/report/generate?aTemplate=utility.DepartmentMemberList&aViewPDF=true',0, 0);

--changeSet 0utl_menu_item:13 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (11007, NULL,'web.menuitem.USER_DEPARTMENT_LIST', '/maintenix/servlet/report/generate?aTemplate=utility.UserDepartmentList&aViewPDF=true',0, 0);

--changeSet 0utl_menu_item:14 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (11008, NULL,'web.menuitem.USER_ACTION', '/maintenix/servlet/report/generate?aTemplate=utility.UserAction&aViewPDF=true', 0, 0);

--changeSet 0utl_menu_item:15 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (11009, NULL,'web.menuitem.FUNCTION_ACTION_ACCESS', '/maintenix/servlet/report/generate?aTemplate=utility.FunctionActionAccess&aViewPDF=true',  0, 0);

--changeSet 0utl_menu_item:16 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (11010, NULL, 'web.menuitem.FUNCTION_ACTION_ACCESS_ROLE', '/maintenix/servlet/report/generate?aTemplate=utility.FunctionActionAccessRole&aViewPDF=true',  0, 0);

--changeSet 0utl_menu_item:17 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (11011, null, 'web.menuitem.BUSINESS_INTELLIGENCE', '/maintenix/servlet/BusinessIntelligenceRedirect', 1, 0);

--changeSet 0utl_menu_item:18 stripComments:false
--Integration Pages
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (13000, NULL,'web.menuitem.SEND_INTEGRATION_MESSAGE', '/maintenix/common/integration/SendMessage.jsp', 0, 0);

--changeSet 0utl_menu_item:19 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (13001, NULL,'web.menuitem.INTEGRATION_MESSAGE_SEARCH', '/maintenix/common/integration/LogSearch.jsp',  0,0);

--changeSet 0utl_menu_item:20 stripComments:false
-- Security Setup
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (13002, NULL,'web.menuitem.ROLESECURITY', '/maintenix/web/role/RoleSecurity.jsp', 0, 0);

--changeSet 0utl_menu_item:21 stripComments:false
--Permission Matrix
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (13003,NULL,'web.menuitem.PERMSECURITY', '/maintenix/web/permatrix/PermMatrixSecurity.jsp',0,'permission matrix security editor',0,0);

--changeSet 0utl_menu_item:22 stripComments:false
--Report Setup
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (13004, NULL,'web.menuitem.REPORT_CONFIGURATION', '/maintenix/web/report/ReportConfig.jsp', 0, 0);

--changeSet 0utl_menu_item:23 stripComments:false
-- Work Item Administration Console
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
VALUES (20025, NULL,'web.menuitem.WORK_ITEM_ADMIN_CONSOLE', '/maintenix/common/work/WorkItemAdminConsole.jsp',  0,0);

--changeSet 0utl_menu_item:24 stripComments:false
-- Searches
/***********************
** Searches
************************/
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10102, NULL, 'web.menuitem.TASK_SEARCH', '/maintenix/web/task/TaskSearch.jsp',  0,0);

--changeSet 0utl_menu_item:25 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10103, NULL, 'web.menuitem.INVENTORY_SEARCH', '/maintenix/web/inventory/InventorySearch.jsp',  0,0);

--changeSet 0utl_menu_item:26 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10104, NULL,'web.menuitem.PART_SEARCH', '/maintenix/web/part/PartSearch.jsp',  0,0);

--changeSet 0utl_menu_item:27 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10105, NULL,'web.menuitem.OWNER_SEARCH', '/maintenix/web/owner/OwnerSearch.jsp',  0,0);

--changeSet 0utl_menu_item:28 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10106, NULL,'web.menuitem.VENDOR_SEARCH', '/maintenix/web/vendor/VendorSearch.jsp',  0,0);

--changeSet 0utl_menu_item:29 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10107, NULL,'web.menuitem.MANUFACTURER_SEARCH', '/maintenix/web/manufacturer/ManufacturerSearch.jsp', 0,0);

--changeSet 0utl_menu_item:30 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10108, NULL,'web.menuitem.SHIPMENT_SEARCH', '/maintenix/web/shipment/ShipmentSearch.jsp', 0,0);

--changeSet 0utl_menu_item:31 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10109, NULL,'web.menuitem.FLIGHT_SEARCH', '/maintenix/web/flight/FlightSearch.jsp', 0,0);

--changeSet 0utl_menu_item:32 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10110, NULL,'web.menuitem.FAULT_SEARCH', '/maintenix/web/fault/FaultSearch.jsp',  0,0);

--changeSet 0utl_menu_item:33 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10111, NULL,'web.menuitem.STOCK_SEARCH', '/maintenix/web/stock/StockSearch.jsp',  0,0);

--changeSet 0utl_menu_item:34 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10112, NULL,'web.menuitem.LOCATION_SEARCH', '/maintenix/web/location/LocationSearch.jsp',  0,0);

--changeSet 0utl_menu_item:35 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10113, NULL,'web.menuitem.ROLE_SEARCH', '/maintenix/web/role/RoleSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:36 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10114, NULL,'web.menuitem.USER_SEARCH', '/maintenix/web/user/UserSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:37 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10115, NULL,'web.menuitem.DEPARTMENT_SEARCH', '/maintenix/web/department/DepartmentSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:38 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10116, NULL,'web.menuitem.AUTHORITY_SEARCH', '/maintenix/web/authority/AuthoritySearch.jsp', 0, 0);

--changeSet 0utl_menu_item:39 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10117, NULL,'web.menuitem.TRANSFER_SEARCH', '/maintenix/web/transfer/TransferSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:40 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10118, NULL,'web.menuitem.PART_GROUP_SEARCH', '/maintenix/web/part/BOMPartSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:41 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10119, NULL,'web.menuitem.PART_REQUEST_SEARCH', '/maintenix/web/req/PartRequestSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:42 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10120, NULL,'web.menuitem.ORDER_SEARCH', '/maintenix/web/po/POSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:43 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10121, NULL,'web.menuitem.TASK_DEFINITION_SEARCH', '/maintenix/web/taskdefn/TaskDefinitionSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:44 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10122, NULL,'web.menuitem.TOOL_SEARCH', '/maintenix/web/tool/ToolSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:45 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10123, NULL,'web.menuitem.CAPABILITY_SEARCH', '/maintenix/web/capability/CapabilitySearch.jsp', 0, 0);

--changeSet 0utl_menu_item:46 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10124, NULL,'web.menuitem.PO_INVOICE_SEARCH', '/maintenix/web/pi/POInvoiceSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:47 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10125, NULL,'web.menuitem.ACCOUNT_SEARCH', '/maintenix/web/fnc/AccountSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:48 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10126, NULL,'web.menuitem.TCODE_SEARCH', '/maintenix/web/fnc/TCodeSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:49 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10127, NULL, 'web.menuitem.INVENTORY_SEARCH_BY_TYPE', '/maintenix/web/inventory/InventorySearchByType.jsp',  0,0);

--changeSet 0utl_menu_item:50 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10128, NULL, 'web.menuitem.TASK_SEARCH_BY_TYPE', '/maintenix/web/task/TaskSearchByType.jsp',  0,0);

--changeSet 0utl_menu_item:51 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10129, NULL, 'web.menuitem.RFQ_SEARCH', '/maintenix/web/rfq/RFQSearch.jsp',  0,0);

--changeSet 0utl_menu_item:52 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10130, NULL,'web.menuitem.DATABASE_RULE_SEARCH', '/maintenix/web/dbrulechecker/DatabaseRuleSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:53 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10131, NULL,'web.menuitem.FAULT_DEFIINITION_SEARCH', '/maintenix/web/faultdefn/FaultDefinitionSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:54 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10132, NULL,'web.menuitem.FAILURE_EFFECT_SEARCH', '/maintenix/web/faileffect/FailureEffectSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:55 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10133, NULL,'web.menuitem.IETM_SEARCH', '/maintenix/web/ietm/IetmSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:56 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10134, NULL,'web.menuitem.MEASUREMENT_SEARCH', '/maintenix/web/assembly/measurements/MeasurementSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:57 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10135, NULL,'web.menuitem.XACTION_SEARCH', '/maintenix/web/fnc/TransactionSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:58 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10136, NULL,'web.menuitem.ORDER_SEARCH_BY_TYPE', '/maintenix/web/po/OrderSearchByType.jsp', 0, 0);

--changeSet 0utl_menu_item:59 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10139, NULL,'web.menuitem.BLOCK_SEARCH_BY_TYPE', '/maintenix/web/taskdefn/BlockSearchByType.jsp', 0, 0);

--changeSet 0utl_menu_item:60 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10140, NULL,'web.menuitem.REQ_SEARCH_BY_TYPE', '/maintenix/web/taskdefn/ReqSearchByType.jsp', 0, 0);

--changeSet 0utl_menu_item:61 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10141, NULL,'web.menuitem.JOB_CARD_SEARCH_BY_TYPE', '/maintenix/web/taskdefn/JobCardSearchByType.jsp', 0, 0);

--changeSet 0utl_menu_item:62 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10142, NULL,'web.menuitem.REF_DOC_SEARCH', '/maintenix/web/taskdefn/RefDocSearchByType.jsp', 0, 0);

--changeSet 0utl_menu_item:63 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10143, NULL,'web.menuitem.PART_SEARCH_BY_TYPE', '/maintenix/web/part/PartSearchByType.jsp', 0, 0);

--changeSet 0utl_menu_item:64 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10144, NULL,'web.menuitem.LICENSE_DEFN_SEARCH', '/maintenix/web/licensedefn/LicenseDefnSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:65 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10146, NULL, 'web.menuitem.FC_MODEL_SEARCH_BY_TYPE', '/maintenix/web/forecast/FcModelSearchByType.jsp',  0,0);

--changeSet 0utl_menu_item:66 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10147, NULL, 'web.menuitem.ORGANIZATION_SEARCH_BY_TYPE', '/maintenix/web/org/OrganizationSearchByType.jsp', 0, 0);

--changeSet 0utl_menu_item:67 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10148, NULL, 'web.menuitem.WARRANTY_CONTRACT_SEARCH', '/maintenix/web/warranty/WarrantyContractSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:68 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10149, NULL, 'web.menuitem.FAULT_SEARCH_BY_TYPE', '/maintenix/web/fault/FaultSearchByType.jsp', 0, 0);

--changeSet 0utl_menu_item:69 stripComments:false
-- please do not use 10150 as it appeared in an older version of the std sol and thus at some customer sites
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10151, NULL,'web.menuitem.ADVISORY_SEARCH', '/maintenix/web/advisory/OutstandingAdvsrySearchByType.jsp', 0, 0);

--changeSet 0utl_menu_item:70 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (10152, NULL, 'web.menuitem.RELIABILITY_NOTE_SEARCH', '/maintenix/web/inventory/reliability/ReliabilityNoteSearch.jsp', 0, 0);

--changeSet 0utl_menu_item:72 stripComments:false
--Finance
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (120922, NULL,'web.menuitem.DETAILED_FNC_LOG_REPORT', '/maintenix/servlet/report/generate?aTemplate=inventory.DetailInvFncLog&aViewPDF=true', 0, 0);

--changeSet 0utl_menu_item:73 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (120923, NULL,'web.menuitem.SUMMARY_FNC_LOG_REPORT', '/maintenix/servlet/report/generate?aTemplate=inventory.SummaryInvFncLog&aViewPDF=true', 0, 0);

--changeSet 0utl_menu_item:74 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (120924, NULL,'web.menuitem.ROTABLE_ADJUSTMENT_REPORT', '/maintenix/servlet/report/generate?aTemplate=inventory.RotableAdjustment&aViewPDF=true', 0, 0);

--changeSet 0utl_menu_item:75 stripComments:false
--Purchase Orders
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (12116, NULL,'web.menuitem.ISSUED_CSGN_INV_REPORT', '/maintenix/servlet/report/generate?aTemplate=inventory.IssuedCsgnInventory&aViewPDF=true', 0, 0);

--changeSet 0utl_menu_item:76 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (120945, NULL,'web.menuitem.RAISED_ORDERS_BY_ACCOUNT', '/maintenix/servlet/report/generate?aTemplate=po.RaisedOrdersByAccount&aViewPDF=true', 0, 0);

--changeSet 0utl_menu_item:77 stripComments:false
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (120943, NULL, 'web.menuitem.SPEC2K_PO_EXCEPTION_SEARCH', '/integrationweb/spec2000poexceptionsearch/index.html', 0, 0);

--changeSet 0utl_menu_item:78 stripComments:false
--Oil Consumption
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (12117, NULL,'web.menuitem.OIL_CONSUMPTION_REPORT', '/maintenix/servlet/report/generate?aTemplate=oilconsumption.OilConsumptionReport&aViewPDF=true', 0, 0);

--changeSet 0utl_menu_item:79 stripComments:false
--FSR
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (120925,NULL,'web.menuitem.EVENT_SEARCH', '/maintenix/web/event/EventSearch.jsp',0,'Event Search Page',0,0);

--changeSet 0utl_menu_item:80 stripComments:false
-- TAG
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
VALUES (120926, NULL, 'web.menuitem.TAG_DETAILS', '/maintenix/servlet/report/generate?aTemplate=tag.TagTaskDefinitions&aViewPDF=true',  0,0);

--changeSet 0utl_menu_item:81 stripComments:false
--IETM Search By Type
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (120927,NULL,'web.menuitem.IETM_SEARCH_BY_TYPE', '/maintenix/web/ietm/IetmSearchByType.jsp',0,'Ietm Search By Type',0,0);

--changeSet 0utl_menu_item:82 stripComments:false
--Claim Search
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (120928,NULL,'web.menuitem.CLAIM_SEARCH', '/maintenix/web/claim/ClaimSearch.jsp',0,'Claim Search',0,0);

--changeSet 0utl_menu_item:83 stripComments:false
--Flight Disruption Search By Type
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (120929,NULL,'web.menuitem.FLDISRUPTION_SEARCH_BY_TYPE', '/maintenix/web/flightdisruption/FlightDisruptionSearchByType.jsp',0,'Flight Disruption Search',0,0);

--changeSet 0utl_menu_item:84 stripComments:false
--Alert Search By Type
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (120930,NULL,'web.menuitem.ALERT_SEARCH', '/maintenix/web/alert/AlertSearchByType.jsp',0,'Alert Search',0,0);

--changeSet 0utl_menu_item:85 stripComments:false
--Draft Flight Safety Regulatory Report
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
VALUES (120931, NULL, 'web.menuitem.DRAFT_REGULATORY_REPORT', '/maintenix/servlet/report/generate?aTemplate=fault.DraftRegulatoryReport&aViewPDF=true', 0, 0);

--changeSet 0utl_menu_item:86 stripComments:false
-- MPC Search By Type
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (10150,NULL,'web.menuitem.MPC_SEARCH_BY_TYPE', '/maintenix/web/taskdefn/MPCSearchByType.jsp',0,'Master Panel Card Search By Type',0,0);

--changeSet 0utl_menu_item:87 stripComments:false
-- Procurement
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (10155,NULL,'web.menuitem.MANAGE_TAXES', '/maintenix/web/procurement/ManageTaxCharge.jsp?aWorkflow=TAX',0,'Manage taxes',0,0);

--changeSet 0utl_menu_item:88 stripComments:false
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (10156,NULL,'web.menuitem.MANAGE_CHARGES', '/maintenix/web/procurement/ManageTaxCharge.jsp?aWorkflow=CHARGE',0,'Manage charges',0,0);

--changeSet 0utl_menu_item:89 stripComments:false
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (10157,NULL,'web.menuitem.EDIT_VENDOR_TAX', '/maintenix/web/procurement/EditVendorTaxCharge.jsp?aWorkflow=TAX',0,'Edit Vendor Tax',0,0);

--changeSet 0utl_menu_item:90 stripComments:false
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (10158,NULL,'web.menuitem.EDIT_VENDOR_CHARGE', '/maintenix/web/procurement/EditVendorTaxCharge.jsp?aWorkflow=CHARGE',0,'Edit Vendor Charge',0,0);

--changeSet 0utl_menu_item:91 stripComments:false
-- Fleet Settings page
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
   VALUES (120932, NULL,'web.menuitem.FLEET_SETTINGS', '/maintenix/web/lpa/FleetSettings.jsp',  0,0);

--changeSet 0utl_menu_item:92 stripComments:false
-- MOBILE
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (120936,NULL,'web.menuitem.LINE_MAINTENANCE', '/maintenix/mobile/linemaint/app.html',0,'Mobile Line Maintenance',0,0);

--changeSet 0utl_menu_item:93 stripComments:false
--Manage Skills page
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (120938,NULL,'web.menuitem.MANAGE_SKILLS', '/maintenix/web/org/ManageSkills.jsp',0,'Manage Skills',0,0);

--changeSet 0utl_menu_item:94 stripComments:false
-- PPC Management Console
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (120939,NULL,'web.menuitem.PPC_MGMT', '/maintenix/ui/maint/planning/ppc/MgmtConsole.jsp',0,'Production Planning and Control Management Page',0,0);

--changeSet 0utl_menu_item:95 stripComments:false
-- ARC UI
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
VALUES (120940,NULL,'web.menuitem.ARC_MESSAGE_MANAGEMENT', '/arc',0,'Manage ARC Messages',0,0);

--changeSet 0utl_menu_item:96 stripComments:false
-- Vendor Search By Type
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (120941,NULL,'web.menuitem.VENDOR_SEARCH_BY_TYPE', '/maintenix/web/vendor/VendorSearchByType.jsp',0,'Vendor Search By Type',0,0);

--changeSet 0utl_menu_item:97 stripComments:false
-- Manage aircraft group page
INSERT  INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (120942,NULL,'web.menuitem.MANAGE_AIRCRAFT_GROUPS', '/maintenix/web/acftgroup/ManageAircraftGroups.jsp',0,'Manage Aircraft Groups',0,0);

--changeSet 0utl_menu_item:98 stripComments:false
-- Phone up Deferral
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,UTL_ID)
VALUES (120944,NULL,'web.menuitem.PHONE_UP_DEFERRAL','/lmocweb/phone-up-deferral/index.html',0,'Phone Up Deferral',0,0);

--changeSet 0utl_menu_item:99 stripComments:false
-- Deferral Request Authorization Application
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, MENU_LDESC, REPL_APPROVED, UTL_ID)
VALUES (120947, NULL, 'web.menuitem.REFERENCE_APPROVAL', '/lmocweb/pending-reference-approvals/index.html',0,'The List of Reference Approval Requests',0,0);

--changeSet 0utl_menu_item:100 stripComments:false
-- Deferral Reference Search
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, MENU_LDESC, REPL_APPROVED, UTL_ID)
VALUES (120948, NULL, 'web.menuitem.DEFERRAL_REFERENCE_SEARCH', '/lmocweb/deferral-reference/index.html',0,'Deferral Reference Search',0,0);

--changeSet 0utl_menu_item:101 stripComments:false
-- Launch Work Package Loader
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, MENU_LDESC, REPL_APPROVED, UTL_ID)
VALUES (120949, NULL, 'web.menuitem.WORK_PACKAGE_LOADER', '/wpl-web/ui/induction/WorkPackageLoader.html',0,'Work Package Loader',0,0);

--changeSet 0utl_menu_item:102 stripComments:false
-- Launch Record Oil Uptake
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, MENU_LDESC, REPL_APPROVED, UTL_ID)
VALUES (120950, NULL, 'web.menuitem.RECORD_OIL_UPTAKE', '/lmocweb/oil-recording/index.html',0,'Record Oil Uptake',0,0);

--changeSet 0utl_menu_item:103 stripComments:false
-- Launch Quick Text Admin Page
INSERT INTO UTL_MENU_ITEM (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, MENU_LDESC, REPL_APPROVED, UTL_ID)
VALUES (120951, NULL, 'web.menuitem.QUICK_TEXT', '/lmocweb/quick-text/index.html',0,'Manage Quick Text',0,0);