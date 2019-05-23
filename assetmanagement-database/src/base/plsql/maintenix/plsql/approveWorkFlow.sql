--liquibase formatted sql


--changeSet approveWorkFlow:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure approveWorkflow(
aWfStepDbId in number,
aWfStepId in number) is
begin
  UPDATE wf_wf SET wf_wf.wf_status_db_id = 0, wf_wf.wf_status_cd = 'COMPLETE'

WHERE EXISTS (
       SELECT 1
         FROM
              wf_defn,
              wf_step,
              wf_steps
        WHERE
             wf_step.wf_step_db_id = aWfStepDbId AND
             wf_step.wf_step_id    = aWfStepId
             AND
             wf_steps.wf_step_db_id = wf_step.wf_step_db_id AND
             wf_steps.wf_step_id    = wf_step.wf_step_id
             AND
             wf_steps.wf_db_id = wf_wf.wf_db_id AND
             wf_steps.wf_id    = wf_wf.wf_id
             AND
             wf_wf.wf_status_db_id = 0 AND
             wf_wf.wf_status_cd    = 'INPROGRESS'

             UNION
             SELECT 1
         FROM
              wf_defn,
              wf_steps,
              wf_step_group
        WHERE
             wf_step_group.child_wf_step_db_id = aWfStepDbId AND
             wf_step_group.child_wf_step_id    = aWfStepId
             AND
             wf_step_group.wf_step_db_id = wf_steps.wf_step_db_id AND
             wf_step_group.wf_step_id    = wf_steps.wf_step_id
             AND
             wf_steps.wf_db_id = wf_wf.wf_db_id AND
             wf_steps.wf_id    = wf_wf.wf_id
             AND
             wf_wf.wf_status_db_id = 0 AND
             wf_wf.wf_status_cd    = 'INPROGRESS'
);
UPDATE task_task
      SET
         approved_bool = 1
      WHERE
         ( task_task.task_db_id, task_task.task_id ) IN
         (	SELECT
               task_wf.task_db_id,
               task_wf.task_id
            FROM
               wf_steps
               INNER JOIN task_wf   ON task_wf.wf_db_id  = wf_steps.wf_db_id  AND
                                       task_wf.wf_id     = wf_steps.wf_id
            WHERE
               wf_steps.wf_step_db_id  = aWfStepDbId AND
               wf_steps.wf_step_id     = aWfStepId
         );
end approveWorkflow;
/