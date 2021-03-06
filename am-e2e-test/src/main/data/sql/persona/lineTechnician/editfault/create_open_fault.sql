--create an event for fault
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, editor_hr_db_id, editor_hr_id, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, h_event_db_id, h_event_id, event_sdesc, hist_bool, actual_start_dt, actual_start_gdt)
VALUES (
  4650,
  event_id_seq.nextval,
  0,
  'CF',
  4650,
  (SELECT user_id FROM utl_user WHERE username = 'mxi'),
  0,
  'CFACTV',
  0,
  118,
  4650,
  event_id_seq.currval,
  'FAULT-OPER-1509',
  0,
  SYSDATE,
  SYSDATE
);

--create a fault
INSERT INTO sd_fault ( fault_db_id, fault_id, fault_source_db_id, fault_source_cd, found_by_hr_db_id, found_by_hr_id, fail_sev_db_id, fail_sev_cd )
VALUES (
  4650,
  event_id_seq.currval,
  0,
  'PILOT',
  0,
  3,
  0,
  'MINOR'
);

--create the event for inventory
INSERT INTO evt_inv ( event_db_id, event_id, event_inv_id, inv_no_db_id, inv_no_id, nh_inv_no_db_id, nh_inv_no_id, assmbl_inv_no_db_id, assmbl_inv_no_id, h_inv_no_db_id, h_inv_no_id, assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, main_inv_bool)
VALUES (
  4650,
  event_id_seq.currval,
  1,
  4650,
  (SELECT inv_no_id FROM inv_inv WHERE assmbl_cd = 'ACFT_LT7' AND inv_class_cd = 'SYS'),
  4650,
  (SELECT inv_no_id FROM inv_inv WHERE serial_no_oem = 'OPER-1509'),
  4650,
  (SELECT inv_no_id FROM inv_inv WHERE serial_no_oem = 'OPER-1509'),
  4650,
  (SELECT inv_no_id FROM inv_inv WHERE serial_no_oem = 'OPER-1509'),
  4650,
  'ACFT_LT7',
  1,
  1,
  1
);

--create an event for corrective task
INSERT INTO evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, sched_priority_db_id, sched_priority_cd, h_event_db_id, h_event_id, event_sdesc, hist_bool)
VALUES (
  4650,
  event_id_seq.nextval,
  0,
  'TS',
  0,
  'ACTV',
  0,
  76,
  0,
  'NONE',
  4650,
  event_id_seq.currval,
  'CORR-OPER-1509',
  0
);

--create a corrective task
INSERT INTO sched_stask ( sched_db_id, sched_id, h_sched_db_id, h_sched_id, fault_db_id, fault_id, task_class_db_id, task_class_cd, main_inv_no_db_id, main_inv_no_id, barcode_sdesc)
VALUES (
  4650,
  event_id_seq.currval,
  4650,
  event_id_seq.currval,
  4650,
  (SELECT evt_event.event_id FROM evt_event WHERE evt_event.event_sdesc = 'FAULT-OPER-1509'),
  0,
  'CORR',
  4650,
  (SELECT inv_no_id FROM inv_inv WHERE assmbl_cd = 'ACFT_LT7' AND inv_class_cd = 'SYS'),
  'OPER1509BARCODE'
);

--create the event for the inventory
INSERT INTO evt_inv ( event_db_id, event_id, event_inv_id, inv_no_db_id, inv_no_id, nh_inv_no_db_id, nh_inv_no_id, assmbl_inv_no_db_id, assmbl_inv_no_id, h_inv_no_db_id, h_inv_no_id, assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, main_inv_bool)
VALUES (
  4650,
  event_id_seq.currval,
  1,
  4650,
  (SELECT inv_no_id FROM inv_inv WHERE assmbl_cd = 'ACFT_LT7' AND inv_class_cd = 'SYS'),
  4650,
  (SELECT inv_no_id FROM inv_inv WHERE serial_no_oem = 'OPER-1509'),
  4650,
  (SELECT inv_no_id FROM inv_inv WHERE serial_no_oem = 'OPER-1509'),
  4650,
  (SELECT inv_no_id FROM inv_inv WHERE serial_no_oem = 'OPER-1509'),
  4650,
  'ACFT_LT7',
  1,
  1,
  1
);

--create the related event
INSERT INTO evt_event_rel ( event_db_id, event_id, event_rel_id, rel_event_db_id, rel_event_id, rel_type_db_id, rel_type_cd, rel_event_ord)
VALUES (
  4650,
  (SELECT evt_event.event_id FROM evt_event WHERE evt_event.event_sdesc = 'FAULT-OPER-1509'),
  1,
  4650,
  (SELECT evt_event.event_id FROM evt_event WHERE evt_event.event_sdesc = 'CORR-OPER-1509'),
  0,
  'CORRECT',
  1
);