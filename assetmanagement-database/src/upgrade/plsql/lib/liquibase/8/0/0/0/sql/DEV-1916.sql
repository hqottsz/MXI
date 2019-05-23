--liquibase formatted sql


--changeSet DEV-1916:1 stripComments:false
-- delete all 0level utl_help rows first
DELETE FROM utl_help where utl_help.utl_id = 0;

--changeSet DEV-1916:2 stripComments:false
-- INSERT SCRIPT FOR TABLE UTL_HELP
insert into utl_help values ('/maintenix/web/advisory/AdvisoryDetails.jsp', 'htmlhelp.htm#advisory_details_page.htm',0);

--changeSet DEV-1916:3 stripComments:false
insert into utl_help values ('/maintenix/web/alert/AlertDetails.jsp', 'htmlhelp.htm#alert_details_page.htm',0);

--changeSet DEV-1916:4 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/AssemblyDetails.jsp', 'htmlhelp.htm#assembly_details_page.htm',0);

--changeSet DEV-1916:5 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/deferral/details/DeferRefDetails.jsp', 'htmlhelp.htm#deferral_reference_details_page.htm',0);

--changeSet DEV-1916:6 stripComments:false
insert into utl_help values ('/maintenix/web/authority/AuthorityDetails.jsp', 'htmlhelp.htm#authority_details_page.htm',0);

--changeSet DEV-1916:7 stripComments:false
insert into utl_help values ('/maintenix/web/bom/ConfigSlotDetails.jsp', 'htmlhelp.htm#config_slot_details_page.htm',0);

--changeSet DEV-1916:8 stripComments:false
insert into utl_help values ('/maintenix/web/bom/faultthresholdFaultThresholdDetails.jsp', 'htmlhelp.htm#fault_threshold_details_page.htm',0);

--changeSet DEV-1916:9 stripComments:false
insert into utl_help values ('/maintenix/web/capability/CapabilityDetails.jsp', 'htmlhelp.htm#capability_details_page.htm',0);

--changeSet DEV-1916:10 stripComments:false
insert into utl_help values ('/maintenix/web/claim/ClaimDetails.jsp', 'htmlhelp.htm#claims_details_page.htm',0);

--changeSet DEV-1916:11 stripComments:false
insert into utl_help values ('/maintenix/web/dbrulechecker/DatabaseRuleDetails.jsp', 'htmlhelp.htm#database_rule_details_page.htm',0);

--changeSet DEV-1916:12 stripComments:false
insert into utl_help values ('/maintenix/web/department/DepartmentDetails.jsp', 'htmlhelp.htm#department_details_page.htm',0);

--changeSet DEV-1916:13 stripComments:false
insert into utl_help values ('/maintenix/web/er/ExtractionRuleDetails.jsp', 'htmlhelp.htm#extraction_rules_details_page.htm',0);

--changeSet DEV-1916:14 stripComments:false
insert into utl_help values ('/maintenix/web/event/CreateEditEvent.jsp', 'htmlhelp.htm#event_details_page.htm',0);

--changeSet DEV-1916:15 stripComments:false
insert into utl_help values ('/maintenix/web/faileffect/FailEffectDetails.jsp', 'htmlhelp.htm#failure_effect_details_page.htm',0);

--changeSet DEV-1916:16 stripComments:false
insert into utl_help values ('/maintenix/web/fault/PossibleFaultDetails.jsp', 'htmlhelp.htm#possible_fault_details_page.htm',0);

--changeSet DEV-1916:17 stripComments:false
insert into utl_help values ('/maintenix/web/fault/deferral/DeferralReference.jsp', 'htmlhelp.htm#deferral_reference_details_page.htm',0);

--changeSet DEV-1916:18 stripComments:false
insert into utl_help values ('/maintenix/web/faultdefn/FaultDefinitionDetails.jsp', 'htmlhelp.htm#fault_definition_details_page.htm',0);

--changeSet DEV-1916:19 stripComments:false
insert into utl_help values ('/maintenix/web/flight/FlightDetails.jsp', 'htmlhelp.htm#flight_details_page.htm',0);

--changeSet DEV-1916:20 stripComments:false
insert into utl_help values ('/maintenix/web/flight/MasterFlightDetails.jsp', 'htmlhelp.htm#master_flight_details_page.htm',0);

--changeSet DEV-1916:21 stripComments:false
insert into utl_help values ('/maintenix/web/flightdisruption/FlightDisruptionDetails.jsp', 'htmlhelp.htm#flight_disruption_details_page.htm',0);

--changeSet DEV-1916:22 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/AccountDetails.jsp', 'htmlhelp.htm#account_details_page.htm',0);

--changeSet DEV-1916:23 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/TCodeDetails.jsp', 'htmlhelp.htm#T_code_details_page.htm',0);

--changeSet DEV-1916:24 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/TransactionDetails.jsp', 'htmlhelp.htm#transaction_details_page.htm',0);

--changeSet DEV-1916:25 stripComments:false
insert into utl_help values ('/maintenix/web/forecast/ForecastModelDetails.jsp', 'htmlhelp.htm#forecast_model_details_page.htm',0);

--changeSet DEV-1916:26 stripComments:false
insert into utl_help values ('/maintenix/web/ietm/IetmDetails.jsp', 'htmlhelp.htm#IETM_details_page.htm',0);

--changeSet DEV-1916:27 stripComments:false
insert into utl_help values ('/maintenix/web/ietm/IetmTopicDetails.jsp', 'htmlhelp.htm#IETM_topic_details_page.htm',0);

--changeSet DEV-1916:28 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/InventoryDetails.jsp', 'htmlhelp.htm#inventory_details_page.htm',0);

--changeSet DEV-1916:29 stripComments:false
insert into utl_help values ('/maintenix/web/licensedefn/LicenseDefnDetails.jsp', 'htmlhelp.htm#license_definition_details_page.htm',0);

--changeSet DEV-1916:30 stripComments:false
insert into utl_help values ('/maintenix/web/location/LocationDetails.jsp', 'htmlhelp.htm#location_details_page.htm',0);

--changeSet DEV-1916:31 stripComments:false
insert into utl_help values ('/maintenix/web/maint/MaintProgramDetails.jsp', 'htmlhelp.htm#maintenance_program_details_page.htm',0);

--changeSet DEV-1916:32 stripComments:false
insert into utl_help values ('/maintenix/web/manufacturer/ManufacturerDetails.jsp', 'htmlhelp.htm#manufacturer_details_page.htm',0);

--changeSet DEV-1916:33 stripComments:false
insert into utl_help values ('/maintenix/web/menu/MenuGroupDetails.jsp', 'htmlhelp.htm#menu_details_page.htm',0);

--changeSet DEV-1916:34 stripComments:false
insert into utl_help values ('/maintenix/web/org/OrganizationDetails.jsp', 'htmlhelp.htm#organizations_details_page.htm',0);

--changeSet DEV-1916:35 stripComments:false
insert into utl_help values ('/maintenix/web/owner/OwnerDetails.jsp', 'htmlhelp.htm#owner_details_page.htm',0);

--changeSet DEV-1916:36 stripComments:false
insert into utl_help values ('/maintenix/web/panel/PanelDetails.jsp', 'htmlhelp.htm#panel_details_page.htm',0);

--changeSet DEV-1916:37 stripComments:false
insert into utl_help values ('/maintenix/web/part/BOMPartDetails.jsp', 'htmlhelp.htm#part_group_details_page.htm',0);

--changeSet DEV-1916:38 stripComments:false
insert into utl_help values ('/maintenix/web/part/ExchangeVendorDetails.jsp', 'htmlhelp.htm#exchange_vendor_details_page.htm',0);

--changeSet DEV-1916:39 stripComments:false
insert into utl_help values ('/maintenix/web/part/PartDetails.jsp', 'htmlhelp.htm#part_details_page.htm',0);

--changeSet DEV-1916:40 stripComments:false
insert into utl_help values ('/maintenix/web/pi/POInvoiceDetails.jsp', 'htmlhelp.htm#po_invoice_details_page.htm',0);

--changeSet DEV-1916:41 stripComments:false
insert into utl_help values ('/maintenix/web/po/OrderDetails.jsp', 'htmlhelp.htm#purchase_order_details_page.htm',0);

--changeSet DEV-1916:42 stripComments:false
insert into utl_help values ('/maintenix/web/quarantine/QuarantineDetails.jsp', 'htmlhelp.htm#quarantine_details_page.htm',0);

--changeSet DEV-1916:43 stripComments:false
insert into utl_help values ('/maintenix/web/req/PartRequestDetails.jsp', 'htmlhelp.htm#part_request_details_page.htm',0);

--changeSet DEV-1916:44 stripComments:false
insert into utl_help values ('/maintenix/web/rfq/RFQDetails.jsp', 'htmlhelp.htm#rfq_details_page.htm',0);

--changeSet DEV-1916:45 stripComments:false
insert into utl_help values ('/maintenix/web/role/RoleDetails.jsp', 'htmlhelp.htm#role_details_page.htm',0);

--changeSet DEV-1916:46 stripComments:false
insert into utl_help values ('/maintenix/web/shift/CapacityPatternDetails.jsp', 'htmlhelp.htm#capacity_pattern_details_page.htm',0);

--changeSet DEV-1916:47 stripComments:false
insert into utl_help values ('/maintenix/web/shift/usershiftpattern/UserShiftPatternDetails.jsp', 'htmlhelp.htm#user_shift_pattern_details_page.htm',0);

--changeSet DEV-1916:48 stripComments:false
insert into utl_help values ('/maintenix/web/shipment/ShipmentDetails.jsp', 'htmlhelp.htm#shipment_details_page.htm',0);

--changeSet DEV-1916:49 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/StatusBoardDetails.jsp', 'htmlhelp.htm#status_board_details_page.htm',0);

--changeSet DEV-1916:50 stripComments:false
insert into utl_help values ('/maintenix/web/stock/StockDetails.jsp', 'htmlhelp.htm#stock_details_page.htm',0);

--changeSet DEV-1916:51 stripComments:false
insert into utl_help values ('/maintenix/web/tag/TagDetails.jsp', 'htmlhelp.htm#tag_details_page.htm',0);

--changeSet DEV-1916:52 stripComments:false
insert into utl_help values ('/maintenix/web/task/CheckDetails.jsp', 'htmlhelp.htm#work_package_details_page.htm',0);

--changeSet DEV-1916:53 stripComments:false
insert into utl_help values ('/maintenix/web/task/TaskDetails.jsp', 'htmlhelp.htm#task_details_page.htm',0);

--changeSet DEV-1916:54 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/BlockDetails.jsp', 'htmlhelp.htm#block_details_page.htm',0);

--changeSet DEV-1916:55 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/JICDetails.jsp', 'htmlhelp.htm#job_card_details_page.htm',0);

--changeSet DEV-1916:56 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/MPCDetails.jsp', 'htmlhelp.htm#master_panel_details_page.htm',0);

--changeSet DEV-1916:57 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/MPCSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-1916:58 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/RefDocDetails.jsp', 'htmlhelp.htm#reference_doc_details_page.htm',0);

--changeSet DEV-1916:59 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/ReqDetails.jsp', 'htmlhelp.htm#requirement_details_page.htm',0);

--changeSet DEV-1916:60 stripComments:false
insert into utl_help values ('/maintenix/web/transfer/TransferDetails.jsp', 'htmlhelp.htm#transfer_details_page.htm',0);

--changeSet DEV-1916:61 stripComments:false
insert into utl_help values ('/maintenix/web/usage/UsageRecordDetails.jsp', 'htmlhelp.htm#usage_record_details_page.htm',0);

--changeSet DEV-1916:62 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/UsageDefinitionDetails.jsp', 'htmlhelp.htm#usage_definition_details_page.htm',0);

--changeSet DEV-1916:63 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/usageparm/UsageParameterDetails.jsp', 'htmlhelp.htm#parameter_details_pages.htm',0);

--changeSet DEV-1916:64 stripComments:false
insert into utl_help values ('/maintenix/web/user/UserDetails.jsp', 'htmlhelp.htm#user_details_page.htm',0);

--changeSet DEV-1916:65 stripComments:false
insert into utl_help values ('/maintenix/web/vendor/VendorDetails.jsp', 'htmlhelp.htm#vendor_details_page.htm',0);

--changeSet DEV-1916:66 stripComments:false
insert into utl_help values ('/maintenix/web/warranty/WarrantyContractDetails.jsp', 'htmlhelp.htm#warranty_contract_details_page.htm',0);

--changeSet DEV-1916:67 stripComments:false
insert into utl_help values ('/maintenix/web/workflow/ApprovalLevelDetails.jsp', 'htmlhelp.htm#approval_levels_details_page.htm',0);

--changeSet DEV-1916:68 stripComments:false
insert into utl_help values ('/maintenix/web/workflow/AppWorkflowDefnDetails.jsp', 'htmlhelp.htm#approvalworkflows_details_page.htm',0);

--changeSet DEV-1916:69 stripComments:false
insert into utl_help values ('/maintenix/common/integration/MessageDetails.jsp', 'htmlhelp.htm#message_details_page.htm',0);

--changeSet DEV-1916:70 stripComments:false
insert into utl_help values ('/maintenix/common/job/JobViewer.jsp', 'htmlhelp.htm#Mx_Jobs.htm',0);

--changeSet DEV-1916:71 stripComments:false
insert into utl_help values ('/maintenix/common/ Entities.jsp', 'htmlhelp.htm#barcode_search_results_page.htm',0);

--changeSet DEV-1916:72 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabAssemblyList', 'htmlhelp.htm#assembly_list_tab.htm',0);

--changeSet DEV-1916:73 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabAssignedWorkList', 'htmlhelp.htm#assigned_work_list_tab.htm',0);

--changeSet DEV-1916:74 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabCapacityPatternSetup', 'htmlhelp.htm#capacity_pattern_setup_tab.htm',0);

--changeSet DEV-1916:75 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabCapacitySummary', 'htmlhelp.htm#capacity_summary_tab.htm',0);

--changeSet DEV-1916:76 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabCondemned', 'htmlhelp.htm#condemned_tab.htm',0);

--changeSet DEV-1916:77 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabDeferralReference', 'htmlhelp.htm#deferral_references_tab.htm',0);

--changeSet DEV-1916:78 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabExtractionRuleList', 'htmlhelp.htm#extraction_rules_tab.htm',0);

--changeSet DEV-1916:79 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabFaultThreshold', 'htmlhelp.htm#fault_thresholds_tab.htm',0);

--changeSet DEV-1916:80 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabFleetDueList', 'htmlhelp.htm#fleet_due_list_tab.htm',0);

--changeSet DEV-1916:81 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabFleetList', 'htmlhelp.htm#fleet_list_tab.htm',0);

--changeSet DEV-1916:82 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabHighOilConsumption', 'htmlhelp.htm#high_oil_consumption_tab.htm',0);

--changeSet DEV-1916:83 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabInboundShipments', 'htmlhelp.htm#inbound_shipments_tab.htm',0);

--changeSet DEV-1916:84 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabIncompleteKits', 'htmlhelp.htm#incomplete_kits_tab.htm',0);

--changeSet DEV-1916:85 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabInspectionRequired', 'htmlhelp.htm#inspection_required_tab.htm',0);

--changeSet DEV-1916:86 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabLooseInventoryDue', 'htmlhelp.htm#loose_inventory_due_tab.htm',0);

--changeSet DEV-1916:87 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabMyAlertList', 'htmlhelp.htm#my_alert_list_tab.htm',0);

--changeSet DEV-1916:88 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabMyOpenOrders', 'htmlhelp.htm#my_open_orders_tab.htm',0);

--changeSet DEV-1916:89 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabOpenPartRequests', 'htmlhelp.htm#open_part_requests_tab.htm',0);

--changeSet DEV-1916:90 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabOutboundShipments', 'htmlhelp.htm#outbound_shipments_tab.htm',0);

--changeSet DEV-1916:91 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabOverCompleteKits', 'htmlhelp.htm#over_complete_kits_tab.htm',0);

--changeSet DEV-1916:92 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabPlanningTypes', 'htmlhelp.htm#planning_types_tab.htm',0);

--changeSet DEV-1916:93 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabQuarantine', 'htmlhelp.htm#quarantine_tab.htm',0);

--changeSet DEV-1916:94 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabQuarantineCorrectiveActions', 'htmlhelp.htm#quarantine_corrective_actions_tab.htm',0);

--changeSet DEV-1916:95 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabShiftSchedule', 'htmlhelp.htm#shift_schedule_tab.htm',0);

--changeSet DEV-1916:96 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabShiftSetup', 'htmlhelp.htm#shift_setup_tab.htm',0);

--changeSet DEV-1916:97 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabStockTransfer', 'htmlhelp.htm#stock_transfer_tab.htm',0);

--changeSet DEV-1916:98 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabTurnIn', 'htmlhelp.htm#turn_in_tab.htm',0);

--changeSet DEV-1916:99 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabUnAssignedAlertList', 'htmlhelp.htm#unassigned_alert_list_tab.htm',0);

--changeSet DEV-1916:100 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabUnserviceableStaging', 'htmlhelp.htm#unserviceable_staging_tab.htm',0);

--changeSet DEV-1916:101 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabUserShiftPatternSetup', 'htmlhelp.htm#user_shift_pattern_setup_tab.htm',0);