--liquibase formatted sql


--changeSet PREP_DEADLINE_PKG_PREP_CA:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure PREP_DEADLINE_PKG_PREP_CA (
            an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
            an_SchedId          IN sched_stask.sched_id%TYPE,
            an_OrigTaskTaskDbId IN task_task.task_db_id%TYPE,
            an_OrigTaskTaskId   IN task_task.task_id%TYPE,
            an_DataTypeDbId     IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId       IN  evt_sched_dead.data_type_id%TYPE,
            anSyncWithBaseline  IN INTEGER,
            on_Return           OUT NUMBER
        ) IS



     BEGIN


       PREP_DEADLINE_PKG.preparecalendardeadline(
            an_SchedDbId,
            an_SchedId,
            an_OrigTaskTaskDbId,
            an_OrigTaskTaskId,
            an_DataTypeDbId,
            an_DataTypeId,
            sys.diutil.int_to_bool(anSyncWithBaseline),
            NULL,
            on_Return
   );

END PREP_DEADLINE_PKG_PREP_CA;
/