/*
 * Asset Management's Control File
 *
 * This file manages the order of execution for the base asset management.
 * The base scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */

-- Setup Session
ALTER SESSION SET "_pred_move_around" = FALSE;

-- Run Migration
@maintenix/control.sql
@arc/control.sql
@api/control.sql
@opr/control.sql
@rbl/control.sql
@lrp/control.sql
@maintenix/schema/audit_triggers.sql
