--liquibase formatted sql


--changeSet OPER-2909:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_orders_v1
AS 
WITH
rvw_auth_bool
AS (
   SELECT
      po_db_id,
      po_id,
      DECODE(isOrderAuthorized( po_header_auth.po_db_id, po_header_auth.po_id ), 1, 1,
         DECODE(
            (
               SELECT
                  COUNT(*)
               FROM
                  po_auth po_auth
               WHERE
                  po_auth.auth_hr_db_id IS NULL
                  AND
                  po_auth.auth_dt IS NULL
                  AND
                  -- Ignore historic authorizations
                  po_auth.hist_bool   = 0
            ), 0, 0, 2
         )
      ) AS auth_status
               -- authorization status values Authorized =1, Rquested=2 others=0
   FROM
      po_header po_header_auth
),
rvw_po_total_amount
AS (
   SELECT
      po_line.po_db_id,
      po_line.po_id,
      SUM( po_line.line_price ) AS line_price
   FROM
      po_line
   WHERE
      po_line.deleted_bool = 0
   GROUP BY
      po_line.po_db_id,
      po_line.po_id
),
rvw_po_promised_date
AS (
   SELECT
      po_line.po_db_id,
      po_line.po_id,
      MAX( po_line.promise_by_dt) AS promised_by_date
   FROM
      po_line
   WHERE
      po_line.deleted_bool = 0
   GROUP BY
      po_line.po_db_id,
      po_line.po_id
),
rvw_issued_by
AS (
   SELECt
      evt_stage.event_db_id,
      evt_stage.event_id,
      evt_stage.hr_db_id,
      evt_stage.hr_id
   FROM
   (
      SELECT
          event_db_id,
          event_id,
          MIN(stage_id) stage_id
      FROM
         evt_stage
      WHERE
         event_status_cd = 'POISSUED'
      GROUP BY
         event_db_id,
         event_id
    ) fpoissued
    INNER JOIN evt_stage ON
       fpoissued.event_db_id = evt_stage.event_db_id AND
       fpoissued.event_id    = evt_stage.event_id AND
       fpoissued.stage_id    = evt_stage.stage_id
),
rvw_auth_by
AS (
   SELECT
      po_auth.po_db_id,
      po_auth.po_id,
      po_auth.auth_hr_db_id,
      po_auth.auth_hr_id,
      po_auth.auth_dt
   FROM
   (
      SELECT
         po_db_id,
         po_id,
         MAX(po_auth_id) AS po_auth_id
      FROM
         po_auth
      GROUP BY
         po_db_id,
         po_id
   ) lauth
   INNER JOIN po_auth On
     lauth.po_db_id   = po_auth.po_db_id AND
     lauth.po_id      = po_auth.po_id AND
     lauth.po_auth_id = po_auth.po_auth_id
),
-- to get workpackage id
rvw_workpackage
AS (
   SELECT
      po_db_id,
      po_id,
      alt_id
   FROM
   (
      SELECT
         po_db_id,
         po_id,
         sched_stask.alt_id,
         MAX(po_line_id) AS po_line_id
      FROM
         po_line
      LEFT JOIN sched_stask ON
         po_line.sched_db_id = sched_stask.sched_db_id AND
         po_line.sched_id    = sched_stask.sched_id
      WHERE
         po_line.sched_id IS NOT NULL
      GROUP BY
         po_db_id,
         po_id,
         sched_stask.alt_id
   )
)
SELECT
   po_header.alt_id AS order_id,
   po_header.po_db_id,
   po_header.po_id,
   PO_TYPE_CD AS Order_type_code,
   po_header.REQ_PRIORITY_CD AS Priority,
   VENDOR_ACCOUNT_CD AS vendor_account_code,
   po_header.CURRENCY_CD AS currency_code,
   po_line_price.line_price AS total_amount,
   DECODE(po_header.terms_conditions_cd,null,'', po_header.terms_conditions_cd) AS Terms_Conditions,
   DECODE(po_header.fob_cd, null,'', po_header.fob_cd) AS Freight_On_Board,
   DECODE(po_header.transport_type_cd,null,'',po_header.transport_type_cd) AS Transportation_Type,
   po_header.VENDOR_NOTE,
   po_header.po_revision_no issue_number,
   po_header.ISSUED_DT AS issued_date,
   issued_by_utl_user.first_name || ' ' || issued_by_utl_user.last_name as Issued_by,
   CLOSED_DT AS closed_date,
   SHIP_TO_CODE,
   po_header.RECEIVE_NOTE,
   ref_event_status.user_status_cd AS status,
   evt_event.event_sdesc AS Order_Number,
   CASE
      WHEN po_auth_bool.auth_status = 1 THEN
         'AUTHORIZED'
      WHEN po_auth_bool.auth_status = 2 THEN
         'REQUESTED'
      WHEN po_auth_bool.auth_status = 0 THEN
         ''
   END AS Authorization,
   auth_user.first_name || ' ' || auth_user.last_name AS Authorized_By,
   auth.auth_dt AS authorization_date,
   org_vendor.alt_id AS vendor_id,
   utl_user.last_name || ', ' || utl_user.first_name AS Purchasing_Contact,
   rvw_po_promised_date.promised_by_date,
   org_org.org_cd || ' (' || org_org.org_sdesc || ' )' AS receiving_organization,
   ship_to_loc.alt_id AS ship_loc_id,
   rvw_workpackage.alt_id AS wp_id,
   (
      SELECT
         MIN( req_part.req_by_dt )
      FROM
         req_part
      WHERE
         req_part.po_db_id = po_header.po_db_id AND
         req_part.po_id    = po_header.po_id
   ) AS needed_by_date,
   ref_terms_conditions.desc_sdesc        AS terms_condition_short_desc,
   ref_terms_conditions.desc_ldesc        AS terms_condition_long_desc,
   po_header.last_mod_dt,
   evt_event.ext_key_sdesc
FROM
   po_header
   INNER JOIN evt_event ON
      po_header.po_db_id = evt_event.event_db_id AND
      po_header.po_id    = evt_event.event_id
   -- get status
   INNER JOIN ref_event_status ON
      ref_event_status.event_status_db_id = evt_event.event_status_db_id AND
      ref_event_status.event_status_cd    = evt_event.event_status_cd
   -- get issued by info
   LEFT JOIN rvw_issued_by ON
      evt_event.event_db_id = rvw_issued_by.event_db_id AND
      evt_event.event_id    = rvw_issued_by.event_id
   LEFT JOIN org_hr issued_by_org_hr ON
      rvw_issued_by.hr_db_id = issued_by_org_hr.hr_db_id AND
      rvw_issued_by.hr_id    = issued_by_org_hr.hr_id
   LEFT JOIN utl_user issued_by_utl_user ON
      issued_by_org_hr.user_id = issued_by_utl_user.user_id
   LEFT JOIN rvw_po_total_amount po_line_price ON
      po_header.po_db_id = po_line_price.po_db_id AND
      po_header.po_id = po_line_price.po_id
   -- authorization info
   INNER JOIN rvw_auth_bool po_auth_bool ON
      po_header.PO_DB_ID = po_auth_bool.po_db_id AND
      po_header.PO_ID    = po_auth_bool.po_id
   LEFT JOIN rvw_auth_by auth ON
      po_header.po_db_id = auth.po_db_id AND
      po_header.po_id    = auth.po_id
   LEFT JOIN org_hr auth_hr ON
      auth.auth_hr_db_id = auth_hr.hr_db_id AND
      auth.auth_hr_id    = auth_hr.hr_id
   LEFT JOIN utl_user auth_user ON
      auth_hr.user_id = auth_user.user_id
   INNER JOIN org_vendor ON
      po_header.vendor_db_id = org_vendor.vendor_db_id AND
      po_header.vendor_id    = org_vendor.vendor_id
   -- get purchasing contact
   INNER JOIN org_hr ON
      po_header.contact_hr_db_id = org_hr.hr_db_id AND
      po_header.contact_hr_id    = org_hr.hr_id
   INNER JOIN utl_user ON
      utl_user.user_id = org_hr.user_id
   -- get receiving orgnization
   INNER JOIN org_org ON
      po_header.org_db_id = org_org.org_db_id AND
      po_header.org_id    = org_org.org_id
   INNER JOIN inv_loc ship_to_loc ON
      po_header.ship_to_loc_db_id = ship_to_loc.loc_db_id AND
      po_header.ship_to_loc_id    = ship_to_loc.loc_id
   LEFT JOIN rvw_po_promised_date ON
      po_header.po_db_id = rvw_po_promised_date.po_db_id AND
      po_header.po_id    = rvw_po_promised_date.po_id
   -- get work package info
   LEFT JOIN rvw_workpackage ON
      po_header.po_db_id = rvw_workpackage.PO_DB_ID AND
      po_header.po_id    = rvw_workpackage.PO_ID
   LEFT JOIN ref_terms_conditions ON
      po_header.terms_conditions_db_id = ref_terms_conditions.terms_conditions_db_id AND
      po_header.terms_conditions_cd    = ref_terms_conditions.terms_conditions_cd
WHERE
   po_header.rstat_cd = 0
;