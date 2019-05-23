/*
 * Opr's Data Control File
 *
 * This file manages the order of execution for the base opr data.
 * The base scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */
@opr/data/0utl_menu_item.sql
@opr/data/0utl_menu_item_arg.sql
@opr/data/0utl_report_type.sql
@opr/data/opr_ad_status.sql
@opr/data/opr_sb_status.sql
