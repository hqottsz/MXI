--liquibase formatted sql


--changeSet report_weight_and_balance_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE report_weight_and_balance_pkg AS
/************************************************************************
** Description: Weight And Balance Impact Report Package
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

  TYPE WeightBalanceUnitTRec IS RECORD(
    weight_unit   utl_config_parm.parm_value%TYPE,
    moment_unit   utl_config_parm.parm_value%TYPE
  );

  TYPE WeightBalanceUnitTTRec IS TABLE OF WeightBalanceUnitTRec;

  TYPE WeightBalanceImpactTRec IS RECORD
   (
    event_db_id    evt_event.event_db_id%TYPE,
    event_id       evt_event.event_id%TYPE,
    task_weight_balance_db_id task_weight_balance.task_weight_balance_db_id%TYPE,
    task_weight_balance_id task_weight_balance.task_weight_balance_id%TYPE,
    assembl_cd     eqp_assmbl.assmbl_cd%TYPE,
    ac_reg_cd      inv_ac_reg.ac_reg_cd%TYPE,
    event_dt       evt_event.event_dt%TYPE,
    weight         task_weight_balance.weight%TYPE,
    moment         task_weight_balance.moment%TYPE,
    req_name       evt_event.event_sdesc%TYPE,
    task_rev_ord   task_task.revision_ord%TYPE
    );

  TYPE WeightBalanceImpactTTRec IS TABLE OF WeightBalanceImpactTRec;

  -----------------------------------------------------------------------------
   -- Function:    GetWeightAndBalanceImpact
   -- Arguments:   reportDate
   -- Description: returns all the weight and balance impacts that occurred
   --              within the provided date or within the previous
   --              day if no reportDate is provided
  -----------------------------------------------------------------------------
  FUNCTION GetWeightAndBalanceImpact(
      reportDate        IN DATE
   ) RETURN WeightBalanceImpactTTRec PIPELINED;


  -----------------------------------------------------------------------------
   -- Function:    GetWeightAndBalanceUnits
   -- Description: Returns a single row containing the configured WEIGHT and MOMENT units
  -----------------------------------------------------------------------------
  FUNCTION GetWeightAndBalanceUnits RETURN WeightBalanceUnitTTRec PIPELINED;
END report_weight_and_balance_pkg;
/