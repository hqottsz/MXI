--liquibase formatted sql


--changeSet isSchedulingRulesChanged:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isSchedulingRulesChanged
* Arguments:     aOldTaskDbId, aOldTaskId - pk for the previous requirement
*		 aNewTaskDbId, aNewTaskId - pk for the current requirement
*
* Description:   This function will return 1 if the scheduling rules have been changed
*                Otherwise, return 0.
*
* Orig.Coder:    edo
* Recent Coder:  edo
* Recent Date:   2008.02.14
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isSchedulingRulesChanged
(
   aOldTaskDbId   NUMBER,
   aOldTaskId     NUMBER,
   aNewTaskDbId   NUMBER,
   aNewTaskId     NUMBER

) RETURN NUMBER
IS
   /* local variables */
  lNewInterval NUMBER;
  lNewDeviation NUMBER;
  lNewFound BOOLEAN;
  lNewDataTypeCd VARCHAR(20);

  lOldInterval NUMBER;
  lOldDeviation NUMBER;
  lOldFound BOOLEAN;
  lOldDataTypeCd VARCHAR(20);

  lResult NUMBER;

 /* Cursors and Records */

  -- Get old scheduling rules
  CURSOR lOldSchedulingRules IS
      SELECT
           task_sched_rule.def_interval_qt,
           task_sched_rule.def_deviation_qt,
           mim_data_type.data_type_cd
      FROM
           task_sched_rule,
           mim_data_type
      WHERE
           task_sched_rule.task_db_id = aOldTaskDbId AND
           task_sched_rule.task_id    = aOldTaskId
           AND
           mim_data_type.data_type_db_id = task_sched_rule.data_type_db_id AND
           mim_data_type.data_type_id    = task_sched_rule.data_type_id
      ORDER BY
           mim_data_type.data_type_cd;

  -- Get new scheduling rules
  CURSOR lNewSchedulingRules IS
      SELECT
           task_sched_rule.def_interval_qt,
           task_sched_rule.def_deviation_qt,
           mim_data_type.data_type_cd
      FROM
           task_sched_rule,
           mim_data_type
      WHERE
           task_sched_rule.task_db_id = aNewTaskDbId AND
           task_sched_rule.task_id    = aNewTaskId
           AND
           mim_data_type.data_type_db_id = task_sched_rule.data_type_db_id AND
           mim_data_type.data_type_id    = task_sched_rule.data_type_id
      ORDER BY
           mim_data_type.data_type_cd;

BEGIN

   lResult := 0;

   OPEN lOldSchedulingRules;
   OPEN lNewSchedulingRules;

   FETCH lOldSchedulingRules INTO lOldInterval, lOldDeviation, lOldDataTypeCd;
   FETCH lNewSchedulingRules INTO lNewInterval, lNewDeviation, lNewDataTypeCd;

   IF ( lOldDataTypeCd IS NULL AND lNewDataTypeCd IS NOT NULL )  OR
        ( lOldDataTypeCd IS NOT NULL AND lNewDataTypeCd IS NULL ) THEN
           lResult := 1;
   END IF;

   WHILE (lResult != 1) AND (lNewSchedulingRules%FOUND OR lOldSchedulingRules%FOUND) LOOP

        -- if the data type cd, interval and/or deviation do not match then return 1
        IF ( lOldDataTypeCd != lNewDataTypeCd ) OR ( lOldInterval != lNewInterval ) OR (lOldDeviation != lNewDeviation) THEN
           lResult := 1;
           EXIT;
        END IF;

        FETCH lOldSchedulingRules INTO lOldInterval, lOldDeviation, lOldDataTypeCd;
        FETCH lNewSchedulingRules INTO lNewInterval, lNewDeviation, lNewDataTypeCd;

        IF  ( lNewSchedulingRules%FOUND AND lOldSchedulingRules%NOTFOUND ) OR
            ( lNewSchedulingRules%NOTFOUND AND lOldSchedulingRules%FOUND ) THEN
           lResult := 1;
           EXIT;
        END IF;
   END LOOP;

   CLOSE lOldSchedulingRules;
   CLOSE lNewSchedulingRules;

  RETURN lResult;
END isSchedulingRulesChanged;
/        