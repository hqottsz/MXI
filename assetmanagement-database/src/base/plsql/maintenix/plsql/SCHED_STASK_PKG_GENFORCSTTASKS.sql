--liquibase formatted sql


--changeSet SCHED_STASK_PKG_GENFORCSTTASKS:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure SCHED_STASK_PKG_GENFORCSTTASKS(
               an_StartSchedDbId IN sched_stask.sched_db_id%TYPE,
               an_StartSchedId IN sched_stask.sched_db_id%TYPE,
               os_ExitCd OUT VARCHAR2,
               on_Return OUT NUMBER)
   IS
BEGIN

   SCHED_STASK_PKG.GENFORECASTEDTASKS(an_StartSchedDbId,
                                      an_StartSchedId,
                                      NULL, -- Use the default FORECAST range logic
                                      os_ExitCd,
                                      on_Return);

END SCHED_STASK_PKG_GENFORCSTTASKS;
/