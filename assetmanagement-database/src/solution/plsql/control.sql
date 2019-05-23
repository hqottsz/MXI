/*
 * Maintenix's Solution Data Control File
 *
 * This file manages the order of execution for the base rbl views.
 * The base scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */

@system/10charge.sql
@system/10utl_role.sql
@system/10utl_role_parm.sql
@system/10utl_action_role_parm.sql
@system/10utl_todo_list.sql
@system/10utl_todo_list_button.sql
@system/10utl_todo_list_tab.sql
@system/10utl_menu_item.sql
@system/10utl_menu_group.sql
@system/10utl_menu_group_item.sql
@system/10utl_alert_type_role.sql
@system/10utl_timezone.sql
@refterm/10ref_eng_unit.sql
@refterm/10mim_data_type.sql
@refterm/10ref_advsry_type.sql
@refterm/10ref_assmbl_class.sql
@refterm/10ref_attach_type.sql
@refterm/10ref_borrow_rate.sql
@refterm/10ref_country.sql
@refterm/10ref_currency.sql
@refterm/10ref_data_source.sql
@refterm/10ref_dept_type.sql
@refterm/10ref_domain_type.sql
@refterm/10ref_effect_sev.sql
@refterm/10ref_event_reason.sql
@refterm/10ref_event_status.sql
@refterm/10ref_fail_catgry.sql
@refterm/10ref_fail_effect_type.sql
@refterm/10ref_fail_factor.sql
@refterm/10ref_fail_priority.sql
@refterm/10ref_fail_sev.sql
@refterm/10ref_fail_type.sql
@refterm/10ref_fault_source.sql
@refterm/10ref_financial_class.sql
@refterm/10ref_flight_stage.sql
@refterm/10ref_flight_type.sql
@refterm/10ref_fob.sql
@refterm/10ref_inv_capability.sql
@refterm/10ref_inv_oper.sql
@refterm/10ref_labour_skill.sql
@refterm/10ref_labour_time.sql
@refterm/10ref_license_type.sql
@refterm/10ref_loc_type.sql
@refterm/10ref_lrp_priority.sql
@refterm/10ref_oil_status.sql
@refterm/10ref_part_provider_type.sql
@refterm/10ref_part_use.sql
@refterm/10ref_pay_method.sql
@refterm/10ref_po_auth_flow.sql
@refterm/10ref_po_auth_lvl.sql
@refterm/10ref_prec_proc.sql
@refterm/10ref_purch_type.sql
@refterm/10ref_qty_unit.sql
@refterm/10ref_quar_action_catgry.sql
@refterm/10ref_rcv_priority.sql
@refterm/10ref_receive_cond.sql
@refterm/10ref_ref_unit.sql
@refterm/10ref_reg_body.sql
@refterm/10ref_remove_reason.sql
@refterm/10ref_req_action.sql
@refterm/10ref_req_priority.sql
@refterm/10ref_sd_nature.sql
@refterm/10ref_sd_type.sql
@refterm/10ref_service_type.sql
@refterm/10ref_shipment_reason.sql
@refterm/10ref_size_class.sql
@refterm/10ref_stage_reason.sql
@refterm/10ref_state.sql
@refterm/10ref_task_class.sql
@refterm/10ref_task_originator.sql
@refterm/10ref_task_priority.sql
@refterm/10ref_task_rev_reason.sql
@refterm/10ref_task_subclass.sql
@refterm/10ref_terms_conditions.sql
@refterm/10ref_timeoff_type.sql
@refterm/10ref_transport_type.sql
@refterm/10ref_vendor_approval.sql
@refterm/10ref_warranty_priority.sql
@refterm/10ref_warranty_sub_type.sql
@refterm/10ref_wf_priority.sql
@refterm/10ref_wf_step_reason.sql
@refterm/10ref_work_type.sql
@refterm/20ref_event_status.sql
@refterm/20ref_flight_type.sql
@refterm/20ref_inv_capability.sql
@refterm/20ref_inv_oper.sql
@refterm/20ref_labour_skill.sql
@refterm/20ref_sd_type.sql
@refterm/20ref_stage_reason.sql
@refterm/20ref_task_originator.sql
@refterm/10ref_acft_cap.sql
@refterm/10ref_acft_cap_level.sql