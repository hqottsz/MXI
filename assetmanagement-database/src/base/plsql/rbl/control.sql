/*
 * Reliability's Control File
 *
 * This file manages the order of execution for the base rbl.
 * The base scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */
@rbl/schema/rbl.sql
@rbl/mview/control.sql
@rbl/view/control.sql
@rbl/plsql/control.sql
@rbl/data/control.sql
@rbl/schema/funcindex.sql