/*
 * Asset Management's database validation file
 *
 * This file manages the extra validations that are performed on the base
 * database, beyond what is captured in the structure of the database. These
 * should be executed after every change to the database to ensure integrity.
 * Order of execution should not be important.
 */

@chk_invalid_objects.sql
@chk_audit_columns.sql
@chk_foreign_key_names.sql
@chk_index_names.sql
@chk_unindexed_foreign_keys.sql
