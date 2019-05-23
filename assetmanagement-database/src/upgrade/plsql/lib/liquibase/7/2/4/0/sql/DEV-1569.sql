--liquibase formatted sql


--changeSet DEV-1569:1 stripComments:false
-- delete all 0level utl_help rows first
DELETE FROM utl_help where utl_help.utl_id = 0;

--changeSet DEV-1569:2 stripComments:false
-- INSERT SCRIPT FOR TABLE UTL_HELP
insert into utl_help values ('/maintenix/web/advisory/AdvisoryDetails.jsp', 'htmlhelp.htm#advisory_details_page.htm',0);

--changeSet DEV-1569:3 stripComments:false
insert into utl_help values ('/maintenix/web/alert/AlertDetails.jsp', 'htmlhelp.htm#alert_details_page.htm',0);

--changeSet DEV-1569:4 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/AssemblyDetails.jsp', 'htmlhelp.htm#assembly_details_page.htm',0);

--changeSet DEV-1569:5 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/deferral/details/DeferRefDetails.jsp', 'htmlhelp.htm#deferral_reference_details_page.htm',0);

--changeSet DEV-1569:6 stripComments:false
insert into utl_help values ('/maintenix/web/authority/AuthorityDetails.jsp', 'htmlhelp.htm#authority_details_page.htm',0);

--changeSet DEV-1569:7 stripComments:false
insert into utl_help values ('/maintenix/web/bom/ConfigSlotDetails.jsp', 'htmlhelp.htm#config_slot_details_page.htm',0);

--changeSet DEV-1569:8 stripComments:false
insert into utl_help values ('/maintenix/web/bom/faultthresholdFaultThresholdDetails.jsp', 'htmlhelp.htm#fault_threshold_details_page.htm',0);

--changeSet DEV-1569:9 stripComments:false
insert into utl_help values ('/maintenix/web/capability/CapabilityDetails.jsp', 'htmlhelp.htm#capability_details_page.htm',0);

--changeSet DEV-1569:10 stripComments:false
insert into utl_help values ('/maintenix/web/claim/ClaimDetails.jsp', 'htmlhelp.htm#claims_details_page.htm',0);

--changeSet DEV-1569:11 stripComments:false
insert into utl_help values ('/maintenix/web/dbrulechecker/DatabaseRuleDetails.jsp', 'htmlhelp.htm#database_rule_details_page.htm',0);

--changeSet DEV-1569:12 stripComments:false
insert into utl_help values ('/maintenix/web/department/DepartmentDetails.jsp', 'htmlhelp.htm#department_details_page.htm',0);

--changeSet DEV-1569:13 stripComments:false
insert into utl_help values ('/maintenix/web/er/ExtractionRuleDetails.jsp', 'htmlhelp.htm#extraction_rules_details_page.htm',0);

--changeSet DEV-1569:14 stripComments:false
insert into utl_help values ('/maintenix/web/event/CreateEditEvent.jsp', 'htmlhelp.htm#event_details_page.htm',0);

--changeSet DEV-1569:15 stripComments:false
insert into utl_help values ('/maintenix/web/faileffect/FailEffectDetails.jsp', 'htmlhelp.htm#failure_effect_details_page.htm',0);

--changeSet DEV-1569:16 stripComments:false
insert into utl_help values ('/maintenix/web/fault/PossibleFaultDetails.jsp', 'htmlhelp.htm#possible_fault_details_page.htm',0);

--changeSet DEV-1569:17 stripComments:false
insert into utl_help values ('/maintenix/web/fault/deferral/DeferralReference.jsp', 'htmlhelp.htm#deferral_reference_details_page.htm',0);

--changeSet DEV-1569:18 stripComments:false
insert into utl_help values ('/maintenix/web/faultdefn/FaultDefinitionDetails.jsp', 'htmlhelp.htm#fault_definition_details_page.htm',0);

--changeSet DEV-1569:19 stripComments:false
insert into utl_help values ('/maintenix/web/flight/FlightDetails.jsp', 'htmlhelp.htm#flight_details_page.htm',0);

--changeSet DEV-1569:20 stripComments:false
insert into utl_help values ('/maintenix/web/flight/MasterFlightDetails.jsp', 'htmlhelp.htm#master_flight_details_page.htm',0);

--changeSet DEV-1569:21 stripComments:false
insert into utl_help values ('/maintenix/web/flightdisruption/FlightDisruptionDetails.jsp', 'htmlhelp.htm#flight_disruption_details_page.htm',0);

--changeSet DEV-1569:22 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/AccountDetails.jsp', 'htmlhelp.htm#account_details_page.htm',0);

--changeSet DEV-1569:23 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/TCodeDetails.jsp', 'htmlhelp.htm#T_code_details_page.htm',0);

--changeSet DEV-1569:24 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/TransactionDetails.jsp', 'htmlhelp.htm#transaction_details_page.htm',0);

--changeSet DEV-1569:25 stripComments:false
insert into utl_help values ('/maintenix/web/forecast/ForecastModelDetails.jsp', 'htmlhelp.htm#forecast_model_details_page.htm',0);

--changeSet DEV-1569:26 stripComments:false
insert into utl_help values ('/maintenix/web/ietm/IetmDetails.jsp', 'htmlhelp.htm#IETM_details_page.htm',0);

--changeSet DEV-1569:27 stripComments:false
insert into utl_help values ('/maintenix/web/ietm/IetmTopicDetails.jsp', 'htmlhelp.htm#IETM_topic_details_page.htm',0);

--changeSet DEV-1569:28 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/InventoryDetails.jsp', 'htmlhelp.htm#inventory_details_page.htm',0);

--changeSet DEV-1569:29 stripComments:false
insert into utl_help values ('/maintenix/web/licensedefn/LicenseDefnDetails.jsp', 'htmlhelp.htm#license_definition_details_page.htm',0);

--changeSet DEV-1569:30 stripComments:false
insert into utl_help values ('/maintenix/web/location/LocationDetails.jsp', 'htmlhelp.htm#location_details_page.htm',0);

--changeSet DEV-1569:31 stripComments:false
insert into utl_help values ('/maintenix/web/maint/MaintProgramDetails.jsp', 'htmlhelp.htm#maintenance_program_details_page.htm',0);

--changeSet DEV-1569:32 stripComments:false
insert into utl_help values ('/maintenix/web/manufacturer/ManufacturerDetails.jsp', 'htmlhelp.htm#manufacturer_details_page.htm',0);

--changeSet DEV-1569:33 stripComments:false
insert into utl_help values ('/maintenix/web/menu/MenuGroupDetails.jsp', 'htmlhelp.htm#menu_details_page.htm',0);

--changeSet DEV-1569:34 stripComments:false
insert into utl_help values ('/maintenix/web/org/OrganizationDetails.jsp', 'htmlhelp.htm#organizations_details_page.htm',0);

--changeSet DEV-1569:35 stripComments:false
insert into utl_help values ('/maintenix/web/owner/OwnerDetails.jsp', 'htmlhelp.htm#owner_details_page.htm',0);

--changeSet DEV-1569:36 stripComments:false
insert into utl_help values ('/maintenix/web/panel/PanelDetails.jsp', 'htmlhelp.htm#panel_details_page.htm',0);

--changeSet DEV-1569:37 stripComments:false
insert into utl_help values ('/maintenix/web/part/BOMPartDetails.jsp', 'htmlhelp.htm#part_group_details_page.htm',0);

--changeSet DEV-1569:38 stripComments:false
insert into utl_help values ('/maintenix/web/part/ExchangeVendorDetails.jsp', 'htmlhelp.htm#exchange_vendor_details_page.htm',0);

--changeSet DEV-1569:39 stripComments:false
insert into utl_help values ('/maintenix/web/part/PartDetails.jsp', 'htmlhelp.htm#part_details_page.htm',0);

--changeSet DEV-1569:40 stripComments:false
insert into utl_help values ('/maintenix/web/pi/POInvoiceDetails.jsp', 'htmlhelp.htm#po_invoice_details_page.htm',0);

--changeSet DEV-1569:41 stripComments:false
insert into utl_help values ('/maintenix/web/po/OrderDetails.jsp', 'htmlhelp.htm#purchase_order_details_page.htm',0);

--changeSet DEV-1569:42 stripComments:false
insert into utl_help values ('/maintenix/web/quarantine/QuarantineDetails.jsp', 'htmlhelp.htm#quarantine_details_page.htm',0);

--changeSet DEV-1569:43 stripComments:false
insert into utl_help values ('/maintenix/web/req/PartRequestDetails.jsp', 'htmlhelp.htm#part_request_details_page.htm',0);

--changeSet DEV-1569:44 stripComments:false
insert into utl_help values ('/maintenix/web/rfq/RFQDetails.jsp', 'htmlhelp.htm#rfq_details_page.htm',0);

--changeSet DEV-1569:45 stripComments:false
insert into utl_help values ('/maintenix/web/role/RoleDetails.jsp', 'htmlhelp.htm#role_details_page.htm',0);

--changeSet DEV-1569:46 stripComments:false
insert into utl_help values ('/maintenix/web/shift/CapacityPatternDetails.jsp', 'htmlhelp.htm#capacity_pattern_details_page.htm',0);

--changeSet DEV-1569:47 stripComments:false
insert into utl_help values ('/maintenix/web/shift/usershiftpattern/UserShiftPatternDetails.jsp', 'htmlhelp.htm#user_shift_pattern_details_page.htm',0);

--changeSet DEV-1569:48 stripComments:false
insert into utl_help values ('/maintenix/web/shipment/ShipmentDetails.jsp', 'htmlhelp.htm#shipment_details_page.htm',0);

--changeSet DEV-1569:49 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/StatusBoardDetails.jsp', 'htmlhelp.htm#status_board_details_page.htm',0);

--changeSet DEV-1569:50 stripComments:false
insert into utl_help values ('/maintenix/web/stock/StockDetails.jsp', 'htmlhelp.htm#stock_details_page.htm',0);

--changeSet DEV-1569:51 stripComments:false
insert into utl_help values ('/maintenix/web/tag/TagDetails.jsp', 'htmlhelp.htm#tag_details_page.htm',0);

--changeSet DEV-1569:52 stripComments:false
insert into utl_help values ('/maintenix/web/task/CheckDetails.jsp', 'htmlhelp.htm#work_package_details_page.htm',0);

--changeSet DEV-1569:53 stripComments:false
insert into utl_help values ('/maintenix/web/task/TaskDetails.jsp', 'htmlhelp.htm#task_details_page.htm',0);

--changeSet DEV-1569:54 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/BlockDetails.jsp', 'htmlhelp.htm#block_details_page.htm',0);

--changeSet DEV-1569:55 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/JICDetails.jsp', 'htmlhelp.htm#job_card_details_page.htm',0);

--changeSet DEV-1569:56 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/MPCDetails.jsp', 'htmlhelp.htm#master_panel_details_page.htm',0);

--changeSet DEV-1569:57 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/MPCSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-1569:58 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/RefDocDetails.jsp', 'htmlhelp.htm#reference_doc_details_page.htm',0);

--changeSet DEV-1569:59 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/ReqDetails.jsp', 'htmlhelp.htm#requirement_details_page.htm',0);

--changeSet DEV-1569:60 stripComments:false
insert into utl_help values ('/maintenix/web/transfer/TransferDetails.jsp', 'htmlhelp.htm#transfer_details_page.htm',0);

--changeSet DEV-1569:61 stripComments:false
insert into utl_help values ('/maintenix/web/usage/UsageRecordDetails.jsp', 'htmlhelp.htm#usage_record_details_page.htm',0);

--changeSet DEV-1569:62 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/UsageDefinitionDetails.jsp', 'htmlhelp.htm#usage_definition_details_page.htm',0);

--changeSet DEV-1569:63 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/usageparm/UsageParameterDetails.jsp', 'htmlhelp.htm#parameter_details_pages.htm',0);

--changeSet DEV-1569:64 stripComments:false
insert into utl_help values ('/maintenix/web/user/UserDetails.jsp', 'htmlhelp.htm#user_details_page.htm',0);

--changeSet DEV-1569:65 stripComments:false
insert into utl_help values ('/maintenix/web/vendor/VendorDetails.jsp', 'htmlhelp.htm#vendor_details_page.htm',0);

--changeSet DEV-1569:66 stripComments:false
insert into utl_help values ('/maintenix/web/warranty/WarrantyContractDetails.jsp', 'htmlhelp.htm#warranty_contract_details_page.htm',0);

--changeSet DEV-1569:67 stripComments:false
insert into utl_help values ('/maintenix/web/workflow/ApprovalLevelDetails.jsp', 'htmlhelp.htm#approval_levels_details_page.htm',0);

--changeSet DEV-1569:68 stripComments:false
insert into utl_help values ('/maintenix/web/workflow/AppWorkflowDefnDetails.jsp', 'htmlhelp.htm#approvalworkflows_details_page.htm',0);

--changeSet DEV-1569:69 stripComments:false
insert into utl_help values ('/maintenix/common/integration/MessageDetails.jsp', 'htmlhelp.htm#message_details_page.htm',0);

--changeSet DEV-1569:70 stripComments:false
insert into utl_help values ('/maintenix/common/job/JobViewer.jsp', 'htmlhelp.htm#Mx_Jobs.htm',0);

--changeSet DEV-1569:71 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabAssemblyList', 'htmlhelp.htm#assembly_list_tab.htm',0);

--changeSet DEV-1569:72 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabAssignedWorkList', 'htmlhelp.htm#assigned_work_list_tab.htm',0);

--changeSet DEV-1569:73 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabCapacityPatternSetup', 'htmlhelp.htm#capacity_pattern_setup_tab.htm',0);

--changeSet DEV-1569:74 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabCapacitySummary', 'htmlhelp.htm#capacity_summary_tab.htm',0);

--changeSet DEV-1569:75 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabCondemned', 'htmlhelp.htm#condemned_tab.htm',0);

--changeSet DEV-1569:76 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabDeferralReference', 'htmlhelp.htm#deferral_references_tab.htm',0);

--changeSet DEV-1569:77 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabExtractionRuleList', 'htmlhelp.htm#extraction_rules_tab.htm',0);

--changeSet DEV-1569:78 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabFaultThreshold', 'htmlhelp.htm#fault_thresholds_tab.htm',0);

--changeSet DEV-1569:79 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabHighOilConsumption', 'htmlhelp.htm#high_oil_consumption_tab.htm',0);

--changeSet DEV-1569:80 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabInboundShipments', 'htmlhelp.htm#inbound_shipments_tab.htm',0);

--changeSet DEV-1569:81 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabIncompleteKits', 'htmlhelp.htm#incomplete_kits_tab.htm',0);

--changeSet DEV-1569:82 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabInspectionRequired', 'htmlhelp.htm#inspection_required_tab.htm',0);

--changeSet DEV-1569:83 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabLooseInventoryDue', 'htmlhelp.htm#loose_inventory_due_tab.htm',0);

--changeSet DEV-1569:84 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabMyAlertList', 'htmlhelp.htm#my_alert_list_tab.htm',0);

--changeSet DEV-1569:85 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabMyOpenOrders', 'htmlhelp.htm#my_open_orders_tab.htm',0);

--changeSet DEV-1569:86 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabOpenPartRequests', 'htmlhelp.htm#open_part_requests_tab.htm',0);

--changeSet DEV-1569:87 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabOutboundShipments', 'htmlhelp.htm#outbound_shipments_tab.htm',0);

--changeSet DEV-1569:88 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabOverCompleteKits', 'htmlhelp.htm#over_complete_kits_tab.htm',0);

--changeSet DEV-1569:89 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabPlanningTypes', 'htmlhelp.htm#planning_types_tab.htm',0);

--changeSet DEV-1569:90 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabQuarantine', 'htmlhelp.htm#quarantine_tab.htm',0);

--changeSet DEV-1569:91 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabQuarantineCorrectiveActions', 'htmlhelp.htm#quarantine_corrective_actions_tab.htm',0);

--changeSet DEV-1569:92 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabShiftSchedule', 'htmlhelp.htm#shift_schedule_tab.htm',0);

--changeSet DEV-1569:93 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabShiftSetup', 'htmlhelp.htm#shift_setup_tab.htm',0);

--changeSet DEV-1569:94 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabStockTransfer', 'htmlhelp.htm#stock_transfer_tab.htm',0);

--changeSet DEV-1569:95 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabTurnIn', 'htmlhelp.htm#turn_in_tab.htm',0);

--changeSet DEV-1569:96 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabUnAssignedAlertList', 'htmlhelp.htm#unassigned_alert_list_tab.htm',0);

--changeSet DEV-1569:97 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabUnserviceableStaging', 'htmlhelp.htm#unserviceable_staging_tab.htm',0);

--changeSet DEV-1569:98 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabUserShiftPatternSetup', 'htmlhelp.htm#user_shift_pattern_setup_tab.htm',0);

--changeSet DEV-1569:99 stripComments:false
insert into utl_help values ('/maintenix/common/ Entities.jsp', 'htmlhelp.htm#barcode_search_results_page.htm',0);

--changeSet DEV-1569:100 stripComments:false
-- Update config parm modified_in values to match zero level values
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '8.0' WHERE PARM_NAME = 'ALLOW_ACTIVATING_TD_BEFORE_APPROVAL' AND PARM_TYPE = 'LOGIC';

--changeSet DEV-1569:101 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '7.2' WHERE PARM_NAME = 'ELA_USERNAME' AND PARM_TYPE = 'ELA';

--changeSet DEV-1569:102 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '7.5' WHERE PARM_NAME = 'HTTP_CACHE_DEBUG' AND PARM_TYPE = 'HTTP';

--changeSet DEV-1569:103 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '7.5' WHERE PARM_NAME = 'HTTP_CACHE_ENABLED' AND PARM_TYPE = 'HTTP';

--changeSet DEV-1569:104 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '7.5' WHERE PARM_NAME = 'HTTP_CACHE_EXCLUDE_URLS' AND PARM_TYPE = 'HTTP';

--changeSet DEV-1569:105 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '7.5' WHERE PARM_NAME = 'HTTP_CACHE_EXPIRATION' AND PARM_TYPE = 'HTTP';

--changeSet DEV-1569:106 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '7.5' WHERE PARM_NAME = 'HTTP_CACHE_INCLUDE_URLS' AND PARM_TYPE = 'HTTP';

--changeSet DEV-1569:107 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '8.0' WHERE PARM_NAME = 'HTTP_COMPRESSION_EXCLUDE_URLS' AND PARM_TYPE = 'HTTP';

--changeSet DEV-1569:108 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '8.0' WHERE PARM_NAME = 'SELECT_WINDOW_POPUP_MAX_ACCOUNTS' AND PARM_TYPE = 'MXWEB';

--changeSet DEV-1569:109 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '8.0' WHERE PARM_NAME = 'SHOW_COST' AND PARM_TYPE = 'SECURED_RESOURCE';

--changeSet DEV-1569:110 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '8.0' WHERE PARM_NAME = 'SUBCOMPONENTS_THRESHOLD_ON_INVENTORY_TASKS' AND PARM_TYPE = 'MXWEB';

--changeSet DEV-1569:111 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '8.0' WHERE PARM_NAME = 'TOBE_COMP_NOT_PART_COMPATIBLE' AND PARM_TYPE = 'LOGIC';

--changeSet DEV-1569:112 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '7.1-SP3' WHERE PARM_NAME = 'TOBE_INST_BATCH_INCORRECT_QTY' AND PARM_TYPE = 'LOGIC';

--changeSet DEV-1569:113 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '7.5' WHERE PARM_NAME = 'sOrderSearchOrgCodeByReceiptOrg' AND PARM_TYPE = 'SESSION';

--changeSet DEV-1569:114 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '8.0' WHERE PARM_NAME = 'ACTION_APPROVE_PART';

--changeSet DEV-1569:115 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.2' WHERE PARM_NAME = 'ACTION_ASSIGN_FAULT_TO_CHECK';

--changeSet DEV-1569:116 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.2' WHERE PARM_NAME = 'ACTION_ASSIGN_FAULT_TO_COMPLETED_CHECK';

--changeSet DEV-1569:117 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.2' WHERE PARM_NAME = 'ACTION_ASSIGN_FAULT_TO_COMPONENT_WORK_ORDER';

--changeSet DEV-1569:118 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '8.0' WHERE PARM_NAME = 'ACTION_ASSIGN_FLIGHT_MEASUREMENT';

--changeSet DEV-1569:119 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.2' WHERE PARM_NAME = 'ACTION_ASSIGN_TASK_TO_CHECK';

--changeSet DEV-1569:120 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.2' WHERE PARM_NAME = 'ACTION_ASSIGN_TASK_TO_COMPLETED_CHECK';

--changeSet DEV-1569:121 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.2' WHERE PARM_NAME = 'ACTION_ASSIGN_TASK_TO_COMPONENT_WORK_ORDER';

--changeSet DEV-1569:122 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.2' WHERE PARM_NAME = 'ACTION_ASSUME_CONTROL_OF_FAULT';

--changeSet DEV-1569:123 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '8.0' WHERE PARM_NAME = 'ACTION_COLLECT_JIC_HISTORIC';

--changeSet DEV-1569:124 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.5' WHERE PARM_NAME = 'ACTION_CREATE_FAULT_AGAINST_CHECK_OR_WO';

--changeSet DEV-1569:125 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.5' WHERE PARM_NAME = 'ACTION_CREATE_FAULT_AGAINST_TASK';

--changeSet DEV-1569:126 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '8.0 alpha' WHERE PARM_NAME = 'ACTION_EDIT_SIGNED_PART_REQUIREMENT';

--changeSet DEV-1569:127 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '8.0' WHERE PARM_NAME = 'ACTION_REF_DOC_LOCK';

--changeSet DEV-1569:128 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '8.0' WHERE PARM_NAME = 'ACTION_REF_DOC_UNLOCK';

--changeSet DEV-1569:129 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.2' WHERE PARM_NAME = 'ACTION_UNASSIGN_FAULT_FROM_CHECK';

--changeSet DEV-1569:130 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.2' WHERE PARM_NAME = 'ACTION_UNASSIGN_FAULT_FROM_COMPLETED_CHECK';

--changeSet DEV-1569:131 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.2' WHERE PARM_NAME = 'ACTION_UNASSIGN_FAULT_FROM_COMPONENT_WORK_ORDER';

--changeSet DEV-1569:132 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.2' WHERE PARM_NAME = 'ACTION_UNASSIGN_TASK_FROM_CHECK';

--changeSet DEV-1569:133 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.2' WHERE PARM_NAME = 'ACTION_UNASSIGN_TASK_FROM_COMPLETED_CHECK';

--changeSet DEV-1569:134 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '7.2' WHERE PARM_NAME = 'ACTION_UNASSIGN_TASK_FROM_COMPONENT_WORK_ORDER';

--changeSet DEV-1569:135 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET MODIFIED_IN = '8.0' WHERE PARM_NAME = 'ACTION_UNCOLLECT_JIC_HISTORIC';