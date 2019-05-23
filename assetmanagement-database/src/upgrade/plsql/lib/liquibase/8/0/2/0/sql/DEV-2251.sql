--liquibase formatted sql


--changeSet DEV-2251:1 stripComments:false
-- delete all 0level utl_help rows first
DELETE FROM utl_help where utl_help.utl_id = 0;

--changeSet DEV-2251:2 stripComments:false
-- INSERT SCRIPT FOR TABLE UTL_HELP
insert into utl_help values ('/maintenix/web/advisory/AdvisoryDetails.jsp', 'advisory_details_page.html',0);

--changeSet DEV-2251:3 stripComments:false
insert into utl_help values ('/maintenix/web/alert/AlertDetails.jsp', 'alert_details_page.html',0);

--changeSet DEV-2251:4 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/AssemblyDetails.jsp', 'assembly_details_page.html',0);

--changeSet DEV-2251:5 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/deferral/details/DeferRefDetails.jsp', 'deferral_reference_details_page.html',0);

--changeSet DEV-2251:6 stripComments:false
insert into utl_help values ('/maintenix/web/authority/AuthorityDetails.jsp', 'authority_details_page.html',0);

--changeSet DEV-2251:7 stripComments:false
insert into utl_help values ('/maintenix/web/bom/ConfigSlotDetails.jsp', 'config_slot_details_page.html',0);

--changeSet DEV-2251:8 stripComments:false
insert into utl_help values ('/maintenix/web/bom/faultthresholdFaultThresholdDetails.jsp', 'fault_threshold_details_page.html',0);

--changeSet DEV-2251:9 stripComments:false
insert into utl_help values ('/maintenix/web/capability/CapabilityDetails.jsp', 'capability_details_page.html',0);

--changeSet DEV-2251:10 stripComments:false
insert into utl_help values ('/maintenix/web/claim/ClaimDetails.jsp', 'claim_details_page.html',0);

--changeSet DEV-2251:11 stripComments:false
insert into utl_help values ('/maintenix/web/dbrulechecker/DatabaseRuleDetails.jsp', 'database_rule_details_page.html',0);

--changeSet DEV-2251:12 stripComments:false
insert into utl_help values ('/maintenix/web/department/DepartmentDetails.jsp', 'department_details_page.html',0);

--changeSet DEV-2251:13 stripComments:false
insert into utl_help values ('/maintenix/web/er/ExtractionRuleDetails.jsp', 'extraction_rules_details_page.html',0);

--changeSet DEV-2251:14 stripComments:false
insert into utl_help values ('/maintenix/web/event/CreateEditEvent.jsp', 'event_details_page.html',0);

--changeSet DEV-2251:15 stripComments:false
insert into utl_help values ('/maintenix/web/faileffect/FailEffectDetails.jsp', 'failure_effect_details_page.html',0);

--changeSet DEV-2251:16 stripComments:false
insert into utl_help values ('/maintenix/web/fault/PossibleFaultDetails.jsp', 'possible_fault_details_page.html',0);

--changeSet DEV-2251:17 stripComments:false
insert into utl_help values ('/maintenix/web/fault/deferral/DeferralReference.jsp', 'deferral_reference_details_page.html',0);

--changeSet DEV-2251:18 stripComments:false
insert into utl_help values ('/maintenix/web/faultdefn/FaultDefinitionDetails.jsp', 'fault_definition_details_page.html',0);

--changeSet DEV-2251:19 stripComments:false
insert into utl_help values ('/maintenix/web/flight/FlightDetails.jsp', 'flight_details_page.html',0);

--changeSet DEV-2251:20 stripComments:false
insert into utl_help values ('/maintenix/web/flight/MasterFlightDetails.jsp', 'master_flight_details_page.html',0);

--changeSet DEV-2251:21 stripComments:false
insert into utl_help values ('/maintenix/web/flightdisruption/FlightDisruptionDetails.jsp', 'flight_disruption_details_page.html',0);

--changeSet DEV-2251:22 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/AccountDetails.jsp', 'account_details_page.html',0);

--changeSet DEV-2251:23 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/TCodeDetails.jsp', 'T_code_details_page.html',0);

--changeSet DEV-2251:24 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/TransactionDetails.jsp', 'transaction_details_page.html',0);

--changeSet DEV-2251:25 stripComments:false
insert into utl_help values ('/maintenix/web/forecast/ForecastModelDetails.jsp', 'forecast_model_details_page.html',0);

--changeSet DEV-2251:26 stripComments:false
insert into utl_help values ('/maintenix/web/ietm/IetmDetails.jsp', 'IETM_details_page.html',0);

--changeSet DEV-2251:27 stripComments:false
insert into utl_help values ('/maintenix/web/ietm/IetmTopicDetails.jsp', 'IETM_topic_details_page.html',0);

--changeSet DEV-2251:28 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/InventoryDetails.jsp', 'inventory_details_page.html',0);

--changeSet DEV-2251:29 stripComments:false
insert into utl_help values ('/maintenix/web/licensedefn/LicenseDefnDetails.jsp', 'license_definition_details_page.html',0);

--changeSet DEV-2251:30 stripComments:false
insert into utl_help values ('/maintenix/web/location/LocationDetails.jsp', 'location_details_page.html',0);

--changeSet DEV-2251:31 stripComments:false
insert into utl_help values ('/maintenix/web/maint/MaintProgramDetails.jsp', 'maintenance_program_details_page.html',0);

--changeSet DEV-2251:32 stripComments:false
insert into utl_help values ('/maintenix/web/manufacturer/ManufacturerDetails.jsp', 'manufacturer_details_page.html',0);

--changeSet DEV-2251:33 stripComments:false
insert into utl_help values ('/maintenix/web/menu/MenuGroupDetails.jsp', 'menu_details_page.html',0);

--changeSet DEV-2251:34 stripComments:false
insert into utl_help values ('/maintenix/web/org/ManageSkills.jsp', 'manage_skills_page.html',0);

--changeSet DEV-2251:35 stripComments:false
insert into utl_help values ('/maintenix/web/org/OrganizationDetails.jsp', 'organizations_details_page.html',0);

--changeSet DEV-2251:36 stripComments:false
insert into utl_help values ('/maintenix/web/owner/OwnerDetails.jsp', 'owner_details_page.html',0);

--changeSet DEV-2251:37 stripComments:false
insert into utl_help values ('/maintenix/web/panel/PanelDetails.jsp', 'panel_details_page.html',0);

--changeSet DEV-2251:38 stripComments:false
insert into utl_help values ('/maintenix/web/part/BOMPartDetails.jsp', 'part_group_details_page.html',0);

--changeSet DEV-2251:39 stripComments:false
insert into utl_help values ('/maintenix/web/part/ExchangeVendorDetails.jsp', 'exchange_vendor_details_page.html',0);

--changeSet DEV-2251:40 stripComments:false
insert into utl_help values ('/maintenix/web/part/PartDetails.jsp', 'part_details_page.html',0);

--changeSet DEV-2251:41 stripComments:false
insert into utl_help values ('/maintenix/web/pi/POInvoiceDetails.jsp', 'po_invoice_details_page.html',0);

--changeSet DEV-2251:42 stripComments:false
insert into utl_help values ('/maintenix/web/po/OrderDetails.jsp', 'purchase_order_details_page.html',0);

--changeSet DEV-2251:43 stripComments:false
insert into utl_help values ('/maintenix/web/quarantine/QuarantineDetails.jsp', 'quarantine_details_page.html',0);

--changeSet DEV-2251:44 stripComments:false
insert into utl_help values ('/maintenix/web/req/PartRequestDetails.jsp', 'part_request_details_page.html',0);

--changeSet DEV-2251:45 stripComments:false
insert into utl_help values ('/maintenix/web/rfq/RFQDetails.jsp', 'rfq_details_page.html',0);

--changeSet DEV-2251:46 stripComments:false
insert into utl_help values ('/maintenix/web/role/RoleDetails.jsp', 'role_details_page.html',0);

--changeSet DEV-2251:47 stripComments:false
insert into utl_help values ('/maintenix/web/search/AdvancedSearch.jsp', 'advanced_search_page.html',0);

--changeSet DEV-2251:48 stripComments:false
insert into utl_help values ('/maintenix/web/shift/CapacityPatternDetails.jsp', 'capacity_pattern_details_page.html',0);

--changeSet DEV-2251:49 stripComments:false
insert into utl_help values ('/maintenix/web/shift/usershiftpattern/UserShiftPatternDetails.jsp', 'user_shift_pattern_details_page.html',0);

--changeSet DEV-2251:50 stripComments:false
insert into utl_help values ('/maintenix/web/shipment/ShipmentDetails.jsp', 'shipment_details_page.html',0);

--changeSet DEV-2251:51 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/StatusBoardDetails.jsp', 'status_board_details_page.html',0);

--changeSet DEV-2251:52 stripComments:false
insert into utl_help values ('/maintenix/web/stock/StockDetails.jsp', 'stock_details_page.html',0);

--changeSet DEV-2251:53 stripComments:false
insert into utl_help values ('/maintenix/web/tag/TagDetails.jsp', 'tag_details_page.html',0);

--changeSet DEV-2251:54 stripComments:false
insert into utl_help values ('/maintenix/web/task/CheckDetails.jsp', 'work_package_details_page.html',0);

--changeSet DEV-2251:55 stripComments:false
insert into utl_help values ('/maintenix/web/task/TaskDetails.jsp', 'task_details_page.html',0);

--changeSet DEV-2251:56 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/BlockDetails.jsp', 'block_details_page.html',0);

--changeSet DEV-2251:57 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/JICDetails.jsp', 'job_card_details_page.html',0);

--changeSet DEV-2251:58 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/MPCDetails.jsp', 'master_panel_details_page.html',0);

--changeSet DEV-2251:59 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/MPCSearchByType.jsp', 'performing_searches.html',0);

--changeSet DEV-2251:60 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/RefDocDetails.jsp', 'reference_doc_details_page.html',0);

--changeSet DEV-2251:61 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/ReqDetails.jsp', 'requirement_details_page.html',0);

--changeSet DEV-2251:62 stripComments:false
insert into utl_help values ('/maintenix/web/transfer/TransferDetails.jsp', 'transfer_details_page.html',0);

--changeSet DEV-2251:63 stripComments:false
insert into utl_help values ('/maintenix/web/usage/UsageRecordDetails.jsp', 'usage_record_details_page.html',0);

--changeSet DEV-2251:64 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/UsageDefinitionDetails.jsp', 'usage_definition_details_page.html',0);

--changeSet DEV-2251:65 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/usageparm/UsageParameterDetails.jsp', 'parameter_details_pages.html',0);

--changeSet DEV-2251:66 stripComments:false
insert into utl_help values ('/maintenix/web/user/UserDetails.jsp', 'user_details_page.html',0);

--changeSet DEV-2251:67 stripComments:false
insert into utl_help values ('/maintenix/web/vendor/VendorDetails.jsp', 'vendor_details_page.html',0);

--changeSet DEV-2251:68 stripComments:false
insert into utl_help values ('/maintenix/web/warranty/WarrantyContractDetails.jsp', 'warranty_contract_details_page.html',0);

--changeSet DEV-2251:69 stripComments:false
insert into utl_help values ('/maintenix/web/workflow/ApprovalLevelDetails.jsp', 'approval_levels_details_page.html',0);

--changeSet DEV-2251:70 stripComments:false
insert into utl_help values ('/maintenix/web/workflow/AppWorkflowDefnDetails.jsp', 'approval_workflows_details_page.html',0);

--changeSet DEV-2251:71 stripComments:false
insert into utl_help values ('/maintenix/ui/maint/planning/wpb/WorkPackageBuilder.jsp', 'Work_Package_Builder_Page.html',0);

--changeSet DEV-2251:72 stripComments:false
insert into utl_help values ('/maintenix/common/integration/MessageDetails.jsp', 'message_details_page.html',0);

--changeSet DEV-2251:73 stripComments:false
insert into utl_help values ('/maintenix/common/job/JobViewer.jsp', 'Mx_Jobs.html',0);

--changeSet DEV-2251:74 stripComments:false
insert into utl_help values ('/maintenix/common/ Entities.jsp', 'barcode_search_results_page.html',0);

--changeSet DEV-2251:75 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabAssemblyList', 'assembly_list_tab.html',0);

--changeSet DEV-2251:76 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabAssignedWorkList', 'assigned_work_list_tab.html',0);

--changeSet DEV-2251:77 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabCapacityPatternSetup', 'capacity_pattern_setup_tab.html',0);

--changeSet DEV-2251:78 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabCapacitySummary', 'capacity_summary_tab.html',0);

--changeSet DEV-2251:79 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabCondemned', 'condemned_tab.html',0);

--changeSet DEV-2251:80 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabDeferralReference', 'deferral_references_tab.html',0);

--changeSet DEV-2251:81 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabExtractionRuleList', 'extraction_rules_tab.html',0);

--changeSet DEV-2251:82 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabFaultThreshold', 'fault_thresholds_tab.html',0);

--changeSet DEV-2251:83 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabFleetDueList', 'fleet_due_list_tab.html',0);

--changeSet DEV-2251:84 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabFleetList', 'fleet_list_tab.html',0);

--changeSet DEV-2251:85 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabHighOilConsumption', 'high_oil_consumption_tab.html',0);

--changeSet DEV-2251:86 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabInboundShipments', 'inbound_shipments_tab.html',0);

--changeSet DEV-2251:87 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabIncompleteKits', 'incomplete_kits_tab.html',0);

--changeSet DEV-2251:88 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabInspectionRequired', 'inspection_required_tab.html',0);

--changeSet DEV-2251:89 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabLooseInventoryDue', 'loose_inventory_due_tab.html',0);

--changeSet DEV-2251:90 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabMyAlertList', 'my_alert_list_tab.html',0);

--changeSet DEV-2251:91 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabMyOpenOrders', 'my_open_orders_tab.html',0);

--changeSet DEV-2251:92 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabOpenPartRequests', 'open_part_requests_tab.html',0);

--changeSet DEV-2251:93 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabOutboundShipments', 'outbound_shipments_tab.html',0);

--changeSet DEV-2251:94 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabOverCompleteKits', 'over_complete_kits_tab.html',0);

--changeSet DEV-2251:95 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabPlanningTypes', 'planning_types_tab.html',0);

--changeSet DEV-2251:96 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabQuarantine', 'quarantine_tab.html',0);

--changeSet DEV-2251:97 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabQuarantineCorrectiveActions', 'quarantine_corrective_actions_tab.html',0);

--changeSet DEV-2251:98 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabShiftSchedule', 'shift_schedule_tab.html',0);

--changeSet DEV-2251:99 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabShiftSetup', 'shift_setup_tab.html',0);

--changeSet DEV-2251:100 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabStockTransfer', 'stock_transfer_tab.html',0);

--changeSet DEV-2251:101 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabTurnIn', 'turn_in_tab.html',0);

--changeSet DEV-2251:102 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabUnAssignedAlertList', 'unassigned_alert_list_tab.html',0);

--changeSet DEV-2251:103 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabUnserviceableStaging', 'unserviceable_staging_tab.html',0);

--changeSet DEV-2251:104 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabUserShiftPatternSetup', 'user_shift_pattern_setup_tab.html',0);