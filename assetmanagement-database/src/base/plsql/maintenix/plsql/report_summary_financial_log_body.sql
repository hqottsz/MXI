--liquibase formatted sql


--changeSet report_summary_financial_log_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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