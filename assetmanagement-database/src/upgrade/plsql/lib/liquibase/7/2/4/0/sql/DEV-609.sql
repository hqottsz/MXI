--liquibase formatted sql


--changeSet DEV-609:1 stripComments:false
-- delete all 0level utl_help rows first
DELETE FROM utl_help where utl_help.utl_id = 0;

--changeSet DEV-609:2 stripComments:false
-- INSERT SCRIPT FOR TABLE UTL_HELP
insert into utl_help values ('/maintenix/web/advisory/AddEditAdvisory.jsp', 'htmlhelp.htm#ME_Part_Numbers.htm#proc-add_advisory_part',0);

--changeSet DEV-609:3 stripComments:false
insert into utl_help values ('/maintenix/web/advisory/AdvisoryDetails.jsp', 'htmlhelp.htm#advisory_details_page.htm',0);

--changeSet DEV-609:4 stripComments:false
insert into utl_help values ('/maintenix/web/advisory/ClearAdvisory.jsp', 'htmlhelp.htm#ME_Part_Numbers.htm#proc-clear_advisory_parts',0);

--changeSet DEV-609:5 stripComments:false
insert into utl_help values ('/maintenix/web/advisory/OutstandingAdvsrySearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:6 stripComments:false
insert into utl_help values ('/maintenix/web/alert/AddNote.jsp', 'htmlhelp.htm#user_alerts.htm#proc-add_note_to_alert',0);

--changeSet DEV-609:7 stripComments:false
insert into utl_help values ('/maintenix/web/alert/AlertDetails.jsp', 'htmlhelp.htm#alert_details_page.htm',0);

--changeSet DEV-609:8 stripComments:false
insert into utl_help values ('/maintenix/web/alert/AlertSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:9 stripComments:false
insert into utl_help values ('/maintenix/web/alert/AlertSetup.jsp', 'htmlhelp.htm#alert_setup_page.htm',0);

--changeSet DEV-609:10 stripComments:false
insert into utl_help values ('/maintenix/web/alert/DispositionAlert.jsp', 'htmlhelp.htm#user_alerts.htm#proc-disposition_alert',0);

--changeSet DEV-609:11 stripComments:false
insert into utl_help values ('/maintenix/web/alert/EditRoleAssignments.jsp', 'htmlhelp.htm#Setting_Up_Alerts.htm#proc-edit_alert_roles',0);

--changeSet DEV-609:12 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/AssemblyDetails.jsp', 'htmlhelp.htm#assembly_details_page.htm',0);

--changeSet DEV-609:13 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/deferral/details/DeferRefDetails.jsp', 'htmlhelp.htm#deferral_reference_details_page.htm',0);

--changeSet DEV-609:14 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/measurements/CreateEditMeasurement.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-create_measurement_parameter',0);

--changeSet DEV-609:15 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/measurements/MeasurementSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:16 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/measurements/ReorderMeasurements.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-reorder_measurement_parameter',0);

--changeSet DEV-609:17 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/oilconsumption/AddOperatorSpecificThreshold.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-add_operator_specific_threshold',0);

--changeSet DEV-609:18 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/oilconsumption/AddPartNoSpecificThreshold.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-add_part_specific_threshold',0);

--changeSet DEV-609:19 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/oilconsumption/EditAssemblyThreshold.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-edit_assembly_thresholds',0);

--changeSet DEV-609:20 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/oilconsumption/EditOperatorSpecificThreshold.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-edit_operator_specific_threshold',0);

--changeSet DEV-609:21 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/oilconsumption/EditPartNoSpecificThreshold.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-edit_part_no_specific_threshold',0);

--changeSet DEV-609:22 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/oilconsumption/EditSerialNoSpecificThreshold.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-edit_serial_no_specific_threshold',0);

--changeSet DEV-609:23 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/oilconsumption/OilConsumptionRateDefinition.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-add_or_edit_oil_consumption_rate_defn',0);

--changeSet DEV-609:24 stripComments:false
insert into utl_help values ('/maintenix/web/assembly/subtypes/CreateEditSubtype.jsp', 'htmlhelp.htm#HM_LRP.htm#proc-create_ass_subtype',0);

--changeSet DEV-609:25 stripComments:false
insert into utl_help values ('/maintenix/web/attach/AddAttachment.jsp', 'htmlhelp.htm#M_Executing_Maintenance_Tasks.htm#proc-add_attachment',0);

--changeSet DEV-609:26 stripComments:false
insert into utl_help values ('/maintenix/web/authority/AddFailurePriority.jsp', 'htmlhelp.htm#OR_Authorities.htm#proc-assign_fail_priority_to_auth',0);

--changeSet DEV-609:27 stripComments:false
insert into utl_help values ('/maintenix/web/authority/AuthorityDetails.jsp', 'htmlhelp.htm#authority_details_page.htm',0);

--changeSet DEV-609:28 stripComments:false
insert into utl_help values ('/maintenix/web/authority/AuthoritySearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:29 stripComments:false
insert into utl_help values ('/maintenix/web/authority/CreateEditAuthority.jsp', 'htmlhelp.htm#OR_Authorities.htm#proc-create_authority',0);

--changeSet DEV-609:30 stripComments:false
insert into utl_help values ('/maintenix/web/authority/EditFailurePriority.jsp', 'htmlhelp.htm#OR_Authorities.htm#proc-assign_fail_priority_to_auth',0);

--changeSet DEV-609:31 stripComments:false
insert into utl_help values ('/maintenix/web/bom/ConfigSlotDetails.jsp', 'htmlhelp.htm#config_slot_details_page.htm',0);

--changeSet DEV-609:32 stripComments:false
insert into utl_help values ('/maintenix/web/bom/CreateEditAssembly.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-create_assembly',0);

--changeSet DEV-609:33 stripComments:false
insert into utl_help values ('/maintenix/web/bom/CreateEditBomPart.jsp', 'htmlhelp.htm#ME_Configuration_Slots.htm#proc-create_part_group',0);

--changeSet DEV-609:34 stripComments:false
insert into utl_help values ('/maintenix/web/bom/CreateEditConfigSlot.jsp', 'htmlhelp.htm#ME_Configuration_Slots.htm#proc-create_config_slot',0);

--changeSet DEV-609:35 stripComments:false
insert into utl_help values ('/maintenix/web/bom/EditAssignedPartDetails.jsp', 'htmlhelp.htm#ME_Configuration_Slots.htm#proc-edit_assigned_part',0);

--changeSet DEV-609:36 stripComments:false
insert into utl_help values ('/maintenix/web/bom/EditPartGroupApplicability.jsp', 'htmlhelp.htm#ME_Configuration_Slots.htm#proc-edit_part_group_applicability',0);

--changeSet DEV-609:37 stripComments:false
insert into utl_help values ('/maintenix/web/bom/EditPositions.jsp', 'htmlhelp.htm#ME_Configuration_Slots.htm#proc-edit_positions_config_slot',0);

--changeSet DEV-609:38 stripComments:false
insert into utl_help values ('/maintenix/web/bom/faultthresholdFaultThresholdDetails.jsp', 'htmlhelp.htm#fault_threshold_details_page.htm',0);

--changeSet DEV-609:39 stripComments:false
insert into utl_help values ('/maintenix/web/capability/CapabilityDetails.jsp', 'htmlhelp.htm#capability_details_page.htm',0);

--changeSet DEV-609:40 stripComments:false
insert into utl_help values ('/maintenix/web/capability/CapabilitySearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:41 stripComments:false
insert into utl_help values ('/maintenix/web/claim/CancelClaim.jsp', 'htmlhelp.htm#F_Warranty_Contracts.htm#proc-cancel_claim',0);

--changeSet DEV-609:42 stripComments:false
insert into utl_help values ('/maintenix/web/claim/ClaimDetails.jsp', 'htmlhelp.htm#claims_details_page.htm',0);

--changeSet DEV-609:43 stripComments:false
insert into utl_help values ('/maintenix/web/claim/ClaimSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:44 stripComments:false
insert into utl_help values ('/maintenix/web/claim/CloseClaim.jsp', 'htmlhelp.htm#F_Warranty_Contracts.htm#proc-close_claim',0);

--changeSet DEV-609:45 stripComments:false
insert into utl_help values ('/maintenix/web/claim/CreateEditClaim.jsp', 'htmlhelp.htm#F_Warranty_Contracts.htm#proc-create_claim',0);

--changeSet DEV-609:46 stripComments:false
insert into utl_help values ('/maintenix/web/claim/EditClaimLaborLineNote.jsp', 'htmlhelp.htm#F_Warranty_Contracts.htm',0);

--changeSet DEV-609:47 stripComments:false
insert into utl_help values ('/maintenix/web/claim/EditClaimPartLineNote.jsp', 'htmlhelp.htm#F_Warranty_Contracts.htm',0);

--changeSet DEV-609:48 stripComments:false
insert into utl_help values ('/maintenix/web/claim/EditClaimPartLines.jsp', 'htmlhelp.htm#F_Warranty_Contracts.htm',0);

--changeSet DEV-609:49 stripComments:false
insert into utl_help values ('/maintenix/web/cyclecount/LoadCycleCountResults.jsp', 'htmlhelp.htm#MM_counting.htm#proc-load_cycle_count',0);

--changeSet DEV-609:50 stripComments:false
insert into utl_help values ('/maintenix/web/cyclecount/RecountResults.jsp', 'htmlhelp.htm#MM_counting.htm#proc-view_recount_results',0);

--changeSet DEV-609:51 stripComments:false
insert into utl_help values ('/maintenix/web/dbrulechecker/CreateEditDatabaseRule.jsp', 'htmlhelp.htm#database_rules.htm#proc-create_database_rule',0);

--changeSet DEV-609:52 stripComments:false
insert into utl_help values ('/maintenix/web/dbrulechecker/DatabaseRuleDetails.jsp', 'htmlhelp.htm#database_rule_details_page.htm',0);

--changeSet DEV-609:53 stripComments:false
insert into utl_help values ('/maintenix/web/dbrulechecker/DatabaseRuleSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:54 stripComments:false
insert into utl_help values ('/maintenix/web/department/CreateEditDepartment.jsp', 'htmlhelp.htm#OR_Departments.htm#proc-create_department',0);

--changeSet DEV-609:55 stripComments:false
insert into utl_help values ('/maintenix/web/department/DepartmentDetails.jsp', 'htmlhelp.htm#department_details_page.htm',0);

--changeSet DEV-609:56 stripComments:false
insert into utl_help values ('/maintenix/web/department/DepartmentSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:57 stripComments:false
insert into utl_help values ('/maintenix/web/er/CreateEditExtractionRule.jsp', 'htmlhelp.htm#HM_LRP.htm#proc-create_extraction_rule',0);

--changeSet DEV-609:58 stripComments:false
insert into utl_help values ('/maintenix/web/er/ExtractionRuleDetails.jsp', 'htmlhelp.htm#extraction_rules_details_page.htm',0);

--changeSet DEV-609:59 stripComments:false
insert into utl_help values ('/maintenix/web/esigner/ElectronicSignature.jsp', 'htmlhelp.htm#U_electronic_signatures.htm#proc-sign_with_electronic_signature',0);

--changeSet DEV-609:60 stripComments:false
insert into utl_help values ('/maintenix/web/esigner/GenerateCertificate.jsp', 'htmlhelp.htm#U_electronic_signatures.htm#proc-generate_certificate',0);

--changeSet DEV-609:61 stripComments:false
insert into utl_help values ('/maintenix/web/event/CreateEditEvent.jsp', 'htmlhelp.htm#event_details_page.htm',0);

--changeSet DEV-609:62 stripComments:false
insert into utl_help values ('/maintenix/web/event/EventSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:63 stripComments:false
insert into utl_help values ('/maintenix/web/faileffect/CreateEditFailEffect.jsp', 'htmlhelp.htm#ME_Fault_Definitions.htm#proc-create_failure_effect',0);

--changeSet DEV-609:64 stripComments:false
insert into utl_help values ('/maintenix/web/faileffect/FailEffectDetails.jsp', 'htmlhelp.htm#failure_effect_details_page.htm',0);

--changeSet DEV-609:65 stripComments:false
insert into utl_help values ('/maintenix/web/faileffect/FailureEffectSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:66 stripComments:false
insert into utl_help values ('/maintenix/web/fault/CorrectiveActions.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Execution.htm#proc-create_corrective_actions',0);

--changeSet DEV-609:67 stripComments:false
insert into utl_help values ('/maintenix/web/fault/FaultDefinitionSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:68 stripComments:false
insert into utl_help values ('/maintenix/web/fault/FaultSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:69 stripComments:false
insert into utl_help values ('/maintenix/web/fault/FaultSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:70 stripComments:false
insert into utl_help values ('/maintenix/web/fault/PossibleFaultDetails.jsp', 'htmlhelp.htm#possible_fault_details_page.htm',0);

--changeSet DEV-609:71 stripComments:false
insert into utl_help values ('/maintenix/web/fault/RaiseFault.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Execution.htm#proc-create_fault',0);

--changeSet DEV-609:72 stripComments:false
insert into utl_help values ('/maintenix/web/fault/SelectResultEvent.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Execution.htm#proc-create_fault',0);

--changeSet DEV-609:73 stripComments:false
insert into utl_help values ('/maintenix/web/fault/deferral/CreateEditFaultDeferRef.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Execution.htm#proc-create_deferral_reference',0);

--changeSet DEV-609:74 stripComments:false
insert into utl_help values ('/maintenix/web/fault/deferral/DeferralReference.jsp', 'htmlhelp.htm#deferral_reference_details_page.htm',0);

--changeSet DEV-609:75 stripComments:false
insert into utl_help values ('/maintenix/web/fault/threshold/CreateEditFaultThreshold.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Execution.htm#proc-create_fault_threshold',0);

--changeSet DEV-609:76 stripComments:false
insert into utl_help values ('/maintenix/web/faultdefn/AddTechnicalReference.jsp', 'htmlhelp.htm#ME_Fault_Definitions.htm#proc-assign_TR_to_fault_def',0);

--changeSet DEV-609:77 stripComments:false
insert into utl_help values ('/maintenix/web/faultdefn/AssignFailureEffect.jsp', 'htmlhelp.htm#ME_Fault_Definitions.htm#proc-assign_failure_effect',0);

--changeSet DEV-609:78 stripComments:false
insert into utl_help values ('/maintenix/web/faultdefn/AssignFaultSuppression.jsp', 'htmlhelp.htm#ME_Fault_Definitions.htm#proc-assign_fault_suppression',0);

--changeSet DEV-609:79 stripComments:false
insert into utl_help values ('/maintenix/web/faultdefn/AssignTaskDefinition.jsp', 'htmlhelp.htm#ME_Fault_Definitions.htm#proc-assign_initiating_task_to_fault_def',0);

--changeSet DEV-609:80 stripComments:false
insert into utl_help values ('/maintenix/web/faultdefn/CreateEditFaultDefinition.jsp', 'htmlhelp.htm#ME_Fault_Definitions.htm#proc-create_fault_definition',0);

--changeSet DEV-609:81 stripComments:false
insert into utl_help values ('/maintenix/web/faultdefn/EditFailureEffectOrder.jsp', 'htmlhelp.htm#ME_Fault_Definitions.htm#proc-reorder_failure_effects',0);

--changeSet DEV-609:82 stripComments:false
insert into utl_help values ('/maintenix/web/faultdefn/EditTechnicalReferenceOrder.jsp', 'htmlhelp.htm#ME_IETMs.htm#proc-edit_technical_reference_order',0);

--changeSet DEV-609:83 stripComments:false
insert into utl_help values ('/maintenix/web/faultdefn/FaultDefinitionDetails.jsp', 'htmlhelp.htm#fault_definition_details_page.htm',0);

--changeSet DEV-609:84 stripComments:false
insert into utl_help values ('/maintenix/web/faultdefn/FaultDefinitionSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:85 stripComments:false
insert into utl_help values ('/maintenix/web/flight/CompleteFlight.jsp', 'htmlhelp.htm#ME_flights.htm#proc-complete_flight',0);

--changeSet DEV-609:86 stripComments:false
insert into utl_help values ('/maintenix/web/flight/EditFlight.jsp', 'htmlhelp.htm#ME_flights.htm#proc-edit_historic_flight',0);

--changeSet DEV-609:87 stripComments:false
insert into utl_help values ('/maintenix/web/flight/FlightDetails.jsp', 'htmlhelp.htm#flight_details_page.htm',0);

--changeSet DEV-609:88 stripComments:false
insert into utl_help values ('/maintenix/web/flight/FlightSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:89 stripComments:false
insert into utl_help values ('/maintenix/web/flight/MasterFlightDetails.jsp', 'htmlhelp.htm#master_flight_details_page.htm',0);

--changeSet DEV-609:90 stripComments:false
insert into utl_help values ('/maintenix/web/flightdisruption/CreateEditFlightDisruption.jsp', 'htmlhelp.htm#LM_Flight_Disruptions.htm#proc-create_flight_disruption',0);

--changeSet DEV-609:91 stripComments:false
insert into utl_help values ('/maintenix/web/flightdisruption/FlightDisruptionDetails.jsp', 'htmlhelp.htm#flight_disruption_details_page.htm',0);

--changeSet DEV-609:92 stripComments:false
insert into utl_help values ('/maintenix/web/flightdisruption/FlightDisruptionSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:93 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/AccountDetails.jsp', 'htmlhelp.htm#account_details_page.htm',0);

--changeSet DEV-609:94 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/AccountSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:95 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/CreateEditAccount.jsp', 'htmlhelp.htm#F_Accounts.htm#proc-create_account',0);

--changeSet DEV-609:96 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/CreateEditTCode.jsp', 'htmlhelp.htm#F_Accounts.htm#proc-create_t_code',0);

--changeSet DEV-609:97 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/TCodeDetails.jsp', 'htmlhelp.htm#T_code_details_page.htm',0);

--changeSet DEV-609:98 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/TCodeSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:99 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/TransactionDetails.jsp', 'htmlhelp.htm#transaction_details_page.htm',0);

--changeSet DEV-609:100 stripComments:false
insert into utl_help values ('/maintenix/web/fnc/TransactionSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:101 stripComments:false
insert into utl_help values ('/maintenix/web/forecast/AssignForecastModel.jsp', 'htmlhelp.htm#ME_forecast_models.htm#proc-assign_forecast_model',0);

--changeSet DEV-609:102 stripComments:false
insert into utl_help values ('/maintenix/web/forecast/CreateEditForecastModel.jsp', 'htmlhelp.htm#ME_forecast_models.htm#proc-create_forecast_model',0);

--changeSet DEV-609:103 stripComments:false
insert into utl_help values ('/maintenix/web/forecast/FcModelSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:104 stripComments:false
insert into utl_help values ('/maintenix/web/forecast/ForecastModelDetails.jsp', 'htmlhelp.htm#forecast_model_details_page.htm',0);

--changeSet DEV-609:105 stripComments:false
insert into utl_help values ('/maintenix/web/ietm/AddTechnicalReference.jsp', 'htmlhelp.htm#ME_IETMs.htm#proc-add_technical_reference',0);

--changeSet DEV-609:106 stripComments:false
insert into utl_help values ('/maintenix/web/ietm/CreateEditIetm.jsp', 'htmlhelp.htm#ME_IETMs.htm#proc-create_IETM',0);

--changeSet DEV-609:107 stripComments:false
insert into utl_help values ('/maintenix/web/ietm/CreateEditIetmTopic.jsp', 'htmlhelp.htm#ME_IETMs.htm#proc-add_IETM_technical_reference',0);

--changeSet DEV-609:108 stripComments:false
insert into utl_help values ('/maintenix/web/ietm/IetmDetails.jsp', 'htmlhelp.htm#IETM_details_page.htm',0);

--changeSet DEV-609:109 stripComments:false
insert into utl_help values ('/maintenix/web/ietm/IetmSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:110 stripComments:false
insert into utl_help values ('/maintenix/web/ietm/IetmSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:111 stripComments:false
insert into utl_help values ('/maintenix/web/ietm/IetmTopicDetails.jsp', 'htmlhelp.htm#IETM_topic_details_page.htm',0);

--changeSet DEV-609:112 stripComments:false
insert into utl_help values ('/maintenix/web/impacts/AddEditImpacts.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-add_impact_to_req',0);

--changeSet DEV-609:113 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/ArchiveInventory.jsp', 'htmlhelp.htm#ME_managing_inventory.htm#proc-archive_inventory',0);

--changeSet DEV-609:114 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/ChangeBOMItem.jsp', 'htmlhelp.htm#ME_Configuration_Slots.htm#proc-change_config_slot',0);

--changeSet DEV-609:115 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/ChangeOwner.jsp', 'htmlhelp.htm#OR_manufacturers_and_owners.htm#proc-change_owner',0);

--changeSet DEV-609:116 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/CondemnInventory.jsp', 'htmlhelp.htm#MM_inspecting_inventory.htm#proc-condemn_inventory',0);

--changeSet DEV-609:117 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/CreateInventory.jsp', 'htmlhelp.htm#ME_Setting_Up_Inventory.htm#proc-create_inventory_item',0);

--changeSet DEV-609:118 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/DetachInventory.jsp', 'htmlhelp.htm#ME_managing_inventory.htm#proc-detach_inventory',0);

--changeSet DEV-609:119 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/EditInventory.jsp', 'htmlhelp.htm#ME_Setting_Up_Inventory.htm#proc-edit_inventory',0);

--changeSet DEV-609:120 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/InspectAsServiceable.jsp', 'htmlhelp.htm#MM_inspecting_inventory.htm#proc-inspect_as_serviceable',0);

--changeSet DEV-609:121 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/InventoryDetails.jsp', 'htmlhelp.htm#inventory_details_page.htm',0);

--changeSet DEV-609:122 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/InventorySearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:123 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/InventorySearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:124 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/LockInventory.jsp', 'htmlhelp.htm#ME_managing_inventory.htm#proc-lock_inventory',0);

--changeSet DEV-609:125 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/MarkInventoryAsAdvisory.jsp', 'htmlhelp.htm#OR_Managing_Vendors.htm#proc-add_advisory_vendor',0);

--changeSet DEV-609:126 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/MarkInventoryAsInspRequired.jsp', 'htmlhelp.htm#MM_inspecting_inventory.htm#proc-mark_as_inspreq',0);

--changeSet DEV-609:127 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/MarkInventoryAsRepairRequired.jsp', 'htmlhelp.htm#MM_inspecting_inventory.htm#proc-mark_inventory_for_repair',0);

--changeSet DEV-609:128 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/ReturnToVendor.jsp', 'htmlhelp.htm#F_returning_items_to_vendor.htm#proc-return_to_vendor',0);

--changeSet DEV-609:129 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/ScrapInventory.jsp', 'htmlhelp.htm#MM_inspecting_inventory.htm#proc-scrap_tracked_inventory',0);

--changeSet DEV-609:130 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/UnarchiveInventory.jsp', 'htmlhelp.htm#ME_managing_inventory.htm#proc-unarchive_inventory',0);

--changeSet DEV-609:131 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/oilconsumption/EscalateOilConsumptionStatus.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-automatic_oil_consumption_status',0);

--changeSet DEV-609:132 stripComments:false
insert into utl_help values ('/maintenix/web/inventory/reliability/CreateEditReliabilityNote.jsp', 'htmlhelp.htm#ME_managing_inventory.htm#proc-add_reliability_note',0);

--changeSet DEV-609:133 stripComments:false
insert into utl_help values ('/maintenix/web/kit/PickItemsForKit.jsp', 'htmlhelp.htm#MM_managing_kits.htm#proc-add_inv_to_kit',0);

--changeSet DEV-609:134 stripComments:false
insert into utl_help values ('/maintenix/web/labour/CreateLabour.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-add_labor_req',0);

--changeSet DEV-609:135 stripComments:false
insert into utl_help values ('/maintenix/web/labour/EditLabourHistory.jsp', 'htmlhelp.htm#M_Executing_Maintenance_Tasks#proc-edit_labor_history_task',0);

--changeSet DEV-609:136 stripComments:false
insert into utl_help values ('/maintenix/web/labour/EditLabourRequirements.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-edit_labor_req',0);

--changeSet DEV-609:137 stripComments:false
insert into utl_help values ('/maintenix/web/labour/EditWPLabourHistory.jsp', 'htmlhelp.htm#M_Executing_Maintenance_Tasks#proc-edit_labor_history_work_package',0);

--changeSet DEV-609:138 stripComments:false
insert into utl_help values ('/maintenix/web/labour/LabourAssignment.jsp', 'htmlhelp.htm#M_Assigning_Work.htm#proc-assign_HR_labor_req',0);

--changeSet DEV-609:139 stripComments:false
insert into utl_help values ('/maintenix/web/licensedefn/ActivateLicenceDefn.jsp', 'htmlhelp.htm#U_Defining_Licenses.htm#proc-activate_license_definition',0);

--changeSet DEV-609:140 stripComments:false
insert into utl_help values ('/maintenix/web/licensedefn/AircraftAssignLicense.jsp', 'htmlhelp.htm#U_Defining_Licenses.htm#proc-specify_aircraft_for_license',0);

--changeSet DEV-609:141 stripComments:false
insert into utl_help values ('/maintenix/web/licensedefn/CreateEditLicenseDefn.jsp', 'htmlhelp.htm#U_Defining_Licenses.htm#proc-create_license_definition',0);

--changeSet DEV-609:142 stripComments:false
insert into utl_help values ('/maintenix/web/licensedefn/LicenseDefnDetails.jsp', 'htmlhelp.htm#license_definition_details_page.htm',0);

--changeSet DEV-609:143 stripComments:false
insert into utl_help values ('/maintenix/web/licensedefn/LicenseDefnSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:144 stripComments:false
insert into utl_help values ('/maintenix/web/licensedefn/ObsoleteLicenseDefn.jsp', 'htmlhelp.htm#U_Defining_Licenses.htm#proc-obsolete_license_definition',0);

--changeSet DEV-609:145 stripComments:false
insert into utl_help values ('/maintenix/web/licensedefn/PrintLicenseCards.jsp', 'htmlhelp.htm#U_Defining_Licenses.htm#proc-print_license',0);

--changeSet DEV-609:146 stripComments:false
insert into utl_help values ('/maintenix/web/location/AssignCapability.jsp', 'htmlhelp.htm#OR_Locations.htm#proc-assign_capability',0);

--changeSet DEV-609:147 stripComments:false
insert into utl_help values ('/maintenix/web/location/AssignCapacity.jsp', 'htmlhelp.htm#OR_Locations.htm#proc-assign_capacity',0);

--changeSet DEV-609:148 stripComments:false
insert into utl_help values ('/maintenix/web/location/CreateEditContact.jsp', 'htmlhelp.htm#OR_Locations.htm#proc-create_contact',0);

--changeSet DEV-609:149 stripComments:false
insert into utl_help values ('/maintenix/web/location/CreateEditPrinter.jsp', 'htmlhelp.htm#OR_Locations.htm#proc-add_printer',0);

--changeSet DEV-609:150 stripComments:false
insert into utl_help values ('/maintenix/web/location/CreateLocation.jsp', 'htmlhelp.htm#OR_Locations.htm#proc-create_location',0);

--changeSet DEV-609:151 stripComments:false
insert into utl_help values ('/maintenix/web/location/EditCapacity.jsp', 'htmlhelp.htm#OR_Locations.htm#proc-edit_capacity',0);

--changeSet DEV-609:152 stripComments:false
insert into utl_help values ('/maintenix/web/location/EditLocation.jsp', 'htmlhelp.htm#OR_Locations.htm#proc-edit_location',0);

--changeSet DEV-609:153 stripComments:false
insert into utl_help values ('/maintenix/web/location/LocationDetails.jsp', 'htmlhelp.htm#location_details_page.htm',0);

--changeSet DEV-609:154 stripComments:false
insert into utl_help values ('/maintenix/web/location/LocationSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:155 stripComments:false
insert into utl_help values ('/maintenix/web/lpa/LPAResult.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Planning.htm#proc-run_lpa',0);

--changeSet DEV-609:156 stripComments:false
insert into utl_help values ('/maintenix/web/maint/ActivateMaintProgram.jsp', 'htmlhelp.htm#ME_Maintenance_Programs.htm#proc-activate_maintenance_program',0);

--changeSet DEV-609:157 stripComments:false
insert into utl_help values ('/maintenix/web/maint/AssignReq.jsp', 'htmlhelp.htm#ME_Maintenance_Programs.htm#proc-assign_req_to_MP',0);

--changeSet DEV-609:158 stripComments:false
insert into utl_help values ('/maintenix/web/maint/CreateEditMaintProgram.jsp', 'htmlhelp.htm#ME_Maintenance_Programs.htm#proc-create_maintenance_program',0);

--changeSet DEV-609:159 stripComments:false
insert into utl_help values ('/maintenix/web/maint/DeadlineExtensions.jsp', 'htmlhelp.htm#ME_Maintenance_Programs.htm#proc-add_mp_deadline_extensions',0);

--changeSet DEV-609:160 stripComments:false
insert into utl_help values ('/maintenix/web/maint/EditIssueNumbers.jsp', 'htmlhelp.htm#ME_Maintenance_Programs.htm#proc-edit_issue_numbers',0);

--changeSet DEV-609:161 stripComments:false
insert into utl_help values ('/maintenix/web/maint/MaintProgramDetails.jsp', 'htmlhelp.htm#maintenance_program_details_page.htm',0);

--changeSet DEV-609:162 stripComments:false
insert into utl_help values ('/maintenix/web/maint/UnassignReq.jsp', 'htmlhelp.htm#ME_Maintenance_Programs.htm#proc-unassign_req_to_MP',0);

--changeSet DEV-609:163 stripComments:false
insert into utl_help values ('/maintenix/web/manufacturer/CreateEditManufacturer.jsp', 'htmlhelp.htm#OR_manufacturers_and_owners.htm#proc-create_manufacturer',0);

--changeSet DEV-609:164 stripComments:false
insert into utl_help values ('/maintenix/web/manufacturer/ManufacturerDetails.jsp', 'htmlhelp.htm#manufacturer_details_page.htm',0);

--changeSet DEV-609:165 stripComments:false
insert into utl_help values ('/maintenix/web/manufacturer/ManufacturerSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:166 stripComments:false
insert into utl_help values ('/maintenix/web/menu/CreateEditMenuGroup.jsp', 'htmlhelp.htm#Lists_and_Menus.htm#proc-create_menu',0);

--changeSet DEV-609:167 stripComments:false
insert into utl_help values ('/maintenix/web/menu/EditMenuItemOrder.jsp', 'htmlhelp.htm#Lists_and_Menus.htm#proc-edit_menu_items_order',0);

--changeSet DEV-609:168 stripComments:false
insert into utl_help values ('/maintenix/web/menu/EditMenuOrder.jsp', 'htmlhelp.htm#Lists_and_Menus.htm#proc-edit_menu_order',0);

--changeSet DEV-609:169 stripComments:false
insert into utl_help values ('/maintenix/web/menu/MenuGroupDetails.jsp', 'htmlhelp.htm#menu_details_page.htm',0);

--changeSet DEV-609:170 stripComments:false
insert into utl_help values ('/maintenix/web/menu/MenuItemSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:171 stripComments:false
insert into utl_help values ('/maintenix/web/operator/CreateEditOperator.jsp', 'htmlhelp.htm#OR_operators.htm#proc-create_operator',0);

--changeSet DEV-609:172 stripComments:false
insert into utl_help values ('/maintenix/web/operator/OperatorDetails.jsp', 'htmlhelp.htm#operator_details_page.htm',0);

--changeSet DEV-609:173 stripComments:false
insert into utl_help values ('/maintenix/web/operator/OperatorSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:174 stripComments:false
insert into utl_help values ('/maintenix/web/org/CreateEditOrganization.jsp', 'htmlhelp.htm#OR_Organizations.htm#proc-create_suborganization',0);

--changeSet DEV-609:175 stripComments:false
insert into utl_help values ('/maintenix/web/org/OrganizationDetails.jsp', 'htmlhelp.htm#organizations_details_page.htm',0);

--changeSet DEV-609:176 stripComments:false
insert into utl_help values ('/maintenix/web/org/OrganizationSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:177 stripComments:false
insert into utl_help values ('/maintenix/web/owner/CreateEditOwner.jsp', 'htmlhelp.htm#OR_manufacturers_and_owners.htm#proc-create_owner',0);

--changeSet DEV-609:178 stripComments:false
insert into utl_help values ('/maintenix/web/owner/OwnerDetails.jsp', 'htmlhelp.htm#owner_details_page.htm',0);

--changeSet DEV-609:179 stripComments:false
insert into utl_help values ('/maintenix/web/owner/OwnerSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:180 stripComments:false
insert into utl_help values ('/maintenix/web/panel/PanelDetails.jsp', 'htmlhelp.htm#panel_details_page.htm',0);

--changeSet DEV-609:181 stripComments:false
insert into utl_help values ('/maintenix/web/panel/SelectPanel.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-add_panel',0);

--changeSet DEV-609:182 stripComments:false
insert into utl_help values ('/maintenix/web/part/AddContentsToKit.jsp', 'htmlhelp.htm#ME_kits.htm#proc-add_content_to_kit',0);

--changeSet DEV-609:183 stripComments:false
insert into utl_help values ('/maintenix/web/part/AddExchangeVendorPart.jsp', 'htmlhelp.htm#OR_Managing_Vendors.htm#proc-add_parts_to_exchange_vendor',0);

--changeSet DEV-609:184 stripComments:false
insert into utl_help values ('/maintenix/web/part/AddExternallyControlledPart.jsp', 'htmlhelp.htm#M_Part_Requirements.htm#proc-add_ext_controlled_part_req',0);

--changeSet DEV-609:185 stripComments:false
insert into utl_help values ('/maintenix/web/part/AddTaskIncompatibility.jsp', 'htmlhelp.htm#ME_Configuration_Slots.htm#proc-add_task_incompatibility',0);

--changeSet DEV-609:186 stripComments:false
insert into utl_help values ('/maintenix/web/part/BOMPartDetails.jsp', 'htmlhelp.htm#part_group_details_page.htm',0);

--changeSet DEV-609:187 stripComments:false
insert into utl_help values ('/maintenix/web/part/BOMPartSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:188 stripComments:false
insert into utl_help values ('/maintenix/web/part/CreateBinLevel.jsp', 'htmlhelp.htm#MM_bins_and_bin_levels.htm#proc-create_bin_level',0);

--changeSet DEV-609:189 stripComments:false
insert into utl_help values ('/maintenix/web/part/CreateEditPart.jsp', 'htmlhelp.htm#ME_Part_Numbers.htm#proc-create_part',0);

--changeSet DEV-609:190 stripComments:false
insert into utl_help values ('/maintenix/web/part/CreatePriceBreak.jsp', 'htmlhelp.htm#OR_Managing_Vendors.htm#proc-add_price_breaks',0);

--changeSet DEV-609:191 stripComments:false
insert into utl_help values ('/maintenix/web/part/CreatePurchaseVendorPrice.jsp', 'htmlhelp.htm#OR_Managing_Vendors.htm#proc-add_parts_to_vendor',0);

--changeSet DEV-609:192 stripComments:false
insert into utl_help values ('/maintenix/web/part/EditBinLevels.jsp', 'htmlhelp.htm#MM_bins_and_bin_levels.htm#proc-edit_bin_level',0);

--changeSet DEV-609:193 stripComments:false
insert into utl_help values ('/maintenix/web/part/EditExchangeVendor.jsp', 'htmlhelp.htm#OR_Managing_Vendors.htm#proc-edit_exchange_vendor',0);

--changeSet DEV-609:194 stripComments:false
insert into utl_help values ('/maintenix/web/part/EditExternalPartRequirements.jsp', 'htmlhelp.htm#M_Part_Requirements.htm#proc-edit_externally_controlled_part_requirement',0);

--changeSet DEV-609:195 stripComments:false
insert into utl_help values ('/maintenix/web/part/EditInstallKitMap.jsp', 'htmlhelp.htm#ME_kits.htm#proc-edit_install_kit_map',0);

--changeSet DEV-609:196 stripComments:false
insert into utl_help values ('/maintenix/web/part/EditInstallKits.jsp', 'htmlhelp.htm#ME_kits.htm#proc-adjut_install_kit_for_part_group',0);

--changeSet DEV-609:197 stripComments:false
insert into utl_help values ('/maintenix/web/part/EditKitContents.jsp', 'htmlhelp.htm#ME_kits.htm#proc-edit_kit_content',0);

--changeSet DEV-609:198 stripComments:false
insert into utl_help values ('/maintenix/web/part/EditPartRequirements.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-edit_part_requirement',0);

--changeSet DEV-609:199 stripComments:false
insert into utl_help values ('/maintenix/web/part/EditPurchaseVendor.jsp', 'htmlhelp.htm#OR_Managing_Vendors.htm#proc-edit_vendor_part_price_details',0);

--changeSet DEV-609:200 stripComments:false
insert into utl_help values ('/maintenix/web/part/EditRepairVendor.jsp', 'htmlhelp.htm#OR_Managing_Vendors.htm#proc-edit_repair_vendor_details',0);

--changeSet DEV-609:201 stripComments:false
insert into utl_help values ('/maintenix/web/part/ExchangeVendorDetails.jsp', 'htmlhelp.htm#exchange_vendor_details_page.htm',0);

--changeSet DEV-609:202 stripComments:false
insert into utl_help values ('/maintenix/web/part/GetPartsForAssembly.jsp', 'htmlhelp.htm#ME_Assemblies.htm',0);

--changeSet DEV-609:203 stripComments:false
insert into utl_help values ('/maintenix/web/part/ManageAlternatePart.jsp', 'htmlhelp.htm#ME_Configuration_Slots.htm#proc-add_part_number_to_part_group',0);

--changeSet DEV-609:204 stripComments:false
insert into utl_help values ('/maintenix/web/part/PartDetails.jsp', 'htmlhelp.htm#part_details_page.htm',0);

--changeSet DEV-609:205 stripComments:false
insert into utl_help values ('/maintenix/web/part/PartSearch.jsp', 'htmlhelp.htm#performing_searches.htmperforming_searches',0);

--changeSet DEV-609:206 stripComments:false
insert into utl_help values ('/maintenix/web/part/PartSearchByType.jsp', 'htmlhelp.htm#performing_searches.htmperforming_searches',0);

--changeSet DEV-609:207 stripComments:false
insert into utl_help values ('/maintenix/web/pi/AddMiscPOInvoiceLine.jsp', 'htmlhelp.htm#F_Order_Invoices.htm#proc-add_POI_misc_line',0);

--changeSet DEV-609:208 stripComments:false
insert into utl_help values ('/maintenix/web/pi/AddPartPOInvoiceLine.jsp', 'htmlhelp.htm#F_Order_Invoices.htm#proc-add_POI_part_line',0);

--changeSet DEV-609:209 stripComments:false
insert into utl_help values ('/maintenix/web/pi/AddRemoveCharge.jsp', 'htmlhelp.htm#F_Order_Invoices.htm#proc-add_remove_charges_POI',0);

--changeSet DEV-609:210 stripComments:false
insert into utl_help values ('/maintenix/web/pi/AddRemoveTax.jsp', 'htmlhelp.htm#F_Order_Invoices.htm#proc-add_remove_tax_POI',0);

--changeSet DEV-609:211 stripComments:false
insert into utl_help values ('/maintenix/web/pi/CreateEditPOInvoice.jsp', 'htmlhelp.htm#F_Order_Invoices.htm#proc-create_po_invoice',0);

--changeSet DEV-609:212 stripComments:false
insert into utl_help values ('/maintenix/web/pi/EditPOInvoiceLineNote.jsp', 'htmlhelp.htm#F_Order_Invoices.htm',0);

--changeSet DEV-609:213 stripComments:false
insert into utl_help values ('/maintenix/web/pi/EditPOInvoiceLines.jsp', 'htmlhelp.htm#F_Order_Invoices.htm#proc-edit_POI_lines',0);

--changeSet DEV-609:214 stripComments:false
insert into utl_help values ('/maintenix/web/pi/MapPOInvoiceLine.jsp', 'htmlhelp.htm#F_Order_Invoices.htm#proc-map_POI_to_PO',0);

--changeSet DEV-609:215 stripComments:false
insert into utl_help values ('/maintenix/web/pi/POInvoiceDetails.jsp', 'htmlhelp.htm#po_invoice_details_page.htm',0);

--changeSet DEV-609:216 stripComments:false
insert into utl_help values ('/maintenix/web/pi/POInvoiceSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:217 stripComments:false
insert into utl_help values ('/maintenix/web/pi/ValidateForPayment.jsp', 'htmlhelp.htm#F_Order_Invoices.htm#proc-validate_POI',0);

--changeSet DEV-609:218 stripComments:false
insert into utl_help values ('/maintenix/web/po/AddMiscOrderLine.jsp', 'htmlhelp.htm#F_purchase_orders.htm#proc-add_misc_PO_part_line',0);

--changeSet DEV-609:219 stripComments:false
insert into utl_help values ('/maintenix/web/po/AddPartOrderLine.jsp', 'htmlhelp.htm#F_purchase_orders.htm#proc-add_PO_part_line',0);

--changeSet DEV-609:220 stripComments:false
insert into utl_help values ('/maintenix/web/po/AddRemoveCharge.jsp', 'htmlhelp.htm#F_purchase_orders.htm#proc-add_remove_charges_PO',0);

--changeSet DEV-609:221 stripComments:false
insert into utl_help values ('/maintenix/web/po/AddRemoveTax.jsp', 'htmlhelp.htm#F_purchase_orders.htm#proc-add_remove_tax_PO',0);

--changeSet DEV-609:222 stripComments:false
insert into utl_help values ('/maintenix/web/po/AOGAuthorizeOrder.jsp', 'htmlhelp.htm#F_purchase_orders.htm#proc-AOG_authorization_PO',0);

--changeSet DEV-609:223 stripComments:false
insert into utl_help values ('/maintenix/web/po/AuthorizeOrder.jsp', 'htmlhelp.htm#F_purchase_orders.htm#proc-authorize_PO',0);

--changeSet DEV-609:224 stripComments:false
insert into utl_help values ('/maintenix/web/po/ConvertBorrowToExchange.jsp', 'htmlhelp.htm#F_borrow_orders.htm#proc-convert_BO_to_EO',0);

--changeSet DEV-609:225 stripComments:false
insert into utl_help values ('/maintenix/web/po/ConvertBorrowToPurchase.jsp', 'htmlhelp.htm#F_borrow_orders.htm#proc-convert_BO_to_PO',0);

--changeSet DEV-609:226 stripComments:false
insert into utl_help values ('/maintenix/web/po/ConvertExchangeToPurchase.jsp', 'htmlhelp.htm#F_purchase_orders.htm',0);

--changeSet DEV-609:227 stripComments:false
insert into utl_help values ('/maintenix/web/po/ConvertPurchaseToXchg.jsp', 'htmlhelp.htm#F_purchase_orders.htm#proc-convert_line_po_eo',0);

--changeSet DEV-609:228 stripComments:false
insert into utl_help values ('/maintenix/web/po/ConvertXchgToPurchase.jsp', 'htmlhelp.htm#F_exchange_orders.htm#proc-convert_line_from_eo_po',0);

--changeSet DEV-609:229 stripComments:false
insert into utl_help values ('/maintenix/web/po/CreateEditOrder.jsp', 'htmlhelp.htm#F_purchase_orders.htm#proc-create_purchase_order',0);

--changeSet DEV-609:230 stripComments:false
insert into utl_help values ('/maintenix/web/po/EditOrderLines.jsp', 'htmlhelp.htm#F_purchase_orders.htm#proc-edit_PO_lines',0);

--changeSet DEV-609:231 stripComments:false
insert into utl_help values ('/maintenix/web/po/EditPurchaseOrderLineReceiverNote.jsp', 'htmlhelp.htm#F_purchase_orders.htm',0);

--changeSet DEV-609:232 stripComments:false
insert into utl_help values ('/maintenix/web/po/EditPurchaseOrderLineVendorNote.jsp', 'htmlhelp.htm#F_purchase_orders.htm',0);

--changeSet DEV-609:233 stripComments:false
insert into utl_help values ('/maintenix/web/po/IssueOrder.jsp', 'htmlhelp.htm#F_purchase_orders.htm#proc-issue_purchase_order',0);

--changeSet DEV-609:234 stripComments:false
insert into utl_help values ('/maintenix/web/po/OrderDetails.jsp', 'htmlhelp.htm#purchase_order_details_page.htm',0);

--changeSet DEV-609:235 stripComments:false
insert into utl_help values ('/maintenix/web/po/OrderSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:236 stripComments:false
insert into utl_help values ('/maintenix/web/po/POSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:237 stripComments:false
insert into utl_help values ('/maintenix/web/po/RequestOrderAuthorization.jsp', 'htmlhelp.htm#F_purchase_orders.htm#proc-request_PO_authorization',0);

--changeSet DEV-609:238 stripComments:false
insert into utl_help values ('/maintenix/web/po/SelectInventory.jsp', 'htmlhelp.htm#F_exchange_orders.htm#proc-select_return_inventory',0);

--changeSet DEV-609:239 stripComments:false
insert into utl_help values ('/maintenix/web/quarantine/AddEditCloseCorrectiveAction.jsp', 'htmlhelp.htm##proc-quarantine_inventory_howto.htm#proc-quarantine_inventory_howto',0);

--changeSet DEV-609:240 stripComments:false
insert into utl_help values ('/maintenix/web/quarantine/QuarantineDetails.jsp', 'htmlhelp.htm#quarantine_details_page.htm',0);

--changeSet DEV-609:241 stripComments:false
insert into utl_help values ('/maintenix/web/report/ComplianceReport.jsp', 'htmlhelp.htm#compliance_report_page.htm',0);

--changeSet DEV-609:242 stripComments:false
insert into utl_help values ('/maintenix/web/req/CreatePurchaseRequest.jsp', 'htmlhelp.htm#MM_requesting_inventory.htm#proc-create_purchase_request',0);

--changeSet DEV-609:243 stripComments:false
insert into utl_help values ('/maintenix/web/req/CreatePurchaseRequestForStock.jsp', 'htmlhelp.htm#MM_requesting_inventory.htm#proc-create_consignment_request',0);

--changeSet DEV-609:244 stripComments:false
insert into utl_help values ('/maintenix/web/req/IssueInventory.jsp', 'htmlhelp.htm#MM_Issue_Inventory.htm#proc-issue_inventory',0);

--changeSet DEV-609:245 stripComments:false
insert into utl_help values ('/maintenix/web/req/PartRequestDetails.jsp', 'htmlhelp.htm#part_request_details_page.htm',0);

--changeSet DEV-609:246 stripComments:false
insert into utl_help values ('/maintenix/web/req/PartRequestSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:247 stripComments:false
insert into utl_help values ('/maintenix/web/req/ReserveLocalInventory.jsp', 'htmlhelp.htm#MM_reserving_inventory.htm#proc-reserve_local_inventory_item',0);

--changeSet DEV-609:248 stripComments:false
insert into utl_help values ('/maintenix/web/req/ReserveOnOrderInventory.jsp', 'htmlhelp.htm#MM_reserving_inventory.htm#proc-reserve_on_order_inventory',0);

--changeSet DEV-609:249 stripComments:false
insert into utl_help values ('/maintenix/web/req/ReserveRemoteInventory.jsp', 'htmlhelp.htm#MM_reserving_inventory.htm#proc-reserve_remote_inventory',0);

--changeSet DEV-609:250 stripComments:false
insert into utl_help values ('/maintenix/web/req/StealRemoteReservation.jsp', 'htmlhelp.htm#MM_reserving_inventory.htm#proc-steal_remote_reservation',0);

--changeSet DEV-609:251 stripComments:false
insert into utl_help values ('/maintenix/web/rfq/AddMiscRFQLine.jsp', 'htmlhelp.htm#F_RFQ.htm#proc-add_misc_line_to_RFQ',0);

--changeSet DEV-609:252 stripComments:false
insert into utl_help values ('/maintenix/web/rfq/AddRemoveCharge.jsp', 'htmlhelp.htm#F_RFQ.htm#proc-add_remove_charges_RFQ',0);

--changeSet DEV-609:253 stripComments:false
insert into utl_help values ('/maintenix/web/rfq/AddRemoveTax.jsp', 'htmlhelp.htm#F_RFQ.htm#proc-add_remove_tax_RFQ',0);

--changeSet DEV-609:254 stripComments:false
insert into utl_help values ('/maintenix/web/rfq/CreateEditRFQ.jsp', 'htmlhelp.htm#F_RFQ.htm#proc-create_RFQ',0);

--changeSet DEV-609:255 stripComments:false
insert into utl_help values ('/maintenix/web/rfq/EditRFQLines.jsp', 'htmlhelp.htm#F_RFQ.htm#proc-edit_RFQ',0);

--changeSet DEV-609:256 stripComments:false
insert into utl_help values ('/maintenix/web/rfq/EditVendorQuotes.jsp', 'htmlhelp.htm#F_RFQ.htm#proc-record_vendor_quote_for_RFQ',0);

--changeSet DEV-609:257 stripComments:false
insert into utl_help values ('/maintenix/web/rfq/RFQDetails.jsp', 'htmlhelp.htm#rfq_details_page.htm',0);

--changeSet DEV-609:258 stripComments:false
insert into utl_help values ('/maintenix/web/rfq/RFQSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:259 stripComments:false
insert into utl_help values ('/maintenix/web/role/CreateEditRole.jsp', 'htmlhelp.htm#User_Roles.htm#proc-create_role',0);

--changeSet DEV-609:260 stripComments:false
insert into utl_help values ('/maintenix/web/role/RoleDetails.jsp', 'htmlhelp.htm#role_details_page.htm',0);

--changeSet DEV-609:261 stripComments:false
insert into utl_help values ('/maintenix/web/role/RoleSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:262 stripComments:false
insert into utl_help values ('/maintenix/web/shift/CapacityPatternDetails.jsp', 'htmlhelp.htm#capacity_pattern_details_page.htm',0);

--changeSet DEV-609:263 stripComments:false
insert into utl_help values ('/maintenix/web/shift/CreateEditCapacityPattern.jsp', 'htmlhelp.htm#HM_Heavy_Maintenance_Visit_Planning.htm#proc-add_capacity_pattern',0);

--changeSet DEV-609:264 stripComments:false
insert into utl_help values ('/maintenix/web/shift/CreateEditShift.jsp', 'htmlhelp.htm#U_shifts_and_schedules.htm#proc-create_shift',0);

--changeSet DEV-609:265 stripComments:false
insert into utl_help values ('/maintenix/web/shift/DuplicateCapacityPattern.jsp', 'htmlhelp.htm#HM_Heavy_Maintenance_Visit_Planning.htm#proc-duplicate_capacity_pattern',0);

--changeSet DEV-609:266 stripComments:false
insert into utl_help values ('/maintenix/web/shift/EditLaborProfile.jsp', 'htmlhelp.htm#HM_Heavy_Maintenance_Visit_Planning.htm#proc-edit_labor_profile_for_capacity_pattern',0);

--changeSet DEV-609:267 stripComments:false
insert into utl_help values ('/maintenix/web/shift/EditLaborSkills.jsp', 'htmlhelp.htm#HM_Heavy_Maintenance_Visit_Planning.htm#proc-edit_labor_skills_for_cap_patt',0);

--changeSet DEV-609:268 stripComments:false
insert into utl_help values ('/maintenix/web/shift/EditShiftsForDay.jsp', 'htmlhelp.htm#HM_Heavy_Maintenance_Visit_Planning.htm#proc-edit_day_shift_for_capacity_pattern',0);

--changeSet DEV-609:269 stripComments:false
insert into utl_help values ('/maintenix/web/shift/usershiftpattern/CreateEditUserShiftPattern.jsp', 'htmlhelp.htm#U_shifts_and_schedules.htm#proc-create_schedule',0);

--changeSet DEV-609:270 stripComments:false
insert into utl_help values ('/maintenix/web/shift/usershiftpattern/EditShifts.jsp', 'htmlhelp.htm#U_shifts_and_schedules.htm#proc-modify_schedule_shifts',0);

--changeSet DEV-609:271 stripComments:false
insert into utl_help values ('/maintenix/web/shift/usershiftpattern/UserShiftPatternDetails.jsp', 'htmlhelp.htm#user_shift_pattern_details_page.htm',0);

--changeSet DEV-609:272 stripComments:false
insert into utl_help values ('/maintenix/web/shipment/AddShipmentLine.jsp', 'htmlhelp.htm#MM_shipping_inventory.htm#proc-add_part_shipment_line',0);

--changeSet DEV-609:273 stripComments:false
insert into utl_help values ('/maintenix/web/shipment/AssignToWaybillGroup.jsp', 'htmlhelp.htm#MM_shipping_inventory.htm#proc-create_waybill_group',0);

--changeSet DEV-609:274 stripComments:false
insert into utl_help values ('/maintenix/web/shipment/CreateEditShipment.jsp', 'htmlhelp.htm#MM_shipping_inventory.htm#proc-create_shipment',0);

--changeSet DEV-609:275 stripComments:false
insert into utl_help values ('/maintenix/web/shipment/InstallKitsAvailable.jsp', 'htmlhelp.htm#MM_shipping_inventory.htm#proc-add_part_shipment_line',0);

--changeSet DEV-609:276 stripComments:false
insert into utl_help values ('/maintenix/web/shipment/PickShipment.jsp', 'htmlhelp.htm#MM_shipping_inventory.htm#proc-pick_shipment_items',0);

--changeSet DEV-609:277 stripComments:false
insert into utl_help values ('/maintenix/web/shipment/ReceiveShipment.jsp', 'htmlhelp.htm#MM_recieiving_inventory.htm#proc-receive_shipment',0);

--changeSet DEV-609:278 stripComments:false
insert into utl_help values ('/maintenix/web/shipment/SendShipment.jsp', 'htmlhelp.htm#MM_shipping_inventory.htm#proc-send_shipment',0);

--changeSet DEV-609:279 stripComments:false
insert into utl_help values ('/maintenix/web/shipment/ShipmentDetails.jsp', 'htmlhelp.htm#shipment_details_page.htm',0);

--changeSet DEV-609:280 stripComments:false
insert into utl_help values ('/maintenix/web/shipment/ShipmentSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:281 stripComments:false
insert into utl_help values ('/maintenix/web/shipment/routing/EditShipmentRouting.jsp', 'htmlhelp.htm#MM_shipping_inventory.htm#proc-add_shipment_segment',0);

--changeSet DEV-609:282 stripComments:false
insert into utl_help values ('/maintenix/web/shipment/routing/ReceiveShipmentSegment.jsp', 'htmlhelp.htm#MM_recieiving_inventory.htm#proc-receive_shipment_segment',0);

--changeSet DEV-609:283 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/AddEditStatusBoardQuery.jsp', 'htmlhelp.htm#Status_Board.htm#proc-add_query_status_board',0);

--changeSet DEV-609:284 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/AddStatusBoardAssmbl.jsp', 'htmlhelp.htm#Status_Board.htm#proc-add_assembly_status_board',0);

--changeSet DEV-609:285 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/AddStatusBoardColumnGroup.jsp', 'htmlhelp.htm#Status_Board.htm#proc-add_columngroup_status_board',0);

--changeSet DEV-609:286 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/CreateEditStatusBoard.jsp', 'htmlhelp.htm#Status_Board.htm#proc-create_status_board',0);

--changeSet DEV-609:287 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/CreateEditStatusBoardColumn.jsp', 'htmlhelp.htm#Status_Board.htm#proc-add_column_status_board',0);

--changeSet DEV-609:288 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/EditAssetNote.jsp', 'htmlhelp.htm#Status_Board.htm#proc-add_note_status_board',0);

--changeSet DEV-609:289 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/EditStatusBoardColumnOrder.jsp', 'htmlhelp.htm#Status_Board.htm#proc-order_columns_status_board',0);

--changeSet DEV-609:290 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/StatusBoardAssmbls.jsp', 'htmlhelp.htm#Status_Board.htm',0);

--changeSet DEV-609:291 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/StatusBoardColumnGroups.jsp', 'htmlhelp.htm#Status_Board.htm',0);

--changeSet DEV-609:292 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/StatusBoardColumns.jsp', 'htmlhelp.htm#Status_Board.htm',0);

--changeSet DEV-609:293 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/StatusBoardDetails.jsp', 'htmlhelp.htm#status_board_details_page.htm',0);

--changeSet DEV-609:294 stripComments:false
insert into utl_help values ('/maintenix/web/statusboard/StatusBoardQueries.jsp', 'htmlhelp.htm#Status_Board.htm',0);

--changeSet DEV-609:295 stripComments:false
insert into utl_help values ('/maintenix/web/stock/AdjustStockPercentage.jsp', 'htmlhelp.htm#MM_stocks_and_stock_levels.htm#proc-edit_stock_percentage',0);

--changeSet DEV-609:296 stripComments:false
insert into utl_help values ('/maintenix/web/stock/CreateEditStock.jsp', 'htmlhelp.htm#MM_stocks_and_stock_levels.htm#proc-create_stock_number',0);

--changeSet DEV-609:297 stripComments:false
insert into utl_help values ('/maintenix/web/stock/EditStockLevels.jsp', 'htmlhelp.htm#MM_stocks_and_stock_levels.htm#proc-edit_stock_level',0);

--changeSet DEV-609:298 stripComments:false
insert into utl_help values ('/maintenix/web/stock/StockDetails.jsp', 'htmlhelp.htm#stock_details_page.htm',0);

--changeSet DEV-609:299 stripComments:false
insert into utl_help values ('/maintenix/web/stock/StockSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:300 stripComments:false
insert into utl_help values ('/maintenix/web/stocklevel/CreateStockLevel.jsp', 'htmlhelp.htm#MM_stocks_and_stock_levels.htm#proc-create_stock_level',0);

--changeSet DEV-609:301 stripComments:false
insert into utl_help values ('/maintenix/web/tag/CreateEditTag.jsp', 'htmlhelp.htm#ME_tagging_task_defs.htm#proc-create_tag',0);

--changeSet DEV-609:302 stripComments:false
insert into utl_help values ('/maintenix/web/tag/TagDetails.jsp', 'htmlhelp.htm#tag_details_page.htm',0);

--changeSet DEV-609:303 stripComments:false
insert into utl_help values ('/maintenix/web/task/AddDeadline.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Planning.htm#proc-add_deadline',0);

--changeSet DEV-609:304 stripComments:false
insert into utl_help values ('/maintenix/web/task/AddMeasurements.jsp', 'htmlhelp.htm#M_tasks_and_work_packages.htm#proc-add_measurement_parameter',0);

--changeSet DEV-609:305 stripComments:false
insert into utl_help values ('/maintenix/web/task/AdjustHoursForBilling.jsp', 'htmlhelp.htm#M_tasks_and_work_packages.htm#proc-adjust_billing_hours',0);

--changeSet DEV-609:306 stripComments:false
insert into utl_help values ('/maintenix/web/task/CancelTasks.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Planning.htm#proc-cancel_task',0);

--changeSet DEV-609:307 stripComments:false
insert into utl_help values ('/maintenix/web/task/CheckDelay.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Planning.htm#proc-delay_work_package',0);

--changeSet DEV-609:308 stripComments:false
insert into utl_help values ('/maintenix/web/task/CheckDetails.jsp', 'htmlhelp.htm#work_package_details_page.htm',0);

--changeSet DEV-609:309 stripComments:false
insert into utl_help values ('/maintenix/web/task/CompleteTask.jsp', 'htmlhelp.htm#M_Executing_Maintenance_Tasks.htm#proc-complete_task',0);

--changeSet DEV-609:310 stripComments:false
insert into utl_help values ('/maintenix/web/task/CompleteTasks.jsp', 'htmlhelp.htm#M_Executing_Maintenance_Tasks.htm#proc-complete_task',0);

--changeSet DEV-609:311 stripComments:false
insert into utl_help values ('/maintenix/web/task/CreateOrEditCheck.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Planning.htm#proc-create_work_package',0);

--changeSet DEV-609:312 stripComments:false
insert into utl_help values ('/maintenix/web/task/CreateRepetitiveTask.jsp', 'htmlhelp.htm#M_Executing_Maintenance_Tasks.htm#proc-create_repetitive_task',0);

--changeSet DEV-609:313 stripComments:false
insert into utl_help values ('/maintenix/web/task/CreateTaskFromDefinition.jsp', 'htmlhelp.htm#M_tasks_and_work_packages.htm#proc-create_task_in_work_package',0);

--changeSet DEV-609:314 stripComments:false
insert into utl_help values ('/maintenix/web/task/EditEstimatedEndDate.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Planning.htm#proc-edit_est_end_date_work_package',0);

--changeSet DEV-609:315 stripComments:false
insert into utl_help values ('/maintenix/web/task/EditMeasurements.jsp', 'htmlhelp.htm#M_Executing_Maintenance_Tasks.htm#proc-edit_measurement_parameter',0);

--changeSet DEV-609:316 stripComments:false
insert into utl_help values ('/maintenix/web/task/EditTask.jsp', 'htmlhelp.htm#M_tasks_and_work_packages.htm#proc-edit_task_details',0);

--changeSet DEV-609:317 stripComments:false
insert into utl_help values ('/maintenix/web/task/EditWorkscopeOrder.jsp', 'htmlhelp.htm#SM_Shop_Maintenance_Control.htm#proc-edit_workscope_order',0);

--changeSet DEV-609:318 stripComments:false
insert into utl_help values ('/maintenix/web/task/EnforceNSVDeadlines.jsp', 'htmlhelp.htm#SM_Shop_Maintenance_Control.htm#proc-enforce_NSV_deadlines',0);

--changeSet DEV-609:319 stripComments:false
insert into utl_help values ('/maintenix/web/task/ExtendDeadline.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Planning.htm#proc-extend_deadline',0);

--changeSet DEV-609:320 stripComments:false
insert into utl_help values ('/maintenix/web/task/PlanShift.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Planning.htm#proc-plan_shift_work_package',0);

--changeSet DEV-609:321 stripComments:false
insert into utl_help values ('/maintenix/web/task/SetIssueToAccounts.jsp', 'htmlhelp.htm#F_Accounts.htm#proc-set_issue_to_accounts',0);

--changeSet DEV-609:322 stripComments:false
insert into utl_help values ('/maintenix/web/task/SetPlanByDate.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-set_plan_by_date',0);

--changeSet DEV-609:323 stripComments:false
insert into utl_help values ('/maintenix/web/task/StartCheck.jsp', 'htmlhelp.htm#M_Executing_Maintenance_Tasks.htm#proc-start_work_package',0);

--changeSet DEV-609:324 stripComments:false
insert into utl_help values ('/maintenix/web/task/TaskDetails.jsp', 'htmlhelp.htm#task_details_page.htm',0);

--changeSet DEV-609:325 stripComments:false
insert into utl_help values ('/maintenix/web/task/TaskSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:326 stripComments:false
insert into utl_help values ('/maintenix/web/task/TaskSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:327 stripComments:false
insert into utl_help values ('/maintenix/web/task/TerminateTasks.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Planning.htm#proc-terminate_task',0);

--changeSet DEV-609:328 stripComments:false
insert into utl_help values ('/maintenix/web/task/ViewOpportunisticTasks.jsp', 'htmlhelp.htm#M_tasks_and_work_packages.htm#proc-add_opportunistic_tasks',0);

--changeSet DEV-609:329 stripComments:false
insert into utl_help values ('/maintenix/web/task/ViewPreviousMeasurements.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards#proc-add_measurement',0);

--changeSet DEV-609:330 stripComments:false
insert into utl_help values ('/maintenix/web/task/invoicing/CreateEstimateCostLineItem.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Planning.htm#proc-add_cost_line_item',0);

--changeSet DEV-609:331 stripComments:false
insert into utl_help values ('/maintenix/web/task/invoicing/EditEstimateCostLineItems.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Planning.htm#proc-edit_cost_line_item',0);

--changeSet DEV-609:332 stripComments:false
insert into utl_help values ('/maintenix/web/task/labour/EditWorkCapture.jsp', 'htmlhelp.htm#M_Executing_Maintenance_Tasks#proc-edit_work_captured',0);

--changeSet DEV-609:333 stripComments:false
insert into utl_help values ('/maintenix/web/task/labour/WorkCapture.jsp', 'htmlhelp.htm#M_Executing_Maintenance_Tasks#proc-finish_a_task',0);

--changeSet DEV-609:334 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/ActivateTaskDefinition.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-activate_requirement',0);

--changeSet DEV-609:335 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddAttachment.jsp', 'htmlhelp.htm#ME_IETMs.htm#proc-add_JIC_attachment',0);

--changeSet DEV-609:336 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddEditMeasureSpecificSchedulingRules.jsp', 'htmlhelp.htm#ME_Task_Scheduling.htm#proc-add_measurement_specific_scheduling_rule',0);

--changeSet DEV-609:337 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddFollowingTask.jsp', 'htmlhelp.htm#ME_Task_Relationships.htm#proc-add_following_task',0);

--changeSet DEV-609:338 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddJicToReq.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-assign_JIC_to_req',0);

--changeSet DEV-609:339 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddLabourRequirement.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-add_labor_requirement',0);

--changeSet DEV-609:340 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddLinkedTask.jsp', 'htmlhelp.htm#ME_Task_Relationships.htm#proc-add_linked_task',0);

--changeSet DEV-609:341 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddPanel.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-add_panel',0);

--changeSet DEV-609:342 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddPartRequirement.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-add_part_requirement',0);

--changeSet DEV-609:343 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddPartTransformation.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-add_part_transformation',0);

--changeSet DEV-609:344 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddReqToBlock.jsp', 'htmlhelp.htm#ME_Blocks.htm#proc-add_req_to_block',0);

--changeSet DEV-609:345 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddSchedulingRule.jsp', 'htmlhelp.htm#ME_Task_Scheduling.htm#proc-add_scheduling_rules',0);

--changeSet DEV-609:346 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddStep.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-add_job_card_step',0);

--changeSet DEV-609:347 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddTechnicalReference.jsp', 'htmlhelp.htm#ME_IETMs.htm#proc-add_technical_reference',0);

--changeSet DEV-609:348 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddToolRequirement.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-add_tool_requirement',0);

--changeSet DEV-609:349 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/AddZone.jsp', 'htmlhelp.htm#ME_master_panel_card.htm#proc-add_zone',0);

--changeSet DEV-609:350 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/BlockDetails.jsp', 'htmlhelp.htm#block_details_page.htm',0);

--changeSet DEV-609:351 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/BlockSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:352 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/CreateEditBlock.jsp', 'htmlhelp.htm#ME_Blocks.htm#proc-create_block',0);

--changeSet DEV-609:353 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/CreateEditJobCard.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-create_JIC',0);

--changeSet DEV-609:354 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/CreateEditMPC.jsp', 'htmlhelp.htm#ME_master_panel_card.htm#proc-create_edit_panel_card',0);

--changeSet DEV-609:355 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/CreateEditPlanningType.jsp', 'htmlhelp.htm#HM_Heavy_Maintenance_Visit_Planning.htm#proc-ppc_create_planning_type',0);

--changeSet DEV-609:356 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/CreateEditRefDoc.jsp', 'htmlhelp.htm#ME_reference_docs.htm#proc-create_reference_doc',0);

--changeSet DEV-609:357 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/CreateEditReq.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-create_requirement',0);

--changeSet DEV-609:358 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/CreateRevision.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-create_requirement_rev',0);

--changeSet DEV-609:359 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/DeadlineExtensions.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-add_deadline_extensions',0);

--changeSet DEV-609:360 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/DispositionRefDocument.jsp', 'htmlhelp.htm#ME_reference_docs.htm#proc-disposition_reference_doc',0);

--changeSet DEV-609:361 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/DuplicateTaskDefinition.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-duplicate_requirement',0);

--changeSet DEV-609:362 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/EditLabourRequirements.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-edit_labor_requirement',0);

--changeSet DEV-609:363 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/EditMeasurementOrder.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-edit_measurement_order',0);

--changeSet DEV-609:364 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/EditPartRequirements.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-edit_part_requirement',0);

--changeSet DEV-609:365 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/EditPartSpecificSchedulingRules.jsp', 'htmlhelp.htm#ME_Task_Scheduling.htm#proc-edit_part_scheduling_rules',0);

--changeSet DEV-609:366 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/EditPlanningValues.jsp', 'htmlhelp.htm#HM_Heavy_Maintenance_Visit_Planning.htm#proc-ppc_set_nr_for_block_def',0);

--changeSet DEV-609:367 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/EditReqMap.jsp', 'htmlhelp.htm#ME_Blocks.htm#proc-edit_req_mapping_for_block',0);

--changeSet DEV-609:368 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/EditSchedulingRules.jsp', 'htmlhelp.htm#ME_Task_Scheduling.htm#proc-edit_scheduling_rules',0);

--changeSet DEV-609:369 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/EditSteps.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-edit_job_card_steps',0);

--changeSet DEV-609:370 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/EditSubtaskOrder.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-set_subtask_order',0);

--changeSet DEV-609:371 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/EditTailNoSpecificSchedRules.jsp', 'htmlhelp.htm#ME_Task_Scheduling.htm#proc-edit_tail_scheduling_rules',0);

--changeSet DEV-609:372 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/EditTechnicalReferenceOrder.jsp', 'htmlhelp.htm#ME_IETMs.htm#proc-edit_technical_reference_order',0);

--changeSet DEV-609:373 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/EditToolRequirements.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-edit_tool_requirement',0);

--changeSet DEV-609:374 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/EditWorkConditions.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-edit_task_conditions',0);

--changeSet DEV-609:375 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/InitializeTask.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-initialize_req',0);

--changeSet DEV-609:376 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/InstallKitsAvailable.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards#proc-add_part_requirement',0);

--changeSet DEV-609:377 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/JICDetails.jsp', 'htmlhelp.htm#job_card_details_page.htm',0);

--changeSet DEV-609:378 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/JobCardSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:379 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/MoveTaskDefinition.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-move_requirement',0);

--changeSet DEV-609:380 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/MPCDetails.jsp', 'htmlhelp.htm#master_panel_details_page.htm',0);

--changeSet DEV-609:381 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/ObsoleteTaskDefn.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-obsolete_requirement',0);

--changeSet DEV-609:382 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/PreventAllowReq.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-prevent_req_exe',0);

--changeSet DEV-609:383 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/RefDocDetails.jsp', 'htmlhelp.htm#reference_doc_details_page.htm',0);

--changeSet DEV-609:384 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/RefDocSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:385 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/ReqDetails.jsp', 'htmlhelp.htm#requirement_details_page.htm',0);

--changeSet DEV-609:386 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/ReqSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:387 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/SetPlanByDates.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-set_plan_by_date',0);

--changeSet DEV-609:388 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/ShowHideDifferences.jsp', 'htmlhelp.htm#ME_Task_Definition.htm#proc-show_task_def_differences',0);

--changeSet DEV-609:389 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/TaskDefinitionSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:390 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/TaskExtendDeadline.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-add_deadline_extensions',0);

--changeSet DEV-609:391 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/TaskLabourSummary.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards.htm#proc-view_labor_summary',0);

--changeSet DEV-609:392 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/ToggleEnforceWorkscopeOrder.jsp', 'htmlhelp.htm#SM_Shop_Maintenance_Control.htm#proc-enforce_workscope_order',0);

--changeSet DEV-609:393 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/reqdetails/AddAdvisory.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-add_advisory_to_req',0);

--changeSet DEV-609:394 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/taskdefnapproval/ApproveTaskDefn.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-approve_requirement_request',0);

--changeSet DEV-609:395 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/taskdefnapproval/RejectTaskDefn.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-reject_requirement_request',0);

--changeSet DEV-609:396 stripComments:false
insert into utl_help values ('/maintenix/web/taskdefn/taskdefnapproval/RequestRestartApproval.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-request_requirement_approval',0);

--changeSet DEV-609:397 stripComments:false
insert into utl_help values ('/maintenix/web/todolist/AssignToDoButton.jsp', 'htmlhelp.htm#Lists_and_Menus.htm#proc-add_buttons_to_do_list',0);

--changeSet DEV-609:398 stripComments:false
insert into utl_help values ('/maintenix/web/todolist/AssignToDoTab.jsp', 'htmlhelp.htm#Lists_and_Menus.htm#proc-add_tabs_to_do_list',0);

--changeSet DEV-609:399 stripComments:false
insert into utl_help values ('/maintenix/web/todolist/CreateEditToDoList.jsp', 'htmlhelp.htm#Lists_and_Menus.htm#proc-create_to_do_list',0);

--changeSet DEV-609:400 stripComments:false
insert into utl_help values ('/maintenix/web/todolist/EditToDoButtonOrder.jsp', 'htmlhelp.htm#Lists_and_Menus.htm#proc-edit_buttons_to_do_list',0);

--changeSet DEV-609:401 stripComments:false
insert into utl_help values ('/maintenix/web/todolist/EditToDoTabOrder.jsp', 'htmlhelp.htm#Lists_and_Menus.htm#proc-edit_tabs_to_do_list',0);

--changeSet DEV-609:402 stripComments:false
insert into utl_help values ('/maintenix/web/tool/ToolSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:403 stripComments:false
insert into utl_help values ('/maintenix/web/transfer/CreatePutAway.jsp', 'htmlhelp.htm#MM_recieiving_inventory.htm#proc-create_put_away_ticket',0);

--changeSet DEV-609:404 stripComments:false
insert into utl_help values ('/maintenix/web/transfer/CreateTransfer.jsp', 'htmlhelp.htm#MM_Transfers.htm#proc-create_transfer',0);

--changeSet DEV-609:405 stripComments:false
insert into utl_help values ('/maintenix/web/transfer/PreDrawInventory.jsp', 'htmlhelp.htm#MM_Issue_Inventory.htm#proc-predraw_inventory',0);

--changeSet DEV-609:406 stripComments:false
insert into utl_help values ('/maintenix/web/transfer/TransferDetails.jsp', 'htmlhelp.htm#transfer_details_page.htm',0);

--changeSet DEV-609:407 stripComments:false
insert into utl_help values ('/maintenix/web/transfer/TransferSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:408 stripComments:false
insert into utl_help values ('/maintenix/web/usage/CreateUsageRecord.jsp', 'htmlhelp.htm#ME_correcting_faults_and_tasks.htm#proc-create_usage_record',0);

--changeSet DEV-609:409 stripComments:false
insert into utl_help values ('/maintenix/web/usage/EditUsageSnapshot.jsp', 'htmlhelp.htm#ME_correcting_faults_and_tasks.htm#proc-edit_usage_snapshot',0);

--changeSet DEV-609:410 stripComments:false
insert into utl_help values ('/maintenix/web/usage/UsageRecordDetails.jsp', 'htmlhelp.htm#usage_record_details_page.htm',0);

--changeSet DEV-609:411 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/AssignUsageParm.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-assign_usage_parameter',0);

--changeSet DEV-609:412 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/CreateUsageDefinition.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-create_usage_parameter',0);

--changeSet DEV-609:413 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/ReorderUsageParms.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-reorder_usage_parameters',0);

--changeSet DEV-609:414 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/UsageDefinitionDetails.jsp', 'htmlhelp.htm#usage_definition_details_page.htm',0);

--changeSet DEV-609:415 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/calc/AddConstant.jsp', 'htmlhelp.htm#ME_Configuration_Slots.htm#proc-add_constant_as_calc_input',0);

--changeSet DEV-609:416 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/calc/EditConstants.jsp', 'htmlhelp.htm#ME_Configuration_Slots.htm#proc-change_calc_input_constant_value',0);

--changeSet DEV-609:417 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/calc/EditPartSpecConstants.jsp', 'htmlhelp.htm#ME_Configuration_Slots.htm#proc-edit_part_specific_constant',0);

--changeSet DEV-609:418 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/calc/EditSymbols.jsp', 'htmlhelp.htm#ME_Configuration_Slots.htm#proc-change_calc_input_constant_symbol',0);

--changeSet DEV-609:419 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/usageparm/AssignDataValue.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-assign_data_value',0);

--changeSet DEV-609:420 stripComments:false
insert into utl_help values ('/maintenix/web/usagedefn/usageparm/UsageParameterDetails.jsp', 'htmlhelp.htm#parameter_details_pages.htm',0);

--changeSet DEV-609:421 stripComments:false
insert into utl_help values ('/maintenix/web/user/AddEditHrShift.jsp', 'htmlhelp.htm#U_shifts_and_schedules.htm#proc-change_user_shift',0);

--changeSet DEV-609:422 stripComments:false
insert into utl_help values ('/maintenix/web/user/AddEditHrUserShiftPattern.jsp', 'htmlhelp.htm#U_shifts_and_schedules.htm#proc-assign_schedule_to_user',0);

--changeSet DEV-609:423 stripComments:false
insert into utl_help values ('/maintenix/web/user/AddTimeOff.jsp', 'htmlhelp.htm#U_shifts_and_schedules.htm#proc-record_time_off_for_user',0);

--changeSet DEV-609:424 stripComments:false
insert into utl_help values ('/maintenix/web/user/AssignLicense.jsp', 'htmlhelp.htm#U_Defining_Licenses.htm#proc-assign_license',0);

--changeSet DEV-609:425 stripComments:false
insert into utl_help values ('/maintenix/web/user/AssignSkill.jsp', 'htmlhelp.htm#U_Skills.htm#proc-assign_skills',0);

--changeSet DEV-609:426 stripComments:false
insert into utl_help values ('/maintenix/web/user/CreateEditUser.jsp', 'htmlhelp.htm#U_Users.htm#proc-create_user',0);

--changeSet DEV-609:427 stripComments:false
insert into utl_help values ('/maintenix/web/user/EditRoleOrder.jsp', 'htmlhelp.htm#User_Roles.htm#proc-edit_role_order',0);

--changeSet DEV-609:428 stripComments:false
insert into utl_help values ('/maintenix/web/user/SuspendLicense.jsp', 'htmlhelp.htm#U_Defining_Licenses.htm#proc-suspend_license',0);

--changeSet DEV-609:429 stripComments:false
insert into utl_help values ('/maintenix/web/user/UnSuspendLicense.jsp', 'htmlhelp.htm#U_Defining_Licenses.htm#proc-activate_suspended_license',0);

--changeSet DEV-609:430 stripComments:false
insert into utl_help values ('/maintenix/web/user/UserDetails.jsp', 'htmlhelp.htm#user_details_page.htm',0);

--changeSet DEV-609:431 stripComments:false
insert into utl_help values ('/maintenix/web/user/UserSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:432 stripComments:false
insert into utl_help values ('/maintenix/web/user/UserSearchByType.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:433 stripComments:false
insert into utl_help values ('/maintenix/web/vendor/ChangeOrderTypeStatus.jsp', 'htmlhelp.htm#OR_Managing_Vendors.htm#proc-approve_vendor_org',0);

--changeSet DEV-609:434 stripComments:false
insert into utl_help values ('/maintenix/web/vendor/ChangeServiceTypeStatus.jsp', 'htmlhelp.htm#OR_Managing_Vendors.htm#proc-approve_vendor_org',0);

--changeSet DEV-609:435 stripComments:false
insert into utl_help values ('/maintenix/web/vendor/CreateEditVendor.jsp', 'htmlhelp.htm#OR_Managing_Vendors.htm#proc-create_vendor',0);

--changeSet DEV-609:436 stripComments:false
insert into utl_help values ('/maintenix/web/vendor/VendorDetails.jsp', 'htmlhelp.htm#vendor_details_page.htm',0);

--changeSet DEV-609:437 stripComments:false
insert into utl_help values ('/maintenix/web/vendor/VendorSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:438 stripComments:false
insert into utl_help values ('/maintenix/web/warranty/CreateEditWarrantyContract.jsp', 'htmlhelp.htm#F_Warranty_Contracts.htm#proc-create_warranty_contract',0);

--changeSet DEV-609:439 stripComments:false
insert into utl_help values ('/maintenix/web/warranty/InitEditWarranty.jsp', 'htmlhelp.htm#F_Warranty_Contracts.htm#proc-edit_initialize_warranty',0);

--changeSet DEV-609:440 stripComments:false
insert into utl_help values ('/maintenix/web/warranty/WarrantyContractDetails.jsp', 'htmlhelp.htm#warranty_contract_details_page.htm',0);

--changeSet DEV-609:441 stripComments:false
insert into utl_help values ('/maintenix/web/warranty/WarrantyContractSearch.jsp', 'htmlhelp.htm#performing_searches.htm',0);

--changeSet DEV-609:442 stripComments:false
insert into utl_help values ('/maintenix/web/workflow/ApprovalLevelDetails.jsp', 'htmlhelp.htm#approval_levels_details_page.htm',0);

--changeSet DEV-609:443 stripComments:false
insert into utl_help values ('/maintenix/web/workflow/AppWorkflowDefnDetails.jsp', 'htmlhelp.htm#approvalworkflows_details_page.htm',0);

--changeSet DEV-609:444 stripComments:false
insert into utl_help values ('/maintenix/web/workflow/CreateEditApprovalLevel.jsp', 'htmlhelp.htm#ME_task_definition_approval_workflows.htm#proc-create_new_approval_level',0);

--changeSet DEV-609:445 stripComments:false
insert into utl_help values ('/maintenix/web/workflow/CreateEditAppWorkflowDefn.jsp', 'htmlhelp.htm#ME_task_definition_approval_workflows.htm#proc-create_approval_workflow',0);

--changeSet DEV-609:446 stripComments:false
insert into utl_help values ('/maintenix/web/workflow/EditApprovalLevelOrder.jsp', 'htmlhelp.htm#ME_task_definition_approval_workflows.htm#proc-revise_approval_workflow',0);

--changeSet DEV-609:447 stripComments:false
insert into utl_help values ('/maintenix/common/alert/CreateLoginAlert.jsp', 'htmlhelp.htm#Setting_Up_Alerts.htm#proc-create_login_alert',0);

--changeSet DEV-609:448 stripComments:false
insert into utl_help values ('/maintenix/common/alert/UserAlerts.jsp', 'htmlhelp.htm#alerts_menu.htm#alerts_menu',0);

--changeSet DEV-609:449 stripComments:false
insert into utl_help values ('/maintenix/common/integration/LogSearch.jsp', 'htmlhelp.htm#message_search_page.htm',0);

--changeSet DEV-609:450 stripComments:false
insert into utl_help values ('/maintenix/common/integration/MessageDetails.jsp', 'htmlhelp.htm#message_details_page.htm',0);

--changeSet DEV-609:451 stripComments:false
insert into utl_help values ('/maintenix/common/integration/SendMessage.jsp', 'htmlhelp.htm#Sending_Messages_Manually.htm#proc-send_message',0);

--changeSet DEV-609:452 stripComments:false
insert into utl_help values ('/maintenix/common/job/JobViewer.jsp', 'htmlhelp.htm#Mx_Jobs.htm',0);

--changeSet DEV-609:453 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabAssemblyList', 'htmlhelp.htm#assembly_list_tab.htm',0);

--changeSet DEV-609:454 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabCapacityPatternSetup', 'htmlhelp.htm#capacity_pattern_setup_tab.htm',0);

--changeSet DEV-609:455 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabCapacitySummary', 'htmlhelp.htm#capacity_summary_tab.htm',0);

--changeSet DEV-609:456 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabCondemned', 'htmlhelp.htm#condemned_tab.html',0);

--changeSet DEV-609:457 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabDeferralReference', 'htmlhelp.htm#deferral_references_tab.htm',0);

--changeSet DEV-609:458 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabExtractionRuleList', 'htmlhelp.htm#extraction_rules_tab.htm',0);

--changeSet DEV-609:459 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabFaultThreshold', 'htmlhelp.htm#fault_thresholds_tab.htm',0);

--changeSet DEV-609:460 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabHighOilConsumption', 'htmlhelp.htm#high_oil_consumption_tab.htm',0);

--changeSet DEV-609:461 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabIncompleteKits', 'htmlhelp.htm#incomplete_kits_tab.htm',0);

--changeSet DEV-609:462 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabInspectionRequired', 'htmlhelp.htm#inspection_required_tab.htm',0);

--changeSet DEV-609:463 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabLooseInventoryDue', 'htmlhelp.htm#loose_inventory_due_tab.htm',0);

--changeSet DEV-609:464 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabMyAlertList', 'htmlhelp.htm#my_alert_list_tab.htm',0);

--changeSet DEV-609:465 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabOpenPartRequests', 'htmlhelp.htm#open_part_requests_tab.htm',0);

--changeSet DEV-609:466 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabOverCompleteKits', 'htmlhelp.htm#over_complete_kits_tab.htm',0);

--changeSet DEV-609:467 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabPlanningTypes', 'htmlhelp.htm#planning_types_tab.htm',0);

--changeSet DEV-609:468 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabQuarantine', 'htmlhelp.htm#quarantine_tab.htm',0);

--changeSet DEV-609:469 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabQuarantineCorrectiveActions', 'htmlhelp.htm#quarantine_corrective_actions_tab.htm',0);

--changeSet DEV-609:470 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabShiftSchedule', 'htmlhelp.htm#shift_schedule_tab.htm',0);

--changeSet DEV-609:471 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabShiftSetup', 'htmlhelp.htm#shift_setup_tab.htm',0);

--changeSet DEV-609:472 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabTurnIn', 'htmlhelp.htm#turn_in_tab.htm',0);

--changeSet DEV-609:473 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabUnAssignedAlertList', 'htmlhelp.htm#unassigned_alert_list_tab.htm',0);

--changeSet DEV-609:474 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabUnserviceableStaging', 'htmlhelp.htm#unserviceable_staging_tab.htm',0);

--changeSet DEV-609:475 stripComments:false
insert into utl_help values ('/maintenix/common/ToDoList.jsp?aTab=idTabUserShiftPatternSetup', 'htmlhelp.htm#user_shift_pattern_setup_tab.htm',0);