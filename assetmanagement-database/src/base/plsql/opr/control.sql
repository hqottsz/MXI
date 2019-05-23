/*
 * Opr's Control File
 *
 * This file manages the order of execution for the base opr.
 * The base scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */
@opr/schema/opr.sql
@opr/plsql/control.sql
@opr/mview/control.sql
@opr/view/control.sql
@opr/data/control.sql
@opr/schema/funcindex.sql