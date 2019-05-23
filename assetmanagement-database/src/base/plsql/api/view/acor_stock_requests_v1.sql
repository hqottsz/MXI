--liquibase formatted sql


--changeSet acor_stock_requests_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_stock_requests_v1
AS
SELECT req_part.alt_id                        AS ID,
       eqp_stock_no.alt_id                    AS stock_id,
       eqp_part_no.alt_id                     AS part_id,
       req_loc.alt_id                         AS where_needed_loc_id,
       org_vendor.alt_id                      AS pref_vendor_id,
       evt_event.event_sdesc                  AS barcode,
       req_part.creation_dt                   AS stock_creation_date,
       eqp_stock_no.stock_no_cd               AS stock_number,
       eqp_part_no.part_no_oem                AS part_number,
       req_part.req_qt                        AS qty,
       ref_qty_unit.qty_unit_cd               AS unit,
       req_loc.loc_cd                         AS where_needed,
       req_part.req_by_dt                     AS stock_low_date,
       req_part.purch_type_cd                 AS purchase_type,
       DECODE(eqp_part_no.stock_no_db_id,
              req_part.req_stock_no_db_id,
              DECODE(eqp_part_no.stock_no_id,
                     req_part.req_stock_no_id,
                     'NORMAL',
                     'NOT_IN_STOCK_NO'),
              'NOT_IN_STOCK_NO')              AS stock_status,
       org_vendor.vendor_cd                   AS preferred_vendor_code,
       org_vendor.vendor_name                 AS preferred_vendor_name,
       /* Calculates the estimated cost as follows:-
                  Estimated cost calculated only if there is an active vendor part price, AND
                  current date is after effective from date AND
                  current date is before effective to date.
                  If effective to date is null, price is considered to be effective indefinitely.) */
       DECODE(eqp_part_vendor_price.part_vendor_price_db_id,
              NULL,
              NULL,
              DECODE(eqp_part_vendor_price.effective_from_dt,
                     NULL,
                     NULL,
                     DECODE(SIGN(SYSDATE -
                                 eqp_part_vendor_price.effective_from_dt),
                            -1,
                            NULL,
                            DECODE(eqp_part_vendor_price.effective_to_dt,
                                   NULL,
                                   DECODE(eqp_part_vendor_price.discount_pct,
                                          NULL,
                                          1,
                                          1 - eqp_part_vendor_price.discount_pct) *
                                   eqp_part_vendor_price.unit_price *
                                   req_part.req_qt,
                                   DECODE(SIGN(eqp_part_vendor_price.effective_to_dt -
                                               SYSDATE),
                                          -1,
                                          NULL,
                                          DECODE(eqp_part_vendor_price.discount_pct,
                                                 NULL,
                                                 1,
                                                 1 -
                                                 eqp_part_vendor_price.discount_pct) *
                                          eqp_part_vendor_price.unit_price *
                                          req_part.req_qt))))) AS estimated_cost,
       org_vendor.currency_cd AS currency,
       doesPartHaveInProgressRFQ(eqp_part_no.part_no_db_id,
                                 eqp_part_no.part_no_id) AS in_progress_rfq,
       CASE
         WHEN (SELECT COUNT(1)
                 FROM po_line, po_header, evt_event po_evt_event
                WHERE po_line.part_no_db_id = eqp_part_no.part_no_db_id
                  AND po_line.part_no_id = eqp_part_no.part_no_id
                  AND
                     -- excludes deleted po line
                      po_line.deleted_bool = 0
                  AND po_header.po_db_id = po_line.po_db_id
                  AND po_header.po_id = po_line.po_id
                  AND po_header.po_type_db_id = 0
                  AND po_header.po_type_cd = 'PURCHASE'
                  AND po_evt_event.event_db_id = po_header.po_db_id
                  AND po_evt_event.event_id = po_header.po_id
                  AND po_evt_event.event_status_db_id = 0
                  AND po_evt_event.event_status_cd NOT IN
                      ('PORECEIVED', 'POPARTIAL', 'POCLOSED', 'POCANCEL')) > 0 THEN
          1
         ELSE
          0
       END AS in_progress_po
  FROM evt_event
 INNER JOIN req_part ON req_part.req_part_db_id = evt_event.event_db_id
                    AND req_part.req_part_id = evt_event.event_id
                    AND req_part.req_type_db_id = 0
                    AND req_part.req_type_cd = 'STOCK'
 INNER JOIN inv_loc req_loc ON req_loc.loc_db_id = req_part.req_loc_db_id
                           AND req_loc.loc_id = req_part.req_loc_id
  LEFT  JOIN eqp_part_no ON
-- obtain details about the desired part
 eqp_part_no.part_no_db_id = req_part.req_spec_part_no_db_id
 AND eqp_part_no.part_no_id = req_part.req_spec_part_no_id
  LEFT  JOIN eqp_stock_no ON eqp_stock_no.stock_no_db_id =
                                  req_part.req_stock_no_db_id
                              AND eqp_stock_no.stock_no_id =
                                  req_part.req_stock_no_id
  LEFT  JOIN eqp_part_vendor ON eqp_part_vendor.part_no_db_id =
                                     eqp_part_no.part_no_db_id
                                 AND eqp_part_vendor.part_no_id =
                                     eqp_part_no.part_no_id
                                 AND eqp_part_vendor.pref_bool = 1
  LEFT  JOIN org_vendor ON org_vendor.vendor_db_id =
                                eqp_part_vendor.vendor_db_id
                            AND org_vendor.vendor_id =
                                eqp_part_vendor.vendor_id
  LEFT  JOIN eqp_part_vendor_price ON eqp_part_vendor_price.vendor_db_id =
                                           eqp_part_vendor.vendor_db_id
                                       AND eqp_part_vendor_price.vendor_id =
                                           eqp_part_vendor.vendor_id
                                       AND eqp_part_vendor_price.part_no_db_id =
                                           eqp_part_vendor.part_no_db_id
                                       AND eqp_part_vendor_price.part_no_id =
                                           eqp_part_vendor.part_no_id
                                       AND eqp_part_vendor_price.hist_bool = 0
  LEFT  JOIN ref_qty_unit ON
--get quantity related information
 ref_qty_unit.qty_unit_db_id = eqp_part_no.qty_unit_db_id
 AND ref_qty_unit.qty_unit_cd = eqp_part_no.qty_unit_cd
  LEFT  JOIN ref_currency ON ref_currency.currency_db_id =
                                  org_vendor.currency_db_id
                              AND ref_currency.currency_cd =
                                  org_vendor.currency_cd
 WHERE
-- Get only Part Requests that require a Purchase Order
 evt_event.event_status_cd = 'PRPOREQ'
 AND req_part.rfq_db_id IS NULL
 AND req_part.po_db_id IS NULL
;