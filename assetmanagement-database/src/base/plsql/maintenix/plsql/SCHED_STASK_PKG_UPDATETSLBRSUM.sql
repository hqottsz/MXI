--liquibase formatted sql


--changeSet SCHED_STASK_PKG_UPDATETSLBRSUM:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure SCHED_STASK_PKG_UPDATETSLBRSUM(
               an_Months IN NUMBER,
               on_Return OUT NUMBER)
   IS
BEGIN

  SCHED_STASK_PKG.UpdateTaskLabourSummary(an_Months, on_Return);
END SCHED_STASK_PKG_UPDATETSLBRSUM;
/