--liquibase formatted sql


--changeSet report_summary_financial_log_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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