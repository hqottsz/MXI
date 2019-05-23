--Task definition
INSERT INTO task_defn ( task_defn_db_id, task_defn_id, alt_id ) VALUES (4650, 999, 'AB481D95FFD140EBB6F71CED2B8EBF65' );
INSERT INTO task_task ( task_db_id, task_id, task_defn_db_id, task_defn_id ) VALUES ( 4650, 888, 4650, 999 );

--Deferral reference
INSERT INTO fail_defer_ref ( fail_defer_ref_db_id, fail_defer_ref_id, assmbl_db_id, assmbl_cd, defer_ref_sdesc, alt_id ) VALUES ( 4650, 2000, 4650, 'F-2000', 'Deferral Reference Test', 'D68CD935981E47D89E2120514D921F0D' );
INSERT INTO fail_defer_ref_task_defn ( fail_defer_ref_id, task_defn_id ) VALUES ( 'D68CD935981E47D89E2120514D921F0D', 'AB481D95FFD140EBB6F71CED2B8EBF65' );

--Fault and corrective task
INSERT INTO sd_fault ( fault_db_id, fault_id, defer_ref_sdesc ) VALUES ( 4650, 3, 'Deferral Reference Test' );
INSERT INTO sched_stask ( sched_db_id, sched_id, fault_db_id, fault_id ) VALUES (4650, 4, 4650, 3 );
INSERT INTO evt_event ( event_db_id, event_id, h_event_db_id, h_event_id ) VALUES ( 4650, 4, 4650, 100 );
INSERT INTO evt_inv ( event_db_id, event_id, event_inv_id, main_inv_bool, assmbl_db_id, assmbl_cd, h_inv_no_db_id, h_inv_no_id ) VALUES ( 4650, 100, 1, 1, 4650, 'F-2000', 4650, 400 );

--Recurring inspection tasks
INSERT INTO sched_stask ( sched_db_id, sched_id, task_db_id, task_id, orphan_frct_bool, barcode_sdesc ) VALUES ( 4650, 5, 4650, 888, 0, 'T40S0001BK7' );
INSERT INTO evt_event ( event_db_id, event_id, event_status_db_id, event_status_cd, event_sdesc ) VALUES ( 4650, 5, 0, 'ACTV', 'baseline recurring task 1' );
INSERT INTO evt_event_rel ( event_db_id, event_id, rel_event_db_id, rel_event_id, rel_type_cd, event_rel_id ) VALUES ( 4650, 5, 4650, 6, 'DEPT', 1 );
INSERT INTO evt_inv ( event_db_id, event_id, event_inv_id, h_inv_no_db_id, h_inv_no_id ) VALUES ( 4650, 5, 1, 4650, 400 );

INSERT INTO sched_stask ( sched_db_id, sched_id, task_db_id, task_id, orphan_frct_bool, barcode_sdesc ) VALUES ( 4650, 6, 4650, 888, 0, 'T40S0001ACN' );
INSERT INTO evt_event ( event_db_id, event_id, event_status_db_id, event_status_cd, event_sdesc ) VALUES ( 4650, 6, 0, 'FORECAST', 'baseline recurring task 2' );
INSERT INTO evt_inv ( event_db_id, event_id, event_inv_id, h_inv_no_db_id, h_inv_no_id ) VALUES ( 4650, 6, 1, 4650, 400 );

-- recurring inspections linked to another fault
INSERT INTO sched_stask ( sched_db_id, sched_id, task_db_id, task_id, orphan_frct_bool, barcode_sdesc ) VALUES ( 4650, 7, 4650, 888, 0, 'T40S0001BK8' );
INSERT INTO evt_event ( event_db_id, event_id, event_status_db_id, event_status_cd, event_sdesc ) VALUES ( 4650, 7, 0, 'ACTV', 'unrelated baseline recurring task 1' );
INSERT INTO evt_event_rel ( event_db_id, event_id, rel_event_db_id, rel_event_id, rel_type_cd, event_rel_id ) VALUES ( 4650, 7, 4650, 8, 'DEPT', 1 );
INSERT INTO evt_inv ( event_db_id, event_id, event_inv_id, h_inv_no_db_id, h_inv_no_id ) VALUES ( 4650, 7, 1, 4650, 400 );

INSERT INTO sched_stask ( sched_db_id, sched_id, task_db_id, task_id, orphan_frct_bool, barcode_sdesc ) VALUES ( 4650, 8, 4650, 888, 0, 'T40S0001ACO' );
INSERT INTO evt_event ( event_db_id, event_id, event_status_db_id, event_status_cd, event_sdesc ) VALUES ( 4650, 8, 0, 'FORECAST', 'unrelated baseline recurring task 2' );
INSERT INTO evt_inv ( event_db_id, event_id, event_inv_id, h_inv_no_db_id, h_inv_no_id ) VALUES ( 4650, 8, 1, 4650, 400 );
