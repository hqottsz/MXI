INSERT
INTO
   evt_event
   (
      event_db_id,
      event_id,
      event_type_db_id,
      event_type_cd,
      event_status_db_id,
      event_status_cd,
      event_sdesc,
      hist_bool,
      seq_err_bool
   )
VALUES
   (
      4650,
      999999,
      0,
      'PR',
      0,
      'PROPEN',
      'PR1',
      0,
      0
   );


INSERT
INTO
   req_part
   (
      req_part_db_id,
      req_part_id,
      req_type_db_id,
      req_type_cd,
      req_priority_db_id,
      req_priority_cd,
      po_part_no_db_id,
      po_part_no_id,
      req_loc_db_id,
      req_loc_id,
      req_hr_db_id,
      req_hr_id,
      issue_account_db_id,
      issue_account_id,
      req_master_id,
      req_qt,
      req_by_dt,
      req_note,
      supply_chain_db_id,
      supply_chain_cd,
      qty_unit_db_id,
      qty_unit_cd
   )
VALUES
   (
      4650,
      999999,
      0,
      'ADHOC',
      0,
      'NORMAL',
      4650,
      (SELECT part_no_id FROM eqp_part_no WHERE part_no_oem = 'CHW000023'),
      4650,
      (SELECT loc_id FROM inv_loc WHERE loc_cd = 'AIRPORT3/STORE'),
      4650,
      (SELECT user_id FROM utl_user WHERE username = 'mxi'),
      4650,
      (SELECT account_id FROM fnc_account WHERE account_cd = '5'),
      'PR1',
      1,
      TO_DATE('2016/10/11', 'yyyy/mm/dd'),
      'This part request was created adhoc.',
      0,
      'DEFAULT',
      0,
      'EA'
   );