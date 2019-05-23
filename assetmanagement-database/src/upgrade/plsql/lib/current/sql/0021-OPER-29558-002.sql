--liquibase formatted sql

--changeset OPER-29558:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- Enable parallel DML on sched_step table
   UTL_PARALLEL_PKG.PARALLEL_UPDATE_BEGIN('sched_step');

   -- Migrate task step data from task_step table into sched_step table
   UPDATE   
    sched_step  sched_step_target 
    SET (
      sched_step_target.task_db_id,
      sched_step_target.task_id, 
      sched_step_target.task_step_id
    ) = (
      SELECT  
        task_step.task_db_id, 
        task_step.task_id, 
        task_step.step_id 
      FROM   
        sched_stask, task_step  
      WHERE
        task_step.task_db_id = sched_stask.task_db_id AND
        task_step.task_id    = sched_stask.task_id AND
        sched_stask.sched_db_id = sched_step_target.sched_db_id  AND
        sched_stask.sched_id = sched_step_target.sched_id  AND
        task_step.step_ord  = sched_step_target.step_ord 
    )  
    WHERE EXISTS ( 
      SELECT  
        1
    FROM 
      sched_stask, task_step 
    WHERE
      task_step.task_db_id = sched_stask.task_db_id AND
      task_step.task_id    = sched_stask.task_id AND
      sched_stask.sched_db_id = sched_step_target.sched_db_id  AND
      sched_stask.sched_id = sched_step_target.sched_id  AND
      task_step.step_ord  = sched_step_target.step_ord     
    );

   -- Disable parallel DML on sched_step table
   UTL_PARALLEL_PKG.PARALLEL_UPDATE_END('sched_step', FALSE);

END;
/
