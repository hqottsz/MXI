/*
 * LRP's PL/SQL Control File
 *
 * This file manages the order of execution for the base lrp pl/sql.
 * The scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */

@lrp/plsql/LRP_REPORT_PKG_SPEC.sql
@lrp/plsql/LRP_REPORT_PKG_BODY.sql