--liquibase formatted sql


--changeSet OPER-4755:1 stripComments:false
-- Detailed Financial Log
-- Update utl_menu_item 
UPDATE
   utl_menu_item
SET   
   menu_name = 'web.menuitem.DETAILED_FNC_LOG_REPORT' 
WHERE
   menu_id = 120922;     

--changeSet OPER-4755:2 stripComments:false
-- Update utl_report_type
UPDATE
   utl_report_type
SET
   report_path = '/organizations/Maintenix/Reports/Core/inventory/DetailedFinancialLog'
WHERE
   report_name = 'inventory.DetailInvFncLog';

--changeSet OPER-4755:3 stripComments:false
-- Summary Financial Log   
-- Update utl_menu_item
UPDATE
   utl_menu_item
SET   
   menu_name = 'web.menuitem.SUMMARY_FNC_LOG_REPORT' 
WHERE
   menu_id = 120923;     

--changeSet OPER-4755:4 stripComments:false
-- Update utl_report_type
UPDATE
   utl_report_type
SET
   report_path = '/organizations/Maintenix/Reports/Core/inventory/SummaryFinancialLog'
WHERE
   report_name = 'inventory.SummaryInvFncLog';

--changeSet OPER-4755:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- drop types object 
BEGIN
   utl_migr_schema_pkg.type_drop('detail_financial_tx_rtyp');
END;
/

--changeSet OPER-4755:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.type_drop('summary_financial_tx_rtyp');
END;
/

--changeSet OPER-4755:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- detailed financial transaction record type
CREATE OR REPLACE TYPE detail_financial_tx_rtyp
AS
  OBJECT
  (
    xaction_id      NUMBER (10) ,
    xaction_dt      DATE ,
    xaction_type_cd VARCHAR2 (16) ,
    xaction_ldesc   VARCHAR2 (4000) ,
    currency_cd     VARCHAR2 (8) ,
    account_cd      VARCHAR2 (80) ,
    account_type_cd VARCHAR2 (8) ,
    ext_key_sdesc   VARCHAR2 (80) ,
    credit_cost     NUMBER (15,5) ,
    debit_cost      NUMBER (15,5) ,
    tcode_cd        VARCHAR2 (80) ,
    po_number       VARCHAR2 (500) ,
    invoice_number  VARCHAR2 (500) ,
    part_no_oem     VARCHAR2 (40) ,
    serial_no_oem   VARCHAR2 (40) ,
    wo_ref_sdesc    VARCHAR2 (80) ,
    ac_reg_cd       VARCHAR2 (10) ,
    loc_cd          VARCHAR2 (2000) ,
    supply_loc_cd   VARCHAR2 (2000) 
 ) NOT FINAL ;
/

--changeSet OPER-4755:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.type_drop('detail_financial_tx_ttyp');
END;
/

--changeSet OPER-4755:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- detailed financial transaction table type
CREATE OR REPLACE TYPE detail_financial_tx_ttyp IS TABLE OF detail_financial_tx_rtyp;
/

--changeSet OPER-4755:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- summary financial transaction record type
CREATE OR REPLACE TYPE summary_financial_tx_rtyp AS OBJECT (
    account_cd       VARCHAR2 (80) ,
    account_type_cd  VARCHAR2 (8) ,
    credit_cost      NUMBER (15,5) ,
    debit_cost       NUMBER (15,5) ,
    tcode_cd         VARCHAR2 (80) ,
    ext_key_sdesc    VARCHAR2 (80) ,
    default_currency VARCHAR2 (8) 
) NOT FINAL ;
/

--changeSet OPER-4755:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.type_drop('summary_financial_tx_ttyp');
END;
/

--changeSet OPER-4755:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- summary financial transaction table type
CREATE OR REPLACE TYPE summary_financial_tx_ttyp IS TABLE OF summary_financial_tx_rtyp ;
/

--changeSet OPER-4755:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- plsql detailed financial transaction report package
CREATE OR REPLACE PACKAGE report_detailed_financial_log AS

/************************************************************************
** Date:        March 15, 2016
** Author:      Gustavo Pichetto
** Description: Detailed Financial Log report
*************************************************************************
**
** Confidential, proprietary and/or trade secret information of
** Mxi Technologies.
**
** Copyright 2005-2016 Mxi Technologies. All Rights Reserved.
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
   ) RETURN detail_financial_tx_ttyp ;


END report_detailed_financial_log;
/

--changeSet OPER-4755:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
** Copyright 2005-2016 Mxi Technologies. All Rights Reserved.
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
     xaction_issue_xfer.transaction_id,
     wp.wo_ref_sdesc      AS work_package_number,
     inv_ac_reg.ac_reg_cd AS registration_code,
     inv_loc_to.loc_cd,
     supply_loc.loc_cd AS supply_loc_cd
  FROM
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
            fnc_xaction_log.event_db_id = turnin_xfer.xfer_db_id AND
            fnc_xaction_log.event_id    = turnin_xfer.xfer_id
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
   -- get aircraft inv info from req_part for a aircraft work package task
   LEFT JOIN inv_ac_reg ON
      req_part.req_ac_inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      req_part.req_ac_inv_no_id    = inv_ac_reg.inv_no_id
   LEFT JOIN inv_inv acft_inv ON
      inv_ac_reg.inv_no_db_id = acft_inv.inv_no_db_id AND
      inv_ac_reg.inv_no_id    = acft_inv.inv_no_id
   -- get location and supply chain location
   LEFT JOIN inv_loc inv_loc_to ON
      inv_loc_to.loc_db_id = req_part.req_loc_db_id AND
      inv_loc_to.loc_id = req_part.req_loc_id
   LEFT JOIN inv_loc supply_loc ON
      supply_loc.loc_db_id = inv_loc_to.supply_loc_db_id AND
      supply_loc.loc_id = inv_loc_to.supply_loc_id
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

--changeSet OPER-4755:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- plsql summary financial transaction report package
CREATE OR REPLACE PACKAGE report_summary_financial_log AS

/************************************************************************
** Date:        March 15, 2016
** Author:      Gustavo Pichetto
** Description: Summary Financial Log report
*************************************************************************
**
** Confidential, proprietary and/or trade secret information of
** Mxi Technologies.
**
** Copyright 2005-2016 Mxi Technologies. All Rights Reserved.
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
   -- Description: Bring summary financial transactions results 
   -----------------------------------------------------------------------------
   FUNCTION get_summary_financial_tx (  
      aidt_from_date   IN DATE,
      aidt_to_date     IN DATE,
      aiv_account_code IN VARCHAR2,
      ait_account_type_code IN VARCHAR2
   ) RETURN summary_financial_tx_ttyp ;
  
   
END report_summary_financial_log;
/

--changeSet OPER-4755:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY report_summary_financial_log AS

/************************************************************************
** Date:        March 15, 2016
** Author:      Gustavo Pichetto
** Description: Summary Financial Log report
*************************************************************************
**
** Confidential, proprietary and/or trade secret information of
** Mxi Technologies.
**
** Copyright 2005-2016 Mxi Technologies. All Rights Reserved.
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
   -- Description: Bring summary financial transactions results 
-----------------------------------------------------------------------------
FUNCTION get_summary_financial_tx (  
      aidt_from_date   IN DATE,
      aidt_to_date     IN DATE,
      aiv_account_code IN VARCHAR2,
      ait_account_type_code IN VARCHAR2
   ) RETURN summary_financial_tx_ttyp 
IS

 CURSOR lcur_fnc_tx IS
   SELECT
      summary_financial_tx_rtyp (
         fnc_account.account_cd,
         fnc_account.account_type_cd,
         SUM(fnc_xaction_account.credit_cost),
         SUM(fnc_xaction_account.debit_cost ),
         fnc_tcode.tcode_cd,
         fnc_account.ext_key_sdesc,
        (
          SELECT 
             ref_currency.currency_cd  
          FROM 
             ref_currency 
          WHERE 
             ref_currency.default_bool = 1
        )
      )
   FROM
      fnc_xaction_log
      INNER JOIN fnc_xaction_account ON
         fnc_xaction_account.xaction_db_id = fnc_xaction_log.xaction_db_id AND
         fnc_xaction_account.xaction_id = fnc_xaction_log.xaction_id
      INNER JOIN fnc_account ON
         fnc_xaction_account.account_db_id = fnc_account.account_db_id AND
         fnc_xaction_account.account_id    = fnc_account.account_id
      LEFT JOIN fnc_tcode ON
         fnc_tcode.tcode_db_id = fnc_account.tcode_db_id AND
         fnc_tcode.tcode_id = fnc_account.tcode_id
   WHERE
      -- filters from reports
      fnc_xaction_log.xaction_dt BETWEEN aidt_from_date AND aidt_to_date
      AND
      DECODE(aiv_account_code, '*ALL', '1', fnc_account.account_cd) = DECODE(aiv_account_code, '*ALL', '1', aiv_account_code)
      AND
      fnc_account.account_type_cd IN 
         ( 
          SELECT 
             -- the ait_account_type_code parameter is a separated comma string
             -- the logic below will convert the string into rows and trim out
             -- blank spaces (example: 'A,B,C' will be transformed to A B C rows
             TRIM(REGEXP_SUBSTR(ait_account_type_code,'[^,]+', 1, LEVEL))
          FROM 
             DUAL
          CONNECT BY 
             REGEXP_SUBSTR(ait_account_type_code, '[^,]+', 1, LEVEL) 
             IS NOT NULL   
         )
   GROUP BY
      fnc_account.account_cd,
      fnc_account.ext_key_sdesc,
      fnc_account.account_type_cd,
      fnc_tcode.tcode_cd
   ORDER BY    
      fnc_account.account_cd, 
      fnc_account.account_type_cd;

   lrec_fnc_tx   summary_financial_tx_ttyp;

BEGIN

   OPEN lcur_fnc_tx;
   FETCH lcur_fnc_tx BULK COLLECT INTO lrec_fnc_tx;
   CLOSE lcur_fnc_tx;

   RETURN lrec_fnc_tx;

EXCEPTION
   WHEN OTHERS THEN
      RAISE;

END get_summary_financial_tx;

END report_summary_financial_log;
/