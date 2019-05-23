/*
 * Reliability's Materialized Views Control File
 *
 * This file manages the order of execution for the base rbl mviews.
 * The base scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */

@rbl/mview/01-opr_rbl_delay_mv.sql
@rbl/mview/02-opr_rbl_monthly_usage_mv.sql
@rbl/mview/03-opr_rbl_monthly_incident_mv.sql
@rbl/mview/04-opr_rbl_monthly_delay_mv.sql
@rbl/mview/05-opr_rbl_monthly_dispatch_mv.sql
@rbl/mview/06-aopr_reliability_v1.sql
@rbl/mview/07-aopr_tdrl_v1.sql
@rbl/mview/08-aopr_service_difficulty_v1.sql
@rbl/mview/09-aopr_station_delay_v1.sql
@rbl/mview/10-aopr_technical_delay_v1.sql
@rbl/mview/11-aopr_aircraft_delay_v1.sql
@rbl/mview/12-aopr_ata_delay_v1.sql
@rbl/mview/13-aopr_dispatch_metric_v1.sql
@rbl/mview/14-aopr_dispatch_reliability_v1.sql
@rbl/mview/15-aopr_monthly_operator_usage_v1.sql
@rbl/mview/50.0.acor_rbl_comp_ata_lower_fh_mv1.sql
@rbl/mview/50.0.aopr_rbl_comp_rmvl_fleet_ucl_mv1.sql
@rbl/mview/50.0.aopr_rbl_comp_rmvl_flt_12mon_mv1.sql
@rbl/mview/50.0.aopr_rbl_comp_rmvl_flt_24mon_mv1.sql
@rbl/mview/50.0.aopr_rbl_comp_rmvl_flt_6mon_mv1.sql
@rbl/mview/50.0.aopr_rbl_comp_rmvl_flt_curr_mv1.sql
@rbl/mview/50.0.aopr_rbl_comp_rmvl_flt_q1_mv1.sql
@rbl/mview/50.0.aopr_rbl_comp_rmvl_flt_q2_mv1.sql
@rbl/mview/50.0.aopr_rbl_comp_rmvl_flt_q3_mv1.sql
@rbl/mview/50.0.aopr_rbl_comp_rmvl_flt_q4_mv1.sql
@rbl/mview/50.0.aopr_rbl_eng_usage_mon_mv1.sql
@rbl/mview/50.0.aopr_rbl_fault_fleet_ucl_mv1.sql
@rbl/mview/50.0.aopr_rbl_fault_tail_ucl_mv1.sql
@rbl/mview/50.1.aopr_rbl_comp_rmvl_fleet_mv1.sql
@rbl/mview/50.1.aopr_rbl_eng_delay_mon_mv1.sql
@rbl/mview/50.1.aopr_rbl_eng_delay_mv1.sql
@rbl/mview/50.1.aopr_rbl_eng_indicator_mon_mv1.sql
@rbl/mview/50.1.aopr_rbl_eng_indicator_mv1.sql
@rbl/mview/50.1.aopr_rbl_eng_qty_mon_mv1.sql
@rbl/mview/50.1.aopr_rbl_eng_qty_mv1.sql
@rbl/mview/50.1.aopr_rbl_eng_shopvisit_mon_mv1.sql
@rbl/mview/50.1.aopr_rbl_eng_shopvisit_mv1.sql
@rbl/mview/50.1.aopr_rbl_eng_urmvl_mon_mv1.sql
@rbl/mview/50.1.aopr_rbl_eng_urmvl_mv1.sql
@rbl/mview/50.1.aopr_rbl_eng_usage_mv1.sql
@rbl/mview/50.1.aopr_rbl_fault_fleet_mon_mv1.sql
@rbl/mview/50.1.aopr_rbl_fault_fleet_mv1.sql
@rbl/mview/50.1.aopr_rbl_fault_mon_mv1.sql
@rbl/mview/50.1.aopr_rbl_fault_mv1.sql
