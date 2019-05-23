/*
 * Reliability's Views Control File
 *
 * This file manages the order of execution for the base rbl views.
 * The base scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */

@rbl/view/aopr_calendar_month_v1.sql
@rbl/view/aopr_fleet_movement_v1.sql
@rbl/view/aopr_hist_comp_rmvl_basic_v1.sql
@rbl/view/aopr_hist_logfaults_basic_v1.sql
@rbl/view/aopr_rbl_acft_eng_type_v1.sql
@rbl/view/aopr_rbl_fault_v1.sql
@rbl/view/aopr_rbl_fleet_fault_v1.sql
@rbl/view/acor_rbl_eng_measure_v1.sql
