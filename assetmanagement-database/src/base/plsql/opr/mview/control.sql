/*
 * Opr's Materialized Views Control File
 *
 * This file manages the order of execution for the base opr mviews.
 * The base scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */
@opr/mview/aopr_comp_rmvl_mv1.sql
@opr/mview/aopr_logbook_fault_mv1.sql
