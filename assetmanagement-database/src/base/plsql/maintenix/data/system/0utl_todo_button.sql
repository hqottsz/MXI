--liquibase formatted sql


--changeSet 0utl_todo_button:1 stripComments:false
-- Shipment Search button
/******************************************
** 0-Level INSERT SCRIPT FOR UTL_TODO_BUTTON
*******************************************/
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10000, 'ACTION_SEARCH_SHIPMENT', 'web.todobutton.SHIPMENT_SEARCH_NAME', '/common/images/tracker/shipment_srch.gif', '/web/shipment/ShipmentSearch.jsp', 'web.todobutton.SHIPMENT_SEARCH_TOOLTIP', 'web.todobutton.SHIPMENT_SEARCH_LDESC', 0);

--changeSet 0utl_todo_button:2 stripComments:false
-- Inventory Search button
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10002, NULL, 'web.todobutton.INVENTORY_SEARCH_NAME', '/common/images/tracker/inv_srch.gif', '/web/inventory/InventorySearch.jsp', 'web.todobutton.INVENTORY_SEARCH_TOOLTIP', 'web.todobutton.INVENTORY_SEARCH_LDESC', 0);

--changeSet 0utl_todo_button:3 stripComments:false
-- Create Inventory button
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10003, 'ACTION_CREATE_INVENTORY', 'web.todobutton.CREATE_INVENTORY_NAME', '/common/images/tracker/inv_cre.gif', '/web/inventory/CreateInventory.jsp?aMode=CREATE_INV', 'web.todobutton.CREATE_INVENTORY_TOOLTIP', 'web.todobutton.CREATE_INVENTORY_LDESC', 0);

--changeSet 0utl_todo_button:4 stripComments:false
-- Part Search
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10005, NULL, 'web.todobutton.PART_SEARCH_NAME', '/common/images/tracker/part_srch.gif', '/web/part/PartSearch.jsp', 'web.todobutton.PART_SEARCH_TOOLTIP', 'web.todobutton.PART_SEARCH_LDESC', 0);

--changeSet 0utl_todo_button:5 stripComments:false
-- Create Owner
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10006, 'ACTION_CREATE_OWNER', 'web.todobutton.CREATE_OWNER_NAME', '/common/images/tracker/own_cre.gif', '/web/owner/CreateEditOwner.jsp', 'web.todobutton.CREATE_OWNER_TOOLTIP', 'web.todobutton.CREATE_OWNER_LDESC', 0);

--changeSet 0utl_todo_button:6 stripComments:false
-- Owner Search
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10007, NULL, 'web.todobutton.OWNER_SEARCH_NAME', '/common/images/tracker/own_srch.gif', '/web/owner/OwnerSearch.jsp', 'web.todobutton.OWNER_SEARCH_TOOLTIP', 'web.todobutton.OWNER_SEARCH_LDESC', 0);

--changeSet 0utl_todo_button:7 stripComments:false
-- Create Vendor
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10008, 'ACTION_CREATE_VENDOR', 'web.todobutton.CREATE_VENDOR_NAME', '/common/images/tracker/vendor_add.gif', '/web/vendor/CreateEditVendor.jsp?aMode=CREATE_VENDOR', 'web.todobutton.CREATE_VENDOR_TOOLTIP', 'web.todobutton.CREATE_VENDOR_LDESC', 0);

--changeSet 0utl_todo_button:8 stripComments:false
-- Vendor Search
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10009, NULL, 'web.todobutton.VENDOR_SEARCH_NAME', '/common/images/tracker/vendor_srch.gif', '/web/vendor/VendorSearch.jsp', 'web.todobutton.VENDOR_SEARCH_TOOLTIP', 'web.todobutton.VENDOR_SEARCH_LDESC', 0);

--changeSet 0utl_todo_button:9 stripComments:false
-- Create Manufacturer
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10010, 'ACTION_CREATE_MANUFACTURER', 'web.todobutton.CREATE_MANUFACTURER_NAME', '/common/images/tracker/manufact_cre.gif', '/web/manufacturer/CreateEditManufacturer.jsp', 'web.todobutton.CREATE_MANUFACTURER_TOOLTIP', 'web.todobutton.CREATE_MANUFACTURER_LDESC', 0);

--changeSet 0utl_todo_button:10 stripComments:false
-- Manufacturer Search
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10011, NULL, 'web.todobutton.MANUFACTURER_SEARCH_NAME', '/common/images/tracker/manufact_srch.gif', '/web/manufacturer/ManufacturerSearch.jsp', 'web.todobutton.MANUFACTURER_SEARCH_TOOLTIP', 'web.todobutton.MANUFACTURER_SEARCH_LDESC', 0);

--changeSet 0utl_todo_button:11 stripComments:false
-- Flight Search
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10013, NULL, 'web.todobutton.FLIGHT_SEARCH_NAME', '/common/images/tracker/flight_srch.gif', '/web/flight/FlightSearch.jsp', 'web.todobutton.FLIGHT_SEARCH_TOOLTIP', 'web.todobutton.FLIGHT_SEARCH_LDESC', 0);

--changeSet 0utl_todo_button:12 stripComments:false
-- Create Stock
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10014, 'ACTION_CREATE_STOCK', 'web.todobutton.CREATE_STOCK_NAME', '/common/images/tracker/stock_cre.gif', '/web/stock/CreateEditStock.jsp?aMode=CREATE_MODE', 'web.todobutton.CREATE_STOCK_TOOLTIP', 'web.todobutton.CREATE_STOCK_LDESC', 0);

--changeSet 0utl_todo_button:13 stripComments:false
-- Stock Search
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10015, NULL, 'web.todobutton.STOCK_SEARCH_NAME', '/common/images/tracker/stock_srch.gif', '/web/stock/StockSearch.jsp', 'web.todobutton.STOCK_SEARCH_TOOLTIP', 'web.todobutton.STOCK_SEARCH_LDESC', 0);

--changeSet 0utl_todo_button:14 stripComments:false
-- Create Location
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10016, 'ACTION_CREATE_LOCATION', 'web.todobutton.CREATE_LOCATION_NAME', '/common/images/tracker/loc_add.gif', '/web/location/CreateLocation.jsp', 'web.todobutton.CREATE_LOCATION_TOOLTIP', 'web.todobutton.CREATE_LOCATION_LDESC', 0);

--changeSet 0utl_todo_button:15 stripComments:false
-- Location Search
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10017, NULL, 'web.todobutton.LOCATION_SEARCH_NAME', '/common/images/tracker/loc_srch.gif', '/web/location/LocationSearch.jsp', 'web.todobutton.LOCATION_SEARCH_TOOLTIP', 'web.todobutton.LOCATION_SEARCH_LDESC', 0);

--changeSet 0utl_todo_button:16 stripComments:false
-- Create User
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10018, 'ACTION_CREATE_USER', 'web.todobutton.CREATE_USER_NAME', '/common/images/tracker/user_cre.gif', '/web/user/CreateEditUser.jsp?aMode=CREATE_USER', 'web.todobutton.CREATE_USER_TOOLTIP', 'web.todobutton.CREATE_USER_LDESC', 0);

--changeSet 0utl_todo_button:17 stripComments:false
-- User Search
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10019, NULL, 'web.todobutton.USER_SEARCH_NAME', '/common/images/tracker/user_srch.gif', '/web/user/UserSearch.jsp', 'web.todobutton.USER_SEARCH_TOOLTIP', 'web.todobutton.USER_SEARCH_LDESC', 0);

--changeSet 0utl_todo_button:18 stripComments:false
-- Create Role
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10020, 'ACTION_CREATE_ROLE', 'web.todobutton.CREATE_ROLE_NAME', '/common/images/tracker/role_add.gif', '/web/role/CreateEditRole.jsp?aMode=CREATE_ROLE', 'web.todobutton.CREATE_ROLE_TOOLTIP', 'web.todobutton.CREATE_ROLE_LDESC', 0);

--changeSet 0utl_todo_button:19 stripComments:false
-- Role Search
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10021, NULL, 'web.todobutton.ROLE_SEARCH_NAME', '/common/images/tracker/role_srch.gif', '/web/role/RoleSearch.jsp', 'web.todobutton.ROLE_SEARCH_TOOLTIP', 'web.todobutton.ROLE_SEARCH_LDESC', 0);

--changeSet 0utl_todo_button:20 stripComments:false
-- Authority Search
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10022, NULL, 'web.todobutton.AUTHORITY_SEARCH_NAME', '/common/images/tracker/authority_srch.gif', '/web/authority/AuthoritySearch.jsp', 'web.todobutton.AUTHORITY_SEARCH_TOOLTIP', 'web.todobutton.AUTHORITY_SEARCH_LDESC', 0);

--changeSet 0utl_todo_button:21 stripComments:false
-- 5Day Planner
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10023, 'ACTION_VIEW_FIVE_DAY_PLANNER', 'web.todobutton.FIVE_DAY_PLANNER_NAME', '/common/images/tracker/planner.gif', '/web/todolist/AircraftPlanner.jsp', 'web.todobutton.FIVE_DAY_PLANNER_TOOLTIP', 'web.todobutton.FIVE_DAY_PLANNER_LDESC', 0);

--changeSet 0utl_todo_button:23 stripComments:false
-- Create Authority Button
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10025, 'ACTION_CREATE_AUTHORITY', 'web.todobutton.CREATE_AUTHORITY_NAME', '/common/images/tracker/authority_cre.gif', '/web/authority/CreateEditAuthority.jsp?aMode=CREATE_MODE', 'web.todobutton.CREATE_AUTHORITY_TOOLTIP', 'web.todobutton.CREATE_AUTHORITY_LDESC', 0);

--changeSet 0utl_todo_button:24 stripComments:false
 -- Transfer Search
 INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
    VALUES (10026, NULL, 'web.todobutton.TRANSFER_SEARCH_NAME', '/common/images/tracker/transfer_srch.gif', '/web/transfer/TransferSearch.jsp', 'web.todobutton.TRANSFER_SEARCH_TOOLTIP', 'web.todobutton.TRANSFER_SEARCH_LDESC', 0);

--changeSet 0utl_todo_button:25 stripComments:false
 -- Create Ad Hoc Purchase Request
 INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
    VALUES (10027, 'ACTION_CREATE_ADHOC_PURCHASE_REQUEST', 'web.todobutton.CREATE_AD_HOC_PU_REQ_NAME', '/common/images/tracker/purchase_request_cre.gif', '/web/req/CreatePartRequest.jsp?aMode=CREATE_PURCHASE_REQUEST', 'web.todobutton.CREATE_AD_HOC_PU_REQ_TOOLTIP', 'web.todobutton.CREATE_AD_HOC_PU_REQ_LDESC', 0);

--changeSet 0utl_todo_button:26 stripComments:false
 -- Create Ad Hoc Part Request
 INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
    VALUES (10028, 'ACTION_CREATE_ADHOC_PART_REQUEST', 'web.todobutton.CREATE_AD_HOC_PT_REQ_NAME', '/common/images/tracker/part_req_cre.gif', '/web/req/CreatePartRequest.jsp?aMode=CREATE_PART_REQUEST', 'web.todobutton.CREATE_AD_HOC_PT_REQ_TOOLTIP', 'web.todobutton.CREATE_AD_HOC_PT_REQ_LDESC', 0);

--changeSet 0utl_todo_button:27 stripComments:false
-- Create Ad Hoc Purchase Order
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
    VALUES (10029, 'ACTION_CREATE_AD_HOC_PO', 'web.todobutton.CREATE_AD_HOC_PU_ORD_NAME', '/common/images/tracker/po_cre.gif', '/web/po/CreateEditOrder.jsp', 'web.todobutton.CREATE_AD_HOC_PU_ORD_TOOLTIP', 'web.todobutton.CREATE_AD_HOC_PU_ORD_LDESC', 0);

--changeSet 0utl_todo_button:28 stripComments:false
-- Create BOM Part
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10030, 'ACTION_CREATE_BOM_PART', 'web.todobutton.CREATE_BOM_PART_NAME', '/common/images/tracker/bom_part_cre.gif', '/web/bom/CreateEditBomPart.jsp', 'web.todobutton.CREATE_BOM_PART_TOOLTIP', 'web.todobutton.CREATE_BOM_PART_LDESC', 0);

--changeSet 0utl_todo_button:29 stripComments:false
-- Create Account
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10031, 'ACTION_CREATE_ACCOUNT', 'web.todobutton.CREATE_ACCOUNT', '/common/images/tracker/tcode_cre.gif', '/web/fnc/CreateEditAccount.jsp', 'web.todobutton.CREATE_ACCOUNT_TOOLTIP', 'web.todobutton.CREATE_ACCOUNT_LDESC', 0);

--changeSet 0utl_todo_button:30 stripComments:false
-- Create TCode
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10032, 'ACTION_CREATE_TCODE', 'web.todobutton.CREATE_TCODE', '/common/images/tracker/account_cre.gif', '/web/fnc/CreateEditTCode.jsp', 'web.todobutton.CREATE_TCODE_TOOLTIP', 'web.todobutton.CREATE_TCODE_LDESC', 0);

--changeSet 0utl_todo_button:31 stripComments:false
-- Cycle Count
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, POCKET_BUTTON_BOOL, UTL_ID)
   VALUES (10033, NULL, 'web.todobutton.CYCLE_COUNT_NAME', '/common/images/tracker/recount_cycle_cnt.gif', '/web/todolist/CycleCountToDoList.jsp', 'web.todobutton.CYCLE_COUNT_TOOLTIP', 'web.todobutton.CYCLE_COUNT_LDESC', 1, 0);

--changeSet 0utl_todo_button:32 stripComments:false
-- Item Recount
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, QUERY_PATH, POCKET_BUTTON_BOOL, UTL_ID)
   VALUES (10034, NULL, 'web.todobutton.ITEM_RECOUNT_NAME', '/common/images/tracker/recount_item.gif', '/web/todolist/ItemRecountToDoList.jsp', 'web.todobutton.ITEM_RECOUNT_TOOLTIP', 'web.todobutton.ITEM_RECOUNT_LDESC', 'com.mxi.mx.web.query.todolist.ItemRecountToDoList', 1, 0);

--changeSet 0utl_todo_button:33 stripComments:false
-- Pre-Draw Inventory
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, QUERY_PATH, POCKET_BUTTON_BOOL, UTL_ID)
   VALUES (10036, NULL, 'web.todobutton.PREDRAW_INVENTORY_NAME', '/common/images/tracker/predraw_to_do_list.gif', '/web/todolist/PreDrawSelectWarehouse.jsp', 'web.todobutton.PREDRAW_INVENTORY_TOOLTIP', 'web.todobutton.PREDRAW_INVENTORY_LDESC', null, 1, 0);

--changeSet 0utl_todo_button:34 stripComments:false
-- Put Away Inventory
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, QUERY_PATH, POCKET_BUTTON_BOOL, UTL_ID)
   VALUES (10037, NULL, 'web.todobutton.PUT_AWAY_INVENTORY_NAME', '/common/images/tracker/putaway_to_do_list.gif', '/web/todolist/PutAwayToDoList.jsp', 'web.todobutton.PUT_AWAY_INVENTORY_TOOLTIP', 'web.todobutton.PUT_AWAY_INVENTORY_LDESC', 'com.mxi.mx.web.query.todolist.PutAways', 1, 0);

--changeSet 0utl_todo_button:35 stripComments:false
-- Create Ad Hoc RFQ
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, QUERY_PATH, POCKET_BUTTON_BOOL, UTL_ID)
   VALUES (10038, 'ACTION_CREATE_ADHOC_RFQ', 'web.todobutton.CREATE_AD_HOC_RFQ', '/common/images/tracker/rfq_cre.gif', '/web/rfq/CreateEditRFQ.jsp', 'web.todobutton.CREATE_AD_HOC_RFQ_TOOLTIP', 'web.todobutton.CREATE_AD_HOC_RFQ_LDESC', NULL, 0, 0);

--changeSet 0utl_todo_button:36 stripComments:false
-- Create Exchange Order
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10041, 'ACTION_CREATE_EO', 'web.todobutton.CREATE_EXCH_ORDER', '/common/images/tracker/exch_o_cre.gif', '/web/po/CreateEditOrder.jsp?aMode=CREATE_EO', 'web.todobutton.CREATE_EXCH_ORDER', 'web.todobutton.CREATE_EXCHANGE_ORDER_LDESC', 0);

--changeSet 0utl_todo_button:37 stripComments:false
-- Create Borrow Order
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10042, 'ACTION_CREATE_BO', 'web.todobutton.CREATE_BORROW_ORDER', '/common/images/tracker/borrow_o_cre.gif', '/web/po/CreateEditOrder.jsp?aMode=CREATE_BO', 'web.todobutton.CREATE_BORROW_ORDER', 'web.todobutton.CREATE_BORROW_ORDER_LDESC', 0);

--changeSet 0utl_todo_button:38 stripComments:false
-- Refresh Configuration
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10043, 'ACTION_REFRESH_CONFIGURATION', 'web.todobutton.REFRESH_CONFIGURATION', '/common/images/tracker/refresh.gif', '/servlet/RefreshConfiguration', 'web.todobutton.REFRESH_CONFIGURATION_TOOLTIP', 'web.todobutton.REFRESH_CONFIGURATION_LDESC', 0);

--changeSet 0utl_todo_button:39 stripComments:false
-- Create Shipment
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10044, 'ACTION_CREATE_SHIPMENT', 'web.todobutton.CREATE_SHIPMENT', '/common/images/tracker/shipment_cre.gif', '/web/shipment/CreateEditShipment.jsp', 'web.todobutton.CREATE_SHIPMENT', 'web.todobutton.CREATE_SHIPMENT_LDESC', 0);

--changeSet 0utl_todo_button:40 stripComments:false
-- Issue Inventory
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10045, 'ACTION_ISSUE_INVENTORY', 'web.todobutton.ISSUE_INVENTORY_NAME', '/common/images/tracker/inv_issue.gif', '/web/req/IssueInventory.jsp', 'web.todobutton.ISSUE_INVENTORY_TOOLTIP', 'web.todobutton.ISSUE_INVENTORY_LDESC', 0);

--changeSet 0utl_todo_button:41 stripComments:false
-- Pick Items for Shipment
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10046, 'ACTION_PICK_SHIPMENT', 'web.todobutton.PICK_ITEMS_FOR_SHIPMENT', '/common/images/tracker/shipment_pick.gif', '/web/shipment/PickShipment.jsp', 'web.todobutton.PICK_ITEMS_FOR_SHIPMENT', 'web.todobutton.PICK_ITEMS_FOR_SHIPMENT_LDESC', 0);

--changeSet 0utl_todo_button:42 stripComments:false
-- Refresh Alert Engine Cache
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10047, 'ACTION_REFRESH_ALERT_ENGINE_CACHE', 'web.todobutton.REFRESH_ALERT_ENGINE_CAC', '/common/images/tracker/refresh.gif', '/servlet/RefreshAlertEngineCache', 'web.todobutton.REFRESH_ALERT_ENGINE_CACHE_TOOLTIP', 'web.todobutton.REFRESH_ALERT_ENGINE_CACHE_LDESC', 0);

--changeSet 0utl_todo_button:43 stripComments:false
-- Create Maintenance Program
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10048, 'ACTION_CREATE_MAINT_PROGRAM', 'web.todobutton.CREATE_MAINT_PROGRAM', '/common/images/tracker/exch_o_cre.gif', '/web/maint/CreateEditMaintProgram.jsp', 'web.todobutton.CREATE_MAINT_PROGRAM', 'web.todobutton.CREATE_MAINT_PROGRAM', 0);

--changeSet 0utl_todo_button:44 stripComments:false
-- Create Requirement
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10049, 'ACTION_CREATE_REQ', 'web.todobutton.CREATE_REQ', '/common/images/tracker/task_add.gif', '/web/taskdefn/CreateEditReq.jsp', 'web.todobutton.CREATE_REQ_TOOLTIP', 'web.todobutton.CREATE_REQ_LDESC', 0);

--changeSet 0utl_todo_button:45 stripComments:false
-- Create Block
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10050, 'ACTION_CREATE_BLOCK', 'web.todobutton.CREATE_BLOCK', '/common/images/tracker/task_add.gif', '/web/taskdefn/CreateEditBlock.jsp', 'web.todobutton.CREATE_BLOCK_TOOLTIP', 'web.todobutton.CREATE_BLOCK_LDESC', 0);

--changeSet 0utl_todo_button:46 stripComments:false
-- Create Job Card
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10051, 'ACTION_CREATE_JIC', 'web.todobutton.CREATE_JIC', '/common/images/tracker/task_add.gif', '/web/taskdefn/CreateEditJobCard.jsp', 'web.todobutton.CREATE_JIC_TOOLTIP', 'web.todobutton.CREATE_JIC_LDESC', 0);

--changeSet 0utl_todo_button:47 stripComments:false
-- Create Consignment Order
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10052, 'ACTION_CREATE_CONSIGN_ORDER', 'web.todobutton.CREATE_CONSIGN_ORDER', '/common/images/tracker/cons_o_cre.gif', '/web/po/CreateEditOrder.jsp?aMode=CREATE_CONSGIN_ORDER', 'web.todobutton.CREATE_CONSIGN_ORDER', 'web.todobutton.CREATE_CONSIGN_ORDER_LDESC', 0);

--changeSet 0utl_todo_button:48 stripComments:false
-- Create Requirement
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10053, 'ACTION_CREATE_REF_DOC', 'web.todobutton.CREATE_REF_DOC', '/common/images/tracker/task_add.gif', '/web/taskdefn/CreateEditRefDoc.jsp', 'web.todobutton.CREATE_REF_DOC_TOOLTIP', 'web.todobutton.CREATE_REF_DOC_LDESC', 0);

--changeSet 0utl_todo_button:49 stripComments:false
-- Inventory Turn-In
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10054, 'ACTION_INV_TURN_IN', 'web.todobutton.INV_TURN_IN', '/common/images/tracker/transfer_cre.gif', '/web/transfer/TurnIn.jsp', 'web.todobutton.INV_TURN_IN_TOOLTIP', 'web.todobutton.INV_TURN_IN_LDESC', 0);

--changeSet 0utl_todo_button:50 stripComments:false
-- Launch Long Range Planner
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10055, 'APP_LONG_RANGE_PLANNER', 'web.todobutton.LAUNCH_LRP_NAME', '/common/images/tracker/lrp_launch.gif', '/web/lrp/LongRangePlannerJNLP.jsp', 'web.todobutton.LAUNCH_LRP_TOOLTIP', 'web.todobutton.LAUNCH_LRP_LDESC', 0);

--changeSet 0utl_todo_button:51 stripComments:false
-- Create Ad-Hoc Stock Purchase Request
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
   VALUES (10056, 'ACTION_CREATE_PURCHASE_REQUEST_FOR_STOCK', 'web.todobutton.CREATE_AD_HOC_SR_REQ_NAME', '/common/images/tracker/purchase_request_cre.gif', '/web/req/CreatePurchaseRequestForStock.jsp?aMode=CREATE_STOCK_REQUEST', 'web.todobutton.CREATE_AD_HOC_SR_REQ_TOOLTIP', 'web.todobutton.CREATE_AD_HOC_SR_REQ_LDESC', 0);

--changeSet 0utl_todo_button:52 stripComments:false
-- Create Planning Type
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10057, 'ACTION_CREATE_PLANNING_TYPE', 'web.todobutton.CREATE_PLANNING_TYPE_NAME', '/common/images/tracker/create.gif', '/web/taskdefn/CreateEditPlanningType.jsp?aMode=CREATE_PLANNING_TYPE', 'web.todobutton.CREATE_PLANNING_TYPE_TOOLTIP', 'web.todobutton.CREATE_PLANNING_TYPE_LDESC', 0);

--changeSet 0utl_todo_button:53 stripComments:false
-- Edit Planning Type
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10058, 'ACTION_EDIT_PLANNING_TYPE', 'web.todobutton.EDIT_PLANNING_TYPE_NAME', '/common/images/tracker/edit.gif', '/web/taskdefn/CreateEditPlanningType.jsp?aMode=EDIT_PLANNING_TYPE', 'web.todobutton.EDIT_PLANNING_TYPE_TOOLTIP', 'web.todobutton.EDIT_PLANNING_TYPE_LDESC', 0);

--changeSet 0utl_todo_button:54 stripComments:false
-- Remove Planning Type
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10059, 'ACTION_REMOVE_PLANNING_TYPE', 'web.todobutton.REMOVE_PLANNING_TYPE_NAME', '/common/images/tracker/delete.gif', '/web/taskdefn/RemovePlanningType.jsp', 'web.todobutton.REMOVE_PLANNING_TYPE_TOOLTIP', 'web.todobutton.REMOVE_PLANNING_TYPE_LDESC', 0);

--changeSet 0utl_todo_button:55 stripComments:false
-- Launch Production Planning and Control
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10060, 'APP_PRODUCTION_PLAN_CONTROLLER', 'web.todobutton.LAUNCH_PPC_NAME', '/common/images/tracker/projPlan_open.gif', '/web/ppc/PpcApplicationJNLP.jsp', 'web.todobutton.LAUNCH_PPC_TOOLTIP', 'web.todobutton.LAUNCH_PPC_LDESC', 0);

--changeSet 0utl_todo_button:56 stripComments:false
-- Manage taxes
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10061, 'ACTION_MANAGE_TAXES', 'web.todobutton.MANAGE_TAXES_NAME', '/common/images/tracker/tax_prp.gif', '/web/procurement/ManageTaxCharge.jsp?aWorkflow=TAX', 'web.todobutton.MANAGE_TAXES_TOOLTIP', 'web.todobutton.MANAGE_TAXES_LDESC', 0);

--changeSet 0utl_todo_button:57 stripComments:false
-- Manage charges
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10062, 'ACTION_MANAGE_CHARGES', 'web.todobutton.MANAGE_CHARGES_NAME', '/common/images/tracker/tax_prp.gif', '/web/procurement/ManageTaxCharge.jsp?aWorkflow=CHARGE', 'web.todobutton.MANAGE_CHARGES_TOOLTIP', 'web.todobutton.MANAGE_CHARGES_LDESC', 0);

--changeSet 0utl_todo_button:58 stripComments:false
--  Edit Vendor Tax
INSERT INTO utl_todo_button (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10063, 'ACTION_EDIT_VENDOR_TAX', 'web.todobutton.EDIT_VENDOR_TAX', '/common/images/tracker/pencil.gif', '/web/procurement/EditVendorTaxCharge.jsp?aWorkflow=TAX', 'web.todobutton.EDIT_VENDOR_TAX_TOOLTIP', 'web.todobutton.EDIT_VENDOR_TAX_LDESC', 0);

--changeSet 0utl_todo_button:59 stripComments:false
--  Edit Vendor Charge
INSERT INTO utl_todo_button (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10064, 'ACTION_EDIT_VENDOR_CHARGE', 'web.todobutton.EDIT_VENDOR_CHARGE', '/common/images/tracker/pencil.gif', '/web/procurement/EditVendorTaxCharge.jsp?aWorkflow=CHARGE', 'web.todobutton.EDIT_VENDOR_CHARGE_TOOLTIP', 'web.todobutton.EDIT_VENDOR_CHARGE_LDESC', 0);

--changeSet 0utl_todo_button:60 stripComments:false
-- Manage AD Status button
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10065, NULL, 'web.todobutton.MANAGE_AD_STATUS', NULL, '/../eng/ui/maint/exe/workpackage/ManageADStatusOptions.html', 'web.todobutton.MANAGE_AD_STATUS_TOOLTIP', 'web.todobutton.MANAGE_AD_STATUS_LDESC', 0);

--changeSet 0utl_todo_button:61 stripComments:false
-- Manage SB Status button
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10066, NULL, 'web.todobutton.MANAGE_SB_STATUS', NULL, '/../eng/ui/maint/exe/workpackage/ManageSBStatusOptions.html', 'web.todobutton.MANAGE_SB_STATUS_TOOLTIP', 'web.todobutton.MANAGE_SB_STATUS_LDESC', 0);

--changeSet 0utl_todo_button:62 stripComments:false
-- Launch LMOC Phone Up Deferral Application
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10067, NULL, 'web.todobutton.PHONE_UP_DEFERRAL_NAME', NULL, '/../lmocweb/phone-up-deferral/index.html', 'web.todobutton.PHONE_UP_DEFERRAL_TOOLTIP', 'web.todobutton.PHONE_UP_DEFERRAL_LDESC', 0);

--changeSet 0utl_todo_button:63 stripComments:false
-- Launch Planning Viewer
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10068, 'APP_PLANNING_VIEWER', 'web.todobutton.LAUNCH_PV_NAME', '/common/images/tracker/projPlan_open.gif', '/web/pv/PlanningViewerJNLP.jsp', 'web.todobutton.LAUNCH_PV_TOOLTIP', 'web.todobutton.LAUNCH_PV_LDESC', 0);

--changeSet 0utl_todo_button:64 stripComments:false
-- Manage Station Monitoring button
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10069, 'ACTION_OPEN_SMA', 'web.todobutton.APP_STN_MONITORING', NULL, ':open:/../lmocweb/station-monitoring', 'web.todobutton.APP_STN_MONITORING_TOOL_TIP', 'web.todobutton.APP_STN_MONITORING_LDESC', 0);

--changeSet 0utl_todo_button:65 stripComments:false
-- Launch Pending Reference Approvals Application
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10071, NULL, 'web.todobutton.REFERENCE_APPROVAL_NAME', NULL, '/../lmocweb/pending-reference-approvals/index.html', 'web.todobutton.REFERENCE_APPROVAL_TOOLTIP', 'web.todobutton.REFERENCE_APPROVAL_LDESC', 0);

--changeSet 0utl_todo_button:66 stripComments:false
-- Launch LMOC Deferral Reference Search
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10072, NULL, 'web.todobutton.DEFERRAL_REF_SEARCH_NAME', NULL, '/../lmocweb/deferral-reference/index.html', 'web.todobutton.DEFERRAL_REF_SEARCH_TOOLTIP', 'web.todobutton.DEFERRAL_REF_SEARCH_LDESC', 0);

--changeSet 0utl_todo_button:67 stripComments:false
-- Launch Work Package Loader
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10073, 'ACTION_LAUNCH_WORK_PACKAGE_LOADER', 'web.todobutton.WORK_PACKAGE_LOADER_NAME', NULL, '/../wpl-web/ui/induction/WorkPackageLoader.html', 'web.todobutton.WORK_PACKAGE_LOADER_TOOLTIP', 'web.todobutton.WORK_PACKAGE_LOADER_LDESC', 0);

--changeSet 0utl_todo_button:68 stripComments:false
-- Inventory Count button
INSERT INTO UTL_TODO_BUTTON (TODO_BUTTON_ID, PARM_NAME, BUTTON_NAME, ICON, ACTION, TOOLTIP, TODO_BUTTON_LDESC, UTL_ID)
VALUES (10074, 'ACTION_INVENTORY_COUNT', 'web.todobutton.INVENTORY_COUNT_NAME', NULL, '/web/inventorycount/InventoryCount.jsp', 'web.todobutton.INVENTORY_COUNT_TOOLTIP', 'web.todobutton.INVENTORY_COUNT_LDESC', 0);
