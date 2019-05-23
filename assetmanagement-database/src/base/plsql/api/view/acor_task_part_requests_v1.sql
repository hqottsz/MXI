--liquibase formatted sql


--changeSet acor_task_part_requests_v1:1 stripComments:false
CREATE OR REPLACE VIEW acor_task_part_requests_v1 
AS
SELECT
   req_part.ALT_ID                           AS part_request_id,
   evt_event.event_sdesc                     AS Barcode,
   eqp_part_no.part_no_oem                   AS part_number,
   REQ_TYPE_CD                               AS request_type_code,
   REQ_QT                                    AS requested_quantity,
   req_part.REQ_PRIORITY_CD                  AS request_priority_code,
   REQ_TASK_SDESC                            AS request_task_description,
   REQ_NOTE                                  AS request_note,
   LOCK_RESERVE_BOOL,
   EST_ARRIVAL_DT                            AS estimated_arrival_date,
   REQ_MASTER_ID,
   MA_TRACK_BOOL,
   req_part.PURCH_TYPE_CD                    AS purchase_type_code,
   LAST_AUTO_RSRV_DT                         As last_auto_reserved_date,
   ISSUE_UNIT_PRICE,
   CSGN_OWED_QT                              AS consign_owed_quantity,
   PART_PROVIDER_TYPE_CD                     AS part_provider_type_code,
   eqp_part_no.alt_id                        AS part_id,
   sched_stask.alt_id                        AS sched_task_id,
   ref_event_status.event_status_cd          AS pr_status_code,
   ref_event_status.user_status_cd           AS pr_status,
   req_part.req_by_dt                        AS needed_by_date,
   wo_event.event_status_cd                  AS wp_status_code,
   inv_loc.alt_id                            AS wp_location_id
FROM
   req_part
   INNER JOIN evt_event ON
      req_part.req_part_db_id = evt_event.event_db_id AND
      req_part.req_part_id    = evt_event.event_id
   -- get part request status
   INNER JOIN ref_event_status ON
      ref_event_status.event_status_db_id = evt_event.event_status_db_id AND
      ref_event_status.event_status_cd    = evt_event.event_status_cd
   INNER JOIN eqp_part_no ON
      req_part.po_part_no_db_id = eqp_part_no.part_no_db_id AND
      req_part.po_part_no_id    = eqp_part_no.part_no_id
   -- make sure part request is made during a task
   INNER JOIN sched_stask ON
      req_part.pr_sched_db_id = sched_stask.sched_db_id AND
      req_part.pr_sched_id    = sched_stask.sched_id
   INNER JOIN evt_event task_event ON
      task_event.event_db_id    = sched_stask.sched_db_id  AND
      task_event.event_id       = sched_stask.sched_id
   LEFT JOIN evt_event wo_event   ON
      wo_event.event_db_id      = task_event.h_event_db_id AND
      wo_event.event_id         = task_event.h_event_id
   LEFT JOIN sched_stask wo_stask ON
      wo_stask.sched_db_id     = wo_event.event_db_id      AND
      wo_stask.sched_id        = wo_event.event_id
   LEFT JOIN evt_loc ON
      evt_loc.event_db_id      = wo_event.event_db_id      AND
      evt_loc.event_id         = wo_event.event_id
   LEFT JOIN inv_loc ON
      inv_loc.loc_db_id        = evt_loc.loc_db_id         AND
      inv_loc.loc_id           = evt_loc.loc_id
 WHERE
      wo_stask.task_class_cd = 'CHECK'
;