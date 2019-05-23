/*
 * API's Views Control File
 *
 * This file manages the order of execution for the base api views.
 * The scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */

@api/view/acor_accounts_v1.sql
@api/view/acor_acft_usg_wp_completion_v1.sql
@api/view/acor_task_work_type_v1.sql
@api/view/acor_inv_aircraft_v1.sql
@api/view/acor_req_ata_v1.sql
@api/view/acor_req_defn_v1.sql
@api/view/acor_orders_v1.sql
@api/view/acor_order_invoices_v1.sql
@api/view/acor_workpackages_v1.sql
@api/view/acor_workpackages_v2.sql

@api/view/acor_acft_wp_v1.sql
@api/view/acor_acft_wp_workscope_v1.sql
@api/view/acor_acft_wp_labor_v1.sql
@api/view/acor_acft_wp_parts_v1.sql
@api/view/acor_acft_wp_steps_v1.sql
@api/view/acor_acft_wp_task_v1.sql
@api/view/acor_acft_wp_tools_v1.sql

@api/view/acor_aircraft_fault_v1.sql
@api/view/acor_aircraft_usage_v1.sql
@api/view/acor_assembly_bom_v1.sql
@api/view/acor_assembly_subtype_v1.sql
@api/view/acor_assembly_v1.sql
@api/view/acor_assigned_tasks_to_wp_v1.sql
@api/view/acor_assy_oil_threshold_v1.sql

@api/view/acor_2digit_ata_code_v1.sql

@api/view/acor_assy_wp_v1.sql
@api/view/acor_assy_wp_workscope_v1.sql
@api/view/acor_assy_wp_labor_v1.sql
@api/view/acor_assy_wp_parts_v1.sql
@api/view/acor_assy_wp_steps_v1.sql
@api/view/acor_assy_wp_tools_v1.sql

@api/view/acor_baselinetask_deadlines_v1.sql
@api/view/acor_block_acft_sched_rules_v1.sql

@api/view/acor_block_defn_v1.sql
@api/view/acor_block_defn_sched_rule_v1.sql

@api/view/acor_acft_ts_adhoc_v1.sql
@api/view/acor_acft_ts_block_v1.sql
@api/view/acor_acft_ts_deadline_v1.sql
@api/view/acor_acft_ts_req_v1.sql

@api/view/acor_block_part_sched_rule_v1.sql
@api/view/acor_block_req_map_v1.sql
@api/view/acor_borrow_orders_v1.sql
@api/view/acor_comp_ts_adhoc_v1.sql
@api/view/acor_comp_ts_deadline_v1.sql
@api/view/acor_comp_ts_req_v1.sql
@api/view/acor_comp_usg_atremoval_v1.sql
@api/view/acor_comp_usg_wp_completion_v1.sql

@api/view/acor_comp_wp_v1.sql
@api/view/acor_comp_wp_workscope_v1.sql
@api/view/acor_comp_wp_labor_v1.sql
@api/view/acor_comp_wp_parts_v1.sql
@api/view/acor_comp_wp_steps_v1.sql
@api/view/acor_comp_wp_tools_v1.sql

@api/view/acor_consign_orders_v1.sql
@api/view/acor_corrective_task_v1.sql
@api/view/acor_csgnxchg_orders_v1.sql
@api/view/acor_csn_stock_requests_v1.sql
@api/view/acor_delay_v1.sql
@api/view/acor_department_v1.sql
@api/view/acor_eqp_task_panel_v1.sql
@api/view/acor_eqp_task_zone_v1.sql
@api/view/acor_exchange_orders_v1.sql

@api/view/acor_fault_v1.sql
@api/view/acor_fault_action_v1.sql
@api/view/acor_fault_deadline_v1.sql
@api/view/acor_fault_labor_v1.sql
@api/view/acor_fault_parts_v1.sql
@api/view/acor_fault_result_v1.sql
@api/view/acor_fault_stage_notes_v1.sql
@api/view/acor_fault_step_v1.sql
@api/view/acor_fault_usage_v1.sql

@api/view/acor_financial_trx_accounts_v1.sql
@api/view/acor_financial_trx_v1.sql
@api/view/acor_flights_v1.sql
@api/view/acor_fl_acft_usage_v1.sql
@api/view/acor_fl_disruptions_v1.sql
@api/view/acor_fl_faults_v1.sql
@api/view/acor_fl_measurements_v1.sql
@api/view/acor_fnc_trx_wp_info_v1.sql
@api/view/acor_fnc_trx_wp_ac_info_v1.sql
@api/view/acor_hist_comp_rmvl_v1.sql
@api/view/acor_hist_ts_deadline_v1.sql

@api/view/acor_hr_v1.sql
@api/view/acor_hr_authority_v1.sql
@api/view/acor_hr_department_v1.sql
@api/view/acor_hr_license_v1.sql
@api/view/acor_hr_po_auth_level_v1.sql
@api/view/acor_hr_skill_v1.sql

@api/view/acor_location_v1.sql
@api/view/acor_loc_repairable_parts_v1.sql

@api/view/acor_logbook_fault_v1.sql
@api/view/acor_lookup_deferral_class.sql
@api/view/acor_lookup_fault_severity.sql
@api/view/acor_lookup_fault_source.sql
@api/view/acor_maint_prgm_task_v1.sql
@api/view/acor_maint_prgm_v1.sql
@api/view/acor_non_routine_fault_v1.sql
@api/view/acor_operator_v1.sql

@api/view/acor_order_filled_requests_v1.sql
@api/view/acor_order_invoice_lines_v1.sql
@api/view/acor_order_invoice_mapping_v1.sql
@api/view/acor_order_lines_v1.sql
@api/view/acor_order_line_charge_v1.sql
@api/view/acor_order_line_tax_v1.sql
@api/view/acor_order_receipts_v1.sql
@api/view/acor_order_receipts_v2.sql
@api/view/acor_order_returns_v1.sql

@api/view/acor_organization_v1.sql
@api/view/acor_part_exchange_vendor_v1.sql
@api/view/acor_part_number_v1.sql
@api/view/acor_part_purchase_vendor_v1.sql
@api/view/acor_part_repair_vendor_v1.sql
@api/view/acor_part_requests_v1.sql
@api/view/acor_purchase_orders_v1.sql
@api/view/acor_rbl_acft_assy_type_v1.sql
@api/view/acor_rbl_acft_part_qty_v1.sql
@api/view/acor_rbl_comp_removal_v1.sql
@api/view/acor_rbl_eng_usage_v1.sql


@api/view/acor_inv_assembly_v1.sql
@api/view/acor_inv_batch_allocation_v1.sql
@api/view/acor_inv_batch_tool_alloc_v1.sql
@api/view/acor_inv_batch_tool_v1.sql
@api/view/acor_inv_batch_v1.sql
@api/view/acor_inv_current_usage_v1.sql
@api/view/acor_inv_info_v1.sql
@api/view/acor_inv_kit_v1.sql
@api/view/acor_inv_serialized_tool_v1.sql
@api/view/acor_inv_serialized_v1.sql
@api/view/acor_inv_serial_cntrl_asset_v1.sql
@api/view/acor_inv_system_v1.sql
@api/view/acor_inv_tracked_v1.sql
@api/view/acor_inventory_value_v1.sql

@api/view/acor_rbl_eng_v1.sql
@api/view/acor_rbl_eng_wp_loc_v1.sql

@api/view/acor_rbl_pirepmarep_v1.sql
@api/view/acor_rbl_subassy_type_v1.sql
@api/view/acor_ref_delay_reason.sql
@api/view/acor_ref_docs_compl_link_v1.sql
@api/view/acor_ref_doc_defn_v1.sql
@api/view/acor_reg_body_v1.sql
@api/view/acor_repair_orders_v1.sql
@api/view/acor_repetitive_faults_v1.sql
@api/view/acor_repetitive_faults_info_v1.sql
@api/view/acor_req_acft_sched_rule_v1.sql
@api/view/acor_req_defn_compl_link_v1.sql
@api/view/acor_req_exec_ata_v1.sql
@api/view/acor_req_exec_labor_v1.sql
@api/view/acor_req_exec_parts_v1.sql
@api/view/acor_req_exec_pn_v1.sql
@api/view/acor_req_exec_tools_v1.sql
@api/view/acor_req_ietm_v1.sql
@api/view/acor_req_panel_v1.sql
@api/view/acor_req_part_sched_rules_v1.sql
@api/view/acor_req_part_v1.sql
@api/view/acor_req_pn_v1.sql
@api/view/acor_req_sched_rules_v1.sql
@api/view/acor_req_zone_v1.sql

@api/view/acor_rfq_v1.sql
@api/view/acor_rfq_filled_requests_v1.sql
@api/view/acor_rfq_lines_v1.sql
@api/view/acor_rfq_quotes_v1.sql
@api/view/acor_rfq_vendor_quotes_v1.sql

@api/view/acor_sched_labour_v1.sql
@api/view/acor_sched_panel_list_v1.sql
@api/view/acor_sched_zone_list_v1.sql

@api/view/acor_shipments_routing_v1.sql
@api/view/acor_shipments_lines_v1.sql
@api/view/acor_shipments_v1.sql

@api/view/acor_shipment_info_v1.sql
@api/view/acor_shop_findings_fault_v1.sql
@api/view/acor_stocks_v1.sql
@api/view/acor_stock_levels_v1.sql
@api/view/acor_stock_parts_v1.sql
@api/view/acor_stock_requests_v1.sql
@api/view/acor_task_defn_ietm_v1.sql
@api/view/acor_task_panel_v1.sql
@api/view/acor_task_part_requests_v1.sql
@api/view/acor_task_zone_list_v1.sql

@api/view/acor_jic_v1.sql
@api/view/acor_jic_ata_v1.sql
@api/view/acor_jic_cond_v1.sql
@api/view/acor_jic_history_v1.sql
@api/view/acor_jic_labor_v1.sql
@api/view/acor_jic_parts_v1.sql
@api/view/acor_jic_pn_v1.sql
@api/view/acor_jic_req_map_v1.sql
@api/view/acor_jic_steps_v1.sql
@api/view/acor_jic_tools_v1.sql
@api/view/acor_mpc_closepanel_jic_v1.sql
@api/view/acor_mpc_cond_v1.sql
@api/view/acor_mpc_defn_v1.sql
@api/view/acor_mpc_labor_v1.sql
@api/view/acor_mpc_openpanel_jic_v1.sql
@api/view/acor_mpc_parts_v1.sql
@api/view/acor_mpc_steps_v1.sql
@api/view/acor_mpc_tools_v1.sql

@api/view/acor_vendors_v1.sql
@api/view/acor_wp_task_deadlines_v1.sql
@api/view/acor_wp_task_work_type_v1.sql
