--liquibase formatted sql


--changeSet MX-17763:1 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/alert/AddNote.jsp', 'htmlhelp.htm#user_alerts.htm#proc-add_note_to_alert',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/alert/AddNote.jsp' );                

--changeSet MX-17763:2 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/alert/DispositionAlert.jsp', 'htmlhelp.htm#user_alerts.htm#proc-disposition_alert',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/alert/DispositionAlert.jsp' );    

--changeSet MX-17763:3 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/assembly/oilconsumption/AddOperatorSpecificThreshold.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-add_operator_specific_threshold',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/assembly/oilconsumption/AddOperatorSpecificThreshold.jsp' );          

--changeSet MX-17763:4 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/assembly/oilconsumption/AddPartNoSpecificThreshold.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-add_part_specific_threshold',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/assembly/oilconsumption/AddPartNoSpecificThreshold.jsp' );    

--changeSet MX-17763:5 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/assembly/oilconsumption/EditAssemblyThreshold.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-edit_assembly_thresholds',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/assembly/oilconsumption/EditAssemblyThreshold.jsp' );                

--changeSet MX-17763:6 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/assembly/oilconsumption/EditOperatorSpecificThreshold.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-edit_operator_specific_threshold',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/assembly/oilconsumption/EditOperatorSpecificThreshold.jsp' );    

--changeSet MX-17763:7 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/assembly/oilconsumption/EditPartNoSpecificThreshold.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-edit_part_no_specific_threshold',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/assembly/oilconsumption/EditPartNoSpecificThreshold.jsp' );          

--changeSet MX-17763:8 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/assembly/oilconsumption/EditSerialNoSpecificThreshold.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-edit_serial_no_specific_threshold',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/assembly/oilconsumption/EditSerialNoSpecificThreshold.jsp' );    

--changeSet MX-17763:9 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/assembly/oilconsumption/OilConsumptionRateDefinition.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-add_or_edit_oil_consumption_rate_defn',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/assembly/oilconsumption/OilConsumptionRateDefinition.jsp' );          

--changeSet MX-17763:10 stripComments:false
UPDATE utl_help
  SET help_topic = 'htmlhelp.htm#LM_Line_Maintenance_Execution.htm#proc-create_fault'
  WHERE help_context = '/maintenix/web/fault/RaiseFault.jsp';              

--changeSet MX-17763:11 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/fault/SelectResultEvent.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Execution.htm#proc-create_fault',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/fault/SelectResultEvent.jsp' );    

--changeSet MX-17763:12 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/impacts/AddEditImpacts.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-add_impact_to_req',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/impacts/AddEditImpacts.jsp' );          

--changeSet MX-17763:13 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/inventory/oilconsumption/EscalateOilConsumptionStatus.jsp', 'htmlhelp.htm#ME_Assemblies.htm#proc-automatic_oil_consumption_status',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/inventory/oilconsumption/EscalateOilConsumptionStatus.jsp' );    

--changeSet MX-17763:14 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/labour/EditLabourHistory.jsp', 'htmlhelp.htm#M_Executing_Maintenance_Tasks#proc-edit_labor_history_task',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/labour/EditLabourHistory.jsp' );                

--changeSet MX-17763:15 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/labour/EditWPLabourHistory.jsp', 'htmlhelp.htm#M_Executing_Maintenance_Tasks#proc-edit_labor_history_work_package',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/labour/EditWPLabourHistory.jsp');    

--changeSet MX-17763:16 stripComments:false
DELETE FROM utl_help
  WHERE help_context = '/maintenix/web/location/CreateScheduleDefinition.jsp';      

--changeSet MX-17763:17 stripComments:false
DELETE FROM utl_help
  WHERE help_context = '/maintenix/web/location/CreateShift.jsp';  

--changeSet MX-17763:18 stripComments:false
DELETE FROM utl_help
  WHERE help_context = '/maintenix/web/location/EditScheduleShift.jsp';      

--changeSet MX-17763:19 stripComments:false
DELETE FROM utl_help
  WHERE help_context = '/maintenix/web/location/ScheduleDefinitionDetails.jsp';      

--changeSet MX-17763:20 stripComments:false
DELETE FROM utl_help
  WHERE help_context = '/maintenix/web/panel/EditPanel.jsp';                      

--changeSet MX-17763:21 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/panel/PanelDetails.jsp', 'htmlhelp.htm#panel_details_page.htm',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/panel/PanelDetails.jsp' );          

--changeSet MX-17763:22 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/part/EditInstallKitMap.jsp', 'htmlhelp.htm#ME_kits.htm#proc-edit_install_kit_map',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/part/EditInstallKitMap.jsp' );    

--changeSet MX-17763:23 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/part/EditInstallKits.jsp', 'htmlhelp.htm#ME_kits.htm#proc-adjut_install_kit_for_part_group',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/part/EditInstallKits.jsp');                

--changeSet MX-17763:24 stripComments:false
UPDATE utl_help
  SET help_topic = 'htmlhelp.htm#exchange_vendor_details_page.htm'
  WHERE help_context = '/maintenix/web/part/ExchangeVendorDetails.jsp';      

--changeSet MX-17763:25 stripComments:false
UPDATE utl_help
  SET help_topic = 'htmlhelp.htm#part_details_page.htm'
  WHERE help_context = '/maintenix/web/part/PartDetails.jsp';          

--changeSet MX-17763:26 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/part/GetPartsForAssembly.jsp', 'htmlhelp.htm#ME_Assemblies.htm',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/part/GetPartsForAssembly.jsp');          

--changeSet MX-17763:27 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/shift/CapacityPatternDetails.jsp', 'htmlhelp.htm#capacity_pattern_details_page.htm',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/shift/CapacityPatternDetails.jsp' );    

--changeSet MX-17763:28 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/shift/CreateEditCapacityPattern.jsp', 'htmlhelp.htm#HM_Heavy_Maintenance_Visit_Planning.htm#proc-add_capacity_pattern',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/shift/CreateEditCapacityPattern.jsp');                   

--changeSet MX-17763:29 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/shift/CreateEditShift.jsp', 'htmlhelp.htm#U_shifts_and_schedules.htm#proc-create_shift',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/shift/CreateEditShift.jsp');          

--changeSet MX-17763:30 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/shift/DuplicateCapacityPattern.jsp', 'htmlhelp.htm#HM_Heavy_Maintenance_Visit_Planning.htm#proc-duplicate_capacity_pattern',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/shift/DuplicateCapacityPattern.jsp');    

--changeSet MX-17763:31 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/shift/EditLaborProfile.jsp', 'htmlhelp.htm#HM_Heavy_Maintenance_Visit_Planning.htm#proc-edit_labor_profile_for_capacity_pattern',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/shift/EditLaborProfile.jsp');                      

--changeSet MX-17763:32 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/shift/EditLaborSkills.jsp', 'htmlhelp.htm#HM_Heavy_Maintenance_Visit_Planning.htm#proc-edit_labor_skills_for_cap_patt',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/shift/EditLaborSkills.jsp');          

--changeSet MX-17763:33 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/shift/usershiftpattern/CreateEditUserShiftPattern.jsp', 'htmlhelp.htm#U_shifts_and_schedules.htm#proc-create_schedule',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/shift/usershiftpattern/CreateEditUserShiftPattern.jsp');    

--changeSet MX-17763:34 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/shift/usershiftpattern/UserShiftPatternDetails.jsp', 'htmlhelp.htm#user_shift_pattern_details_page.htm',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/shift/usershiftpattern/UserShiftPatternDetails.jsp');                      

--changeSet MX-17763:35 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/shipment/InstallKitsAvailable.jsp', 'htmlhelp.htm#MM_shipping_inventory.htm#proc-add_part_shipment_line',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/shipment/InstallKitsAvailable.jsp');          

--changeSet MX-17763:36 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/task/EditEstimatedEndDate.jsp', 'htmlhelp.htm#LM_Line_Maintenance_Planning.htm#proc-edit_est_end_date_work_package',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/task/EditEstimatedEndDate.jsp');    

--changeSet MX-17763:37 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/task/EditWorkscopeOrder.jsp', 'htmlhelp.htm#SM_Shop_Maintenance_Control.htm#proc-edit_workscope_order',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/task/EditWorkscopeOrder.jsp');                      

--changeSet MX-17763:38 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/task/EnforceNSVDeadlines.jsp', 'htmlhelp.htm#SM_Shop_Maintenance_Control.htm#proc-enforce_NSV_deadlines',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/task/EnforceNSVDeadlines.jsp');          

--changeSet MX-17763:39 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/task/SetPlanByDate.jsp', 'htmlhelp.htm#ME_requirements.htm#proc-set_plan_by_date',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/task/SetPlanByDate.jsp');    

--changeSet MX-17763:40 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/task/ViewPreviousMeasurements.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards#proc-add_measurement',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/task/ViewPreviousMeasurements.jsp');                      

--changeSet MX-17763:41 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/task/WarningsBand.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/task/WarningsBand.jsp');          

--changeSet MX-17763:42 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/task/labour/EditWorkCapture.jsp', 'htmlhelp.htm#M_Executing_Maintenance_Tasks#proc-edit_work_captured',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/task/labour/EditWorkCapture.jsp');    

--changeSet MX-17763:43 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/task/labour/WorkCapture.jsp', 'htmlhelp.htm#M_Executing_Maintenance_Tasks#proc-finish_a_task',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/task/labour/WorkCapture.jsp');             

--changeSet MX-17763:44 stripComments:false
DELETE FROM utl_help
  WHERE help_context = '/maintenix/web/taskdefn/AddNREstimate.jsp';       

--changeSet MX-17763:45 stripComments:false
DELETE FROM utl_help
  WHERE help_context = '/maintenix/web/taskdefn/EditNRLabourEstimates.jsp';           

--changeSet MX-17763:46 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/taskdefn/CreateEditPlanningType.jsp', 'htmlhelp.htm#HM_Heavy_Maintenance_Visit_Planning.htm#proc-ppc_create_planning_type',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/taskdefn/CreateEditPlanningType.jsp');          

--changeSet MX-17763:47 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/taskdefn/EditPlanningValues.jsp', 'htmlhelp.htm#HM_Heavy_Maintenance_Visit_Planning.htm#proc-ppc_set_nr_for_block_def',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/taskdefn/EditPlanningValues.jsp');    

--changeSet MX-17763:48 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/taskdefn/InstallKitsAvailable.jsp', 'htmlhelp.htm#ME_Maintenance_Job_Instruction_Cards#proc-add_part_requirement',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/taskdefn/InstallKitsAvailable.jsp');           

--changeSet MX-17763:49 stripComments:false
UPDATE utl_help
  SET help_topic =  'htmlhelp.htm#master_panel_details_page.htm'
  WHERE help_context = '/maintenix/web/taskdefn/MPCDetails.jsp';      

--changeSet MX-17763:50 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/taskdefn/ShowHideDifferences.jsp', 'htmlhelp.htm#ME_Task_Definition.htm#proc-show_task_def_differences',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/taskdefn/ShowHideDifferences.jsp');          

--changeSet MX-17763:51 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/taskdefn/ToggleEnforceWorkscopeOrder.jsp', 'htmlhelp.htm#SM_Shop_Maintenance_Control.htm#proc-enforce_workscope_order',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/taskdefn/ToggleEnforceWorkscopeOrder.jsp');    

--changeSet MX-17763:52 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/web/user/AddEditHrUserShiftPattern.jsp', 'htmlhelp.htm#U_shifts_and_schedules.htm#proc-assign_schedule_to_user',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/user/AddEditHrUserShiftPattern.jsp');                 

--changeSet MX-17763:53 stripComments:false
DELETE FROM utl_help
  WHERE help_context = '/maintenix/web/user/AddEditHrSchedule.jsp';         

--changeSet MX-17763:54 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/common/ToDoList.jsp?aTab=idTabCapacityPatternSetup', 'htmlhelp.htm#capacity_pattern_setup_tab.htm',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/common/ToDoList.jsp?aTab=idTabCapacityPatternSetup');          

--changeSet MX-17763:55 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/common/ToDoList.jsp?aTab=idTabCapacitySummary', 'htmlhelp.htm#capacity_summary_tab.htm',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/common/ToDoList.jsp?aTab=idTabCapacitySummary');    

--changeSet MX-17763:56 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/common/ToDoList.jsp?aTab=idTabHighOilConsumption', 'htmlhelp.htm#high_oil_consumption_tab.htm',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/common/ToDoList.jsp?aTab=idTabHighOilConsumption');                       

--changeSet MX-17763:57 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/common/ToDoList.jsp?aTab=idTabPlanningTypes', 'htmlhelp.htm#planning_types_tab.htm',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/common/ToDoList.jsp?aTab=idTabPlanningTypes');          

--changeSet MX-17763:58 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/common/ToDoList.jsp?aTab=idTabShiftSchedule', 'htmlhelp.htm#shift_schedule_tab.htm',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/common/ToDoList.jsp?aTab=idTabShiftSchedule');    

--changeSet MX-17763:59 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/common/ToDoList.jsp?aTab=idTabShiftSetup', 'htmlhelp.htm#shift_setup_tab.htm',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/common/ToDoList.jsp?aTab=idTabShiftSetup');                 

--changeSet MX-17763:60 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/common/ToDoList.jsp?aTab=idTabUnserviceableStaging', 'htmlhelp.htm#unserviceable_staging_tab.htm',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/common/ToDoList.jsp?aTab=idTabUnserviceableStaging');          

--changeSet MX-17763:61 stripComments:false
INSERT INTO
   utl_help
   (
      help_context, help_topic, utl_id
   )
   SELECT '/maintenix/common/ToDoList.jsp?aTab=idTabUserShiftPatternSetup', 'htmlhelp.htm#user_shift_pattern_setup_tab.htm',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/common/ToDoList.jsp?aTab=idTabUserShiftPatternSetup');                                  