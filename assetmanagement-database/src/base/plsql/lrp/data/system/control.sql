/*
 * LRP's System Data Control File
 *
 * This file manages the order of execution for the base lrp system data.
 * The scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */

@lrp/data/system/0ref_lrp_duration_mode.sql
@lrp/data/system/0utl_report_type.sql
