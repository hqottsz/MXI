--liquibase formatted sql


--changeSet isWithinPlanningYield:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isWithinPlanningYield
* Arguments:     aSchedDbId, aSchedId - pk for the root task
*                aAcftDbId, aAcftId   - pk for the root aircraft
*                aCurrentDate         - the Current Date of execution
*
* Description:   This function determines if the current date/usage is within the Planned Yield Date/Usage Qt 
*                and Task Due Date/Usage Qt
*                For Calendar Based Due Dates, this function will return '1' if
*                the current date falls between the plnaning yield date and the task due date.
*                Otherwise, it will return '0'.
*                For Usage Based Due Dates, this function will return the '1' if
*                the current usage falls between the planning yield usage and the task due usage.
*                Otherwise, it will return '0'.
*                This function is a simplified version of the getTaskPlanningYieldDate function 
*
* Orig.Coder:    mbajer
* Recent Coder:  edo
* Recent Date:   2007.04.03
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isWithinPlanningYield
(
   aSchedDbId     NUMBER,
   aSchedId       NUMBER,
   aAcftDbId      NUMBER,
   aAcftId        NUMBER,
   aCurrentDate   DATE
) RETURN NUMBER
IS
   /* Local Variables */
   ln_MinPlanningYield         sched_stask.min_plan_yield_pct%TYPE;
   ld_TaskDueDate              evt_sched_dead.sched_dead_dt%TYPE;
   ln_UsageDueAtQt             evt_sched_dead.sched_dead_qt%TYPE;
   ls_DeadlineDataType         mim_data_type.data_type_cd%TYPE;
   ls_DeadlineDomainType       mim_data_type.domain_type_cd%TYPE;
   ln_DeadlineDataTypeDbId     mim_data_type.data_type_db_id%TYPE;
   ln_DeadlineDataTypeId       mim_data_type.data_type_id%TYPE;
   ln_DeadlineInterval         evt_sched_dead.interval_qt%TYPE;
   ln_CurrentUsageTSN          inv_curr_usage.tsn_qt%TYPE;
   ln_YieldDeadlineQt          evt_sched_dead.sched_dead_qt%TYPE;
   ln_UnitMultiplier           ref_eng_unit.ref_mult_qt%TYPE;
   lPlanningYieldDate          DATE;

   /* Cursors and Records */

   CURSOR lCurDeadline IS
      SELECT
         evt_event.event_db_id AS event_db_id,
         evt_event.event_id AS event_id,
         -- Deadline Details
         evt_sched_dead.sched_dead_qt,
         evt_sched_dead.sched_dead_dt,
         evt_sched_dead.interval_qt,
         mim_data_type.data_type_db_id,
         mim_data_type.data_type_id,
         mim_data_type.data_type_cd,
         mim_data_type.domain_type_cd,
         ref_eng_unit.ref_mult_qt,
         DECODE( sched_stask.min_plan_yield_pct, NULL,
            config_min_plan_yield.default_pct,
            sched_stask.min_plan_yield_pct
         ) AS min_plan_yield_pct,
         -- Date Sorting tool
         DECODE(
            task_task.last_sched_dead_bool,
            NULL, (evt_sched_dead.sched_dead_dt - aCurrentDate),
            DECODE( task_task.last_sched_dead_bool, 0,
               (evt_sched_dead.sched_dead_dt - aCurrentDate),
               -1 * (evt_sched_dead.sched_dead_dt - aCurrentDate)
           )
         ) AS sort_column
      FROM
         (  SELECT
               evt_event.event_db_id,
               evt_event.event_id
            FROM
               evt_event
            WHERE rstat_cd = 0
            START WITH
               evt_event.event_db_id = aSchedDbId AND
               evt_event.event_id    = aSchedId
            CONNECT BY
               evt_event.nh_event_db_id = PRIOR evt_event.event_db_id AND
               evt_event.nh_event_id    = PRIOR evt_event.event_id
         ) task_tree,
         evt_event,
         sched_stask,
         evt_sched_dead,
         task_task,
         mim_data_type,
         ref_eng_unit,
         -- Default Value
         (  SELECT
               utl_config_parm.parm_value AS default_pct
            FROM
               utl_config_parm
            WHERE
               utl_config_parm.parm_name = 'TASK_MINIMUM_PLANNING_YIELD'
         ) config_min_plan_yield
      WHERE
         -- Get Driving Deadling specific tables
         evt_event.event_db_id = task_tree.event_db_id AND
         evt_event.event_id    = task_tree.event_id
         AND
         evt_event.hist_bool = 0
         AND
         sched_stask.sched_db_id = evt_event.event_db_id and
         sched_stask.sched_id    = evt_event.event_id
         AND
         task_task.task_db_id (+)= sched_stask.task_db_id and
         task_task.task_id    (+)= sched_stask.task_id
         AND
         evt_sched_dead.event_db_id       = evt_event.event_db_id AND
         evt_sched_dead.event_id          = evt_event.event_id AND
         evt_sched_dead.sched_driver_bool = 1
         AND
         -- Get the Usage Data Type
         mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
         mim_data_type.data_type_id    = evt_sched_dead.data_type_id
         AND
         -- Get the Unit Data
         ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
         ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd
      ORDER BY
         sort_column ASC;
   lRecDeadline lCurDeadline%ROWTYPE;

BEGIN

   /*** DATA LOOKUP ***/

   /* Determine the Driving Deadline details
      - Usage Type,
      - Usage Domain Type,
      - Deadline Qt,
      - Deadline Date,
      - Deadline Interval,
      - Unit Converter to Days
      - Minumum Planning Yield Percentage */
   OPEN lCurDeadline;
   FETCH lCurDeadline INTO lRecDeadline;

      /* If no Driving Deadline, then there is no Yield Date */
      IF NOT lCurDeadline%FOUND THEN
         CLOSE lCurDeadline;
         RETURN 0;
      /* Otherwise, get all the details */
      ELSE
         ln_DeadlineDataTypeDbId := lRecDeadline.data_type_db_id;
         ln_DeadlineDataTypeId := lRecDeadline.data_type_id;
         ls_DeadlineDataType := lRecDeadline.data_type_cd;
         ls_DeadlineDomainType := lRecDeadline.domain_type_cd;
         ln_UsageDueAtQt := lRecDeadline.sched_dead_qt;
         ld_TaskDueDate := lRecDeadline.sched_dead_dt;
         ln_DeadlineInterval := lRecDeadline.interval_qt;
         ln_UnitMultiplier := lRecDeadline.ref_mult_qt;
         ln_MinPlanningYield := lRecDeadline.min_plan_yield_pct;
      END IF;

   CLOSE lCurDeadline;

   /*** LOGIC ***/

   -- return '0' if no Yield Percentage
   IF ( ln_MinPlanningYield IS NULL ) THEN
      RETURN 0;
   END IF;

   -- return '0' if no Due Date
   IF ( ld_TaskDueDate IS NULL ) THEN
      RETURN 0;
   END IF;

   -- return '0' if Interval is negative
   IF ( ln_DeadlineInterval < 0 ) THEN
      RETURN 0;
   END IF;

   /* Calculate the Planning Yield Date */

   -- CASE 1: Calendar Based Due Dates
   IF ( ls_DeadlineDomainType = 'CA' ) THEN

      -- if data type Months, then use built in Oracle function to calculate % of Months
      IF ( ( ls_DeadlineDataType = 'CLMON' ) OR ( ls_DeadlineDataType = 'CMON' ) ) THEN
         lPlanningYieldDate :=
               ADD_MONTHS(
                  ld_TaskDueDate,
                  - ( ln_DeadlineInterval * (1 - ln_MinPlanningYield) )
               );
      ELSE
         -- Use Default Conversion rate from our Database
         ln_DeadlineInterval := ln_DeadlineInterval * ln_UnitMultiplier;
         lPlanningYieldDate := ld_TaskDueDate - ( ln_DeadlineInterval * (1 - ln_MinPlanningYield) );
      END IF;

      -- return '1' if the current date falls between the planning yield date and the task due date.
      -- Otherwise, return '0'.
      IF (lPlanningYieldDate <= aCurrentDate AND ld_TaskDueDate >= aCurrentDate) THEN
         RETURN 1;
      ELSE
         RETURN 0;
      END IF;

   -- CASE 2: Usage Based Due Dates
   ELSIF ( ls_DeadlineDomainType = 'US' ) THEN

      /* Determine the Aircraft for this Task, and Current Usage */
      SELECT inv_curr_usage.tsn_qt
      INTO   ln_CurrentUsageTSN
      FROM   inv_curr_usage
      WHERE
         -- Tie to Current Usage
         inv_curr_usage.inv_no_db_id = aAcftDbId AND
         inv_curr_usage.inv_no_id    = aAcftId
         AND
         inv_curr_usage.data_type_db_id = ln_DeadlineDataTypeDbId AND
         inv_curr_usage.data_type_id    = ln_DeadlineDataTypeId;

      -- Calculate the Expected Usage QT when at the Planned Yield Percentage
      ln_YieldDeadlineQt :=  ln_UsageDueAtQt - ( ln_DeadlineInterval * (1 - ln_MinPlanningYield) );

      -- return '1' if the current usage falls between the planned yield usage qt and the due usage qt
      -- otherwise, return '0'
      IF( ln_YieldDeadlineQt<=ln_CurrentUsageTSN AND ln_UsageDueAtQt>=ln_CurrentUsageTSN ) THEN
          RETURN 1;
      ELSE
          RETURN 0;
      END IF;

   ELSE
      RETURN 0; -- Case for invalid Usage Type selected.
   END IF;


EXCEPTION

   WHEN OTHERS THEN RETURN 0;

END isWithinPlanningYield;
/