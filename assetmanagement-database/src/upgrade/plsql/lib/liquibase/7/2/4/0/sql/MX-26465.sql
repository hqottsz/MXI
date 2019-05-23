--liquibase formatted sql


--changeSet MX-26465:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskPlanningYieldDate
* Arguments:     aSchedDbId, aSchedId - pk for the root task
*                aAcftDbId, aAcftId   - pk for the root aircraft
*                aCurrentDate         - the Current Date of execution
*
* Description:   This function will return the planned yield date for the given task.
*
* Orig.Coder:    jclarkin
* Recent Coder:  jclarkin
* Recent Date:   2007.03.13
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskPlanningYieldDate
(
   aSchedDbId     NUMBER,
   aSchedId       NUMBER,
   aAcftDbId      NUMBER,
   aAcftId        NUMBER,  
   aCurrentDate   DATE
) RETURN DATE
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
   ld_CurrentDate              evt_sched_dead.sched_dead_dt%TYPE;
   ol_Return                   NUMBER;

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
         evt_event.hist_bool = 0	AND
         evt_event.rstat_cd	= 0
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
      
   /* Return Variable */
   lPlanningYieldDate DATE;
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
         RETURN NULL;
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
      END IF;

   CLOSE lCurDeadline;

   -- Get the minimum planning yield from the task which should be highest
   SELECT NVL( sched_stask.min_plan_yield_pct,  
               (SELECT utl_config_parm.parm_value AS default_pct
                FROM
                      utl_config_parm
                WHERE
                      utl_config_parm.parm_name = 'TASK_MINIMUM_PLANNING_YIELD'
               )
             ) INTO ln_MinPlanningYield
   FROM
      sched_stask
   WHERE
      sched_stask.sched_db_id = aSchedDbId AND
      sched_stask.sched_id    = aSchedId;
      
   /*** LOGIC ***/

   -- No Minimum Plan Yield Date if no Yield Percentage
   IF ( ln_MinPlanningYield IS NULL ) THEN
      RETURN NULL; 
   END IF;
   
   -- No Min Plan Yield Date if no Due Date
   IF ( ld_TaskDueDate IS NULL ) THEN
      RETURN NULL; 
   END IF;

   -- No Min Plan Yield Date if Interval is negative
   IF ( ln_DeadlineInterval < 0 ) THEN
      RETURN NULL; 
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

      -- Determine the Planning Yield Date based on Forecasting
      ld_CurrentDate := aCurrentDate;
      EVENT_PKG.findForecastedDeadDt(
            aAcftDbId,
            aAcftId,
            ln_DeadlineDataTypeDbId,
            ln_DeadlineDataTypeId,
            ln_YieldDeadlineQt - ln_CurrentUsageTSN,
            ld_CurrentDate,
            lPlanningYieldDate,       -- Used to return the date the deadline will become due
            ol_Return
      );
      
   ELSE
      RETURN NULL; -- Case for invalid Usage Type selected.
   END IF;
   
   -- If Planning Yield Date after the Due Date, return Null
   IF ( lPlanningYieldDate > ld_TaskDueDate ) THEN
      RETURN NULL; 
   END IF;


   RETURN lPlanningYieldDate;
EXCEPTION

   WHEN OTHERS THEN RETURN NULL;

END getTaskPlanningYieldDate;
/