-- Create part requests for each of Fault1's part requirements
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
      event_sdesc
   )
SELECT
   4650,
   event_id_seq.nextval,
   0,
   'PR',
   0,
   'PROPEN',
   'ETA-PR'|| part_id
FROM
   (
      -- Get a column of integers
      SELECT 1 AS part_id FROM dual UNION
      SELECT 2 FROM dual UNION
      SELECT 3 FROM dual
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
      req_bom_part_db_id,
      req_bom_part_id,
      req_spec_part_no_db_id,
      req_spec_part_no_id,
      req_hr_db_id,
      req_hr_id,
      req_ac_inv_no_db_id,
      req_ac_inv_no_id,
      sched_db_id,
      sched_id,
      sched_part_id,
      sched_inst_part_id,
      pr_sched_db_id,
      pr_sched_id,
      req_master_id,
      req_qt,
      req_note,
      supply_chain_db_id,
      supply_chain_cd,
      qty_unit_db_id,
      qty_unit_cd
   )
SELECT
   4650,
   req_event.event_id,
   0,
   'TASK',
   0,
   'NORMAL',
   4650,
   eqp_part_no.part_no_id,
   4650,
   eqp_bom_part.bom_part_id,
   4650,
   eqp_part_no.part_no_id,
   4650,
   (SELECT user_id FROM utl_user WHERE username = 'mxi'),
   4650,
   inv_ac_reg.inv_no_id,
   4650,
   fault_event.event_id,
   rownum,
   1,
   4650,
   fault_event.event_id,
   req_event.event_sdesc,
   1,
   'This part request was created by sql script.',
   0,
   'DEFAULT',
   0,
   'EA'
FROM evt_event fault_event
-- find part requirements on Fault 1
JOIN sched_part ON
   sched_part.sched_db_id = fault_event.event_db_id AND
   sched_part.sched_id    = fault_event.event_id
-- find the part request events related to the fault and link them to the part requirements
JOIN evt_event req_event ON
   req_event.event_sdesc = 'ETA-PR' || sched_part.sched_part_id
-- add additional information for part request event
JOIN
(
      SELECT
         1 AS req_sched_part_id,
         'A0000008' AS req_part_no_oem,
         'ENG-SYS-2-1-1-TRK' AS req_bom_part_cd FROM dual UNION
      SELECT 2, 'A0000007', 'ACFTMOC5' FROM dual UNION
      SELECT 3, 'A0000017A', 'APU-ASSY' FROM dual
) ON
   req_sched_part_id = sched_part.sched_part_id
-- look up part ids
JOIN eqp_part_no ON
   eqp_part_no.part_no_oem = req_part_no_oem
-- look up bom part ids
JOIN eqp_bom_part ON
   eqp_bom_part.bom_part_cd = req_bom_part_cd
-- look up the AC ID
JOIN inv_ac_reg ON
   inv_ac_reg.ac_reg_cd    = 'MAT-1'
WHERE
   fault_event.event_sdesc = 'ETA Fault1';