/*
 * LRP's Control File
 *
 * This file manages the order of execution for the base lrp.
 * The scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */
@lrp/schema/lrp.sql
@lrp/plsql/control.sql
@lrp/data/control.sql
@lrp/mview/control.sql
@lrp/view/control.sql
@lrp/schema/audit_triggers.sql
