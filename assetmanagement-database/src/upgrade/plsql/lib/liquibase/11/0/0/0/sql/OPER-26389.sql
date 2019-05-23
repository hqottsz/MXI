--liquibase formatted sql


--changeSet OPER-26389:1 stripComments:false
CREATE OR REPLACE FORCE VIEW ACOR_FINANCIAL_TRX_V1 AS
SELECT
   log.alt_id AS transaction_id,
   log.xaction_id AS transaction_number,
   xaction_type_cd AS transaction_type,
   xaction_dt AS transaction_date,
   xaction_ldesc AS description,
   CASE log.unit_price
      WHEN 0.00000 THEN
         TRIM(TO_CHAR(log.UNIT_PRICE,'9999999999999999999990' || RTRIM(RPAD('.', NVL(ref_currency.sub_units_qt,2) + 1,'9'),'.')))
      ELSE
         TRIM(TO_CHAR(log.UNIT_PRICE,'9999999999999999999999' || RTRIM(RPAD('.', NVL(ref_currency.sub_units_qt,2) + 1,'9'),'.')))
   END  AS unit_price,
   qty,
   currency.currency_cd AS currency,
   currency.exchg_qt   AS exchange_rate,
   inv_loc.alt_id AS inv_from_loc_id,
   supl_loc.alt_id AS supply_loc_id,
   vendor.alt_id AS vendor_id,
   inv_inv.alt_id AS inventory_id,
   eqp_part_no.alt_id AS part_id,
   po_header.alt_id AS order_id,
   po_line.alt_id AS order_line_id,
   po_invoice.alt_id AS invoice_id,
   acor_order_invoices_v1.invoice_number,
   po_invoice_line.alt_id AS invoice_line_id,
   po_line.line_no_ord AS order_line_number,
   po_invoice_line.line_no_ord AS invoice_line_number,
   ref_financial_class.financial_class_cd AS financial_class_code,
   ref_financial_class.finance_type_cd    AS finance_type_code
FROM
   fnc_xaction_log log
   LEFT JOIN evt_loc ON
      (
         log.event_db_id      = evt_loc.event_db_id AND
         log.event_id         = evt_loc.event_id    AND
         evt_loc.event_loc_id = 1
      ) OR
      (
         log.ac_event_db_id   = evt_loc.event_db_id AND
         log.ac_event_id      = evt_loc.event_id    AND
         evt_loc.event_loc_id = 1
      )
   LEFT JOIN inv_inv ON
      log.inv_no_db_id = inv_inv.inv_no_db_id AND
      log.inv_no_id    = inv_inv.inv_no_id
   -- part financial type
   LEFT JOIN eqp_part_no ON
      log.part_no_db_id = eqp_part_no.part_no_db_id AND
      log.part_no_id    = eqp_part_no.part_no_id
   LEFT JOIN ref_financial_class ON
      eqp_part_no.financial_class_db_id = ref_financial_class.financial_class_db_id AND
      eqp_part_no.financial_class_cd    = ref_financial_class.financial_class_cd
   -- get inventory ship from location
   LEFT JOIN inv_loc ON
      inv_loc.loc_db_id = evt_loc.loc_db_id AND
      inv_loc.loc_id    = evt_loc.loc_id
   -- get supply location
   LEFT JOIN inv_loc supl_loc ON
      inv_loc.supply_loc_db_id = supl_loc.loc_db_id AND
      inv_loc.supply_loc_id    = supl_loc.loc_id
   LEFT OUTER JOIN org_vendor vendor ON
      inv_loc.vendor_db_id = vendor.vendor_db_id AND
      inv_loc.vendor_id    = vendor.vendor_id
   -- get currency
   LEFT OUTER JOIN ref_currency currency ON
      log.currency_db_id = currency.currency_db_id AND
      log.currency_cd    = currency.currency_cd
   LEFT JOIN po_header ON
      inv_inv.po_db_id = po_header.po_db_id AND
      inv_inv.po_id    = po_header.po_id
   LEFT JOIN po_line ON
      log.po_db_id = po_line.po_db_id AND
      log.po_id    = po_line.po_id AND
      log.po_line_id = po_line.po_line_id
   LEFT JOIN po_invoice ON
      log.po_invoice_db_id = po_invoice.po_invoice_db_id AND
      log.po_invoice_id    = po_invoice.po_invoice_id
   LEFT JOIN acor_order_invoices_v1 ON
      po_invoice.alt_id = acor_order_invoices_v1.id
   LEFT JOIN po_invoice_line ON
      log.po_invoice_db_id = po_invoice_line.po_invoice_db_id AND
      log.po_invoice_id    = po_invoice_line.po_invoice_id AND
      log.po_invoice_line_id = po_invoice_line.po_invoice_line_id
   LEFT JOIN ref_currency ON
      log.currency_db_id = ref_currency.currency_db_id AND
      log.currency_cd    = ref_currency.currency_cd
;

--changeSet OPER-26389:2 stripComments:false
CREATE OR REPLACE VIEW ACOR_FNC_TRX_WP_INFO_V1 AS
SELECT
   -- NOTE: If column is changed here, make sure you make the same changes in acor_fnc_trx_wp_ac_info_v1
   xaction_issue_xfer.transaction_id,
   xaction_issue_xfer.transaction_type,
   DECODE(xaction_issue_xfer.transaction_type, 'TURN IN', null, req_part.alt_id) AS part_request_id,
   task.alt_id          AS task_id,
   wp.alt_id            AS wp_id,
   wp.wo_ref_sdesc      AS work_package_number,
   acft_inv.alt_id      AS aircraft_id,
   inv_ac_reg.ac_reg_cd AS registration_code
FROM
   -----------------------------------------------------------------------
   -- Get work package, task and part request from financial transaction
   -----------------------------------------------------------------------
   (
      -- get issue xfer from turnin xaction
      SELECT
         fnc_xaction_log.alt_id          AS transaction_id,
         fnc_xaction_log.xaction_type_cd AS transaction_type,
         -- get issue xfer from turnin xfer (adhoc turnin has no
         -- corresponding part installation with issue xfer)
         turnin_xfer.init_event_db_id    AS issue_xfer_db_id,
         turnin_xfer.init_event_id       AS issue_xfer_id
      FROM
         fnc_xaction_log
         -- get turnin xfer from fnc_xaction_log
         INNER JOIN inv_xfer turnin_xfer ON
         (
            fnc_xaction_log.event_db_id = turnin_xfer.xfer_db_id AND
            fnc_xaction_log.event_id    = turnin_xfer.xfer_id
         ) OR
         (
            fnc_xaction_log.ac_event_db_id = turnin_xfer.xfer_db_id AND
            fnc_xaction_log.ac_event_id    = turnin_xfer.xfer_id
         )
      WHERE
         fnc_xaction_log.xaction_type_db_id = 0 AND
         fnc_xaction_log.xaction_type_cd = 'TURN IN'
      UNION ALL
      -- get issue xfer from issue xaction
      SELECT
         alt_id          AS transaction_id,
         xaction_type_cd AS transaction_type,
         event_db_id     AS issue_xfer_db_id,
         event_id        AS issue_xfer_id
      FROM
         fnc_xaction_log
      WHERE
         xaction_type_db_id = 0 AND
         xaction_type_cd = 'ISSUE'
   ) xaction_issue_xfer
   INNER JOIN inv_xfer issue_xfer ON
      xaction_issue_xfer.issue_xfer_db_id = issue_xfer.xfer_db_id AND
      xaction_issue_xfer.issue_xfer_id    = issue_xfer.xfer_id
   -- get req_part from issue xfer
   INNER JOIN req_part ON
      issue_xfer.init_event_db_id = req_part.req_part_db_id AND
      issue_xfer.init_event_id    = req_part.req_part_id
   -- get task from req_part
   INNER JOIN sched_stask task ON
      req_part.sched_db_id   = task.sched_db_id AND
      req_part.sched_id      = task.sched_id
   -- get task event from task
   INNER JOIN evt_event task_evt ON
      task.sched_db_id = task_evt.event_db_id AND
      task.sched_id    = task_evt.event_id
   -- get work package from task event
   INNER JOIN sched_stask wp ON
      task_evt.h_event_db_id = wp.sched_db_id AND
      task_evt.h_event_id    = wp.sched_id
   -----------------------------------------------------------------------
   -- Get aircraft inv info from req_part for a aircraft work package task
   -----------------------------------------------------------------------
   LEFT JOIN inv_ac_reg ON
      req_part.req_ac_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      req_part.req_ac_inv_no_id    = inv_ac_reg.inv_no_id
   LEFT JOIN inv_inv acft_inv ON
      inv_ac_reg.inv_no_db_id = acft_inv.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = acft_inv.inv_no_id
      ;