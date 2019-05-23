/*
 * Reliability's Data Control File
 *
 * This file manages the order of execution for the base rbl data.
 * The base scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */

@rbl/data/opr_fleet_movement.sql
@rbl/data/opr_rbl_delay_category.sql
@rbl/data/opr_rbl_eng_indicator_measure.sql
@rbl/data/opr_rbl_eng_shopvisit_loc.sql
@rbl/data/opr_report.sql
@rbl/data/opr_report_job.sql
@rbl/data/opr_report_mview.sql
@rbl/data/utl_config_parm.sql
@rbl/data/utl_job.sql
@rbl/data/xx_opr_calendar_month.sql
