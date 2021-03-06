/*
 * Opr's Views Control File
 *
 * This file manages the order of execution for the base opr views.
 * The base scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */

@opr/view/aopr_acft_actv_ts_adhoc_v1.sql
@opr/view/aopr_acft_actv_ts_block_v1.sql
@opr/view/aopr_acft_actv_ts_req_v1.sql
@opr/view/aopr_acft_ad_compl_v1.sql
@opr/view/aopr_acft_ad_compl_eo_v1.sql
@opr/view/aopr_acft_ad_compl_sb_v1.sql
@opr/view/aopr_acft_hist_ts_req_ldone_v1.sql
@opr/view/aopr_acft_registration_v1.sql
@opr/view/aopr_acft_sb_compl_v1.sql
@opr/view/aopr_acft_sb_compl_eo_v1.sql
@opr/view/aopr_acft_ts_adhoc_ldone_v1.sql
@opr/view/aopr_acft_ts_block_ldone_v1.sql
@opr/view/aopr_actv_deferred_fault_v1.sql
@opr/view/aopr_ad_status_acft_v1.sql
@opr/view/aopr_ad_status_assmbl_v1.sql
@opr/view/aopr_ad_status_comp_v1.sql
@opr/view/aopr_airport_inv_rfi_v1.sql
@opr/view/aopr_assmbl_ad_compl_v1.sql
@opr/view/aopr_assmbl_ad_compl_eo_v1.sql
@opr/view/aopr_assmbl_ad_compl_sb_v1.sql
@opr/view/aopr_assmbl_sb_compl_v1.sql
@opr/view/aopr_assmbl_sb_compl_eo_v1.sql
@opr/view/aopr_borrow_orders_v1.sql
@opr/view/aopr_comp_actv_ts_adhoc_v1.sql
@opr/view/aopr_comp_actv_ts_req_v1.sql
@opr/view/aopr_comp_ad_compl_v1.sql
@opr/view/aopr_comp_ad_compl_eo_v1.sql
@opr/view/aopr_comp_ad_compl_sb_v1.sql
@opr/view/aopr_comp_hist_ts_req_ldone_v1.sql
@opr/view/aopr_assmbl_eo_compl_v1.sql
@opr/view/aopr_comp_eo_compl_v1.sql
@opr/view/aopr_acft_eo_compl_v1.sql
@opr/view/aopr_comp_sb_compl_v1.sql
@opr/view/aopr_comp_sb_compl_eo_v1.sql
@opr/view/aopr_comp_ts_adhoc_ldone_v1.sql
@opr/view/aopr_consign_orders_v1.sql
@opr/view/aopr_csgnxchg_orders_v1.sql
@opr/view/aopr_exchange_orders_v1.sql
@opr/view/aopr_financial_trx_accounts_v1.sql
@opr/view/aopr_financial_trx_v1.sql
@opr/view/aopr_fl_faults_v1.sql
@opr/view/aopr_hr_active_license_v1.sql
@opr/view/aopr_hr_inactive_license_v1.sql
@opr/view/aopr_hr_v1.sql
@opr/view/aopr_logbook_fault_v1.sql
@opr/view/aopr_non_routine_fault_v1.sql
@opr/view/aopr_operator_v1.sql
@opr/view/aopr_orders_v1.sql
@opr/view/aopr_order_filled_requests_v1.sql
@opr/view/aopr_order_invoices_v1.sql
@opr/view/aopr_order_invoice_lines_v1.sql
@opr/view/aopr_order_invoice_mapping_v1.sql
@opr/view/aopr_order_lines_v1.sql
@opr/view/aopr_order_line_charge_v1.sql
@opr/view/aopr_order_line_tax_v1.sql
@opr/view/aopr_order_receipts_v1.sql
@opr/view/aopr_order_returns_v1.sql
@opr/view/aopr_purchase_orders_v1.sql
@opr/view/aopr_ref_doc_defn_v1.sql
@opr/view/aopr_repair_orders_v1.sql
@opr/view/aopr_req_ietm_part_access_v1.sql
@opr/view/aopr_rfq_filled_requests_v1.sql
@opr/view/aopr_rfq_lines_v1.sql
@opr/view/aopr_rfq_quotes_v1.sql
@opr/view/aopr_rfq_v1.sql
@opr/view/aopr_rfq_vendor_quotes_v1.sql
@opr/view/aopr_sb_status_acft_v1.sql
@opr/view/aopr_sb_status_assmbl_v1.sql
@opr/view/aopr_sb_status_comp_v1.sql
