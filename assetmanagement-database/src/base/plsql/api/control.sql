/*
 * API's Control File
 *
 * This file manages the order of execution for the base api.
 * The scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */

@api/schema/api.sql
@api/plsql/control.sql
@api/data/control.sql
@api/mview/control.sql
@api/view/control.sql
@api/schema/funcindex.sql