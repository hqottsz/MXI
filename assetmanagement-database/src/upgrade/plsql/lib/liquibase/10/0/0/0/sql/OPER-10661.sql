--liquibase formatted sql

--changeSet OPER-10661:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table FNC_XACTION_LOG add (
   SCHED_DB_ID NUMBER(10,0)  Check (SCHED_DB_ID BETWEEN 0 AND 4294967295 ) 
)
');
END;
/

--changeSet OPER-10661:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table FNC_XACTION_LOG add (
   SCHED_ID NUMBER(10,0)  Check (SCHED_ID BETWEEN 0 AND 4294967295 ) 
)
');
END;
/

--changeSet OPER-10661:3 stripComments:false
-- comments on SCHED_DB_ID column
COMMENT ON COLUMN FNC_XACTION_LOG.SCHED_DB_ID
IS
  'FK SCHED_STASK. Identifies the task associated with this financial transaction.' ;

--changeSet OPER-10661:4 stripComments:false
-- comments on SCHED_ID column
COMMENT ON COLUMN FNC_XACTION_LOG.SCHED_ID
IS
  'FK SCHED_STASK. Identifies the task associated with this financial transaction.' ;
  
--changeSet OPER-10661:5 stripComments:false
-- comments on EVENT_DB_ID column
COMMENT ON COLUMN FNC_XACTION_LOG.EVENT_DB_ID
IS
  'FK EVT_EVENT. Link to the transfer event created for manually issue or expected turn in associated with this financial transaction.' ;

--changeSet OPER-10661:6 stripComments:false
-- comments on EVENT_ID column
COMMENT ON COLUMN FNC_XACTION_LOG.EVENT_ID
IS
  'FK EVT_EVENT. Link to the transfer event created for manually issue or expected turn in associated with this financial transaction.' ;

--changeSet OPER-10661:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('  
   Alter table FNC_XACTION_LOG add Constraint FK_SCHEDSTASK_FNCXACTIONLOG foreign key (SCHED_DB_ID,SCHED_ID) 
   references SCHED_STASK (SCHED_DB_ID, SCHED_ID)  
');
END;
/

--changeset OPER-10661:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      CREATE INDEX IX_SCHEDSTASK_FNCXACTIONLOG ON FNC_XACTION_LOG
       (
         SCHED_DB_ID ASC ,
         SCHED_ID ASC
       )
   ');
END;
/

--changeset OPER-10661:9 stripComments:false
-- Migration script for merging sched_db_id/id into fnc_xaction_log for existing logs
MERGE INTO  
   fnc_xaction_log
USING
   ( 
      -- get all ISSUE transactions which have accociated work package
      SELECT
         fnc_xaction_log.xaction_db_id,
         fnc_xaction_log.xaction_id,
         req_part.pr_sched_db_id,
         req_part.pr_sched_id
      FROM
         fnc_xaction_log 
         INNER JOIN inv_xfer ON
            fnc_xaction_log.event_db_id = inv_xfer.xfer_db_id AND
            fnc_xaction_log.event_id    = inv_xfer.xfer_id   
         INNER JOIN evt_event xfer_event ON
            fnc_xaction_log.event_db_id = xfer_event.event_db_id AND
            fnc_xaction_log.event_id    = xfer_event.event_id
         INNER JOIN req_part ON
            inv_xfer.init_event_db_id = req_part.req_part_db_id AND
            inv_xfer.init_event_id    = req_part.req_part_id
      WHERE
         inv_xfer.xfer_type_db_id = 0 AND
         inv_xfer.xfer_type_cd    = 'ISSUE'
         AND
         -- for adhoc part request, the needed-for-task is null
         req_part.pr_sched_db_id IS NOT NULL
   UNION ALL
      -- get all TURN IN transactions which have accociated work package
      SELECT
         fnc_xaction_log.xaction_db_id,
         fnc_xaction_log.xaction_id,
         req_part.pr_sched_db_id,
         req_part.pr_sched_id
      FROM
         fnc_xaction_log 
         INNER JOIN inv_xfer turnin_xfer ON
            fnc_xaction_log.event_db_id = turnin_xfer.xfer_db_id AND
            fnc_xaction_log.event_id    = turnin_xfer.xfer_id  
         -- for expected turn in, the transfer is always created for the removed inventory
         -- after issue the installed inventory with init_event_db_id/id = issue xfer db_id/id 
         INNER JOIN inv_xfer issue_xfer ON
            turnin_xfer.init_event_db_id = issue_xfer.xfer_db_id AND
            turnin_xfer.init_event_id    = issue_xfer.xfer_id
         INNER JOIN req_part ON
            issue_xfer.init_event_db_id = req_part.req_part_db_id AND
            issue_xfer.init_event_id    = req_part.req_part_id
      WHERE
         turnin_xfer.xfer_type_db_id = 0 AND
         turnin_xfer.xfer_type_cd    = 'TURNIN'
         AND
         -- for adhoc part request, the needed-for-task is null
         req_part.pr_sched_db_id IS NOT NULL
   ) fill_in_task
ON
   (
      fnc_xaction_log.xaction_db_id = fill_in_task.xaction_db_id AND
      fnc_xaction_log.xaction_id    = fill_in_task.xaction_id 
   )
WHEN MATCHED 
   THEN UPDATE SET 
      fnc_xaction_log.sched_db_id = fill_in_task.pr_sched_db_id,
      fnc_xaction_log.sched_id    = fill_in_task.pr_sched_id;

--changeSet OPER-10661:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Modify Detailed Financial Log report body
CREATE OR REPLACE PACKAGE BODY report_detailed_financial_log AS

/************************************************************************
** Date:        March 15, 2016
** Author:      Gustavo Pichetto
** Description: Detailed Financial Log report
*************************************************************************
**
** Confidential, proprietary and/or trade secret information of
** Mxi Technologies.
**
** Copyright 2005-2017 Mxi Technologies. All Rights Reserved.
**
** Except as expressly provided by written license signed by a duly appointed
** officer of Mxi Technologies. any disclosure, distribution,
** reproduction, compilation, modification, creation of derivative works and/or
** other use of the Mxi source code is strictly prohibited.  Inclusion of a
** copyright notice shall not be taken to indicate that the source code has
** been published.
**
***************************************************************************/

-----------------------------------------------------------------------------
   -- Description: Bring detailed financial transactions results
-----------------------------------------------------------------------------
FUNCTION get_detailed_financial_tx (
      aidt_from_date   IN DATE,
      aidt_to_date     IN DATE,
      aiv_account_code IN VARCHAR2,
      ait_account_type_code IN VARCHAR2
   ) RETURN detail_financial_tx_ttyp
IS

 CURSOR lcur_fnc_tx IS
   WITH po_info
   AS
   (
      SELECT
         evt_event.event_sdesc AS po_number,
         po_line.po_db_id,
         po_line.po_id,
         po_line.po_line_id
      FROM
         evt_event
         INNER JOIN po_header ON
            evt_event.event_db_id = po_header.po_db_id AND
            evt_event.event_id   =  po_header.po_id
         INNER JOIN po_line ON
            po_header.po_db_id = po_line.po_db_id AND
            po_header.po_id = po_line.po_id
  ),
  invoice_info
   AS
   (
      SELECT
         evt_event.event_sdesc AS invoice_number,
         po_invoice_line.po_invoice_db_id,
         po_invoice_line.po_invoice_id,
         po_invoice_line.po_invoice_line_id
      FROM
         evt_event
         INNER JOIN po_invoice ON
            evt_event.event_db_id = po_invoice.po_invoice_db_id AND
            evt_event.event_id = po_invoice.po_invoice_id
         INNER JOIN po_invoice_line ON
            po_invoice.po_invoice_db_id = po_invoice_line.po_invoice_db_id AND
            po_invoice.po_invoice_id  = po_invoice_line.po_invoice_id
   ),
  -- Get work package, acft info, part request and location from ISSUE and TURN IN financial transaction
  trx_wp_info AS
  (
  SELECT
     fnc_xaction.transaction_id,
     DECODE( task_sched.wo_ref_sdesc, NULL, wp_sched.wo_ref_sdesc, task_sched.wo_ref_sdesc ) AS work_package_number,
     inv_ac_reg.ac_reg_cd AS registration_code,
     wp_loc.loc_cd,
     supply_loc.loc_cd AS supply_loc_cd
  FROM
   (
      SELECT
         fnc_xaction_log.alt_id          AS transaction_id,
         fnc_xaction_log.xaction_type_cd AS transaction_type,
         fnc_xaction_log.sched_db_id,
         fnc_xaction_log.sched_id       
      FROM
         fnc_xaction_log
      WHERE
         (
            fnc_xaction_log.xaction_type_db_id = 0 AND
            fnc_xaction_log.xaction_type_cd = 'TURN IN'
         ) 
         OR
         (
            fnc_xaction_log.xaction_type_db_id = 0 AND
            fnc_xaction_log.xaction_type_cd = 'ISSUE'
         )
   ) fnc_xaction
   -- get work package
   INNER JOIN sched_stask task_sched ON
      fnc_xaction.sched_db_id = task_sched.sched_db_id AND
      fnc_xaction.sched_id    = task_sched.sched_id
   INNER JOIN evt_event task_event ON
      fnc_xaction.sched_db_id = task_event.event_db_id AND
      fnc_xaction.sched_id    = task_event.event_id
   INNER JOIN sched_stask wp_sched ON
      /*
       * If the reserved inventory in the part request has been auto-issued when user
       * completes the work package, then the wo_db_id will be populated into sched_stask.wo_db_id at that point.
       * But before that, if use just manually issued the reserved inventory in that part request, the
       * sched_stask.wo_db_id will still be null. Then we have to retrieve it from h_event.
       */
      wp_sched.sched_db_id = NVL( task_sched.wo_db_id, task_event.h_event_db_id  ) AND
      wp_sched.sched_id    = NVL( task_sched.wo_id   , task_event.h_event_id )
   -- get aircraft inv info 
   INNER JOIN evt_inv ON
      wp_sched.sched_db_id = evt_inv.event_db_id  AND
      wp_sched.sched_id    = evt_inv.event_id AND
      evt_inv.main_inv_bool = 1   
   LEFT JOIN inv_ac_reg ON
      evt_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      evt_inv.inv_no_id    = inv_ac_reg.inv_no_id      
   -- get work package location and its supply location   
   INNER JOIN evt_loc ON
      wp_sched.sched_db_id = evt_loc.event_db_id AND
      wp_sched.sched_id    = evt_loc.event_id
   INNER JOIN inv_loc wp_loc ON
      evt_loc.loc_db_id = wp_loc.loc_db_id AND
      evt_loc.loc_id    = wp_loc.loc_id
   LEFT JOIN inv_loc supply_loc ON
      supply_loc.loc_db_id = wp_loc.supply_loc_db_id AND
      supply_loc.loc_id    = wp_loc.supply_loc_id  
  )
   SELECT
      detail_financial_tx_rtyp (
         fnc_xaction_log.xaction_id,
         fnc_xaction_log.xaction_dt,
         fnc_xaction_log.xaction_type_cd,
         fnc_xaction_log.xaction_ldesc,
         fnc_xaction_log.currency_cd,
         fnc_account.account_cd,
         fnc_account.account_type_cd,
         fnc_account.ext_key_sdesc,
         fnc_xaction_account.credit_cost,
         fnc_xaction_account.debit_cost,
         fnc_tcode.tcode_cd,
         po_info.po_number,
         -- invoice number
         invoice_info.invoice_number,
         eqp_part_no.part_no_oem,
         inv_inv.serial_no_oem,
         trx_wp_info.work_package_number,
         trx_wp_info.registration_code,
         trx_wp_info.loc_cd,
         trx_wp_info.supply_loc_cd
      )
   FROM
      fnc_xaction_log
      INNER JOIN fnc_xaction_account ON
         fnc_xaction_log.xaction_db_id = fnc_xaction_account.xaction_db_id AND
         fnc_xaction_log.xaction_id = fnc_xaction_account.xaction_id
      INNER JOIN fnc_account ON
         fnc_xaction_account.account_db_id = fnc_account.account_db_id AND
         fnc_xaction_account.account_id = fnc_account.account_id
      LEFT JOIN fnc_tcode ON
         fnc_account.tcode_db_id = fnc_tcode.tcode_db_id AND
         fnc_account.tcode_id = fnc_tcode.tcode_id
      LEFT JOIN invoice_info ON
         fnc_xaction_log.po_invoice_db_id = invoice_info.po_invoice_db_id AND
         fnc_xaction_log.po_invoice_id = invoice_info.po_invoice_id AND
         fnc_xaction_log.po_invoice_line_id = invoice_info.po_invoice_line_id
      LEFT JOIN po_info ON
         fnc_xaction_log.po_db_id = po_info.po_db_id AND
         fnc_xaction_log.po_id = po_info.po_id AND
         fnc_xaction_log.po_line_id = po_info.po_line_id
      LEFT JOIN eqp_part_no ON
         fnc_xaction_log.part_no_db_id = eqp_part_no.part_no_db_id AND
         fnc_xaction_log.part_no_id = eqp_part_no.part_no_id
      LEFT JOIN inv_inv ON
         fnc_xaction_log.inv_no_db_id = inv_inv.inv_no_db_id AND
         fnc_xaction_log.inv_no_id = inv_inv.inv_no_id
      LEFT JOIN trx_wp_info ON
         trx_wp_info.transaction_id = fnc_xaction_log.alt_id
   WHERE
      -- filters from reports
      fnc_xaction_log.xaction_dt BETWEEN aidt_from_date AND aidt_to_date
      AND
      DECODE(aiv_account_code, '*ALL', '1', fnc_account.account_cd) = DECODE(aiv_account_code, '*ALL', '1', aiv_account_code)
      AND
      fnc_account.account_type_cd IN
         (
          -- the ait_account_type_code parameter is a separated comma string
          -- the logic below will convert the string into rows and trim out
          -- blank spaces (example: 'A,B,C' will be transformed to A B C rows
          SELECT
             TRIM(REGEXP_SUBSTR(ait_account_type_code,'[^,]+', 1, LEVEL))
          FROM
             DUAL
          CONNECT BY
             REGEXP_SUBSTR(ait_account_type_code, '[^,]+', 1, LEVEL)
             IS NOT NULL
         )
   ORDER BY
      fnc_xaction_log.xaction_dt,
      fnc_xaction_log.xaction_id;

   lrec_fnc_tx   detail_financial_tx_ttyp;

BEGIN

   OPEN lcur_fnc_tx;
   FETCH lcur_fnc_tx BULK COLLECT INTO lrec_fnc_tx;
   CLOSE lcur_fnc_tx;

   RETURN lrec_fnc_tx;

EXCEPTION
   WHEN OTHERS THEN
      RAISE;

END get_detailed_financial_tx;

END report_detailed_financial_log;
/