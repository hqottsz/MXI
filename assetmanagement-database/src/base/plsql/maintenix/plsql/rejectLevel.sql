--liquibase formatted sql


--changeSet rejectLevel:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure rejectLevel(
aWfLevelDbId in number,
aWfLevelId in number,
aReApproveWfLevelDbId in number, -- Default is -1
aReApproveWfLevelId in number,   -- Default is -1
aHrDbId in number,
aHrId in number,
aError out number
) is
--variable required
t_wf_step_db_id               wf_step.wf_step_db_id%TYPE;
t_wf_step_id                  wf_step.wf_step_id%TYPE;
t_root_wf_step_db_id          wf_step.wf_step_db_id%TYPE;
t_root_wf_step_id             wf_step.wf_step_id%TYPE;
t_wf_db_id                    wf_wf.wf_db_id%TYPE;
t_wf_id                       wf_wf.wf_id%TYPE;
t_new_wf_db_id                wf_wf.wf_db_id%TYPE;
t_new_wf_id                   wf_wf.wf_id%TYPE;
t_wf_defn_db_id               wf_defn.wf_defn_db_id%TYPE;
t_wf_defn_id                  wf_defn.wf_defn_id%TYPE;
t_wf_priority_db_id           wf_wf.wf_priority_db_id%TYPE;
t_wf_priority_cd              wf_wf.wf_priority_cd%TYPE;
t_wf_due_date                 wf_wf.wf_due_date%TYPE;
t_wf_defn_step_db_id          wf_defn_step.wf_defn_step_db_id%TYPE;
t_wf_defn_step_id             wf_defn_step.wf_defn_step_id%TYPE;
lTaskDbId                     task_task.task_db_id%TYPE;
lTaskId                       task_task.task_id%TYPE;

lPrevStepCt NUMBER;

/**
   Identify all INPROGRESS, PEND step-level within the Workflow
*/
CURSOR lcur_PendingLevels( lWfDbId IN NUMBER, lWfId IN NUMBER ) IS
         SELECT
            wf_step_levels.wf_level_db_id,
            wf_step_levels.wf_level_id
         FROM
            wf_steps
            INNER JOIN wf_step  ON wf_step.wf_step_db_id   = wf_steps.wf_step_db_id   AND
                                   wf_step.wf_step_id      = wf_steps.wf_step_id
            INNER JOIN wf_step_levels ON wf_step_levels.wf_step_db_id  = wf_step.wf_step_db_id AND
                                      wf_step_levels.wf_step_id        = wf_step.wf_step_id
         WHERE
            wf_steps.wf_db_id   = lWfDbId   AND
            wf_steps.wf_id      = lWfId
            AND
            wf_step.wf_step_status_cd = 'PENDING'

         UNION ALL

         -- Identify Leaf levels within the Workflow
         SELECT
            wf_step_levels.wf_level_db_id,
            wf_step_levels.wf_level_id
         FROM
            wf_steps
            RIGHT OUTER JOIN wf_step_group   ON wf_step_group.wf_step_db_id = wf_steps.wf_step_db_id AND
                                                wf_step_group.wf_step_id    = wf_steps.wf_Step_id
            RIGHT OUTER JOIN wf_step_levels  ON wf_step_levels.wf_step_db_id  = wf_step_group.child_wf_step_db_id   AND
                                                wf_step_levels.wf_step_id     = wf_step_group.child_wf_step_id
            RIGHT OUTER JOIN wf_step   ON wf_step.wf_step_db_id   = wf_step_levels.wf_step_db_id AND
                                          wf_step.wf_step_id      = wf_step_levels.wf_step_id
         WHERE
            wf_steps.wf_db_id = lWfDbId AND
            wf_steps.wf_id    = lWfId
            AND
            wf_step.wf_step_status_cd = 'PENDING'

         UNION ALL

         SELECT
            aWfLevelDbId,
            aWfLevelId
         FROM
            dual;

begin
   aError := 0;
   -- identify the step corresponding to the posted level
   SELECT
      wf_step_db_id,
      wf_step_id
   INTO
      t_wf_step_db_id,
      t_wf_step_id
   FROM
      wf_step_levels
   WHERE
      wf_step_levels.wf_level_db_id = aWfLevelDbId AND
      wf_step_levels.wf_level_id    = aWfLevelId;

-- identify the workflow
   SELECT
      wf_db_id,
      wf_id
   INTO
      t_wf_db_id,
      t_wf_id
   FROM
   (
      SELECT
         wf_steps.wf_db_id,
         wf_steps.wf_id
      FROM
         wf_step_group,
         wf_steps,
         wf_step_levels
      WHERE
         wf_steps.wf_step_db_id = wf_step_group.wf_step_db_id AND
         wf_steps.wf_step_id    = wf_step_group.wf_step_id
      AND
         wf_step_group.child_wf_step_db_id = t_wf_step_db_id AND
         wf_step_group.child_wf_step_id    = t_wf_step_id
      UNION
      SELECT
         wf_steps.wf_db_id,
         wf_steps.wf_id
      FROM
         wf_steps,
         wf_step_levels
      WHERE
         wf_steps.wf_step_db_id = t_wf_step_db_id AND
         wf_steps.wf_step_id    = t_wf_step_id
   )
   WHERE ROWNUM = 1;

   -- identify all levels whose status is to be set to 'REJECTED'
   FOR pending IN lcur_PendingLevels( t_wf_db_id, t_wf_id ) LOOP

      -- identify the step corresponding to the current level
      SELECT
         wf_step_db_id,
         wf_step_id
      INTO
         t_wf_step_db_id,
         t_wf_step_id
      FROM
         wf_step_levels
      WHERE
         wf_step_levels.wf_level_db_id = pending.wf_level_db_id AND
         wf_step_levels.wf_level_id    = pending.wf_level_id;

      --Reject the provided level's step and its sibling steps
      UPDATE
         wf_step
      SET
         wf_step.wf_step_status_db_id = 0,
         wf_step.wf_step_status_cd = 'REJECTED'
      WHERE
         ( wf_step.wf_step_db_id, wf_step.wf_step_id)
         IN
         (  -- Provided step
            SELECT
               t_wf_step_db_id,
               t_wf_step_id
            FROM
               dual

            UNION ALL
            -- Sibling steps
            SELECT
               sibling_step_group.child_wf_step_db_id,
               sibling_step_group.child_wf_step_id
            FROM
               wf_step_group
               INNER JOIN wf_step_group sibling_step_group  ON wf_step_group.wf_step_db_id   = sibling_step_group.wf_step_db_id  AND
                                                               wf_step_group.wf_step_id      = sibling_step_group.wf_step_id
            WHERE
               wf_step_group.child_wf_step_db_id   = t_wf_step_db_id AND
               wf_step_group.child_wf_step_id      = t_wf_step_id
         );

      -- set the audit trail for the current level
      UPDATE
         wf_level
      SET
         wf_level.level_hr_db_id = aHrDbId,
         wf_level.level_hr_id    = aHrId,
         wf_level.level_dt       = SYSDATE
      WHERE
         ( wf_level.wf_level_db_id, wf_level.wf_level_id )
         IN
         (  SELECT
               wf_step_levels.wf_level_db_id,
               wf_step_levels.wf_level_id
            FROM
               wf_step_levels
            WHERE
               ( wf_step_levels.wf_step_db_id, wf_step_levels.wf_step_id )
               IN
               ( -- Provided step
                  SELECT
                     t_wf_step_db_id,
                     t_wf_step_id
                  FROM
                     dual

                  UNION ALL
                  -- Sibling steps
                  SELECT
                     sibling_step_group.child_wf_step_db_id,
                     sibling_step_group.child_wf_step_id
                  FROM
                     wf_step_group
                     INNER JOIN wf_step_group sibling_step_group  ON wf_step_group.wf_step_db_id   = sibling_step_group.wf_step_db_id  AND
                                                                     wf_step_group.wf_step_id      = sibling_step_group.wf_step_id
                  WHERE
                     wf_step_group.child_wf_step_db_id   = t_wf_step_db_id AND
                     wf_step_group.child_wf_step_id      = t_wf_step_id
               )
         );

   END LOOP;

   --SET work flow to REJECTED

   UPDATE
      wf_wf
   SET
      wf_wf.wf_status_db_id = 0,
      wf_wf.wf_status_cd = 'REJECT'
   WHERE
      wf_wf.wf_db_id = t_wf_db_id AND
      wf_wf.wf_id    = t_wf_id;

   -----------------
   --Now look for Re-Approve
   -----------------

   IF aReApproveWfLevelDbId != -1 AND aReApproveWfLevelId != -1 THEN
      --Look for the work flow definition
      SELECT wf_wf.wf_defn_db_id, wf_wf.wf_defn_id,
             wf_wf.wf_priority_db_id, wf_wf.wf_priority_cd,
             wf_wf.wf_due_date
      INTO t_wf_defn_db_id, t_wf_defn_id,
           t_wf_priority_db_id, t_wf_priority_cd,
           t_wf_due_date
      FROM wf_wf
      WHERE
      wf_wf.wf_db_id = t_wf_db_id AND
      wf_wf.wf_id    = t_wf_id;



      createworkflow(aWfDefnDbId => t_wf_defn_db_id,
                     aWfDefnId => t_wf_defn_id,
                     aRefPriorityDbId => t_wf_priority_db_id,
                     aRefPriorityCd => t_wf_priority_cd,
                     aDueDate => t_wf_due_date,
                     aWfDbId => t_new_wf_db_id,
                     aWfId => t_new_wf_id,
                     aError => aError);

      -- Tie the new workflow to the task
      SELECT
         task_wf.task_db_id,
         task_wf.task_id
      INTO
         lTaskDbId,
         lTaskId
      FROM
         task_wf
      WHERE
         task_wf.wf_db_id = t_wf_db_id AND
         task_wf.wf_id    = t_wf_id;

      INSERT INTO task_wf (wf_db_id, wf_id, task_db_id, task_id)
      VALUES
      (  t_new_wf_db_id,
         t_new_wf_id,
         lTaskDbId,
         lTaskId
      );

      --IF no error then do the core re-approval
      IF aError = 0 THEN

         -- Find the step defn ID to re-approve
         -- First select statement is for steps with only one level assigned.
         SELECT
            wf_step_defn_to_reapprove.wf_defn_step_db_id,
            wf_step_defn_to_reapprove.wf_defn_step_id
         INTO
            t_wf_defn_step_db_id,
            t_wf_defn_step_id
         FROM
            (  SELECT
                  wf_step.wf_defn_step_db_id,
                  wf_step.wf_defn_step_id
               FROM
                  wf_steps
                  INNER JOIN wf_step   ON wf_step.wf_step_db_id   = wf_steps.wf_step_db_id   AND
                                          wf_step.wf_step_id      = wf_steps.wf_step_id
                  INNER JOIN wf_step_levels  ON wf_step_levels.wf_step_db_id  = wf_step.wf_step_db_id AND
                                                wf_step_levels.wf_step_id     = wf_step.wf_step_id
                  INNER JOIN wf_level  ON wf_level.wf_level_db_id = wf_step_levels.wf_level_db_id  AND
                                          wf_level.wf_level_id    = wf_step_levels.wf_level_id
               WHERE
                  wf_steps.wf_db_id = t_new_wf_db_id  AND
                  wf_steps.wf_id    = t_new_wf_id
                  AND
                  wf_level.wf_level_defn_db_id  = aReApproveWfLevelDbId AND
                  wf_level.wf_level_defn_id     = aReApproveWfLevelId
               UNION
               -- This second select statement is for steps with more than one level assigned - I.e. nested steps.
               SELECT
                  wf_step_to_reapprove.wf_defn_step_db_id,
                  wf_step_to_reapprove.wf_defn_step_id
               FROM
                  wf_steps
                  INNER JOIN wf_step   ON wf_step.wf_step_db_id   = wf_steps.wf_step_db_id   AND
                                          wf_step.wf_step_id      = wf_steps.wf_step_id
                  INNER JOIN wf_step_group   ON wf_step_group.wf_step_db_id   = wf_step.wf_step_db_id AND
                                                wf_step_group.wf_step_id      = wf_step.wf_step_id
                  INNER JOIN wf_step_levels  ON wf_step_levels.wf_step_db_id  = wf_step_group.child_wf_step_db_id AND
                                                wf_step_levels.wf_step_id     = wf_step_group.child_wf_step_id
                  INNER JOIN wf_level  ON wf_level.wf_level_db_id = wf_step_levels.wf_level_db_id  AND
                                          wf_level.wf_level_id    = wf_step_levels.wf_level_id
                  INNER JOIN wf_step wf_step_to_reapprove   ON wf_step_to_reapprove.wf_step_db_id  = wf_step_group.wf_step_db_id AND
                                                               wf_step_to_reapprove.wf_step_id     = wf_step_group.wf_step_id
               WHERE
                  wf_steps.wf_db_id = t_new_wf_db_id  AND
                  wf_steps.wf_id    = t_new_wf_id
                  AND
                  wf_level.wf_level_defn_db_id  = aReApproveWfLevelDbId AND
                  wf_level.wf_level_defn_id     = aReApproveWfLevelId
            ) wf_step_defn_to_reapprove;

         -- Find the step ID in the new workflow to re-approve using the step defn ID
         -- First select statement is for steps with only one level assigned.
         SELECT
            wf_step_to_reapprove.wf_step_db_id,
            wf_step_to_reapprove.wf_step_id
         INTO
            t_wf_step_db_id,
            t_wf_step_id
         FROM
            (  SELECT
                  wf_step.wf_step_db_id,
                  wf_step.wf_step_id
               FROM
                  wf_steps
                  INNER JOIN wf_step   ON wf_step.wf_step_db_id   = wf_steps.wf_step_db_id   AND
                                          wf_step.wf_step_id      = wf_steps.wf_step_id
               WHERE
                  wf_steps.wf_db_id = t_new_wf_db_id AND
                  wf_steps.wf_id    = t_new_wf_id
                  AND
                  wf_step.wf_defn_step_db_id = t_wf_defn_step_db_id AND
                  wf_step.wf_defn_step_id    = t_wf_defn_step_id
               UNION
               -- Second select statement is for steps with more than one level assigned.
               SELECT
                  child_step.wf_step_db_id,
                  child_step.wf_step_id
               FROM
                  wf_steps
                  INNER JOIN wf_step   ON wf_step.wf_step_db_id   = wf_steps.wf_step_db_id   AND
                                          wf_step.wf_step_id      = wf_steps.wf_step_id
                  INNER JOIN wf_step_group   ON wf_step_group.wf_step_db_id   = wf_step.wf_step_db_id AND
                                                wf_step_group.wf_step_id      = wf_step.wf_step_id
                  INNER JOIN wf_step child_step ON child_step.wf_step_db_id   = wf_step_group.child_wf_step_db_id AND
                                                   child_step.wf_step_id      = wf_step_group.child_wf_step_id
               WHERE
                  wf_steps.wf_db_id = t_new_wf_db_id AND
                  wf_steps.wf_id    = t_new_wf_id
                  AND
                  child_step.wf_defn_step_db_id = t_wf_defn_step_db_id  AND
                  child_step.wf_defn_step_id    = t_wf_defn_step_id
            ) wf_step_to_reapprove;

         -- UPDATE the current step to In Progress
         UPDATE
            wf_step
         SET
            wf_step.wf_step_status_db_id = 0,
            wf_step.wf_step_status_cd = 'INPROGRESS'
         WHERE
            ( wf_step.wf_step_db_id, wf_step.wf_step_id )
            IN
            (
              SELECT
                 wf_step.wf_step_db_id,
                 wf_step.wf_step_id
              FROM
                 wf_step
              WHERE
                  wf_step.wf_step_db_id = t_wf_step_db_id AND
                  wf_step.wf_step_id    = t_wf_step_id
              UNION

              SELECT
                 wf_step_group.child_wf_step_db_id,
                 wf_step_group.child_wf_step_id
              FROM
                 wf_step_group
              WHERE
                 wf_step_group.wf_step_db_id   = t_wf_step_db_id AND
                 wf_step_group.wf_step_id      = t_wf_step_id
            );

         --MOVE BACK and set them approved
         WHILE t_wf_step_db_id IS NOT NULL AND t_wf_step_id IS NOT NULL LOOP

               --Look for its previous step
               lPrevStepCt := 0;
               SELECT
                  COUNT(*)
               INTO
                  lPrevStepCt
               FROM
                  wf_step_flow
               WHERE
                  wf_step_flow.next_wf_step_db_id = t_wf_step_db_id AND
                  wf_step_flow.next_wf_step_id    = t_wf_step_id;

               -- If there are no more previous steps, we're done; exit.
               IF lPrevStepCt = 0 THEN
                  EXIT;
               END IF;

               SELECT
                  wf_step_flow.wf_step_db_id,
                  wf_step_flow.wf_step_id
               INTO
                  t_root_wf_step_db_id,
                  t_root_wf_step_id
               FROM
                  wf_step_flow
               WHERE
                  wf_step_flow.next_wf_step_db_id = t_wf_step_db_id AND
                  wf_step_flow.next_wf_step_id    = t_wf_step_id;

               --assign them back
               t_wf_step_db_id := t_root_wf_step_db_id;
               t_wf_step_id    := t_root_wf_step_id;

               --UPDATE the step to Approved
               UPDATE
                  wf_step
               SET
                  wf_step.wf_step_status_db_id = 0,
                  wf_step.wf_step_status_cd = 'APPROVED'
               WHERE
                  ( wf_step.wf_step_db_id, wf_step.wf_step_id )
                  IN
                  (  -- First select is for steps with only one level assigned.
                     SELECT
                        wf_step.wf_step_db_id,
                        wf_step.wf_step_id
                     FROM
                        wf_step
                     WHERE
                        wf_step.wf_step_db_id = t_wf_step_db_id AND
                        wf_step.wf_step_id    = t_wf_step_id
                     UNION
                     -- Second select is for steps with more than one level assigned.
                     SELECT
                        wf_step_group.child_wf_step_db_id,
                        wf_step_group.child_wf_step_id
                     FROM
                        wf_step_group
                     WHERE
                        wf_step_group.wf_step_db_id   = t_wf_step_db_id AND
                        wf_step_group.wf_step_id      = t_wf_step_id
                  );

               -- UPDATE the date and HR on the approved levels
               UPDATE
                  wf_level
               SET
                  wf_level.level_dt = SYSDATE,
                  wf_level.level_hr_db_id = aHrDbId,
                  wf_level.level_hr_id    = aHrId
               WHERE
                  ( wf_level.wf_level_db_id, wf_level.wf_level_id )
                  IN
                  (  -- First select is for steps with only one level assigned.
                     SELECT
                        wf_step_levels.wf_level_db_id,
                        wf_step_levels.wf_level_id
                     FROM
                        wf_step_levels
                     WHERE
                        wf_step_levels.wf_step_db_id  = t_wf_step_db_id AND
                        wf_step_levels.wf_step_id     = t_wf_step_id
                     UNION
                     -- Second select is for steps with more than one level assigned.
                     SELECT
                        wf_step_levels.wf_level_db_id,
                        wf_step_levels.wf_level_id
                     FROM
                        wf_step_levels
                        INNER JOIN wf_step_group   ON wf_step_group.child_wf_step_db_id   = wf_step_levels.wf_step_db_id   AND
                                                      wf_step_group.child_wf_step_id      = wf_step_levels.wf_step_id
                     WHERE
                        wf_step_group.wf_step_db_id = t_wf_step_db_id AND
                        wf_step_group.wf_step_id    = t_wf_step_id
                  );


         END LOOP;
      END IF;
   END IF;
EXCEPTION
WHEN NO_DATA_FOUND THEN
ROLLBACK;
aError:=1;

end RejectLevel;
/