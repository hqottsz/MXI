--liquibase formatted sql


--changeSet EVENT_PKG_UPDTDRVDEADLINE:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure EVENT_PKG_UPDTDRVDEADLINE(
          an_SchedStaskDbId          IN  sched_stask.sched_db_id%TYPE,
          an_SchedStaskId               IN  sched_stask.sched_id%TYPE,
          on_Return                        OUT NUMBER) is
begin
  EVENT_PKG.UpdateDrivingDeadline(an_SchedStaskDbId, an_SchedStaskId, on_Return);
end EVENT_PKG_UPDTDRVDEADLINE;
/