/*
 * API's Materialized Views Control File
 *
 * This file manages the order of execution for the base api mviews.
 * The scripts get executed in order from top to bottom.
 * New entries should be inserted at the bottom of this file by default.
 *
 * Example:
 *
 * @lib/current/sql/EXAMPLE.sql
 */
@api\mview\acor_acft_wp_parts_mv1.sql
@api\mview\acor_adsb_ref_doc_defn_mv1.sql
@api\mview\acor_block_lastdone_nextdue_v1.sql
@api\mview\acor_non_routine_fault_mv1.sql
@api\mview\acor_req_lastdone_nextdue_v1.sql
