--liquibase formatted sql


--changeSet IsTaskApplicable:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure ISTASKAPPLICABLE(
               an_InvNoDbId IN inv_inv.inv_no_db_id%TYPE,
               an_InvNoId IN inv_inv.inv_no_id%TYPE,
               an_TaskDbId IN task_task.task_db_id%TYPE,
               an_TaskId IN task_task.task_id%TYPE,
          an_Return OUT NUMBER)
   IS
BEGIN

  an_Return := SCHED_STASK_PKG.ISTASKAPPLICABLE(an_InvNoDbId, an_InvNoId,an_TaskDbId,an_TaskId);
END ISTASKAPPLICABLE;
/