--liquibase formatted sql


--changeSet PREP_DEADLINE_PKG_PREP_DEAD:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure PREP_DEADLINE_PKG_PREP_DEAD (
            an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
            an_SchedId          IN sched_stask.sched_id%TYPE,
            an_OrigTaskTaskDbId IN task_task.task_db_id%TYPE,
            an_OrigTaskTaskId   IN task_task.task_id%TYPE,
            anSyncWithBaseline  IN INTEGER,
            on_Return           OUT NUMBER
        ) IS



     BEGIN


       PREP_DEADLINE_PKG.preparescheddeadlines(
            an_SchedDbId,
            an_SchedId,
            an_OrigTaskTaskDbId,
            an_OrigTaskTaskId,
            sys.diutil.int_to_bool(anSyncWithBaseline),
            NULL,
            on_Return
   );

END PREP_DEADLINE_PKG_PREP_DEAD;
/