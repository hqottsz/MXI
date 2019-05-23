--liquibase formatted sql


--changeSet report_weight_and_balance_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY report_weight_and_balance_pkg AS
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

   -----------------------------------------------------------------------------------------------------------
   -- local package constants
   -----------------------------------------------------------------------------------------------------------
   gk_row_limit    CONSTANT PLS_INTEGER := 100;


  -----------------------------------------------------------------------------
   -- Function:    GetWeightAndBalanceImpact
   -- Arguments:   reportDate
   -- Description: returns all the weight and balance impacts that occurred
   --              within the provided date or within the previous
   --              day if no reportDate is provided
  -----------------------------------------------------------------------------
   FUNCTION GetWeightAndBalanceImpact(
      reportDate        IN DATE
   ) RETURN WeightBalanceImpactTTRec PIPELINED
   IS

      CURSOR lcur_weightbalance(startDate DATE, endDate DATE) IS
        SELECT
           evt_event.event_db_id,
           evt_event.event_id,
           task_weight_balance.task_weight_balance_db_id,
           task_weight_balance.task_weight_balance_id,
           wp_main_inv.assmbl_cd,
           inv_ac_reg.ac_reg_cd,
           evt_event.event_dt,
           task_weight_balance.weight,
           task_weight_balance.moment,
           evt_event.event_sdesc,
           task_task.revision_ord
        FROM
           sched_stask req_stask
           JOIN evt_event ON
              evt_event.event_db_id = req_stask.sched_db_id AND
              evt_event.event_id    = req_stask.sched_id
           JOIN sched_stask wp_stask ON
              wp_stask.sched_db_id  = evt_event.h_event_db_id AND
              wp_stask.sched_id     = evt_event.h_event_id
           JOIN task_task ON
              task_task.task_db_id = req_stask.task_db_id AND
              task_task.task_id    = req_stask.task_id
           JOIN inv_inv wp_main_inv ON
              wp_main_inv.inv_no_db_id  = wp_stask.main_inv_no_db_id AND
              wp_main_inv.inv_no_id     = wp_stask.main_inv_no_id
           JOIN inv_ac_reg ON
              inv_ac_reg.inv_no_db_id   = wp_main_inv.inv_no_db_id AND
              inv_ac_reg.inv_no_id      = wp_main_inv.inv_no_id
           JOIN task_weight_balance ON
              task_weight_balance.task_db_id = req_stask.task_db_id AND
              task_weight_balance.task_id    = req_stask.task_id
        WHERE
           evt_event.event_dt BETWEEN startDate AND endDate
           AND
           (
              (
                task_weight_balance.part_no_db_id = wp_main_inv.part_no_db_id AND
                task_weight_balance.part_no_id     = wp_main_inv.part_no_id
              )
              OR
              (
                task_weight_balance.part_no_db_id IS NULL
                AND
                 NOT EXISTS
                   (
                      SELECT 1
                      FROM
                         task_weight_balance spec_part_weightbalance
                      WHERE
                         spec_part_weightbalance.task_db_id = req_stask.task_db_id AND
                         spec_part_weightbalance.task_id    = req_stask.task_id
                         AND
                         spec_part_weightbalance.part_no_db_id = wp_main_inv.part_no_db_id AND
                         spec_part_weightbalance.part_no_id = wp_main_inv.part_no_id
                   )
              )
           );

      lWeightAndBalanceImpactTable  WeightBalanceImpactTTRec;
      aStartdate DATE;
      aEndDate DATE;
   BEGIN
      IF  reportDate IS NULL THEN
         --Default to midnight yesterday
         aStartDate:= TRUNC(SYSDATE)-1;
      ELSE
         --Set to midnight of the provided date
         aStartDate:= TRUNC(reportDate);
      END IF;

      -- The end date is 11:59:59 pm of the start day
      aEndDate := aStartDate + 1 - 1/(24*60*60);

      OPEN  lcur_weightbalance(aStartdate,aEndDate);
      LOOP
         FETCH lcur_weightbalance
         BULK COLLECT INTO lWeightAndBalanceImpactTable
         LIMIT gk_row_limit;

         EXIT WHEN lWeightAndBalanceImpactTable.COUNT = 0;

         FOR i IN 1..lWeightAndBalanceImpactTable.COUNT LOOP
            PIPE ROW (lWeightAndBalanceImpactTable(i));
         END LOOP;

      END LOOP;
      EXCEPTION
        WHEN OTHERS THEN
           RAISE;
   END GetWeightAndBalanceImpact;


  -----------------------------------------------------------------------------
   -- Function:    GetWeightAndBalanceUnits
   -- Description: Returns a single row containing the configured WEIGHT and MOMENT units
  -----------------------------------------------------------------------------
   FUNCTION GetWeightAndBalanceUnits RETURN  WeightBalanceUnitTTRec PIPELINED
   IS
      lWeightBalanceMeasurementUnit WeightBalanceUnitTRec;
   BEGIN

      SELECT PARM_VALUE
      INTO lWeightBalanceMeasurementUnit.weight_unit
      FROM utl_config_parm
      WHERE parm_name = 'WEIGHT_BALANCE_WEIGHT_UNIT';

      SELECT parm_value
      INTO lWeightBalanceMeasurementUnit.moment_unit
      FROM utl_config_parm
      WHERE parm_name = 'WEIGHT_BALANCE_MOMENT_UNIT';

      PIPE ROW (lWeightBalanceMeasurementUnit);
   END GetWeightAndBalanceUnits;

END report_weight_and_balance_pkg;
/