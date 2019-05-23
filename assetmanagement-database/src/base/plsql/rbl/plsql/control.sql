/*
 * Reliability's PL/SQL Control File
 *
 * This file manages the order of execution for the base rbl pl/sql.
 * The base scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */

@rbl/plsql/opr_rbl_utility_pkg_spec.sql
@rbl/plsql/opr_rbl_utility_pkg_body.sql
@rbl/plsql/opr_fleet_summary_pkg_spec.sql
@rbl/plsql/opr_fleet_summary_pkg_body.sql
@rbl/plsql/opr_rbl_comp_rmvl_pkg_spec.sql
@rbl/plsql/opr_rbl_comp_rmvl_pkg_body.sql
@rbl/plsql/opr_rbl_eng_pkg_spec.sql
@rbl/plsql/opr_rbl_eng_pkg_body.sql
@rbl/plsql/opr_rbl_job_call.sql
@rbl/plsql/opr_rbl_pirepmarep_pkg_spec.sql
@rbl/plsql/opr_rbl_pirepmarep_pkg_body.sql
@rbl/plsql/opr_report_period_pkg_spec.sql
@rbl/plsql/opr_report_period_pkg_body.sql

