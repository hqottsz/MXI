--liquibase formatted sql


--changeSet acor_part_requests_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_part_requests_v1
AS 
WITH
rvw_requested_by_date
AS (
   SELECT
      req_part.alt_id AS part_request_id,
      MIN(req_part.req_by_dt) AS needed_by_date
   FROM
      req_part
   GROUP BY
      req_part.alt_id
)
SELECT
   REQ_TYPE_CD                               AS request_type_code,
   REQ_QT                                    AS requested_quantity,
   req_part.REQ_PRIORITY_CD                  AS request_priority_code,
   REQ_BY_DT                                 As request_by_date,
   REQ_TASK_SDESC                            AS request_task_description,
   REQ_NOTE                                  AS request_note,
   LOCK_RESERVE_BOOL,
   EST_ARRIVAL_DT                            AS estimated_arrival_date,
   REQ_MASTER_ID,
   MA_TRACK_BOOL,
   req_part.PURCH_TYPE_CD                    AS purchase_type_code,
   rfq_line.alt_id                           AS RFQ_LINE_ID,
   LAST_AUTO_RSRV_DT                         As last_auto_reserved_date,
   ISSUE_UNIT_PRICE,
   CSGN_OWED_QT                              AS consign_owed_quantity,
   PART_PROVIDER_TYPE_CD                     AS part_provider_type_code,
   req_part.ALT_ID                           AS part_request_id,
   evt_event.event_sdesc                     AS Barcode,
   po_header.alt_id                          AS order_id,
   po_part.alt_id                            AS po_part_id,
   req_spec_part.alt_id                      AS req_spec_part_id,
   req_stock_part.alt_id                     AS req_stock_id,
   fnc_account.alt_id                        AS issue_acct_id,
   inv_inv.alt_id                            AS inventory_id,
   po_dest_loc.alt_id                        AS po_destination_location_id,
   printed_supl_loc.alt_id                   AS printed_supl_location_id,
   req_loc.alt_id                            AS req_location_id,
   remote_loc.alt_id                         AS remote_location_id,
   assign_hr.alt_id                          AS assign_contact_id,
   req_hr.alt_id                             AS req_contact_id,
   po_vendor.alt_id                          AS po_vendor_id,
   po_line.alt_id                            AS order_line_id,
   sched_stask.alt_id                        AS sched_task_id,
   ref_event_status.user_status_cd           AS status,
   rvw_requested_by_date.needed_by_date
FROM
   req_part
   INNER JOIN evt_event ON
      req_part.req_part_db_id = evt_event.event_db_id AND
      req_part.req_part_id    = evt_event.event_id
   -- get part request status
   INNER JOIN ref_event_status ON
      ref_event_status.event_status_db_id = evt_event.event_status_db_id AND
      ref_event_status.event_status_cd    = evt_event.event_status_cd
   LEFT JOIN po_header ON
      req_part.po_db_id = po_header.po_db_id AND
      req_part.po_id    = po_header.po_id
   LEFT JOIN eqp_part_no po_part ON
      req_part.po_part_no_db_id = po_part.part_no_db_id AND
      req_part.po_part_no_id    = po_part.part_no_id
   LEFT JOIN eqp_part_no req_spec_part ON
      req_part.req_spec_part_no_db_id = req_spec_part.part_no_db_id AND
      req_part.req_spec_part_no_id    = req_spec_part.part_no_id
   LEFT JOIN eqp_stock_no req_stock_part ON
      req_part.req_stock_no_db_id = req_stock_part.stock_no_db_id AND
      req_part.req_stock_no_id    = req_stock_part.stock_no_id
   LEFT JOIN fnc_account ON
      req_part.issue_account_db_id = fnc_account.account_db_id AND
      req_part.issue_account_id    = fnc_account.account_id
   LEFT JOIN inv_inv ON
      req_part.inv_no_db_id = inv_inv.inv_no_db_id AND
      req_part.inv_no_id    = inv_inv.inv_no_id
   LEFT JOIN inv_loc po_dest_loc ON
      req_part.po_dest_loc_db_id = po_dest_loc.loc_db_id AND
      req_part.po_dest_loc_id    = po_dest_loc.loc_id
   LEFT JOIN inv_loc printed_supl_loc ON
      req_part.printed_supply_loc_db_id = printed_supl_loc.loc_db_id AND
      req_part.printed_supply_loc_id    = printed_supl_loc.loc_id
   LEFT JOIN inv_loc req_loc ON
      req_part.req_loc_db_id = req_loc.loc_db_id AND
      req_part.req_loc_id    = req_loc.loc_id
   LEFT JOIN inv_loc remote_loc ON
      req_part.remote_loc_db_id = remote_loc.loc_db_id AND
      req_part.remote_loc_id    = remote_loc.loc_id
   LEFT JOIN org_hr assign_hr ON
      req_part.assign_hr_db_id = assign_hr.hr_db_id AND
      req_part.assign_hr_id    = assign_hr.hr_id
   LEFT JOIN org_hr req_hr ON
      req_part.req_hr_db_id = req_hr.hr_db_id AND
      req_part.req_hr_id    = req_hr.hr_id
   LEFT JOIN org_vendor po_vendor ON
      req_part.po_vendor_db_id = po_vendor.vendor_db_id AND
      req_part.po_vendor_id    = po_vendor.vendor_id
   LEFT JOIN po_line ON
      req_part.po_db_id = po_line.po_db_id AND
      req_part.po_id    = po_line.po_id AND
      req_part.po_line_id = po_line.po_line_id
   LEFT JOIN sched_stask ON
      req_part.pr_sched_db_id = sched_stask.sched_db_id AND
      req_part.pr_sched_id    = sched_stask.sched_id
   LEFT JOIN rfq_line ON
      req_part.rfq_db_id = rfq_line.rfq_db_id AND
      req_part.rfq_id    = rfq_line.rfq_id AND
      req_part.rfq_line_id = rfq_line.rfq_line_id
   LEFT JOIN rvw_requested_by_date ON
      req_part.alt_id = rvw_requested_by_date.part_request_id
;