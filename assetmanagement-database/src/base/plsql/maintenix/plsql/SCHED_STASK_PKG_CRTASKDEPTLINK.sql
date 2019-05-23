--liquibase formatted sql


--changeSet SCHED_STASK_PKG_CRTASKDEPTLINK:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure SCHED_STASK_PKG_CRTASKDEPTLINK(
          an_PreviousSchedDbId     IN sched_stask.sched_db_id%TYPE,
          an_PreviousSchedId       IN sched_stask.sched_id%TYPE,
		  an_SchedDbId             IN sched_stask.sched_db_id%TYPE,
		  an_SchedId               IN sched_stask.sched_id%TYPE,
          on_Return                OUT NUMBER) is
begin
  SCHED_STASK_PKG.CreateTaskDependencyLink(an_PreviousSchedDbId, an_PreviousSchedId, an_SchedDbId, an_SchedId, on_Return);
end SCHED_STASK_PKG_CRTASKDEPTLINK;
/